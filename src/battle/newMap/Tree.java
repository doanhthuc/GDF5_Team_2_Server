package battle.newMap;

public class Tree extends ObjectInCell {
    private double hp;

    public Tree(double hp) {
        super(ObjectInCellType.TREE);
        this.hp = hp;
    }

    public double getHp() {
        return hp;
    }

    public void setHp(double hp) {
        this.hp = hp;
    }

    @Override
    public String toString() {
        return "Tree{" +
                "hp=" + hp +
                '}' + '\n';
    }
}
