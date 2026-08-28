package ru.xopyc.module.api.setting.impl;

import ru.xopyc.module.api.Module;
import ru.xopyc.module.api.setting.Setting;

import java.util.HashSet;
import java.util.Set;

public final class MultiBoxSetting extends Setting {
    private final String[] modes;
    private final Set<String> selected = new HashSet<>();

    public MultiBoxSetting(Module module, String settingName, String... modes) {
        super(module, settingName, SettingType.MULTI_BOX_SETTING);
        this.modes = modes;
    }

    public void select(String mode) {
        if (!isSelected(mode)) {
            selected.add(mode);
        }
    }

    public void unselect(String mode) {
        selected.remove(mode);
    }

    public boolean isSelected(String mode) {
        return selected.contains(mode);
    }

    public Set<String> getSelected() {
        return selected;
    }

    public String[] getModes() {
        return modes;
    }
}
