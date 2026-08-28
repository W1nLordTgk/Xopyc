
package ru.xopyc.util.input;

import lombok.Data;
import net.minecraft.client.input.Input;
import net.minecraft.util.PlayerInput;

@Data
public final class DirectionalInput {
    private boolean w, s, a, d;

    public static final DirectionalInput NONE = new DirectionalInput(false, false, false, false);
    public static final DirectionalInput FORWARDS = new DirectionalInput(true, false, false, false);
    public static final DirectionalInput BACKWARDS = new DirectionalInput(false, true, false, false);
    public static final DirectionalInput LEFT = new DirectionalInput(false, false, true, false);
    public static final DirectionalInput RIGHT = new DirectionalInput(false, false, false, true);

    public DirectionalInput(boolean w, boolean s, boolean a, boolean d) {
        this.w = w;
        this.s = s;
        this.a = a;
        this.d = d;
    }

    public DirectionalInput(Input input) {
        this(((IPlayerInput) input).getUntransformed());
    }

    public DirectionalInput(PlayerInput input) {
        this(input.forward(), input.backward(), input.left(), input.right());
    }

    public DirectionalInput(float movementForward, float movementSideways) {
        this(movementForward > 0.0f, movementForward < 0.0f, movementSideways > 0.0f, movementSideways < 0.0f);
    }

    public boolean isMoving() {
        return w || s || a || d;
    }
}
