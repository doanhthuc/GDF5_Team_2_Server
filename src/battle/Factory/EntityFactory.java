package battle.factory;

<<<<<<< HEAD:src/battle/Factory/EntityFactory.java
import battle.Common.EntityMode;
import battle.Common.Point;
import battle.Common.Utils;
import battle.Component.Component.*;
import battle.Component.EffectComponent.EffectComponent;
import battle.Component.InfoComponent.BulletInfoComponent;
import battle.Component.InfoComponent.LifeComponent;
import battle.Component.InfoComponent.MonsterInfoComponent;
import battle.Component.InfoComponent.TowerInfoComponent;
import battle.Config.GameConfig;
import battle.Entity.EntityECS;
import battle.Manager.EntityManager;
import battle.Pool.EntityPool;
=======
import battle.common.Point;
import battle.common.Utils;
import battle.component.Component.*;
import battle.component.EffectComponent.EffectComponent;
import battle.component.InfoComponent.*;
import battle.config.GameConfig;
import battle.entity.EntityECS;
import battle.manager.EntityManager;
import battle.pool.EntityPool;
>>>>>>> master:src/battle/factory/EntityFactory.java

import java.util.ArrayList;
import java.util.List;

public class EntityFactory {
    static EntityFactory _instance;
    public EntityPool pool = new EntityPool();

    public static EntityFactory getInstance() {
        if (_instance == null) _instance = new EntityFactory();
        return _instance;
    }

    public EntityECS _createEntity(int typeID) {
        EntityECS entity = null;
        if (entity == null) {
            entity = new EntityECS(typeID);
            this.pool.push(entity);
            EntityManager.getInstance().addEntity(entity);
        }
        return entity;
    }

    public EntityECS createBullet(int towerType, Point startPosition, Point targetPosition, List<EffectComponent> effects, EntityMode mode) throws Exception {
        int typeID;
        EntityECS entity;
        BulletInfoComponent infoComponent;
        PositionComponent positionComponent = ComponentFactory.getInstance().createPositionComponent((int) startPosition.x, (int) startPosition.y);
        CollisionComponent collisionComponent;
        double bulletSpeed;
        switch (towerType) {
            case GameConfig.ENTITY_ID.CANNON_TOWER:
                typeID = GameConfig.ENTITY_ID.BULLET;
                entity = this._createEntity(typeID);
                infoComponent = ComponentFactory.getInstance().createBulletInfoComponent(effects, 1);
                collisionComponent = ComponentFactory.getInstance().createCollisionComponent(0, 0);

                bulletSpeed = 5 * GameConfig.TILE_WIDTH;
                Point speed = Utils.getInstance().calculateVelocityVector(startPosition, targetPosition, bulletSpeed);
                VelocityComponent velocityComponent = ComponentFactory.getInstance().createVelocityComponent(speed.x, speed.y, targetPosition);

                entity.addComponent(infoComponent);
                entity.addComponent(positionComponent);
                entity.addComponent(velocityComponent);
                entity.addComponent(collisionComponent);
                return entity;

            case GameConfig.ENTITY_ID.BEAR_TOWER:
                typeID = GameConfig.ENTITY_ID.BULLET;
                entity = this._createEntity(typeID);
                infoComponent = ComponentFactory.getInstance().createBulletInfoComponent(effects, 2);
                collisionComponent = ComponentFactory.getInstance().createCollisionComponent(1, 1);
                bulletSpeed = 4 * GameConfig.TILE_WIDTH;
                speed = Utils.getInstance().calculateVelocityVector(startPosition, targetPosition, bulletSpeed);
                velocityComponent = ComponentFactory.getInstance().createVelocityComponent(speed.x, speed.y, targetPosition);

                entity.addComponent(infoComponent);
                entity.addComponent(positionComponent);
                entity.addComponent(velocityComponent);
                entity.addComponent(collisionComponent);
                return entity;
            case GameConfig.ENTITY_ID.FROG_TOWER:
                typeID = GameConfig.ENTITY_ID.BULLET;
                entity = this._createEntity(typeID);

                infoComponent = ComponentFactory.getInstance().createBulletInfoComponent(effects, 3);
                collisionComponent = ComponentFactory.getInstance().createCollisionComponent(20, 20);

                ArrayList<Point> path = new ArrayList<>();
                path.add(startPosition);
                path.add(targetPosition);
                path.add(startPosition);
                PathComponent pathComponent = ComponentFactory.getInstance().createPathComponent(path);
                bulletSpeed = 4 * GameConfig.TILE_WIDTH;
                speed = Utils.getInstance().calculateVelocityVector(startPosition, targetPosition, bulletSpeed);
                velocityComponent = ComponentFactory.getInstance().createVelocityComponent(speed.x, speed.y, targetPosition);

                entity.addComponent(infoComponent);
                entity.addComponent(positionComponent);
                entity.addComponent(velocityComponent);
                entity.addComponent(collisionComponent);
                entity.addComponent(pathComponent);
        }
        return null;
    }

