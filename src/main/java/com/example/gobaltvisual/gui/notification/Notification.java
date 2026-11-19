package com.example.gobaltvisual.gui.notification;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class Notification {
    public String message;
    public long startTime;
    public long duration = 3000;
    public float alpha = 1.0f;
    public float offsetY = 0;

    public Notification(String message) {
        this.message = message;
        this.startTime = System.currentTimeMillis();
    }

    public boolean isExpired() {
        return System.currentTimeMillis() - startTime > duration + 500;
    }

    public void tick() {
        long elapsed = System.currentTimeMillis() - startTime;
        if (elapsed < 200) {
            alpha = elapsed / 200f;
            offsetY = (1 - alpha) * 20;
        } else if (elapsed > duration) {
            alpha = 1 - (elapsed - duration) / 500f;
            if (alpha < 0) alpha = 0;
        } else {
            alpha = 1.0f;
            offsetY = 0;
        }
    }

    public void render(DrawContext context, TextRenderer textRenderer, int x, int y) {
        if (alpha <= 0.01f) return;
        int a = (int)(alpha * 255);
        int color = (a << 24) | 0xFFFFFF;

        context.fill(x - 8, y - 4, x + textRenderer.getWidth(message) + 8, y + 10, (int)(0x80000000 | (a << 24)));
        context.drawTextWithShadow(textRenderer, Text.literal(message), x, y, color);
    }
}