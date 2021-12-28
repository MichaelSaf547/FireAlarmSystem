/*
 * ITI INTAKE 42 EMBEDDED SYSTEM 
 * JAVA COURSE 
 * GROUP 8
 * STUDENTS ARE 1-Michael Safwat Sobhy Nakhla --> "LEADER"
 *              2-Abdelrahman Mahmoud Mohamed Saleh
 *              3-Abdelrahman Omar Mohamed Shafik
 *              4-Mostafa Hani Imam
 *              5-mohamed maged abdrabuh 
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
    /*Start communication between Ardunio and the server using SerialPort*/
    static public String init_com() {
        Vector<String> portList = new Vector<String>();
        SerialPort[] portNames = SerialPort.getCommPorts();
        String comPort = "";
        for (int i = 0; i < portNames.length; i++) {
            portList.add(portNames[i].getSystemPortName());
            System.out.println(i + "- " + portNames[i].getSystemPortName());
            comPort = portNames[i].getSystemPortName();
        }
        chosenPort = SerialPort.getCommPort(comPort);
        chosenPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
        output = new PrintWriter(chosenPort.getOutputStream());
        input = new Scanner(chosenPort.getInputStream());
        chosenPort.openPort();
        return comPort;
    }

    /*Recieve from Ardunio using SerialPort*/
    static public void recieve_com() {
        Thread thread;
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }

                while (input.hasNextLine()) {
                    try {
                        temper = Integer.parseInt(input.nextLine());
                        humid = Integer.parseInt(input.nextLine());
                        System.out.println(temper + " " + humid);
                    } catch (Exception e) {
                    }
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
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        /*call the init_com to start the connection betwen the server and the Arduino*/
        com=init_com();
        /*call the recieve_com to recieve the reading from the the server*/
        recieve_com();
        /*call the constuctor of the server*/
        Server server = new Server();

    }

}
