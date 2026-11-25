package com.example.gobaltvisual.mixin;

import com.example.gobaltvisual.config.HitboxesConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class MixinEntityRenderDispatcher {

    private static final MinecraftClient mc = MinecraftClient.getInstance();
    private static int errorCount = 0;
    private static final int MAX_ERRORS = 10;

    @Inject(method = "render", at = @At("RETURN"))
    private void onRender(Entity entity, double x, double y, double z, float yaw, float tickDelta,
                          MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {

        HitboxesConfig.HitboxesData cfg = HitboxesConfig.data;

        if (!cfg.enabled) return;
        if (entity == mc.player) return;
        if (!(entity instanceof LivingEntity livingEntity)) return;
        if (livingEntity instanceof ArmorStandEntity) return;
        if (livingEntity instanceof MobEntity && !cfg.showMobs) return;
        if (livingEntity instanceof PlayerEntity && !cfg.showPlayers) return;
        if (livingEntity.isInvisible() || livingEntity.hasStatusEffect(StatusEffects.INVISIBILITY)) return;

        try {
            Vec3d cameraPos = mc.gameRenderer.getCamera().getPos();

            double lerpX = MathHelper.lerp(tickDelta, livingEntity.prevX, livingEntity.getX()) - cameraPos.x;
            double lerpY = MathHelper.lerp(tickDelta, livingEntity.prevY, livingEntity.getY()) - cameraPos.y;
            double lerpZ = MathHelper.lerp(tickDelta, livingEntity.prevZ, livingEntity.getZ()) - cameraPos.z;

            float halfWidth = livingEntity.getWidth() / 2.0f;
            float height = livingEntity.getHeight();

            float x1 = (float) (lerpX - halfWidth);
            float y1 = (float) lerpY;
            float z1 = (float) (lerpZ - halfWidth);
            float x2 = (float) (lerpX + halfWidth);
            float y2 = (float) (lerpY + height);
            float z2 = (float) (lerpZ + halfWidth);

            VertexConsumer consumer = vertexConsumers.getBuffer(RenderLayer.getLines());
            MatrixStack.Entry entry = matrices.peek();

            float r = cfg.red;
            float g = cfg.green;
            float b = cfg.blue;
            float a = cfg.alpha;
            float width = Math.max(1.0f, Math.min(10.0f, cfg.lineWidth));

            // Нижняя грань
            drawLine(consumer, entry, x1, y1, z1, x2, y1, z1, r, g, b, a, width);
            drawLine(consumer, entry, x2, y1, z1, x2, y1, z2, r, g, b, a, width);
            drawLine(consumer, entry, x2, y1, z2, x1, y1, z2, r, g, b, a, width);
            drawLine(consumer, entry, x1, y1, z2, x1, y1, z1, r, g, b, a, width);

            // Верхняя грань
            drawLine(consumer, entry, x1, y2, z1, x2, y2, z1, r, g, b, a, width);
            drawLine(consumer, entry, x2, y2, z1, x2, y2, z2, r, g, b, a, width);
            drawLine(consumer, entry, x2, y2, z2, x1, y2, z2, r, g, b, a, width);
            drawLine(consumer, entry, x1, y2, z2, x1, y2, z1, r, g, b, a, width);

            // Вертикальные рёбра
            drawLine(consumer, entry, x1, y1, z1, x1, y2, z1, r, g, b, a, width);
            drawLine(consumer, entry, x2, y1, z1, x2, y2, z1, r, g, b, a, width);
            drawLine(consumer, entry, x2, y1, z2, x2, y2, z2, r, g, b, a, width);
            drawLine(consumer, entry, x1, y1, z2, x1, y2, z2, r, g, b, a, width);

        } catch (Exception e) {
            if (errorCount < MAX_ERRORS) {
                errorCount++;
                System.err.println("[GobaltVisual] Ошибка рендера хитбокса: " + e.getMessage());
                if (errorCount == MAX_ERRORS) {
                    System.err.println("[GobaltVisual] Лимит ошибок рендера исчерпан.");
                }
            }
        }
    }

    private static void drawLine(VertexConsumer consumer, MatrixStack.Entry entry,
                                 float x1, float y1, float z1, float x2, float y2, float z2,
                                 float r, float g, float b, float a, float thickness) {

        float dx = x2 - x1;
        float dy = y2 - y1;
        float dz = z2 - z1;
        float len = (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
        if (len == 0) len = 1;
        float nx = dx / len;
        float ny = dy / len;
        float nz = dz / len;

        if (thickness <= 1.0f) {
            consumer.vertex(entry, x1, y1, z1).color(r, g, b, a).normal(entry, nx, ny, nz);
            consumer.vertex(entry, x2, y2, z2).color(r, g, b, a).normal(entry, nx, ny, nz);
            return;
        }

        int passes = (int) Math.ceil(thickness);
        float offsetStep = 0.005f;

        for (int i = 0; i < passes; i++) {
            float offset = (i - passes / 2.0f) * offsetStep;
            consumer.vertex(entry, x1 + offset, y1 + offset, z1).color(r, g, b, a).normal(entry, nx, ny, nz);
            consumer.vertex(entry, x2 + offset, y2 + offset, z2).color(r, g, b, a).normal(entry, nx, ny, nz);
        }
    }
}
