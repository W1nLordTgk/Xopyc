package ru.xopyc.module.impl.visuals;

import lombok.Getter;
import ru.xopyc.event.api.listener.EventHandler;
import ru.xopyc.event.impl.client.UpdateEvent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import ru.xopyc.module.api.Module;
import ru.xopyc.module.api.category.Category;

public final class NightVision extends Module {
    @Getter
    private static final NightVision instance = new NightVision();
    private StatusEffectInstance previousEffect;

    private NightVision() {
        super("Night Vision", Category.VISUALS);
    }

    @Override
    protected void onEnable() {
        previousEffect = mc.player.getStatusEffect(StatusEffects.NIGHT_VISION);
    }

    @Override
    protected void onDisable() {
        mc.player.removeStatusEffect(StatusEffects.NIGHT_VISION);

        if (previousEffect != null) {
            mc.player.addStatusEffect(previousEffect);
        }

        previousEffect = null;
    }

    @EventHandler
    private void onUpdate(UpdateEvent e) {
        if (mc.player == null || mc.world == null) return;
        mc.player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false, false));
    }
}
