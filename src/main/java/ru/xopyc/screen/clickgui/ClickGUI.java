package ru.xopyc.screen.clickgui;

import lombok.Getter;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import ru.xopyc.IMinecraft;
import ru.xopyc.screen.clickgui.bind.KeyBindingStorage;
import ru.xopyc.screen.clickgui.setting.SettingsWindow;
import ru.xopyc.util.math.MathUtil;
import ru.xopyc.module.api.Module;
import ru.xopyc.module.api.ModuleManager;
import ru.xopyc.module.api.category.Category;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class ClickGUI extends Screen implements IMinecraft {

    @Getter
    private static final ClickGUI instance = new ClickGUI();

    private final List<Panel> panels = new ArrayList<>();
    private final List<SettingsWindow> windows = new ArrayList<>();
    private Module bindingModule;

    private ClickGUI() {
        super(Text.literal("ClickGUI"));
    }

    @Override
    protected void init() {
        panels.clear();

        int panelWidth = 110;
        int step = 116;

        int x = (width - (Category.values().length - 1) * step - panelWidth) / 2 + 4;
        int y = 10;

        for (Category category : Category.values()) {
            panels.add(new Panel(category, x, y));
            x += 116;
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        panels.forEach(panel -> panel.render(context, mouseX, mouseY));
        windows.forEach(window -> window.render(context, mouseX, mouseY));
        // super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (mouseClicked2(mouseX, mouseY, button)) return true;
        panels.forEach(panel -> panel.mouseClicked(mouseX, mouseY, button));
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyPressed2(keyCode, scanCode, modifiers)) return false;
        panels.forEach(panel -> panel.keyPressed(keyCode, scanCode, modifiers));
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (mouseDragged2(mouseX, mouseY, button, deltaX, deltaY)) return true;
        panels.forEach(panel -> panel.mouseDragged(mouseX, mouseY, button, deltaX, deltaY));
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (mouseReleased2(mouseX, mouseY, button)) return false;
        panels.forEach(panel -> panel.mouseReleased(mouseX, mouseY, button));
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (mouseScrolled2(mouseX, mouseY, horizontalAmount, verticalAmount)) return true;
        panels.forEach(panel -> panel.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount));
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    public boolean mouseClicked2(double mouseX, double mouseY, int button) {

        // сверху вниз
        for (int i = windows.size() - 1; i >= 0; i--) {

            SettingsWindow window = windows.get(i);

            if (window.mouseClicked(mouseX, mouseY, button)) {

                // переносим окно наверх (z-order)
                windows.remove(i);
                windows.add(window);

                return true;
            }
        }

        return false;
    }

    public boolean mouseDragged2(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        for (int i = windows.size() - 1; i >= 0; i--) {
            if (windows.get(i).mouseDragged(mouseX, mouseY, button)) return true;
        }
        return false;
    }
    public boolean keyPressed2(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    public boolean mouseScrolled2(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        return false;
    }

    public boolean mouseReleased2(double mouseX, double mouseY, int button) {

        for (SettingsWindow window : windows) {
            window.mouseReleased(mouseX, mouseY, button);
        }

        return false;
    }

    public void toggleSetting(Module module) {
        Iterator<SettingsWindow> iterator = windows.iterator();
        while (iterator.hasNext()) {
            SettingsWindow window = iterator.next();
            if (window.getModule() == module) {
                iterator.remove();
                return;
            }
        }
        float x = 50 + windows.size() * 25, y = 50 + windows.size() * 25;
        windows.add(new SettingsWindow(module, x, y));
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private final class Panel {

        private final int x, y;
        private final Category category;
        private final List<Module> modules;

        private boolean binding;

        public Panel(Category category, int x, int y) {
            this.category = category;
            this.x = x;
            this.y = y;
            modules = ModuleManager.getInstance().getModules().stream().filter(m -> m.getCategory() == category).toList();
        }

        public void render(DrawContext context, int mouseX, int mouseY) {
            int px = x - 4;
            int py = 10;
            int panelWidth = 110;
            int panelHeight = modules.size() * 19 + 16 + 3;
            // фон панели
            context.fill(px, py + 4, px + panelWidth, py + panelHeight, new Color(0xFFE3E3E3).getRGB());
            // шапка
            context.fill(px, py, px + panelWidth, py + 16, new Color(0xFF4D88FF).getRGB());
            context.drawCenteredTextWithShadow(IMinecraft.textRenderer, category.name(), px + panelWidth / 2, 14, new Color(0xFFFFFFFF).getRGB());
            int yy = py + 17;
            for (Module module : modules) {
                int rx = px + 2;
                int ry = yy + 2;
                int rw = panelWidth - 4;
                int rh = 16;
                // белая карточка
                context.fill(rx, ry, rx + rw, ry + rh, module.isEnabled() ? new Color(0xFF4D88FF).getRGB() : new Color(0xFFFFFFFF).getRGB());
                context.drawText(IMinecraft.textRenderer, module.getName(), rx + 5, ry + 4, module.isEnabled() ? new Color(0xFFFFFFFF).getRGB() : new Color(0xFF111111).getRGB(), false);
                String bind = binding && bindingModule == module ? "..." : KeyBindingStorage.getInstance().getKey(module.getBind());
                context.drawText(IMinecraft.textRenderer, bind, rx + rw - 5 - IMinecraft.textRenderer.getWidth(bind), ry + 4, module.isEnabled() ? new Color(0xFFFFFFFF).getRGB() : new Color(0xFF111111).getRGB(), false);
                yy += 19;
            }
        }

        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            int yy = 27;

            for (Module module : modules) {
                if (MathUtil.isHovered(mouseX, mouseY, x - 2, yy + 2, 106, 16)) {
                    switch (button) {
                        case GLFW.GLFW_MOUSE_BUTTON_LEFT -> module.toggle();
                        case GLFW.GLFW_MOUSE_BUTTON_RIGHT -> {
                            if (!module.getSettings().isEmpty()) {
                                toggleSetting(module);
                            }
                        }
                        case GLFW.GLFW_MOUSE_BUTTON_MIDDLE -> {
                            binding = true;
                            bindingModule = module;
                        }
                    }
                    return true;
                }
                yy += 19;
            }
            return false;
        }

        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
            if (!binding || bindingModule == null) return false;
            bindingModule.setBind(keyCode == GLFW.GLFW_KEY_ESCAPE ? 0 : keyCode);
            binding = false;
            bindingModule = null;
            return true;
        }

        public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
            return false;
        }

        public boolean mouseReleased(double mouseX, double mouseY, int button) {
            return false;
        }

        public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
            return false;
        }
    }
}