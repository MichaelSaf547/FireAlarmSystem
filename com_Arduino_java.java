/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package project;

import com.fazecast.jSerialComm.SerialPort;

import java.io.PrintWriter;

import java.util.Scanner;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javax.swing.JComboBox;



/**
 *
 * @author abdelrahman
 */
public class Project extends Application {
    static SerialPort chosenPort;
    int flag=0;
    @Override
    public void start(Stage primaryStage) {
        // populate the drop-down box
                JComboBox<String> portList = new JComboBox<String>();

		SerialPort[] portNames = SerialPort.getCommPorts();
		for(int i = 0; i < portNames.length; i++)
                {
			portList.addItem(portNames[i].getSystemPortName());
                        System.out.println(i+"- "+portNames[i].getSystemPortName());
                }
                chosenPort = SerialPort.getCommPort(portList.getSelectedItem().toString());
                chosenPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
                
		PrintWriter output = new PrintWriter(chosenPort.getOutputStream());
                Scanner data = new Scanner(chosenPort.getInputStream());
                
                    
                
                System.out.println(chosenPort.openPort());
		if(chosenPort.openPort()) 
                {
                    System.out.println(chosenPort.openPort());
                }
		                 
                
                
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                
                Thread thread = new Thread(){
                    @Override public void run() {
                        try {Thread.sleep(100); } catch(Exception e) {} 
                        output.println("1");
                        output.flush();
                        Scanner data = new Scanner(chosenPort.getInputStream());    
                        while(data.hasNextLine()) {
                            int number = 0;
                            try{number = Integer.parseInt(data.nextLine());}catch(Exception e){}
                            System.out.println(number);
                        }
                    }
                
            };
	    thread.start();
                    
            }
        });
        
        StackPane root = new StackPane();
        root.getChildren().add(btn);
        
        Scene scene = new Scene(root, 300, 250);
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
