package ru.xopyc.util.client;

import lombok.Data;

@Data
public final class Draggable {
    private float x, y, w, h;

    public Draggable(float x, float y, float w, float h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setSize(float w, float h) {
        this.w = w;
        this.h = h;
    }
}
