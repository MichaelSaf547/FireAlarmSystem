/*
 * ITI INTAKE 42 EMBEDDED SYSTEM 
 * JAVA COURSE 
 * GROUP 8
 * STUDENTS ARE 1-Michael Safwat Sobhy Nakhla --> "LEADER"
 *              2-Abdelrahman Mahmoud Mohamed Saleh
 *              3-Abdelrahman Omar Mohamed Shafik
 *              4-Mostafa Hani Imam
 *              5-Mohamed Maged Abdrabuh 
 */
package server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;

import java.net.Socket;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import static server.Server.chosenPort;

public class CleintsHandler extends Thread {

    DataInputStream dis;
    PrintStream ps;
    static Vector<CleintsHandler> clients = new Vector<>();
    int counter_to_exit=0;


    /*start the to way communication between the clients with the server and the server with the Arduino*/
    public CleintsHandler(Socket s) {
        try {
            dis = new DataInputStream(s.getInputStream());
            ps = new PrintStream(s.getOutputStream());
            clients.add(this);

            /*send the com port to the user*/
            ps.println(Server.com);

            start();

            /*this thread is to send the reading to all clients*/
            new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                    }
                    int counter;
                    counter=0;
                    for (CleintsHandler ch : clients) {
                        System.out.println("Send readings to client ("+counter+ ") " +"temp= "+ Server.temper + " humid= " + Server.humid);
                        counter++;
                        if (Server.chosenPort.openPort()) {
                            ch.ps.println(Server.temper);
                            ch.ps.println(Server.humid);
                        } else {
                            /*handeling if the arduino is not connected*/
                            Server.open_Port = false;
                            ch.ps.println(2);
                            ch.ps.println(2);
                           // System.out.println("disconected from the Arduino for " + counter_to_exit + " sec");
                            for (counter_to_exit = 1; counter_to_exit < 6; counter_to_exit++) {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(CleintsHandler.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                //System.out.println("disconected from the Arduino for " + counter_to_exit + " sec");
                            }
                            Platform.exit();
                            System.exit(0);
                        }
                    }
                }
            }).start();
        } catch (IOException ex) {
            Logger.getLogger(CleintsHandler.class.getName()).log(Level.SEVERE, null, ex);
            try {
                dis.close();
                ps.close();
                s.close();
            } catch (IOException ex1) {
                Logger.getLogger(CleintsHandler.class.getName()).log(Level.SEVERE, null, ex1);
            }

        }
    }

    /*Send from the server to the Ardunio using SerialPort*/
    public void send_com(String msg) {

        Thread thread;
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                }

                Server.output.println(msg);
                Server.output.flush();
            }
        };
        thread.start();
    }

    /*this thread is to recieve the ordars from the clients and sent to the Arduino*/
    @Override
    public void run() {
        while (true) {
            try {
                /*recieve from the clients*/
                String str = dis.readLine();

                /*send to the Arduino*/
                send_com(str);

            } catch (IOException ex) {
                try {
                    dis.close();
                    break;
                } catch (IOException ex1) {
                    Logger.getLogger(CleintsHandler.class.getName()).log(Level.SEVERE, null, ex1);
                }
                Logger.getLogger(CleintsHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
