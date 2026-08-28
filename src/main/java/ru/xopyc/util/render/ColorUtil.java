package ru.xopyc.util.render;

import java.awt.Color;

public final class ColorUtil {

    public static final int[] RAINBOW_RGB_ARRAY = createRainbow();
    public static final int[] ASTOLFO_RGB_ARRAY = createAstolfo();

    private ColorUtil() {
    }

    public static int red(int color) {
        return color >> 16 & 255;
    }

    public static int green(int color) {
        return color >> 8 & 255;
    }

    public static int blue(int color) {
        return color & 255;
    }

    public static int alpha(int color) {
        return color >> 24 & 255;
    }

    private static int[] createRainbow() {
        int[] colors = new int[360];
        for (int i = 0; i < colors.length; i++) colors[i] = Color.HSBtoRGB(i / 360F, 1F, 1F);
        return colors;
    }

    private static int[] createAstolfo() {
        int[] colors = new int[360];
        for (int i = 0; i < colors.length; i++) {
            float hue = i / 360F;
            if (hue > 0.5F) hue = 1F - hue;
            colors[i] = Color.HSBtoRGB(hue + 0.5F, 0.55F, 1F);
        }
        return colors;
    }
}
