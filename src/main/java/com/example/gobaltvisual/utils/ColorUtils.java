package com.example.gobaltvisual.utils;

public class ColorUtils {
    /**
     * Конвертирует RGB (0-255) в float (0.0-1.0)
     */
    public static float[] rgbToFloat(int red, int green, int blue) {
        return new float[]{
                red / 255.0f,
                green / 255.0f,
                blue / 255.0f
        };
    }

    /**
     * Смешивает два цвета с заданным весом
     */
    public static int blendColors(int color1, int color2, float weight) {
        int r1 = (color1 >> 16) & 0xFF;
        int g1 = (color1 >> 8) & 0xFF;
        int b1 = color1 & 0xFF;

        int r2 = (color2 >> 16) & 0xFF;
        int g2 = (color2 >> 8) & 0xFF;
        int b2 = color2 & 0xFF;

        int r = (int) (r1 + (r2 - r1) * weight);
        int g = (int) (g1 + (g2 - g1) * weight);
        int b = (int) (b1 + (b2 - b1) * weight);

        return (r << 16) | (g << 8) | b;
    }

    /**
     * Генерирует цвет по названию
     */
    public static int getColorByName(String name) {
        return switch (name.toLowerCase()) {
            case "red" -> 0xFF0000;
            case "green" -> 0x00FF00;
            case "blue" -> 0x0000FF;
            case "yellow" -> 0xFFFF00;
            case "cyan" -> 0x00FFFF;
            case "magenta" -> 0xFF00FF;
            case "orange" -> 0xFFA500;
            default -> 0xFFFFFF;
        };
    }
}