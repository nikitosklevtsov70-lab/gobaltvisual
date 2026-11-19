package com.example.gobaltvisual.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import java.util.ArrayList;
import java.util.List;

import com.example.gobaltvisual.config.TogglesConfig;
import com.example.gobaltvisual.config.HitboxesConfig;
import com.example.gobaltvisual.gui.Column;
import com.example.gobaltvisual.gui.SubmenuScreen;

import static com.example.gobaltvisual.gui.Colors.*;
import static com.example.gobaltvisual.gui.RenderUtils.*;

public class GobaltVisualScreen extends Screen {

    private final List<Column> columns = new ArrayList<>();
    private final List<CachedCircle> circleCache = new ArrayList<>();

    private int itemHeight = 14;
    private int columnHeight = 120;
    private int columnWidth = 110;
    private int spacing = 8;

    private int columnsStartX, columnsStartY;
    private int scrollX = -20;
    private int maxScrollX = 0;

    private int menuX, menuY, menuWidth, menuHeight;

    // 🔥 Кешированный тумблер
    private static class CachedCircle {
        int x, y, radius, color;
        float alpha;
        boolean visible;
    }

    public GobaltVisualScreen() {
        super(Text.literal("Gobalt Visual"));

        columns.add(new Column("ПВП", new String[]{"хитбоксы", "Чамы", "Кастом кристал", "Руки", "цвет хита", "Блинк"}));
        columns.add(new Column("ВИЗУАЛ", new String[]{"Кастом время", "Яркость", "Кастом погода", "Фейк игрок", "Цвет неба", "китай. шляпа", "аним. удара", "оптимизация"}));
        columns.add(new Column("ПАРТИКЛ", new String[]{"Стрелы", "Блок частицы", "Частицы урона", "Хит-частицы", "Джамп частицы", "Килл эффект", "След частиц", "Рофл-частицы"}));
        columns.add(new Column("КЛИЕНТ", new String[]{"Стрелы", "Хит-саунд", "Кил-стрик", "Комбо звук", "Фейк фпс", "тэги"}));
        columns.add(new Column("НАСТРОЙКИ", new String[]{"Конфиги", "Экран загрузки", "Гуи-звуки", "Интерфейс", "Язык", "Кастом ник", "NoRender"}));
        columns.add(new Column("ДРУЗЬЯ", new String[]{"Список друзей", "Добавить", "Удалить"}));

        // 🔥 Загружаем состояние из конфига
        for (Column column : columns) {
            for (int j = 0; j < column.items.length; j++) {
                boolean saved = TogglesConfig.get(column.title, column.items[j]);
                column.enabled[j] = saved;
                column.alpha[j] = saved ? 1f : 0f;
                column.targetAlpha[j] = saved ? 1f : 0f;
            }
        }
    }

