package ru.xopyc.event.impl.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.entity.LivingEntity;
import ru.xopyc.event.api.EventCancellable;

@Getter
@AllArgsConstructor
public class JumpEvent extends EventCancellable {
    private final LivingEntity entity;
}
