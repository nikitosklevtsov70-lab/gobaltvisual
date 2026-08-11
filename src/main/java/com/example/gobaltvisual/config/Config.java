package com.example.gobaltvisual.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import java.io.*;
import java.nio.file.Path;

public class Config {
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("gobaltvisual_config.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static ConfigData data = new ConfigData();

    public static class ConfigData {
        // ===== ХИТБОКСЫ =====
        public boolean renderPlayers = true;
        public boolean renderMobs = true;
        public boolean renderOther = true;

        // ===== ЦВЕТА ХИТБОКСОВ =====
        public float red = 0.0f;
        public float green = 1.0f;
        public float blue = 0.0f;
        public float alpha = 0.5f;

        // ===== РАЗМЕРЫ =====
        public float widthScale = 1.0f;
        public float heightScale = 1.0f;
        public float lineThickness = 1.0f;

        // ===== ПОДСВЕТКА =====
        public boolean enableHitGlow = true;
        public float hitRed = 1.0f;
        public float hitGreen = 0.0f;
        public float hitBlue = 0.0f;
        public float hitAlpha = 0.8f;

        // ===== ПРЕДУПРЕЖДЕНИЕ О ВРАГАХ =====
        public boolean enableEnemyWarning = true;
        public float enemyWarningDistance = 2.5f;
        public int enemyWarningRed = 255;
        public int enemyWarningGreen = 0;
        public int enemyWarningBlue = 0;
        public float enemyWarningAlpha = 1.0f;

        // ===== ДРУЗЬЯ =====
        public int friendRed = 0;
        public int friendGreen = 255;
        public int friendBlue = 0;
        public float friendAlpha = 0.8f;

        // ===== НАСТРОЙКИ ХИТБОКСОВ =====
        public boolean showNicknames = true;
        public boolean showHealth = false;
        public boolean showArmor = false;
    }

    public static void load() {
        if (PATH.toFile().exists()) {
            try (Reader reader = new FileReader(PATH.toFile())) {
                data = GSON.fromJson(reader, ConfigData.class);
            } catch (IOException ignored) {}
        }
        save();
    }

    public static void save() {
        try (Writer writer = new FileWriter(PATH.toFile())) {
            GSON.toJson(data, writer);
        } catch (IOException ignored) {}
    }
}
