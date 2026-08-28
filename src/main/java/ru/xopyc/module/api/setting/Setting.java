package ru.xopyc.module.api.setting;

import lombok.Getter;
import ru.xopyc.module.api.Module;

@Getter
public abstract class Setting {
    private final String name;
    private final SettingType type;

    public Setting(Module module, String name, SettingType type) {
        this.name = name;
        this.type = type;

        module.getSettings().add(this); // Сама себя регистрирует
    }

    public enum SettingType {
        MODE_SETTING, SLIDER_SETTING, MULTI_BOX_SETTING
    }
}
