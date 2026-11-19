
package com.example.gobaltvisual.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class RoundedButtonWidget extends ButtonWidget {

    private static final int BACKGROUND = 0xE61B1724;
    private static final int HOVER_BACKGROUND = 0xF02E2140;
    private static final int BORDER = 0xFF51416D;
    private static final int HOVER_BORDER = 0xFFB58AFF;
    private static final int TEXT_COLOR = 0xFFE8E0F5;
    private static final int DISABLED_TEXT = 0xFF77717F;

    public RoundedButtonWidget(
            int x, int y, int width, int height,
            Text message, PressAction onPress
    ) {
        super(x, y, width, height, message, onPress,
                DEFAULT_NARRATION_SUPPLIER);
    }

    @Override
    public void renderWidget(
            DrawContext context, int mouseX, int mouseY, float delta
    ) {
        boolean hovered = this.isHovered();
        boolean enabled = this.active;

        int background = hovered ? HOVER_BACKGROUND : BACKGROUND;
        int border = hovered ? HOVER_BORDER : BORDER;
        int textColor = !enabled ? DISABLED_TEXT : TEXT_COLOR;

        int x = this.getX();
        int y = this.getY();
        int width = this.getWidth();
        int height = this.getHeight();

        // Тёмный фон кнопки
        context.fill(x + 2, y, x + width - 2, y + height, background);
        context.fill(x, y + 2, x + width, y + height - 2, background);

        // Тонкая рамка с фиолетовым акцентом
        context.fill(x + 2, y, x + width - 2, y + 1, border);
        context.fill(x + 2, y + height - 1,
                x + width - 2, y + height, border);
        context.fill(x, y + 2, x + 1, y + height - 2, border);
        context.fill(x + width - 1, y + 2,
                x + width, y + height - 2, border);

        // Текст по центру
        MinecraftClient client = MinecraftClient.getInstance();
        context.drawCenteredTextWithShadow(
                client.textRenderer,
                this.getMessage(),
                x + width / 2,
                y + (height - 8) / 2,
                textColor
        );
    }
}
