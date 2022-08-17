package battle.system;

import battle.Battle;
import battle.component.common.AttackComponent;
import battle.component.common.Component;
import battle.config.GameConfig;
import battle.entity.EntityECS;

import java.util.Arrays;
import java.util.List;

public class ResetSystem extends SystemECS {
    private static final String SYSTEM_NAME = "ResetSystem";

    public ResetSystem(long id) {
        super(GameConfig.SYSTEM_ID.RESET_SYSTEM, SYSTEM_NAME,id);
    }

    @Override
    public void run(Battle battle) {
        this.tick = this.getElapseTime();
        this.handleResetDamageEffect(battle);
    }

    @Override
    public boolean checkEntityCondition(EntityECS entity, Component component) {
        return false;
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
