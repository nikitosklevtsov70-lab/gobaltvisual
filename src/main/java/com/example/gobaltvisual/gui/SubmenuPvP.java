package com.example.gobaltvisual.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class SubmenuPvP extends Screen {
    private final Screen parent;

    public SubmenuPvP(Screen parent) {
        super(Text.literal("ПВП - Gobalt Visual"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int cx = this.width / 2;
        int cy = this.height / 2 - 80;

        // ===== ЗАГОЛОВОК =====
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("⚔️ НАСТРОЙКИ ПВП").formatted(Formatting.BOLD, Formatting.AQUA),
                button -> {}
        ).dimensions(cx - 90, cy - 20, 180, 20).build());

        // ===== ХИТБОКСЫ =====
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("🎯 Хитбоксы").formatted(Formatting.GREEN),
                button -> {
                    if (client.player != null) {
                        client.player.sendMessage(Text.literal("§a[GV] 🎯 Настройка хитбоксов открыта!"), false);
                    }
                }
        ).dimensions(cx - 80, cy + 20, 160, 22).build());

        // ===== ПРИЦЕЛ =====
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("🎯 Прицел").formatted(Formatting.GOLD),
                button -> {
                    if (client.player != null) {
                        client.player.sendMessage(Text.literal("§6[GV] 🎯 Настройка прицела открыта!"), false);
                    }
                }
        ).dimensions(cx - 80, cy + 50, 160, 22).build());

        // ===== KILL STREAK =====
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("☠️ Kill Streak").formatted(Formatting.RED),
                button -> {
                    if (client.player != null) {
                        client.player.sendMessage(Text.literal("§c[GV] ☠️ Настройка Kill Streak открыта!"), false);
                    }
                }
        ).dimensions(cx - 80, cy + 80, 160, 22).build());

        // ===== ОТБРАСЫВАНИЕ =====
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("👟 Отбрасывание").formatted(Formatting.LIGHT_PURPLE),
                button -> {
                    if (client.player != null) {
                        client.player.sendMessage(Text.literal("§d[GV] 👟 Настройка отбрасывания открыта!"), false);
                    }
                }
        ).dimensions(cx - 80, cy + 110, 160, 22).build());

        // ===== КНОПКА НАЗАД =====
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("🔙 Назад").formatted(Formatting.GRAY),
                button -> client.setScreen(parent)
        ).dimensions(cx - 80, cy + 160, 160, 22).build());
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, this.width, this.height, 0xCC0A192F);

        int wx = this.width / 2 - 110;
        int wy = this.height / 2 - 120;
        int ww = 220;
        int wh = 280;

        context.fill(wx, wy, wx + ww, wy + wh, 0xCC1A2A4A);
        context.drawBorder(wx, wy, ww, wh, 0xFF00D4FF);
        context.fill(wx + 10, wy + 22, wx + ww - 10, wy + 23, 0xFF00D4FF);

        context.drawText(textRenderer,
                Text.literal("v2.0 (a0)").formatted(Formatting.DARK_GRAY),
                wx + ww - 50, wy + wh - 12, 0xFF666666, false);

        super.render(context, mouseX, mouseY, delta);
    }
}
