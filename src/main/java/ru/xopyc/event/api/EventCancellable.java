package ru.xopyc.event.api;

import lombok.Getter;
import lombok.Setter;

public abstract class EventCancellable implements Event, Cancellable {
    @Getter @Setter private boolean cancelled;
}