package ru.xopyc.module.api.setting.impl;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.math.MathHelper;
import ru.xopyc.module.api.Module;
import ru.xopyc.module.api.setting.Setting;

@Getter
public final class SliderSetting extends Setting {
    private float value;
    private final float min;
    private final float max;
    private final float increment;
    private final String name;


    public SliderSetting(Module module, String name, float value, float min, float max, float increment) {
        super(module, name, SettingType.SLIDER_SETTING);
        this.name = name;
        this.value = value;
        this.min = min;
        this.max = max;
        this.increment = increment;
    }

    public float getValue() {
        return MathHelper.clamp(value, getMin(), getMax());
    }

    public void setValue(float value) {
        this.value = MathHelper.clamp(value, getMin(), getMax());
    }
}
