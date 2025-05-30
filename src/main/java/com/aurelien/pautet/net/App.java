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
    private java.awt.TrayIcon trayIcon;

    @Override
    public void start(Stage primaryStage) {
        //ta grand mère la ligne de code, pourqoi t'est true de base toi
        javafx.application.Platform.setImplicitExit(false); 

        TextSaveManager textSaveManager = new TextSaveManager();
        textSaveManager.createFile();
        if (TextSaveManager.textMap.isEmpty()) {
            textSaveManager.addText("Correction sans reformulation", "Corrige toute les fautes, SANS REFORMULER sauf si la phrases est gramaticalement fausse\r\n" + //
                                                                        "");
            textSaveManager.addText("Correction avec reformulation", "Corrige toute les fautes, et reformule légèrement le texte pour améliorer la construction des phrases (sans pour autant le résumer).");
        }
        textSaveManager.writeFile();
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
            event.consume();
            try {
                if (java.awt.SystemTray.isSupported()) {
                    java.awt.SystemTray tray = java.awt.SystemTray.getSystemTray();
                    if (trayIcon == null) {
                        java.awt.Image image = java.awt.Toolkit.getDefaultToolkit().getImage(getClass().getResource("/logo1.png"));
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