package ru.xopyc.mixin.entity;

import net.minecraft.entity.projectile.FireworkRocketEntity;
import org.spongepowered.asm.mixin.Mixin;
import ru.xopyc.IMinecraft;

@Mixin(FireworkRocketEntity.class)
public abstract class FireworkRocketEntityMixin implements IMinecraft {


    /*
    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getRotationVector()Lnet/minecraft/util/math/Vec3d;"))
    private Vec3d redirectGetRotationVector(LivingEntity instance) {
        Rotation current = RotationManager.get().getCurrentRotation();
        if (instance == mc.player && current != null) {
            return Vec3d.fromPolar(current.pitch(), current.yaw());
        }

        return instance.getRotationVector();
    }
     */

}
