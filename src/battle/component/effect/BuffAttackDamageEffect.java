package battle.component.effect;

import battle.config.GameConfig;

public class BuffAttackDamageEffect extends EffectComponent {
    private String name = "BuffAttackDamageEffect";
    private double percent;

    public BuffAttackDamageEffect(double percent) {
        super(GameConfig.COMPONENT_ID.BUFF_ATTACK_DAMAGE);
        this.percent = percent;
    }

    public BuffAttackDamageEffect clone() {
        return new BuffAttackDamageEffect(this.percent);
    }

    public void reset(double percent) {
        this.percent = percent;
    }
}