package battle.config.GameStat;

import java.util.HashMap;

public class MonsterConfigItem {
    private HashMap<Integer, MonsterMultiplier> monsterMultiplierHashMap = new HashMap<>();
    private HashMap<Integer, MonsterStat> monsterStatHashMap = new HashMap<>();

    public static class MonsterMultiplier {
        private double hp;
        private int numberMonster;

        public MonsterMultiplier(double hp, int numberMonster) {
            this.hp = hp;
            this.numberMonster = numberMonster;
        }

        public double getHp() {
            return hp;
        }

        public void setHp(double hp) {
            this.hp = hp;
        }

        public int getNumberMonster() {
            return numberMonster;
        }

        public void setNumberMonster(int numberMonster) {
            this.numberMonster = numberMonster;
        }
    }

    public static class MonsterStat {
        private String name;
        private String category;
        private String classs;
        private double hp;
        private double speed;
        private double hitRadius;
        private int weight;
        private int ability;
        private int energy;
        private int gainEnergy;
        private int numberMonster;

        public MonsterStat(String name, String category, String classs, double hp, double speed, double hitRadius, int weight, int ability, int energy, int gainEnergy, int numberMonster) {
            this.name = name;
            this.category = category;
            this.classs = classs;
            this.hp = hp;
            this.speed = speed;
            this.hitRadius = hitRadius;
            this.weight = weight;
            this.ability = ability;
            this.energy = energy;
            this.gainEnergy = gainEnergy;
            this.numberMonster = numberMonster;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getClasss() {
            return classs;
        }

        public void setClasss(String classs) {
            this.classs = classs;
        }

        public double getHp() {
            return hp;
        }

        public void setHp(double hp) {
            this.hp = hp;
        }

        public double getSpeed() {
            return speed;
        }

        public void setSpeed(double speed) {
            this.speed = speed;
        }

        public double getHitRadius() {
            return hitRadius;
        }

        public void setHitRadius(double hitRadius) {
            this.hitRadius = hitRadius;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public int getAbility() {
            return ability;
        }

        public void setAbility(int ability) {
            this.ability = ability;
        }

        public int getEnergy() {
            return energy;
        }

        public void setEnergy(int energy) {
            this.energy = energy;
        }

        public int getGainEnergy() {
            return gainEnergy;
        }

        public void setGainEnergy(int gainEnergy) {
            this.gainEnergy = gainEnergy;
        }

        public int getNumberMonster() {
            return numberMonster;
        }

        public void setNumberMonster(int numberMonster) {
            this.numberMonster = numberMonster;
        }
    }

    public void addMonsterMultiplier(int multi, MonsterMultiplier multiplier)
    {
        this.monsterMultiplierHashMap.put(multi,multiplier);
    }

    public void addMonsterStat(int monsterID, MonsterStat monsterStat)
    {
        this.monsterStatHashMap.put(monsterID,monsterStat);
    }

    public HashMap<Integer, MonsterMultiplier> getMonsterMultiplierHashMap() {
        return monsterMultiplierHashMap;
    }

    public void setMonsterMultiplierHashMap(HashMap<Integer, MonsterMultiplier> monsterMultiplierHashMap) {
        this.monsterMultiplierHashMap = monsterMultiplierHashMap;
    }

    public HashMap<Integer, MonsterStat> getMonsterStatHashMap() {
        return monsterStatHashMap;
    }

    public void setMonsterStatHashMap(HashMap<Integer, MonsterStat> monsterStatHashMap) {
        this.monsterStatHashMap = monsterStatHashMap;
    }

    public MonsterStat getMonsterStatByMonsterID(int monsterID)
    {
        return this.monsterStatHashMap.get(monsterID);
    }

    public MonsterMultiplier getMonsterMultiplierByNumber(int number)
    {
        return this.monsterMultiplierHashMap.get(number);
    }
}
