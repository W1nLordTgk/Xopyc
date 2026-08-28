package ru.xopyc.util.client;

import lombok.Getter;
import net.minecraft.util.Identifier;
import ru.xopyc.Xopyc;

public enum Image {
    JUMP_CIRCLE("module/circle");

    @Getter
    private final Identifier identifier;

    Image(String name) {
        this.identifier = Identifier.of(Xopyc.getInstance().getModID(), "image/" + name + ".png");
    }
}
