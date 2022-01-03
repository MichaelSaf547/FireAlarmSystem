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

public class CleintsHandler extends Thread {

    DataInputStream dis;
    PrintStream ps;
    static Vector<CleintsHandler> clients = new Vector<>();

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

                    for (CleintsHandler ch : clients) {
                        System.out.println("ClientHandeler" + Server.temper + " " + Server.humid);
                        ch.ps.println(Server.temper);
                        ch.ps.println(Server.humid);
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
