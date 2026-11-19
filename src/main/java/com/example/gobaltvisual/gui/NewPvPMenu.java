package com.example.gobaltvisual.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import com.example.gobaltvisual.config.HitboxesConfig;
import com.example.gobaltvisual.config.TogglesConfig;

public class NewPvPMenu extends Screen {
    private final Screen parent;
    private int currentTab = 0; // 0-PvP, 1-Визуал, 2-Партиклы, 3-Клиент, 4-HUD, 5-Шаблоны

    private int scrollOffset = 0;
    private final int maxScroll = -150;

    // Переменные для динамических колонок
    private int colWidth;
    private int col1X, col2X, col3X;
    private final int spacing = 10; // Отступ между колонками

    private static final String[] PATTERNS = {"sentry", "tide", "ward", "silence", "vex", "wild", "coast", "dune"};
    private static final String[] MATERIALS = {"gold", "diamond", "netherite", "redstone", "emerald", "iron", "copper", "amethyst"};

    public NewPvPMenu(Screen parent) {
        super(Text.literal("Gobalt Visual"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int sidebarWidth = 120;
        int startY = 30;

        // 🔥 ДИНАМИЧЕСКИЙ РАСЧЕТ КОЛОНОК
        int availableWidth = this.width - sidebarWidth - 30; // 30 - отступы от краев
        this.colWidth = (availableWidth - (spacing * 2)) / 3;
        
        // Защита от слишком узких колонок
        if (this.colWidth < 100) this.colWidth = 100;

        this.col1X = sidebarWidth + 15;
        this.col2X = this.col1X + this.colWidth + spacing;
        this.col3X = this.col2X + this.colWidth + spacing;

        // ==========================================
        // ЛЕВАЯ ПАНЕЛЬ (ВКЛАДКИ)
        // ==========================================
        this.addDrawableChild(new FlatButton(10, startY, 100, 20, Text.literal("PvP").formatted(currentTab == 0 ? Formatting.AQUA : Formatting.WHITE), button -> { currentTab = 0; scrollOffset = 0; this.clearAndInit(); }));
        this.addDrawableChild(new FlatButton(10, startY + 22, 100, 20, Text.literal("Визуал").formatted(currentTab == 1 ? Formatting.AQUA : Formatting.WHITE), button -> { currentTab = 1; scrollOffset = 0; this.clearAndInit(); }));
        this.addDrawableChild(new FlatButton(10, startY + 44, 100, 20, Text.literal("Партиклы").formatted(currentTab == 2 ? Formatting.AQUA : Formatting.WHITE), button -> { currentTab = 2; scrollOffset = 0; this.clearAndInit(); }));
        this.addDrawableChild(new FlatButton(10, startY + 66, 100, 20, Text.literal("Клиент").formatted(currentTab == 3 ? Formatting.AQUA : Formatting.WHITE), button -> { currentTab = 3; scrollOffset = 0; this.clearAndInit(); }));
        this.addDrawableChild(new FlatButton(10, startY + 88, 100, 20, Text.literal("HUD").formatted(currentTab == 4 ? Formatting.AQUA : Formatting.WHITE), button -> { currentTab = 4; scrollOffset = 0; this.clearAndInit(); }));
        this.addDrawableChild(new FlatButton(10, startY + 110, 100, 20, Text.literal("Шаблоны").formatted(currentTab == 5 ? Formatting.AQUA : Formatting.WHITE), button -> { currentTab = 5; scrollOffset = 0; this.clearAndInit(); }));

        this.addDrawableChild(new FlatButton(10, this.height - 30, 100, 20, Text.literal("Назад").formatted(Formatting.RED), button -> { if (this.client != null) this.client.setScreen(parent); }));

        // ==========================================
        // ПРАВАЯ ЧАСТЬ (ТРИ КОЛОНКИ)
        // ==========================================
        int startContentY = 50 + scrollOffset;

        if (currentTab == 0) {
            // --- ВКЛАДКА PvP ---
            this.addDrawableChild(new FlatToggle(col1X, startContentY, colWidth, 20, Text.literal("Хитбоксы"), HitboxesConfig.data.enabled, button -> { HitboxesConfig.data.enabled = ((FlatToggle) button).isToggled(); HitboxesConfig.save(); }));
            this.addDrawableChild(new FlatSlider(col1X, startContentY + 25, colWidth, 20, Text.literal("Красный"), HitboxesConfig.data.red, value -> { HitboxesConfig.data.red = value.floatValue(); HitboxesConfig.save(); }));
            this.addDrawableChild(new FlatSlider(col1X, startContentY + 50, colWidth, 20, Text.literal("Зеленый"), HitboxesConfig.data.green, value -> { HitboxesConfig.data.green = value.floatValue(); HitboxesConfig.save(); }));
            this.addDrawableChild(new FlatSlider(col1X, startContentY + 75, colWidth, 20, Text.literal("Синий"), HitboxesConfig.data.blue, value -> { HitboxesConfig.data.blue = value.floatValue(); HitboxesConfig.save(); }));
            this.addDrawableChild(new FlatSlider(col1X, startContentY + 100, colWidth, 20, Text.literal("Прозрачность"), HitboxesConfig.data.alpha, value -> { HitboxesConfig.data.alpha = value.floatValue(); HitboxesConfig.save(); }));
            this.addDrawableChild(new FlatSlider(col1X, startContentY + 125, colWidth, 20, Text.literal("Толщина"), (HitboxesConfig.data.lineWidth - 1.0f) / 9.0f, value -> { HitboxesConfig.data.lineWidth = 1.0f + value.floatValue() * 9.0f; HitboxesConfig.save(); }));

            this.addDrawableChild(new FlatToggle(col2X, startContentY, colWidth, 20, Text.literal("Показывать мобов"), HitboxesConfig.data.showMobs, button -> { HitboxesConfig.data.showMobs = ((FlatToggle) button).isToggled(); HitboxesConfig.save(); }));
            this.addDrawableChild(new FlatToggle(col2X, startContentY + 25, colWidth, 20, Text.literal("Показывать игроков"), HitboxesConfig.data.showPlayers, button -> { HitboxesConfig.data.showPlayers = ((FlatToggle) button).isToggled(); HitboxesConfig.save(); }));
            
            String[] pvpExtras = {"Чамы", "Кастом кристал", "Руки", "Цвет хита", "Блинк"};
            for (int i = 0; i < pvpExtras.length; i++) {
                String opt = pvpExtras[i];
                this.addDrawableChild(new FlatToggle(col3X, startContentY + (i * 25), colWidth, 20, Text.literal(opt), TogglesConfig.get("ПВП", opt), button -> { TogglesConfig.set("ПВП", opt, ((FlatToggle) button).isToggled()); }));
            }
            
        } else if (currentTab == 1) {
            // --- ВКЛАДКА ВИЗУАЛ ---
            String[] visCol1 = {"Кастом время", "Кастом погода", "Яркость", "Фейк игрок"};
            for (int i = 0; i < visCol1.length; i++) {
                String opt = visCol1[i];
                this.addDrawableChild(new FlatToggle(col1X, startContentY + (i * 25), colWidth, 20, Text.literal(opt), TogglesConfig.get("ВИЗУАЛ", opt), button -> { TogglesConfig.set("ВИЗУАЛ", opt, ((FlatToggle) button).isToggled()); }));
            }
            String[] visCol2 = {"Цвет неба", "китай. шляпа", "аним. удара", "оптимизация"};
            for (int i = 0; i < visCol2.length; i++) {
                String opt = visCol2[i];
                this.addDrawableChild(new FlatToggle(col2X, startContentY + (i * 25), colWidth, 20, Text.literal(opt), TogglesConfig.get("ВИЗУАЛ", opt), button -> { TogglesConfig.set("ВИЗУАЛ", opt, ((FlatToggle) button).isToggled()); }));
            }
        } else if (currentTab == 2) {
            // --- ВКЛАДКА ПАРТИКЛЫ ---
            String[] partCol1 = {"Стрелы", "Блок частицы", "Частицы урона", "Хит-частицы", "Джамп частицы"};
            for (int i = 0; i < partCol1.length; i++) {
                String opt = partCol1[i];
                this.addDrawableChild(new FlatToggle(col1X, startContentY + (i * 25), colWidth, 20, Text.literal(opt), TogglesConfig.get("ПАРТИКЛ", opt), button -> { TogglesConfig.set("ПАРТИКЛ", opt, ((FlatToggle) button).isToggled()); }));
            }
            String[] partCol2 = {"Килл эффект", "След частиц", "Рофл-частицы"};
            for (int i = 0; i < partCol2.length; i++) {
                String opt = partCol2[i];
                this.addDrawableChild(new FlatToggle(col2X, startContentY + (i * 25), colWidth, 20, Text.literal(opt), TogglesConfig.get("ПАРТИКЛ", opt), button -> { TogglesConfig.set("ПАРТИКЛ", opt, ((FlatToggle) button).isToggled()); }));
            }
        } else if (currentTab == 3) {
            // --- ВКЛАДКА КЛИЕНТ ---
            String[] clientCol1 = {"Хит-саунд", "Кил-стрик", "Комбо звук", "Фейк фпс", "тэги"};
            for (int i = 0; i < clientCol1.length; i++) {
                String opt = clientCol1[i];
                this.addDrawableChild(new FlatToggle(col1X, startContentY + (i * 25), colWidth, 20, Text.literal(opt), TogglesConfig.get("КЛИЕНТ", opt), button -> { TogglesConfig.set("КЛИЕНТ", opt, ((FlatToggle) button).isToggled()); }));
            }
            String[] clientCol2 = {"Кастом ник", "Гуи-звуки", "NoRender", "Интерфейс", "Экран загрузки"};
            for (int i = 0; i < clientCol2.length; i++) {
                String opt = clientCol2[i];
                this.addDrawableChild(new FlatToggle(col2X, startContentY + (i * 25), colWidth, 20, Text.literal(opt), TogglesConfig.get("КЛИЕНТ", opt), button -> { TogglesConfig.set("КЛИЕНТ", opt, ((FlatToggle) button).isToggled()); }));
            }
        } else if (currentTab == 4) {
            // --- ВКЛАДКА HUD ---
            String[] hudCol1 = {"Показывать FPS", "Показывать Пинг", "Координаты", "Ватермарка"};
            for (int i = 0; i < hudCol1.length; i++) {
                String opt = hudCol1[i];
                this.addDrawableChild(new FlatToggle(col1X, startContentY + (i * 25), colWidth, 20, Text.literal(opt), TogglesConfig.get("HUD", opt), button -> { TogglesConfig.set("HUD", opt, ((FlatToggle) button).isToggled()); }));
            }
            String[] hudCol2 = {"Keystrokes", "CPS", "Броня HUD", "Эффекты"};
            for (int i = 0; i < hudCol2.length; i++) {
                String opt = hudCol2[i];
                this.addDrawableChild(new FlatToggle(col2X, startContentY + (i * 25), colWidth, 20, Text.literal(opt), TogglesConfig.get("HUD", opt), button -> { TogglesConfig.set("HUD", opt, ((FlatToggle) button).isToggled()); }));
            }
        } else if (currentTab == 5) {
            // --- ВКЛАДКА ШАБЛОНЫ БРОНИ ---
            int patternIdx = TogglesConfig.getInt("ТРИМЫ", "ПаттернИндекс");
            int materialIdx = TogglesConfig.getInt("ТРИМЫ", "МатериалИндекс");
            if (patternIdx >= PATTERNS.length) patternIdx = 0;
            if (materialIdx >= MATERIALS.length) materialIdx = 0;

            this.addDrawableChild(new FlatButton(col1X, startContentY, colWidth - 60, 20, Text.literal("Шаблон: " + PATTERNS[patternIdx]), button -> {}));
            this.addDrawableChild(new FlatButton(col1X + colWidth - 55, startContentY, 25, 20, Text.literal("<"), button -> { int idx = TogglesConfig.getInt("ТРИМЫ", "ПаттернИндекс") - 1; if (idx < 0) idx = PATTERNS.length - 1; TogglesConfig.setInt("ТРИМЫ", "ПаттернИндекс", idx); this.clearAndInit(); }));
            this.addDrawableChild(new FlatButton(col1X + colWidth - 25, startContentY, 25, 20, Text.literal(">"), button -> { int idx = TogglesConfig.getInt("ТРИМЫ", "ПаттернИндекс") + 1; if (idx >= PATTERNS.length) idx = 0; TogglesConfig.setInt("ТРИМЫ", "ПаттернИндекс", idx); this.clearAndInit(); }));
            
            this.addDrawableChild(new FlatButton(col1X, startContentY + 30, colWidth - 60, 20, Text.literal("Цвет: " + MATERIALS[materialIdx]), button -> {}));
            this.addDrawableChild(new FlatButton(col1X + colWidth - 55, startContentY + 30, 25, 20, Text.literal("<"), button -> { int idx = TogglesConfig.getInt("ТРИМЫ", "МатериалИндекс") - 1; if (idx < 0) idx = MATERIALS.length - 1; TogglesConfig.setInt("ТРИМЫ", "МатериалИндекс", idx); this.clearAndInit(); }));
            this.addDrawableChild(new FlatButton(col1X + colWidth - 25, startContentY + 30, 25, 20, Text.literal(">"), button -> { int idx = TogglesConfig.getInt("ТРИМЫ", "МатериалИндекс") + 1; if (idx >= MATERIALS.length) idx = 0; TogglesConfig.setInt("ТРИМЫ", "МатериалИндекс", idx); this.clearAndInit(); }));

            this.addDrawableChild(new FlatToggle(col1X, startContentY + 65, colWidth, 20, Text.literal("Применять ко всем"), TogglesConfig.get("ТРИМЫ", "Оверрайд"), button -> { TogglesConfig.set("ТРИМЫ", "Оверрайд", ((FlatToggle) button).isToggled()); }));
            this.addDrawableChild(new FlatButton(col1X, startContentY + 100, colWidth, 20, Text.literal("§aПРИМЕНИТЬ").formatted(Formatting.GREEN), button -> { TogglesConfig.save(); if (this.client != null && this.client.player != null) this.client.player.sendMessage(Text.literal("§a[GV] Шаблоны применены!"), true); }));

            this.addDrawableChild(new FlatToggle(col2X, startContentY, colWidth, 20, Text.literal("Показывать контур"), TogglesConfig.get("ТРИМЫ", "Показывать контур"), button -> { TogglesConfig.set("ТРИМЫ", "Показывать контур", ((FlatToggle) button).isToggled()); }));
            this.addDrawableChild(new FlatToggle(col2X, startContentY + 25, colWidth, 20, Text.literal("Показывать узор"), TogglesConfig.get("ТРИМЫ", "Показывать узор"), button -> { TogglesConfig.set("ТРИМЫ", "Показывать узор", ((FlatToggle) button).isToggled()); }));
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (mouseX > 120) {
            scrollOffset += (int) (verticalAmount * 20);
            if (scrollOffset > 0) scrollOffset = 0;
            if (scrollOffset < maxScroll) scrollOffset = maxScroll;
            this.clearAndInit();
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, this.width, this.height, 0xCC0A0A14);
        
        int sidebarWidth = 120;
        
        // 1. Рисуем Scissor (отсечение) для правой части
        context.enableScissor(sidebarWidth + 5, 25, this.width, this.height - 30);
        
        int cardY = 50 + scrollOffset;
        int cardHeight = 230;

        // Карточки теперь используют динамические координаты
        drawCard(context, col1X, cardY, colWidth, cardHeight, getTitle(0));
        drawCard(context, col2X, cardY, colWidth, cardHeight, getTitle(1));
        drawCard(context, col3X, cardY, colWidth, cardHeight, getTitle(2));

        context.disableScissor(); // Отключаем Scissor

        // 2. Рисуем ВСЕ виджеты
        super.render(context, mouseX, mouseY, delta);

        // 3. Рисуем левую панель ПОВЕРХ виджетов, чтобы перекрыть вылезшие элементы
        context.fill(0, 0, sidebarWidth, this.height, 0xEE12121F);
        context.fill(sidebarWidth, 0, sidebarWidth + 2, this.height, 0xFF7A00FF);
        context.drawTextWithShadow(this.textRenderer, "GOBALT", 15, 10, 0xFF7A00FF);
        context.drawTextWithShadow(this.textRenderer, "VISUAL", 15, 22, 0xFFFFFFFF);

        // 4. Рисуем скроллбар
        if (scrollOffset < 0) {
            int scrollbarX = this.width - 10;
            int scrollbarHeight = 100;
            int scrollbarY = (int) (30 + (-scrollOffset / (float) -maxScroll) * (this.height - 60 - scrollbarHeight));
            context.fill(scrollbarX, scrollbarY, scrollbarX + 6, scrollbarY + scrollbarHeight, 0x807A00FF);
        }
    }

    private String getTitle(int col) {
        if (currentTab == 0) return col == 0 ? "Настройки" : (col == 1 ? "Фильтры" : "Дополнительно");
        if (currentTab == 1) return col == 0 ? "Окружение" : (col == 1 ? "Эффекты" : "Прочее");
        if (currentTab == 2) return col == 0 ? "Боевые" : (col == 1 ? "Мирные" : "Прочее");
        if (currentTab == 3) return col == 0 ? "Звуки" : (col == 1 ? "Интерфейс" : "Прочее");
        if (currentTab == 4) return col == 0 ? "Экран" : (col == 1 ? "Виджеты" : "Прочее");
        if (currentTab == 5) return col == 0 ? "Выбор" : (col == 1 ? "Настройки" : "Прочее");
        return "";
    }

    private void drawCard(DrawContext context, int x, int y, int width, int height, String title) {
        context.drawTextWithShadow(this.textRenderer, title, x, y - 15, 0xFFAAAAAA);
        context.fill(x, y, x + width, y + height, 0x80000000);
        context.drawBorder(x, y, width, height, 0xFF333333);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}