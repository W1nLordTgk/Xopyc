package ru.xopyc.module.api.setting.impl;

import ru.xopyc.module.api.Module;
import ru.xopyc.module.api.setting.Setting;

import java.util.Arrays;


public final class ModeSetting extends Setting {

    private int index;
    public final String[] modes;

    public ModeSetting(Module module, String name, String current, String... modes) {
        super(module, name, SettingType.MODE_SETTING);
        this.modes = modes;
        this.index = Arrays.asList(modes).indexOf(current);
    }

    public boolean is(String mode) {
        return get().equals(mode);
    }

    public String get() {
        try {
            if (index < 0 || index >= modes.length) {
                return modes[0];
            }
            return modes[index];
        } catch (ArrayIndexOutOfBoundsException e) {
            return "ERROR";
        }
    }

    public void set(String mode) {
        this.index = Arrays.asList(modes).indexOf(mode);
    }

    public void set(int mode) {
        this.index = mode;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public String[] getModes() {
        return modes;
    }
}
