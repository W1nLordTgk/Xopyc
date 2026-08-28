package ru.xopyc.module.impl.movement;

import lombok.Getter;
import net.minecraft.util.hit.EntityHitResult;
import ru.xopyc.event.impl.client.UpdateEvent;
import ru.xopyc.event.api.listener.EventHandler;
import ru.xopyc.module.api.Module;
import ru.xopyc.module.api.category.Category;

import java.util.concurrent.ThreadLocalRandom;

public final class ShiftTap extends Module {
    @Getter
    private final static ShiftTap instance = new ShiftTap();

    private boolean wasSneaking;
    private boolean wasAttacking;
    private boolean wasInAir;
    private boolean canCritAgain = true;

    private int sneakTicks;
    private int sneakDuration;
    private int attackCounter;

    private ShiftTap() {
        super("Shift Tap", Category.MOVEMENT);
    }

    @Override
    protected void onEnable() {
        reset();
    }

    @Override
    protected void onDisable() {
        reset();
        if (mc.options != null) {
            mc.options.sneakKey.setPressed(false);
        }
    }

    private void reset() {
        wasSneaking = false;
        wasAttacking = false;
        wasInAir = false;
        canCritAgain = true;
        sneakTicks = 0;
        sneakDuration = 0;
        attackCounter = 0;
    }

    @EventHandler
    public void onUpdate(UpdateEvent event) {
        updateAttackState();

        if (shouldSneak() && !wasSneaking) {
            activateSneak();
        }

        if (wasSneaking) {
            if (++sneakTicks >= sneakDuration) {
                mc.options.sneakKey.setPressed(false);
                wasSneaking = false;
                sneakTicks = 0;
            }
        }
    }

    private void updateAttackState() {
        boolean attacking = mc.options.attackKey.isPressed();
        boolean inAir = !mc.player.isOnGround();

        if (attacking && !wasAttacking) {
            attackCounter++;
        }

        if (!inAir && wasInAir) {
            attackCounter = 0;
            canCritAgain = true;
        }

        if (!attacking && wasAttacking) {
            canCritAgain = true;
        }

        wasAttacking = attacking;
        wasInAir = inAir;
    }

    private boolean shouldSneak() {
        if (!(mc.crosshairTarget instanceof EntityHitResult))
            return false;

        if (!mc.options.attackKey.isPressed())
            return false;

        if (mc.player.isOnGround())
            return false;

        if (mc.player.getVelocity().y >= -0.2)
            return false;

        if (attackCounter != 1)
            return false;

        if (!canCritAgain)
            return false;

        return !mc.player.isClimbing()
                && !mc.player.isTouchingWater()
                && !mc.player.hasVehicle();
    }

    private void activateSneak() {
        wasSneaking = true;
        sneakTicks = 0;
        sneakDuration = ThreadLocalRandom.current().nextBoolean() ? 2 : 3;
        canCritAgain = false;

        mc.options.sneakKey.setPressed(true);
    }
}