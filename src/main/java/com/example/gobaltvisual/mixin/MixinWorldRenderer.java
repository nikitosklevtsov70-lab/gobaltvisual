package com.example.gobaltvisual.mixin;

import com.example.gobaltvisual.config.HitboxesConfig;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {
    
    // ПРАВИЛЬНАЯ СИГНАТУРА ДЛЯ 1.21.1
    @Inject(method = "renderEntity", at = @At("HEAD"))
    private void renderHitbox(Entity entity, double x, double y, double z, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        HitboxesConfig.HitboxesData cfg = HitboxesConfig.data;
        if (!cfg.enabled) return;
        if (entity == net.minecraft.client.MinecraftClient.getInstance().player) return;
        
        Box box = entity.getBoundingBox();
        float x1 = (float) (box.minX - x);
        float y1 = (float) (box.minY - y);
        float z1 = (float) (box.minZ - z);
        float x2 = (float) (box.maxX - x);
        float y2 = (float) (box.maxY - y);
        float z2 = (float) (box.maxZ - z);
        
        float r = cfg.red;
        float g = cfg.green;
        float b = cfg.blue;
        float a = cfg.alpha;
        
        VertexConsumer consumer = vertexConsumers.getBuffer(RenderLayer.getDebugQuads());
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        
        drawLine(consumer, matrix, x1, y1, z1, x2, y1, z1, r, g, b, a);
        drawLine(consumer, matrix, x2, y1, z1, x2, y1, z2, r, g, b, a);
        drawLine(consumer, matrix, x2, y1, z2, x1, y1, z2, r, g, b, a);
        drawLine(consumer, matrix, x1, y1, z2, x1, y1, z1, r, g, b, a);
        
        drawLine(consumer, matrix, x1, y2, z1, x2, y2, z1, r, g, b, a);
        drawLine(consumer, matrix, x2, y2, z1, x2, y2, z2, r, g, b, a);
        drawLine(consumer, matrix, x2, y2, z2, x1, y2, z2, r, g, b, a);
        drawLine(consumer, matrix, x1, y2, z2, x1, y2, z1, r, g, b, a);
        
        drawLine(consumer, matrix, x1, y1, z1, x1, y2, z1, r, g, b, a);
        drawLine(consumer, matrix, x2, y1, z1, x2, y2, z1, r, g, b, a);
        drawLine(consumer, matrix, x2, y1, z2, x2, y2, z2, r, g, b, a);
        drawLine(consumer, matrix, x1, y1, z2, x1, y2, z2, r, g, b, a);
    }
    
    private static void drawLine(VertexConsumer consumer, Matrix4f matrix,
                                 float x1, float y1, float z1, float x2, float y2, float z2,
                                 float r, float g, float b, float a) {
        consumer.vertex(matrix, x1, y1, z1).color(r, g, b, a);
        consumer.vertex(matrix, x2, y2, z2).color(r, g, b, a);
    }
}
