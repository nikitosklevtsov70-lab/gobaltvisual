package com.example.gobaltvisual.gui.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class GVButton extends ClickableWidget {
    private final Runnable action;
    private int color = 0xFFFFFFFF;
    private boolean hovered = false;

    public GVButton(int x, int y, int width, int height, Text message, Runnable action) {
        super(x, y, width, height, message);
        this.action = action;
    }

    public GVButton(int x, int y, int width, int height, Text message, int color, Runnable action) {
        super(x, y, width, height, message);
        this.action = action;
        this.color = color;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        this.hovered = mouseX >= this.getX() && mouseX <= this.getX() + this.width &&
                       mouseY >= this.getY() && mouseY <= this.getY() + this.height;

        int x = this.getX();
        int y = this.getY();
        int w = this.width;
        int h = this.height;

        // Фон
        int bgColor = 0x33000000;
        if (hovered) {
            bgColor = 0x66000000;
        }
        context.fill(x, y, x + w, y + h, bgColor);

        // Левая полоска при наведении
        if (hovered) {
            context.fill(x, y, x + 2, y + h, 0xCC6633FF);
        }

        // Текст
        int textColor = hovered ? 0xFFFFFFFF : 0xCCCCCCCC;
        String text = this.getMessage().getString();
        context.drawText(MinecraftClient.getInstance().textRenderer,
                Text.literal(text).formatted(Formatting.WHITE),
                x + 8, y + (h - 8) / 2, textColor, false);

        // Стрелка при наведении
        if (hovered) {
            context.drawText(MinecraftClient.getInstance().textRenderer,
                    Text.literal("›").formatted(Formatting.LIGHT_PURPLE),
                    x + w - 12, y + (h - 8) / 2, 0xFFFF55FF, false);
        }
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        // Пустая реализация для совместимости
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (action != null) {
            action.run();
        }
    }
}
