/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package gui;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.skins.FlatSkin;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
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

/**
 *
 * @author Mostafa
 */
public class GUI extends Application {

    Gauge tempG = new Gauge();
    Gauge humidG = new Gauge();
    VBox vbox = new VBox();
    Label label;
    HBox hGauge = new HBox();
    HBox bButtons = new HBox();
    Button test = new Button("test");
    Button stop = new Button("stop");
    Button log = new Button("log");

    @Override
    public void init() {
        label = new Label("FireAlarm Version 0.1");
        settempGauge(tempG);
        sethumidGauge(humidG);
        hGauge = new HBox(200, tempG, humidG);
        hGauge.setAlignment(Pos.CENTER);
        bButtons = new HBox(100, test, stop, log);
        bButtons.setAlignment(Pos.CENTER);
        label.setTextFill(Color.WHITE);
        label.setFont(Font.font(26));
        vbox.getChildren().add(bButtons);
        vbox.getChildren().add(label);
        test.setId("test");
        stop.setId("stop");
        log.setId("log");
    }

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {

        StackPane root = new StackPane();

        /*Set Scaling for The Buttons*/
        test.setScaleX(1.5);
        stop.setScaleX(1.5);
        log.setScaleX(1.5);

        /*Chnging Color of the Buttons*/
        //test.setStyle("-fx-background-color:grey; -fx-border-color: black;");
        test.setTextFill(Color.WHITE);

        //stop.setStyle("-fx-background-color:grey; -fx-border-color: black;");
        stop.setTextFill(Color.WHITE);

        //log.setStyle("-fx-background-color:grey; -fx-border-color: black;");
        log.setTextFill(Color.WHITE);

        /*Pressing Escape will affect The stop Button*/
        stop.setCancelButton(true);

        /*Pressing Enter Will affect the test Button*/
        test.setDefaultButton(true);

        BorderPane Pane = new BorderPane();
        Image image = new Image(new FileInputStream("C:\\Users\\abdelrahman\\Documents\\NetBeansProjects\\proGit\\1.jpg"));
        ImageView imageView = new ImageView(image);
        Group root2 = new Group(imageView);

        imageView.setFitHeight(1000);
        imageView.setFitWidth(1500);
        test.setOnAction(e -> {
            System.out.print("testing");
        });
        stop.setOnAction(e -> {
            System.out.print("stop");
        });
        
        Pane.setBottom(vbox);
        Pane.setCenter(hGauge);
        root.getChildren().add(root2);
        root.getChildren().add(Pane);

        Scene scene = new Scene(root, 1500, 600, Color.WHITE);

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
        //gauge.setBarColor(Color.BLUE);
        gauge.setValue(0);
    }

    //public void CurrentTem
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
