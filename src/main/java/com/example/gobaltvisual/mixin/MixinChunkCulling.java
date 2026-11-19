package com.example.gobaltvisual.mixin;

import com.example.gobaltvisual.config.ChunkCullingConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldRenderer.class)
public class MixinChunkCulling {

    private static final MinecraftClient mc = MinecraftClient.getInstance();

    @Redirect(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/Frustum;isVisible(Lnet/minecraft/util/math/Box;)Z"
        )
    )
    private boolean redirectIsVisible(Frustum frustum, Box box) {
        ChunkCullingConfig.ChunkCullingData cfg = ChunkCullingConfig.data;
        if (!cfg.enabled) return frustum.isVisible(box);

        // Если объект очень маленький (скорее всего сущность) — всегда виден
        double size = box.getAverageSideLength();
        if (size < 0.5) {
            return true; // Сущности всегда видны
        }

        // Проверка дистанции
        Vec3d cameraPos = mc.gameRenderer.getCamera().getPos();
        Vec3d boxCenter = box.getCenter();
        double distance = cameraPos.distanceTo(boxCenter);

        if (distance > cfg.maxDistance * 16) {
            return false; // Чанк слишком далеко
        }

        // Проверка угла обзора (отсечение за спиной)
        if (cfg.cullBehind) {
            Vec3d lookVec = mc.player.getRotationVec(1.0f);
            Vec3d toCenter = boxCenter.subtract(cameraPos).normalize();
            double dot = lookVec.dotProduct(toCenter);

            // Если объект за спиной (dot < 0) и не сущность — пропускаем
            if (dot < -0.1 && size > 1.0) {
                return false;
            }
        }

        // Проверка по бокам (горизонтальный угол)
        if (cfg.cullSides) {
            Vec3d lookVec = mc.player.getRotationVec(1.0f);
            Vec3d toCenter = boxCenter.subtract(cameraPos).normalize();
            double dot = lookVec.dotProduct(toCenter);

            // Если объект сильно сбоку
            if (dot < 0.3 && size > 1.0) {
                return false;
            }
        }

        // Иначе — стандартная проверка
        return frustum.isVisible(box);
    }
}
