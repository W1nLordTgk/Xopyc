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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import ru.xopyc.event.impl.render.Render3DEvent;
import ru.xopyc.event.api.listener.EventHandler;
import ru.xopyc.module.api.Module;
import ru.xopyc.module.api.category.Category;
import ru.xopyc.util.render.ColorUtil;

import java.util.Arrays;

public final class Trails extends Module {

    private static final Trails instance = new Trails();

    private static final int MAX_POINTS = 256;

    private static final float POINT_DISTANCE = 0.00025F;

    private static final long RAINBOW_SPEED = 15L;

    private static final int RAINBOW_STEP = 5;

    private static final float START_FADE_LENGTH = 10.0F;

    private static final float START_HEIGHT_LENGTH = 5.0F;

    private static final float BOTTOM_OFFSET = 0.2F;

    private static final float TOP_OFFSET = 0.3F;

    private static final int TOP_ALPHA_DIVIDER = 5;

    private float lastX;
    private float lastY;
    private float lastZ;

    private final Point[] points =
            new Point[MAX_POINTS];

    private int head;
    private int size;

    private Trails() {
        super("Trails", Category.VISUALS);
    }

    @Override
    public void onEnable() {
        clearTrail();

        if (mc.player == null) {
            return;
        }

        lastX = (float) mc.player.getX();
        lastY = (float) mc.player.getY();
        lastZ = (float) mc.player.getZ();

        addPoint(
                lastX,
                lastY,
                lastZ
        );
    }

    @Override
    public void onDisable() {
        clearTrail();
    }

    @EventHandler
    public void onRender3D(Render3DEvent event) {
        if (mc.options.getPerspective().isFirstPerson()) {
            return;
        }

        float tickDelta = event.getTickDelta();

        float x = (float) MathHelper.lerp(
                tickDelta,
                mc.player.prevX,
                mc.player.getX()
        );

        float y = (float) MathHelper.lerp(
                tickDelta,
                mc.player.prevY,
                mc.player.getY()
        );

        float z = (float) MathHelper.lerp(
                tickDelta,
                mc.player.prevZ,
                mc.player.getZ()
        );

        float dx = x - lastX;
        float dy = y - lastY;
        float dz = z - lastZ;

        if (dx * dx + dy * dy + dz * dz >= POINT_DISTANCE) {
            addPoint(x, y, z);

            lastX = x;
            lastY = y;
            lastZ = z;
        }

        for (int i = 0; i < size; i++) {
            Point point = getPoint(i);

            if (point != null) {
                point.age++;
            }
        }

        removeExpired();

        MatrixStack matrices = event.getMatrices();

        matrices.push();

        renderTrail(matrices);

        matrices.pop();
    }

    private void renderTrail(
            MatrixStack matrices
    ) {
        if (size < 2) {
            return;
        }

        Camera camera =
                mc.gameRenderer.getCamera();

        Vec3d cam =
                camera.getPos();

        Matrix4f matrix =
                matrices.peek().getPositionMatrix();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.depthMask(false);

        RenderSystem.setShader(
                ShaderProgramKeys.POSITION_COLOR
        );

        BufferBuilder buffer =
                Tessellator.getInstance().begin(
                        VertexFormat.DrawMode.TRIANGLE_STRIP,
                        VertexFormats.POSITION_COLOR
                );

        long time =
                System.currentTimeMillis()
                        / RAINBOW_SPEED;

        for (int i = 0; i < size; i++) {

            Point point =
                    getPoint(i);

            if (point == null) {
                continue;
            }

            float x =
                    (float) (point.x - cam.x);

            float y =
                    (float) (point.y - cam.y);

            float z =
                    (float) (point.z - cam.z);

            int rgb =
                    ColorUtil.RAINBOW_RGB_ARRAY[
                            Math.floorMod(
                                    (int) time +
                                            i * RAINBOW_STEP,
                                    360
                            )
                            ];

            int r =
                    (rgb >> 16) & 255;

            int g =
                    (rgb >> 8) & 255;

            int b =
                    rgb & 255;

            float age =
                    point.age /
                            (float) Point.MAX_AGE;

            float ageFade =
                    1.0F - age * age;

            float startFade =
                    Math.min(
                            i / START_FADE_LENGTH,
                            1.0F
                    );

            int alpha =
                    (int) (
                            255.0F *
                                    ageFade *
                                    startFade
                    );

            int topAlpha =
                    alpha / TOP_ALPHA_DIVIDER;

            float startScale =
                    Math.min(
                            i / START_HEIGHT_LENGTH,
                            1.0F
                    );

            float top =
                    BOTTOM_OFFSET +
                            (mc.player.getHeight() -
                                    TOP_OFFSET)
                                    * startScale;

            buffer.vertex(
                            matrix,
                            x,
                            y + top,
                            z
                    )
                    .color(
                            r,
                            g,
                            b,
                            topAlpha
                    );

            buffer.vertex(
                            matrix,
                            x,
                            y + BOTTOM_OFFSET,
                            z
                    )
                    .color(
                            r,
                            g,
                            b,
                            alpha
                    );
        }

        BufferRenderer.drawWithGlobalProgram(
                buffer.end()
        );

        RenderSystem.depthMask(true);
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    private void removeExpired() {
        while (size > 0) {

            Point point =
                    getPoint(0);

            if (point == null ||
                    point.age < Point.MAX_AGE) {
                break;
            }

            size--;
        }
    }

    private Point getPoint(int index) {
        return points[
                (head - size + index + MAX_POINTS)
                        % MAX_POINTS
                ];
    }

    private void clearTrail() {
        Arrays.fill(points, null);

        head = 0;
        size = 0;

        lastX = 0.0F;
        lastY = 0.0F;
        lastZ = 0.0F;
    }

    private void addPoint(
            float x,
            float y,
            float z
    ) {
        Point point =
                points[head];

        if (point == null) {
            points[head] =
                    point = new Point(
                            x,
                            y,
                            z
                    );
        }

        point.x = x;
        point.y = y;
        point.z = z;
        point.age = 0;

        head =
                (head + 1) & (MAX_POINTS - 1);

        if (size < MAX_POINTS) {
            size++;
        }
    }

    private static final class Point {

        private static final int MAX_AGE = 100;

        private float x;
        private float y;
        private float z;

        private int age;

        private Point(
                float x,
                float y,
                float z
        ) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    public static Trails getInstance() {
        return instance;
    }
}