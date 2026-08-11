package com.example.gobaltvisual.gui;

import com.example.gobaltvisual.gui.widgets.GVButton;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class GobaltVisualScreen extends Screen {
    private static final int WINDOW_WIDTH = 580;
    private static final int WINDOW_HEIGHT = 240;
    private static final int BUTTON_WIDTH = 130;
    private static final int BUTTON_HEIGHT = 16;
    private static final int BUTTON_GAP = 18;

    private int windowX;
    private int windowY;

    public GobaltVisualScreen() {
        super(Text.literal("Gobalt Visual"));
    }

    @Override
    protected void init() {
        this.windowX = (this.width - WINDOW_WIDTH) / 2;
        this.windowY = (this.height - WINDOW_HEIGHT) / 2;
        createButtons();
    }

    private void createButtons() {
        this.clearChildren();

        int cx = this.windowX + WINDOW_WIDTH / 2;
        int cy = this.windowY + WINDOW_HEIGHT / 2 - 10;
        int y1 = cy - 20;

        // ===== ПВП (6 функций) =====
        int x1 = cx - 200;
        this.addDrawableChild(new GVButton(x1, y1 + 20, BUTTON_WIDTH, BUTTON_HEIGHT, 
                Text.literal("🎯 Хитбоксы"), 
                () -> client.setScreen(new SubmenuHitboxes(this))));
        this.addDrawableChild(new GVButton(x1, y1 + 38, BUTTON_WIDTH, BUTTON_HEIGHT, 
                Text.literal("🎯 Прицел"), 
                () -> sendMessage("🎯 Прицел открыт!")));
        this.addDrawableChild(new GVButton(x1, y1 + 56, BUTTON_WIDTH, BUTTON_HEIGHT, 
                Text.literal("☠️ Kill Streak"), 
                () -> sendMessage("☠️ Kill Streak открыт!")));
        this.addDrawableChild(new GVButton(x1, y1 + 74, BUTTON_WIDTH, BUTTON_HEIGHT, 
                Text.literal("👟 Отбрасывание"), 
                () -> sendMessage("👟 Отбрасывание открыто!")));

        // ===== ВИЗУАЛ (6 функций) =====
        int x2 = cx - 60;
        this.addDrawableChild(new GVButton(x2, y1 + 20, BUTTON_WIDTH, BUTTON_HEIGHT, 
                Text.literal("☀️ Гамма"), 
                () -> sendMessage("☀️ Гамма открыта!")));
        this.addDrawableChild(new GVButton(x2, y1 + 38, BUTTON_WIDTH, BUTTON_HEIGHT, 
                Text.literal("🌤️ Небо"), 
                () -> sendMessage("🌤️ Небо открыто!")));
        this.addDrawableChild(new GVButton(x2, y1 + 56, BUTTON_WIDTH, BUTTON_HEIGHT, 
                Text.literal("🖐️ Руки"), 
                () -> sendMessage("🖐️ Руки открыты!")));
        this.addDrawableChild(new GVButton(x2, y1 + 74, BUTTON_WIDTH, BUTTON_HEIGHT, 
                Text.literal("🌀 Шейдеры"), 
                () -> sendMessage("🌀 Шейдеры открыты!")));

        // ===== ИНТЕРФЕЙС (6 функций) =====
        int x3 = cx + 80;
        this.addDrawableChild(new GVButton(x3, y1 + 20, BUTTON_WIDTH, BUTTON_HEIGHT, 
                Text.literal("📈 FPS"), 
                () -> sendMessage("📈 FPS открыт!")));
        this.addDrawableChild(new GVButton(x3, y1 + 38, BUTTON_WIDTH, BUTTON_HEIGHT, 
                Text.literal("📍 Координаты"), 
                () -> sendMessage("📍 Координаты открыты!")));
        this.addDrawableChild(new GVButton(x3, y1 + 56, BUTTON_WIDTH, BUTTON_HEIGHT, 
                Text.literal("🧭 Компас"), 
                () -> sendMessage("🧭 Компас открыт!")));
        this.addDrawableChild(new GVButton(x3, y1 + 74, BUTTON_WIDTH, BUTTON_HEIGHT, 
                Text.literal("🕐 Часы"), 
                () -> sendMessage("🕐 Часы открыты!")));

        // Закрыть
        this.addDrawableChild(new GVButton(
            this.windowX + WINDOW_WIDTH / 2 - 30,
            this.windowY + WINDOW_HEIGHT - 22,
            60, 16,
            Text.literal("✕ Закрыть"),
            0xFFFF4444,
            () -> this.close()
        ));
    }

    private void sendMessage(String msg) {
        if (client.player != null) {
            client.player.sendMessage(Text.literal("§5[GV] " + msg), false);
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, this.width, this.height, 0xDD0A0510);

        int wx = this.windowX;
        int wy = this.windowY;
        int ww = WINDOW_WIDTH;
        int wh = WINDOW_HEIGHT;

        drawRoundedRect(context, wx, wy, ww, wh, 12, 0xCC1A0A2A);
        drawRoundedBorder(context, wx, wy, ww, wh, 12, 0x8844CCFF);
        drawRoundedBorder(context, wx + 2, wy + 2, ww - 4, wh - 4, 10, 0x444466AA);

        for (int i = 0; i < 25; i++) {
            int alpha = 18 - (int)(i * 0.5);
            context.fill(wx + 15, wy + 4 + i, wx + ww - 15, wy + 5 + i, (alpha << 24) | 0x9966FF);
        }

        context.drawText(
            textRenderer,
            Text.literal("⚡ GOBALT VISUAL").formatted(Formatting.BOLD, Formatting.LIGHT_PURPLE),
            wx + 16, wy + 8, 0xCC99FF, false
        );

        context.drawText(
            textRenderer,
            Text.literal("v2.0 • нажми P").formatted(Formatting.DARK_GRAY),
            wx + 16, wy + 20, 0x666688, false
        );

        context.fill(wx + 14, wy + 32, wx + ww - 14, wy + 33, 0x334466AA);

        super.render(context, mouseX, mouseY, delta);
    }

    private void drawRoundedRect(DrawContext context, int x, int y, int w, int h, int r, int color) {
        context.fill(x + r, y, x + w - r, y + h, color);
        context.fill(x, y + r, x + w, y + h - r, color);
        context.fill(x, y, x + r, y + r, color);
        context.fill(x + w - r, y, x + w, y + r, color);
        context.fill(x, y + h - r, x + r, y + h, color);
        context.fill(x + w - r, y + h - r, x + w, y + h, color);
    }

    private void drawRoundedBorder(DrawContext context, int x, int y, int w, int h, int r, int color) {
        context.fill(x + r, y, x + w - r, y + 1, color);
        context.fill(x + r, y + h - 1, x + w - r, y + h, color);
        context.fill(x, y + r, x + 1, y + h - r, color);
        context.fill(x + w - 1, y + r, x + w, y + h - r, color);
        context.fill(x, y, x + r, y + 1, color);
        context.fill(x + w - r, y, x + w, y + 1, color);
        context.fill(x, y + h - 1, x + r, y + h, color);
        context.fill(x + w - r, y + h - 1, x + w, y + h, color);
    }
}
