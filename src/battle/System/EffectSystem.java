package battle.System;

import battle.Component.Component.Component;
import battle.Component.EffectComponent.DamageEffect;
import battle.Component.InfoComponent.LifeComponent;
import battle.Config.GameConfig;
import battle.Entity.EntityECS;
import battle.Manager.EntityManager;

import java.util.ArrayList;

public class EffectSystem extends System {
    int id = GameConfig.SYSTEM_ID.EFFECT;
    public String name = "EffectSystem";

    public EffectSystem() {
        java.lang.System.out.println("new EffectSystem");
    }

    @Override
    public void run() {
        this.tick = this.getEclapseTime();
        this._handleBuffAttackSpeedEffect(tick);
        this._handleDamageEffect(tick);
    }

    public void _handleBuffAttackSpeedEffect(long tick) {

    }

    public void _handleBuffAttackDamageEffect(long tick) {

    }

    public void _handleDamageEffect(long tick) {
        ArrayList<Integer> damageEffectID = new ArrayList<>();
        damageEffectID.add(GameConfig.COMPONENT_ID.DAMAGE_EFFECT);
        ArrayList<EntityECS> damagedEntity = EntityManager.getInstance().getEntitiesHasComponents(damageEffectID);

        for (EntityECS entity : damagedEntity) {
            LifeComponent life = (LifeComponent) entity.getComponent(GameConfig.COMPONENT_ID.LIFE);
            if (life!=null) {
                DamageEffect damageEffect= (DamageEffect) entity.getComponent(GameConfig.COMPONENT_ID.DAMAGE_EFFECT);
                life.hp-=damageEffect.damage;
                entity.removeComponent(damageEffect);
            }
        }
    }


}
