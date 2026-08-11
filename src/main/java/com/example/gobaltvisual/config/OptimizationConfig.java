package com.example.gobaltvisual.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import java.io.*;
import java.nio.file.Path;

public class OptimizationConfig {
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("gobaltvisual_optimization.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static OptimizationData data = new OptimizationData();

    public static class OptimizationData {
        public boolean enabled = false;
        public boolean cullParticles = true;
        public boolean cullEntities = true;
        public boolean cullBlocks = false;
        public int cullEntitiesDistance = 32;
        public boolean cullEntitiesBehind = true;
        public boolean cullParticlesBehind = true;
        public boolean cullItems = true;
        public boolean cullExperience = true;
    }

    public static void load() {
        if (PATH.toFile().exists()) {
            try (Reader reader = new FileReader(PATH.toFile())) {
                data = GSON.fromJson(reader, OptimizationData.class);
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
