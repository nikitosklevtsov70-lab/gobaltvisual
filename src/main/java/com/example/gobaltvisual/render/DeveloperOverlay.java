package com.example.gobaltvisual.render;

import com.example.gobaltvisual.config.HitboxesConfig;
import com.example.gobaltvisual.utils.DeveloperMode;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class DeveloperOverlay {

    private static final MinecraftClient mc = MinecraftClient.getInstance();

    public static void register() {
        HudRenderCallback.EVENT.register((context, tickDelta) -> {
            if (!DeveloperMode.isEnabled()) return;
            if (mc.player == null || mc.world == null) return;
            renderOverlay(context);
        });
    }

    private static void renderOverlay(DrawContext context) {
        TextRenderer textRenderer = mc.textRenderer;
        int x = 10;
        int y = 10;
        int lineHeight = 12;

        // 1. ЗАГОЛОВОК
        context.drawText(textRenderer, Text.literal("[GV] РЕЖИМ РАЗРАБОТЧИКА"), x, y, 0xFFAA00, true);
        y += lineHeight + 5;

        // 2. КЛАВИШИ (подсветка при нажатии)
        String shiftText = "Shift: " + (isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT) ? "НАЖАТ" : "НЕ НАЖАТ");
        context.drawText(textRenderer, Text.literal(shiftText), x, y, 0xFFFFFF, true);
        y += lineHeight;

        String ctrlText = "Ctrl: " + (isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL) ? "НАЖАТ" : "НЕ НАЖАТ");
        context.drawText(textRenderer, Text.literal(ctrlText), x, y, 0xFFFFFF, true);
        y += lineHeight;

        String altText = "Alt: " + (isKeyDown(GLFW.GLFW_KEY_LEFT_ALT) ? "НАЖАТ" : "НЕ НАЖАТ");
        context.drawText(textRenderer, Text.literal(altText), x, y, 0xFFFFFF, true);
        y += lineHeight + 5;

        // 3. АКТИВНЫЕ ФУНКЦИИ
        context.drawText(textRenderer, Text.literal("[АКТИВНЫЕ ФУНКЦИИ]"), x, y, 0x55FF55, true);
        y += lineHeight;

        // Хитбоксы
        String hitboxStatus = "Хитбоксы: " + (HitboxesConfig.data.enabled ? "ВКЛ" : "ВЫКЛ");
        context.drawText(textRenderer, Text.literal(hitboxStatus), x, y, HitboxesConfig.data.enabled ? 0x55FF55 : 0xFF5555, true);
        y += lineHeight;

        // Прицел (пока заглушка)
        context.drawText(textRenderer, Text.literal("Прицел: ОТКЛ"), x, y, 0x888888, true);
        y += lineHeight;

        // Kill Streak (пока заглушка)
        context.drawText(textRenderer, Text.literal("Kill Streak: ОТКЛ"), x, y, 0x888888, true);
        y += lineHeight;

        // Гамма (пока заглушка)
        context.drawText(textRenderer, Text.literal("Гамма: ОТКЛ"), x, y, 0x888888, true);
        y += lineHeight + 5;

        // 4. ИНФОРМАЦИЯ О КОНФИГЕ
        context.drawText(textRenderer, Text.literal("Конфиг: " + HitboxesConfig.data.enabled), x, y, 0x888888, true);
        y += lineHeight;

        // 5. СОВЕТЫ
        context.drawText(textRenderer, Text.literal("/gv:admin — вкл/выкл, /gv:reset — выкл"), x, y, 0x888888, true);
    }

    private static boolean isKeyDown(int keyCode) {
        return InputUtil.isKeyPressed(mc.getWindow().getHandle(), keyCode);
    }
}
