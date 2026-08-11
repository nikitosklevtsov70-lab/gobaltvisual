package com.example.gobaltvisual.gui.widgets;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ScrollablePanel {
    private final int x, y, width, height;
    private final List<ClickableWidget> widgets = new ArrayList<>();
    private int scrollOffset = 0;
    private int maxScroll = 0;
    private final int itemHeight = 20;
    private final int gap = 2;

    public ScrollablePanel(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void addWidget(ClickableWidget widget) {
        widgets.add(widget);
        recalculateScroll();
    }

    public void addWidgets(List<ClickableWidget> widgets) {
        this.widgets.addAll(widgets);
        recalculateScroll();
    }

    public void clear() {
        widgets.clear();
        scrollOffset = 0;
        maxScroll = 0;
    }

    private void recalculateScroll() {
        int totalHeight = widgets.size() * (itemHeight + gap);
        maxScroll = Math.max(0, totalHeight - height + 20);
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Рамка панели
        context.fill(x, y, x + width, y + height, 0xCC0A192F);
        context.drawBorder(x, y, width, height, 0xFF00D4FF);

        // Обрезаем область рисования (для скролла)
        context.enableScissor(x + 1, y + 1, x + width - 1, y + height - 1);

        int currentY = y + 4 - scrollOffset;
        for (Widget widget : widgets) {
            if (widget instanceof ClickableWidget) {
                ClickableWidget w = (ClickableWidget) widget;
                w.setX(x + 4);
                w.setY(currentY);
                w.setWidth(width - 8);
                w.setHeight(itemHeight);
                w.render(context, mouseX, mouseY, delta);
                currentY += itemHeight + gap;
            }
        }

        context.disableScissor();

        // Индикатор скролла (полоса справа)
        if (maxScroll > 0) {
            int scrollBarHeight = Math.max(20, height * height / (height + maxScroll));
            int scrollBarY = y + (height - scrollBarHeight) * scrollOffset / maxScroll;
            context.fill(x + width - 4, scrollBarY, x + width - 1, scrollBarY + scrollBarHeight, 0xFF00D4FF);
        }
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height) {
            scrollOffset = (int) Math.max(0, Math.min(maxScroll, scrollOffset - amount * 10));
            return true;
        }
        return false;
    }

    public List<ClickableWidget> getWidgets() {
        return widgets;
    }

    public void setScrollOffset(int offset) {
        this.scrollOffset = Math.max(0, Math.min(maxScroll, offset));
    }
}