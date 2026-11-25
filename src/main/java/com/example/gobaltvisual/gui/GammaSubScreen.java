/*
package com.example.gobaltvisual.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

public class GammaSubScreen extends Screen {
    private final Screen parentScreen;

    public GammaSubScreen(Screen parent) {
        super(Text.literal("Настройка Гаммы"));
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

        // Ползунок Гаммы
        double currentGamma = MinecraftClient.getInstance().options.getGamma().getValue();
        this.addDrawableChild(new SliderWidget(
            menuX + 20, menuY + 60, menuWidth - 40, 20,
            Text.literal("Гамма: " + String.format("%.1f", currentGamma)),
            currentGamma
        ) {
            @Override
            protected void updateMessage() {
                this.setMessage(Text.literal("Гамма: " + String.format("%.1f", this.value)));
            }

            @Override
            protected void applyValue() {
                double val = Math.max(0.1, Math.min(15.0, this.value));
                MinecraftClient.getInstance().options.getGamma().setValue(val);
            }
        });

        // Кнопка "Назад"
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("← Назад"),
            btn -> this.close()
        ).dimensions(menuX + menuWidth / 2 - 40, menuY + 100, 80, 20).build());
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

        // Рисуем заголовок
        context.drawTextWithShadow(
            this.textRenderer, 
            Text.literal("Гамма"), 
            menuX + menuWidth / 2 - 20, 
            menuY + 20, 
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
