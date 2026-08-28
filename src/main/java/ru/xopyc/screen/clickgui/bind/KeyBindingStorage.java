package ru.xopyc.screen.clickgui.bind;

import lombok.Getter;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

public final class KeyBindingStorage {
    @Getter
    private static final KeyBindingStorage instance = new KeyBindingStorage();
    private final Map<Integer, String> keyMap = new HashMap<>();
    private KeyBindingStorage() {
    }

    public String getKey(int key) {
        return keyMap.getOrDefault(key, "Нет");
    }

    public void load() {
        keyMap.put(-100, "LMB");
        keyMap.put(-99, "RMB");
        keyMap.put(-98, "MMB");
        keyMap.put(-97, "MOUSE4");
        keyMap.put(-96, "MOUSE5");

        keyMap.put(GLFW.GLFW_KEY_A, "A");
        keyMap.put(GLFW.GLFW_KEY_B, "B");
        keyMap.put(GLFW.GLFW_KEY_C, "C");
        keyMap.put(GLFW.GLFW_KEY_D, "D");
        keyMap.put(GLFW.GLFW_KEY_E, "E");
        keyMap.put(GLFW.GLFW_KEY_F, "F");
        keyMap.put(GLFW.GLFW_KEY_G, "G");
        keyMap.put(GLFW.GLFW_KEY_H, "H");
        keyMap.put(GLFW.GLFW_KEY_I, "I");
        keyMap.put(GLFW.GLFW_KEY_J, "J");
        keyMap.put(GLFW.GLFW_KEY_K, "K");
        keyMap.put(GLFW.GLFW_KEY_L, "L");
        keyMap.put(GLFW.GLFW_KEY_M, "M");
        keyMap.put(GLFW.GLFW_KEY_N, "N");
        keyMap.put(GLFW.GLFW_KEY_O, "O");
        keyMap.put(GLFW.GLFW_KEY_P, "P");
        keyMap.put(GLFW.GLFW_KEY_Q, "Q");
        keyMap.put(GLFW.GLFW_KEY_R, "R");
        keyMap.put(GLFW.GLFW_KEY_S, "S");
        keyMap.put(GLFW.GLFW_KEY_T, "T");
        keyMap.put(GLFW.GLFW_KEY_U, "U");
        keyMap.put(GLFW.GLFW_KEY_V, "V");
        keyMap.put(GLFW.GLFW_KEY_W, "W");
        keyMap.put(GLFW.GLFW_KEY_X, "X");
        keyMap.put(GLFW.GLFW_KEY_Y, "Y");
        keyMap.put(GLFW.GLFW_KEY_Z, "Z");

        keyMap.put(GLFW.GLFW_KEY_0, "0");
        keyMap.put(GLFW.GLFW_KEY_1, "1");
        keyMap.put(GLFW.GLFW_KEY_2, "2");
        keyMap.put(GLFW.GLFW_KEY_3, "3");
        keyMap.put(GLFW.GLFW_KEY_4, "4");
        keyMap.put(GLFW.GLFW_KEY_5, "5");
        keyMap.put(GLFW.GLFW_KEY_6, "6");
        keyMap.put(GLFW.GLFW_KEY_7, "7");
        keyMap.put(GLFW.GLFW_KEY_8, "8");
        keyMap.put(GLFW.GLFW_KEY_9, "9");

        keyMap.put(GLFW.GLFW_KEY_F1, "F1");
        keyMap.put(GLFW.GLFW_KEY_F2, "F2");
        keyMap.put(GLFW.GLFW_KEY_F3, "F3");
        keyMap.put(GLFW.GLFW_KEY_F4, "F4");
        keyMap.put(GLFW.GLFW_KEY_F5, "F5");
        keyMap.put(GLFW.GLFW_KEY_F6, "F6");
        keyMap.put(GLFW.GLFW_KEY_F7, "F7");
        keyMap.put(GLFW.GLFW_KEY_F8, "F8");
        keyMap.put(GLFW.GLFW_KEY_F9, "F9");
        keyMap.put(GLFW.GLFW_KEY_F10, "F10");
        keyMap.put(GLFW.GLFW_KEY_F11, "F11");
        keyMap.put(GLFW.GLFW_KEY_F12, "F12");
        keyMap.put(GLFW.GLFW_KEY_F13, "F13");
        keyMap.put(GLFW.GLFW_KEY_F14, "F14");
        keyMap.put(GLFW.GLFW_KEY_F15, "F15");
        keyMap.put(GLFW.GLFW_KEY_F16, "F16");
        keyMap.put(GLFW.GLFW_KEY_F17, "F17");
        keyMap.put(GLFW.GLFW_KEY_F18, "F18");
        keyMap.put(GLFW.GLFW_KEY_F19, "F19");
        keyMap.put(GLFW.GLFW_KEY_F20, "F20");
        keyMap.put(GLFW.GLFW_KEY_F21, "F21");
        keyMap.put(GLFW.GLFW_KEY_F22, "F22");
        keyMap.put(GLFW.GLFW_KEY_F23, "F23");
        keyMap.put(GLFW.GLFW_KEY_F24, "F24");
        keyMap.put(GLFW.GLFW_KEY_F25, "F25");

        keyMap.put(GLFW.GLFW_KEY_KP_0, "KP0");
        keyMap.put(GLFW.GLFW_KEY_KP_1, "KP1");
        keyMap.put(GLFW.GLFW_KEY_KP_2, "KP2");
        keyMap.put(GLFW.GLFW_KEY_KP_3, "KP3");
        keyMap.put(GLFW.GLFW_KEY_KP_4, "KP4");
        keyMap.put(GLFW.GLFW_KEY_KP_5, "KP5");
        keyMap.put(GLFW.GLFW_KEY_KP_6, "KP6");
        keyMap.put(GLFW.GLFW_KEY_KP_7, "KP7");
        keyMap.put(GLFW.GLFW_KEY_KP_8, "KP8");
        keyMap.put(GLFW.GLFW_KEY_KP_9, "KP9");
        keyMap.put(GLFW.GLFW_KEY_KP_DECIMAL, "KP.");
        keyMap.put(GLFW.GLFW_KEY_KP_DIVIDE, "KP/");
        keyMap.put(GLFW.GLFW_KEY_KP_MULTIPLY, "KP*");
        keyMap.put(GLFW.GLFW_KEY_KP_SUBTRACT, "KP-");
        keyMap.put(GLFW.GLFW_KEY_KP_ADD, "KP+");
        keyMap.put(GLFW.GLFW_KEY_KP_ENTER, "KPENTER");
        keyMap.put(GLFW.GLFW_KEY_KP_EQUAL, "KP=");

        keyMap.put(GLFW.GLFW_KEY_SPACE, "SPACE");
        keyMap.put(GLFW.GLFW_KEY_ENTER, "ENTER");
        keyMap.put(GLFW.GLFW_KEY_ESCAPE, "ESC");
        keyMap.put(GLFW.GLFW_KEY_TAB, "TAB");
        keyMap.put(GLFW.GLFW_KEY_BACKSPACE, "BACK");
        keyMap.put(GLFW.GLFW_KEY_INSERT, "INS");
        keyMap.put(GLFW.GLFW_KEY_DELETE, "DEL");
        keyMap.put(GLFW.GLFW_KEY_HOME, "HOME");
        keyMap.put(GLFW.GLFW_KEY_END, "END");
        keyMap.put(GLFW.GLFW_KEY_PAGE_UP, "PGUP");
        keyMap.put(GLFW.GLFW_KEY_PAGE_DOWN, "PGDN");

        keyMap.put(GLFW.GLFW_KEY_UP, "UP");
        keyMap.put(GLFW.GLFW_KEY_DOWN, "DOWN");
        keyMap.put(GLFW.GLFW_KEY_LEFT, "LEFT");
        keyMap.put(GLFW.GLFW_KEY_RIGHT, "RIGHT");

        keyMap.put(GLFW.GLFW_KEY_LEFT_SHIFT, "LSHIFT");
        keyMap.put(GLFW.GLFW_KEY_RIGHT_SHIFT, "RSHIFT");
        keyMap.put(GLFW.GLFW_KEY_LEFT_CONTROL, "LCTRL");
        keyMap.put(GLFW.GLFW_KEY_RIGHT_CONTROL, "RCTRL");
        keyMap.put(GLFW.GLFW_KEY_LEFT_ALT, "LALT");
        keyMap.put(GLFW.GLFW_KEY_RIGHT_ALT, "RALT");
        keyMap.put(GLFW.GLFW_KEY_LEFT_SUPER, "LWIN");
        keyMap.put(GLFW.GLFW_KEY_RIGHT_SUPER, "RWIN");

        keyMap.put(GLFW.GLFW_KEY_CAPS_LOCK, "CAPS");
        keyMap.put(GLFW.GLFW_KEY_NUM_LOCK, "NUM");
        keyMap.put(GLFW.GLFW_KEY_SCROLL_LOCK, "SCRLK");

        keyMap.put(GLFW.GLFW_KEY_MENU, "MENU");
        keyMap.put(GLFW.GLFW_KEY_PRINT_SCREEN, "PRTSC");
        keyMap.put(GLFW.GLFW_KEY_PAUSE, "PAUSE");

        keyMap.put(GLFW.GLFW_KEY_GRAVE_ACCENT, "`");
        keyMap.put(GLFW.GLFW_KEY_MINUS, "-");
        keyMap.put(GLFW.GLFW_KEY_EQUAL, "=");
        keyMap.put(GLFW.GLFW_KEY_LEFT_BRACKET, "[");
        keyMap.put(GLFW.GLFW_KEY_RIGHT_BRACKET, "]");
        keyMap.put(GLFW.GLFW_KEY_BACKSLASH, "\\");
        keyMap.put(GLFW.GLFW_KEY_SEMICOLON, ";");
        keyMap.put(GLFW.GLFW_KEY_APOSTROPHE, "'");
        keyMap.put(GLFW.GLFW_KEY_COMMA, ",");
        keyMap.put(GLFW.GLFW_KEY_PERIOD, ".");
        keyMap.put(GLFW.GLFW_KEY_SLASH, "/");

        // International
        keyMap.put(GLFW.GLFW_KEY_WORLD_1, "WORLD1");
        keyMap.put(GLFW.GLFW_KEY_WORLD_2, "WORLD2");

        keyMap.put(GLFW.GLFW_KEY_UNKNOWN, "NONE");
    }
}
