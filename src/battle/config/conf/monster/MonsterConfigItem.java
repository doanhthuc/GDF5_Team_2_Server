package battle.config.conf.monster;


import java.io.Serializable;

public class MonsterConfigItem implements Serializable {

    private String name;
    private String category;
    private String monsterClass;
    private double hp;
    private double speed;
    private double hitRadius;
    private double weight;
    private int ability;
    private Integer energy;
    private int gainEnergy;
    private int numberMonsters;
    private boolean priority;
    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getMonsterClass() {
        return monsterClass;
    }

    public double getHp() {
        return hp;
    }

    public double getSpeed() {
        return speed;
    }

    public double getHitRadius() {
        return hitRadius;
    }

    public double getWeight() {
        return weight;
    }

    public int getAbility() {
        return ability;
    }

    public Integer getEnergy() {
        return energy;
    }

    public int getGainEnergy() {
        return gainEnergy;
    }

    public int getNumberMonsters() {
        return numberMonsters;
    }

    public boolean isPriority() {
        return priority;
    }
}
