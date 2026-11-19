package com.example.gobaltvisual.utils;

import java.util.EnumSet;
import java.util.Set;

public class DeveloperMode {
    private static boolean enabled = false;
    private static final Set<Feature> debugFeatures = EnumSet.noneOf(Feature.class);

    public enum Feature {
        HITBOX,
        FAKE_PLAYER,
        TIME,
        GAMMA,
        RENDER,
        MIXIN,
        CONFIG
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static void setEnabled(boolean value) {
        enabled = value;
        if (!value) {
            debugFeatures.clear();
        }
        GVLog.info("[DEV] Режим разработчика " + (value ? "ВКЛЮЧЁН" : "ОТКЛЮЧЁН"));
    }

    public static void toggle() {
        setEnabled(!enabled);
    }

    public static void reset() {
        setEnabled(false);
    }

    public static boolean isDebug(Feature feature) {
        return enabled && debugFeatures.contains(feature);
    }

    public static void toggleFeature(Feature feature) {
        if (debugFeatures.contains(feature)) {
            debugFeatures.remove(feature);
        } else {
            debugFeatures.add(feature);
        }
    }

    public static Set<Feature> getActiveFeatures() {
        return EnumSet.copyOf(debugFeatures);
    }
}