package com.example.gobaltvisual.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ArmorConfig {

    public static class ArmorData {
        public boolean enabled = false;
        public String template = "silence";      // ID шаблона
        public String material = "diamond";      // ID материала
    }

    public static ArmorData data = new ArmorData();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "gobalt_armor.json");

    public static void load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                data = GSON.fromJson(reader, ArmorData.class);
            } catch (IOException e) {
                System.err.println("[GV] Ошибка загрузки конфига брони: " + e.getMessage());
            }
        } else {
            save();
        }
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(data, writer);
        } catch (IOException e) {
            System.err.println("[GV] Ошибка сохранения конфига брони: " + e.getMessage());
        }
    }

    public static boolean isEnabled() {
        return data.enabled;
    }
}
