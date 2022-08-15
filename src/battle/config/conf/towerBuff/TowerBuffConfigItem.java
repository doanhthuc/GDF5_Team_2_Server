package battle.config.conf.towerBuff;

import battle.config.conf.targetBuff.TargetBuffEffect;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class TowerBuffConfigItem implements Serializable {
    private String name;
    private String durationType;
    private boolean durationUseCardLevel;
    private Map<Short, Long> duration;
    private Map<Short, List<TowerBuffEffect>> effects;
    private String state;

    public boolean isDurationUseCardLevel() {
        return durationUseCardLevel;
    }

    public void setDurationUseCardLevel(boolean durationUseCardLevel) {
        this.durationUseCardLevel = durationUseCardLevel;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDurationType() {
        return durationType;
    }

    public void setDurationType(String durationType) {
        this.durationType = durationType;
    }

    public Map<Short, Long> getDuration() {
        return duration;
    }

    public void setDuration(Map<Short, Long> duration) {
        this.duration = duration;
    }

    public Map<Short, List<TowerBuffEffect>> getEffects() {
        return effects;
    }

    public void setEffects(Map<Short, List<TowerBuffEffect>> effects) {
        this.effects = effects;
    }

    @Override
    public String toString() {
        return "TowerBuffConfigItem{" +
                "name='" + name + '\'' +
                ", durationType='" + durationType + '\'' +
                ", durationUseCardLevel=" + durationUseCardLevel +
                ", duration=" + duration +
                ", effects=" + effects +
                ", state='" + state + '\'' +
                '}';
    }
}
