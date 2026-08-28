package ru.xopyc.rotation;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;;

@Data
@AllArgsConstructor
public final class Rotation {
    private float yaw, pitch;
    public Vec3d getRotationVector() {
        float pitchRad = (float) Math.toRadians(pitch);
        float yawRad = (float) Math.toRadians(yaw);

        float cosPitch = MathHelper.cos(pitchRad);
        float sinPitch = MathHelper.sin(pitchRad);

        float cosYaw = MathHelper.cos(yawRad);
        float sinYaw = MathHelper.sin(yawRad);

        return new Vec3d(-sinYaw * cosPitch, -sinPitch, cosYaw * cosPitch);
    }
}
