package battle.system;

import battle.component.EffectComponent.DamageEffect;
import battle.component.InfoComponent.LifeComponent;
import battle.config.GameConfig;
import battle.entity.EntityECS;
import battle.manager.EntityManager;

import java.util.ArrayList;

public class EffectSystem extends SystemECS {
    public String name = "EffectSystem";
    int id = GameConfig.SYSTEM_ID.EFFECT;

    public EffectSystem() {
        java.lang.System.out.println("new EffectSystem");
    }

    @Override
    public void run() {
        this.tick = this.getElapseTime();
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
            if (life != null) {
                DamageEffect damageEffect = (DamageEffect) entity.getComponent(GameConfig.COMPONENT_ID.DAMAGE_EFFECT);
                life.hp -= damageEffect.damage;
                entity.removeComponent(damageEffect);
            }
        }
    }


}
