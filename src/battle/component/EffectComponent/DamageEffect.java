package battle.component.EffectComponent;

import battle.config.GameConfig;

public class DamageEffect extends EffectComponent {
    public String name = "DamageEffect";
    public double damage;

    public DamageEffect(double damage) {
        super(GameConfig.COMPONENT_ID.DAMAGE_EFFECT);
        this.damage = damage;
    }

    public DamageEffect clone() {
        return new DamageEffect(this.damage);
    }

    public void reset() {

    }
}
