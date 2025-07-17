package com.aurelien.pautet.net;

import java.io.IOException;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;

public class ClipboardManager {

    static Clipboard clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();

    public String getClipBoard(){
    try {
        return (String) clipBoard.getData(DataFlavor.stringFlavor);          
    } catch (IOException | java.awt.datatransfer.UnsupportedFlavorException e) {
        e.printStackTrace();
    }
    return "";
    }
    public void setClipBoard(String text){
        clipBoard.setContents(new java.awt.datatransfer.StringSelection(text), null);
    }

    public static void main(String[] args) {
        ClipboardManager clipboardManager = new ClipboardManager();

        String clipboardText = clipboardManager.getClipBoard();
        System.out.println("Clipboard text: " + clipboardText);

        clipboardManager.setClipBoard("Ca marche ou bien ?");
        System.out.println("AHH dac");
    }

}
