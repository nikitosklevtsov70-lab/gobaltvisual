package com.example.gobaltvisual.gui;

import com.example.gobaltvisual.gui.Colors;
import com.example.gobaltvisual.gui.RenderUtils;
import com.example.gobaltvisual.gui.GobaltVisualScreen;
/**
 * 📌 Колонка — это маленькое "окошко" в меню (например, "ПВП").
 * Содержит заголовок, список функций, состояния чекбоксов и параметры скролла.
 */
public class Column {
    public String title;
    public String[] items;
    public boolean[] enabled;
    public int scrollOffset = 0;
    public int maxScroll = 0;

    // 🔥 Теперь это массивы для каждой функции
    public float[] alpha;
    public float[] targetAlpha;

    public Column(String title, String[] items) {
        this.title = title;
        this.items = items;
        this.enabled = new boolean[items.length];
        this.alpha = new float[items.length];
        this.targetAlpha = new float[items.length];
    }
}