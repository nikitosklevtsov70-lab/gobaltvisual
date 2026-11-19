package com.example.gobaltvisual.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import java.util.ArrayList;
import java.util.List;

import com.example.gobaltvisual.config.TogglesConfig;

import static com.example.gobaltvisual.gui.Colors.*;
import static com.example.gobaltvisual.gui.RenderUtils.*;

public class SubmenuScreen extends Screen {

    private final Screen parent;
    private final String title;
    private final List<SubItem> items = new ArrayList<>();

    private int itemHeight = 22;
    private int startX, startY;
    private int scrollOffset = 0;
    private int maxScroll = 0;

    private SubItem activeSlider = null;

    private enum ItemType { TOGGLE, BUTTON, SLIDER }

    private static class SubItem {
        String name;
        String key;
        ItemType type;
        boolean enabled;
        float alpha;
        float targetAlpha;
        int buttonState = 0;
        float sliderValue = 1.0f;

        // Тумблер
        SubItem(String name, String key, ItemType type, boolean enabled) {
            this.name = name;
            this.key = key;
            this.type = type;
            this.enabled = enabled;
            this.alpha = enabled ? 1f : 0f;
            this.targetAlpha = enabled ? 1f : 0f;
        }

        // Кнопка
        SubItem(String name, String key, ItemType type, int buttonState) {
            this.name = name;
            this.key = key;
            this.type = type;
            this.buttonState = buttonState;
        }

        // Слайдер
        SubItem(String name, String key, ItemType type, float sliderValue) {
            this.name = name;
            this.key = key;
            this.type = type;
            this.sliderValue = sliderValue;
        }
    }

    public SubmenuScreen(Screen parent, String title) {
        super(Text.literal(title));
        this.parent = parent;
        this.title = title;

        if (title.equals("хитбоксы")) {
            items.add(new SubItem("Цвет", "Цвет", ItemType.BUTTON, TogglesConfig.getInt(title, "Цвет")));
            items.add(new SubItem("Прозрачность", "Прозрачность", ItemType.SLIDER, TogglesConfig.getFloat(title, "Прозрачность")));
            items.add(new SubItem("Показывать мобов", "Показывать мобов", ItemType.TOGGLE, TogglesConfig.get(title, "Показывать мобов")));
            items.add(new SubItem("Показывать игроков", "Показывать игроков", ItemType.TOGGLE, TogglesConfig.get(title, "Показывать игроков")));

            if (TogglesConfig.getInt(title, "Цвет") == 0) {
                items.add(new SubItem("Красный", "ЦветR", ItemType.SLIDER, TogglesConfig.getFloat(title, "ЦветR")));
                items.add(new SubItem("Зелёный", "ЦветG", ItemType.SLIDER, TogglesConfig.getFloat(title, "ЦветG")));
                items.add(new SubItem("Синий", "ЦветB", ItemType.SLIDER, TogglesConfig.getFloat(title, "ЦветB")));
            }
        } else if (title.equals("Цвет неба")) {
            items.add(new SubItem("Космос", "Космос", ItemType.TOGGLE, TogglesConfig.get(title, "Космос")));
            items.add(new SubItem("Закат", "Закат", ItemType.TOGGLE, TogglesConfig.get(title, "Закат")));
            items.add(new SubItem("Глубина", "Глубина", ItemType.TOGGLE, TogglesConfig.get(title, "Глубина")));
        } else {
            items.add(new SubItem("Настроить", "Настроить", ItemType.TOGGLE, TogglesConfig.get(title, "Настроить")));
        }
    }

    @Override
    protected void init() {
        super.init();
        startX = 20;
        startY = 40;
        int contentHeight = items.size() * itemHeight + 20;
        maxScroll = Math.max(0, contentHeight - (this.height - 80));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, this.width, this.height, 0xCC0A0A14);
        context.drawCenteredTextWithShadow(this.textRenderer, Text.literal(title), this.width / 2, 15, TEXT_TITLE);

        for (SubItem item : items) {
            if (item.type == ItemType.TOGGLE) {
                float speed = 0.15f;
                if (item.alpha < item.targetAlpha) {
                    item.alpha += speed;
                    if (item.alpha > item.targetAlpha) item.alpha = item.targetAlpha;
                } else if (item.alpha > item.targetAlpha) {
                    item.alpha -= speed;
                    if (item.alpha < item.targetAlpha) item.alpha = item.targetAlpha;
                }
            }
        }

