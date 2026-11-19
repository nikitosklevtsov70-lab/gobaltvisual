package com.example.gobaltvisual.utils;

import com.example.gobaltvisual.config.TogglesConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.RenderTickCounter;

public class HudRenderer {
    
    // Переменные для трекинга CPS
    private static int leftClicks = 0;
    private static int rightClicks = 0;
    private static int currentLeftCps = 0;
    private static int currentRightCps = 0;
    private static long lastSecond = 0;
    private static boolean wasLeftPressed = false;
    private static boolean wasRightPressed = false;

    public static void init() {
        // 🔥 Слушаем тики игры, чтобы считать клики
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            long now = System.currentTimeMillis();

            // Считаем левую кнопку мыши (Атака)
            boolean isLeftPressed = client.options.attackKey.isPressed();
            if (isLeftPressed && !wasLeftPressed) leftClicks++;
            wasLeftPressed = isLeftPressed;

            // Считаем правую кнопку мыши (Использование)
            boolean isRightPressed = client.options.useKey.isPressed();
            if (isRightPressed && !wasRightPressed) rightClicks++;
            wasRightPressed = isRightPressed;

            // Обновляем счетчик раз в секунду
            if (now - lastSecond >= 1000) {
                currentLeftCps = leftClicks;
                currentRightCps = rightClicks;
                leftClicks = 0;
                rightClicks = 0;
                lastSecond = now;
            }
        });

        // 🔥 Рисуем HUD
        HudRenderCallback.EVENT.register((DrawContext context, RenderTickCounter tickCounter) -> {
            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.player == null || mc.options.hudHidden) return;

            // 1. Ватермарка
            if (TogglesConfig.get("HUD", "Ватермарка")) {
                context.drawTextWithShadow(mc.textRenderer, "§bGOBALT §fVISUAL", 5, 5, 0xFFFFFF);
            }

            // 2. FPS и Пинг
            if (TogglesConfig.get("HUD", "Показывать FPS") || TogglesConfig.get("HUD", "Показывать Пинг")) {
                int screenWidth = mc.getWindow().getScaledWidth();
                int y = 5;
                
                if (TogglesConfig.get("HUD", "Показывать FPS")) {
                    String fpsText = "§aFPS: §f" + mc.getCurrentFps();
                    int width = mc.textRenderer.getWidth(fpsText);
                    context.drawTextWithShadow(mc.textRenderer, fpsText, screenWidth - width - 5, y, 0xFFFFFF);
                    y += 10;
                }

                if (TogglesConfig.get("HUD", "Показывать Пинг") && mc.getNetworkHandler() != null) {
                    PlayerListEntry entry = mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid());
                    int ping = (entry != null) ? entry.getLatency() : 0;
                    String pingText = "§ePing: §f" + ping + "ms";
                    int width = mc.textRenderer.getWidth(pingText);
                    context.drawTextWithShadow(mc.textRenderer, pingText, screenWidth - width - 5, y, 0xFFFFFF);
                }
            }

            // 3. Keystrokes (WASD)
            int screenHeight = mc.getWindow().getScaledHeight();
            int startX = 10;
            int startY = screenHeight - 90; // Чуть подняли, чтобы влез CPS
            int size = 18;
            int spacing = 2;

            if (TogglesConfig.get("HUD", "Keystrokes")) {
                int colorOn = 0xFF7A00FF;
                int colorOff = 0x80000000;
                int textColor = 0xFFFFFFFF;

                drawKey(context, mc, startX + size + spacing, startY, size, "W", (boolean) mc.options.forwardKey.isPressed(), colorOn, colorOff, textColor);
                drawKey(context, mc, startX, startY + size + spacing, size, "A", (boolean) mc.options.leftKey.isPressed(), colorOn, colorOff, textColor);
                drawKey(context, mc, startX + size + spacing, startY + size + spacing, size, "S", (boolean) mc.options.backKey.isPressed(), colorOn, colorOff, textColor);
                drawKey(context, mc, startX + (size + spacing) * 2, startY + size + spacing, size, "D", (boolean) mc.options.rightKey.isPressed(), colorOn, colorOff, textColor);
            }

            // 4. Счетчик CPS (Под Keystrokes)
            if (TogglesConfig.get("HUD", "CPS")) {
                int cpsY = startY + (size + spacing) * 2 + 5;
                int cpsWidth = (size * 4) + (spacing * 3); // Ширина как у ряда ASD
                
                // Фон для плашки
                context.fill(startX, cpsY, startX + cpsWidth, cpsY + 14, 0x80000000);
                context.drawBorder(startX, cpsY, cpsWidth, 14, 0xFF333333);

                String cpsText = "§dL: §f" + currentLeftCps + " §d| §dR: §f" + currentRightCps;
                
                // Центрируем текст
                int textWidth = mc.textRenderer.getWidth(cpsText);
                int textX = startX + (cpsWidth - textWidth) / 2;
                context.drawTextWithShadow(mc.textRenderer, cpsText, textX, cpsY + 3, 0xFFFFFF);
            }
        });
    }

    private static void drawKey(DrawContext context, MinecraftClient mc, int x, int y, int size, String letter, boolean pressed, int colorOn, int colorOff, int textColor) {
        int bgColor = pressed ? colorOn : colorOff;
        context.fill(x, y, x + size, y + size, bgColor);
        context.drawBorder(x, y, size, size, 0xFF333333);

        int textWidth = mc.textRenderer.getWidth(letter);
        int textX = x + (size - textWidth) / 2;
        int textY = y + (size - 8) / 2;
        context.drawTextWithShadow(mc.textRenderer, letter, textX, textY, textColor);
    }
}