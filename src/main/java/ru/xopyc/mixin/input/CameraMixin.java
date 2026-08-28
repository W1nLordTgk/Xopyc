package ru.xopyc.mixin.input;

import net.minecraft.client.render.Camera;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import ru.xopyc.IMinecraft;
import ru.xopyc.rotation.RotationManager;

@Mixin(Camera.class)
public abstract class CameraMixin implements IMinecraft {

    /**
     * Передает камеру текущей ротации
     */
    @ModifyArgs(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V"))
    private void rotationHook(Args args) {
        RotationManager rotationManager = RotationManager.getInstance();
        if (!rotationManager.isRotating()) return;

        Camera camera = (Camera) (Object) this;
        float yaw = camera.getYaw(), pitch = camera.getPitch(), tickDelta = camera.getLastTickDelta();

        switch (rotationManager.getCorrection()) {
            /*
            case NONE -> {
                return; // Просто ничего не делаем
            }
             */
            case FREE -> {
                yaw = rotationManager.getVisualRotation().getYaw();
                pitch = rotationManager.getVisualRotation().getPitch();
            }
            case FOCUSED -> {
                yaw = MathHelper.lerpAngleDegrees(tickDelta, rotationManager.getPreviousRotation().getYaw(), rotationManager.getCurrentRotation().getYaw());
                pitch = MathHelper.lerp(tickDelta, rotationManager.getPreviousRotation().getPitch(), rotationManager.getCurrentRotation().getPitch());
            }
        }

        args.set(0, yaw);
        args.set(1, pitch);
    }

}