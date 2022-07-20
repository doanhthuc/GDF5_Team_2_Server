package battle.component.EffectComponent;

import battle.config.GameConfig;

public class DamageEffect extends EffectComponent {
    private String name = "DamageEffect";
    private double damage;

    public DamageEffect(double damage) {
        super(GameConfig.COMPONENT_ID.DAMAGE_EFFECT);
        this.damage = damage;
    }

    public DamageEffect clone() {
        return new DamageEffect(this.damage);
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public void reset() {
    }
}
