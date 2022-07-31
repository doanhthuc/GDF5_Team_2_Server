package battle.component.info;

import battle.config.GameConfig;
import battle.factory.ComponentFactory;

public class LifeComponent extends InfoComponent {
    private String name = "LifeComponent";
    public static int typeID = GameConfig.COMPONENT_ID.LIFE;
    private double hp;
    private double maxHP;

    public LifeComponent(double hp, double maxHP) {
        super(GameConfig.COMPONENT_ID.LIFE);
        this.hp = hp;
        this.maxHP = maxHP;
    }

    public LifeComponent(double hp) {
        super(GameConfig.COMPONENT_ID.LIFE);
        this.hp = hp;
        this.maxHP = hp;
    }

    public void reset(double hp) {
        this.hp = hp;
    }

//    public LifeComponent clone(double hp) {
//        try {
//            return ComponentFactory.getInstance().createLifeComponent(hp);
//        } catch (Exception e) {
//            return null;
//        }
//    }

    public double getHp() {
        return hp;
    }

    public void setHp(double hp) {
        this.hp = hp;
    }

    public double getMaxHP() {
        return maxHP;
    }

    public void setMaxHP(double maxHP) {
        this.maxHP = maxHP;
    }
}