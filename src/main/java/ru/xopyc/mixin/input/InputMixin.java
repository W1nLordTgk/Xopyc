package ru.xopyc.mixin.input;

import ru.xopyc.IMinecraft;
import ru.xopyc.util.input.IPlayerInput;
import net.minecraft.client.input.Input;
import net.minecraft.util.PlayerInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Input.class)
public abstract class InputMixin implements IPlayerInput, IMinecraft {
    @Unique
    protected PlayerInput untransformed = PlayerInput.DEFAULT;
    @Override
    public PlayerInput getUntransformed() {
        return untransformed;
    }
}