    @Override
    protected void init() {
        super.init();

        // 🔥 Синхронизируем конфиг ОДИН РАЗ при открытии меню, а не каждый кадр!
        HitboxesConfig.sync();

        menuWidth = Math.min(850, this.width - 40);
        menuHeight = Math.min(400, this.height - 40);
        menuX = (this.width - menuWidth) / 2;
        menuY = (this.height - menuHeight) / 2;

        int totalWidth = columns.size() * (columnWidth + spacing) - spacing;
        columnsStartX = (this.width - totalWidth) / 2;
        columnsStartY = (this.height - columnHeight) / 2 - 10;

        int contentWidth = columns.size() * (columnWidth + spacing) - spacing;
        int visibleWidth = this.width - 60;
        maxScrollX = Math.max(0, contentWidth - visibleWidth + 20);

        for (Column column : columns) {
            int contentHeight = column.items.length * itemHeight + 20;
            column.maxScroll = Math.max(0, contentHeight - columnHeight);
        }

        scrollX = -20;

        // 🔥 Добавляем кнопку перехода на НОВОЕ меню
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("✨ Новый дизайн"),
                button -> {
                    System.out.println("КНОПКА НАЖАТА! Открываю новое меню...");
                    if (this.client != null) {
                        this.client.setScreen(new NewPvPMenu(this));
                    }
                }
        ).dimensions(this.width - 110, this.height - 30, 100, 20).build());
    }

    private void updateCircleCache() {
        circleCache.clear();
        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);
            int colX = columnsStartX + i * (columnWidth + spacing) + scrollX;
            int topY = columnsStartY;
            int bottomY = topY + columnHeight;

            for (int j = 0; j < column.items.length; j++) {
                int rowY = topY + 22 + j * itemHeight + column.scrollOffset;
                if (rowY < bottomY - 4 && rowY >= topY + 12) {
                    CachedCircle circle = new CachedCircle();
                    circle.x = colX + columnWidth - 10;
                    circle.y = rowY + 1;
                    circle.radius = 3;
                    circle.color = CHECKBOX_ON;
                    circle.alpha = column.alpha[j];
                    circle.visible = true;
                    circleCache.add(circle);
                }
            }
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // 🔥 Анимация тумблеров
        for (Column column : columns) {
            for (int j = 0; j < column.items.length; j++) {
                float speed = 0.15f;
                if (column.alpha[j] < column.targetAlpha[j]) {
                    column.alpha[j] += speed;
                    if (column.alpha[j] > column.targetAlpha[j]) column.alpha[j] = column.targetAlpha[j];
                } else if (column.alpha[j] > column.targetAlpha[j]) {
                    column.alpha[j] -= speed;
                    if (column.alpha[j] < column.targetAlpha[j]) column.alpha[j] = column.targetAlpha[j];
                }
            }
        }

        // 🔥 Всегда обновляем кеш
        updateCircleCache();

        context.getMatrices().push();
        context.getMatrices().translate(scrollX, 0, 0);

        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);
            int colX = columnsStartX + i * (columnWidth + spacing);
            int topY = columnsStartY;
            int bottomY = topY + columnHeight;

            drawRoundedRect(context, colX, topY, columnWidth, columnHeight, 6, COLUMN_BG);
            drawRoundedBorder(context, colX, topY, columnWidth, columnHeight, 6, BORDER_COLUMN);

            context.drawTextWithShadow(this.textRenderer, Text.literal(column.title), colX + 6, topY + 6, TEXT_TITLE);

            for (int j = 0; j < column.items.length; j++) {
                int rowY = topY + 22 + j * itemHeight + column.scrollOffset;

                if (rowY < bottomY - 4 && rowY >= topY + 12) {
                    context.drawTextWithShadow(this.textRenderer, Text.literal(column.items[j]), colX + 6, rowY, TEXT_ITEM);
                }
            }

            if (column.maxScroll > 0) {
                int barHeight = Math.max(12, (columnHeight * columnHeight) / (column.items.length * itemHeight + 10));
                int barY = topY + Math.round(((-column.scrollOffset) / (float) column.maxScroll) * (columnHeight - barHeight));
                context.fill(colX + columnWidth - 3, barY, colX + columnWidth, barY + barHeight, SCROLLBAR);
            }
        }

        context.getMatrices().pop();

        // 🔥 Рисуем тумблеры из кеша
        for (CachedCircle circle : circleCache) {
            if (circle.visible) {
                boolean enabled = circle.alpha > 0.5f;
                drawToggle(context, circle.x - 12, circle.y - 5, enabled, circle.alpha);
            }
        }

        context.drawTextWithShadow(this.textRenderer,
                Text.literal("Скролл: колёсико | Горизонтальный: Shift + колёсико | ПКМ: настройки"),
                10, this.height - 20, TEXT_HINT);

        // 🔥 ОБЯЗАТЕЛЬНО вызываем super.render, чтобы кнопки отрисовались!
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (Screen.hasShiftDown()) {
            scrollX += verticalAmount * 8;
            scrollX = Math.max(-maxScrollX, Math.min(maxScrollX, scrollX));
            return true;
        }

        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);
            int colX = columnsStartX + i * (columnWidth + spacing) + scrollX;
            int topY = columnsStartY;
            int bottomY = topY + columnHeight;

            if (mouseX >= colX && mouseX <= colX + columnWidth &&
                    mouseY >= topY && mouseY <= bottomY) {
                column.scrollOffset += verticalAmount * 5;
                column.scrollOffset = Math.max(-column.maxScroll, Math.min(0, column.scrollOffset));
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);
            int colX = columnsStartX + i * (columnWidth + spacing) + scrollX;
            int topY = columnsStartY;
            int bottomY = topY + columnHeight;

            for (int j = 0; j < column.items.length; j++) {
                int rowY = topY + 22 + j * itemHeight + column.scrollOffset;

                if (rowY < bottomY - 4 && rowY >= topY + 12) {
                    if (mouseX >= colX && mouseX <= colX + columnWidth &&
                            mouseY >= rowY - 2 && mouseY <= rowY + itemHeight) {

                        // 🔥 ПКМ — открываем полноценное подменю
                        if (button == 1) {
                            if (this.client != null) {
                                this.client.setScreen(new SubmenuScreen(this, column.items[j]));
                            }
                            return true;
                        }

                        // 🔥 ЛКМ — переключаем тумблер
                        if (button == 0) {
                            column.enabled[j] = !column.enabled[j];
                            column.targetAlpha[j] = column.enabled[j] ? 1f : 0f;
                            TogglesConfig.set(column.title, column.items[j], column.enabled[j]);
                            return true;
                        }
                    }
                }
            }
        }
        
        // 🔥 Если клик не попал по колонкам, передаем его кнопкам!
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}