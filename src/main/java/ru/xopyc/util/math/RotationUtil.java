package ru.xopyc.util.math;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import ru.xopyc.rotation.Rotation;

public final class RotationUtil {

    private RotationUtil() {}

    public static Rotation lookAt(Vec3d from, Vec3d to) {
        double x = to.x - from.x, y = to.y - from.y, z = to.z - from.z;
        double horizontal = Math.hypot(x, z);

        float yaw = (float) Math.toDegrees(Math.atan2(z, x)) - 90.0F;
        float pitch = (float) -Math.toDegrees(Math.atan2(y, horizontal));

        return new Rotation(yaw, pitch);
    }

    public static Rotation delta(Rotation from, Rotation to) {
        return new Rotation(MathHelper.wrapDegrees(to.getYaw() - from.getYaw()), to.getPitch() - from.getPitch());
    }
}