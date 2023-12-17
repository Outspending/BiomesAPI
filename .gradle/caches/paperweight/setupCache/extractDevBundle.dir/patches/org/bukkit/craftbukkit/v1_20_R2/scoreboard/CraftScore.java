package org.bukkit.craftbukkit.v1_20_R2.scoreboard;

import java.util.Map;
import net.minecraft.world.scores.Scoreboard;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

/**
 * TL;DR: This class is special and lazily grabs a handle...
 * ...because a handle is a full fledged (I think permanent) hashMap for the associated name.
 * <p>
 * Also, as an added perk, a CraftScore will (intentionally) stay a valid reference so long as objective is valid.
 */
final class CraftScore implements Score {
    private final String entry;
    private final CraftObjective objective;

    CraftScore(CraftObjective objective, String entry) {
        this.objective = objective;
        this.entry = entry;
    }

    @Override
    public OfflinePlayer getPlayer() {
        return Bukkit.getOfflinePlayer(this.entry);
    }

    @Override
    public String getEntry() {
        return this.entry;
    }

    @Override
    public Objective getObjective() {
        return this.objective;
    }

    @Override
    public int getScore() {
        Scoreboard board = this.objective.checkState().board;

        if (board.getTrackedPlayers().contains(this.entry)) { // Lazy
            Map<net.minecraft.world.scores.Objective, net.minecraft.world.scores.Score> scores = board.getPlayerScores(this.entry);
            net.minecraft.world.scores.Score score = scores.get(this.objective.getHandle());
            if (score != null) { // Lazy
                return score.getScore();
            }
        }

        return 0; // Lazy
    }

    @Override
    public void setScore(int score) {
        this.objective.checkState().board.getOrCreatePlayerScore(this.entry, this.objective.getHandle()).setScore(score);
    }

    @Override
    public boolean isScoreSet() {
        Scoreboard board = this.objective.checkState().board;

        return board.getTrackedPlayers().contains(this.entry) && board.getPlayerScores(this.entry).containsKey(this.objective.getHandle());
    }

    @Override
    public CraftScoreboard getScoreboard() {
        return this.objective.getScoreboard();
    }

    // Paper start
    @Override
    public void resetScore() {
        Scoreboard board = this.objective.checkState().board;
        board.resetPlayerScore(entry, this.objective.getHandle());
    }
    // Paper end
}
