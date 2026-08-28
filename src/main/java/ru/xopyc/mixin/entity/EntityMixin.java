package ru.xopyc.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import ru.xopyc.rotation.correction.Correction;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.xopyc.rotation.Rotation;
import ru.xopyc.rotation.RotationManager;
import ru.xopyc.IMinecraft;

@Mixin(Entity.class)
public class EntityMixin implements IMinecraft {

    @ModifyExpressionValue(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isControlledByPlayer()Z"))
    private boolean fixFallDistance(boolean original) {
        if ((Entity) (Object) this == mc.player) {
            return false;
        }
        return original;
    }


    /*
    @Redirect(method = "updateVelocity", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getYaw()F"))
    public float syncMovementInput(Entity instance) {
        Rotation visualRotation = RotationManager.getInstance().getVisualRotation();
        if (instance == mc.player && RotationManager.getInstance().isRotating()) {
            return visualRotation.getYaw();
        }
        return instance.getYaw();
    }
     */

    /**
     * Изменяет не настоящую ротацию, а визуальную, если коррекция FREE
     * Не дает сдвинуть камеру игроку, пока идет ротация
     */
    @Inject(method = "changeLookDirection", at = @At("HEAD"), cancellable = true)
    private void setVisualRotation(double cursorDeltaX, double cursorDeltaY, CallbackInfo ci) {
        if ((Entity) (Object) this == mc.player && RotationManager.getInstance().isRotating()) {
            if (RotationManager.getInstance().getCorrection() == Correction.FREE) {
                Rotation visualRotation = RotationManager.getInstance().getVisualRotation();

                float pitchDelta = (float) cursorDeltaY * 0.15F, yawDelta = (float) cursorDeltaX * 0.15F;

                visualRotation.setYaw(visualRotation.getYaw() + yawDelta);
                visualRotation.setPitch(visualRotation.getPitch() + pitchDelta);

                // Фиксированный угол по вертикали
                visualRotation.setPitch(MathHelper.clamp(visualRotation.getPitch(), -90.0F, 90.0F));
            }
            ci.cancel();
        }
    }
}
