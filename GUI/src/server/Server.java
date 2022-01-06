/*
 * ITI INTAKE 42 EMBEDDED SYSTEM 
 * JAVA COURSE 
 * GROUP 8
 * STUDENTS Names:
                1-Michael Safwat Sobhy Nakhla --> "LEADER"
 *              2-Abdelrahman Mahmoud Mohamed Saleh
 *              3-Abdelrahman Omar Mohamed Shafik
 *              4-Mostafa Hani Imam
 *              5-Mohamed Maged Abdrabuh 
 */
package server;

import com.fazecast.jSerialComm.SerialPort;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.Scene;

/*
*1-This class represent the server that recives reading sensor from the Arduino and bradcasting it to all clients
*2-recievs ordars from the clients and sends it to the Arduino
 */
public class Server {

    ServerSocket server;
    Socket s;
    static SerialPort chosenPort;
    static PrintWriter output;
    static Scanner input;
    int alertFlag = 0;
    static int temper;
    static int humid;
    static String com;
    Scene scene;
    Scene log_Scene;
    static Boolean open_Port = false;
    static String comPort = "";
    static int counter_to_exit;


    /*Start communication between Ardunio and the server using SerialPort*/
    static public String init_com() {

        while (open_Port == false) {
            System.out.println("try to conncet to arduino");

            Vector<String> portList = new Vector<>();
            SerialPort[] portNames = SerialPort.getCommPorts();

            for (int i = 0; i < portNames.length; i++) {
                portList.add(portNames[i].getSystemPortName());
                System.out.println(i + "- " + portNames[i].getSystemPortName());
                comPort = portNames[i].getSystemPortName();
            }
            chosenPort = SerialPort.getCommPort(comPort);
            chosenPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
            output = new PrintWriter(chosenPort.getOutputStream());
            input = new Scanner(chosenPort.getInputStream());

            open_Port = chosenPort.openPort();
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        }

        return comPort;
    }

    /*Recieve from Ardunio using SerialPort*/
    static public void recieve_com() {
        Thread thread;
        thread = new Thread() {
            @Override
            public void run() {
                int temp_humd_flag = 0;
                int count = 0, temp_temper = 0, temp_humid = 0;

                while (count < 5) {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                    }

                    if (chosenPort.openPort()) {
                        if (input.hasNextLine()) {
                            String input1 = input.nextLine();

                            String pattern = "-?\\d+";
                            if (input1.matches("-?\\d+")) { // any positive or negetive integer or not!
                                temp_temper = Integer.parseInt(input1);
                            }

                        }

                        if (input.hasNextLine()) {
                            try {
                                temp_humid = Integer.parseInt(input.nextLine());
                            } catch (NumberFormatException e) {

                                // Print the message if exception occured
                                System.out.println("NumberFormatException occured from arduino");
                            }
                        }
                        count++;
                        System.out.println(count + ":-" + temp_temper + " " + temp_humid);
                    } else {
                        System.out.println("disconected from the Arduino for " + counter_to_exit + " sec");
                        for (counter_to_exit = 1; counter_to_exit < 6; counter_to_exit++) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(CleintsHandler.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            System.out.println("disconected from the Arduino for " + counter_to_exit + " sec");

                        }
                        Platform.exit();
                        System.exit(0);
                    }
                }

                if (temp_temper > temp_humid) {
                    temp_humd_flag = 1;
                    System.out.println("-------the reading is flipped----------" + temp_temper + " " + temp_humid);
                }

                while (input.hasNextLine()) {
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                    }

                    if (temp_humd_flag == 1) {
                        humid = Integer.parseInt(input.nextLine());
                        temper = Integer.parseInt(input.nextLine());
                    } else {
                        temper = Integer.parseInt(input.nextLine());
                        humid = Integer.parseInt(input.nextLine());
                    }
                    System.out.println("temp= " + temper + " humid= " + humid + " " + open_Port);

                }
                open_Port = chosenPort.openPort();
                if (open_Port == false) {
                    System.out.println("disconected from the Arduino for " + counter_to_exit + " sec");
                    for (counter_to_exit = 1; counter_to_exit < 6; counter_to_exit++) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(CleintsHandler.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        System.out.println("disconected from the Arduino for " + counter_to_exit + " sec");
                    }
                    Platform.exit();
                    System.exit(0);
                }

            }
        };
        thread.start();
    }

    /*start the server*/
    public Server() {
        try {
            /*Creat the server socket->5005 */
            server = new ServerSocket(5005);
            while (true) {
                /*waiting for clients to join the server*/
                s = server.accept();
                /*add new client to the class CleintsHandler*/
                new CleintsHandler(s);
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

}
