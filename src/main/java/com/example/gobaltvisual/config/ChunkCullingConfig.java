package com.example.gobaltvisual.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public class ChunkCullingConfig {

    private static final File CONFIG_FILE = new File("config/ChunkCullingConfig.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static ChunkCullingData data = new ChunkCullingData();

    public static void load() {
        if (!CONFIG_FILE.exists()) {
            save();
            return;
        }

        try (Reader reader = new FileReader(CONFIG_FILE)) {
            data = GSON.fromJson(reader, ChunkCullingData.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        try (Writer writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class ChunkCullingData {
        public boolean enabled = true;
        public double maxDistance = 32.0; // Максимальная дистанция в блоках
        public boolean cullBehind = true; // Отсекать ли чанки за спиной
        public boolean cullSides = true; // Отсекать ли чанки по бокам
        public int verticalAngle = 70; // Угол обзора по вертикали (градусы)
        public int horizontalAngle = 90; // Угол обзора по горизонтали (градусы)
        public boolean debugLog = false; // Логирование для отладки
    }
}
