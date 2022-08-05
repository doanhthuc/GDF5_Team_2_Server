package battle.system;

import battle.Battle;
import battle.common.EntityMode;
import battle.common.Point;
import battle.common.Utils;
import battle.component.common.AttackComponent;
import battle.component.common.PositionComponent;
import battle.component.common.VelocityComponent;
import battle.component.effect.EffectComponent;
import battle.component.info.LifeComponent;
import battle.component.info.MonsterInfoComponent;
import battle.component.info.SpellInfoComponent;
import battle.config.GameConfig;
import battle.entity.EntityECS;
import battle.manager.EntityManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ResetSystem extends SystemECS {
    public int id = GameConfig.SYSTEM_ID.RESET_SYSTEM;

    public ResetSystem() {
        super(GameConfig.SYSTEM_ID.RESET_SYSTEM);
        java.lang.System.out.println("new SpellSystem");
    }

    @Override
    public void run(Battle battle) {
        this.tick = this.getElapseTime();
        this.handleResetDamageEffect(battle);
    }
    public void handleResetDamageEffect(Battle battle) {
        List<EntityECS> towerList = battle.getEntityManager().getEntitiesHasComponents(Arrays.asList(AttackComponent.typeID));
        for(EntityECS tower: towerList)
        {
            AttackComponent attackComponent = (AttackComponent) tower.getComponent(AttackComponent.typeID);
            attackComponent.setDamage(attackComponent.getOriginDamage());
            attackComponent.setSpeed(attackComponent.getOriginSpeed());
        }
    }
}
