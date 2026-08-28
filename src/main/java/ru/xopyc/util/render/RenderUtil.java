package ru.xopyc.util.render;

import com.mojang.blaze3d.systems.RenderSystem;
import ru.xopyc.IMinecraft;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;

public final class RenderUtil implements IMinecraft {

    private RenderUtil() {
    }

    public static void drawBox(MatrixStack matrices, Box box, int color) {
        float a = ColorUtil.alpha(color) / 255.0F;
        float r = ColorUtil.red(color) / 255.0F;
        float g = ColorUtil.green(color) / 255.0F;
        float b = ColorUtil.blue(color) / 255.0F;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.disableDepthTest();

        VertexConsumerProvider.Immediate consumers = mc.getBufferBuilders().getEntityVertexConsumers();
        VertexConsumer consumer = consumers.getBuffer(RenderLayer.getLines());
        VertexRendering.drawBox(matrices, consumer, box, r, g, b, a);

        consumers.draw();

        RenderSystem.enableDepthTest();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }
}