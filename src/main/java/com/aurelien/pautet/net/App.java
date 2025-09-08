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

    private java.awt.TrayIcon trayIcon;
        private Stage primaryStage; 
    private Image defaultIcon;
    private Image busyIcon;
    private Image errorIcon;


    public void setAppIconBusy(String state) {
        javafx.application.Platform.runLater(() -> {
            if (state.equals("busy")) {
                primaryStage.getIcons().set(0, busyIcon);
                if (trayIcon != null) {
                    java.awt.Image awtBusyIcon = java.awt.Toolkit.getDefaultToolkit().getImage(getClass().getResource("/logo_busy.png"));
                    trayIcon.setImage(awtBusyIcon);
                }
            } else if(state.equals("default")) {
                primaryStage.getIcons().set(0, defaultIcon);
                if (trayIcon != null) {
                    java.awt.Image awtDefaultIcon = java.awt.Toolkit.getDefaultToolkit().getImage(getClass().getResource("/logo.png"));
                    trayIcon.setImage(awtDefaultIcon);
                }
            }else if (state.equals("error")) {
                primaryStage.getIcons().set(0, errorIcon);
                if (trayIcon != null) {
                    java.awt.Image awtErrorIcon = java.awt.Toolkit.getDefaultToolkit().getImage(getClass().getResource("/logo_error.png"));
                    trayIcon.setImage(awtErrorIcon);
                }
            }
        });
    }

    @Override
    public void start(Stage primaryStage) {
        //ta grand mère la ligne de code, pourqoi t'est true de base toi
        CustomHotkeyListener customHotkeyListener = new CustomHotkeyListener();

        javafx.application.Platform.setImplicitExit(false); 
        TextSaveManager textSaveManager = new TextSaveManager();
        textSaveManager.createFile();
        textSaveManager.readFile();
        if (TextSaveManager.textMap.isEmpty()) {
            textSaveManager.addText("Correction sans reformulation", "Corrige toute les fautes, SANS REFORMULER sauf si la phrases est gramaticalement fausse, et sans résumer le texte.");
            textSaveManager.addText("Correction avec reformulation", "Corrige toute les fautes, et reformule légèrement le texte pour améliorer la construction des phrases (sans pour autant le résumer).");
            textSaveManager.addText("Mail", "Transfrome le texte en un mail professionnel, en corrigeant les fautes et en reformulant légèrement le texte pour améliorer la construction des phrases (sans pour autant le résumer). Ajoute les formules de politesse appropriées en début et fin de mail sachant que mon nom (celui de l'expediteur) est PAUTET Aurélien.");
        }
        textSaveManager.writeFile();
        textSaveManager.readFile();

        //textSaveManager.writeFile();
        //textSaveManager.readFile();

        
        Parent root = null;
        MainController mainController = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
            root = loader.load();
            mainController = loader.getController();
            mainController.setAppInstance(this);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading FXML file: " + e.getMessage());
        }
        Scene scene = new Scene(root, Color.BLACK);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
 this.primaryStage = primaryStage; 
        defaultIcon = new Image(getClass().getResourceAsStream("/logo.png"));
        busyIcon = new Image(getClass().getResourceAsStream("/logo_busy.png"));
        errorIcon = new Image(getClass().getResourceAsStream("/logo_error.png"));
        primaryStage.getIcons().add(defaultIcon);
        primaryStage.setTitle("Ai Corrector");
        //primaryStage.setWidth(420);
        //primaryStage.setHeight(600);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);

        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            try {
                if (java.awt.SystemTray.isSupported()) {
                    java.awt.SystemTray tray = java.awt.SystemTray.getSystemTray();
                    if (trayIcon == null) {
                        java.awt.Image image = java.awt.Toolkit.getDefaultToolkit().getImage(getClass().getResource("/logo.png"));
                        trayIcon = new java.awt.TrayIcon(image, "Ai Corrector");
                        trayIcon.setImageAutoSize(true);
                        trayIcon.setPopupMenu(new java.awt.PopupMenu()); 
                        trayIcon.addMouseListener(new java.awt.event.MouseAdapter() {
                            @Override
                            public void mouseClicked(java.awt.event.MouseEvent e) {
                                if (e.getButton() == java.awt.event.MouseEvent.BUTTON1 && e.getClickCount() == 1) {
                                    System.out.println("Tray icon left-clicked, restoring application.");
                                    javafx.application.Platform.runLater(() -> {
                                        primaryStage.show();
                                        primaryStage.toFront();
                                    });
                                    tray.remove(trayIcon);
                                    trayIcon = null;
                                }
                            }
                        });
                        tray.add(trayIcon);
                    }
                    javafx.application.Platform.runLater(() -> primaryStage.hide());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
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