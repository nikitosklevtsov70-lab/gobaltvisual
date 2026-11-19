package com.example.gobaltvisual.gui.submenus.pvp;

import com.example.gobaltvisual.config.HitboxesConfig;
import com.example.gobaltvisual.config.TogglesConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class SubmenuHitboxes extends Screen {
    private final Screen parent;

    // 🔥 Палитра: имя → ARGB
    private static final int[][] PALETTE = {
            {0xFFFF0000, 0xFF00FF00, 0xFF0000FF, 0xFFFFFF00},
            {0xFFFF00FF, 0xFF00FFFF, 0xFFFFFFFF, 0xFFFF8000}
    };

    public SubmenuHitboxes(Screen parent) {
        super(Text.literal("Настройка хитбоксов"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int cx = this.width / 2;
        int cy = this.height / 2;

        // Заголовок
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("🎯 НАСТРОЙКА ХИТБОКСОВ").formatted(Formatting.BOLD, Formatting.AQUA),
                button -> {}
        ).dimensions(cx - 100, cy - 100, 200, 20).build());

        // Кнопка ВКЛ/ВЫКЛ
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal(HitboxesConfig.data.enabled ? "🟢 Включены" : "🔴 Выключены")
                        .formatted(HitboxesConfig.data.enabled ? Formatting.GREEN : Formatting.RED),
                button -> {
                    HitboxesConfig.data.enabled = !HitboxesConfig.data.enabled;
                    TogglesConfig.set("ПВП", "хитбоксы", HitboxesConfig.data.enabled);
                    client.setScreen(new SubmenuHitboxes(parent));
                }
        ).dimensions(cx - 60, cy - 70, 120, 20).build());

        // 🔥 Палитра — 2 ряда по 4 кнопки
        int btnSize = 28;
        int spacing = 8;
        int paletteWidth = 4 * btnSize + 3 * spacing;  // 4*28 + 3*8 = 136
        int startX = cx - paletteWidth / 2;
        int startY = cy - 30;

        for (int row = 0; row < PALETTE.length; row++) {
            for (int col = 0; col < PALETTE[row].length; col++) {
                final int argb = PALETTE[row][col];
                int bx = startX + col * (btnSize + spacing);
                int by = startY + row * (btnSize + spacing);

                this.addDrawableChild(ButtonWidget.builder(
                        Text.literal(""),  // без текста — только цвет
                        button -> HitboxesConfig.setColor(argb)
                ).dimensions(bx, by, btnSize, btnSize).build());
            }
        }

        // Кнопка "Назад"
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("🔙 Назад").formatted(Formatting.GRAY),
                button -> client.setScreen(parent)
        ).dimensions(cx - 40, cy + 60, 80, 20).build());
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Фон
        context.fill(0, 0, this.width, this.height, 0xCC0A192F);

        // 🔥 Рисуем цветные квадраты ПОВЕРХ кнопок палитры
        int cx = this.width / 2;
        int cy = this.height / 2;
        int btnSize = 28;
        int spacing = 8;
        int paletteWidth = 4 * btnSize + 3 * spacing;
        int startX = cx - paletteWidth / 2;
        int startY = cy - 30;

        for (int row = 0; row < PALETTE.length; row++) {
            for (int col = 0; col < PALETTE[row].length; col++) {
                int argb = PALETTE[row][col];
                int bx = startX + col * (btnSize + spacing);
                int by = startY + row * (btnSize + spacing);

                // Цветной квадрат
                context.fill(bx + 2, by + 2, bx + btnSize - 2, by + btnSize - 2, argb);

                // Обводка: если это текущий цвет — белая, иначе тёмная
                int currentColor = HitboxesConfig.getColor();
                int borderColor = (argb == currentColor) ? 0xFFFFFFFF : 0xFF333333;
                context.drawBorder(bx, by, btnSize, btnSize, borderColor);
            }
        }

        // Текст-подсказка под палитрой
        context.drawCenteredTextWithShadow(
                this.textRenderer,
                Text.literal("Текущий цвет:"),
                cx,
                cy + 25,
                0xFFCCCCCC
        );

        // Превью текущего цвета
        int previewSize = 20;
        int previewX = cx - previewSize / 2;
        int previewY = cy + 38;
        int current = HitboxesConfig.getColor();
        context.fill(previewX, previewY, previewX + previewSize, previewY + previewSize, current);
        context.drawBorder(previewX, previewY, previewSize, previewSize, 0xFFFFFFFF);

        super.render(context, mouseX, mouseY, delta);
    }
}