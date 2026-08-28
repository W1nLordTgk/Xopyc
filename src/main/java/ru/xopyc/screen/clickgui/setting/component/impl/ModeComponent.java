package ru.xopyc.screen.clickgui.setting.component.impl;

import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;
import ru.xopyc.screen.clickgui.setting.component.SettingComponent;
import ru.xopyc.module.api.setting.impl.ModeSetting;

import java.awt.*;

public final class ModeComponent extends SettingComponent<ModeSetting> {

    private boolean expanded;
    private static final int ENTRY_HEIGHT = 14;

    public ModeComponent(ModeSetting setting) {
        super(setting);
        this.height = 24;
    }

    @Override
    public void render(DrawContext context, int x, int y, int width) {
        this.x = x;
        this.y = y;
        this.width = width;

        context.drawText(textRenderer, setting.getName(), x, y + 2, Color.WHITE.getRGB(), false);
        context.fill(x, y + 12, x + width, y + 24, new Color(40, 40, 40).getRGB());
        context.drawBorder(x, y + 12, width, 12, new Color(65, 65, 65).getRGB());
        String text = setting.get() + (expanded ? " ▲" : " ▼");
        context.drawText(textRenderer, text, x + 4, y + 14, Color.WHITE.getRGB(), false);
    }

    @Override
    public void renderOverlay(DrawContext context, int mouseX, int mouseY) {
        if (!expanded) return;

        int popupY = y + 24;
        int popupHeight = setting.getModes().length * ENTRY_HEIGHT;

        context.fill(x, popupY, x + width, popupY + popupHeight, new Color(24, 24, 24, 245).getRGB());
        context.drawBorder(x, popupY, width, popupHeight, new Color(60, 60, 60).getRGB());

        int yy = popupY;

        for (String mode : setting.getModes()) {
            if (mode.equals(setting.get())) {
                context.fill(x + 1, yy + 1, x + width - 1, yy + ENTRY_HEIGHT - 1, new Color(52, 52, 52).getRGB());
            }
            context.drawText(textRenderer, mode, x + 4, yy + 3, Color.WHITE.getRGB(), false);
            yy += ENTRY_HEIGHT;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != GLFW.GLFW_MOUSE_BUTTON_LEFT && button != GLFW.GLFW_MOUSE_BUTTON_RIGHT) return false;

        if (mouseX >= x && mouseX <= x + width && mouseY >= y + 12 && mouseY <= y + 24) {
            expanded = !expanded;
            return true;
        }

        return false;
    }

    @Override
    public boolean mouseClickedOverlay(double mouseX, double mouseY, int button) {

        if (!expanded) return false;

        if (button != GLFW.GLFW_MOUSE_BUTTON_LEFT && button != GLFW.GLFW_MOUSE_BUTTON_RIGHT) return false;

        // Клик по самому компоненту при открытом списке — просто закрываем
        if (mouseX >= x && mouseX <= x + width && mouseY >= y + 12 && mouseY <= y + 24) {
            expanded = false;
            return true;
        }

        int popupY = y + 24;
        int popupHeight = setting.getModes().length * ENTRY_HEIGHT;

        if (mouseX < x || mouseX > x + width || mouseY < popupY || mouseY > popupY + popupHeight) {

            expanded = false;
            return false;
        }

        int index = (int)((mouseY - popupY) / ENTRY_HEIGHT);

        if (index >= 0 && index < setting.getModes().length) {
            setting.setIndex(index);
        }

        expanded = false;
        return true;
    }

    public boolean isExpanded() {
        return expanded;
    }
}