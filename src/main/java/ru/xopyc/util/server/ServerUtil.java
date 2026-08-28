package ru.xopyc.util.server;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.*;
import net.minecraft.scoreboard.number.NumberFormat;
import ru.xopyc.IMinecraft;

public final class ServerUtil implements IMinecraft {
    private ServerUtil() {
    }

    public static boolean isNPC(PlayerEntity player) {
        return mc.getNetworkHandler().getPlayerListEntry(player.getUuid()) == null;
    }

    public static float resolveHealth(LivingEntity living) {
        float scoreboardResolved = getBelowNameScore(living);
        return scoreboardResolved == -1 ? living.getHealth() : scoreboardResolved;
    }

    private static int getBelowNameScore(LivingEntity living) {
        Scoreboard scoreboard = living.getWorld().getScoreboard();
        ScoreboardObjective objective = scoreboard.getObjectiveForSlot(ScoreboardDisplaySlot.BELOW_NAME);
        if (objective == null) return -1;

        ReadableScoreboardScore score = scoreboard.getScore(living, objective);
        if (score == null) return -1;

        return score.getScore();
    }

    private static int getScore2(LivingEntity living) {
        Scoreboard scoreboard = living.getWorld().getScoreboard();
        ScoreboardObjective objective = scoreboard.getObjectiveForSlot(ScoreboardDisplaySlot.BELOW_NAME);

        if (objective == null) return -1;

        ReadableScoreboardScore score = scoreboard.getScore(living, objective);
        if (score == null) return -1;

        NumberFormat format = score.getNumberFormat();
        if (format == null) return -1;

        String text = score.getFormattedScore(format).getString();

        int start = -1;
        for (int i = 0; i < text.length(); i++) {
            if (Character.isDigit(text.charAt(i))) {
                start = i;
                break;
            }
        }

        if (start == -1) return -1;

        int end = start;
        while (end < text.length() && Character.isDigit(text.charAt(end))) {
            end++;
        }

        return Integer.parseInt(text.substring(start, end));
    }

}
