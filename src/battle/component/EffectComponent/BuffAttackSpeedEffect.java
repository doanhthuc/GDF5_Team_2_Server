package battle.Component.EffectComponent;

import battle.Config.GameConfig;

public class BuffAttackSpeedEffect extends EffectComponent {
    public String name = "BuffAttackSpeedEffect";
    public double percent;

    public BuffAttackSpeedEffect(double percent) {
        super(GameConfig.COMPONENT_ID.BUFF_ATTACK_SPEED);
        this.percent = percent;
    }

    public BuffAttackSpeedEffect clone() {
        return new BuffAttackSpeedEffect(this.percent);
    }

    public void reset() {

    }
}