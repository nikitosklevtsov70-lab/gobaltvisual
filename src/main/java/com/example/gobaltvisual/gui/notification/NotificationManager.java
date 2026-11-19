package com.example.gobaltvisual.gui.notification;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import java.util.ArrayList;
import java.util.List;

public class NotificationManager {
    private static final List<Notification> notifications = new ArrayList<>();
    private static final int MAX_NOTIFICATIONS = 5;
    private static final int X_OFFSET = 20;
    private static final int Y_OFFSET = 20;

    public static void show(String message) {
        notifications.add(new Notification(message));
        if (notifications.size() > MAX_NOTIFICATIONS) {
            notifications.remove(0);
        }
    }

    public static void tick() {
        // Удаляем только те, которые полностью истекли
        notifications.removeIf(Notification::isExpired);
        for (Notification n : notifications) {
            n.tick();
        }
    }

    public static void render(DrawContext context, TextRenderer textRenderer) {
        MinecraftClient client = MinecraftClient.getInstance();
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        int y = Y_OFFSET;
        for (int i = notifications.size() - 1; i >= 0; i--) {
            Notification n = notifications.get(i);
            // Если альфа стала 0 — пропускаем отрисовку
            if (n.alpha <= 0.01f) continue;
            int x = X_OFFSET;
            n.render(context, textRenderer, x, y);
            y += 20;
        }
    }
}