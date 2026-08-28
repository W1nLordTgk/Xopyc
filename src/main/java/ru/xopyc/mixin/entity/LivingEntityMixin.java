package ru.xopyc.mixin.entity;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.xopyc.event.api.EventManager;
import ru.xopyc.IMinecraft;
import ru.xopyc.event.impl.entity.JumpEvent;
import ru.xopyc.module.impl.visuals.SwordAnimation;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements IMinecraft {

    @Inject(method = "jump", at = @At("HEAD"), cancellable = true)
    private void jump(CallbackInfo ci) {
        JumpEvent jumpEvent = new JumpEvent((LivingEntity) (Object) this);
        EventManager.getInstance().call(jumpEvent);
        if (jumpEvent.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "getHandSwingDuration", at = @At("HEAD"), cancellable = true)
    private void injectCustomHandSwingDuration(CallbackInfoReturnable<Integer> cir) {
        SwordAnimation animation = SwordAnimation.getInstance();
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        if (animation.isEnabled() && livingEntity == mc.player) {
            cir.setReturnValue((int) animation.getSpeed().getValue());
        }
    }


    /*
    @ModifyExpressionValue(method = "jump", at = @At(value = "NEW", target = "(DDD)Lnet/minecraft/util/math/Vec3d;"))
    public Vec3d movementCorrection(Vec3d original) {
        Rotation current = RotationManager.get().getCurrentRotation();
        if ((LivingEntity) (Object) this == mc.player && current != null) {
            float yaw = current.yaw() * (float) (Math.PI / 180.0);
            return new Vec3d(-MathHelper.sin(yaw) * 0.2F, 0.0, MathHelper.cos(yaw) * 0.2F);
        }
        return original;
    }


    @Redirect(method = "calcGlidingVelocity(Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getPitch()F"))
    private float redirectGetPitch(LivingEntity instance) {
        Rotation current = RotationManager.get().getCurrentRotation();
        if (instance == mc.player && current != null) {
            return current.pitch();
        }
        return instance.getPitch();
    }

    @Redirect(method = "calcGlidingVelocity(Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getRotationVector()Lnet/minecraft/util/math/Vec3d;"))
    private Vec3d redirectGetRotationVector(LivingEntity instance) {
        Rotation current = RotationManager.get().getCurrentRotation();
        if (instance == mc.player && current != null) {
            return current.getRotationVector();
        }
        return instance.getRotationVector();
    }

     */


}
