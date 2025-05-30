package com.aurelien.pautet.net;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

public class SettingsController {

    private Stage primaryStage;
    private Scene scene;
    private Parent root = null;

    
    public void switchToMainScene(MouseEvent event) {
        System.out.println("Switching to main scene...");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
            root = loader.load();
            MainController mainController = loader.getController();
            if (mainController != null) {
                mainController.addOptions(); // Refresh options only, not initialize()
            }
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
