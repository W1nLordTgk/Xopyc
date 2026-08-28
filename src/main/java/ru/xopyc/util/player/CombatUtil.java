package ru.xopyc.util.player;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import ru.xopyc.IMinecraft;

import java.util.concurrent.ThreadLocalRandom;

public final class CombatUtil implements IMinecraft {
    private CombatUtil() {}

    public static void attack(LivingEntity target, boolean resetSprinting, boolean onlyCrits) {
        if (isValidTarget(target) && onCrosshair(target) && bestMomentToHit(resetSprinting, onlyCrits)) {
            boolean isSprinting = mc.player.isSprinting(); // Сброс спринта

            if (isSprinting && resetSprinting) {
                mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.STOP_SPRINTING));
            }

            mc.interactionManager.attackEntity(mc.player, target);
            mc.player.swingHand(Hand.MAIN_HAND);

            if (isSprinting && resetSprinting) {
                mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_SPRINTING));
            }
        }
    }

    public static void attack(LivingEntity taget) {
        attack(taget, false, false);
    }

    public static boolean bestMomentToHit(boolean resetSprinting, boolean onlyCrits) {
        if (!isAttackFullyCharged()) return false;
        return (!onlyCrits && mc.player.isOnGround()) || bestMomentToCrit(resetSprinting);
    }

    public static boolean bestMomentToCrit(boolean resetSprinting) {
        boolean canCrit = mc.player.fallDistance > 0.0F && !mc.player.isOnGround() && !mc.player.isClimbing() && !mc.player.isTouchingWater() && !mc.player.hasStatusEffect(StatusEffects.BLINDNESS) && !mc.player.hasVehicle();
        return resetSprinting ? canCrit : canCrit && !mc.player.isSprinting();
    }

    private static boolean isAttackFullyCharged() {
        return mc.player.getAttackCooldownProgress(0.5F) > ThreadLocalRandom.current().nextFloat(0.9F, 1.0F);
    }

    private static boolean onCrosshair(Entity entity) {
        return mc.crosshairTarget instanceof EntityHitResult result && result.getEntity() == entity;
    }

    public static boolean isValidTarget(LivingEntity target) {
        return target != mc.player && target.isAlive() && !target.isDead() && !target.isSpectator() && target.canHit();
    }
}