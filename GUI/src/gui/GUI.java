/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package gui;

import com.fazecast.jSerialComm.SerialPort;
import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.skins.FlatSkin;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Time;
import java.time.LocalTime;
import java.util.Optional;
import java.util.Scanner;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GUI extends Application {

    Gauge tempG = new Gauge();
    Gauge humidG = new Gauge();
    VBox vbox = new VBox();
    Label label;
    Label com_label;
    HBox hGauge = new HBox();
    HBox bButtons = new HBox();
    Button test = new Button("Test");
    Button stop = new Button("Stop");
    Button log = new Button("Log");
    Button start = new Button("Start");
    static SerialPort chosenPort;
    PrintWriter output;
    Scanner input;
    int alertFlag = 0;
    int temper;
    int humid;
    Scene scene;
    Scene log_Scene;
    Vector<Integer> data_H = new Vector<>();
    Vector<Integer> data_T = new Vector<>();
    Vector<String> time = new Vector<>();

    int counter_Read = 0; // every 15 input will log one data
    int counter_Log = 0; // to check how many data has benn logged

    Label log_Label_DataT;
    Label log_Label_DataH;
    Label log_Label_Time;
    Button log_Ok;
    HBox log_HBox;
    HBox log_Data_HBox;
    VBox log_VBox;
    TextArea log_AreaT;
    TextArea log_AreaH;
    TextArea log_AreaTD;
    Group root2;
    LocalTime currentTime;

    // Start communication between Ardunio and Java using SerialPort
    public String init_com() {
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

    // Send to Ardunio using SerialPort
    public void send(String msg) {

        Thread thread;
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
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
                } catch (Exception e) {
                }

                while (input.hasNextLine()) {
                    try {
                        humid = Integer.parseInt(input.nextLine());
                        temper = Integer.parseInt(input.nextLine());
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
                    } catch (Exception e) {
                    }
                }
            }
        };
        thread.start();
    }

    @Override
    public void init() {
        // label = new Label("FireAlarm Version 0.1");
        label = new Label(" Ardunio Uno(" + init_com() + ")");
        settempGauge(tempG);
        sethumidGauge(humidG);
        hGauge = new HBox(200, tempG, humidG);
        hGauge.setAlignment(Pos.CENTER);
        bButtons = new HBox(100, start, test, stop, log);
        bButtons.setAlignment(Pos.CENTER);
        label.setTextFill(Color.WHITE);
        label.setFont(Font.font(26));
        vbox.getChildren().add(bButtons);
        vbox.getChildren().add(label);
        test.setId("test");
        stop.setId("stop");
        log.setId("log");
        start.setId("start");

        Font font = Font.font("Verdana", FontWeight.BOLD, 18);

        log_Label_DataT = new Label("          Temperature      ");
        log_Label_DataT.setFont(font);
        log_Label_DataH = new Label("         Humidity");
        log_Label_DataH.setFont(font);
        log_Label_Time = new Label("           Time         ");
        log_Label_Time.setFont(font);
        log_Ok = new Button("Ok");
        log_HBox = new HBox(log_Label_Time, log_Label_DataT, log_Label_DataH);

        log_VBox = new VBox();

        log_AreaT = new TextArea();
        log_AreaT.setPrefHeight(400);
        log_AreaT.setPrefWidth(300);
        log_AreaT.setEditable(false);

        log_AreaH = new TextArea();
        log_AreaH.setPrefHeight(400);
        log_AreaH.setPrefWidth(300);
        log_AreaH.setEditable(false);

        log_AreaTD = new TextArea();
        log_AreaTD.setPrefHeight(400);
        log_AreaTD.setPrefWidth(300);
        log_AreaTD.setEditable(false);

        log_Data_HBox = new HBox();

        log_Data_HBox.getChildren().addAll(log_AreaTD, log_AreaT, log_AreaH);

        log_VBox.getChildren().addAll(log_HBox, log_Data_HBox, log_Ok);
        currentTime = LocalTime.now();

    }

    ////////////////////////////
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

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        alertMethod();
        StackPane root = new StackPane();

        /* Set Scaling for The Buttons */
        test.setScaleX(1.25);
        stop.setScaleX(1.25);
        log.setScaleX(1.25);
        start.setScaleX(1.25);

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
        Image image = new Image(new FileInputStream("1.jpg"));
        ImageView imageView = new ImageView(image);
        root2 = new Group(imageView);

        imageView.setFitHeight(1000);
        imageView.setFitWidth(1500);

        test.setOnAction(e -> {
            send("1");
        });

        stop.setOnAction(e -> {
            send("2");
        });

        start.setOnAction(e -> {
            send("3");
            alertMethod();
        });

        log.setOnAction(e -> {
            primaryStage.setScene(log_Scene);
            print_Log_Data();
        });

        log_Ok.setOnAction(e -> {
            primaryStage.setScene(scene);
        });

        recieve();

        new Thread(() -> {
            while (true) {
                tempG.setValue(temper);
                humidG.setValue(humid);
            }
        }).start();

        

        Pane.setBottom(vbox);
        Pane.setCenter(hGauge);
        root.getChildren().add(root2);
        root.getChildren().add(Pane);

        scene = new Scene(root, 1500, 600, Color.WHITE);
        log_Scene = new Scene(log_VBox, 600, 600);
        scene.getStylesheets().add(getClass().getResource("CSS.css").toString());

        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("FireAlarm");
        primaryStage.setScene(scene);
        primaryStage.show();
        /**
         * if(true) { Alert alert = new Alert(AlertType.WARNING);
         * alert.setTitle("Temprature Warning !");
         * alert.setContentText("Temprature is above 50 °C");
         * alert.showAndWait(); alert.set }
         *
         */
    }

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

    public void alertMethod() {
        new Thread(() -> {
            while (alertFlag == 0) {
                System.out.println("---------");
                if (temper >= 26) {
                    alertFlag = 1;
                }
            }
            if (alertFlag == 1) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Alert alert = new Alert(AlertType.WARNING);
                        alert.setTitle("Temprature Warning !");
                        alert.setContentText("Temprature is above 26 °C");
                        alert.show();
                    }
                });
            }
        }).start();
    }

    // public void CurrentTem
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}