        for (int i = 0; i < items.size(); i++) {
            SubItem item = items.get(i);
            int y = startY + i * itemHeight - scrollOffset;
            if (y < startY - 20 || y > this.height - 40) continue;

            context.drawTextWithShadow(this.textRenderer, Text.literal(item.name), startX, y + 7, TEXT_ITEM);

            if (item.type == ItemType.TOGGLE) {
                boolean enabled = item.alpha > 0.5f;
                drawToggle(context, this.width - 60, y + 5, enabled, item.alpha);

            } else if (item.type == ItemType.BUTTON) {
                int btnX = this.width - 140;
                int btnY = y + 3;
                int btnWidth = 60;
                int btnHeight = 14;
                boolean isClient = item.buttonState == 1;

                drawRoundedRect(context, btnX, btnY, btnWidth, btnHeight, 3,
                    isClient ? 0xFF333333 : 0xFF9F43E5);
                context.drawCenteredTextWithShadow(this.textRenderer,
                    Text.literal("Свой"), btnX + btnWidth / 2, btnY + 3, 0xFFFFFFFF);

                drawRoundedRect(context, btnX + btnWidth + 4, btnY, btnWidth, btnHeight, 3,
                    isClient ? 0xFF9F43E5 : 0xFF333333);
                context.drawCenteredTextWithShadow(this.textRenderer,
                    Text.literal("Клиент"), btnX + btnWidth + 4 + btnWidth / 2, btnY + 3, 0xFFFFFFFF);

            } else if (item.type == ItemType.SLIDER) {
                int sliderX = this.width - 160;
                int sliderY = y + 5;
                int sliderWidth = 120;
                int sliderHeight = 10;

                drawRoundedRect(context, sliderX, sliderY, sliderWidth, sliderHeight, 3, 0xFF333333);
                int fillWidth = (int)(sliderWidth * item.sliderValue);
                drawRoundedRect(context, sliderX, sliderY, fillWidth, sliderHeight, 3, 0xFF9F43E5);

                int knobX = sliderX + fillWidth - 3;
                context.fill(knobX, sliderY - 2, knobX + 6, sliderY + sliderHeight + 2, 0xFFFFFFFF);

                String val = String.format("%.2f", item.sliderValue);
                context.drawTextWithShadow(this.textRenderer,
                    Text.literal(val), sliderX + sliderWidth + 5, y + 7, TEXT_ITEM);
            }
        }

        // 🔥 Превью цвета, если выбран "Свой"
        if (title.equals("хитбоксы") && TogglesConfig.getInt(title, "Цвет") == 0) {
            int previewX = this.width - 200;
            int previewY = startY + items.size() * itemHeight + 10;

            float r = TogglesConfig.getFloat(title, "ЦветR");
            float g = TogglesConfig.getFloat(title, "ЦветG");
            float b = TogglesConfig.getFloat(title, "ЦветB");

            int color = (0xFF << 24) | ((int)(r * 255) << 16) | ((int)(g * 255) << 8) | (int)(b * 255);

            context.fill(previewX, previewY, previewX + 60, previewY + 60, color);
            context.drawBorder(previewX, previewY, 60, 60, 0xFFFFFFFF);

            context.fill(previewX + 70, previewY, previewX + 90, previewY + 20, color);
            context.drawBorder(previewX + 70, previewY, 20, 20, 0xFFFFFFFF);

            context.drawTextWithShadow(this.textRenderer,
                Text.literal("Превью"), previewX + 70, previewY + 25, TEXT_HINT);
        }

        context.drawTextWithShadow(this.textRenderer,
            Text.literal("Esc — назад"), 10, this.height - 20, TEXT_HINT);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (int i = 0; i < items.size(); i++) {
            SubItem item = items.get(i);
            int y = startY + i * itemHeight - scrollOffset;
            if (y < startY - 20 || y > this.height - 40) continue;

            if (item.type == ItemType.TOGGLE) {
                if (mouseX >= startX && mouseX <= this.width - 20 &&
                    mouseY >= y && mouseY <= y + itemHeight) {
                    if (button == 0) {
                        item.enabled = !item.enabled;
                        item.targetAlpha = item.enabled ? 1f : 0f;
                        TogglesConfig.set(title, item.key, item.enabled);
                        return true;
                    }
                }
            } else if (item.type == ItemType.BUTTON) {
                int btnX = this.width - 140;
                int btnY = y + 3;
                int btnWidth = 60;
                int btnHeight = 14;

                if (mouseX >= btnX && mouseX <= btnX + btnWidth &&
                    mouseY >= btnY && mouseY <= btnY + btnHeight) {
                    if (button == 0) {
                        TogglesConfig.setInt(title, item.key, 0);
                        if (this.client != null) {
                            this.client.setScreen(new SubmenuScreen(parent, title));
                        }
                        return true;
                    }
                }
                if (mouseX >= btnX + btnWidth + 4 && mouseX <= btnX + btnWidth * 2 + 4 &&
                    mouseY >= btnY && mouseY <= btnY + btnHeight) {
                    if (button == 0) {
                        TogglesConfig.setInt(title, item.key, 1);
                        if (this.client != null) {
                            this.client.setScreen(new SubmenuScreen(parent, title));
                        }
                        return true;
                    }
                }
            } else if (item.type == ItemType.SLIDER) {
                int sliderX = this.width - 160;
                int sliderY = y + 5;
                int sliderWidth = 120;
                int sliderHeight = 10;

                if (mouseX >= sliderX && mouseX <= sliderX + sliderWidth &&
                    mouseY >= sliderY - 4 && mouseY <= sliderY + sliderHeight + 4) {
                    if (button == 0) {
                        item.sliderValue = (float)((mouseX - sliderX) / sliderWidth);
                        item.sliderValue = Math.max(0f, Math.min(1f, item.sliderValue));
                        TogglesConfig.setFloat(title, item.key, item.sliderValue);
                        activeSlider = item;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (activeSlider == null) return false;

        int sliderX = this.width - 160;
        int sliderWidth = 120;

        activeSlider.sliderValue = (float)((mouseX - sliderX) / sliderWidth);
        activeSlider.sliderValue = Math.max(0f, Math.min(1f, activeSlider.sliderValue));
        TogglesConfig.setFloat(title, activeSlider.key, activeSlider.sliderValue);
        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        activeSlider = null;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        scrollOffset -= verticalAmount * 10;
        scrollOffset = Math.max(0, Math.min(maxScroll, scrollOffset));
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) {
            if (this.client != null) {
                this.client.setScreen(parent);
            }
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void close() {
        if (this.client != null) {
            this.client.setScreen(parent);
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}