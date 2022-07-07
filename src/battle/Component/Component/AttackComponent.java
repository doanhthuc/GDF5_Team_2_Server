package battle.Component.Component;

import battle.Component.EffectComponent.DamageEffect;
import battle.Component.EffectComponent.EffectComponent;
import battle.Config.GameConfig;

import java.util.ArrayList;

class AttackComponent extends Component {
    public String name = " AttackComponent";
    public int originDamage;
    public int _damage;
    public int targetStategy;
    public int range;
    public int originSpeed;
    public int speed;
    public int countdown;
    ArrayList<EffectComponent> effects = new ArrayList<>();

    public AttackComponent(int damage, int targetStrategy, int range, int speed, int countdown, EffectComponent effects) {
        super(GameConfig.COMPONENT_ID.ATTACK);
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