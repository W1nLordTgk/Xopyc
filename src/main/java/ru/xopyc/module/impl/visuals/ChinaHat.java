package ru.xopyc.module.impl.visuals;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix4f;
import ru.xopyc.event.impl.render.Render3DEvent;
import ru.xopyc.event.api.listener.EventHandler;
import ru.xopyc.module.api.Module;
import ru.xopyc.module.api.category.Category;
import ru.xopyc.util.render.ColorUtil;

public final class ChinaHat extends Module {
    private static final ChinaHat instance = new ChinaHat();
    private static final float RADIUS = 0.65F;
    private static final float HEIGHT = 0.28F;

    private final Point[] FAN_POINTS = createPoints(5);
    private final Point[] OUTLINE_POINTS = createPoints(4);

    public ChinaHat() {
        super("China Hat", Category.VISUALS);
    }

    @EventHandler
    public void onRender3D(Render3DEvent event) {
        if (mc.options.getPerspective().isFirstPerson()) {
            return;
        }

        renderHat(
                event.getMatrices(),
                mc.player,
                event.getTickDelta()
        );
    }

    private void renderHat(
            MatrixStack matrices,
            PlayerEntity player,
            float tickDelta
    ) {
        Camera camera =
                mc.gameRenderer.getCamera();

        double x =
                MathHelper.lerp(
                        tickDelta,
                        player.prevX,
                        player.getX()
                ) - camera.getPos().x;

        double y =
                MathHelper.lerp(
                        tickDelta,
                        player.prevY,
                        player.getY()
                ) - camera.getPos().y
                        + player.getHeight();

        double z =
                MathHelper.lerp(
                        tickDelta,
                        player.prevZ,
                        player.getZ()
                ) - camera.getPos().z;

        matrices.push();

        matrices.translate(
                x,
                y,
                z
        );

        Matrix4f matrix =
                matrices.peek().getPositionMatrix();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.depthMask(false);

        RenderSystem.setShader(
                ShaderProgramKeys.POSITION_COLOR
        );

        /*
         * Центральный конус + основание.
         */
        BufferBuilder buffer =
                Tessellator.getInstance().begin(
                        VertexFormat.DrawMode.TRIANGLE_FAN,
                        VertexFormats.POSITION_COLOR
                );

        buffer.vertex(
                        matrix,
                        0.0F,
                        HEIGHT,
                        0.0F
                )
                .color(
                        255,
                        255,
                        255,
                        180
                );

        long time =
                System.currentTimeMillis() / 12L;

        for (Point p : FAN_POINTS) {

            int rgb =
                    ColorUtil.RAINBOW_RGB_ARRAY[
                            Math.floorMod(
                                    p.hue() + (int) time,
                                    360
                            )
                            ];

            buffer.vertex(
                            matrix,
                            p.x() * RADIUS,
                            0.0F,
                            p.z() * RADIUS
                    )
                    .color(
                            (rgb >> 16) & 255,
                            (rgb >> 8) & 255,
                            rgb & 255,
                            120
                    );
        }

        BufferRenderer.drawWithGlobalProgram(
                buffer.end()
        );

        /*
         * Обводка.
         */
        buffer =
                Tessellator.getInstance().begin(
                        VertexFormat.DrawMode.TRIANGLE_STRIP,
                        VertexFormats.POSITION_COLOR
                );

        float outer =
                RADIUS + 0.01F;

        float inner =
                RADIUS - 0.01F;

        for (Point p : OUTLINE_POINTS) {

            int rgb =
                    ColorUtil.RAINBOW_RGB_ARRAY[
                            Math.floorMod(
                                    p.hue() + (int) time,
                                    360
                            )
                            ];

            int r =
                    (rgb >> 16) & 255;

            int g =
                    (rgb >> 8) & 255;

            int b =
                    rgb & 255;

            buffer.vertex(
                            matrix,
                            p.x() * outer,
                            0.0F,
                            p.z() * outer
                    )
                    .color(
                            r,
                            g,
                            b,
                            255
                    );

            buffer.vertex(
                            matrix,
                            p.x() * inner,
                            0.0F,
                            p.z() * inner
                    )
                    .color(
                            r,
                            g,
                            b,
                            255
                    );
        }

        BufferRenderer.drawWithGlobalProgram(
                buffer.end()
        );

        RenderSystem.depthMask(true);
        RenderSystem.enableCull();
        RenderSystem.disableBlend();

        matrices.pop();
    }

    /**
     * Создает массив точек окружности.
     *
     * @param degreeStep каждые сколько градусов создавать новую точку
     */
    private static Point[] createPoints(int degreeStep) {
        Point[] points =
                new Point[360 / degreeStep + 1];

        int index = 0;

        for (
                int degree = 0;
                degree <= 360;
                degree += degreeStep
        ) {
            float radians =
                    (float) Math.toRadians(degree);

            points[index++] =
                    new Point(
                            MathHelper.sin(radians),
                            -MathHelper.cos(radians),
                            degree
                    );
        }

        return points;
    }

    /**
     * Точка окружности.
     */
    private record Point(
            float x,
            float z,
            int hue
    ) {
    }

    public static ChinaHat getInstance() {
        return instance;
    }
}