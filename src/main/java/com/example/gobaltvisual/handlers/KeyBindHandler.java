package com.example.gobaltvisual.handlers;

import com.example.gobaltvisual.gui.GobaltVisualScreen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyBindHandler {
    private static KeyBinding openMenuKey;

    public static void register() {
        // Клавиша P
        openMenuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.gobaltvisual.openmenu",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_P,
            "category.gobaltvisual"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // Открытие по P
            while (openMenuKey.wasPressed()) {
                if (client.currentScreen == null) {
                    System.out.println("[GV] Открываем меню по клавише P");
                    client.setScreen(new GobaltVisualScreen());
                }
            }

            // Открытие по правому Shift
            if (client.currentScreen == null && client.options != null) {
                if (GLFW.glfwGetKey(client.getWindow().getHandle(), GLFW.GLFW_KEY_RIGHT_SHIFT) == GLFW.GLFW_PRESS) {
                    System.out.println("[GV] Открываем меню по правому Shift");
                    client.setScreen(new GobaltVisualScreen());
                    // Небольшая задержка, чтобы не открывалось несколько раз
                    try { Thread.sleep(100); } catch (Exception ignored) {}
                }
            }
        });
    }
}
