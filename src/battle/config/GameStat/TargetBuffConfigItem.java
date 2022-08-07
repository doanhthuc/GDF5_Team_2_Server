package battle.config.GameStat;

import java.util.HashMap;
import java.util.List;

public class TargetBuffConfigItem {
    String name;
    String durationType;
    boolean durationUseCardLevel;
    HashMap<Integer, Double> duration = new HashMap<>();
    HashMap<Integer, List<EffectStat>> listEffect = new HashMap<>();
    String state;

    public TargetBuffConfigItem(String name, String durationType, boolean durationUseCardLevel, String state) {
        this.name = name;
        this.durationType = durationType;
        this.durationUseCardLevel = durationUseCardLevel;
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

    public void addDurationItem(int level, double duration) {
        this.duration.put(level, duration);
    }

    public void addEffectStats(int level, List<EffectStat> effectStats) {
        this.listEffect.put(level, effectStats);
    }


    public void setDurationType(String durationType) {
        this.durationType = durationType;
    }

    public boolean getDurationUseCardLevel() {
        return durationUseCardLevel;
    }

    public void setDurationUseCardLevel(boolean durationUseCardLevel) {
        this.durationUseCardLevel = durationUseCardLevel;
    }

    public HashMap<Integer, Double> getDuration() {
        return duration;
    }

    public void setDuration(HashMap<Integer, Double> duration) {
        this.duration = duration;
    }

    public HashMap<Integer, List<EffectStat>> getListEffect() {
        return listEffect;
    }

    public void setListEffect(HashMap<Integer, List<EffectStat>> listEffect) {
        this.listEffect = listEffect;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
