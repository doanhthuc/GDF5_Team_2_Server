package battle.component.effect;

import battle.config.GameConfig;
import battle.factory.ComponentFactory;

public class BuffAttackSpeedEffect extends EffectComponent {
    private String name = "BuffAttackSpeedEffect";
    public static int typeID = GameConfig.COMPONENT_ID.BUFF_ATTACK_SPEED;
    private double percent;

    public BuffAttackSpeedEffect(double percent) {
        super(GameConfig.COMPONENT_ID.BUFF_ATTACK_SPEED);
        this.percent = percent;
    }

    public BuffAttackSpeedEffect clone(ComponentFactory componentFactory) {
        try {
            return componentFactory.createBuffAttackSpeedEffect(percent);
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
}