package com.example.gobaltvisual.gui.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class GobaltButton extends ButtonWidget {
    private final int bgColor = 0xFF2A2A4A;
    private final int bgHoverColor = 0xFF444466;
    private final int textColor = 0xFFFFFF;
    private final int textHoverColor = 0xFFAAFF;

    public GobaltButton(int x, int y, int width, int height, Text message, PressAction onPress) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION_SUPPLIER);
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        boolean hovered = this.isHovered();

        int color = hovered ? bgHoverColor : bgColor;
        context.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), color);
        context.drawBorder(getX(), getY(), getWidth(), getHeight(), 0xFF4488FF);

        int colorText = hovered ? textHoverColor : textColor;
        context.drawTextWithShadow(
                MinecraftClient.getInstance().textRenderer,
                this.getMessage(),
                getX() + getWidth() / 2 - MinecraftClient.getInstance().textRenderer.getWidth(this.getMessage()) / 2,
                getY() + (getHeight() - 8) / 2,
                colorText
        );
    }
}
