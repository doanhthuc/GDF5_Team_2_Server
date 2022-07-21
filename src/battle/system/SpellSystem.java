package battle.system;

import battle.common.EntityMode;
import battle.common.Point;
import battle.common.Utils;
import battle.component.common.PositionComponent;
import battle.component.common.VelocityComponent;
import battle.component.info.LifeComponent;
import battle.component.info.MonsterInfoComponent;
import battle.component.info.SpellInfoComponent;
import battle.config.GameConfig;
import battle.entity.EntityECS;
import battle.manager.EntityManager;

import java.util.ArrayList;
import java.util.List;

public class SpellSystem extends SystemECS {
    public int id = GameConfig.SYSTEM_ID.MONSTER;

    public SpellSystem() {
        super(GameConfig.SYSTEM_ID.LIFE);
        java.lang.System.out.println("new SpellSystem");
    }

    @Override
    public void run() {
        this.tick = this.getEclapseTime();
        List<Integer> spellIds = new ArrayList<>();
        spellIds.add(SpellInfoComponent.typeID);
        List<EntityECS> spellList = EntityManager.getInstance().getEntitiesHasComponents(spellIds);

        for (EntityECS spellEntity : spellList) {
            SpellInfoComponent spellInfoComponent = (SpellInfoComponent) spellEntity.getComponent(SpellInfoComponent.typeID);
            spellInfoComponent.setCountdown(spellInfoComponent.getCountdown() - tick);

            if (spellInfoComponent.getCountdown()<=0) {
                List<Integer>
            }
        }
    }
}
