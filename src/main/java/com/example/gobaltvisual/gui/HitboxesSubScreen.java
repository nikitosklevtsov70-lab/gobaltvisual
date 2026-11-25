/*
package com.example.gobaltvisual.gui;

import com.example.gobaltvisual.config.HitboxesConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

public class HitboxesSubScreen extends Screen {
    private final Screen parentScreen;

    public HitboxesSubScreen(Screen parent) {
        super(Text.literal("Настройки Хитбоксов"));
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

        // Кнопка ВКЛ/ВЫКЛ
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Хитбоксы: " + (HitboxesConfig.data.enabled ? "ВКЛ" : "ВЫКЛ")),
            btn -> {
                HitboxesConfig.data.enabled = !HitboxesConfig.data.enabled;
                HitboxesConfig.save();
                btn.setMessage(Text.literal("Хитбоксы: " + (HitboxesConfig.data.enabled ? "ВКЛ" : "ВЫКЛ")));
            }
        ).dimensions(menuX + menuWidth / 2 - 60, menuY + 40, 120, 20).build());

        // Ползунки RGBA (с сохранением в реальном времени)
        String[] labels = {"Красный (R)", "Зелёный (G)", "Синий (B)", "Прозрачность (A)"};
        float[] values = {HitboxesConfig.data.red, HitboxesConfig.data.green, HitboxesConfig.data.blue, HitboxesConfig.data.alpha};
        float[] minVals = {0.0f, 0.0f, 0.0f, 0.0f};
        float[] maxVals = {1.0f, 1.0f, 1.0f, 1.0f};
        int startY = menuY + 80;

        for (int i = 0; i < 4; i++) {
            final int index = i;
            this.addDrawableChild(new SliderWidget(
                menuX + 40, startY + i * 30, menuWidth - 80, 20,
                Text.literal(labels[i] + ": " + String.format("%.2f", values[i])),
                values[i]
            ) {
                @Override
                protected void updateMessage() {
                    float val = (float) this.value;
                    this.setMessage(Text.literal(labels[index] + ": " + String.format("%.2f", val)));
                }

                @Override
                protected void applyValue() {
                    float val = (float) this.value;
                    switch (index) {
                        case 0: HitboxesConfig.data.red = val; break;
                        case 1: HitboxesConfig.data.green = val; break;
                        case 2: HitboxesConfig.data.blue = val; break;
                        case 3: HitboxesConfig.data.alpha = val; break;
                    }
                    HitboxesConfig.save();
                }
            });
        }

        // Маленький квадрат для превью цвета
        // Кнопка "Назад"
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
            Text.literal("Хитбоксы"),
            menuX + menuWidth / 2 - 30,
            menuY + 10,
            0xFFFFFF
        );

        // Рисуем квадрат превью
        int previewSize = 30;
        int previewX = menuX + menuWidth - 60;
        int previewY = menuY + 40;
        int color = (int)(HitboxesConfig.data.alpha * 255) << 24 |
                    (int)(HitboxesConfig.data.red * 255) << 16 |
                    (int)(HitboxesConfig.data.green * 255) << 8 |
                    (int)(HitboxesConfig.data.blue * 255);
        context.fill(previewX, previewY, previewX + previewSize, previewY + previewSize, color);
        context.drawBorder(previewX, previewY, previewSize, previewSize, 0xFFFFFFFF);

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        this.client.setScreen(this.parentScreen);
    }
}
*/
