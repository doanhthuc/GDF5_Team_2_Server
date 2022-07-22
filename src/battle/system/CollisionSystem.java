package battle.system;

import battle.common.*;
import battle.component.common.CollisionComponent;
import battle.component.common.PositionComponent;
import battle.common.QuadTreeData;
import battle.component.effect.EffectComponent;
import battle.component.info.BulletInfoComponent;
import battle.config.GameConfig;
import battle.entity.EntityECS;
import battle.manager.EntityManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CollisionSystem extends SystemECS implements Runnable {
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
    public void run() {
        this.tick = this.getElapseTime();
        List<Integer> typeIDs = new ArrayList<>();
        typeIDs.add(GameConfig.COMPONENT_ID.COLLISION);
        List<EntityECS> entityList = EntityManager.getInstance().getEntitiesHasComponents(typeIDs);

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
                this.hanleCollisionBullet(entityList.get(i));
            } else if (ValidatorECS.isEntityIdEqualTypeId(entityList.get(i), GameConfig.ENTITY_ID.TRAP_SPELL)) {
                this.handleCollisionTrap(entityList.get(i), this.tick);
            }
        }
    }

    private void hanleCollisionBullet(EntityECS bulletEntity) {
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
                    if (bulletInfo.getType() == 1000) {

                    } else {
                        System.out.println("Collision line 87");
                        if (bulletInfo.getRadius() > 0) {
                            List<EntityECS> monsterList = EntityManager.getInstance()
                                    .getEntitiesHasComponents(
                                            Collections.singletonList(GameConfig.COMPONENT_ID.MONSTER_INFO));
                            for (EntityECS monsterEntity : monsterList) {
                                PositionComponent monsterPos = (PositionComponent) monsterEntity.getComponent(GameConfig.COMPONENT_ID.POSITION);
                                if (Utils.euclidDistance(monsterPos, pos) <= bulletInfo.getRadius()) {
                                    for (EffectComponent effect : bulletInfo.getEffects()) {
                                        monsterEntity.addComponent(effect.clone());
                                    }
                                }
                            }

                        } else {
                            for (EffectComponent effect : bulletInfo.getEffects()) {
                                monster.addComponent(effect.clone());
                            }
                        }
                        EntityManager.destroy(bullet);
                        break;
                    }
                }
            }
        }
    }

    public void handleCollisionTrap(EntityECS trapEntity, double tick) {

    }

    private boolean isCollide(EntityECS entity1, EntityECS entity2) {
        PositionComponent pos1 = (PositionComponent) entity1.getComponent(GameConfig.COMPONENT_ID.POSITION);
        PositionComponent pos2 = (PositionComponent) entity2.getComponent(GameConfig.COMPONENT_ID.POSITION);
        CollisionComponent collision1 = (CollisionComponent) entity1.getComponent(GameConfig.COMPONENT_ID.COLLISION);
        CollisionComponent collision2 = (CollisionComponent) entity2.getComponent(GameConfig.COMPONENT_ID.COLLISION);
        double w1 = collision1.getWidth(), h1 = collision1.getHeight();
        double w2 = collision2.getWidth(), h2 = collision2.getHeight();
        if ((w1 == 0 && h1 == 0) || (w2 == 0) && (h2 == 0)) return false;

//        return this._interSectRect(pos1.getX() - w1 / 2, pos1.getY() - h1 / 2, w1, h1, pos2.getX() - w2 / 2, pos2.getY() - h2 / 2, w2, h2);
        return this.rectIntersectsRect(
                new Rect(pos1.getX() - w1 / 2, pos1.getY() - h1 / 2, w1, h1),
                new Rect(pos2.getX() - w2 / 2, pos2.getY() - h2 / 2, w2, h2));
    }

    public boolean _interSectRect(double x1, double y1, double w1, double h1, double x2, double y2, double w2, double h2) {
        return ((x1 + w1 >= x2) && (x1 <= x2 + w2)) && (y1 + h1 >= y2) && (y1 <= y2 + h2);
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
