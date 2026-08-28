package ru.xopyc.event.impl.render;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.client.gui.DrawContext;
import ru.xopyc.event.api.Event;

@Getter
@AllArgsConstructor
public class Render2DEvent implements Event {
    private final DrawContext drawContext;
    private final float tickDelta;
}
