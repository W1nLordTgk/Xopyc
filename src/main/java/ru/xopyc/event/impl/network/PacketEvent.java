package ru.xopyc.event.impl.network;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.network.packet.Packet;
import ru.xopyc.event.api.EventCancellable;

@Getter
@AllArgsConstructor
public class PacketEvent extends EventCancellable {
    private final Packet<?> packet;
    private final Type type;

    public boolean isSend() {
        return type == Type.SEND;
    }

    public boolean isReceive() {
        return type == Type.RECEIVE;
    }

    public enum Type {
        SEND, RECEIVE
    }
}
