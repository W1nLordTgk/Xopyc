package ru.xopyc.mixin.input;

import net.minecraft.client.Keyboard;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.xopyc.event.api.EventManager;
import ru.xopyc.event.impl.input.KeyEvent;
import ru.xopyc.screen.clickgui.ClickGUI;
import ru.xopyc.module.api.Module;
import ru.xopyc.module.api.ModuleManager;
import ru.xopyc.IMinecraft;

@Mixin(Keyboard.class)
public class KeyBoardMixin implements IMinecraft {
    @Inject(method = "onKey", at = @At("HEAD"))
    private void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        if (key != GLFW.GLFW_KEY_UNKNOWN && mc.currentScreen == null && action == GLFW.GLFW_PRESS) {

            if (key == GLFW.GLFW_KEY_RIGHT_SHIFT) {
                mc.setScreen(ClickGUI.getInstance());
            }

            for (Module module : ModuleManager.getInstance().getModules()) {
                if (key == module.getBind()) {
                    module.toggle();
                }
            }

            if (!IMinecraft.nullCheck()) {
                KeyEvent keyEvent = new KeyEvent(key);
                EventManager.getInstance().call(keyEvent);
            }
        }
    }
}
