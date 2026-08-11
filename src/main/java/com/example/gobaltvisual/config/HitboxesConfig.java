package com.example.gobaltvisual.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import java.io.*;
import java.nio.file.Path;

public class HitboxesConfig {
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("gobaltvisual_hitboxes.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static HitboxesData data = new HitboxesData();

    public static class HitboxesData {
        public boolean enabled = true;
        public float red = 0.0f;
        public float green = 1.0f;
        public float blue = 0.0f;
        public float alpha = 0.5f;
        public float widthScale = 1.0f;
        public float heightScale = 1.0f;
        public float lineThickness = 1.0f;
        public boolean enableHitGlow = true;
        public float hitRed = 1.0f;
        public float hitGreen = 0.0f;
        public float hitBlue = 0.0f;
        public float hitAlpha = 0.8f;
        public String hitboxMode = "solid";
        public boolean blinkEnabled = false;
        public float blinkSpeed = 1.0f;
        public float blinkMinAlpha = 0.1f;
        public float blinkMaxAlpha = 1.0f;
        public boolean gradientEnabled = false;
        public float gradientRed1 = 1.0f, gradientGreen1 = 0.0f, gradientBlue1 = 0.0f;
        public float gradientRed2 = 0.0f, gradientGreen2 = 1.0f, gradientBlue2 = 0.0f;
        public float gradientRed3 = 0.0f, gradientGreen3 = 0.0f, gradientBlue3 = 1.0f;
        public float gradientSpeed = 1.0f;
        public boolean extendedEnabled = false;
        public float headRed = 0.0f, headGreen = 1.0f, headBlue = 1.0f;
        public float bodyRed = 1.0f, bodyGreen = 0.0f, bodyBlue = 0.0f;
        public float legsRed = 1.0f, legsGreen = 1.0f, legsBlue = 0.0f;
        public boolean modelEnabled = false;
        public float modelRed = 0.0f, modelGreen = 1.0f, modelBlue = 0.0f;
        public float modelAlpha = 0.3f;
        public boolean modelGlow = true;
        public boolean healthColorEnabled = false;
        public float healthGreenR = 0.0f, healthGreenG = 1.0f, healthGreenB = 0.0f;
        public float healthYellowR = 1.0f, healthYellowG = 1.0f, healthYellowB = 0.0f;
        public float healthRedR = 1.0f, healthRedG = 0.0f, healthRedB = 0.0f;
        public float healthThreshold1 = 0.5f;
        public float healthThreshold2 = 0.25f;
        public boolean shaderEnabled = false;
        public String shaderType = "none";
        public float shaderRed = 0.0f, shaderGreen = 1.0f, shaderBlue = 1.0f;
        public float shaderAlpha = 0.6f;
        public float shaderSpeed = 1.0f;
        public float shaderSize = 1.0f;
        public boolean skinHighlightEnabled = false;
        public boolean rainbowEnabled = false;
        public boolean neonEnabled = false;
        public float neonIntensity = 0.5f;
        public float rainbowSpeed = 1.0f;
    }

    public static void load() {
        if (PATH.toFile().exists()) {
            try (Reader reader = new FileReader(PATH.toFile())) {
                data = GSON.fromJson(reader, HitboxesData.class);
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
