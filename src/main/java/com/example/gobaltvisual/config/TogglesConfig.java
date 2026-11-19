package com.example.gobaltvisual.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class TogglesConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File(
        FabricLoader.getInstance().getConfigDir().toFile(),
        "gobaltvisual/toggles.json"
    );

    // 🔥 Булевы значения (тумблеры)
    public static Map<String, Boolean> states = new HashMap<>();

    // 🔥 Целочисленные значения (кнопки)
    public static Map<String, Integer> intStates = new HashMap<>();

    // 🔥 Float-значения (слайдеры)
    public static Map<String, Float> floatStates = new HashMap<>();

    public static void load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                Type type = new TypeToken<ConfigData>(){}.getType();
                ConfigData data = GSON.fromJson(reader, type);
                if (data != null) {
                    if (data.states != null) states = data.states;
                    if (data.intStates != null) intStates = data.intStates;
                    if (data.floatStates != null) floatStates = data.floatStates;
                }
            } catch (IOException e) {
                System.err.println("[GV] Ошибка загрузки toggles.json: " + e.getMessage());
            }
        } else {
            save();
        }
    }

    public static void save() {
        try {
            File parent = CONFIG_FILE.getParentFile();
            if (parent != null && !parent.exists()) {
                if (!parent.mkdirs()) {
                    System.err.println("[GV] Не удалось создать папку: " + parent.getAbsolutePath());
                    return;
                }
            }

            try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
                ConfigData data = new ConfigData();
                data.states = states;
                data.intStates = intStates;
                data.floatStates = floatStates;
                GSON.toJson(data, writer);
            }
        } catch (IOException e) {
            System.err.println("[GV] Ошибка сохранения toggles.json: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("[GV] Неизвестная ошибка при сохранении: " + e.getMessage());
        }
    }

    // 🔥 Boolean (тумблеры)
    public static boolean get(String column, String item) {
        return states.getOrDefault(column + "." + item, false);
    }

    public static void set(String column, String item, boolean value) {
        states.put(column + "." + item, value);
        save();
    }

    // 🔥 Int (кнопки)
    public static int getInt(String column, String item) {
        return intStates.getOrDefault(column + "." + item, 0);
    }

    public static void setInt(String column, String item, int value) {
        intStates.put(column + "." + item, value);
        save();
    }

    // 🔥 Float (слайдеры) — по умолчанию 1.0f, чтобы alpha была яркой
    public static float getFloat(String column, String item) {
        return floatStates.getOrDefault(column + "." + item, -1f);
    }

    public static void setFloat(String column, String item, float value) {
        floatStates.put(column + "." + item, value);
        save();
    }

    // 🔥 Внутренний класс для сериализации
    private static class ConfigData {
        Map<String, Boolean> states = new HashMap<>();
        Map<String, Integer> intStates = new HashMap<>();
        Map<String, Float> floatStates = new HashMap<>();
    }
}