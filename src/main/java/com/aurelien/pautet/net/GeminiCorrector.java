package com.aurelien.pautet.net;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import io.github.cdimascio.dotenv.Dotenv;

public class GeminiCorrector {
    static Dotenv dotenv = Dotenv.load();
    static String apiKey = dotenv.get("GOOGLE_API_KEY");
    public static void main(String[] args) {

        System.out.println("Loaded API key: " + apiKey); // Add this line
        System.out.println(correctText("Ceci est un test de correction de texte. Il y a des fautews dans ce texte, comme par exemple l'orthographe de 'test' et 'fautes'."));
      }
    
    public static String correctText(String text) {
        try (Client client = Client.builder().apiKey(apiKey).build())
        {
            String prompt = "Je vais te donner un texte: \n" +
                    "Corrige toute les fautes, SANS REFORMULER sauf si la phrases est gramaticalement fausse \n" +
                    "Ta réponse sera UNIQUEMENT le texte d'entrée corriger\n" +
                    "Texte: \n" + text;

            GenerateContentResponse response =
                client.models.generateContent(
                    "gemini-2.5-flash-preview-05-20",
                    prompt,
                    null);

            return response.text();
        }
        catch (Exception e) {
            e.printStackTrace();
            return "Error during text correction: " + e.getMessage();
        }   
    }
}
