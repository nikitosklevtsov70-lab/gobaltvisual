package com.example.gobaltvisual.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class ArmorTrimSubScreen extends Screen {
    private final Screen parentScreen;
    private final String selectedTemplate;
    private int menuX, menuY, menuWidth, menuHeight;

    public ArmorTrimSubScreen(Screen parent, String template) {
        super(Text.literal("Цвет отделки"));
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

        // Полный список материалов отделки
        String[] colors = {
            "Кварц", "Железо", "Незерит", "Лазурит",
            "Алмаз", "Золото", "Изумруд", "Редстоун",
            "Аметист", "Медь", "Сеть"
        };
        
        int buttonWidth = 80;
        int buttonHeight = 18;
        int gap = 10;
        int totalWidth = (buttonWidth * 3) + (gap * 2);
        int startX = menuX + (menuWidth - totalWidth) / 2;
        int startY = menuY + 40;

        for (int i = 0; i < colors.length; i++) {
            final String colorName = colors[i];
            int col = i % 3;
            int row = i / 3;

            int x = startX + col * (buttonWidth + gap);
            int y = startY + row * (buttonHeight + 8);

            this.addDrawableChild(ButtonWidget.builder(
                Text.literal(colorName),
                b -> {
                    System.out.println("[GV] Выбрано: " + colorName);
                    this.close();
                }
            ).dimensions(x, y, buttonWidth, buttonHeight).build());
        }

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
            Text.literal("Цвет отделки"),
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
