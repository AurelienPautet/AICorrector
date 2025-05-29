package com.aurelien.pautet.net;
import java.io.*;
import javax.swing.*;
/**
 * Hello world!
 */
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Scene scene = new Scene(root,Color.WHITESMOKE);
        Image icon = new Image(getClass().getResourceAsStream("/logo1.png"));
        primaryStage.getIcons().add(icon);



        primaryStage.setTitle("My first JavaFX app");
        primaryStage.setWidth(420);
        primaryStage.setHeight(600);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}