package ru.xopyc.mixin.render;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.xopyc.event.api.EventManager;
import ru.xopyc.event.impl.render.Render2DEvent;
import ru.xopyc.IMinecraft;

@Mixin(InGameHud.class)
public class InGameHudMixin implements IMinecraft {

    @Inject(at = @At(value = "TAIL"), method = "render")
    public void render(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (IMinecraft.nullCheck() || mc.options.hudHidden || mc.getDebugHud().shouldShowDebugHud()) return;

        Render2DEvent render2DEvent = new Render2DEvent(context, tickCounter.getTickDelta(false));
        EventManager.getInstance().call(render2DEvent);
    }

    /*
    @Inject(method = "renderStatusBars", at = @At("TAIL"))
    private void renderTotems(DrawContext context, CallbackInfo ci) {
        int totems = 0;

        for (int i = 0; i < mc.player.getInventory().size(); ++i) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.isOf(Items.TOTEM_OF_UNDYING)) {
                totems += stack.getCount();
            }
        }

        int x = context.getScaledWindowWidth() / 2 - 8, y = context.getScaledWindowHeight() - 52;
        ItemStack totemStack = Items.TOTEM_OF_UNDYING.getDefaultStack();

        context.drawItem(totemStack, x, y);
        context.getMatrices().push();
        context.getMatrices().scale(1.0F, 1.0F, 0.8F); // 80%
        context.getMatrices().translate(0.0F, 0.0F, 200.0F);
        String count = String.valueOf(totems);
        context.drawTextWithShadow(mc.textRenderer, count, x + 19 - 2 - mc.textRenderer.getWidth(count), y + 6 + 3, 0xFFFFFF);
        context.getMatrices().pop();
    }



    @Inject(method = "renderFood", at = @At("TAIL"))
    private void renderHiddenSaturation(DrawContext context, PlayerEntity player, int top, int right, CallbackInfo ci) {
        HungerManager hungerManager = player.getHungerManager();
        float saturation = hungerManager.getSaturationLevel();

        for (int j = 0; j < 10; ++j) {
            int l = right - j * 8 - 9;

            Identifier FOOD_EMPTY_TEXTURE, FOOD_HALF_TEXTURE, FOOD_FULL_TEXTURE;

            if (player.hasStatusEffect(StatusEffects.HUNGER)) {
                FOOD_EMPTY_TEXTURE = Identifier.ofVanilla("hud/food_empty_hunger");
                FOOD_HALF_TEXTURE = Identifier.ofVanilla("hud/food_half_hunger");
                FOOD_FULL_TEXTURE = Identifier.ofVanilla("hud/food_full_hunger");
            } else {
                FOOD_EMPTY_TEXTURE = Identifier.ofVanilla("hud/food_empty");
                FOOD_HALF_TEXTURE = Identifier.ofVanilla("hud/food_half");
                FOOD_FULL_TEXTURE = Identifier.ofVanilla("hud/food_full");
            }

            context.drawGuiTexture(RenderLayer::getGuiTextured, FOOD_EMPTY_TEXTURE, l, top - 10, 9, 9);

            if (j * 2 + 1 < saturation) {
                context.drawGuiTexture(RenderLayer::getGuiTextured, FOOD_FULL_TEXTURE, l, top - 10, 9, 9);
            }

            if (j * 2 + 1 == MathHelper.ceil(saturation)) {
                context.drawGuiTexture(RenderLayer::getGuiTextured, FOOD_HALF_TEXTURE, l, top - 10, 9, 9);
            }
        }
    }
     */

}
