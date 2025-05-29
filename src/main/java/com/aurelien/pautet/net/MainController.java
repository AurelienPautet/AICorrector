package com.aurelien.pautet.net;

import javafx.scene.control.Label;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.concurrent.Task;
import javafx.application.Platform;


public class MainController {
    private Stage primaryStage;
    private Scene scene;
    private Parent root = null;

    private ClipboardManager clipboardManager = new ClipboardManager();

    @FXML
    private Label StatusLabel; 
    
    public void CorrectText(ActionEvent e) {
        StatusLabel.setText("Status : Busy");
        System.out.println("Correct button clicked!");


        Task<String> correctionTask = new Task<>() {
            @Override
            protected String call() {
                return GeminiCorrector.copyCorrectPaste();
            }

            @Override
            protected void succeeded() {
                String correctedText = getValue();
                System.out.println("Corrected text: " + correctedText);
                Platform.runLater(() -> StatusLabel.setText("Status : Ready"));
            }

            @Override
            protected void failed() {
                Platform.runLater(() -> StatusLabel.setText("Status : Error"));
            }
        };

        new Thread(correctionTask).start();
    }


/*     public void Send(ActionEvent e){
        System.out.println("Send button clicked!");
        String name = NameTextArea.getText();
        System.out.println("Name entered: " + name);
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/settings.fxml"));
        try {
            root = loader.load();
            SettingsController settingsController = loader.getController();
            settingsController.displayName(name);
            primaryStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading settings.fxml: " + ex.getMessage());
            return; // Exit if there's an error
        }     }*/
    
        // Here you can add the logic to handle the send action


public void switchToSettingsScene(MouseEvent event) {
    System.out.println("Switching to settings scene...");
    try {
        root = FXMLLoader.load(getClass().getResource("/settings.fxml"));
        primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
}
}
