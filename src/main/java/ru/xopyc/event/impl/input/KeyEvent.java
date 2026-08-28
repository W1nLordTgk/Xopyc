package ru.xopyc.event.impl.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.xopyc.event.api.Event;

@Getter
@AllArgsConstructor
public class KeyEvent implements Event {
    private final int key;
}
