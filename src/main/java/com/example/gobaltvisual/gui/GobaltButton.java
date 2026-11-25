/*
package com.example.gobaltvisual.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.util.math.MathHelper;

public class GobaltButton extends ButtonWidget {
    private final TextRenderer textRenderer;
    private float hoverProgress = 0.0f;

    public GobaltButton(int x, int y, int width, int height, Text message, PressAction onPress, TextRenderer textRenderer) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION_SUPPLIER);
        this.textRenderer = textRenderer;
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        // Плавная анимация наведения
        if (this.isHovered()) {
            hoverProgress = Math.min(1.0f, hoverProgress + delta * 5.0f);
        } else {
            hoverProgress = Math.max(0.0f, hoverProgress - delta * 5.0f);
        }

        // 🎨 Вычислить яркость цвета
        int red = (int) MathHelper.lerp(hoverProgress, 44, 107);
        int green = (int) MathHelper.lerp(hoverProgress, 16, 66);
        int blue = (int) MathHelper.lerp(hoverProgress, 66, 166);
        
        int color = (0xFF << 24) | (red << 16) | (green << 8) | blue;

        // 🎨 Рисуем ФОН (плоский)
        context.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), color);

        // 🎨 Рисуем ОБВОДКУ (тонкую и точную)
        context.drawBorder(this.getX(), this.getY(), this.getWidth(), this.getHeight(), 0xFFB14BFF);

        // 🎨 Рисуем текст (светло-серый #f3f3f3)
        context.drawTextWithShadow(
            this.textRenderer,
            this.getMessage(),
            this.getX() + 5,
            this.getY() + (this.getHeight() - 8) / 2,
            0xFFF3F3F3
        );
    }
}*/
