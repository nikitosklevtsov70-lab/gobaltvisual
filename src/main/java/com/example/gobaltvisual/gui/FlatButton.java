package com.example.gobaltvisual.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

public class FlatButton extends ButtonWidget {
    private float hoverProgress = 0.0f;
    private boolean selected = false;
    private long lastTime = System.nanoTime();

    public FlatButton(int x, int y, int width, int height, Text message, PressAction onPress) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION_SUPPLIER);
    }

    public FlatButton setSelected(boolean selected) {
        this.selected = selected;
        return this;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        // 1. Точный расчет реального времени кадра в секундах (без скачков tickDelta)
        long now = System.nanoTime();
        float dt = (now - lastTime) / 1_000_000_000.0f;
        lastTime = now;
        if (dt > 0.1f) dt = 0.016f; // Защита от скачка при первом открытии меню

        // 2. Плавное экспоненциальное затухание (скорость 12.0f дает мягкий отклик за ~0.2 сек)
        float target = (this.isHovered() || this.selected) ? 1.0f : 0.0f;
        float factor = 1.0f - (float) Math.exp(-12.0f * dt);
        this.hoverProgress = MathHelper.lerp(factor, this.hoverProgress, target);

        // 3. Космическая пурпурная палитра
        int bgColor = lerpColor(0xCC1E1736, 0xEE352463, this.hoverProgress);
        int borderColor = lerpColor(0x808A2BE2, 0xFFE0AAFF, this.hoverProgress);

        int x = this.getX();
        int y = this.getY();

        context.fill(x, y, x + this.width, y + this.height, bgColor);
        context.drawBorder(x, y, this.width, this.height, borderColor);

        // 4. Плавная сиреневая полоска слева
        if (this.hoverProgress > 0.01f) {
            int barColor = lerpColor(0x009D4EDD, 0xFFE0AAFF, this.hoverProgress);
            context.fill(x + 1, y + 3, x + 3, y + this.height - 3, barColor);
        }

        // 5. Субпиксельный (дробный) сдвиг текста через матрицы — никаких рывков по пикселям!
        context.getMatrices().push();
        context.getMatrices().translate(this.hoverProgress * 2.5f, 0.0f, 0.0f);
        context.drawCenteredTextWithShadow(
                MinecraftClient.getInstance().textRenderer,
                this.getMessage(),
                x + (this.width / 2),
                y + (this.height - 8) / 2,
                0xFFFFFFFF
        );
        context.getMatrices().pop();
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
    }

    private static int lerpColor(int c1, int c2, float t) {
        int a1 = (c1 >> 24) & 0xFF, r1 = (c1 >> 16) & 0xFF, g1 = (c1 >> 8) & 0xFF, b1 = c1 & 0xFF;
        int a2 = (c2 >> 24) & 0xFF, r2 = (c2 >> 16) & 0xFF, g2 = (c2 >> 8) & 0xFF, b2 = c2 & 0xFF;
        int a = (int) (a1 + (a2 - a1) * t);
        int r = (int) (r1 + (r2 - r1) * t);
        int g = (int) (g1 + (g2 - g1) * t);
        int b = (int) (b1 + (b2 - b1) * t);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }
}
