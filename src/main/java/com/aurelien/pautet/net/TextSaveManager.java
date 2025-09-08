package com.aurelien.pautet.net;

import java.io.File;  
import java.io.FileWriter;   
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TextSaveManager {

  public static Map<String, String> textMap = new HashMap<>();
  public static String ModelName = "";
  private static final String FILE_PATH = System.getProperty("user.home") + "/Documents/AICorrectorSave/save.txt";
  public static void main(String[] args) {
    TextSaveManager manager = new TextSaveManager();
    manager.createFile();
    manager.readFile();
    manager.writeFile();
    manager.readFile();
  }

  public void readFile() {
    try {
      File myObj = new File(FILE_PATH);
      Scanner myReader = new Scanner(myObj);
      ArrayList<String> Keys = new ArrayList<>();
      ArrayList<String> Values = new ArrayList<>();
      Integer i = 0;
      
      while (myReader.hasNextLine()) {
      String data = myReader.nextLine();
      if (i == 0 ){}
      else {if (i ==1) {
        ModelName = data;
      }else{
      if (i % 2 == 0) {
        Keys.add(data);
      } else {
        Values.add(data);
      }}}
      i++;
      }
      for (int j = 0; j < Keys.size(); j++) {
        textMap.put(Keys.get(j), Values.get(j));
      }

      myReader.close();
      System.out.println(textMap.toString());
    } catch (FileNotFoundException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
    }

  public void addText(String key, String value) {
    textMap.put(key, value);
    writeFile();
  }

  public void updateModelName(String name){
    ModelName = name;
    writeFile();
  }

  public void ereaseText() {
    textMap.clear();
  }   

  public void removeText(String key) {
    if (textMap.containsKey(key)) {
      textMap.remove(key);
      writeFile();
    } else {
      System.out.println("Key not found: " + key);
    }
  }

  public void createFile() {
  try {
    System.out.println("Creating file at: " + FILE_PATH);
    File myObj = new File(FILE_PATH);
    myObj.getParentFile().mkdirs();
    if (myObj.createNewFile()) {
      System.out.println("File created: " + myObj.getName());
    } else {
      System.out.println("File already exists. at"+ FILE_PATH);
    }
  } catch (IOException e) {
    System.out.println("An error occurred.");
    e.printStackTrace();
  }

  }
  public void writeFile() {
  try {
    FileWriter myWriter = new FileWriter(FILE_PATH);
    myWriter.write("ModelName :" + "\n");
    myWriter.write(ModelName + "\n");
    for (Map.Entry<String, String> entry : textMap.entrySet()) {
      myWriter.write(entry.getKey() + "\n");
      myWriter.write(entry.getValue() + "\n");
    }
    myWriter.close();

  } catch (IOException e) {
    System.out.println("An error occurred.");
    e.printStackTrace();
  }
  }

}
