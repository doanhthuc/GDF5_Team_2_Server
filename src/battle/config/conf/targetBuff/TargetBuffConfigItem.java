package battle.config.conf.targetBuff;


import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class TargetBuffConfigItem implements Serializable {
    private String name;
    private String durationType;
    private Map<Short, Integer> duration;
    private boolean durationUseCardLevel;
    private boolean effectsUseCardLevel;
    private Map<Short, Double> moveDistance;
    private Map<Short, List<TargetBuffEffect>> effects;
    private String state;

    public String getName() {
        return name;
    }

    public String getDurationType() {
        return durationType;
    }

    public Map<Short, Integer> getDuration() {
        return duration;
    }

    public boolean isDurationUseCardLevel() {
        return durationUseCardLevel;
    }

    public boolean isEffectsUseCardLevel() {
        return effectsUseCardLevel;
    }

    public Map<Short, Double> getMoveDistance() {
        return moveDistance;
    }

    public Map<Short, List<TargetBuffEffect>> getEffects() {
        return effects;
    }

    public String getState() {
        return state;
    }

    @Override
    public String toString() {
        return "TargetBuffConfigItem{" +
                "name='" + name + '\'' +
                ", durationType='" + durationType + '\'' +
                ", duration=" + duration +
                ", durationUseCardLevel=" + durationUseCardLevel +
                ", effectsUseCardLevel=" + effectsUseCardLevel +
                ", moveDistance=" + moveDistance +
                ", effects=" + effects +
                ", state='" + state + '\'' +
                '}';
    }
}
