package ru.xopyc.module.impl.visuals;

import lombok.Getter;
import net.minecraft.entity.LivingEntity;
import ru.xopyc.module.api.Module;
import ru.xopyc.module.api.category.Category;

public final class HealthBars extends Module {
    @Getter
    private static final HealthBars instance = new HealthBars();
    private LivingEntity currentEntity;

    private HealthBars() {
        super("Health Bars", Category.VISUALS);
    }

    public void setCurrentEntity(LivingEntity currentEntity) {
        this.currentEntity = currentEntity;
    }

    public LivingEntity getCurrentEntity() {
        return currentEntity;
    }
}
