package com.aurelien.pautet.net;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.Parent;
import javafx.scene.Node;

import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.control.TextField;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SettingsController {

    private Stage primaryStage;
    private Scene scene;
    private Parent root = null;

    TextSaveManager textSaveManager = new TextSaveManager();

    @FXML
    private ScrollPane PromptsScrollPane;

    @FXML
    private VBox PromptsVBox;

    @FXML
    private TextField ModelTextField;

    @FXML
    private TextArea MasterPromptTextArea;

    private void create_prompt_card(String promptName, String promptText) {
        Pane cardPane = new Pane();
        cardPane.setMinHeight(50);
        cardPane.setPrefWidth(950);

        HBox hbox = new HBox(10);
        hbox.setPadding(new Insets(10, 10, 10, 10));
        hbox.setPrefWidth(950);

        TextField leftField = new TextField();
        leftField.setPrefWidth(250);
        leftField.setText(promptName);
        Label colonLabel = new Label(":");

        TextField rightField = new TextField();
        rightField.setPrefWidth(650);
        rightField.setText(promptText);

        Image binImage = new Image(getClass().getResourceAsStream("/BIN.png"));
        ImageView binView = new ImageView(binImage);
        binView.setFitWidth(24);
        binView.setFitHeight(24);
        binView.setPreserveRatio(true);
        Button binButton = new Button();
        binButton.setGraphic(binView);
        binButton.setOnAction(e -> {
            PromptsVBox.getChildren().remove(cardPane);
        });
        HBox.setMargin(binButton, new Insets(0, 0, 0, 10));
        hbox.getChildren().addAll(leftField, colonLabel, rightField, binButton);
        cardPane.getChildren().add(hbox);
        if (PromptsVBox != null) {
            PromptsVBox.getChildren().add(cardPane);
        }
    }

    public void add_new_card_button() {
        Button addButton = new Button("Add New Prompt");
        addButton.setPrefWidth(250);
        HBox buttonBox = new HBox(addButton);
        buttonBox.setPadding(new Insets(10, 10, 10, 10));
        addButton.setOnAction(event -> {
            PromptsVBox.getChildren().remove(buttonBox);
            create_prompt_card("New Prompt", "Enter your prompt here...");
            PromptsVBox.getChildren().add(buttonBox);
        });
        if (PromptsVBox != null) {
            PromptsVBox.getChildren().add(buttonBox);
        }
    }

    public void savePrompts(MouseEvent event) {
        textSaveManager.ereaseText();
        for (Node node : PromptsVBox.getChildren()) {
            Pane cardPane = (Pane) node;
            if (cardPane.getChildren().get(0) instanceof HBox) {
                HBox hbox = (HBox) cardPane.getChildren().get(0);
                TextField leftField = (TextField) hbox.getChildren().get(0);
                TextField rightField = (TextField) hbox.getChildren().get(2);
                String key = leftField.getText();
                String value = rightField.getText();
                textSaveManager.addText(key, value);
            }

        }
        TextSaveManager.updateMasterPrompt(MasterPromptTextArea.getText());
        textSaveManager.updateModelName(ModelTextField.getText());
        textSaveManager.readFile();
        switchToMainScene(event);
    }

    public void switchToMainScene(MouseEvent event) {
        System.out.println("Switching to main scene...");
        try {
            primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene mainScene = MainController.getMainSceneCache();
            MainController controller = MainController.getMainControllerInstance();
            controller.addOptions();
            primaryStage.setScene(mainScene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        ModelTextField.setText(TextSaveManager.ModelName);
        MasterPromptTextArea.setText(TextSaveManager.MasterPrompt);
        for (String key : TextSaveManager.textMap.keySet()) {
            String promptText = TextSaveManager.textMap.get(key);
            create_prompt_card(key, promptText);
        }
        add_new_card_button();
    }

}