    public EntityECS createSwordManMonster(Point pixelPos, String mode) throws Exception {
        int typeID = GameConfig.ENTITY_ID.SWORD_MAN;
        EntityECS entity = new EntityECS(typeID, mode);
        this.pool.push(entity);
        EntityManager.getInstance().addEntity(entity);

        MonsterInfoComponent monsterInfoComponent = ComponentFactory.getInstance().createMonsterInfoComponent("normal", "land", 30, 1, 1, 0, null);
        PositionComponent positionComponent = ComponentFactory.getInstance().createPositionComponent((int) pixelPos.x, (int) pixelPos.y);
        VelocityComponent velocityComponent = ComponentFactory.getInstance().createVelocityComponent(0.8 * GameConfig.TILE_WIDTH, (double) 0, null);
        CollisionComponent collisionComponent = ComponentFactory.getInstance().createCollisionComponent(20, 30);
        LifeComponent lifeComponent = ComponentFactory.getInstance().createLifeComponent(50);

        // FrozenEffect frozenEffect= ComponentFactory.getInstance().createFrozenEffect();
        Point tilePos = Utils.getInstance().pixel2Tile(pixelPos.x, pixelPos.y, mode);
        ArrayList<Point> path = new ArrayList<Point>();
        path.add(new Point(0, 6));
        path.add(new Point(0, 5));
        //ToDo: find shortest Path with TilePos
        PathComponent pathComponent = ComponentFactory.getInstance().createPathComponent(path);

        entity.addComponent(monsterInfoComponent);
        entity.addComponent(positionComponent);
        entity.addComponent(velocityComponent);
        entity.addComponent(collisionComponent);
        entity.addComponent(lifeComponent);
        entity.addComponent(pathComponent);
        return entity;
    }

    public EntityECS createCannonOwlTower(Point tilePos) throws Exception {
        int typeID = GameConfig.ENTITY_ID.CANNON_TOWER;
        EntityECS entity = this._createEntity(typeID);

        double attackRange = 1.5 * GameConfig.TILE_WIDTH;
        Point pixelPos = Utils.tile2Pixel(tilePos.x, tilePos.y);

        TowerInfoComponent towerInfoComponent = ComponentFactory.getInstance().createTowerInfoComponent(10, "bulletTargetType", "attack", "monster", "bulletType");
        PositionComponent positionComponent = ComponentFactory.getInstance().createPositionComponent((int) pixelPos.x, (int) pixelPos.y);
        AttackComponent attackComponent = ComponentFactory.getInstance().createAttackComponent(10, GameConfig.TOWER_TARGET_STRATEGY.MAX_HP, attackRange, 0.6, 0, null);

        entity.addComponent(towerInfoComponent);
        entity.addComponent(positionComponent);
        entity.addComponent(attackComponent);
        return entity;
    }
}

