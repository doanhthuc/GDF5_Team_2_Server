package battle.config.conf.tower;

import java.io.Serializable;
import java.util.HashMap;

public class TowerConfigItem implements Serializable {
    public String name;
    public String category;
    public String archetype;
    public String targetType;
    public String bulletType;
    public int bulletTargetBuffType;
    public int auraTargetBuffType;
    public int energy;
    public int attackAnimationTime;

    public int getAuraTargetBuffType() {
        return auraTargetBuffType;
    }

    public int shootAnimationTime;
    public HashMap<Short, TowerStat> stat;

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getArchetype() {
        return archetype;
    }

    public String getTargetType() {
        return targetType;
    }

    public String getBulletType() {
        return bulletType;
    }

    public int getBulletTargetBuffType() {
        return bulletTargetBuffType;
    }

    public int getEnergy() {
        return energy;
    }

    public int getAttackAnimationTime() {
        return attackAnimationTime;
    }

    public int getShootAnimationTime() {
        return shootAnimationTime;
    }

    public HashMap<Short, TowerStat> getStat() {
        return stat;
    }

    @Override
    public String toString() {
        return "TowerConfigItem{" +
                "name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", archetype='" + archetype + '\'' +
                ", targetType='" + targetType + '\'' +
                ", bulletType='" + bulletType + '\'' +
                ", bulletTargetBuffType=" + bulletTargetBuffType +
                ", energy=" + energy +
                ", attackAnimationTime=" + attackAnimationTime +
                ", shootAnimationTime=" + shootAnimationTime +
                ", stat=" + stat +
                '}';
    }
}
