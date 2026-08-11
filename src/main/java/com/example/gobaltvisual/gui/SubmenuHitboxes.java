package com.example.gobaltvisual.gui;

import com.example.gobaltvisual.config.HitboxesConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class SubmenuHitboxes extends Screen {
    private final Screen parent;

    public SubmenuHitboxes(Screen parent) {
        super(Text.literal("Настройка хитбоксов"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int cx = this.width / 2;
        int cy = this.height / 2;

        // Заголовок
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("🎯 НАСТРОЙКА ХИТБОКСОВ").formatted(Formatting.BOLD, Formatting.AQUA),
                button -> {}
        ).dimensions(cx - 80, cy - 80, 160, 20).build());

        // Кнопка ВКЛ/ВЫКЛ
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal(HitboxesConfig.data.enabled ? "🟢 Включены" : "🔴 Выключены")
                        .formatted(HitboxesConfig.data.enabled ? Formatting.GREEN : Formatting.RED),
                button -> {
                    HitboxesConfig.data.enabled = !HitboxesConfig.data.enabled;
                    HitboxesConfig.save();
                    client.setScreen(new SubmenuHitboxes(parent));
                }
        ).dimensions(cx - 60, cy - 40, 120, 20).build());

        // Кнопка "Назад"
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("🔙 Назад").formatted(Formatting.GRAY),
                button -> client.setScreen(parent)
        ).dimensions(cx - 40, cy + 40, 80, 20).build());
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, this.width, this.height, 0xCC0A192F);
        super.render(context, mouseX, mouseY, delta);
    }
}
