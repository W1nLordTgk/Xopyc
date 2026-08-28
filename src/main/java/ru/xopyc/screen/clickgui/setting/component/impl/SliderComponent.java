package ru.xopyc.screen.clickgui.setting.component.impl;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.MathHelper;
import ru.xopyc.screen.clickgui.setting.component.SettingComponent;
import ru.xopyc.module.api.setting.impl.SliderSetting;

import java.awt.*;

// Реализация для SliderSetting
public final class SliderComponent extends SettingComponent<SliderSetting> {
    private boolean dragging;
    private int sliderX;
    private int sliderY;
    private int sliderWidth;

    public SliderComponent(SliderSetting setting) {
        super(setting);
        this.height = 24;
    }

    @Override
    public void render(DrawContext context, int x, int y, int width) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = 24;

        sliderX = x + 4;
        sliderY = y + 16;
        sliderWidth = width - 8;

        float percent = (setting.getValue() - setting.getMin()) / (setting.getMax() - setting.getMin());
        int knobX = sliderX + (int) (sliderWidth * percent);

        context.drawText(textRenderer, setting.getName(), x + 4, y + 3, Color.WHITE.getRGB(), false); // Название
        String value = String.format("%.2f", setting.getValue());
        context.drawText(textRenderer, value, x + width - textRenderer.getWidth(value) - 4, y + 3, 0xFFAAAAAA, false); // Значение справа
        context.fill(sliderX, sliderY, sliderX + sliderWidth, sliderY + 2, 0xFF444444); // Фон линии
        context.fill(sliderX, sliderY, knobX, sliderY + 2, 0xFFFFFFFF); // Заполненная часть
        context.fill(knobX - 3, sliderY - 3, knobX + 3, sliderY + 5, Color.WHITE.getRGB()); // Ползунок
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return false;

        if (mouseX >= sliderX && mouseX <= sliderX + sliderWidth && mouseY >= sliderY - 4 && mouseY <= sliderY + 6) {
            dragging = true;
            updateValue(mouseX);
            return true;
        }

        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button) {
        if (!dragging) return false;
        updateValue(mouseX);
        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        dragging = false;
        return false;
    }

    private void updateValue(double mouseX) {
        float percent = (float) ((mouseX - sliderX) / sliderWidth);
        percent = MathHelper.clamp(percent, 0F, 1F);
        float value = setting.getMin() + percent * (setting.getMax() - setting.getMin());
        value = Math.round(value / setting.getIncrement()) * setting.getIncrement();
        setting.setValue(value);
    }
}