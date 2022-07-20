package battle.component.effect;

import battle.config.GameConfig;

public class BuffAttackSpeedEffect extends EffectComponent {
    private String name = "BuffAttackSpeedEffect";
    private double percent;

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