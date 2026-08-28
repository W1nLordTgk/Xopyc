package ru.xopyc.mixin.entity;

import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import ru.xopyc.IMinecraft;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements IMinecraft {

    /*

    @Redirect(method = "travel(Lnet/minecraft/util/math/Vec3d;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getRotationVector()Lnet/minecraft/util/math/Vec3d;"))
    private Vec3d redirectGetRotationVectorInTravel(PlayerEntity instance) {
        Rotation current = RotationManager.get().getCurrentRotation();
        if (instance == mc.player && current != null) {
            return current.getRotationVector();
        }
        return instance.getRotationVector();
    }

     */


}
