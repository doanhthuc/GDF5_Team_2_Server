package battle.system;

import battle.component.effect.DamageEffect;
import battle.component.info.LifeComponent;
import battle.config.GameConfig;
import battle.entity.EntityECS;
import battle.manager.EntityManager;

import java.util.ArrayList;
import java.util.List;

public class EffectSystem extends SystemECS implements Runnable {
    int id = GameConfig.SYSTEM_ID.EFFECT;
    public String name = "EffectSystem";

    public EffectSystem() {
        super(GameConfig.SYSTEM_ID.EFFECT);
        java.lang.System.out.println("new EffectSystem");
    }

    @Override
    public void run() {
//        this.tick = this.getEclapseTime();
//        this._handleBuffAttackSpeedEffect(tick);
//        this._handleDamageEffect(tick);
    }

    public void _handleBuffAttackSpeedEffect(long tick) {

    }

    public void _handleBuffAttackDamageEffect(long tick) {

    }

    public void _handleDamageEffect(long tick) {
        List<Integer> damageEffectID = new ArrayList<>();
        damageEffectID.add(GameConfig.COMPONENT_ID.DAMAGE_EFFECT);
        List<EntityECS> damagedEntity = EntityManager.getInstance().getEntitiesHasComponents(damageEffectID);

        for (EntityECS entity : damagedEntity) {
            LifeComponent life = (LifeComponent) entity.getComponent(GameConfig.COMPONENT_ID.LIFE);
            if (life != null) {
                DamageEffect damageEffect = (DamageEffect) entity.getComponent(GameConfig.COMPONENT_ID.DAMAGE_EFFECT);
                life.setHp(life.getHp() - damageEffect.getDamage());
                entity.removeComponent(damageEffect);
            }
        }
    }


}
