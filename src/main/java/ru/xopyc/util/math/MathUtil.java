package ru.xopyc.util.math;

import ru.xopyc.IMinecraft;

public final class MathUtil implements IMinecraft {

    private MathUtil() {
    }

    public static boolean isHovered(double mouseX, double mouseY, double x, double y, double w, double h) {
        return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;
    }

    public static double getMouseRotationStep() {
        double sensitivity = mc.options.getMouseSensitivity().getValue();
        double value = sensitivity * 0.6 + 0.2;

        return value * value * value * 1.2;
    }

    public static double snapToRotationStep(double value, double step) {
        return step <= 1.0E-4 ? value : value - value % step;
    }

    public static double wrapDegrees(double angle) {
        angle %= 360.0;

        if (angle > 180.0) {
            angle -= 360.0;
        }

        if (angle < -180.0) {
            angle += 360.0;
        }

        return angle;
    }
}