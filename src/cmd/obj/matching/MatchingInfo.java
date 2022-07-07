package cmd.obj.matching;

import java.time.LocalDateTime;
import java.util.Objects;

public class MatchingInfo {
    public int playerId;
    public long time;

    public MatchingInfo(long playerId, long time) {

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
}
