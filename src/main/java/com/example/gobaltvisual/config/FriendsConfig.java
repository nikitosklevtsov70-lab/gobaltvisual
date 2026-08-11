package com.example.gobaltvisual.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;
import java.io.*;
import java.nio.file.Path;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class FriendsConfig {
    private static final Path FRIENDS_PATH = FabricLoader.getInstance().getConfigDir().resolve("gobaltvisual_friends.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static Map<String, FriendData> friends = new HashMap<>();

    public static class FriendData {
        public String nickname;
        public int red = 0, green = 255, blue = 0;
        public boolean enabled = true;
        public boolean protected_ = false;
    }

    public static void load() {
        if (FRIENDS_PATH.toFile().exists()) {
            try (Reader reader = new FileReader(FRIENDS_PATH.toFile())) {
                // === ИСПРАВЛЕНО: правильная десериализация Map ===
                Type type = new TypeToken<Map<String, FriendData>>(){}.getType();
                friends = GSON.fromJson(reader, type);
                if (friends == null) friends = new HashMap<>();
            } catch (IOException ignored) {}
        }
        save();
    }

    public static void save() {
        try (Writer writer = new FileWriter(FRIENDS_PATH.toFile())) {
            GSON.toJson(friends, writer);
        } catch (IOException ignored) {}
    }

    public static boolean isFriend(String uuid) {
        return friends.containsKey(uuid) && friends.get(uuid).enabled;
    }

    public static FriendData getFriendData(String uuid) {
        return friends.get(uuid);
    }

    public static void addFriend(String uuid, String nickname, int r, int g, int b) {
        FriendData data = new FriendData();
        data.nickname = nickname;
        data.red = r;
        data.green = g;
        data.blue = b;
        data.enabled = true;
        data.protected_ = false;
        friends.put(uuid, data);
        save();
    }

    public static void removeFriend(String uuid) {
        friends.remove(uuid);
        save();
    }

    public static void toggleProtection(String uuid) {
        FriendData data = friends.get(uuid);
        if (data != null) {
            data.protected_ = !data.protected_;
            save();
        }
    }

    public static boolean isProtected(String uuid) {
        FriendData data = friends.get(uuid);
        return data != null && data.protected_;
    }
}