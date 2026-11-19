package com.example.gobaltvisual;

import com.example.gobaltvisual.gui.GobaltVisualScreen;
import com.example.gobaltvisual.config.HitboxesConfig;
import com.example.gobaltvisual.utils.DeveloperMode;
import com.example.gobaltvisual.utils.GVLog;
import com.example.gobaltvisual.utils.HudRenderer;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class Gobaltvisual implements ClientModInitializer {
    private static KeyBinding openMenuKey;

    @Override
    public void onInitializeClient() {
        GVLog.info("[Gobalt Visual] Мод загружен!");

        // 🔥 Загружаем конфиг хитбоксов
        HitboxesConfig.load();

        // 🔥 Инициализируем HUD (FPS, пинг, CPS, keystrokes)
        HudRenderer.init();

        // Создаём клавишу для открытия меню (RIGHT_SHIFT)
        openMenuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.gobaltvisual.open_menu",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            "category.gobaltvisual"
        ));

        // Слушаем нажатия клавиш
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (openMenuKey.wasPressed()) {
                if (!(MinecraftClient.getInstance().currentScreen instanceof GobaltVisualScreen)) {
                    MinecraftClient.getInstance().setScreen(new GobaltVisualScreen());
                }
            }
        });

        // 🔥 РЕГИСТРАЦИЯ КОМАНД
        registerCommands();
    }

    private void registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {

            // /gv:admin — вкл/выкл DEV
            dispatcher.register(ClientCommandManager.literal("gv:admin")
                .executes(ctx -> {
                    DeveloperMode.toggle();
                    ctx.getSource().sendFeedback(Text.literal(
                        "§b[GV] §fРежим разработчика: " +
                        (DeveloperMode.isEnabled() ? "§aВКЛ" : "§cВЫКЛ")
                    ));
                    return 1;
                })
            );

            // /gv:reset — сбросить DEV
            dispatcher.register(ClientCommandManager.literal("gv:reset")
                .executes(ctx -> {
                    DeveloperMode.reset();
                    ctx.getSource().sendFeedback(Text.literal(
                        "§b[GV] §fРежим разработчика сброшен"
                    ));
                    return 1;
                })
            );

            // /gv:debug <feature> — вкл/выкл отладку конкретной фичи
            dispatcher.register(ClientCommandManager.literal("gv:debug")
                .then(ClientCommandManager.argument("feature", StringArgumentType.word())
                    .suggests((ctx, builder) -> {
                        for (DeveloperMode.Feature f : DeveloperMode.Feature.values()) {
                            builder.suggest(f.name().toLowerCase());
                        }
                        return builder.buildFuture();
                    })
                    .executes(ctx -> {
                        String name = StringArgumentType.getString(ctx, "feature").toUpperCase();
                        try {
                            DeveloperMode.Feature f = DeveloperMode.Feature.valueOf(name);
                            DeveloperMode.toggleFeature(f);
                            ctx.getSource().sendFeedback(Text.literal(
                                "§b[GV] §fОтладка " + f.name() + ": " +
                                (DeveloperMode.isDebug(f) ? "§aВКЛ" : "§cВЫКЛ")
                            ));
                        } catch (IllegalArgumentException e) {
                            ctx.getSource().sendError(Text.literal(
                                "§cНеизвестная фича: " + name
                            ));
                        }
                        return 1;
                    })
                )
            );

        });
    }
}