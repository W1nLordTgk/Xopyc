package ru.xopyc.event.impl.render;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.client.util.math.MatrixStack;
import ru.xopyc.event.api.Event;

@Getter
@AllArgsConstructor
public class Render3DEvent implements Event {
    private final MatrixStack matrices;
    private final float tickDelta;
}
