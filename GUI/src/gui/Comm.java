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
package gui;

import com.fazecast.jSerialComm.SerialPort;
import static gui.GUI.chosenPort;
import java.io.PrintWriter;
import java.time.LocalTime;
import java.util.Scanner;
import java.util.Vector;  


public class Comm {
    
    private PrintWriter output;
    private Scanner input;
    
    public int temper;
    public int humid;
    
    private int counter_Read = 0; // every 15 input will log one data
    private int counter_Log = 0; // to check how many data has benn logged

    private LocalTime currentTime;
    
    
    public Vector<Integer> data_H = new Vector<>();
    public Vector<Integer> data_T = new Vector<>();
    public Vector<String> time = new Vector<>();
    
    public String comPort = "";
    
    // Start communication between Ardunio and Java using SerialPort
    public Comm() {
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
        chosenPort.openPort();
    }

    
    // Send to Ardunio using SerialPort
    public void send(String msg) {

        Thread thread;
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
                output.println(msg);
                output.flush();
            }
        };
        thread.start();
    }

    // Recieve from Ardunio using SerialPort
    public void recieve() {
        Thread thread;
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }

                while (input.hasNextLine()) {
                    try {
                        temper = Integer.parseInt(input.nextLine());
                        humid = Integer.parseInt(input.nextLine());
                        counter_Read++;
                        if (counter_Read == 1) {
                            data_T.add(temper);
                            data_H.add(humid);
                            currentTime = LocalTime.now();
                            time.add(currentTime.getHour() + ":" + currentTime.getMinute() + ":"
                                    + currentTime.getSecond());
                            counter_Log++;
                        } else if (counter_Read == 15) {
                            counter_Read = 0;
                        }
                        if (counter_Log == 10) {
                            data_T.remove(0);
                            data_H.remove(0);
                            time.remove(0);

                            counter_Log--;
                        }
                        System.out.println(temper + " " + humid);
                    } catch (NumberFormatException e) {
                    }
                }
            }
        };
        thread.start();
    }

    public int get_Counter_Log()
    {
        return counter_Log;
    }
}
