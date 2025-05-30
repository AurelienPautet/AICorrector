package com.aurelien.pautet.net;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import com.github.kwhat.jnativehook.GlobalScreen;

public class App extends Application {

    CustomHotkeyListener customHotkeyListener;

    @Override
    public void start(Stage primaryStage) {

        TextSaveManager textSaveManager = new TextSaveManager();
        textSaveManager.createFile();
        textSaveManager.readFile();
        //textSaveManager.writeFile();
        //textSaveManager.readFile();

        
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/main.fxml"));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading FXML file: " + e.getMessage());
        }
        Scene scene = new Scene(root, Color.BLACK);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        Image icon = new Image(getClass().getResourceAsStream("/logo1.png"));
        primaryStage.getIcons().add(icon);
        primaryStage.setTitle("Ai Corrector");
        //primaryStage.setWidth(420);
        //primaryStage.setHeight(600);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);

        primaryStage.setOnCloseRequest(event -> {
           // event.consume(); 
        });

        primaryStage.show();
    }

    @Override
    public void stop() {
        try {
            GlobalScreen.unregisterNativeHook();
        } catch (Exception e) {
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}