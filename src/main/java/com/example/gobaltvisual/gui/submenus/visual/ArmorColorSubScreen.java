package com.example.gobaltvisual.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

public class ArmorColorSubScreen extends Screen {
    private final Screen parentScreen;
    private final String selectedTemplate;
    private int menuX, menuY, menuWidth, menuHeight;

    // Переменные для текущего цвета
    private float r = 0.5f;
    private float g = 0.5f;
    private float b = 0.5f;

    public ArmorColorSubScreen(Screen parent, String template) {
        super(Text.literal("Цвет брони"));
        this.parentScreen = parent;
        this.selectedTemplate = template;
    }

    @Override
    protected void init() {
        int sw = this.width;
        int sh = this.height;

        menuWidth = (int)(sw * 0.75);
        menuHeight = (int)(sh * 0.70);
        menuX = (sw - menuWidth) / 2;
        menuY = (sh - menuHeight) / 2;

        // Ползунки RGB (с сохранением в переменные)
        String[] labels = {"Красный (R)", "Зелёный (G)", "Синий (B)"};
        int startY = menuY + 40;

        for (int i = 0; i < 3; i++) {
            final int index = i;
            float initialValue = (i == 0) ? r : (i == 1) ? g : b;
            this.addDrawableChild(new SliderWidget(
                menuX + 40, startY + i * 30, menuWidth - 80, 20,
                Text.literal(labels[i] + ": " + String.format("%.2f", initialValue)),
                initialValue
            ) {
                @Override
                protected void updateMessage() {
                    float val = (float) this.value;
                    this.setMessage(Text.literal(labels[index] + ": " + String.format("%.2f", val)));
                }

                @Override
                protected void applyValue() {
                    float val = (float) this.value;
                    if (index == 0) r = val;
                    else if (index == 1) g = val;
                    else if (index == 2) b = val;
                }
            });
        }

        // Выбор части брони
        String[] parts = {"Шлем", "Нагрудник", "Штаны", "Ботинки", "Всё"};
        int partY = startY + 100;
        int partWidth = 60;
        int partGap = 5;
        int totalPartWidth = (partWidth * 5) + (partGap * 4);
        int partStartX = menuX + (menuWidth - totalPartWidth) / 2;

        for (int i = 0; i < parts.length; i++) {
            final String partName = parts[i];
            this.addDrawableChild(ButtonWidget.builder(
                Text.literal(partName),
                b -> {
                    System.out.println("[GV] Часть брони: " + partName + " | Шаблон: " + selectedTemplate);
                    this.close();
                }
            ).dimensions(partStartX + i * (partWidth + partGap), partY, partWidth, 18).build());
        }

        // Кнопка "Назад"
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("← Назад"),
            b -> this.close()
        ).dimensions(menuX + 20, menuY + menuHeight - 50, 80, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);

        context.drawTextWithShadow(
            this.textRenderer,
            Text.literal("Цвет для: " + selectedTemplate),
            menuX + menuWidth / 2 - 60,
            menuY + 10,
            0xFFFFFF
        );

        // --- КВАДРАТ ПРЕВЬЮ (обновляется в реальном времени) ---
        int previewSize = 24;
        int previewX = menuX + menuWidth - previewSize - 15;
        int previewY = menuY + 8;
        int color = (int)(255) << 24 | ((int)(r * 255)) << 16 | ((int)(g * 255)) << 8 | (int)(b * 255);
        context.fill(previewX, previewY, previewX + previewSize, previewY + previewSize, color);
        context.drawBorder(previewX, previewY, previewSize, previewSize, 0xFFFFFFFF);

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        this.client.setScreen(this.parentScreen);
    }
}
