package com.example.gobaltvisual.config;

public class HitboxesConfig {

    public static class HitboxesData {
        public boolean enabled = false;
        public float red = 0.5f;
        public float green = 0.0f;
        public float blue = 1.0f;
        public float alpha = 1.0f;
        public float lineWidth = 2.0f;
        public boolean showMobs = true;
        public boolean showPlayers = true;
    }

    public static HitboxesData data = new HitboxesData();

    private static final int DEFAULT_COLOR = 0xFF8000FF; // фиолетовый

    public static void load() {
        sync();
    }

    /** Сохраняет текущие RGB+alpha в TogglesConfig. */
    public static void save() {
        TogglesConfig.set("ПВП", "хитбоксы", data.enabled);
        TogglesConfig.set("хитбоксы", "Показывать мобов", data.showMobs);
        TogglesConfig.set("хитбоксы", "Показывать игроков", data.showPlayers);
        TogglesConfig.setFloat("хитбоксы", "ЦветR", data.red);
        TogglesConfig.setFloat("хитбоксы", "ЦветG", data.green);
        TogglesConfig.setFloat("хитбоксы", "ЦветB", data.blue);
        TogglesConfig.setFloat("хитбоксы", "Прозрачность", data.alpha);
        TogglesConfig.setFloat("хитбоксы", "Толщина", data.lineWidth);
    }

    /** Установить цвет (ARGB int). Используется для кнопок-палитры. */
    public static void setColor(int argb) {
        data.red   = ((argb >> 16) & 0xFF) / 255f;
        data.green = ((argb >> 8)  & 0xFF) / 255f;
        data.blue  = ( argb        & 0xFF) / 255f;
        System.out.println("[GV-SET] argb=0x" + Integer.toHexString(argb)
        + " → data=" + data.red + "," + data.green + "," + data.blue + "," + data.alpha);
        System.out.println("[GV-SET] readback r=" + TogglesConfig.getFloat("хитбоксы", "ЦветR"));
        save();
    }

    /** Получить текущий цвет как ARGB. Используется для превью. */
    public static int getColor() {
        int r = (int)(data.red * 255)   & 0xFF;
        int g = (int)(data.green * 255) & 0xFF;
        int b = (int)(data.blue * 255)  & 0xFF;
        int a = (int)(data.alpha * 255) & 0xFF;
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    /** Читает всё из TogglesConfig. Не перезаписывает RGB дефолтами, если сохранено. */
    private static long lastPrintTick = -1L;
private static String lastPrinted = "";

    public static void sync() {
        data.enabled = TogglesConfig.get("ПВП", "хитбоксы");
        data.showMobs = TogglesConfig.get("хитбоксы", "Показывать мобов");
        data.showPlayers = TogglesConfig.get("хитбоксы", "Показывать игроков");

        float r = TogglesConfig.getFloat("хитбоксы", "ЦветR");
        float g = TogglesConfig.getFloat("хитбоксы", "ЦветG");
        float b = TogglesConfig.getFloat("хитбоксы", "ЦветB");
        float a = TogglesConfig.getFloat("хитбоксы", "Прозрачность");
        float lw = TogglesConfig.getFloat("хитбоксы", "Толщина");

        if (r < 0 || g < 0 || b < 0 || a < 0 || lw < 0) {
        data.red = 0.5f;
        data.green = 0.0f;
        data.blue = 1.0f;
        data.alpha = 1.0f;
        data.lineWidth = 2.0f; // Дефолтная толщина
        save();
        } else {
        data.red = r;
        data.green = g;
        data.blue = b;
        data.alpha = a;
        data.lineWidth = lw;
    }
        // 🔍 ДЕБАГ — раз в 20 тиков и только если изменилось
        net.minecraft.client.MinecraftClient mc = net.minecraft.client.MinecraftClient.getInstance();
        if (mc.player != null) {
            long tick = mc.player.age;
            if (tick != lastPrintTick && tick % 20 == 0) {
                lastPrintTick = tick;
                String snapshot = String.format("r=%.2f g=%.2f b=%.2f a=%.2f | data=%.2f,%.2f,%.2f,%.2f",
                        r, g, b, a, data.red, data.green, data.blue, data.alpha);
                if (!snapshot.equals(lastPrinted)) {
                    lastPrinted = snapshot;
                    System.out.println("[GV-SYNC] " + snapshot);
                }
            }
        }
    }
}