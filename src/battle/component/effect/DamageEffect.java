package battle.component.effect;

import battle.config.GameConfig;
import battle.factory.ComponentFactory;

import java.nio.ByteBuffer;

public class DamageEffect extends EffectComponent {
    private String name = "DamageEffect";
    public static int typeID = GameConfig.COMPONENT_ID.DAMAGE_EFFECT;
    private double damage;

    public DamageEffect(double damage) {
        super(GameConfig.COMPONENT_ID.DAMAGE_EFFECT);
        this.reset(damage);
    }

    public DamageEffect clone(ComponentFactory componentFactory) throws Exception {
        return componentFactory.createDamageEffect(damage);
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public void reset(double damage) {
        this.damage = damage;
    }

    @Override
    public void createData(ByteBuffer bf) {
        super.createData(bf);
        bf.putDouble(damage);
    }
}
