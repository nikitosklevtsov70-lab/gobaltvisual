package com.example.gobaltvisual.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

public class FlatToggle extends ButtonWidget {
    private boolean toggled;
    private float toggleProgress;
    private float hoverProgress = 0.0f;
    private long lastTime = System.nanoTime();

    public FlatToggle(int x, int y, int width, int height, Text message, boolean initialState, PressAction onPress) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION_SUPPLIER);
        this.toggled = initialState;
        this.toggleProgress = initialState ? 1.0f : 0.0f;
    }

    public boolean isToggled() {
        return toggled;
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        // 1. Независимый от FPS таймер в наносекундах
        long now = System.nanoTime();
        float dt = (now - lastTime) / 1_000_000_000.0f;
        lastTime = now;
        if (dt > 0.1f) dt = 0.016f;

        // 2. Плавная анимация переключения и наведения
        float targetToggle = this.toggled ? 1.0f : 0.0f;
        float toggleFactor = 1.0f - (float) Math.exp(-14.0f * dt);
        this.toggleProgress = MathHelper.lerp(toggleFactor, this.toggleProgress, targetToggle);

        float targetHover = this.isHovered() ? 1.0f : 0.0f;
        float hoverFactor = 1.0f - (float) Math.exp(-12.0f * dt);
        this.hoverProgress = MathHelper.lerp(hoverFactor, this.hoverProgress, targetHover);

        int x = this.getX();
        int y = this.getY();

        // 3. Легкая подложка всей строки при наведении (чтобы было удобно целиться мышкой)
        if (this.hoverProgress > 0.01f) {
            int rowBg = lerpColor(0x001E1433, 0x5524173D, this.hoverProgress);
            context.fill(x - 3, y + 1, x + this.width + 3, y + this.height - 1, rowBg);
        }

        // 4. Субпиксельный сдвиг и подсветка текста слева
        int textColor = lerpColor(0xFFCCCCCC, 0xFFFFFFFF, Math.max(this.hoverProgress, this.toggleProgress));
        context.getMatrices().push();
        context.getMatrices().translate(this.hoverProgress * 2.0f, 0.0f, 0.0f);
        context.drawTextWithShadow(
                MinecraftClient.getInstance().textRenderer,
                this.getMessage(),
                x,
                y + (this.height - 8) / 2,
                textColor
        );
        context.getMatrices().pop();

        // 5. Сам тумблер справа в стиле "Пурпурный космос"
        int toggleWidth = 28;
        int toggleHeight = 14;
        int toggleX = x + this.width - toggleWidth;
        int toggleY = y + (this.height - toggleHeight) / 2;

        int bgColor = lerpColor(0x90120E21, 0xFF7A00FF, this.toggleProgress);
        int borderColor = lerpColor(0x554A3B69, 0xFFE0AAFF, Math.max(this.toggleProgress, this.hoverProgress * 0.7f));

        context.fill(toggleX, toggleY, toggleX + toggleWidth, toggleY + toggleHeight, bgColor);
        context.drawBorder(toggleX, toggleY, toggleWidth, toggleHeight, borderColor);

        // 6. Субпиксельный плавный перелет бегунка слева направо
        float knobTravel = toggleWidth - 14.0f; // Дистанция хода (28 - 10 - 2*2 = 14px)
        float knobX = toggleX + 2.0f + (knobTravel * this.toggleProgress);
        int knobColor = lerpColor(0xFF9D8EC7, 0xFFFFFFFF, this.toggleProgress);

        context.getMatrices().push();
        context.getMatrices().translate(knobX, toggleY + 2.0f, 0.0f);
        context.fill(0, 0, 10, toggleHeight - 4, knobColor);
        context.getMatrices().pop();
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.toggled = !this.toggled;
        super.onClick(mouseX, mouseY);
    }

    private static int lerpColor(int c1, int c2, float t) {
        int a1 = (c1 >> 24) & 0xFF, r1 = (c1 >> 16) & 0xFF, g1 = (c1 >> 8) & 0xFF, b1 = c1 & 0xFF;
        int a2 = (c2 >> 24) & 0xFF, r2 = (c2 >> 16) & 0xFF, g2 = (c2 >> 8) & 0xFF, b2 = c2 & 0xFF;
        int a = (int) (a1 + (a2 - a1) * t);
        int r = (int) (r1 + (r2 - r1) * t);
        int g = (int) (g1 + (g2 - g1) * t);
        int b = (int) (b1 + (b2 - b1) * t);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }
}
