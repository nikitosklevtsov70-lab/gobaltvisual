package com.example.gobaltvisual.utils;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ArmorApplier {

    public static void applyTrim(ItemStack stack, String templateName, String materialName) {
        var templateKey = RegistryKey.of(RegistryKeys.TRIM_PATTERN, Identifier.ofVanilla(templateName));
        var materialKey = RegistryKey.of(RegistryKeys.TRIM_MATERIAL, Identifier.ofVanilla(materialName));

    }
}