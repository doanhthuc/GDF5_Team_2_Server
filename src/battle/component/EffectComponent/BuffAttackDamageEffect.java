package battle.component.EffectComponent;

import battle.config.GameConfig;

public class BuffAttackDamageEffect extends EffectComponent {
    public double percent;
    private String name = "BuffAttackDamageEffect";

    public BuffAttackDamageEffect(double percent) {
        super(GameConfig.COMPONENT_ID.BUFF_ATTACK_DAMAGE);
        this.percent = percent;
    }

    public BuffAttackDamageEffect clone() {
        return new BuffAttackDamageEffect(this.percent);
    }

    public void reset() {

    }

    ;
}