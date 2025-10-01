package com.aurelien.pautet.net;

import javafx.scene.control.ChoiceBox;
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

public class MainController {
    private Stage primaryStage;
    private Scene scene;
    private Parent root = null;
    private static Scene mainSceneCache = null; // Add this
    private static MainController mainControllerInstance = null;

    GeminiCorrector geminiCorrector;
    @FXML
    private Label StatusLabel;

    @FXML
    private ChoiceBox<String> PromptChoiceBox;

    private App appInstance;

    public void setAppInstance(App appInstance) {
        this.appInstance = appInstance;
        geminiCorrector = new GeminiCorrector(this, appInstance);
    }

    public static void setMainSceneCache(Scene scene) {
        mainSceneCache = scene;
    }

    public static Scene getMainSceneCache() {
        return mainSceneCache;
    }

    public static void setMainControllerInstance(MainController controller) {
        mainControllerInstance = controller;
    }

    public static MainController getMainControllerInstance() {
        return mainControllerInstance;
    }

    public void changeStatusLabel(String text) {
        StatusLabel.setText("Status : " + text);
        System.out.println("Status changed to: " + text);
    }

    public String getSelectedPrompt() {
        String selectedPrompt = PromptChoiceBox.getSelectionModel().getSelectedItem();
        if (selectedPrompt == null) {
            System.out.println("No prompt selected.");
            return "";
        }
        System.out.println("Selected prompt: " + selectedPrompt);
        return selectedPrompt;
    }

    public void CorrectText(ActionEvent e) {
        System.out.println("Correct button clicked!");
        GeminiCorrector.launchCorrector();
    }

    public void addOptions() {
        PromptChoiceBox.getItems().clear();
        java.util.List<String> keys = new java.util.ArrayList<>(TextSaveManager.textMap.keySet());
        java.util.Collections.reverse(keys);
        PromptChoiceBox.getItems().addAll(keys);
        PromptChoiceBox.getSelectionModel().clearSelection();
        if (!keys.isEmpty()) {
            PromptChoiceBox.getSelectionModel().select(0);
        }
    }

    @FXML
    public void initialize() {
        setMainControllerInstance(this);
        addOptions();
    }

    /*
     * public void Send(ActionEvent e){
     * System.out.println("Send button clicked!");
     * String name = NameTextArea.getText();
     * System.out.println("Name entered: " + name);
     * 
     * FXMLLoader loader = new FXMLLoader(getClass().getResource("/settings.fxml"));
     * try {
     * root = loader.load();
     * SettingsController settingsController = loader.getController();
     * settingsController.displayName(name);
     * primaryStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
     * scene = new Scene(root);
     * scene.getStylesheets().add(getClass().getResource("/style.css").
     * toExternalForm());
     * primaryStage.setScene(scene);
     * primaryStage.show();
     * } catch (Exception ex) {
     * ex.printStackTrace();
     * System.out.println("Error loading settings.fxml: " + ex.getMessage());
     * return; // Exit if there's an error
     * } }
     */

    public void switchToSettingsScene(MouseEvent event) {
        System.out.println("Switching to settings scene...");
        try {
            if (mainSceneCache == null) {
                mainSceneCache = ((Node) event.getSource()).getScene();
            }

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
