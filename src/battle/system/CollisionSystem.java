package battle.system;

import battle.Battle;
import battle.common.*;
import battle.component.common.*;
import battle.common.QuadTreeData;
import battle.component.effect.DamageEffect;
import battle.component.effect.EffectComponent;
import battle.component.info.BulletInfoComponent;
import battle.component.info.MonsterInfoComponent;
import battle.component.info.TrapInfoComponent;
import battle.component.towerskill.FrogBulletSkillComponent;
import battle.component.towerskill.WizardBulletSkillComponent;
import battle.config.GameConfig;
import battle.entity.EntityECS;
import battle.factory.ComponentFactory;
import battle.manager.EntityManager;

import java.util.*;

public class CollisionSystem extends SystemECS {
    private static final String SYSTEM_NAME = "CollisionSystem";
    private final double mapWidth = GameConfig.MAP_WIDTH * GameConfig.TILE_WIDTH;
    private final double mapHeight = GameConfig.MAP_HEIGHT * GameConfig.TILE_HEIGHT;
    private QuadTree quadTreePlayer = new QuadTree(0, new Rect(-mapWidth / 2, -mapHeight / 2, mapWidth, mapHeight));
    private QuadTree quadTreeOpponent = new QuadTree(0, new Rect(-mapWidth / 2, -mapHeight / 2, mapWidth, mapHeight));

    public CollisionSystem(long id) {
        super(GameConfig.SYSTEM_ID.COLLISION, SYSTEM_NAME, id);
    }

