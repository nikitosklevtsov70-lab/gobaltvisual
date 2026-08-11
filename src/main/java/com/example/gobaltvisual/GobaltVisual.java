package com.example.gobaltvisual;

import com.example.gobaltvisual.config.*;
import com.example.gobaltvisual.handlers.*;
import net.fabricmc.api.ModInitializer;

public class GobaltVisual implements ModInitializer {
    public static final String MOD_ID = "gobaltvisual";
    public static final String VERSION = "2.0-a0";

    @Override
    public void onInitialize() {
        System.out.println("[GV] Gobalt Visual " + VERSION + " загружается!");

        // Загрузка конфигов
        Config.load();
        ClientConfig.load();
        FriendsConfig.load();
        HitboxesConfig.load();

        // Только клавиши
        KeyBindHandler.register();

        System.out.println("[GV] Мод успешно загружен! Нажми P для открытия меню.");
    }
}
