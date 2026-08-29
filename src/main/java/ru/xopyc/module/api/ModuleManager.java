package ru.xopyc.module.api;

import lombok.Getter;
import ru.xopyc.event.api.listener.Listener;
import ru.xopyc.module.impl.auto.AutoTool;
import ru.xopyc.module.impl.movement.ShiftTap;
import ru.xopyc.module.impl.visuals.*;

import java.util.*;

public final class ModuleManager implements Listener {
    @Getter private static final ModuleManager instance = new ModuleManager();
    @Getter private final Set<Module> modules = new HashSet<>();

    private ModuleManager() {
    }

    public void load() {
        modules.addAll(Arrays.asList(
                AutoTool.getInstance(),
                ShiftTap.getInstance(),
                HealthBars.getInstance(),
                Trails.getInstance(),
                ChinaHat.getInstance(),
                JumpCircles.getInstance(),
                SwordAnimation.getInstance(),
                ViewModel.getInstance(),
                NightVision.getInstance()
        ));
    }
}
