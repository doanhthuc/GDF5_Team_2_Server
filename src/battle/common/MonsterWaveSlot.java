package battle.common;

public class MonsterWaveSlot {
    private int monsterId;
    private double rate;
    private String monsterClass;
    private String category;

    public MonsterWaveSlot(int monsterId, double rate, String monsterClass, String category) {
        this.monsterId = monsterId;
        this.rate = rate;
        this.monsterClass = monsterClass;
        this.category = category;
    }

    public int getMonsterId() {
        return monsterId;
    }

    public void setMonsterId(int monsterId) {
        this.monsterId = monsterId;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getMonsterClass() {
        return monsterClass;
    }

    public void setMonsterClass(String monsterClass) {
        this.monsterClass = monsterClass;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
