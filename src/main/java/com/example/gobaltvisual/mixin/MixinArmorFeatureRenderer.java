package com.example.gobaltvisual.mixin;

import com.example.gobaltvisual.config.TogglesConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.item.trim.ArmorTrimPattern;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(net.minecraft.client.render.entity.feature.ArmorFeatureRenderer.class)
public class MixinArmorFeatureRenderer {

    private static final String[] PATTERNS = {"sentry", "tide", "ward", "silence", "vex", "wild", "coast", "dune"};
    private static final String[] MATERIALS = {"gold", "diamond", "netherite", "redstone", "emerald", "iron", "copper", "amethyst"};

    @Redirect(
            method = "renderArmor",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;get(Lnet/minecraft/component/ComponentType;)Ljava/lang/Object;")
    )
    private Object forceArmorTrim(ItemStack stack, ComponentType<?> type) {
        Object original = stack.get(type);
        
        if (type == DataComponentTypes.TRIM && TogglesConfig.get("ТРИМЫ", "Оверрайд")) {
            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.world == null) return original;

            // Берем реестр из мира, он синхронизирован с сервером
            DynamicRegistryManager registryManager = mc.world.getRegistryManager();
            
            int patternIdx = TogglesConfig.getInt("ТРИМЫ", "ПаттернИндекс");
            int materialIdx = TogglesConfig.getInt("ТРИМЫ", "МатериалИндекс");

            String patternName = PATTERNS[Math.min(patternIdx, PATTERNS.length - 1)];
            String materialName = MATERIALS[Math.min(materialIdx, MATERIALS.length - 1)];

            // Получаем сами реестры
            Registry<ArmorTrimPattern> patternRegistry = registryManager.get(RegistryKeys.TRIM_PATTERN);
            Registry<ArmorTrimMaterial> materialRegistry = registryManager.get(RegistryKeys.TRIM_MATERIAL);

            if (patternRegistry != null && materialRegistry != null) {
                // Ищем элементы по их ID (например, minecraft:sentry)
                RegistryEntry<ArmorTrimPattern> patternEntry = patternRegistry.getEntry(Identifier.of("minecraft", patternName)).orElse(null);
                RegistryEntry<ArmorTrimMaterial> materialEntry = materialRegistry.getEntry(Identifier.of("minecraft", materialName)).orElse(null);

                if (patternEntry != null && materialEntry != null) {
                    return new ArmorTrim(materialEntry, patternEntry);
                } else {
                    // ДЕБАГ: Выводим список всего, что есть на сервере (раз в пару секунд)
                    if (Math.random() < 0.01) {
                        System.out.println("[GV-TRIM] ERROR: Key not found " + patternName + " or " + materialName);
                        // ИСПРАВЛЕНО: Добавлен .stream() перед .map()
                        System.out.println("[GV-TRIM] Available patterns: " + patternRegistry.getKeys().stream().map(k -> k.getValue().toString()).toList());
                        System.out.println("[GV-TRIM] Available materials: " + materialRegistry.getKeys().stream().map(k -> k.getValue().toString()).toList());
                    }
                }
            } else {
                if (Math.random() < 0.01) {
                    System.out.println("[GV-TRIM] Registries TRIM_PATTERN or TRIM_MATERIAL are null!");
                }
            }
        }
        return original;
    }
}