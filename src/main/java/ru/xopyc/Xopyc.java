package ru.xopyc;

import lombok.Getter;
import net.fabricmc.api.ClientModInitializer;
import ru.xopyc.module.api.ModuleManager;
import ru.xopyc.screen.clickgui.bind.KeyBindingStorage;

public final class Xopyc implements ClientModInitializer {
    @Getter
    private static Xopyc instance;

    @Override
    public void onInitializeClient() {
        instance = this;

        ModuleManager.getInstance().load();
        KeyBindingStorage.getInstance().load();
    }

    public String getModID() {
        return "xopyc";
    }
}
