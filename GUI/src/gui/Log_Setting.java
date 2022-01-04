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

package gui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Log_Setting {
        
    Scene log_Setting_Scene;

    Label log_Label_Setting_Number_read;    // The line for number input 
    Label log_Label_Setting_Period_read;    // The line for period input
    Label log_Label_OK_1;
    Label log_Label_OK_2;
    Label log_Label_OK_3;
    Label log_Label_OK_4;

    TextField number;                       // For the number of reads
    TextField period;                       // For the period between reads
    Button Done;                            // To return to the Log Scene
    HBox number_HBox;                       // Line and textfield of number of reads
    HBox period_HBox;                       // Line and textfield of period between reads
    HBox button_HBox;                       // For the button
    VBox log_VBox;                          
    
    Group root3;                            // To handle the background image of LOG 
    
    // Constructor to initate the paramenters of the log setting scene
    public Log_Setting() throws FileNotFoundException
    {
        
        Font font = Font.font("Verdana", FontWeight.BOLD, 18);
        log_Label_Setting_Number_read = new Label("Enter the number of reads: ");
        log_Label_Setting_Number_read.setFont(font);
        log_Label_Setting_Period_read = new Label("Enter the time between each read(s): ");
        log_Label_Setting_Period_read.setFont(font);
        
        //to adjust the first line 
        log_Label_OK_1 = new Label("\n\n\n");
        
        //to adjust the distance between the two lines 
        log_Label_OK_2 = new Label("\n\n\n");
        
        //to adjust the distance between the second line and the button
        log_Label_OK_3 = new Label("\n\n\n");
        
        // to adjust the distance between the button and the end
        log_Label_OK_4 = new Label("\n\n");

        //Done button is used to return to the log scene
        Done = new Button("Done");
        Done.setDefaultButton(true);
        Done.setScaleX(1.25);
        //set the id to connect it with the CSS file
        Done.setId("log_Set_Done");
        
        
        number = new TextField();
        period = new TextField();
        
        log_VBox = new VBox();
        
        number_HBox = new HBox();
        number_HBox.setAlignment(Pos.CENTER);
        number_HBox.setSpacing(20);
        number_HBox.getChildren().addAll(log_Label_Setting_Number_read, number);

        period_HBox = new HBox();
        period_HBox.setAlignment(Pos.CENTER);
        period_HBox.setSpacing(20);
        period_HBox.getChildren().addAll(log_Label_Setting_Period_read, period);
        
        
        button_HBox = new HBox();
        button_HBox.setAlignment(Pos.CENTER);
        button_HBox.setSpacing(20);
        button_HBox.getChildren().addAll(Done);
        
        
        log_VBox.getChildren().add(log_Label_OK_1);
        log_VBox.getChildren().add(number_HBox);
        log_VBox.getChildren().add(log_Label_OK_2);
        log_VBox.getChildren().add(period_HBox);
        log_VBox.getChildren().add(log_Label_OK_3);
        log_VBox.getChildren().add(button_HBox);
        log_VBox.getChildren().add(log_Label_OK_4);
        

        // Read an image to be used as a background for Applicarion Scene
        Image image2 = new Image(new FileInputStream("2.jpg"));
        ImageView imageView2 = new ImageView(image2);
        root3 = new Group(imageView2);

        imageView2.setFitHeight(400);
        imageView2.setFitWidth(600);

    }

}
