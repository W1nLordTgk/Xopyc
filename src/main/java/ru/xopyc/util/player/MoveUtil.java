package ru.xopyc.util.player;

import ru.xopyc.IMinecraft;

public final class MoveUtil implements IMinecraft {
    public static boolean w() {
        return mc.options.forwardKey.isPressed();
    }

    public static boolean s() {
        return mc.options.backKey.isPressed();
    }

    public static boolean a() {
        return mc.options.leftKey.isPressed();
    }

    public static boolean d() {
        return mc.options.rightKey.isPressed();
    }

    public static double direction(float rotationYaw, final float moveForward, final float moveStrafing) {
        if (moveForward < 0F) rotationYaw += 180F;
        float forward = 1F;
        if (moveForward < 0F) forward = -0.5F;
        if (moveForward > 0F) forward = 0.5F;
        if (moveStrafing > 0F) rotationYaw -= 90F * forward;
        if (moveStrafing < 0F) rotationYaw += 90F * forward;
        return Math.toRadians(rotationYaw);
    }
}
