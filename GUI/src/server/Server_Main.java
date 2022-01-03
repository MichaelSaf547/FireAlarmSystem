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

import static server.Server.com;
import static server.Server.init_com;
import static server.Server.recieve_com;


public class Server_Main {
    
    public static void main(String[] args) {
        /*call the init_com to start the connection betwen the server and the Arduino*/
        com = init_com();
        /*call the recieve_com to recieve the reading from the the server*/
        recieve_com();
        /*call the constuctor of the server*/
        Server server = new Server();

    }
}
