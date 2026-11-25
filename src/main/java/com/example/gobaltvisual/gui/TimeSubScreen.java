/*
package com.example.gobaltvisual.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class TimeSubScreen extends Screen {
    private final Screen parentScreen;

    public TimeSubScreen(Screen parent) {
        super(Text.literal("Кастомное время"));
        this.parentScreen = parent;
    }

    @Override
    protected void init() {
        int sw = this.width;
        int sh = this.height;

        int menuWidth = (int)(sw * 0.75);
        int menuHeight = (int)(sh * 0.70);
        int menuX = (sw - menuWidth) / 2;
        int menuY = (sh - menuHeight) / 2;

        // Кнопки выбора времени
        String[] times = {"День", "Ночь", "Закат", "Рассвет", "Выкл."};
        long[] values = {6000, 18000, 12000, 0, -1};
        int buttonWidth = 100;
        int buttonHeight = 20;
        int startX = menuX + (menuWidth - buttonWidth) / 2;
        int startY = menuY + 40;

        for (int i = 0; i < times.length; i++) {
            final long val = values[i];
            final String label = times[i];
            this.addDrawableChild(ButtonWidget.builder(
                Text.literal(label),
                btn -> {
                    if (MinecraftClient.getInstance().world != null) {
                        // Меняем время в двух местах одновременно
                        MinecraftClient.getInstance().world.setTimeOfDay(val);
                        MinecraftClient.getInstance().world.getLevelProperties().setTimeOfDay(val);
                        this.close();
                    }
                }
            ).dimensions(startX, startY + i * (buttonHeight + 10), buttonWidth, buttonHeight).build());
        }

        // Кнопка "Назад" (слева)
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("← Назад"),
            btn -> this.close()
        ).dimensions(menuX + 20, menuY + menuHeight - 60, 80, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);

        int sw = this.width;
        int sh = this.height;
        int menuWidth = (int)(sw * 0.75);
        int menuHeight = (int)(sh * 0.70);
        int menuX = (sw - menuWidth) / 2;
        int menuY = (sh - menuHeight) / 2;

        context.drawTextWithShadow(
            this.textRenderer,
            Text.literal("Выбери время"),
            menuX + menuWidth / 2 - 40,
            menuY + 10,
            0xFFFFFF
        );

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        this.client.setScreen(this.parentScreen);
    }
}
*/
