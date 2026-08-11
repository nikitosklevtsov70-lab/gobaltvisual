package com.example.gobaltvisual.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ClientConfig {
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("gobaltvisual_client.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static ClientData data = new ClientData();

    public static class ClientData {
        public String theme = "dark";
        public String language = "en_us";
        public int menuKey = 80; // P
        public String menuMode = "categories";
        public boolean showStartupScreen = true;

        // === ЦВЕТ ФОНА ===
        public int menuTintColor = 0x00D4FF;
        public float menuTintIntensity = 0.3f;
        public boolean menuTintEnabled = true;

        // === АНИМАЦИИ ===
        public boolean animationMenuOpen = true;
        public boolean animationLogoRotate = true;
        public boolean animationLogoSlide = false;
        public float animationSpeed = 1.0f;

        // === ИЗБРАННОЕ ===
        public List<String> favorites = new ArrayList<>();
    }

    public static void load() {
        if (PATH.toFile().exists()) {
            try (Reader reader = new FileReader(PATH.toFile())) {
                data = GSON.fromJson(reader, ClientData.class);
            } catch (IOException ignored) {}
        }
        save();
    }

    public static void save() {
        try (Writer writer = new FileWriter(PATH.toFile())) {
            GSON.toJson(data, writer);
        } catch (IOException ignored) {}
    }

    // ================================================================
    // МЕТОДЫ ДЛЯ РАБОТЫ С ИЗБРАННЫМ
    // ================================================================
    public static void toggleFavorite(String id) {
        if (data.favorites.contains(id)) {
            data.favorites.remove(id);
        } else {
            data.favorites.add(id);
        }
        save();
    }

    public static boolean isFavorite(String id) {
        return data.favorites.contains(id);
    }

    public static List<String> getFavorites() {
        return data.favorites;
    }

    public static void clearFavorites() {
        data.favorites.clear();
        save();
    }
}