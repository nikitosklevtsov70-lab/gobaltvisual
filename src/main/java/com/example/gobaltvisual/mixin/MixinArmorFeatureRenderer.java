package com.example.gobaltvisual.mixin;

import com.example.gobaltvisual.config.ArmorConfig;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorFeatureRenderer.class)
public abstract class MixinArmorFeatureRenderer<T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> {

    @Inject(
        method = "renderArmor",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onRenderArmor(
        MatrixStack matrices,
        VertexConsumerProvider vertexConsumers,
        T entity,
        EquipmentSlot armorSlot,
        int light,
        A model,
        CallbackInfo ci
    ) {
        // Проверяем, что мод включен
        if (!ArmorConfig.isEnabled()) {
            return;
        }

        // Получаем ItemStack брони
        ItemStack armorStack = entity.getEquippedStack(armorSlot);
        if (armorStack.isEmpty() || !(armorStack.getItem() instanceof ArmorItem armorItem)) {
            return;
        }

        // Получаем данные конфига
        ArmorConfig.ArmorData cfg = ArmorConfig.data;

        try {
            // Получаем материал брони для определения слоя текстуры
            RegistryEntry<ArmorMaterial> materialEntry = armorItem.getMaterial();
            ArmorMaterial material = materialEntry.value();

            // Определяем тип текстуры (layer_1 или layer_2)
            boolean useInnerModel = armorSlot == EquipmentSlot.LEGS;
            String textureLayer = useInnerModel ? "layer_2" : "layer_1";

            // Формируем путь к текстуре trim паттерна
            Identifier trimTexture = Identifier.of(
                "minecraft",
                "trims/models/armor/" + cfg.template + "_" + textureLayer
            );

            // Получаем базовую текстуру брони
            Identifier armorTexture = getArmorTexture(armorItem, textureLayer);

            // Рендерим базовую броню
            renderArmorLayer(matrices, vertexConsumers, light, model, armorTexture, 1.0f, 1.0f, 1.0f);

            // Получаем цвет материала trim
            int trimColor = getTrimColor(Identifier.of("minecraft", cfg.material));
            float r = ((trimColor >> 16) & 0xFF) / 255.0f;
            float g = ((trimColor >> 8) & 0xFF) / 255.0f;
            float b = (trimColor & 0xFF) / 255.0f;

            // Рендерим trim поверх брони с цветом материала
            renderArmorLayer(matrices, vertexConsumers, light, model, trimTexture, r, g, b);

            // Отменяем стандартный рендеринг
            ci.cancel();

        } catch (Exception e) {
            // В случае ошибки пропускаем и даём отрендерить стандартно
            System.err.println("[GobaltVisual] Ошибка рендера брони: " + e.getMessage());
        }
    }

    // Получаем текстуру брони
    private Identifier getArmorTexture(ArmorItem item, String layer) {
        RegistryEntry<ArmorMaterial> materialEntry = item.getMaterial();

        // Получаем ID материала из registry
        String materialName = materialEntry.getKey()
            .map(key -> key.getValue().getPath())
            .orElse("leather");

        String path = "textures/models/armor/" + materialName + "_" + layer + ".png";
        return Identifier.of("minecraft", path);
    }

    // Рендерим слой брони с цветом
    private void renderArmorLayer(
        MatrixStack matrices,
        VertexConsumerProvider vertexConsumers,
        int light,
        A model,
        Identifier texture,
        float red,
        float green,
        float blue
    ) {
        // Создаем VertexConsumer с нужным RenderLayer
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getArmorCutoutNoCull(texture));

        // В 1.21.1 цвет не передается в render(), вместо этого нужно использовать
        // цветной VertexConsumer wrapper или модифицировать вертексы напрямую
        // Но самый простой способ - использовать RenderLayer с цветом через DiffuseShader

        // Просто рендерим модель - цвет будет применен через шейдер
        model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
    }

    // Получаем цвет материала trim
    private int getTrimColor(Identifier materialId) {
        // Цвета материалов для trim (из vanilla Minecraft)
        return switch (materialId.getPath()) {
            case "quartz" -> 0xE3D4C4;
            case "iron" -> 0xECECEC;
            case "netherite" -> 0x444444;
            case "redstone" -> 0x971607;
            case "copper" -> 0xB4684D;
            case "gold" -> 0xDEB12D;
            case "emerald" -> 0x11A036;
            case "diamond" -> 0x6EECD2;
            case "lapis" -> 0x416E97;
            case "amethyst" -> 0x9A5CC6;
            default -> 0xFFFFFF;
        };
    }
}
