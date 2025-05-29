package com.aurelien.pautet.net;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import io.github.cdimascio.dotenv.Dotenv;
import java.awt.event.KeyEvent;

import java.awt.*;
import java.awt.datatransfer.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.checkerframework.checker.units.qual.t;

public class GeminiCorrector {
    private static ClipboardManager clipboardManager = new ClipboardManager();
    private static boolean debug = true; // Set to true for debugging

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
        System.out.println(correctText("Ceci est un test de correction de texte. Il y a des fautews dans ce texte, comme par exemple l'orthographe de 'test' et 'fautes'."));
    }

    public GeminiCorrector() {
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

                String correctedText = correctText(copiedText);

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

    public static String correctText(String text) {
        try (Client client = Client.builder().apiKey(apiKey).build())
        {
            String prompt = "Je vais te donner un texte: \n" +
                    "Corrige toute les fautes, SANS REFORMULER sauf si la phrases est gramaticalement fausse \n" +
                    "Ne modifie pas les noms propres \n" +
                    "TU NE RESUMERAS RIEN, gardes le texte dans sont intégralité\n" +
                    "Ta réponse sera UNIQUEMENT le texte d'entrée corrigé\n" +
                    "Texte: \n" + text;

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
