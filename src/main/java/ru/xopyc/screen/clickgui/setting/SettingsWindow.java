package ru.xopyc.screen.clickgui.setting;

import net.minecraft.client.gui.DrawContext;
import ru.xopyc.module.api.setting.impl.MultiBoxSetting;
import ru.xopyc.screen.clickgui.setting.component.SettingComponent;
import ru.xopyc.screen.clickgui.setting.component.impl.ModeComponent;
import ru.xopyc.screen.clickgui.setting.component.impl.MultiBoxComponent;
import ru.xopyc.screen.clickgui.setting.component.impl.SliderComponent;
import ru.xopyc.util.client.Draggable;
import ru.xopyc.module.api.Module;
import ru.xopyc.module.api.setting.Setting;
import ru.xopyc.module.api.setting.impl.ModeSetting;
import ru.xopyc.module.api.setting.impl.SliderSetting;
import ru.xopyc.IMinecraft;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// Рисует draggable окно с настройками
public final class SettingsWindow implements IMinecraft {

    private final Module module;
    private final Draggable draggable;

    private final List<SettingComponent<?>> settingComponents = new ArrayList<>();

    private boolean dragging;

    private float dragX;
    private float dragY;

    private static final int WIDTH = 170;
    private static final int HEADER = 18;
    private static final int PADDING = 4;

    public SettingsWindow(Module module, float x, float y) {
        this.module = module;
        this.draggable = new Draggable(x, y, WIDTH, HEADER);

        buildComponents();
        updateHeight();
    }

    private void buildComponents() {
        settingComponents.clear();

        for (Setting setting : module.getSettings()) {

            switch (setting.getType()) {
                case SLIDER_SETTING -> settingComponents.add(new SliderComponent((SliderSetting) setting));
                case MODE_SETTING -> settingComponents.add(new ModeComponent((ModeSetting) setting));
                case MULTI_BOX_SETTING -> settingComponents.add(new MultiBoxComponent((MultiBoxSetting) setting));
            }

            // дальше:
            // BooleanComponent
            // BindComponent
            // ColorComponent
        }
    }

    private void updateHeight() {

        int h = HEADER;

        for (SettingComponent<?> settingComponent : settingComponents) {
            h += settingComponent.getHeight();
        }

        draggable.setSize(WIDTH, h + PADDING);
    }

    public void render(DrawContext context, int mouseX, int mouseY) {

        updateHeight();

        int x = (int) draggable.getX();
        int y = (int) draggable.getY();

        // фон
        context.fill(
                x,
                y,
                x + (int) draggable.getW(),
                y + (int) draggable.getH(),
                new Color(20, 20, 20, 220).getRGB()
        );

        // шапка
        context.fill(
                x,
                y,
                x + (int) draggable.getW(),
                y + HEADER,
                new Color(35, 35, 35).getRGB()
        );

        context.drawText(
                textRenderer,
                module.getName(),
                x + 5,
                y + 5,
                Color.WHITE.getRGB(),
                false
        );

        int componentY = y + HEADER;

        for (SettingComponent<?> settingComponent : settingComponents) {

            settingComponent.render(
                    context,
                    x + PADDING,
                    componentY,
                    (int) draggable.getW() - PADDING * 2
            );

            componentY += settingComponent.getHeight();
        }

        renderOverlay(context, mouseX, mouseY);
    }

    /**
     * Popup-рендер (ModeComponent и т.п.)
     * Вызывается менеджером ПОСЛЕ рендера всех окон.
     */
    private void renderOverlay(DrawContext context, int mouseX, int mouseY) {
        for (SettingComponent<?> settingComponent : settingComponents) {
            settingComponent.renderOverlay(context, mouseX, mouseY);
        }
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {

        // Сначала popup'ы
        for (SettingComponent<?> settingComponent : settingComponents) {
            if (settingComponent.mouseClickedOverlay(mouseX, mouseY, button)) {
                return true;
            }
        }

        // Если хоть один popup всё ещё открыт — дальше клик не пропускаем
        for (SettingComponent<?> settingComponent : settingComponents) {
            if (settingComponent instanceof ModeComponent mode && mode.isExpanded()) {
                return true;
            }
        }

        // Затем обычные компоненты
        for (SettingComponent<?> settingComponent : settingComponents) {
            if (settingComponent.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
        }

        // Заголовок окна
        if (button == 0 && mouseX >= draggable.getX() && mouseX <= draggable.getX() + draggable.getW() && mouseY >= draggable.getY() && mouseY <= draggable.getY() + HEADER) {
            dragging = true;

            dragX = (float) mouseX - draggable.getX();
            dragY = (float) mouseY - draggable.getY();

            return true;
        }

        return false;
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button) {
        for (SettingComponent<?> settingComponent : settingComponents) {
            if (settingComponent.mouseDragged(mouseX, mouseY, button)) {
                return true;
            }
        }

        if (!dragging) {
            return false;
        }

        draggable.setPosition((float) mouseX - dragX, (float) mouseY - dragY);

        return true;
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        dragging = false;

        for (SettingComponent<?> settingComponent : settingComponents) {
            settingComponent.mouseReleased(mouseX, mouseY, button);
        }

        return false;
    }

    public Module getModule() {
        return module;
    }

    public Draggable getDraggable() {
        return draggable;
    }
}