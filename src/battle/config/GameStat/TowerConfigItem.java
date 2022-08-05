package battle.config.GameStat;

import battle.config.GameStat.TowerStat;

import java.util.HashMap;

public class TowerConfigItem {
    private double buildingTime;
    private String name;
    private String archetype;
    private String targetType;
    private String bulletType;
    private int bulletTargetBuffType;
    private int energy;
    private int attackAnimationTime;
    private int shootAnimationTime;
    private HashMap<Integer, TowerStat> towerStatHashMap;

    public TowerConfigItem(String name, String archetype, String targetType, String bulletType, int bulletTargetBuffType, int energy, int attackAnimationTime, int shootAnimationTime, HashMap<Integer, TowerStat> towerStat) {
        this.name = name;
        this.archetype = archetype;
        this.targetType = targetType;
        this.bulletType = bulletType;
        this.bulletTargetBuffType = bulletTargetBuffType;
        this.energy = energy;
        this.attackAnimationTime = attackAnimationTime;
        this.shootAnimationTime = shootAnimationTime;
        this.towerStatHashMap = towerStat;
    }

    public double getBuildingTime() {
        return buildingTime;
    }

    public void setBuildingTime(double buildingTime) {
        this.buildingTime = buildingTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArchetype() {
        return archetype;
    }

    public void setArchetype(String archetype) {
        this.archetype = archetype;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getBulletType() {
        return bulletType;
    }

    public void setBulletType(String bulletType) {
        this.bulletType = bulletType;
    }

    public int getBulletTargetBuffType() {
        return bulletTargetBuffType;
    }

    public void setBulletTargetBuffType(int bulletTargetBuffType) {
        this.bulletTargetBuffType = bulletTargetBuffType;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getAttackAnimationTime() {
        return attackAnimationTime;
    }

    public void setAttackAnimationTime(int attackAnimationTime) {
        this.attackAnimationTime = attackAnimationTime;
    }

    public int getShootAnimationTime() {
        return shootAnimationTime;
    }

    public void setShootAnimationTime(int shootAnimationTime) {
        this.shootAnimationTime = shootAnimationTime;
    }

    public HashMap<Integer, TowerStat> getTowerStat() {
        return towerStatHashMap;
    }


}
