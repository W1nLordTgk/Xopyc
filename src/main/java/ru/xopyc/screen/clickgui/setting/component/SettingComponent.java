package ru.xopyc.screen.clickgui.setting.component;

import net.minecraft.client.gui.DrawContext;
import ru.xopyc.IMinecraft;
import ru.xopyc.module.api.setting.Setting;
import ru.xopyc.util.math.MathUtil;

// Отрисовка для каждой настройки (абстракция)
public abstract class SettingComponent<T extends Setting> implements IMinecraft {
    protected final T setting;

    protected int x;
    protected int y;
    protected int width;
    protected int height;

    public SettingComponent(T setting) {
        this.setting = setting;
    }

    public abstract void render(DrawContext context, int x, int y, int width);

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button) {
        return false;
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return false;
    }

    protected boolean hovered(double mouseX, double mouseY) {
        return hovered(mouseX, mouseY, x, y, width, height);
    }

    protected boolean hovered(double mouseX, double mouseY, int x, int y, int width, int height) {
        return MathUtil.isHovered(mouseX, mouseY, x, y, width, height);
    }

    public T getSetting() {
        return setting;
    }

    public int getHeight() {
        return height;
    }

    public void renderOverlay(DrawContext context, int mouseX, int mouseY) {
    }

    public boolean mouseClickedOverlay(double mouseX, double mouseY, int button) {
        return false;
    }
}