package ru.xopyc.mixin.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.texture.GuiAtlasManager;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.xopyc.IMinecraft;
import ru.xopyc.module.impl.visuals.HealthBars;
import ru.xopyc.util.server.ServerUtil;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> extends EntityRenderer<T, S> implements FeatureRendererContext<S, M>, IMinecraft {
    @Unique
    private static final Identifier ARMOR_EMPTY_TEXTURE = Identifier.ofVanilla("hud/armor_empty");
    @Unique
    private static final Identifier ARMOR_HALF_TEXTURE = Identifier.ofVanilla("hud/armor_half");
    @Unique
    private static final Identifier ARMOR_FULL_TEXTURE = Identifier.ofVanilla("hud/armor_full");

    protected LivingEntityRendererMixin(EntityRendererFactory.Context context) {
        super(context);
    }

    @Inject(method = "updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V", at = @At("TAIL"))
    private void saveEntity(T livingEntity, S state, float tickDelta, CallbackInfo ci) {
        HealthBars.getInstance().setCurrentEntity(livingEntity);
    }

    @Inject(method = "render*", at = @At("RETURN"), cancellable = true)
    private void renderHealth(LivingEntityRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (HealthBars.getInstance().isEnabled()) {
            LivingEntity currentEntity = HealthBars.getInstance().getCurrentEntity();
            if (currentEntity == mc.player || currentEntity instanceof ArmorStandEntity || (currentEntity instanceof PlayerEntity player && ServerUtil.isNPC(player))) return;
            matrices.push();
            matrices.translate(0.0D, currentEntity.getHeight() + 0.5F, 0.0F);
            boolean hasName = currentEntity.hasCustomName() || currentEntity instanceof PlayerEntity;
            double distance = mc.getEntityRenderDispatcher().getSquaredDistanceToCamera(currentEntity);
            if (hasName && distance <= 4096.0D) {
                matrices.translate(0.0D, 9.0F * 1.15F * 0.025F, 0.0F);
            }
            matrices.multiply(mc.getEntityRenderDispatcher().getRotation());
            matrices.scale(-1.0F, 1.0F, 1.0F);
            matrices.scale(0.025F, 0.025F, 0.025F);
            InGameHud.HeartType heartType = fromEntityState(currentEntity);
            boolean hardcore = currentEntity.getWorld().getLevelProperties().isHardcore();
            int health = MathHelper.ceil(ServerUtil.resolveHealth(currentEntity));
            int absorption = MathHelper.ceil(currentEntity.getAbsorptionAmount());
            float maxHealth = Math.max((float) currentEntity.getAttributeValue(EntityAttributes.MAX_HEALTH), health);
            int heartCount = MathHelper.ceil(maxHealth / 2.0F);
            int absorptionHeartCount = MathHelper.ceil(absorption / 2.0F);
            int totalHeartCount = heartCount + absorptionHeartCount;
            if (totalHeartCount <= 0) {
                matrices.pop();
                return;
            }
            int rows = MathHelper.ceil(totalHeartCount / 10.0F);
            int lines = Math.max(10 - (rows - 2), 3);
            GuiAtlasManager guiAtlasManager = mc.getGuiAtlasManager();
            RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
            Sprite containerSprite = guiAtlasManager.getSprite(InGameHud.HeartType.CONTAINER.getTexture(hardcore, false, false));
            RenderSystem.setShaderTexture(0, containerSprite.getAtlasId());
            RenderSystem.enableDepthTest();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
            Matrix4f model = matrices.peek().getPositionMatrix();
            int heartsInFirstRow = Math.min(totalHeartCount, 10);
            float maxX = (heartsInFirstRow * 8.0F + 1.0F) / 2.0F;
            drawArmor(model, buffer, currentEntity.getArmor(), rows, lines, maxX, guiAtlasManager);
            for (int l = totalHeartCount - 1; l >= 0; --l) {
                int row = l / 10;
                int column = l % 10;
                float x = maxX - column * 8.0F;
                float y = row * lines;
                float z = row * 0.01F;
                drawHeart(model, buffer, x, y, z, InGameHud.HeartType.CONTAINER, hardcore, false, false, guiAtlasManager);
                int q = l * 2;
                int k = heartCount * 2;
                if (l >= heartCount) {
                    int r = q - k;
                    if (r < absorption) {
                        boolean half = r + 1 == absorption;
                        InGameHud.HeartType absorptionType = heartType == InGameHud.HeartType.WITHERED ? heartType : InGameHud.HeartType.ABSORBING;
                        drawHeart(model, buffer, x, y, z, absorptionType, hardcore, half, false, guiAtlasManager);
                    }
                    continue;
                }
                if (q < health) {
                    boolean half = q + 1 == health;
                    drawHeart(model, buffer, x, y, z, heartType, hardcore, half, false, guiAtlasManager);
                }
            }
            BufferRenderer.drawWithGlobalProgram(buffer.end());
            RenderSystem.disableBlend();
            matrices.pop();
        }
    }

    @Unique
    private static void drawArmor(Matrix4f model, VertexConsumer vertices, int armor, int rows, int lines, float maxX, GuiAtlasManager guiAtlasManager) {
        if (armor <= 0) return;
        float y = (rows - 1) * lines + 10.0F;
        for (int i = 0; i < 10; i++) {
            float x = maxX - i * 8.0F;
            float z = -0.01F;
            Identifier texture;
            if (i * 2 + 1 < armor) {
                texture = ARMOR_FULL_TEXTURE;
            } else if (i * 2 + 1 == armor) {
                texture = ARMOR_HALF_TEXTURE;
            } else {
                texture = ARMOR_EMPTY_TEXTURE;
            }
            Sprite sprite = guiAtlasManager.getSprite(texture);
            float minU = sprite.getMinU();
            float maxU = sprite.getMaxU();
            float minV = sprite.getMinV();
            float maxV = sprite.getMaxV();
            float size = 9.0F;
            drawVertex(model, vertices, x, y - size, z, minU, maxV);
            drawVertex(model, vertices, x - size, y - size, z, maxU, maxV);
            drawVertex(model, vertices, x - size, y, z, maxU, minV);
            drawVertex(model, vertices, x, y, z, minU, minV);
        }
    }


    @Unique
    private InGameHud.HeartType fromEntityState(LivingEntity livingEntity) {
        InGameHud.HeartType heartType;
        if (livingEntity.hasStatusEffect(StatusEffects.POISON)) {
            heartType = InGameHud.HeartType.POISONED;
        } else if (livingEntity.hasStatusEffect(StatusEffects.WITHER)) {
            heartType = InGameHud.HeartType.WITHERED;
        } else if (livingEntity.isFrozen()) {
            heartType = InGameHud.HeartType.FROZEN;
        } else {
            heartType = InGameHud.HeartType.NORMAL;
        }
        return heartType;
    }

    @Unique
    private static void drawHeart(Matrix4f model, VertexConsumer vertices, float x, float y, float z, InGameHud.HeartType type, boolean hardcore, boolean half, boolean blinking, GuiAtlasManager guiAtlasManager) {
        Identifier texture = type.getTexture(hardcore, half, blinking);
        Sprite sprite = guiAtlasManager.getSprite(texture);
        float minU = sprite.getMinU();
        float maxU = sprite.getMaxU();
        float minV = sprite.getMinV();
        float maxV = sprite.getMaxV();
        float size = 9.0F;
        drawVertex(model, vertices, x, y - size, z, minU, maxV);
        drawVertex(model, vertices, x - size, y - size, z, maxU, maxV);
        drawVertex(model, vertices, x - size, y, z, maxU, minV);
        drawVertex(model, vertices, x, y, z, minU, minV);
    }

    @Unique
    private static void drawVertex(Matrix4f model, VertexConsumer vertices, float x, float y, float z, float u, float v) {
        vertices.vertex(model, x, y, z).texture(u, v).color(255, 255, 255, 255);
    }
}