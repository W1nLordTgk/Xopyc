package ru.xopyc.module.impl.auto;

import lombok.Getter;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import ru.xopyc.event.impl.client.UpdateEvent;
import ru.xopyc.event.api.listener.EventHandler;
import ru.xopyc.module.api.Module;
import ru.xopyc.module.api.category.Category;

public final class AutoTool extends Module {
    @Getter
    private final static AutoTool instance = new AutoTool();
    private int oldSlot = -1;
    private boolean switched;

    private AutoTool() {
        super("Auto Tool", Category.AUTO);
    }

    @Override
    public void onDisable() {
        reset();
    }

    @EventHandler
    public void onUpdate(UpdateEvent event) {
        if (mc.player == null || mc.world == null) return;

        if (mc.crosshairTarget instanceof BlockHitResult blockHit && mc.options.attackKey.isPressed()) {
            int bestSlot = findBestSlot(blockHit);

            if (bestSlot == -1) return;

            switched = true;

            if (oldSlot == -1) {
                oldSlot = mc.player.getInventory().selectedSlot;
            }

            mc.player.getInventory().selectedSlot = bestSlot;

        } else if (switched) {
            mc.player.getInventory().selectedSlot = oldSlot;
            reset();
        }
    }

    private int findBestSlot(BlockHitResult hit) {
        BlockState state = mc.world.getBlockState(hit.getBlockPos());

        int bestSlot = -1;
        float bestSpeed = 1.0F;

        for (int slot = 0; slot < 9; slot++) {
            ItemStack stack = mc.player.getInventory().getStack(slot);

            float speed = stack.getMiningSpeedMultiplier(state);

            if (speed > bestSpeed) {
                bestSpeed = speed;
                bestSlot = slot;
            }
        }

        return bestSlot;
    }

    private void reset() {
        oldSlot = -1;
        switched = false;
    }
}
