package ru.xopyc.event.api;

public interface Cancellable {
    boolean isCancelled();
    void setCancelled(boolean cancelled);
}
