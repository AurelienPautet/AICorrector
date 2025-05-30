package com.aurelien.pautet.net;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import io.github.cdimascio.dotenv.Dotenv;
import java.awt.event.KeyEvent;

import java.awt.*;



import javafx.application.Platform;
import javafx.concurrent.Task;

public class GeminiCorrector {
    private static ClipboardManager clipboardManager = new ClipboardManager();
    private static boolean debug = true; // Set to true for debugging

    private static MainController mainControl;

    private static TextSaveManager textSaveManager = new TextSaveManager();

    static String apiKey;
    static {
        String keyFromEnv = null;
        try {
            keyFromEnv = Dotenv.load().get("GOOGLE_API_KEY");
        } catch (Exception e) {
            // Dotenv failed, will try system env
        }
        if (keyFromEnv != null && !keyFromEnv.isEmpty()) {
            apiKey = keyFromEnv;
        } else {
            apiKey = System.getenv("GOOGLE_API_KEY");
        }
    }



    public static void main(String[] args) {
        
        System.out.println("Loaded API key: " + apiKey);
    }

    public GeminiCorrector(MainController mainController) {
        this.mainControl = mainController;
        System.out.println("GeminiCorrector initialized with API key: " + apiKey);
    }

    public static String copyCorrectPaste(){
        try {
                Robot robot = new Robot();
                Thread.sleep(300); 

                String cliboardContent = clipboardManager.getClipBoard();
                Boolean NewCliboard = true;

                robot.keyPress(KeyEvent.VK_CONTROL);
                robot.keyPress(KeyEvent.VK_C);
                robot.keyRelease(KeyEvent.VK_C);
                robot.keyRelease(KeyEvent.VK_CONTROL);

                Thread.sleep(300); 

                String copiedText = clipboardManager.getClipBoard();
                if (copiedText.isEmpty()) {
                    copiedText = cliboardContent;
                    NewCliboard = false;
                }

                System.out.println("Copied text: " + copiedText);
                String directive = TextSaveManager.textMap.get(mainControl.getSelectedPrompt());
                
                String correctedText = correctText(copiedText, directive);

                clipboardManager.setClipBoard(correctedText);

                if (NewCliboard){
                    Thread.sleep(300); 
                
                    robot.keyPress(KeyEvent.VK_CONTROL);
                    robot.keyPress(KeyEvent.VK_V);
                    robot.keyRelease(KeyEvent.VK_V);
                    robot.keyRelease(KeyEvent.VK_CONTROL);

                    System.out.println("Pasted text.");
                }
                return correctedText;


            } catch (Exception ex) {
                ex.printStackTrace();
                return "Error during copy/correct/paste: " + ex.getMessage();

            }
    }

    public static void launchCorrector(){
        Task<String> correctionTask = new Task<>() {
            @Override
            protected String call() {
                Platform.runLater(() -> mainControl.changeStatusLabel("Busy"));
                return GeminiCorrector.copyCorrectPaste();
            }

            @Override
            protected void succeeded() {
                String correctedText = getValue();
                System.out.println("Corrected text: " + correctedText);
                Platform.runLater(() -> mainControl.changeStatusLabel("Ready"));
            }

            @Override
            protected void failed() {
                Platform.runLater(() -> mainControl.changeStatusLabel("Error"));
            }
        };
        new Thread(correctionTask).start();
    }

    public static String correctText(String text,String directive) {
        try (Client client = Client.builder().apiKey(apiKey).build())
        {
            String prompt = "Je vais te donner un texte: \n" +
                    directive + "\n" +
                    "Ne modifie pas les noms propres \n" +
                    "TU NE RESUMERAS RIEN, gardes le texte dans sont intégralité\n" +
                    "Ta réponse sera UNIQUEMENT le texte d'entrée corrigé\n" +
                    "Texte: \n" + text;
            System.out.println("Prompt: " + prompt);
            GenerateContentResponse response =
                client.models.generateContent(
                    "gemini-2.5-flash-preview-05-20",
                    prompt,
                    null);

            System.out.println("Response: " + response.text());
            clipboardManager.setClipBoard(response.text()); // Assuming clipboardManager is defined somewhere in your code

            return response.text();
        }
        catch (Exception e) {
            e.printStackTrace();
            return "Error during text correction: " + e.getMessage();
        }   
    }
}
