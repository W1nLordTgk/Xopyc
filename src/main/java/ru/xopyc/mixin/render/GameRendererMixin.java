package ru.xopyc.mixin.render;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.xopyc.event.api.EventManager;
import ru.xopyc.event.impl.render.Render3DEvent;
import ru.xopyc.IMinecraft;

@Mixin(GameRenderer.class)
public class GameRendererMixin implements IMinecraft {
    @Inject(at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/GameRenderer;renderHand:Z", opcode = Opcodes.GETFIELD, ordinal = 0), method = "renderWorld")
    private void render3dHook(RenderTickCounter renderTickCounter, CallbackInfo ci) {
        if (IMinecraft.nullCheck()) return;

        Camera camera = mc.gameRenderer.getCamera();
        MatrixStack matrixStack = new MatrixStack();
        // RenderSystem.getModelViewStack().pushMatrix().mul(matrixStack.peek().getPositionMatrix());
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0f));

        Render3DEvent render3DEvent = new Render3DEvent(matrixStack, renderTickCounter.getTickDelta(false));
        EventManager.getInstance().call(render3DEvent);

        // RenderSystem.getModelViewStack().popMatrix();
    }
}