    @Override
    public void run(Battle battle) throws Exception {
        this.tick = this.getElapseTime();


        // construct quadtree
        quadTreePlayer.clear();
        quadTreeOpponent.clear();
        for (Map.Entry<Long, EntityECS> mapElement : this.getEntityStore().entrySet()) {
            EntityECS collideEntity = mapElement.getValue();
            PositionComponent pos = (PositionComponent) collideEntity.getComponent(GameConfig.COMPONENT_ID.POSITION);
            CollisionComponent collision = (CollisionComponent) collideEntity.getComponent(GameConfig.COMPONENT_ID.COLLISION);
            double w = collision.getWidth(), h = collision.getHeight();

            Rect rect = new Rect(pos.getX() - w / 2, pos.getY() - h / 2, w, h);
            if (collideEntity.getMode() == EntityMode.PLAYER) {
                quadTreePlayer.insert(new QuadTreeData(rect, collideEntity));
            } else {
                quadTreeOpponent.insert(new QuadTreeData(rect, collideEntity));
            }
        }

        for (Map.Entry<Long, EntityECS> mapElement : this.getEntityStore().entrySet()) {
            EntityECS collisionEntity = mapElement.getValue();
            if (!collisionEntity._hasComponent(BulletInfoComponent.typeID)) continue;
            if (ValidatorECS.isEntityInGroupId(collisionEntity, GameConfig.GROUP_ID.BULLET_ENTITY)) {
                EntityECS bullet = collisionEntity;
                BulletInfoComponent bulletInfo = (BulletInfoComponent) bullet.getComponent(BulletInfoComponent.typeID);
                if (bulletInfo.getRadius() > 0) {
                    this.handleRadiusBullet(bullet, battle);
                } else {
                    this.handleCollisionBullet(bullet, battle);
                }
            } else if (ValidatorECS.isEntityIdEqualTypeId(collisionEntity, GameConfig.ENTITY_ID.TRAP_SPELL)) {
                try {
                    this.handleCollisionTrap(collisionEntity, this.tick, battle);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean checkEntityCondition(EntityECS entity, Component component) {
        return component.getTypeID() == CollisionComponent.typeID;
    }

    private void handleCollisionBullet(EntityECS bulletEntity, Battle battle) throws Exception {
        PositionComponent pos = (PositionComponent) bulletEntity.getComponent(GameConfig.COMPONENT_ID.POSITION);
        CollisionComponent collision = (CollisionComponent) bulletEntity.getComponent(GameConfig.COMPONENT_ID.COLLISION);
        double w = collision.getWidth(), h = collision.getHeight();

        List<QuadTreeData> returnObjects = null;
        if (bulletEntity.getMode() == EntityMode.PLAYER) {
            returnObjects = quadTreePlayer.retrieve(new Rect(pos.getX() - w / 2, pos.getY() - h / 2, w, h));
        } else {
            returnObjects = quadTreeOpponent.retrieve(new Rect(pos.getX() - w / 2, pos.getY() - h / 2, w, h));
        }

        for (int i = 0; i < returnObjects.size(); i++) {
            EntityECS entity1 = bulletEntity, entity2 = returnObjects.get(i).getEntity();
            if (entity1 != entity2
                    && entity1.getMode() == entity2.getMode()
                    && entity1.getActive() && entity2.getActive()
                    && this.isCollide(entity1, entity2)
             ) {
                MonsterAndBullet data = this.isMonsterAndBullet(entity1, entity2);
                if (data != null) {
                    EntityECS monster = data.getMonster();
                    EntityECS bullet = data.getBullet();
                    BulletInfoComponent bulletInfo = (BulletInfoComponent) bullet.getComponent(GameConfig.COMPONENT_ID.BULLET_INFO);

                    UnderGroundComponent underGroundComponent = (UnderGroundComponent) monster.getComponent(UnderGroundComponent.typeID);
                    if (underGroundComponent != null && underGroundComponent.isInGround()) {
                        continue;
                    }
                    // FIXME: Define bulletInfo type for Frog Tower
                    if (bulletInfo.getType() == "frog") {
                        Map<Long, Integer> hitMonster;
                        PathComponent pathComponent = (PathComponent) bullet.getComponent(PathComponent.typeID);
                        // check the bullet is in the first Path
                        hitMonster = bulletInfo.getHitMonster();
                        if (pathComponent.getCurrentPathIDx() <= pathComponent.getPath().size() / 2) {
                            if (!hitMonster.containsKey(monster.getId())) {
                                for (EffectComponent effectComponent : bulletInfo.getEffects()) {
                                    monster.addComponent(effectComponent.clone(battle.getComponentFactory()));
                                    bulletInfo.getHitMonster().put(monster.getId(), GameConfig.FROG_BULLET.HIT_FIRST_TIME);
                                }
                            }
                        } // check the bullet is in the second Path
                        else {
                            //check the monster was not hit in the first Path
                            hitMonster = bulletInfo.getHitMonster();
                            if (!hitMonster.containsKey(monster.getId())) {
                                for (EffectComponent effectComponent : bulletInfo.getEffects()) {
                                    monster.addComponent(effectComponent.clone(battle.getComponentFactory()));
                                    bulletInfo.getHitMonster().put(monster.getId(), GameConfig.FROG_BULLET.HIT_SECOND_TIME);
                                }
                            } else if (hitMonster.get(monster.getId()) == GameConfig.FROG_BULLET.HIT_FIRST_TIME) {
                                for (EffectComponent effect : bulletInfo.getEffects()) {
                                    if (effect.getTypeID() == DamageEffect.typeID) {
                                        DamageEffect newDamageEffect = (DamageEffect) effect.clone(battle.getComponentFactory());

                                        for (EffectComponent bulletEffect : bulletInfo.getEffects()) {
                                            if (bulletEffect.getTypeID() == FrogBulletSkillComponent.typeID) {
                                                FrogBulletSkillComponent frogBulletEffect = (FrogBulletSkillComponent) bulletEffect;
                                                newDamageEffect.setDamage(newDamageEffect.getDamage() * frogBulletEffect.getIncreaseDamage());
                                            }
                                        }
                                        monster.addComponent(newDamageEffect);
                                        bulletInfo.setHitMonster(monster.getId(), GameConfig.FROG_BULLET.HIT_BOTH_TIME);
                                    }
                                }
                            }
                        }
                    } else {
                        for (EffectComponent effect : bulletInfo.getEffects()) {
                            monster.addComponent(effect.clone(battle.getComponentFactory()));
                        }
                        battle.getEntityManager().destroy(bullet);
                    }
                    break;
                }
            }
        }
    }

    private void handleRadiusBullet(EntityECS bulletEntity, Battle battle) throws Exception {
        PositionComponent bulletPos = (PositionComponent) bulletEntity.getComponent(PositionComponent.typeID);
        VelocityComponent bulletVelocity = (VelocityComponent) bulletEntity.getComponent(VelocityComponent.typeID);
        BulletInfoComponent bulletInfo = (BulletInfoComponent) bulletEntity.getComponent(BulletInfoComponent.typeID);
        Point staticPosition = bulletVelocity.getStaticPosition();
        SystemECS abilitySystem = battle.abilitySystem;
        if ((Math.abs(staticPosition.getX() - bulletPos.getX()) <= 10) && (Math.abs(staticPosition.getY() - bulletPos.getY()) <= 10)) {
            List<EntityECS> monsterInRadius = new ArrayList<>();
            //TODO : fixMonsterList

            for (Map.Entry<Long, EntityECS> mapElement : abilitySystem.getEntityStore().entrySet()) {
                EntityECS monster = mapElement.getValue();
                if (!monster._hasComponent(PositionComponent.typeID)) continue;
                if (monster.getMode() == bulletEntity.getMode()) {
//                    MonsterInfoComponent monsterInfo = (MonsterInfoComponent) monster.getComponent(MonsterInfoComponent.typeID);
//                    if (Objects.equals(monsterInfo.getClasss(), GameConfig.MONSTER.CLASS.AIR)) {
//                        continue;
//                    }
                    if (Utils.euclidDistance((PositionComponent) monster.getComponent(PositionComponent.typeID), bulletPos) <= bulletInfo.getRadius()) {
                        monsterInRadius.add(monster);
                    }
                }
            }
            //
            for (EffectComponent effect : bulletInfo.getEffects()) {
                if (effect.getTypeID() == WizardBulletSkillComponent.typeID) {
                    WizardBulletSkillComponent wizardEffect = (WizardBulletSkillComponent) effect;
                    if (monsterInRadius.size() >= wizardEffect.getAmountMonster()) {
                        for (EffectComponent effect2 : bulletInfo.getEffects()) {
                            if (effect2.getTypeID() == DamageEffect.typeID) {
                                DamageEffect damageEffect = (DamageEffect) effect2;
                                damageEffect.setDamage(damageEffect.getDamage() + wizardEffect.getIncreaseDamage());
                            }
                        }
                    }
                }
            }

            for (EntityECS monster : monsterInRadius) {
                for (EffectComponent effectComponent : bulletInfo.getEffects())
                    monster.addComponent(effectComponent.clone(battle.getComponentFactory()));
            }
            battle.getEntityManager().destroy(bulletEntity);
        }
    }

    public void handleCollisionTrap(EntityECS trapEntity, double tick, Battle battle) throws Exception {
        TrapInfoComponent trapInfoComponent = (TrapInfoComponent) trapEntity.getComponent(TrapInfoComponent.typeID);
        if (trapInfoComponent.isTriggered()) {
            double delayTrigger = trapInfoComponent.getDelayTrigger();
            if (delayTrigger > 0) {
                trapInfoComponent.setDelayTrigger(delayTrigger - tick / 1000);
            } else {
                PositionComponent positionComponent = (PositionComponent) trapEntity.getComponent(PositionComponent.typeID);
                CollisionComponent collisionComponent = (CollisionComponent) trapEntity.getComponent(CollisionComponent.typeID);
                double w = collisionComponent.getWidth();
                double h = collisionComponent.getHeight();

                List<QuadTreeData> returnObjects = null;
                if (trapEntity.getMode() == EntityMode.PLAYER) {
                    returnObjects = quadTreePlayer.retrieve(new Rect(positionComponent.getX() - w / 2, positionComponent.getY() - h / 2, w, h));
                } else {
                    returnObjects = quadTreeOpponent.retrieve(new Rect(positionComponent.getX() - w / 2, positionComponent.getY() - h / 2, w, h));
                }

                for (int j = 0; j < returnObjects.size(); j++) {
                    EntityECS entity1 = trapEntity;
                    EntityECS entity2 = returnObjects.get(j).getEntity();

                    if ((entity1 != entity2)
                            && (entity1.getMode() == entity2.getMode())
                            && ValidatorECS.isEntityInGroupId(entity2, GameConfig.GROUP_ID.MONSTER_ENTITY)
                            && this.isCollide(entity1, entity2)) {

                        MonsterInfoComponent monsterInfo = (MonsterInfoComponent) entity2.getComponent(MonsterInfoComponent.typeID);
                        UnderGroundComponent underGroundComponent = (UnderGroundComponent) entity2.getComponent(UnderGroundComponent.typeID);
                        if (monsterInfo.getClasss().equals(GameConfig.MONSTER.CLASS.AIR)) continue;
                        if (monsterInfo.getCategory().equals(GameConfig.MONSTER.CATEGORY.BOSS)) continue;
                        if (underGroundComponent != null)
                            if (underGroundComponent.isInGround()) continue;
                        entity2.addComponent(battle.getComponentFactory().createTrapEffect());
                    }
                }
                battle.getEntityManager().destroy(trapEntity);
            }
        } else {
            PositionComponent pos = (PositionComponent) trapEntity.getComponent(PositionComponent.typeID);
            CollisionComponent collisionComponent = (CollisionComponent) trapEntity.getComponent(CollisionComponent.typeID);
            double w = collisionComponent.getWidth();
            double h = collisionComponent.getHeight();

            List<QuadTreeData> returnObjects = null;
            if (trapEntity.getMode() == EntityMode.PLAYER) {
                returnObjects = quadTreePlayer.retrieve(new Rect(pos.getX() - w / 2, pos.getY() - h / 2, w, h));
            } else {
                returnObjects = quadTreeOpponent.retrieve(new Rect(pos.getX() - w / 2, pos.getY() - h / 2, w, h));
            }

            for (int j = 0; j < returnObjects.size(); j++) {
                EntityECS entity1 = trapEntity;
                EntityECS entity2 = returnObjects.get(j).getEntity();

                if ((entity1 != entity2)
                        && (entity1.getMode() == entity2.getMode())
                        && ValidatorECS.isEntityInGroupId(entity2, GameConfig.GROUP_ID.MONSTER_ENTITY)
                        && (entity1.getActive() && entity2.getActive())
                        && this.isCollide(entity1, entity2)) {

                    MonsterInfoComponent monsterInfo = (MonsterInfoComponent) entity2.getComponent(MonsterInfoComponent.typeID);
                    UnderGroundComponent underGroundComponent = (UnderGroundComponent) entity2.getComponent(UnderGroundComponent.typeID);
                    if (monsterInfo.getClasss().equals(GameConfig.MONSTER.CLASS.AIR)) continue;
                    if (underGroundComponent != null)
                        if (underGroundComponent.isInGround()) continue;
                    trapInfoComponent.setTriggered(true);
                    // only the first monster triggers this trap
                    break;
                }
            }
        }
    }

    private boolean isCollide(EntityECS entity1, EntityECS entity2) {
        PositionComponent pos1 = (PositionComponent) entity1.getComponent(PositionComponent.typeID);
        PositionComponent pos2 = (PositionComponent) entity2.getComponent(PositionComponent.typeID);
        CollisionComponent collision1 = (CollisionComponent) entity1.getComponent(CollisionComponent.typeID);
        CollisionComponent collision2 = (CollisionComponent) entity2.getComponent(CollisionComponent.typeID);
        double w1 = collision1.getWidth();
        double h1 = collision1.getHeight();
        double w2 = collision2.getWidth();
        double h2 = collision2.getHeight();
        if ((w1 == 0 && h1 == 0) || (w2 == 0) && (h2 == 0)) return false;

        return this.rectIntersectsRect(
                new Rect(pos1.getX() - w1 / 2, pos1.getY() - h1 / 2, w1, h1),
                new Rect(pos2.getX() - w2 / 2, pos2.getY() - h2 / 2, w2, h2));
    }


    public boolean rectIntersectsRect(Rect rectA, Rect rectB) {
        return !(rectA.x > rectB.x + rectB.width ||
                rectA.x + rectA.width < rectB.x ||
                rectA.y > rectB.y + rectB.height ||
                rectA.y + rectA.height < rectB.y);
    }

    private MonsterAndBullet isMonsterAndBullet(EntityECS entity1, EntityECS entity2) {
        MonsterAndBullet res = null;
        if (ValidatorECS.isEntityInGroupId(entity1, GameConfig.GROUP_ID.BULLET_ENTITY)
                && ValidatorECS.isEntityInGroupId(entity2, GameConfig.GROUP_ID.MONSTER_ENTITY)
        ) {
            res = new MonsterAndBullet(entity2, entity1);
        } else if (ValidatorECS.isEntityInGroupId(entity1, GameConfig.GROUP_ID.MONSTER_ENTITY)
                && ValidatorECS.isEntityInGroupId(entity2, GameConfig.GROUP_ID.BULLET_ENTITY)
        ) {
            res = new MonsterAndBullet(entity1, entity2);
        }
        return res;
    }

    static class MonsterAndBullet {
        EntityECS monster;
        EntityECS bullet;

        public MonsterAndBullet(EntityECS monster, EntityECS bullet) {
            this.monster = monster;
            this.bullet = bullet;
        }

        public EntityECS getMonster() {
            return monster;
        }

        public EntityECS getBullet() {
            return bullet;
        }
    }
}
