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

    private NightVision() {
        super("Night Vision", Category.VISUALS);
    }

    @EventHandler
    private void onUpdate(UpdateEvent e) {
        if (mc.player == null || mc.world == null || !mc.player.hasStatusEffect(StatusEffects.NIGHT_VISION)) return;

        mc.player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, Integer.MAX_VALUE, 0));
    }
}
