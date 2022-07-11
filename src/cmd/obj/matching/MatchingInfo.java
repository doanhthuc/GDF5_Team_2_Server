package cmd.obj.matching;

import java.time.LocalDateTime;
import java.util.Objects;

public class MatchingInfo {
    private int playerId;
    private long time;
    private int trophy;
    private int startRank;
    private int endRank;

    public MatchingInfo(int playerId, long time, int trophy) {
        this.playerId = playerId;
        this.time = time;
        this.trophy = trophy;

        int startRank = trophy - 100 < 0 ? 0 : trophy - 100;
        int endRank = trophy + 100;
        this.startRank = startRank;
        this.endRank = endRank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchingInfo that = (MatchingInfo) o;
        return playerId == that.playerId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerId);
    }

    public void increaseRank() {
        this.startRank = Math.max(this.startRank - 50, 0);
        this.endRank = this.endRank + 50;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getTrophy() {
        return trophy;
    }

    public void setTrophy(int trophy) {
        this.trophy = trophy;
    }

    public int getStartRank() {
        return startRank;
    }

    public void setStartRank(int startRank) {
        this.startRank = startRank;
    }

    public int getEndRank() {
        return endRank;
    }

    public void setEndRank(int endRank) {
        this.endRank = endRank;
    }
}
