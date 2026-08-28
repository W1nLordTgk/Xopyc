package ru.xopyc.mixin.input;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import ru.xopyc.event.api.EventManager;
import ru.xopyc.event.impl.input.MovementInputEvent;
import ru.xopyc.rotation.RotationManager;
import ru.xopyc.rotation.correction.Correction;
import ru.xopyc.util.input.DirectionalInput;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(KeyboardInput.class)
public class KeyboardInputMixin extends InputMixin {

    @ModifyExpressionValue(method = "tick", at = @At(value = "NEW", target = "(ZZZZZZZ)Lnet/minecraft/util/PlayerInput;"))
    private PlayerInput onTick(PlayerInput original) {
        MovementInputEvent movementInputEvent = new MovementInputEvent(original, original.jump(), original.sneak(), original.sprint(), new DirectionalInput(original));
        EventManager.getInstance().call(movementInputEvent);

        DirectionalInput untransformedDirectionalInput = movementInputEvent.getDirectionalInput();
        DirectionalInput directionalInput = transformDirection(untransformedDirectionalInput);

        this.untransformed = new PlayerInput(untransformedDirectionalInput.isW(), untransformedDirectionalInput.isS(), untransformedDirectionalInput.isA(), untransformedDirectionalInput.isD(), original.jump(), original.sneak(), original.sprint());

        return new PlayerInput(directionalInput.isW(), directionalInput.isS(), directionalInput.isA(), directionalInput.isD(), movementInputEvent.isJump(), movementInputEvent.isSneak(), movementInputEvent.isSprint());
    }

    @Unique
    private DirectionalInput transformDirection(DirectionalInput input) {
        RotationManager rotationManager = RotationManager.getInstance();

        if (!rotationManager.isRotating()) return input;

        float z = KeyboardInput.getMovementMultiplier(input.isW(), input.isS());
        float x = KeyboardInput.getMovementMultiplier(input.isA(), input.isD());

        float yaw = rotationManager.getCurrentRotation().getYaw();
        float direction = rotationManager.getVisualRotation().getYaw();

        if (
                false
                // MoveFixModule.isTargeting() && !(z != 1 || x != 0)
        ) {
//            AuraModule auraModule = AuraModule.getInstance();
//
//            Vec3d position = auraModule.target != null ? auraModule.target.getPos() : null;
//
//            if (position == null) {
//                return input;
//            }
//
//            double deltaX = position.x - mc.player.getX();
//            double deltaZ = position.z - mc.player.getZ();
//
//            double angleToTarget = Math.toDegrees(Math.atan2(deltaZ, deltaX)) - 90.0;
//            angleToTarget = MathHelper.wrapDegrees(angleToTarget);
//
//            float bestForward = 0F;
//            float bestStrafe = 0F;
//            float minDifference = Float.MAX_VALUE;
//
//            for (float forward = -1F; forward <= 1F; forward += 1F) {
//                for (float strafe = -1F; strafe <= 1F; strafe += 1F) {
//                    if (forward == 0F && strafe == 0F) {
//                        continue;
//                    }
//
//                    double moveAngle = MoveUtil.direction(yaw, forward, strafe);
//                    moveAngle = Math.toDegrees(moveAngle);
//                    moveAngle = MathHelper.wrapDegrees(moveAngle);
//
//                    double difference = Math.abs(MathHelper.wrapDegrees(angleToTarget - moveAngle));
//                    difference = Math.min(difference, 360 - difference);
//
//                    if (difference < minDifference) {
//                        minDifference = (float) difference;
//                        bestForward = forward;
//                        bestStrafe = strafe;
//                    }
//                }
//            }
//            return new DirectionalInput(bestForward, bestStrafe);
        } else if (rotationManager.getCorrection() == Correction.FREE) {
            float deltaYaw = direction - yaw;
            float radians = (float) Math.toRadians(deltaYaw);

            float newX = x * MathHelper.cos(radians) - z * MathHelper.sin(radians);
            float newZ = z * MathHelper.cos(radians) + x * MathHelper.sin(radians);

            int movementSideways = Math.round(newX);
            int movementForward = Math.round(newZ);

            return new DirectionalInput(movementForward, movementSideways);
        }

        return input;
    }
}
