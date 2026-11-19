package com.example.gobaltvisual.gui;

import com.example.gobaltvisual.gui.Column;
import com.example.gobaltvisual.gui.RenderUtils;
import com.example.gobaltvisual.gui.GobaltVisualScreen;
/**
 * 🎨 Централизованное хранение всех цветов для меню.
 * Легко менять всю цветовую схему, редактируя этот файл.
 */
public class Colors {
    // Фоны
    public static final int MENU_BG = 0x80171729;      // Тёмный фиолетовый (полупрозрачный)
    public static final int COLUMN_BG = 0x80171729;    // То же, что и фон меню

    // Рамки
    public static final int BORDER_MAIN = 0xFF9F43E5;  // Фиолетовый
    public static final int BORDER_COLUMN = 0xFF9F43E5;

    // Текст
    public static final int TEXT_TITLE = 0xFF9F43E5;   // Фиолетовый (заголовки)
    public static final int TEXT_ITEM = 0xFFD9D9D9;    // Светло-серый (функции)
    public static final int TEXT_HINT = 0x555555;      // Тёмно-серый (подсказки)

    // Чекбоксы
    //public static final int CHECKBOX_BORDER = 0x3E0040; // Тёмно-фиолетовый
    public static final int CHECKBOX_ON = 0xFF00C853;  // Ярко-зелёный
    public static final int CHECKBOX_OFF = 0x000000;   // Прозрачный (пустой)

    // Скроллбар
    public static final int SCROLLBAR = 0xFF9F43E5;    // Фиолетовый
}