package com.aurelien.pautet.net;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

public class SettingsController {

    private Stage primaryStage;
    private Scene scene;
    private Parent root = null;

    @FXML
    Label NameLabel;

    public void displayName(String name) {
        // This method can be used to display the name in the label
        NameLabel.setText("Name: " + name);
    }

    
    public void switchToMainScene(ActionEvent event) {
        System.out.println("Switching to main scene...");
        try {
            root = FXMLLoader.load(getClass().getResource("/main.fxml"));
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
