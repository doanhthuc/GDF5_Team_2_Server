package battle.system;

import battle.Battle;
import battle.common.Point;
import battle.common.Utils;
import battle.component.common.*;
import battle.component.effect.EffectComponent;
import battle.component.effect.FireBallEffect;
import battle.component.info.MonsterInfoComponent;
import battle.component.info.SpellInfoComponent;
import battle.config.GameConfig;
import battle.entity.EntityECS;

import java.util.Map;

public class SpellSystem extends SystemECS {
    private static final String SYSTEM_NAME = "SpellSystem";

    public SpellSystem(long id) {
        super(GameConfig.SYSTEM_ID.SPELL, SYSTEM_NAME, id);
    }

    @Override
    public void run(Battle battle) throws Exception {
        this.tick = this.getElapseTime() / 1000;
        for (Map.Entry<Long, EntityECS> mapElementSpell : this.getEntityStore().entrySet()) {
            EntityECS spellEntity = mapElementSpell.getValue();
            if (!spellEntity._hasComponent(SpellInfoComponent.typeID)) continue;

            SpellInfoComponent spellInfoComponent = (SpellInfoComponent) spellEntity.getComponent(SpellInfoComponent.typeID);

            spellInfoComponent.setDelay(spellInfoComponent.getDelay() - tick);
            spellInfoComponent.setDelayDestroy(spellInfoComponent.getDelayDestroy() - tick);

            if (spellInfoComponent.getDelayDestroy() <= 0) {
                battle.getEntityManager().destroy(spellEntity);
                continue;
            }

            if (spellInfoComponent.getDelay() <= 0 && !spellInfoComponent.isTriggered()) {
                for (Map.Entry<Long, EntityECS> mapElementMonster : this.getEntityStore().entrySet()) {
                    EntityECS monster = mapElementMonster.getValue();

                    if (monster.getId() == spellEntity.getId()) continue;
                    if (!monster._hasComponent(MonsterInfoComponent.typeID)) continue;
                    if (!monster._hasComponent(PositionComponent.typeID)) continue;

                    // The spell can't reach the under ground monsters
                    UnderGroundComponent underGroundComponent = (UnderGroundComponent) monster.getComponent(UnderGroundComponent.typeID);
                    if ((underGroundComponent != null) && underGroundComponent.isInGround()) {
                        continue;
                    }

                    if (monster.getMode() == spellEntity.getMode()) {
                        PositionComponent monsterPosition = (PositionComponent) monster.getComponent(PositionComponent.typeID);
                        if (monsterPosition == null) continue;;

                        double distance = Utils.euclidDistance(monsterPosition, spellInfoComponent.getPosition());
                        if (distance <= spellInfoComponent.getRange()) {
                            for (EffectComponent effect : spellInfoComponent.getEffects()) {
                                monster.addComponent(effect.clone(battle.getComponentFactory()));

                                if (spellEntity.getTypeID() == GameConfig.ENTITY_ID.FIRE_SPELL) {
                                    VelocityComponent oldVelocity = (VelocityComponent) monster.getComponent(VelocityComponent.typeID);
                                    MonsterInfoComponent monsterInfo = (MonsterInfoComponent) monster.getComponent(MonsterInfoComponent.typeID);

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
                                            A, T, new Point(spellPos), new Point(monsterPos.getPos()), v0);

                                    monster.addComponent(fireBallEffect);
                                    PathComponent pathComponent = (PathComponent) monster.getComponent(PathComponent.typeID);
                                    monster.removeComponent(pathComponent);
                                }
                            }
                        }
                    }
                }

                spellInfoComponent.setTriggered(true);

                VelocityComponent velocityComponent = (VelocityComponent) spellEntity.getComponent(VelocityComponent.typeID);
                PositionComponent positionComponent = (PositionComponent) spellEntity.getComponent(PositionComponent.typeID);

                spellEntity.removeComponent(velocityComponent);
                spellEntity.removeComponent(positionComponent);
            }
        }
    }

    @Override
    public boolean checkEntityCondition(EntityECS entity, Component component) {
        return component.getTypeID() == SpellInfoComponent.typeID
                || component.getTypeID() == MonsterInfoComponent.typeID;
    }
}
