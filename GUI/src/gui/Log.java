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


package gui;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Window;


public class Log {
    
    int MAX_READ = 10;                      // The maximum number of reads to be on the log 
    int TIME_BET_READ = 5;                  // The period between eah read and the next in seconds 
    
    int counter_Log = 0;                    // the number of data in the data vectors to be shown 
    
    Scene log_Scene;

    Label log_Label_OK_1;                   // Space before the buttons
    Label log_Label_OK_2;                   // Space after the buttons
    Label log_Label_DataT;                  // For Temperature data    
    Label log_Label_DataH;                  // For Humidity data
    Label log_Label_Time;                   // For Time data
    
    Button log_Ok;                          // To return to the Application Scene
    Button log_Setting;                     // to adjust number of reads and the period between every read
    Button log_Save;                        // to save as Temperature and Humidity
    
    HBox log_HBox;                          // To handle the labels 
    HBox log_Data_HBox;                     // To handle the Data Areas 
    HBox log_Buttons;                       // To handle three buttons
    
    VBox log_VBox;                          // To handle the Log Scene
    
    TextArea log_AreaT;                     // For Temperature data
    TextArea log_AreaH;                     // For Humidity data
    TextArea log_AreaTD;                    // For Time data

    BorderPane log_Pane;
    
    Group root3;                            // To handle the background image of LOG    
   
    public Vector<Integer> data_H;
    public Vector<Integer> data_T;
    public Vector<String> time;
    private Window primaryStage;
    
    public Log() throws FileNotFoundException
    {
        data_H = new Vector<>();
        data_T = new Vector<>();
        time = new Vector<>();
        
        log_Pane = new BorderPane();
        
        //The whole comming are for the log
        Font font = Font.font("Verdana", FontWeight.BOLD, 18);
        log_Label_OK_1 = new Label("");
        log_Label_OK_2 = new Label("");
        
        log_Label_DataT = new Label("Temperature");
        log_Label_DataT.setFont(font);
        
        log_Label_DataH = new Label("Humidity");
        log_Label_DataH.setFont(font);
        
        log_Label_Time = new Label("Time");
        log_Label_Time.setFont(font);
        
        log_Ok = new Button("Ok");
        log_Ok.setDefaultButton(true);
        log_Ok.setScaleX(1.25);
        log_Ok.setId("log_Ok");
        
        log_Setting = new Button("Setting");
        log_Setting.setDefaultButton(true);
        log_Setting.setScaleX(1.25);
        log_Setting.setId("log_Sett");
        
        log_Save = new Button("Save");
        log_Save.setDefaultButton(true);
        log_Save.setScaleX(1.25);
        log_Save.setId("log_Save");
        
        
        log_HBox = new HBox(log_Label_Time, log_Label_DataT, log_Label_DataH); // add labels
        log_HBox.setAlignment(Pos.CENTER);
        log_HBox.setSpacing(100);
        log_VBox = new VBox();

        // Adjust Text area for Temperature 
        log_AreaT = new TextArea();
        log_AreaT.setPrefHeight(400);
        log_AreaT.setPrefWidth(300);
        log_AreaT.setEditable(false);

        // Adjust Text area for Humidity
        log_AreaH = new TextArea();
        log_AreaH.setPrefHeight(400);
        log_AreaH.setPrefWidth(300);
        log_AreaH.setEditable(false);

        // Adjust Text area for Time 
        log_AreaTD = new TextArea();
        log_AreaTD.setPrefHeight(400);
        log_AreaTD.setPrefWidth(300);
        log_AreaTD.setEditable(false);
        
        log_Buttons = new HBox();
        log_Buttons.getChildren().addAll(log_Ok,log_Save,log_Setting);
        log_Buttons.setAlignment(Pos.CENTER);
        log_Buttons.setSpacing(100);
        
        log_Data_HBox = new HBox();

        log_VBox.getChildren().addAll(log_Label_OK_1, log_Buttons, log_Label_OK_2);

        log_Data_HBox.getChildren().addAll(log_AreaTD, log_AreaT, log_AreaH); // add areas
        log_Data_HBox.setAlignment(Pos.CENTER);

        //log_VBox.getChildren().addAll(log_HBox, log_Data_HBox, log_Ok); // add all parts in log Scene
        HBox log_Ok_Hbx = new HBox();
        log_Ok_Hbx.getChildren().add(log_VBox);
        log_Ok_Hbx.setAlignment(Pos.CENTER);
        log_Pane.setTop(log_HBox);
        log_Pane.setCenter(log_Data_HBox);
        log_Pane.setBottom(log_Ok_Hbx);
        
        FileChooser fileChooser1 = new FileChooser();//Save Part
        fileChooser1.setTitle("Save");
        fileChooser1.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"));
        log_Save.setOnAction((ActionEvent event) -> {
          try {
              try (FileWriter wr = new FileWriter(fileChooser1.showSaveDialog(primaryStage)); BufferedWriter buffer = new BufferedWriter(wr)) {
                  buffer.write("Time\n\n"+log_AreaTD.getText() +"\n"+"Temperature \n\n"+ log_AreaT.getText() +"\n"+"Humidity\n\n"+ log_AreaH.getText());
              }
          } catch (IOException ex) {
              Logger.getLogger(Log.class.getName()).log(Level.SEVERE, null, ex);
          }
    });
        
        // Read an image to be used as a background for Applicarion Scene
        Image image2 = new Image(new FileInputStream("2.jpg"));
        ImageView imageView2 = new ImageView(image2);
        root3 = new Group(imageView2);

        imageView2.setFitHeight(600);
        imageView2.setFitWidth(600);

    }
    
    //Set the counter_log to know how many reads we have
    public void set_Log_Counter(int log_Counter)
    {
        counter_Log = log_Counter;
    }
    
    // Print the Temp, Hum, and Time to the areas 
    public void print_Log_Data() {
         
        int i;
        log_AreaT.clear();
        log_AreaH.clear();
        log_AreaTD.clear();
        for (i = 0; i < counter_Log; i++) {
            log_AreaTD.appendText(time.get(i) + "\n");
            log_AreaT.appendText(data_T.get(i) + "\n");
            log_AreaH.appendText(data_H.get(i) + "\n");
        }
    }
    
}
