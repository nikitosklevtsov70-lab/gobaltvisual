package com.example.gobaltvisual.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import java.util.function.Consumer;

public class FlatSlider extends SliderWidget {
    private final Text prefix;
    private final Consumer<Double> onChange;

    private float visualValue;
    private float hoverProgress = 0.0f;
    private long lastTime = System.nanoTime();

    public FlatSlider(int x, int y, int width, int height, Text prefix, double value, Consumer<Double> onChange) {
        super(x, y, width, height, Text.empty(), value);
        this.prefix = prefix;
        this.onChange = onChange;
        this.visualValue = (float) value;
        this.updateMessage();
    }

    @Override
    protected void updateMessage() {
        this.setMessage(Text.literal(prefix.getString() + ": " + String.format("%.2f", this.value)));
    }

    @Override
    protected void applyValue() {
        this.onChange.accept(this.value);
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        // 1. Точный таймер в наносекундах для независимой от FPS плавности
        long now = System.nanoTime();
        float dt = (now - lastTime) / 1_000_000_000.0f;
        lastTime = now;
        if (dt > 0.1f) dt = 0.016f;

        // 2. Плавное движение самого значения слайдера (скорость 18.0f — отзывчиво, но без рывков)
        float valueFactor = 1.0f - (float) Math.exp(-18.0f * dt);
        this.visualValue = MathHelper.lerp(valueFactor, this.visualValue, (float) this.value);

        // 3. Плавная подсветка при наведении
        float targetHover = this.isHovered() ? 1.0f : 0.0f;
        float hoverFactor = 1.0f - (float) Math.exp(-12.0f * dt);
        this.hoverProgress = MathHelper.lerp(hoverFactor, this.hoverProgress, targetHover);

        int x = this.getX();
        int y = this.getY();

        // 4. Фон и рамка в стиле "Пурпурный космос"
        int bgColor = lerpColor(0x900F0C1D, 0xCC1E1433, this.hoverProgress);
        int borderColor = lerpColor(0x456A0DAD, 0xFFD05CE3, this.hoverProgress);

        context.fill(x, y, x + this.width, y + this.height, bgColor);
        context.drawBorder(x, y, this.width, this.height, borderColor);

        // 5. Субпиксельная полоска заполнения через матрицы (без рывков по пикселям)
        float innerWidth = (this.width - 2) * this.visualValue;
        int fillColor = lerpColor(0x997A00FF, 0xCC9D4EDD, this.hoverProgress);

        if (innerWidth > 0.1f) {
            context.getMatrices().push();
            context.getMatrices().translate(x + 1, y + 1, 0.0f);
            context.getMatrices().scale(innerWidth, this.height - 2, 1.0f);
            context.fill(0, 0, 1, 1, fillColor);
            context.getMatrices().pop();
        }

        // 6. Субпиксельный неоновый ползунок (бегунок)
        float handleX = x + 1 + innerWidth;
        int handleColor = lerpColor(0xFFE0AAFF, 0xFFFFFFFF, this.hoverProgress);

        context.getMatrices().push();
        context.getMatrices().translate(handleX - 1.5f, y - 1.0f, 0.0f);
        context.fill(0, 0, 3, this.height + 2, handleColor);
        context.getMatrices().pop();

        // 7. Четкий белый текст поверх шкалы
        context.drawCenteredTextWithShadow(
                MinecraftClient.getInstance().textRenderer,
                this.getMessage(),
                x + this.width / 2,
                y + (this.height - 8) / 2,
                0xFFFFFFFF
        );
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
