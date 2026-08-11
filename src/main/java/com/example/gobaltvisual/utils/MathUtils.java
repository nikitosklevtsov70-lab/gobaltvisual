package com.example.gobaltvisual.utils;

import net.minecraft.util.math.Vec3d;

public class MathUtils {
    /**
     * Вычисляет расстояние между двумя точками в 3D
     */
    public static double distance(Vec3d a, Vec3d b) {
        return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2) + Math.pow(a.z - b.z, 2));
    }

    /**
     * Линейная интерполяция
     */
    public static double lerp(double start, double end, double t) {
        return start + (end - start) * t;
    }

    /**
     * Ограничивает значение в диапазоне [min, max]
     */
    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    /**
     * Проверяет, находится ли точка внутри сферы
     */
    public static boolean isInsideSphere(Vec3d center, double radius, Vec3d point) {
        return distance(center, point) <= radius;
    }
}