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
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import ru.xopyc.IMinecraft;
import ru.xopyc.event.api.listener.EventHandler;
import ru.xopyc.event.impl.entity.JumpEvent;
import ru.xopyc.event.impl.render.Render3DEvent;
import ru.xopyc.module.api.Module;
import ru.xopyc.module.api.category.Category;
import ru.xopyc.util.client.Image;
import ru.xopyc.util.render.ColorUtil;

import java.util.ArrayList;

public final class JumpCircles extends Module {
    private static final JumpCircles instance = new JumpCircles();
    private static final long LIFE = 1000L;
    private static final float MAX_RADIUS = 4.0F;
    private final ArrayList<Circle> circles = new ArrayList<>(16);

    private JumpCircles() {
        super("Jump Circles", Category.VISUALS);
    }

    @EventHandler
    public void onRender3D(Render3DEvent event) {
        long now = System.currentTimeMillis();
        circles.removeIf(circle -> now - circle.time >= LIFE);
        if (circles.isEmpty()) return;
        MatrixStack matrices = event.getMatrices();
        Camera camera = mc.gameRenderer.getCamera();
        Vec3d cam = camera.getPos();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
        RenderSystem.setShaderTexture(0, Image.JUMP_CIRCLE.getIdentifier());
        BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        int shift = (int) (now / 12L);
        for (Circle circle : circles) {
            float progress = (now - circle.time) / (float) LIFE;
            float radius = progress * MAX_RADIUS;
            float half = radius * 0.5F;
            float x = (float) (circle.pos.x - cam.x);
            float y = (float) (circle.pos.y - cam.y);
            float z = (float) (circle.pos.z - cam.z);
            int alpha = (int) ((1.0F - progress * progress) * 255.0F);
            colorVertex(buffer, matrix, x - half, y, z - half, 0.0F, 0.0F, ColorUtil.RAINBOW_RGB_ARRAY[Math.floorMod(circle.seed + shift, 360)], alpha);
            colorVertex(buffer, matrix, x + half, y, z - half, 1.0F, 0.0F, ColorUtil.RAINBOW_RGB_ARRAY[Math.floorMod(circle.seed + shift + 90, 360)], alpha);
            colorVertex(buffer, matrix, x + half, y, z + half, 1.0F, 1.0F, ColorUtil.RAINBOW_RGB_ARRAY[Math.floorMod(circle.seed + shift + 180, 360)], alpha);
            colorVertex(buffer, matrix, x - half, y, z + half, 0.0F, 1.0F, ColorUtil.RAINBOW_RGB_ARRAY[Math.floorMod(circle.seed + shift + 270, 360)], alpha);
        }
        BufferRenderer.drawWithGlobalProgram(buffer.end());
        RenderSystem.depthMask(true);
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    @EventHandler
    public void onJump(JumpEvent event) {
        if (IMinecraft.nullCheck() || event.getEntity() != mc.player) return;
        long time = System.currentTimeMillis();
        circles.add(new Circle(mc.player.getPos().add(0.0, 0.05, 0.0), time));
    }

    private static void colorVertex(BufferBuilder buffer, Matrix4f matrix, float x, float y, float z, float u, float v, int color, int alpha) {
        buffer.vertex(matrix, x, y, z).texture(u, v).color(ColorUtil.red(color), ColorUtil.green(color), ColorUtil.blue(color), alpha);
    }

    private static final class Circle {
        private final Vec3d pos;
        private final long time;
        private final int seed;

        private Circle(Vec3d pos, long time) {
            this.pos = pos;
            this.time = time;
            this.seed = (int) (time % 360L);
        }
    }

    public static JumpCircles getInstance() {
        return instance;
    }
}