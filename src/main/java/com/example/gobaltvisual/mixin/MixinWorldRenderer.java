package com.example.gobaltvisual.mixin;

import com.example.gobaltvisual.config.ChunkCullingConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {

    private static final MinecraftClient mc = MinecraftClient.getInstance();
    private static final Logger LOGGER = LoggerFactory.getLogger("GV-ChunkCulling");
    private static int frameCounter = 0;

    @Inject(method = "render", at = @At("HEAD"))
    private void onRender(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f projectionMatrix, CallbackInfo ci) {
        ChunkCullingConfig.ChunkCullingData cfg = ChunkCullingConfig.data;
        if (!cfg.enabled) return;

        if (cfg.debugLog && ++frameCounter % 100 == 0) {
            LOGGER.info("[GV-ChunkCulling] Активен, дистанция: {}", cfg.maxDistance);
        }
    }

    @Inject(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/Frustum;isVisible(Lnet/minecraft/util/math/Box;)Z",
            shift = At.Shift.BEFORE
        )
    )
    private void beforeFrustumCheck(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f projectionMatrix, CallbackInfo ci) {
        // Здесь мы можем изменить поведение Frustum
        // Но для простоты мы используем @Redirect (см. MixinChunkCulling)
    }
}
