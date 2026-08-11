package com.example.gobaltvisual.gui.animations;

import com.example.gobaltvisual.config.ClientConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class LogoAnimator {
    private static final MinecraftClient client = MinecraftClient.getInstance();

    // ================================================================
    // ТЕКСТ ЛОГОТИПА ДЛЯ МЕНЮ (БЕЗ ФОНА)
    // ================================================================
    private static final Identifier LOGO_TEXTURE = Identifier.of("gobaltvisual", "textures/gui/logo_text.png");

    // РАЗМЕРЫ ТВОЕГО ТЕКСТА (подгони под свою картинку)
    private static final int LOGO_WIDTH = 128;   // Ширина в пикселях
    private static final int LOGO_HEIGHT = 32;   // Высота в пикселях

    // ================================================================
    // ПЕРЕМЕННЫЕ ДЛЯ АНИМАЦИЙ
    // ================================================================
    private static float logoX = 0;
    private static float logoY = 0;
    private static float rotationAngle = 0;
    private static float slideOffset = 0;
    private static long startTime = 0;
    private static boolean isMenuOpen = false;

    // ================================================================
    // 1. АНИМАЦИЯ ОТКРЫТИЯ МЕНЮ (ЛОГО ВЫЕЗЖАЕТ ИЗ ЦЕНТРА НАВЕРХ)
    // ================================================================
    public static void startMenuOpenAnimation() {
        if (!ClientConfig.data.animationMenuOpen) return;
        isMenuOpen = true;
        startTime = System.currentTimeMillis();
        logoX = 0;
        logoY = 0;
    }

    public static void tick() {
        if (!isMenuOpen) return;
        long elapsed = System.currentTimeMillis() - startTime;
        float duration = 500 * ClientConfig.data.animationSpeed; // 500ms * скорость
        float progress = Math.min(1.0f, elapsed / duration);
        
        // Плавное перемещение: центр → верх (-60 пикселей)
        logoX = 0;
        logoY = -60 * progress;
        
        if (progress >= 1.0f) {
            isMenuOpen = false;
        }
    }

    public static void renderLogo(DrawContext context, int centerX, int centerY) {
        int x = (int) (centerX + logoX - LOGO_WIDTH / 2);
        int y = (int) (centerY + logoY - LOGO_HEIGHT / 2);
        context.drawTexture(LOGO_TEXTURE, x, y, 0, 0, LOGO_WIDTH, LOGO_HEIGHT, LOGO_WIDTH, LOGO_HEIGHT);
    }

    // ================================================================
    // 2. ВЕЧНАЯ АНИМАЦИЯ: ВРАЩЕНИЕ ЛОГО (3D ЭФФЕКТ)
    // ================================================================
    public static void renderRotatingLogo(DrawContext context, int centerX, int centerY) {
        if (!ClientConfig.data.animationLogoRotate) return;
        
        rotationAngle += 0.02f * ClientConfig.data.animationSpeed;
        float scaleX = (float) Math.cos(rotationAngle) * 0.5f + 0.5f; // 0.5 — 1.0
        
        context.getMatrices().push();
        context.getMatrices().translate(centerX, centerY, 0);
        context.getMatrices().scale(scaleX, 1.0f, 1.0f);
        context.getMatrices().translate(-centerX, -centerY, 0);
        
        int x = centerX - LOGO_WIDTH / 2;
        int y = centerY - LOGO_HEIGHT / 2;
        context.drawTexture(LOGO_TEXTURE, x, y, 0, 0, LOGO_WIDTH, LOGO_HEIGHT, LOGO_WIDTH, LOGO_HEIGHT);
        
        context.getMatrices().pop();
    }

    // ================================================================
    // 3. ВЕЧНАЯ АНИМАЦИЯ: ПЕРЕМЕЩЕНИЕ ЛОГО (ЭФФЕКТ ПОРТАЛА)
    // ================================================================
    public static void renderSlidingLogo(DrawContext context, int centerX, int centerY) {
        if (!ClientConfig.data.animationLogoSlide) return;
        
        slideOffset += 0.02f * ClientConfig.data.animationSpeed;
        float shiftX = (float) Math.sin(slideOffset) * 50; // -50 .. +50
        
        int x = (int) (centerX + shiftX - LOGO_WIDTH / 2);
        int y = centerY - LOGO_HEIGHT / 2;
        context.drawTexture(LOGO_TEXTURE, x, y, 0, 0, LOGO_WIDTH, LOGO_HEIGHT, LOGO_WIDTH, LOGO_HEIGHT);
    }
}