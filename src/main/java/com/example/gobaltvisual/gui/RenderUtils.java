package com.example.gobaltvisual.gui;

import net.minecraft.client.gui.DrawContext;

public class RenderUtils {

    /**
     * Рисует закруглённый прямоугольник.
     */
    public static void drawRoundedRect(DrawContext context, int x, int y, int width, int height, int radius, int color) {
        context.fill(x + radius, y, x + width - radius, y + height, color);
        context.fill(x, y + radius, x + width, y + height - radius, color);
        // Углы (можно заменить на drawCircle, если есть)
        context.fill(x, y, x + radius, y + radius, color);
        context.fill(x + width - radius, y, x + width, y + radius, color);
        context.fill(x, y + height - radius, x + radius, y + height, color);
        context.fill(x + width - radius, y + height - radius, x + width, y + height, color);
    }

    /**
     * Рисует закруглённую рамку.
     */
    public static void drawRoundedBorder(DrawContext context, int x, int y, int width, int height, int radius, int color) {
        // Упрощённая рамка (можно улучшить под стиль)
        context.drawBorder(x, y, width, height, color);
    }

    /**
     * Рисует круг.
     */
    public static void drawCircle(DrawContext context, int centerX, int centerY, int radius, int color, float alpha) {
        int alphaColor = (color & 0x00FFFFFF) | ((int)(alpha * 255) << 24);
        for (int y = -radius; y <= radius; y++) {
            int x = (int) Math.sqrt(radius * radius - y * y);
            context.fill(centerX - x, centerY + y, centerX + x + 1, centerY + y + 1, alphaColor);
        }
    }

    /**
     * 🔥 Рисует тумблер (переключатель) вместо кружка.
     */
    public static void drawToggle(DrawContext context, int x, int y, boolean enabled, float alpha) {
        int toggleWidth = 18;
        int toggleHeight = 10;
        int knobSize = 8;
        int knobX;

        // Цвета
        int backgroundColor = enabled ? 0xFF00C853 : 0xFF555555; // зелёный или серый
        int knobColor = 0xFFFFFFFF; // белый ползунок

        // Прозрачность
        backgroundColor = (backgroundColor & 0x00FFFFFF) | ((int)(alpha * 255) << 24);
        knobColor = (knobColor & 0x00FFFFFF) | ((int)(alpha * 255) << 24);

        // Фон тумблера
        context.fill(x, y, x + toggleWidth, y + toggleHeight, backgroundColor);

        // Ползунок
        knobX = enabled ? x + toggleWidth - knobSize - 1 : x + 1;
        context.fill(knobX, y + 1, knobX + knobSize, y + toggleHeight - 1, knobColor);

        // Обводка
        context.drawBorder(x, y, toggleWidth, toggleHeight, 0xFF888888);
    }
}