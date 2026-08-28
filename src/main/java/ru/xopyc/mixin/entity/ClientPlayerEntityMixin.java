package ru.xopyc.mixin.entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.xopyc.event.api.EventManager;
import ru.xopyc.IMinecraft;
import ru.xopyc.event.impl.client.UpdateEvent;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin extends AbstractClientPlayerEntity implements IMinecraft {

    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if (IMinecraft.nullCheck()) return;

        UpdateEvent updateEvent = new UpdateEvent();
        EventManager.getInstance().call(updateEvent);
    }

    /*
    @Redirect(method = "sendMovementPackets", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getYaw()F"))
    public float replaceMovePacketYaw(ClientPlayerEntity instance) {
        Rotation current = RotationManager.get().getCurrentRotation();
        if (instance == mc.player && current != null) {
            return current.yaw();
        }
        // rotationHandler.getServerRotation().setYaw(yaw);
        return instance.getYaw();
    }

    @Redirect(method = "sendMovementPackets", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getPitch()F"))
    public float replaceMovePacketPitch(ClientPlayerEntity instance) {
        Rotation current = RotationManager.get().getCurrentRotation();
        if (instance == mc.player && current != null) {
            return current.pitch();
        }
        // rotationHandler.getServerRotation().setYaw(pitch);
        return instance.getPitch();
    }

     */

    /*
    @Inject(method = "getYaw", at = @At("HEAD"), cancellable = true)
    private void getYaw(float tickDelta, CallbackInfoReturnable<Float> cir) {
        if (RotationManager.getInstance().isRotating()) {
            cir.setReturnValue(RotationManager.getInstance().getCurrentRotation().getYaw());
        }
    }

    @Inject(method = "getPitch", at = @At("HEAD"), cancellable = true)
    private void getPitch(float tickDelta, CallbackInfoReturnable<Float> cir) {
        if (RotationManager.getInstance().isRotating()) {
            cir.setReturnValue(RotationManager.getInstance().getCurrentRotation().getPitch());
        }
    }
     */


}
