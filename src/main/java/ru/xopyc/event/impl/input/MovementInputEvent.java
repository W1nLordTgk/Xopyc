package ru.xopyc.event.impl.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.xopyc.event.api.Event;
import ru.xopyc.util.input.DirectionalInput;
import net.minecraft.util.PlayerInput;

@Getter
@AllArgsConstructor
public class MovementInputEvent implements Event {
    private final PlayerInput playerInput;
    @Setter
    private boolean jump, sneak, sprint;
    private final DirectionalInput directionalInput;
}
