package ru.xopyc.module.impl.visuals;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import ru.xopyc.module.api.Module;
import ru.xopyc.module.api.category.Category;
import ru.xopyc.module.api.setting.impl.ModeSetting;
import ru.xopyc.module.api.setting.impl.SliderSetting;

public final class SwordAnimation extends Module {
    private static final SwordAnimation instance = new SwordAnimation();

    private SwordAnimation() {
        super("Sword Animation", Category.VISUALS);
    }

    private final ModeSetting mode = new ModeSetting(this, "Отображение", "Mode 1", "Mode 1", "Mode 2", "Mode 3", "Mode 4", "Mode 5", "Mode 6");
    private final SliderSetting power = new SliderSetting(this, "Сила анимации", 5.0F, 1.0F, 10.0F, 1.0F);
    private final SliderSetting speed = new SliderSetting(this, "Скорость анимации", 6.0F, 1.0F, 20.0F, 1.0F);

    public void animationProcess(MatrixStack matrices, float swingProgress, Arm handSide) {
        float anim = (float) Math.sin(swingProgress * (Math.PI / 2) * 2);

        float powerVal = power.getValue();
        float speedVal = speed.getValue();
        int handSign = handSide == Arm.RIGHT ? 1 : -1;
        int i = handSide == Arm.RIGHT ? 1 : -1;

        switch (mode.get()) {
            case "Mode 1":
                float swingAngle = -90 - (powerVal * 10) * anim;
                float currentAngle = MathHelper.lerp(anim, -90, swingAngle);
                applyEquipOffset(matrices, handSide, 0.0F);
                matrices.scale(1.0F, 1.0F, 1.0F);
                matrices.translate(handSign * 0.4f, 0.1f, -0.5);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(handSign * 90));
                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(handSign * -60));
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(currentAngle));
                break;
            case "Mode 2": {
                applyEquipOffset(matrices, handSide, 0.0F);
                matrices.scale(1.0F, 1.0F, 1.0F);
                matrices.translate(0.0f, 0.1f, 0.0f);

                if (swingProgress > 1.0e-3f) {
                    float eased = MathHelper.sin(swingProgress * ((float) Math.PI) * 0.5F);
                    float rotationAngle = eased * 360.0f * (10 / 10.0f);
                    if (rotationAngle > 360.0f) rotationAngle = 360.0f;

                    matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(rotationAngle));
                }
                break;
            }
            case "Mode 3":
                applyEquipOffset(matrices, handSide, 0.0F);
                matrices.scale(1.0F, 1.0F, 1.0F);
                matrices.translate(handSign * 0.4f, 0, -0.5f);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(handSign * 90));
                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(handSign * -30));
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90 - (powerVal * 10) * anim));
                break;
            case "Mode 4":
                applyEquipOffset(matrices, handSide, 0.0F);
                matrices.scale(1.0F, 1.0F, 1.0F);
                matrices.translate(handSign * 0.8F, -0.5F, -0.71999997F);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(handSign * 45.0F));
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(anim * -20.0F * speedVal / 10.0F));
                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(anim * -20.0F * powerVal / 10.0F));
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(anim * -40.0F * powerVal / 10.0F));
                matrices.translate(handSign * -0.1F, 0.2F, 0.0F);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(handSign * 30.0F));
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0F));
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(handSign * 60.0F));
                break;
            case "Mode 5":
                applyEquipOffset(matrices, handSide, 0.0F);
                matrices.scale(1.0F, 1.0F, 1.0F);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(i * (45.0F + MathHelper.sin(swingProgress * swingProgress * (float) Math.PI) * (speed.getValue() / 10.0f) * -20.0F)));
                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(i * MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) Math.PI) * (power.getValue() / 10.0f) * -20.0F));
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) Math.PI) * (power.getValue() / 10.0f) * -80.0F));
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(i * -45.0F));
                break;
            case "Mode 6":
                applyEquipOffset(matrices, handSide, 0.0F);
                matrices.scale(1.0F, 1.0F, 1.0F);
                float offset = MathHelper.sin(swingProgress * (float) Math.PI);
                matrices.translate(0.0F, offset * -0.6F * (powerVal / 10.0F), 0.0F);
                break;
            default:
                break;
        }
    }
    private void applyEquipOffset(MatrixStack matrices, Arm arm, float equipProgress) {
        int i = arm == Arm.RIGHT ? 1 : -1;
        matrices.translate((float) i * 0.56F, -0.52F + equipProgress * -0.6F, -0.72F);
    }

    public SliderSetting getSpeed() {
        return speed;
    }

    public ModeSetting getMode() {
        return mode;
    }

    public static SwordAnimation getInstance() {
        return instance;
    }
}