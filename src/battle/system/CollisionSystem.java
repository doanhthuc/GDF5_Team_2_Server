package battle.system;

import battle.Battle;
import battle.common.*;
import battle.component.common.CollisionComponent;
import battle.component.common.PathComponent;
import battle.component.common.PositionComponent;
import battle.common.QuadTreeData;
import battle.component.common.VelocityComponent;
import battle.component.effect.DamageEffect;
import battle.component.effect.EffectComponent;
import battle.component.info.BulletInfoComponent;
import battle.component.info.MonsterInfoComponent;
import battle.component.info.TrapInfoComponent;
import battle.config.GameConfig;
import battle.entity.EntityECS;
import battle.factory.ComponentFactory;
import battle.manager.EntityManager;

import java.util.*;

public class CollisionSystem extends SystemECS {
    public static int typeID = GameConfig.SYSTEM_ID.COLLISION;
    private String name = "CollisionSystem";
    private final double mapWidth = GameConfig.MAP_WIDTH * GameConfig.TILE_WIDTH;
    private final double mapHeight = GameConfig.MAP_HEIGHT * GameConfig.TILE_HEIGHT;
    private QuadTree quadTreePlayer = new QuadTree(0, new Rect(-mapWidth / 2, -mapHeight / 2, mapWidth, mapHeight));
    private QuadTree quadTreeOpponent = new QuadTree(0, new Rect(-mapWidth / 2, -mapHeight / 2, mapWidth, mapHeight));

    public CollisionSystem() {
        super(GameConfig.SYSTEM_ID.COLLISION);
        java.lang.System.out.println(this.name);
    }

    @Override
    public void run(Battle battle) throws Exception {
        this.tick = this.getElapseTime();
        List<Integer> typeIDs = Arrays.asList(CollisionComponent.typeID, PositionComponent.typeID);
        List<EntityECS> entityList = battle.getEntityManager().getEntitiesHasComponents(typeIDs);

        // construct quadtree
        quadTreePlayer.clear();
        quadTreeOpponent.clear();
        for (int i = 0; i < entityList.size() - 1; i++) {
            PositionComponent pos = (PositionComponent) entityList.get(i).getComponent(GameConfig.COMPONENT_ID.POSITION);
            CollisionComponent collision = (CollisionComponent) entityList.get(i).getComponent(GameConfig.COMPONENT_ID.COLLISION);
            double w = collision.getWidth(), h = collision.getHeight();

            Rect rect = new Rect(pos.getX() - w / 2, pos.getY() - h / 2, w, h);
            if (entityList.get(i).getMode() == EntityMode.PLAYER) {
                quadTreePlayer.insert(new QuadTreeData(rect, entityList.get(i)));
            } else {
                quadTreeOpponent.insert(new QuadTreeData(rect, entityList.get(i)));
            }
        }

        for (int i = 0; i < entityList.size(); i++) {
            if (ValidatorECS.isEntityInGroupId(entityList.get(i), GameConfig.GROUP_ID.BULLET_ENTITY)) {
                EntityECS bullet = entityList.get(i);
                BulletInfoComponent bulletInfo = (BulletInfoComponent) bullet.getComponent(BulletInfoComponent.typeID);
                if (bulletInfo.getRadius() > 0) {
                    this.handleRadiusBullet(bullet, battle);
                } else {
                    this.handleCollisionBullet(bullet, battle);
                }
            } else if (ValidatorECS.isEntityIdEqualTypeId(entityList.get(i), GameConfig.ENTITY_ID.TRAP_SPELL)) {
                try {
                    this.handleCollisionTrap(entityList.get(i), this.tick, battle);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
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
            if (entity1 != entity2 && entity1.getMode() == entity2.getMode() && this.isCollide(entity1, entity2)) {
                MonsterAndBullet data = this.isMonsterAndBullet(entity1, entity2);
                if (data != null) {
                    EntityECS monster = data.getMonster();
                    EntityECS bullet = data.getBullet();
                    BulletInfoComponent bulletInfo = (BulletInfoComponent) bullet.getComponent(GameConfig.COMPONENT_ID.BULLET_INFO);
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
                                        newDamageEffect.setDamage(newDamageEffect.getDamage() * 1.5);
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
        if ((Math.abs(staticPosition.getX() - bulletPos.getX()) <= 5) && (Math.abs(staticPosition.getY() - bulletPos.getY()) <= 5)) {
            List<EntityECS> monsterList = battle.getEntityManager().getEntitiesHasComponents(Arrays.asList(MonsterInfoComponent.typeID, PositionComponent.typeID));
            for (EntityECS monster : monsterList) {
                if (monster.getMode() == bulletEntity.getMode()) {
                    MonsterInfoComponent monsterInfo = (MonsterInfoComponent) monster.getComponent(MonsterInfoComponent.typeID);
                    if (Objects.equals(monsterInfo.getClasss(), GameConfig.MONSTER.CLASS.AIR)) {
                        continue;
                    }
                    if (Utils.euclidDistance((PositionComponent) monster.getComponent(PositionComponent.typeID), bulletPos) <= bulletInfo.getRadius()) {
                        for (EffectComponent effect : bulletInfo.getEffects())
                            monster.addComponent(effect.clone(battle.getComponentFactory()));
                    }
                }
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
                        if (monsterInfo.getClasss().equals(GameConfig.MONSTER.CLASS.AIR)) continue;
                        if (monsterInfo.getCategory().equals(GameConfig.MONSTER.CATEGORY.BOSS)) continue;
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
                        && this.isCollide(entity1, entity2)) {
                    MonsterInfoComponent monsterInfo = (MonsterInfoComponent) entity2.getComponent(MonsterInfoComponent.typeID);
                    if (monsterInfo.getClasss().equals(GameConfig.MONSTER.CLASS.AIR)) continue;
                    if (monsterInfo.getCategory().equals(GameConfig.MONSTER.CATEGORY.BOSS)) continue;

                    trapInfoComponent.setTriggered(true);

                    // only the first monster triggers this trap
                    break;
                }
            }
        }
    }

    private boolean isCollide(EntityECS entity1, EntityECS entity2) {
        PositionComponent pos1 = (PositionComponent) entity1.getComponent(GameConfig.COMPONENT_ID.POSITION);
        PositionComponent pos2 = (PositionComponent) entity2.getComponent(GameConfig.COMPONENT_ID.POSITION);
        CollisionComponent collision1 = (CollisionComponent) entity1.getComponent(GameConfig.COMPONENT_ID.COLLISION);
        CollisionComponent collision2 = (CollisionComponent) entity2.getComponent(GameConfig.COMPONENT_ID.COLLISION);
        double w1 = collision1.getWidth(), h1 = collision1.getHeight();
        double w2 = collision2.getWidth(), h2 = collision2.getHeight();
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
