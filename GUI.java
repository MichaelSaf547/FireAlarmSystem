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

    Gauge TempG = new Gauge();
    Gauge HumidG = new Gauge();
    VBox V = new VBox();
    Label L;
    HBox HGauge = new HBox();
    HBox BButtons = new HBox();
    Button Test = new Button("Test");
    Button Stop = new Button("Stop");
    Button Log = new Button("Log");

    @Override
    public void init() {
        L = new Label("FireAlarm Version 0.1");
        setTempGauge(TempG);
        setHumidGauge(HumidG);
        HGauge = new HBox(200, TempG, HumidG);
        HGauge.setAlignment(Pos.CENTER);
        BButtons = new HBox(100, Test, Stop, Log);
        BButtons.setAlignment(Pos.CENTER);
        L.setTextFill(Color.WHITE);
        L.setFont(Font.font(26));
        V.getChildren().add(BButtons);
        V.getChildren().add(L);
        Test.setId("Test");
        Stop.setId("Stop");
        Log.setId("Log");
    }

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {

        StackPane root = new StackPane();

        /*Set Scaling for The Buttons*/
        Test.setScaleX(1.5);
        Stop.setScaleX(1.5);
        Log.setScaleX(1.5);

        /*Chnging Color of the Buttons*/
        //Test.setStyle("-fx-background-color:grey; -fx-border-color: black;");
        Test.setTextFill(Color.WHITE);

        //Stop.setStyle("-fx-background-color:grey; -fx-border-color: black;");
        Stop.setTextFill(Color.WHITE);

        //Log.setStyle("-fx-background-color:grey; -fx-border-color: black;");
        Log.setTextFill(Color.WHITE);

        /*Pressing Escape will affect The Stop Button*/
        Stop.setCancelButton(true);

        /*Pressing Enter Will affect the Test Button*/
        Test.setDefaultButton(true);

        BorderPane Pane = new BorderPane();
        Image image = new Image(new FileInputStream("C:\\Users\\Mostafa\\OneDrive\\Desktop\\Java\\GUI\\src\\gui\\1.jpg"));
        ImageView imageView = new ImageView(image);
        Group root2 = new Group(imageView);

        imageView.setFitHeight(1000);
        imageView.setFitWidth(1500);
        Test.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                System.out.print("Testing");
            }
        });
        Stop.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                System.out.print("Stop");
            }
        });
        Pane.setBottom(V);
        Pane.setCenter(HGauge);
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

    public void setTempGauge(Gauge gauge) {
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

    public void setHumidGauge(Gauge gauge) {
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
