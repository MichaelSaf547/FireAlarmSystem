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

import com.fazecast.jSerialComm.SerialPort;
import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.skins.FlatSkin;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class GUI extends Application {

    // The whole comming are for the application Scene
    Scene app_Scene;            // Application Scene
    Gauge tempG;                // Gauge for Temperature
    Gauge humidG;               // Gauge for Humidity
    VBox vbox;                  // To handle the Application Scene
    Label label = new Label(" Ardunio Nano()"); // Label for the name of the Arduino name and port 
    String com;
    HBox hGauge;                // For Humidity Gauge 
    HBox bButtons;              // For the four buttons in the Application Scene

    StackPane root;             // for the background image in the application scene
    StackPane root4;            // for the background image in the log scene
    StackPane root6;            // for the background image in the log setting scene

    //The four buttons in the Applicatioin Scene
    Button test;                // To test the whole System     
    Button stop;                // To stop the led and buzzer after a high temperature is detected 
    Button log;                 // To open the Log Scene and see the Log data
    Button start;               // To start the system again after the stop is pressed
    Button exit;                // To exit the system again after the exit is pressed

    Group root2;                // To handle the background image

    // Used serial port connection
    static SerialPort chosenPort;

    int alertFlag = 0;                      // Used as a flag to check if the temperature is higher than 26 C
    
    Log log_Object;                         // Object from the log class
    Log_Setting log_Setting;                // Object from the log_Setting class

    // Communication with the Server
    Socket s;                               // Client Socket
    DataInputStream dis;                    // Client Data Input Stream
    PrintStream ps;                         // Client Print Stream
    
    Integer temper = 0;
    Integer humid = 0;

    private int counter_Read = 0;           // every 15 input will log one data
    private int counter_Log = 0;            // to check how many data has benn logged

    private LocalTime currentTime;
    int counter_to_exit;                    // used to count the number of trying to connect with the server
    
    // To get the Arduino Port
    public void get_com_port() {
        /*get the name of the port that the aruino is connect to*/
        com = "";
        try {
            com = dis.readLine();
        } catch (IOException ex) {
            System.out.println("Error reading Port name");
        }
        label.setText(" Ardunio Nano(" + com + ")");
    }

    /* Trying to connect to the the Server
        After Connection, initiating the Client Gui variables
    */
    @Override
    public void init() throws FileNotFoundException {

        /*creat socket for this client to start the conction to the server */
        try {
            s = new Socket("127.0.0.1", 5005);
            dis = new DataInputStream(s.getInputStream());
            ps = new PrintStream(s.getOutputStream());
        } catch (IOException ex) {
            try {
                for (counter_to_exit = 0; counter_to_exit < 6; counter_to_exit++) {
                    Thread.sleep(1000);
                    Platform.runLater(() -> {
                        System.out.println("The server is dowen for " + counter_to_exit + " sec");
                    });
                }
            } catch (Exception e) {
            }
            Platform.exit();
            System.exit(0);
        }

        root = new StackPane();
        root4 = new StackPane();
        root6 = new StackPane();
        
        //Get the name of com port
        get_com_port();
        
        // The Gauges for the Application Scene
        tempG = new Gauge();                // Gauge for Temperature
        humidG = new Gauge();               // Gauge for Humidity
        
        vbox = new VBox();
        hGauge = new HBox();
        bButtons = new HBox();
        
        // The 5 buttions of the Application Scene
        test = new Button("Test");
        stop = new Button("Stop");
        log = new Button("Log");
        start = new Button("Start");
        
        exit = new Button("Exit");
        exit.setCancelButton(true);

        settempGauge(tempG);                                            // Set Temperature Gauge
        sethumidGauge(humidG);                                          // Set Humidity Gauge
        hGauge = new HBox(200, tempG, humidG);                          // Add gauges to Hbox
        hGauge.setAlignment(Pos.CENTER);                                // Center the gauge
        bButtons = new HBox(100, start, test, stop, log, exit);         // Add five buttons to Hbox
        bButtons.setAlignment(Pos.CENTER);                              // Center the buttons
        label.setTextFill(Color.WHITE);                                 // Make the text white 
        label.setFont(Font.font(26));                                   // Make the label font 26 
        vbox.getChildren().add(bButtons);                               // Add buttons to Vbox
        vbox.getChildren().add(label);                                  // Add label to Vbox
        
        //Set the IDs for the 5 buttons for the CSS file  
        test.setId("test");                                             // Setid for the CSS file
        stop.setId("stop");                                             // Setid for the CSS file
        log.setId("log");                                               // Setid for the CSS file
        start.setId("start");                                           // Setid for the CSS file
        exit.setId("exit");

        log_Object = new Log();
        log_Setting = new Log_Setting();
    }
    
    /* Add the data To the vectors of data
    after a specified period in second 
    the period is mutliplied by 2 because we read each 0.5 second 
    and we have a specified maximum of saved reads
    */
    public void add_To_Log() {
        /*assign the values into the vector */
        counter_Read++;
        if (counter_Read == 1) {
            log_Object.data_T.add(temper);
            log_Object.data_H.add(humid);
            currentTime = LocalTime.now();
            log_Object.time.add(currentTime.getHour() + ":" + currentTime.getMinute() + ":"
                    + currentTime.getSecond());
            counter_Log++;

        } else if (counter_Read >= log_Object.TIME_BET_READ * 2) {
            counter_Read = 0;
        }
        while (counter_Log > log_Object.MAX_READ) {
            log_Object.data_T.remove(0);
            log_Object.data_H.remove(0);
            log_Object.time.remove(0);

            counter_Log--;
        }
    }
    /* 
    Receive from the server and checks if it is open or not
    */
    public void recieveFromServer() {
        /*Creating thread to receive the temperature and humidty readings form the server*/
        new Thread(() -> {
            while (true) {
                String str;
                try {
                    /*the first line is the temp reading */
                    str = dis.readLine();

                    /*assign value into the integer temper */
                    temper = new Integer(str);

                    /*the scound line is the humidty reading*/
                    str = dis.readLine();

                    /*assign value into the integer humid*/
                    humid = new Integer(str);

                    /*set the value of the Gauge*/
                    tempG.setValue(temper);
                    humidG.setValue(humid);
                    counter_to_exit = 0;
                    if (temper == 2 && humid == 2) {
                        try {
                            for (counter_to_exit = 0; counter_to_exit < 6; counter_to_exit++) {
                                Thread.sleep(1000);
                                Platform.runLater(() -> {
                                    label.setText("disconected from the Arduino for " + counter_to_exit + " sec");
                                });
                            }
                        } catch (Exception e) {
                        }
                        Platform.runLater(() -> {
                            Platform.exit();
                            System.exit(0);
                        });
                    }

                    add_To_Log();

                } catch (IOException ex) {
                    /*handeling if server is dowen*/
                    try {
                        for (counter_to_exit = 0; counter_to_exit < 6; counter_to_exit++) {
                            Thread.sleep(1000);
                            Platform.runLater(() -> {
                                label.setText("disconected from the server for " + counter_to_exit + " sec");
                            });
                        }
                    } catch (Exception e) {
                    }
                    Platform.runLater(() -> {
                        Platform.exit();
                        System.exit(0);
                    });

                }
            }
        }).start();
    }

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {


        /* Set Scaling for The Buttons */
        test.setScaleX(1.25);
        stop.setScaleX(1.25);
        log.setScaleX(1.25);
        start.setScaleX(1.25);
        exit.setScaleX(1.25);
        /* Chnging Color of the Buttons */
        // test.setStyle("-fx-background-color:grey; -fx-border-color: black;");
        test.setTextFill(Color.WHITE);

        // stop.setStyle("-fx-background-color:grey; -fx-border-color: black;");
        stop.setTextFill(Color.WHITE);

        // log.setStyle("-fx-background-color:grey; -fx-border-color: black;");
        log.setTextFill(Color.WHITE);

        /* Pressing Escape will affect The stop Button */
        stop.setCancelButton(true);

        /* Pressing Enter Will affect the test Button */
        test.setDefaultButton(true);

        BorderPane Pane = new BorderPane();

        // Read an image to be used as a background for Applicarion Scene
        Image image = new Image(new FileInputStream("1.jpg"));
        ImageView imageView = new ImageView(image);
        root2 = new Group(imageView);

        imageView.setFitHeight(1000);
        imageView.setFitWidth(1500);

        // The handling of Test key: just send "1" to Arduino to make it start led and buzzer
        test.setOnAction(e -> {
            ps.println("1");
        });

        // The handling of Stop key: just send "2" to Arduino to make it stop led and buzzer
        stop.setOnAction(e -> {
            ps.println("2");
        });

        // The handling of Start key: just send "3" to Arduino to make it start its funtion again
        start.setOnAction(e -> {
            ps.println("3");
            alertMethod();
        });
        // The handling of Exit key: just to close the Application
        exit.setOnAction(e -> {
            Platform.exit();
            primaryStage.close();
            System.exit(0);
        });
        // The handling of Log key: Change the Scene from Applocation to Log
        // And add the data to the areas
        log.setOnAction(e -> {
            primaryStage.setScene(log_Object.log_Scene);
            primaryStage.centerOnScreen();
            log_Object.set_Log_Counter(counter_Log);
            log_Object.print_Log_Data();
        });
        // The handling of Log key: Change the Scene from Log to Applocation
        log_Object.log_Ok.setOnAction(e -> {
            primaryStage.setScene(app_Scene);
            primaryStage.centerOnScreen();
        });
        /* Handeling the stage close key on top right */

        log_Object.log_Setting.setOnAction(e -> {
            primaryStage.setScene(log_Setting.log_Setting_Scene);
            primaryStage.centerOnScreen();

        });

        log_Setting.Done.setOnAction(e -> {
            
            if("".equals(log_Setting.number.getText()))
            {
                //Do Nothing
            }
            else 
            {
                log_Object.MAX_READ = Integer.parseInt(log_Setting.number.getText());
            }
            
            if("".equals(log_Setting.number.getText()))
            {
                //Do Nothing
            }
            else 
            {
                log_Object.TIME_BET_READ = Integer.parseInt(log_Setting.period.getText());
            }
            
            primaryStage.setScene(log_Object.log_Scene);
            primaryStage.centerOnScreen();

        });

        // force the field to be numeric only
        log_Setting.number.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                log_Setting.number.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        log_Setting.period.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                log_Setting.number.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        primaryStage.setOnCloseRequest((WindowEvent event) -> {
            Platform.exit();
            primaryStage.close();
            System.exit(0);
        });

        /*call the recieve method that recieve the readings form the server*/
        recieveFromServer();
        
        /*call the alert Method after recieving the reading from the server */
        alertMethod();

        Pane.setBottom(vbox);
        Pane.setCenter(hGauge);

        root.getChildren().add(root2);
        root.getChildren().add(Pane);

        root4.getChildren().add(log_Object.root3);
        root4.getChildren().add(log_Object.log_Pane);

        root6.getChildren().add(log_Setting.root3);
        root6.getChildren().add(log_Setting.log_VBox);

        app_Scene = new Scene(root, 1500, 600, Color.WHITE);
        log_Object.log_Scene = new Scene(root4, 600, 600, Color.WHITE);
        app_Scene.getStylesheets().add(getClass().getResource("CSS.css").toString());
        log_Object.log_Scene.getStylesheets().add(getClass().getResource("CSS.css").toString());

        log_Setting.log_Setting_Scene = new Scene(root6, 600, 400, Color.WHITE);
        log_Setting.log_Setting_Scene.getStylesheets().add(getClass().getResource("CSS.css").toString());

        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.setTitle("FireAlarm");
        primaryStage.setScene(app_Scene);
        primaryStage.show();

    }

    // Set the Temperature gauge data
    public void settempGauge(Gauge gauge) {
        gauge.setSkin(new FlatSkin(gauge));
        gauge.setDecimals(0);
        gauge.setValueColor(Color.WHITE);
        gauge.setTitleColor(Color.WHITE);
        gauge.setSubTitleColor(Color.WHITE);
        gauge.setScaleX(1);
        gauge.setScaleY(1);
        gauge.setGradientBarEnabled(true);
        gauge.setTitle("Temprature");
        gauge.setSubTitle("°C");
        gauge.setUnitColor(Color.WHITE);
        gauge.setUnit("°C");
        gauge.setValue(0);
    }

    // Set the Humidity gauge data
    public void sethumidGauge(Gauge gauge) {
        gauge.setSkin(new FlatSkin(gauge));
        gauge.setDecimals(0);
        gauge.setValueColor(Color.WHITE);
        gauge.setTitleColor(Color.WHITE);
        gauge.setSubTitleColor(Color.WHITE);
        gauge.setScaleX(1);
        gauge.setScaleY(1);
        gauge.setGradientBarEnabled(true);
        gauge.setTitle("Humidity");
        gauge.setSubTitle("%rh");
        gauge.setUnitColor(Color.WHITE);
        gauge.setUnit("%rh");
        // gauge.setBarColor(Color.BLUE);
        gauge.setValue(0);
    }

    // Alert Thread appears when Temp >= 26
    public void alertMethod() {
        new Thread(() -> {
            while (alertFlag == 0) {

                //System.out.println("----------"+temper);
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                }
                if (temper >= 26) {

                    alertFlag = 1;
                }
            }
            if (alertFlag == 1) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("Temprature Warning !");
                    alert.setContentText("Temprature is above or equal 26 °C");
                    alert.show();
                });
                alertFlag = 0;
            }
        }).start();
    }

}
