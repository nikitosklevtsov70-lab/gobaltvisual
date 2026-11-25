/*
package com.example.gobaltvisual.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ArmorSubScreen extends Screen {
    private final Screen parentScreen;
    private final List<ButtonWidget> buttons = new ArrayList<>();
    private int menuX, menuY, menuWidth, menuHeight;

    public ArmorSubScreen(Screen parent) {
        super(Text.literal("Шаблоны брони"));
        this.parentScreen = parent;
    }

    @Override
    protected void init() {
        int sw = this.width;
        int sh = this.height;

        menuWidth = (int)(sw * 0.75);
        menuHeight = (int)(sh * 0.70);
        menuX = (sw - menuWidth) / 2;
        menuY = (sh - menuHeight) / 2;

        // Кнопка "Назад"
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("← Назад"),
            b -> this.close()
        ).dimensions(menuX + menuWidth - 100, menuY + 5, 80, 20).build());

        // Список шаблонов
        String[] items = {
            "Берег", "Вождь", "Бредина", "Дебри", "Дюна",
            "Искатель", "Око", "Ось", "Поток", "Прилив",
            "Ребро", "Рыло", "Скульптор", "Собиратель", "Страж",
            "Тишина", "Хранитель", "Шпиль"
        };
        
        int buttonWidth = 100;
        int buttonHeight = 18;
        int gap = 10;
        int totalWidth = (buttonWidth * 3) + (gap * 2);
        int startX = menuX + (menuWidth - totalWidth) / 2;
        int startY = menuY + 45;

        for (int i = 0; i < items.length; i++) {
            final String name = items[i];
            int col = i % 3;
            int row = i / 3;

            int x = startX + col * (buttonWidth + gap);
            int y = startY + row * (buttonHeight + 8);

            ButtonWidget btn = ButtonWidget.builder(
                Text.literal(name),
                b -> {
                    // Открываем подменю выбора цвета отделки
                    this.client.setScreen(new ArmorTrimSubScreen(this, name));
                }
            ).dimensions(x, y, buttonWidth, buttonHeight).build();
            buttons.add(btn);
            this.addDrawableChild(btn);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        
        context.drawTextWithShadow(
            this.textRenderer,
            Text.literal("Выбери шаблон"),
            menuX + menuWidth / 2 - 50,
            menuY + 5,
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
