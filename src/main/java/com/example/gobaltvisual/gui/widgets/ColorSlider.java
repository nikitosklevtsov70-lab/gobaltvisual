package com.example.gobaltvisual.gui.widgets;

import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;
import java.util.function.Consumer;

public class ColorSlider extends SliderWidget {
    private final Consumer<Float> consumer;
    private final float min, max;

    public ColorSlider(int x, int y, int w, int h, Text text, float min, float max, float current, Consumer<Float> consumer) {
        super(x, y, w, h, text, current);
        this.min = min;
        this.max = max;
        this.consumer = consumer;
        this.value = (current - min) / (max - min);
        updateMessage();
    }

    @Override
    protected void updateMessage() {
        float val = min + (max - min) * (float) this.value;
        this.setMessage(Text.literal(this.getMessage().getString() + ": " + String.format("%.2f", val)));
    }

    @Override
    protected void applyValue() {
        float val = min + (max - min) * (float) this.value;
        consumer.accept(val);
    }
}