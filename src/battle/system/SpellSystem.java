package battle.system;

import battle.Battle;
import battle.common.EntityMode;
import battle.common.Point;
import battle.common.Utils;
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

public class SpellSystem extends SystemECS {
    public int id = GameConfig.SYSTEM_ID.SPELL;

    public SpellSystem() {
        super(GameConfig.SYSTEM_ID.SPELL);
        java.lang.System.out.println("new SpellSystem");
    }

    @Override
    public void run(Battle battle) throws Exception {
        this.tick = this.getElapseTime();
        List<EntityECS> spellList = battle.getEntityManager().getEntitiesHasComponents
                (Collections.singletonList(SpellInfoComponent.typeID));
        for (EntityECS spellEntity : spellList) {
            SpellInfoComponent spellInfoComponent = (SpellInfoComponent) spellEntity.getComponent(SpellInfoComponent.typeID);
            spellInfoComponent.setCountdown(spellInfoComponent.getCountdown() - tick / 1000);
            System.out.println(spellInfoComponent.getCountdown());
            if (spellInfoComponent.getCountdown() <= 0) {
                List<Integer> monsterIds = Arrays.asList(MonsterInfoComponent.typeID);
                List<EntityECS> monsterList = battle.getEntityManager().getEntitiesHasComponents(monsterIds);

                for (EntityECS monster : monsterList) {
                    if (monster.getMode() == spellEntity.getMode()) {

                        PositionComponent monsterPos = (PositionComponent) monster.getComponent(PositionComponent.typeID);
                        double distance = Utils.euclidDistance(monsterPos, spellInfoComponent.getPosition());

                        if (distance <= spellInfoComponent.getRange()) {
                            for (EffectComponent effect : spellInfoComponent.getEffects()) {
                                monster.addComponent(effect.clone(battle.getComponentFactory()));
                            }
                        }
                    }
                }
                battle.getEntityManager().remove(spellEntity);
            }
        }
    }
}
