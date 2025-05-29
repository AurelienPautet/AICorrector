package com.aurelien.pautet.net;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

public class CustomHotkeyListener implements NativeKeyListener {


    public CustomHotkeyListener() {
        // Constructor can be used for initialization if needed
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            e.printStackTrace();
        }
        GlobalScreen.addNativeKeyListener(this);
        System.out.println("CustomHotkeyListener initialized and registered.");
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        // Ctrl+Alt+C
        if (e.getKeyCode() == NativeKeyEvent.VC_C &&
            (e.getModifiers() & NativeKeyEvent.CTRL_MASK) != 0 &&
            (e.getModifiers() & NativeKeyEvent.ALT_MASK) != 0) {
            System.out.println("Ctrl+Alt+C pressed!");
            GeminiCorrector.copyCorrectPaste();
            // Place your copy/paste logic here
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {}

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {}
}
