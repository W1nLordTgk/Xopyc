package ru.xopyc;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.util.Window;

public interface IMinecraft {
    MinecraftClient mc = MinecraftClient.getInstance();
    Window window = mc.getWindow();
    Tessellator tessellator = Tessellator.getInstance();
    TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
    static boolean nullCheck() {
        return mc.player == null || mc.world == null;
    }
}
