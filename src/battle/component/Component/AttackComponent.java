package battle.component.Component;

import battle.component.EffectComponent.DamageEffect;
import battle.component.EffectComponent.EffectComponent;
import battle.config.GameConfig;

import java.util.ArrayList;

public class AttackComponent extends Component {
    public String name="AttackComponent";
    public int originDamage;
    public double _damage;
    public int targetStategy;
    public double range;
    public double originSpeed;
    public double speed;
    public double countdown;
    public ArrayList<EffectComponent> effects = new ArrayList<>();

    public AttackComponent(int damage, int targetStrategy, double range, double speed, double countdown, EffectComponent effects) {
        super(GameConfig.COMPONENT_ID.ATTACK);
        this.originDamage = damage;
        this._damage = damage;
        this.targetStategy = targetStrategy;
        this.range = range;
        this.speed = speed;
        this.countdown = countdown;
        this.effects.add(new DamageEffect(this._damage));
    }

    public void reset(int damage, int targetStrategy, double range, double speed, double countdown, EffectComponent effects) {
        this.originDamage = damage;
        this._damage = damage;
        this.targetStategy = targetStrategy;
        this.range = range;
        this.speed = speed;
        this.countdown = countdown;
        this.effects.add(new DamageEffect(this._damage));
    }

    public void setDamage(int damage) {
        this._damage = damage;
        for (int i = 0; i < this.effects.size(); i++) {
            DamageEffect effect = (DamageEffect) this.effects.get(i);
            if (effect.typeID == GameConfig.COMPONENT_ID.DAMAGE_EFFECT) {
                effect.damage = this._damage;
            }
        }
    }

    public double getDamage() {
        return this._damage;
    }
}