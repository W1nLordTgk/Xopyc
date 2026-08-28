package ru.xopyc.module.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.minecraft.text.Text;
import ru.xopyc.event.api.EventManager;
import ru.xopyc.event.api.listener.Listener;
import ru.xopyc.module.api.category.Category;
import ru.xopyc.module.api.setting.Setting;
import ru.xopyc.IMinecraft;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public abstract class Module implements Listener, IMinecraft {
    private final String name;
    private final Category category;
    @Setter
    private int bind = 0;
    private boolean enabled = false;
    private final List<Setting> settings = new ArrayList<>();

    public void setEnabled(boolean enabled) {
        if (mc.player == null || mc.world == null) return; // Пока нету доступа к gui из menu

        if (enabled) {
            mc.player.sendMessage(Text.literal(name + " включен!"), true); // Пока нет notifications
            EventManager.getInstance().register(this);
            onEnable();
        } else {
            onDisable();
            EventManager.getInstance().unregister(this);
            mc.player.sendMessage(Text.literal(name + " выключен!"), true); // Пока нет notifications
        }

        this.enabled = enabled;
    }

    public void toggle() {
        setEnabled(!enabled);
    }

    protected void onDisable() {
    }

    protected void onEnable() {
    }
}
