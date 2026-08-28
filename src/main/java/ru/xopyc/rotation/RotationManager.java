package ru.xopyc.rotation;

import lombok.Getter;
import ru.xopyc.rotation.correction.Correction;
import ru.xopyc.IMinecraft;

// Заготовка для глобальных ротаций
@Getter
public final class RotationManager implements IMinecraft {
    @Getter
    private static final RotationManager instance = new RotationManager();
    private final Rotation visualRotation = new Rotation(0, 0);
    private final Rotation currentRotation = new Rotation(0, 0);
    private final Rotation previousRotation = new Rotation(0, 0);
    private boolean rotating = false;

    private RotationManager() {
    }

    /**
     * Синхронизирует физическую ротацию с визуальной
     * Меняет глобальный флаг rotating на false
     */
    public void reset() {
        if (mc.player != null && rotating) {
            setCurrentRotation(visualRotation);
        }
        rotating = false;
    }

    /**
     * Задает текущую физическую ротацию игрока
     * Меняет глобальный флаг rotating на true
     */
    public void setCurrentRotation(Rotation rotation) {
        // При первом включении синхронизируем визуал с камерой игрока
        if (!rotating && mc.player != null) {
            visualRotation.setYaw(mc.player.getYaw());
            visualRotation.setPitch(mc.player.getPitch());
        }

        rotating = true;

        previousRotation.setYaw(currentRotation.getYaw());
        previousRotation.setPitch(currentRotation.getPitch());

        currentRotation.setYaw(rotation.getYaw());
        currentRotation.setPitch(rotation.getPitch());

        if (getCorrection() == Correction.FOCUSED) {
            visualRotation.setYaw(rotation.getYaw());
            visualRotation.setPitch(rotation.getPitch());
        }

        if (mc.player != null) {
            mc.player.prevYaw = mc.player.getYaw();
            mc.player.prevPitch = mc.player.getPitch();
            mc.player.lastRenderYaw = mc.player.getYaw();
            mc.player.lastRenderPitch = mc.player.getPitch();
            // mc.player.prevBodyYaw = mc.player.getBodyYaw();
            // mc.player.prevHeadYaw = mc.player.getHeadYaw();

            mc.player.setYaw(rotation.getYaw());
            mc.player.setPitch(rotation.getPitch());
            mc.player.renderYaw = rotation.getYaw();
            mc.player.renderPitch = rotation.getPitch();
            // mc.player.setHeadYaw(rotation.getYaw());
            // mc.player.setBodyYaw(rotation.getYaw());
        }

    }

    public Correction getCorrection() {
        return Correction.FREE;
    }
}