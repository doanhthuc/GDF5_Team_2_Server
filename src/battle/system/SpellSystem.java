package battle.system;

import battle.Battle;
import battle.common.Point;
import battle.common.Utils;
import battle.component.common.Component;
import battle.component.common.PathComponent;
import battle.component.common.PositionComponent;
import battle.component.common.VelocityComponent;
import battle.component.effect.EffectComponent;
import battle.component.effect.FireBallEffect;
import battle.component.info.MonsterInfoComponent;
import battle.component.info.SpellInfoComponent;
import battle.config.GameConfig;
import battle.entity.EntityECS;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SpellSystem extends SystemECS {
    private static final String SYSTEM_NAME = "SpellSystem";

    public SpellSystem() {
        super(GameConfig.SYSTEM_ID.SPELL, SYSTEM_NAME);
    }

    @Override
    public void run(Battle battle) throws Exception {
        this.tick = this.getElapseTime();
        List<EntityECS> spellList = battle.getEntityManager().getEntitiesHasComponents
                (Collections.singletonList(SpellInfoComponent.typeID));
        for (EntityECS spellEntity : spellList) {
            SpellInfoComponent spellInfoComponent = (SpellInfoComponent) spellEntity.getComponent(SpellInfoComponent.typeID);
            spellInfoComponent.setCountdown(spellInfoComponent.getCountdown() - (tick / 1000));

            if (spellInfoComponent.getCountdown() <= 0) {
                List<Integer> monsterIds = Arrays.asList(MonsterInfoComponent.typeID);
                List<EntityECS> monsterList = battle.getEntityManager().getEntitiesHasComponents(monsterIds);

                for (EntityECS monster : monsterList) {
                    if (monster.getMode() == spellEntity.getMode()) {
                        PositionComponent monsterPosition =
                                (PositionComponent) monster.getComponent(PositionComponent.typeID);
                        if (monsterPosition == null) {
                            continue;
                        }
                        double distance = Utils.euclidDistance(monsterPosition, spellInfoComponent.getPosition());

                        if (distance <= spellInfoComponent.getRange()) {
                            for (EffectComponent effect : spellInfoComponent.getEffects()) {
                                monster.addComponent(effect.clone(battle.getComponentFactory()));
                                if (spellEntity.getTypeID() == GameConfig.ENTITY_ID.FIRE_SPELL) {
                                    VelocityComponent oldVelocity = (VelocityComponent) spellEntity.getComponent(VelocityComponent.typeID);
                                    MonsterInfoComponent monsterInfo = (MonsterInfoComponent) monster.getComponent(MonsterInfoComponent.typeID);

                                    if (monsterInfo != null)
                                            if (monsterInfo.getClasss().equals(GameConfig.MONSTER.CLASS.AIR)) {
                                        continue;
                                    }
                                    if (oldVelocity == null || monsterInfo == null) {
                                        continue;
                                    }
                                    Point spellPos = spellInfoComponent.getPosition();
                                    PositionComponent monsterPos = monsterPosition;

                                    final int force = 3000;
                                    final double mass = monsterInfo.getWeight();
                                    double A = 40 + (force / mass);
                                    final int T = 1;

                                    final double v0 = Math.abs(A * T);

                                    Point newVectorVelocity = Utils.calculateVelocityVector(
                                            spellPos, monsterPos.getPos(), v0);

                                    oldVelocity.setSpeedX(newVectorVelocity.getX());
                                    oldVelocity.setSpeedY(newVectorVelocity.getY());

                                    FireBallEffect fireBallEffect = battle.getComponentFactory().createFireBallEffect(
                                            A, T, spellPos, monsterPos.getPos(), v0);
                                    monster.addComponent(fireBallEffect);
                                    PathComponent pathComponent = (PathComponent) monster.getComponent(PathComponent.typeID);
                                    monster.removeComponent(pathComponent, battle.getComponentManager());
                                }
                            }
                        }
                    }
                }
                battle.getEntityManager().remove(spellEntity);
            }
        }
    }

    @Override
    public boolean checkEntityCondition(EntityECS entity, Component component) {
        return component.getTypeID() == SpellInfoComponent.typeID
                || component.getTypeID() == MonsterInfoComponent.typeID;
    }
}
