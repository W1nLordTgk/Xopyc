package ru.xopyc.module.impl.visuals;


import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import ru.xopyc.module.api.Module;
import ru.xopyc.module.api.category.Category;
import ru.xopyc.module.api.setting.impl.SliderSetting;

public final class ViewModel extends Module {
    private static final ViewModel instance = new ViewModel();

    public final SliderSetting leftX = new SliderSetting(this, "Левая рука X", 0.0f, -1.0f, 1.0f, 0.1f);
    public final SliderSetting leftY = new SliderSetting(this, "Левая рука Y", 0.0f, -1.0f, 1.0f, 0.1f);
    public final SliderSetting leftZ = new SliderSetting(this, "Левая рука Z", 0.0f, -1.0f, 1.0f, 0.1f);
    public final SliderSetting leftScale = new SliderSetting(this, "Левая рука размер", 1.0f, 0.5f, 1.5f, 0.05f);

    public final SliderSetting rightX = new SliderSetting(this, "Правая рука X", 0.0f, -1.0f, 1.0f, 0.1f);
    public final SliderSetting rightY = new SliderSetting(this, "Правая рука Y", 0.0f, -1.0f, 1.0f, 0.1f);
    public final SliderSetting rightZ = new SliderSetting(this, "Правая рука Z", 0.0f, -1.0f, 1.0f, 0.1f);
    public final SliderSetting rightScale = new SliderSetting(this, "Правая рука размер", 1.0f, 0.5f, 1.5f, 0.05f);

    public ViewModel() {
        super("View Model", Category.VISUALS);
    }


    public void applyHandScale(MatrixStack matrices, Arm arm) {
        if (this.isEnabled()) {
            if (arm == Arm.RIGHT) {
                matrices.scale(rightScale.getValue(), rightScale.getValue(), rightScale.getValue());
            } else {
                matrices.scale(leftScale.getValue(), leftScale.getValue(), leftScale.getValue());
            }
        } else {
            matrices.scale(1.0f, 1.0f, 1.0f);
        }
    }

    public void applyHandPosition(MatrixStack matrices, Arm arm) {
        if (this.isEnabled()) {
            if (arm == Arm.RIGHT) {
                matrices.translate(rightX.getValue(), rightY.getValue(), rightZ.getValue());
            } else {
                matrices.translate(leftX.getValue(), leftY.getValue(), leftZ.getValue());
            }
        } else {
            matrices.translate(0.0f, 0.0f, 0.0f);
        }
    }

    public static ViewModel getInstance() {
        return instance;
    }
}
