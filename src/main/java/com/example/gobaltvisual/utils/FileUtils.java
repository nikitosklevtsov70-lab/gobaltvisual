package com.example.gobaltvisual.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Files;

public class FileUtils {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Сохраняет объект в JSON файл
     */
    public static <T> void saveToFile(Path path, T data) {
        try (Writer writer = new FileWriter(path.toFile())) {
            GSON.toJson(data, writer);
        } catch (IOException ignored) {}
    }

    /**
     * Загружает объект из JSON файла
     */
    public static <T> T loadFromFile(Path path, Class<T> clazz) {
        if (!path.toFile().exists()) {
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                return null;
            }
        }
        try (Reader reader = new FileReader(path.toFile())) {
            return GSON.fromJson(reader, clazz);
        } catch (IOException ignored) {
            return null;
        }
    }

    /**
     * Проверяет, существует ли файл
     */
    public static boolean fileExists(Path path) {
        return path.toFile().exists();
    }

    /**
     * Создаёт папки для пути, если их нет
     */
    public static void createDirectories(Path path) {
        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
        } catch (IOException ignored) {}
    }
}