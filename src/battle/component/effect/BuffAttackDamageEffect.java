package battle.component.effect;

import battle.config.GameConfig;
import battle.factory.ComponentFactory;

public class BuffAttackDamageEffect extends EffectComponent {
    private String name = "BuffAttackDamageEffect";
    public static int typeID = GameConfig.COMPONENT_ID.BUFF_ATTACK_DAMAGE;
    private double percent;

    public BuffAttackDamageEffect(double percent) {
        super(GameConfig.COMPONENT_ID.BUFF_ATTACK_DAMAGE);
        this.percent = percent;
    }

    public BuffAttackDamageEffect clone() {
        try {
            return ComponentFactory.getInstance().createBuffAttackDamageEffect(percent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void reset(double percent) {
        this.percent = percent;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }
}