package com.aurelien.pautet.net;

import java.io.IOException;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;

public class ClipboardManager {

    public String getClipBoard(){
    try {
        return (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);          
    } catch (IOException | java.awt.datatransfer.UnsupportedFlavorException e) {
        e.printStackTrace();
    }
    return "";
    }
    public void setClipBoard(String text){
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new java.awt.datatransfer.StringSelection(text), null);
    }

    public static void main(String[] args) {
        ClipboardManager clipboardManager = new ClipboardManager();

        String clipboardText = clipboardManager.getClipBoard();
        System.out.println("Clipboard text: " + clipboardText);

        clipboardManager.setClipBoard("Ca marche ou bien ?");
        System.out.println("AHH dac");
    }

}
