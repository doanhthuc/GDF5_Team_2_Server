package battle.factory;

import battle.Battle;
import battle.common.EntityMode;
import battle.common.Point;
import battle.common.UUIDGeneratorECS;
import battle.common.Utils;
import battle.component.common.*;
import battle.component.effect.*;
import battle.component.info.*;
import battle.config.GameConfig;
import battle.entity.EntityECS;
import battle.manager.EntityManager;
import battle.pool.EntityPool;
import bitzero.core.P;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EntityFactory {
    public EntityPool pool;
    private EntityManager entityManager;
    private ComponentFactory componentFactory;
    private Battle battle;

    public EntityFactory(EntityManager entityManager, ComponentFactory componentFactory, EntityPool pool, Battle battle) {
        this.pool = pool;
        this.entityManager = entityManager;
        this.componentFactory = componentFactory;
        this.battle = battle;
    }

    public EntityECS _createEntity(int typeID, EntityMode mode) {
        EntityECS entity = null;
        if (entity == null) {
            entity = new EntityECS(typeID, mode);
            this.pool.push(entity);
            this.entityManager.addEntity(entity);
        }
        return entity;
    }

    public EntityECS createBullet(int towerType, PositionComponent startPosition, PositionComponent targetPosition, List<EffectComponent> effects, EntityMode mode, double bulletSpeed, double bulletRadius) throws Exception {
        int typeID;
        EntityECS entity;
        BulletInfoComponent infoComponent;
        PositionComponent positionComponent;
        CollisionComponent collisionComponent;

        switch (towerType) {
            case GameConfig.ENTITY_ID.CANNON_TOWER:
                typeID = GameConfig.ENTITY_ID.BULLET;
                entity = this._createEntity(typeID, mode);
                infoComponent = this.componentFactory.createBulletInfoComponent(effects, "cannon", bulletRadius);
                collisionComponent = this.componentFactory.createCollisionComponent(0, 0, 1, 1);
                Point speed = Utils.getInstance().calculateVelocityVector(startPosition.getPos(), targetPosition.getPos(), bulletSpeed);
                VelocityComponent velocityComponent = this.componentFactory.createVelocityComponent(speed.x, speed.y, targetPosition);
                positionComponent = this.componentFactory.createPositionComponent(startPosition.getX(), startPosition.getY());

                entity.addComponent(infoComponent);
                entity.addComponent(positionComponent);
                entity.addComponent(velocityComponent);
                entity.addComponent(collisionComponent);
                return entity;

            case GameConfig.ENTITY_ID.BEAR_TOWER:
                typeID = GameConfig.ENTITY_ID.BULLET;
                entity = this._createEntity(typeID, mode);

                speed = Utils.getInstance().calculateVelocityVector(startPosition.getPos(), targetPosition.getPos(), bulletSpeed);

                infoComponent = this.componentFactory.createBulletInfoComponent(effects, "bear", bulletRadius);
                collisionComponent = this.componentFactory.createCollisionComponent(0, 0, 1, 1);
                positionComponent = this.componentFactory.createPositionComponent(startPosition.getX(), startPosition.getY());
                velocityComponent = this.componentFactory.createVelocityComponent(speed.x, speed.y, targetPosition);

                entity.addComponent(infoComponent);
                entity.addComponent(positionComponent);
                entity.addComponent(velocityComponent);
                entity.addComponent(collisionComponent);
                return entity;
            case GameConfig.ENTITY_ID.FROG_TOWER:
                typeID = GameConfig.ENTITY_ID.BULLET;
                entity = this._createEntity(typeID, mode);
                List<Point> dividePath = Utils.getInstance().divideCellPath(new Point(startPosition.getX(), startPosition.getY()),
                        new Point(targetPosition.getX(), targetPosition.getY()), 5);

                //create PathComponent for frog Bullet
                List<Point> path = new ArrayList<>();
                path.add(new Point(startPosition.getX(), startPosition.getY()));
                for (int i = 0; i < dividePath.size(); i++) path.add(dividePath.get(i));
                path.add(new Point(targetPosition.getX(), targetPosition.getY()));
                for (int i = dividePath.size() - 1; i >= 0; i--) path.add(dividePath.get(i));
                path.add(new Point(startPosition.getX(), startPosition.getY()));

                PathComponent pathComponent = this.componentFactory.createPathComponent(path, mode, false);

                //create velocityComponent
                speed = Utils.getInstance().calculateVelocityVector(startPosition.getPos(), targetPosition.getPos(), bulletSpeed);
                velocityComponent = this.componentFactory.createVelocityComponent(speed.x, speed.y);

                //OtherComponent
                infoComponent = this.componentFactory.createBulletInfoComponent(effects, "frog", bulletRadius);
                collisionComponent = this.componentFactory.createCollisionComponent(20, 20, 20, 20);
                positionComponent = this.componentFactory.createPositionComponent(startPosition.getX(), startPosition.getY());

                //add Component
                entity.addComponent(infoComponent);
                entity.addComponent(positionComponent);
                entity.addComponent(velocityComponent);
                entity.addComponent(collisionComponent);
                entity.addComponent(pathComponent);
                return entity;
            case GameConfig.ENTITY_ID.BUNNY_TOWER:
                typeID = GameConfig.ENTITY_ID.BULLET;
                entity = this._createEntity(typeID, mode);

                speed = Utils.getInstance().calculateVelocityVector(startPosition.getPos(), targetPosition.getPos(), bulletSpeed);

                infoComponent = this.componentFactory.createBulletInfoComponent(effects, "bunny", bulletRadius);
                collisionComponent = this.componentFactory.createCollisionComponent(0, 0, 1, 1);
                positionComponent = this.componentFactory.createPositionComponent(startPosition.getX(), startPosition.getY());
                velocityComponent = this.componentFactory.createVelocityComponent(speed.x, speed.y, null, new Point(targetPosition.getX(), targetPosition.getY()));
                entity.addComponent(infoComponent);
                entity.addComponent(positionComponent);
                entity.addComponent(velocityComponent);
                entity.addComponent(collisionComponent);
                return entity;
            case GameConfig.ENTITY_ID.WIZARD_TOWER:
                typeID = GameConfig.ENTITY_ID.BULLET;
                entity = this._createEntity(typeID, mode);

                speed = Utils.getInstance().calculateVelocityVector(startPosition.getPos(), targetPosition.getPos(), bulletSpeed);

                infoComponent = this.componentFactory.createBulletInfoComponent(effects, "wizard", bulletRadius);
                collisionComponent = this.componentFactory.createCollisionComponent(0, 0, 20, 20);
                positionComponent = this.componentFactory.createPositionComponent(startPosition.getX(), startPosition.getY());
                velocityComponent = this.componentFactory.createVelocityComponent(speed.x, speed.y, null, new Point(targetPosition.getX(), targetPosition.getY()));
                entity.addComponent(infoComponent);
                entity.addComponent(positionComponent);
                entity.addComponent(velocityComponent);
                entity.addComponent(collisionComponent);
                return entity;
        }
        return null;
    }


    //Create Monster
    public EntityECS createSwordManMonster(Point pixelPos, EntityMode mode) throws Exception {
        int typeID = GameConfig.ENTITY_ID.SWORD_MAN;
        EntityECS entity = new EntityECS(typeID, mode);
        this.pool.push(entity);
        this.entityManager.addEntity(entity);

        MonsterInfoComponent monsterInfoComponent = this.componentFactory.createMonsterInfoComponent("normal", "land", 30, 1, 10, null, null);
        PositionComponent positionComponent = this.componentFactory.createPositionComponent((int) pixelPos.x, (int) pixelPos.y);

        VelocityComponent velocityComponent = this.componentFactory.createVelocityComponent(0.8 * GameConfig.TILE_WIDTH, 0, null);
        CollisionComponent collisionComponent = this.componentFactory.createCollisionComponent(20, 30);
        LifeComponent lifeComponent = this.componentFactory.createLifeComponent(180);

        // FrozenEffect frozenEffect= this.componentFactory.createFrozenEffect();
        //Point tilePos = Utils.getInstance().pixel2Tile(pixelPos.x, pixelPos.y, mode);
        //ToDo: find shortest Path with TilePos

        List<Point> shortestPath = this.getShortestPathInTile(mode, 0, 4);
        PathComponent pathComponent = this.componentFactory.createPathComponent(shortestPath, mode, true);

        entity.addComponent(monsterInfoComponent);
        entity.addComponent(positionComponent);
        entity.addComponent(velocityComponent);
        entity.addComponent(collisionComponent);
        entity.addComponent(lifeComponent);
        entity.addComponent(pathComponent);
        System.out.println("CreateSwordManMonster");

        return entity;
    }

    public EntityECS createAssassinMonster(Point pixelPos, EntityMode mode) throws Exception {
        int typeID = GameConfig.ENTITY_ID.ASSASSIN;
        EntityECS entity = new EntityECS(typeID, mode);
        this.pool.push(entity);
        this.entityManager.addEntity(entity);

        MonsterInfoComponent monsterInfoComponent = this.componentFactory.createMonsterInfoComponent("normal", "land", 15, 1, 10, null, null);
        PositionComponent positionComponent = this.componentFactory.createPositionComponent((int) pixelPos.x, (int) pixelPos.y);

        VelocityComponent velocityComponent = this.componentFactory.createVelocityComponent(1.4 * GameConfig.TILE_WIDTH, 0, null);
        CollisionComponent collisionComponent = this.componentFactory.createCollisionComponent(20, 30);
        LifeComponent lifeComponent = this.componentFactory.createLifeComponent(15);

        // FrozenEffect frozenEffect= this.componentFactory.createFrozenEffect();
        //Point tilePos = Utils.getInstance().pixel2Tile(pixelPos.x, pixelPos.y, mode);
        //ToDo: find shortest Path with TilePos
        List<Point> shortestPath = this.getShortestPathInTile(mode, 0, 4);
        PathComponent pathComponent = this.componentFactory.createPathComponent(shortestPath, mode, true);

        entity.addComponent(monsterInfoComponent);
        entity.addComponent(positionComponent);
        entity.addComponent(velocityComponent);
        entity.addComponent(collisionComponent);
        entity.addComponent(lifeComponent);
        entity.addComponent(pathComponent);
        System.out.println("CreateSwordManMonster");

        return entity;
    }

    public EntityECS createBatMonster(Point pixelPos, EntityMode mode) throws Exception {
        int typeID = GameConfig.ENTITY_ID.BAT;
        EntityECS entity = new EntityECS(typeID, mode);
        this.pool.push(entity);
        this.entityManager.addEntity(entity);

        MonsterInfoComponent monsterInfoComponent = this.componentFactory.createMonsterInfoComponent("normal", "air", 25, 1, 10, null, null);
        PositionComponent positionComponent = this.componentFactory.createPositionComponent((int) pixelPos.x, (int) pixelPos.y);

        VelocityComponent velocityComponent = this.componentFactory.createVelocityComponent(0.7 * GameConfig.TILE_WIDTH, 0, null);
        CollisionComponent collisionComponent = this.componentFactory.createCollisionComponent(20, 30);
        LifeComponent lifeComponent = this.componentFactory.createLifeComponent(140);

        // FrozenEffect frozenEffect= this.componentFactory.createFrozenEffect();
        //Point tilePos = Utils.getInstance().pixel2Tile(pixelPos.x, pixelPos.y, mode);
        //ToDo: find shortest Path with TilePos
        PathComponent pathComponent = this.componentFactory.createPathComponent(this.airMonsterPath(mode), mode, false);

        entity.addComponent(monsterInfoComponent);
        entity.addComponent(positionComponent);
        entity.addComponent(velocityComponent);
        entity.addComponent(collisionComponent);
        entity.addComponent(lifeComponent);
        entity.addComponent(pathComponent);
        System.out.println("CreateSwordManMonster");

        return entity;
    }

    public EntityECS createGiantMonster(Point pixelPos, EntityMode mode) throws Exception {
        int typeID = GameConfig.ENTITY_ID.GIANT;
        EntityECS entity = new EntityECS(typeID, mode);
        this.pool.push(entity);
        this.entityManager.addEntity(entity);

        MonsterInfoComponent monsterInfoComponent = this.componentFactory.createMonsterInfoComponent("normal", "ground", 200, 1, 10, null, null);
        PositionComponent positionComponent = this.componentFactory.createPositionComponent((int) pixelPos.x, (int) pixelPos.y);

        VelocityComponent velocityComponent = this.componentFactory.createVelocityComponent(0.4 * GameConfig.TILE_WIDTH, 0, null);
        CollisionComponent collisionComponent = this.componentFactory.createCollisionComponent(30, 30);
        LifeComponent lifeComponent = this.componentFactory.createLifeComponent(200);

        // FrozenEffect frozenEffect= this.componentFactory.createFrozenEffect();
        //Point tilePos = Utils.getInstance().pixel2Tile(pixelPos.x, pixelPos.y, mode);
        //ToDo: find shortest Path with TilePos
        List<Point> shortestPath = this.getShortestPathInTile(mode, 0, 4);
        PathComponent pathComponent = this.componentFactory.createPathComponent(shortestPath, mode, true);

        entity.addComponent(monsterInfoComponent);
        entity.addComponent(positionComponent);
        entity.addComponent(velocityComponent);
        entity.addComponent(collisionComponent);
        entity.addComponent(lifeComponent);
        entity.addComponent(pathComponent);
        System.out.println("CreateSwordManMonster");

        return entity;
    }

    public EntityECS createNinjaMonster(Point pixelPos, EntityMode mode) throws Exception {
        int typeID = GameConfig.ENTITY_ID.NINJA;
        EntityECS entity = new EntityECS(typeID, mode);
        this.pool.push(entity);
        this.entityManager.addEntity(entity);

        MonsterInfoComponent monsterInfoComponent = this.componentFactory.createMonsterInfoComponent("normal", "ground", 30, 1, 10, null, null);
        PositionComponent positionComponent = this.componentFactory.createPositionComponent((int) pixelPos.x, (int) pixelPos.y);

        VelocityComponent velocityComponent = this.componentFactory.createVelocityComponent(0.5 * GameConfig.TILE_WIDTH, 0, null);
        CollisionComponent collisionComponent = this.componentFactory.createCollisionComponent(20, 30);
        LifeComponent lifeComponent = this.componentFactory.createLifeComponent(60);
        UnderGroundComponent underGroundComponent = this.componentFactory.createUnderGroundComponent();
        // FrozenEffect frozenEffect= this.componentFactory.createFrozenEffect();
        //Point tilePos = Utils.getInstance().pixel2Tile(pixelPos.x, pixelPos.y, mode);
        //ToDo: find shortest Path with TilePos
        List<Point> shortestPath = this.getShortestPathInTile(mode, 0, 4);
        PathComponent pathComponent = this.componentFactory.createPathComponent(shortestPath, mode, true);

        entity.addComponent(monsterInfoComponent);
        entity.addComponent(positionComponent);
        entity.addComponent(velocityComponent);
        entity.addComponent(collisionComponent);
        entity.addComponent(lifeComponent);
        entity.addComponent(pathComponent);
        entity.addComponent(underGroundComponent);
        System.out.println("CreateSwordManMonster");

        return entity;
    }

    //Create Boss
    public EntityECS createDemonTreeBoss(Point pixelPos, EntityMode mode) throws Exception {
        int typeID = GameConfig.ENTITY_ID.DEMON_TREE;
        EntityECS entity = new EntityECS(typeID, mode);
        this.pool.push(entity);
        this.entityManager.addEntity(entity);

        MonsterInfoComponent monsterInfoComponent = this.componentFactory.createMonsterInfoComponent("boss", "land", 400, 10, 10, null, null);
        PositionComponent positionComponent = this.componentFactory.createPositionComponent((int) pixelPos.x, (int) pixelPos.y);

        VelocityComponent velocityComponent = this.componentFactory.createVelocityComponent(0.4 * GameConfig.TILE_WIDTH, 0, null);
        CollisionComponent collisionComponent = this.componentFactory.createCollisionComponent(20, 30);
        LifeComponent lifeComponent = this.componentFactory.createLifeComponent(10000);
        SpawnMinionComponent spawnMinionComponent = this.componentFactory.createSpawnMinionComponent(2);

        //ToDo: find shortest Path with TilePos
        List<Point> shortestPath = this.getShortestPathInTile(mode, 0, 4);
        PathComponent pathComponent = this.componentFactory.createPathComponent(shortestPath, mode, true);

        entity.addComponent(monsterInfoComponent);
        entity.addComponent(positionComponent);
        entity.addComponent(velocityComponent);
        entity.addComponent(collisionComponent);
        entity.addComponent(lifeComponent);
        entity.addComponent(pathComponent);
        entity.addComponent(spawnMinionComponent);

        return entity;
    }

    public EntityECS createDemonTreeMinion(Point pixelPos, EntityMode mode) throws Exception {
        int typeID = GameConfig.ENTITY_ID.DEMON_TREE_MINION;
        EntityECS entity = new EntityECS(typeID, mode);
        this.pool.push(entity);
        this.entityManager.addEntity(entity);

        MonsterInfoComponent monsterInfoComponent = this.componentFactory.createMonsterInfoComponent("normal", "land", 50, 1, 1, null, null);
        PositionComponent positionComponent = this.componentFactory.createPositionComponent((int) pixelPos.x, (int) pixelPos.y);

        VelocityComponent velocityComponent = this.componentFactory.createVelocityComponent(0.8 * GameConfig.TILE_WIDTH, 0, null);
        CollisionComponent collisionComponent = this.componentFactory.createCollisionComponent(20, 30);
        LifeComponent lifeComponent = this.componentFactory.createLifeComponent(30);
        // FrozenEffect frozenEffect= this.componentFactory.createFrozenEffect();
        //Point tilePos = Utils.getInstance().pixel2Tile(pixelPos.x, pixelPos.y, mode);
        //ToDo: find shortest Path with TilePos
        List<Point> shortestPath = this.getShortestPathInTile(mode, 0, 4);
        PathComponent pathComponent = this.componentFactory.createPathComponent(shortestPath, mode, true);
        entity.addComponent(monsterInfoComponent);
        entity.addComponent(positionComponent);
        entity.addComponent(velocityComponent);
        entity.addComponent(collisionComponent);
        entity.addComponent(lifeComponent);
        entity.addComponent(pathComponent);
        return entity;
    }

    public EntityECS createDarkGiantBoss(Point pixelPos, EntityMode mode) throws Exception {
        int typeID = GameConfig.ENTITY_ID.DARK_GIANT;
        EntityECS entity = new EntityECS(typeID, mode);
        this.pool.push(entity);
        this.entityManager.addEntity(entity);

        MonsterInfoComponent monsterInfoComponent = this.componentFactory.createMonsterInfoComponent("normal", "land", 500, 1, 1, null, null);
        PositionComponent positionComponent = this.componentFactory.createPositionComponent((int) pixelPos.x, (int) pixelPos.y);

        VelocityComponent velocityComponent = this.componentFactory.createVelocityComponent(0.4 * GameConfig.TILE_WIDTH, 0, null);
        CollisionComponent collisionComponent = this.componentFactory.createCollisionComponent(40, 40);
        LifeComponent lifeComponent = this.componentFactory.createLifeComponent(10000);
        // FrozenEffect frozenEffect= this.componentFactory.createFrozenEffect();
        //Point tilePos = Utils.getInstance().pixel2Tile(pixelPos.x, pixelPos.y, mode);
        //ToDo: find shortest Path with TilePos
        List<Point> shortestPath = this.getShortestPathInTile(mode, 0, 4);
        PathComponent pathComponent = this.componentFactory.createPathComponent(shortestPath, mode, true);

        entity.addComponent(monsterInfoComponent);
        entity.addComponent(positionComponent);
        entity.addComponent(velocityComponent);
        entity.addComponent(collisionComponent);
        entity.addComponent(lifeComponent);
        entity.addComponent(pathComponent);
        return entity;
    }

    public EntityECS createSatyrBoss(Point pixelPos, EntityMode mode) throws Exception {
        int typeID = GameConfig.ENTITY_ID.SATYR;
        EntityECS entity = new EntityECS(typeID, mode);
        this.pool.push(entity);
        this.entityManager.addEntity(entity);

        MonsterInfoComponent monsterInfoComponent = this.componentFactory.createMonsterInfoComponent("boss", "land", 300, 1, 1, null, null);
        PositionComponent positionComponent = this.componentFactory.createPositionComponent((int) pixelPos.x, (int) pixelPos.y);

        VelocityComponent velocityComponent = this.componentFactory.createVelocityComponent(0.4 * GameConfig.TILE_WIDTH, 0, null);
        CollisionComponent collisionComponent = this.componentFactory.createCollisionComponent(30, 30);
        LifeComponent lifeComponent = this.componentFactory.createLifeComponent(10000);

        HealingAbilityComponent healingAbilityComponent = this.componentFactory.createHealingAbilityComponent(2 * GameConfig.TILE_WIDTH, 100);
        // FrozenEffect frozenEffect= this.componentFactory.createFrozenEffect();
        //Point tilePos = Utils.getInstance().pixel2Tile(pixelPos.x, pixelPos.y, mode);
        //ToDo: find shortest Path with TilePos
        List<Point> shortestPath = this.getShortestPathInTile(mode, 0, 4);
        PathComponent pathComponent = this.componentFactory.createPathComponent(shortestPath, mode, true);

        entity.addComponent(monsterInfoComponent);
        entity.addComponent(positionComponent);
        entity.addComponent(velocityComponent);
        entity.addComponent(collisionComponent);
        entity.addComponent(lifeComponent);
        entity.addComponent(pathComponent);
        entity.addComponent(healingAbilityComponent);
        return entity;
    }


    //Create Tower

    public EntityECS createCannonOwlTower(Point tilePos, EntityMode mode) throws Exception {
        int typeID = GameConfig.ENTITY_ID.CANNON_TOWER;
        EntityECS entity = this._createEntity(typeID, mode);

        double attackRange = 1.5 * GameConfig.TILE_WIDTH;
        Point pixelPos = Utils.tile2Pixel(tilePos.x, tilePos.y, mode);
        double bulletSpeed = 50 * GameConfig.TILE_WIDTH / 10.0;
        double bulletRadius = 0;

        TowerInfoComponent towerInfoComponent = this.componentFactory.createTowerInfoComponent(10, "bulletTargetType", "attack", "monster", "bulletType");
        PositionComponent positionComponent = this.componentFactory.createPositionComponent(pixelPos.x, pixelPos.y);
        AttackComponent attackComponent = this.componentFactory.createAttackComponent(10, GameConfig.TOWER_TARGET_STRATEGY.MAX_HP, attackRange, 0.6, 0, null, bulletSpeed, bulletRadius);

        entity.addComponent(towerInfoComponent);
        entity.addComponent(positionComponent);
        entity.addComponent(attackComponent);

        this.battle.minusPlayerEnergy(towerInfoComponent.getEnergy(), mode);
        return entity;
    }

    public EntityECS createIceGunPolarBearTower(Point tilePos, EntityMode mode) throws Exception {
        int typeID = GameConfig.ENTITY_ID.BEAR_TOWER;
        EntityECS entity = this._createEntity(typeID, mode);

        double attackRange = 1.8 * GameConfig.TILE_WIDTH;
        Point pixelPos = Utils.tile2Pixel(tilePos.x, tilePos.y, mode);
        double bulletSpeed = 60 * GameConfig.TILE_WIDTH / 10.0;
        double bulletRadius = 0;

        FrozenEffect frozenEffect = this.componentFactory.createFrozenEffect(1.5);
        List<EffectComponent> effectList = Arrays.asList(frozenEffect);
        TowerInfoComponent towerInfoComponent = this.componentFactory.createTowerInfoComponent(10, "bulletTargetType", "support", "monster", "bulletType");
        PositionComponent positionComponent = this.componentFactory.createPositionComponent(pixelPos.x, pixelPos.y);
        AttackComponent attackComponent = this.componentFactory.createAttackComponent(0, GameConfig.TOWER_TARGET_STRATEGY.MAX_HP, attackRange, 3.4, 0, effectList, bulletSpeed, bulletRadius);

        entity.addComponent(towerInfoComponent);
        entity.addComponent(positionComponent);
        entity.addComponent(attackComponent);

        this.battle.minusPlayerEnergy(towerInfoComponent.getEnergy(), mode);

        return entity;
    }

    public EntityECS createFrogTower(Point tilePos, EntityMode mode) throws Exception {
        int typeID = GameConfig.ENTITY_ID.FROG_TOWER;
        EntityECS entity = this._createEntity(typeID, mode);

        double attackRange = 2 * GameConfig.TILE_WIDTH;
        Point pixelPos = Utils.tile2Pixel(tilePos.x, tilePos.y, mode);
        double bulletSpeed = 30 * GameConfig.TILE_WIDTH / 10.0;
        double bulletRadius = 0;

        TowerInfoComponent towerInfoComponent = this.componentFactory.createTowerInfoComponent(10, "bulletTargetType", "attack", "monster", "bulletType");
        PositionComponent positionComponent = this.componentFactory.createPositionComponent(pixelPos.x, pixelPos.y);
        AttackComponent attackComponent = this.componentFactory.createAttackComponent(3, GameConfig.TOWER_TARGET_STRATEGY.MAX_HP, attackRange, 1.5, 0, null, bulletSpeed, bulletRadius);

        entity.addComponent(towerInfoComponent);
        entity.addComponent(positionComponent);
        entity.addComponent(attackComponent);

        this.battle.minusPlayerEnergy(towerInfoComponent.getEnergy(), mode);

        return entity;
    }

    public EntityECS createBunnyOilGunTower(Point tilePos, EntityMode mode) throws Exception {
        int typeID = GameConfig.ENTITY_ID.BUNNY_TOWER;
        EntityECS entity = this._createEntity(typeID, mode);

        double attackRange = 1.5 * GameConfig.TILE_WIDTH;
        Point pixelPos = Utils.tile2Pixel(tilePos.x, tilePos.y, mode);
        double bulletSpeed = 40 * GameConfig.TILE_WIDTH / 10.0;
        double bulletRadius = 0.6 * GameConfig.TILE_WIDTH;

        SlowEffect slowEffect = this.componentFactory.createSlowEffect(1, 0.5);
        List<EffectComponent> effectList = Collections.singletonList(slowEffect);

        TowerInfoComponent towerInfoComponent = this.componentFactory.createTowerInfoComponent(10, "bulletTargetType", "attack", "monster", "bulletType");
        PositionComponent positionComponent = this.componentFactory.createPositionComponent(pixelPos.x, pixelPos.y);
        AttackComponent attackComponent = this.componentFactory.createAttackComponent(0, GameConfig.TOWER_TARGET_STRATEGY.MAX_HP, attackRange, 2.0, 0, effectList, bulletSpeed, bulletRadius);

        entity.addComponent(towerInfoComponent);
        entity.addComponent(positionComponent);
        entity.addComponent(attackComponent);

        this.battle.minusPlayerEnergy(towerInfoComponent.getEnergy(), mode);

        return entity;
    }

    public EntityECS createWizardTower(Point tilePos, EntityMode mode) throws Exception {
        int typeID = GameConfig.ENTITY_ID.WIZARD_TOWER;
        EntityECS entity = this._createEntity(typeID, mode);

        double attackRange = 1.5 * GameConfig.TILE_WIDTH;
        Point pixelPos = Utils.tile2Pixel(tilePos.x, tilePos.y, mode);
        double bulletSpeed = 30 * GameConfig.TILE_WIDTH / 10.0;
        double bulletRadius = 1 * GameConfig.TILE_WIDTH * 1.0;

        TowerInfoComponent towerInfoComponent = this.componentFactory.createTowerInfoComponent(12, "bulletTargetType", "attack", "monster", "bulletType");
        PositionComponent positionComponent = this.componentFactory.createPositionComponent(pixelPos.x, pixelPos.y);
        AttackComponent attackComponent = this.componentFactory.createAttackComponent(5, GameConfig.TOWER_TARGET_STRATEGY.MAX_HP, attackRange, 2.2, 0, null, bulletSpeed, bulletRadius);

        entity.addComponent(towerInfoComponent)
                .addComponent(positionComponent)
                .addComponent(attackComponent);

        this.battle.minusPlayerEnergy(towerInfoComponent.getEnergy(), mode);
        return entity;
    }

    public EntityECS createSnakeAttackSpeedTower(Point tilePos, EntityMode mode) throws Exception {
        int typeID = GameConfig.ENTITY_ID.SNAKE_TOWER;
        EntityECS entity = this._createEntity(typeID, mode);

        Point pixelPos = Utils.tile2Pixel(tilePos.x, tilePos.y, mode);

        TowerInfoComponent towerInfoComponent = this.componentFactory.createTowerInfoComponent(12, "bulletTargetType", "support", "aura", "none");
        PositionComponent positionComponent = this.componentFactory.createPositionComponent(pixelPos.x, pixelPos.y);
        BuffAttackSpeedEffect buffAttackSpeedEffect = this.componentFactory.createBuffAttackSpeedEffect(0.2);
        TowerAbilityComponent towerAbilityComponent = this.componentFactory.createTowerAbilityComponent(1.2 * GameConfig.TILE_WIDTH, buffAttackSpeedEffect);
        entity.addComponent(towerInfoComponent)
                .addComponent(positionComponent)
                .addComponent(towerAbilityComponent);

        this.battle.minusPlayerEnergy(towerInfoComponent.getEnergy(), mode);

        return entity;
    }

    public EntityECS createGoatAttackDamageTower(Point tilePos, EntityMode mode) throws Exception {
        int typeID = GameConfig.ENTITY_ID.GOAT_TOWER;
        EntityECS entity = this._createEntity(typeID, mode);

        Point pixelPos = Utils.tile2Pixel(tilePos.x, tilePos.y, mode);

        TowerInfoComponent towerInfoComponent = this.componentFactory.createTowerInfoComponent(12, "bulletTargetType", "support", "aura", "none");
        PositionComponent positionComponent = this.componentFactory.createPositionComponent(pixelPos.x, pixelPos.y);
        BuffAttackDamageEffect buffAttackDamageEffect = this.componentFactory.createBuffAttackDamageEffect(0.2);
        TowerAbilityComponent towerAbilityComponent = this.componentFactory.createTowerAbilityComponent(1.2 * GameConfig.TILE_WIDTH, buffAttackDamageEffect);
        entity.addComponent(towerInfoComponent)
                .addComponent(positionComponent)
                .addComponent(towerAbilityComponent);

        this.battle.minusPlayerEnergy(towerInfoComponent.getEnergy(), mode);
        return entity;
    }

    // Create Spell

    public EntityECS createFireSpell(Point pixelPos, EntityMode mode) throws Exception {
        int typeID = GameConfig.ENTITY_ID.FIRE_SPELL;
        EntityECS entity = this._createEntity(typeID, mode);

        double S = 300;
        double V = 1000;
        double T = S / V;

        PositionComponent positionComponent = this.componentFactory.createPositionComponent(pixelPos.getX(), pixelPos.getY() + S);

        Point speed = Utils.calculateVelocityVector(new Point(pixelPos.getX(), pixelPos.getY() + S), pixelPos, V);
        VelocityComponent velocityComponent = this.componentFactory.createVelocityComponent(speed.getX(), speed.getY());

        DamageEffect damageEffect = this.componentFactory.createDamageEffect(100);
        SpellInfoComponent spellInfoComponent = this.componentFactory.createSpellInfoComponent(pixelPos, Arrays.asList(damageEffect), 1.2 * GameConfig.TILE_WIDTH, T);

        entity.addComponent(positionComponent);
        entity.addComponent(velocityComponent);
        entity.addComponent(spellInfoComponent);


        return entity;
    }

    public EntityECS createFrozenSpell(Point pixelPos, EntityMode mode) throws Exception {
        int typeID = GameConfig.ENTITY_ID.FROZEN_SPELL;
        EntityECS entity = this._createEntity(typeID, mode);

        double S = 300;
        double V = 1000;
        double T = S / V;

        PositionComponent positionComponent = this.componentFactory.createPositionComponent(pixelPos.getX(), pixelPos.getY() + S);
        Point speed = Utils.calculateVelocityVector(new Point(pixelPos.getX(), pixelPos.getY() + S), pixelPos, V);
        VelocityComponent velocityComponent = this.componentFactory.createVelocityComponent(speed.getX(), speed.getY());

        DamageEffect damageEffect = this.componentFactory.createDamageEffect(10);
        FrozenEffect frozenEffect = this.componentFactory.createFrozenEffect(5);
        SpellInfoComponent spellInfoComponent = this.componentFactory.createSpellInfoComponent(pixelPos, Arrays.asList(damageEffect, frozenEffect), 1.2 * GameConfig.TILE_WIDTH, T);

        entity.addComponent(positionComponent);
        entity.addComponent(velocityComponent);
        entity.addComponent(spellInfoComponent);
        return entity;
    }

    public EntityECS createTrapSpell(Point tilePos, EntityMode mode) throws Exception {
        int typeID = GameConfig.ENTITY_ID.TRAP_SPELL;
        EntityECS entity = this._createEntity(typeID, mode);

        Point pixelPos = Utils.tile2Pixel(tilePos.x, tilePos.y, mode);
        PositionComponent positionComponent = this.componentFactory.createPositionComponent(pixelPos.getX(), pixelPos.getY());
        CollisionComponent collisionComponent = this.componentFactory.createCollisionComponent(GameConfig.TILE_WIDTH, GameConfig.TILE_HEIGHT);
        TrapInfoComponent trapInfoComponent = this.componentFactory.createTrapInfoComponent(0.3);
        entity.addComponent(positionComponent);
        entity.addComponent(trapInfoComponent);
        entity.addComponent(collisionComponent);
        return entity;
    }

    public List<Point> getShortestPathInTile(EntityMode mode, int tilePosX, int tilePosY) {
        if (mode == EntityMode.PLAYER) return battle.player1ShortestPath[tilePosX][tilePosY];
        else return battle.player2ShortestPath[tilePosX][tilePosY];
    }

    public ArrayList<Point> fakeGroundMonsterPixelPath(EntityMode mode) {
        ArrayList<Point> path = new ArrayList<Point>();
        path.add(Utils.tile2Pixel(0, 4, mode));
        path.add(Utils.tile2Pixel(1, 4, mode));
        path.add(Utils.tile2Pixel(2, 4, mode));
        path.add(Utils.tile2Pixel(3, 4, mode));
        path.add(Utils.tile2Pixel(4, 4, mode));
        path.add(Utils.tile2Pixel(5, 4, mode));
        path.add(Utils.tile2Pixel(6, 4, mode));
        path.add(Utils.tile2Pixel(6, 3, mode));
        path.add(Utils.tile2Pixel(6, 2, mode));
        path.add(Utils.tile2Pixel(5, 2, mode));
        path.add(Utils.tile2Pixel(4, 2, mode));
        path.add(Utils.tile2Pixel(3, 2, mode));
        path.add(Utils.tile2Pixel(2, 2, mode));
        path.add(Utils.tile2Pixel(1, 2, mode));
        path.add(Utils.tile2Pixel(0, 2, mode));
        path.add(Utils.tile2Pixel(0, 1, mode));
        path.add(Utils.tile2Pixel(0, 0, mode));
        path.add(Utils.tile2Pixel(1, 0, mode));
        path.add(Utils.tile2Pixel(2, 0, mode));
        path.add(Utils.tile2Pixel(3, 0, mode));
        path.add(Utils.tile2Pixel(4, 0, mode));
        path.add(Utils.tile2Pixel(5, 0, mode));
        path.add(Utils.tile2Pixel(6, 0, mode));

        return path;
    }

    public ArrayList<Point> fakeGroundMonsterTilePath(EntityMode mode) {
        ArrayList<Point> path = new ArrayList<Point>();
        path.add(new Point(0, 4));
        path.add(new Point(1, 4));
        path.add(new Point(2, 4));
        path.add(new Point(3, 4));
        path.add(new Point(4, 4));
        path.add(new Point(5, 4));
        path.add(new Point(6, 4));
        path.add(new Point(6, 3));
        path.add(new Point(6, 2));
        path.add(new Point(5, 2));
        path.add(new Point(4, 2));
        path.add(new Point(3, 2));
        path.add(new Point(2, 2));
        path.add(new Point(1, 2));
        path.add(new Point(0, 2));
        path.add(new Point(0, 1));
        path.add(new Point(0, 0));
        path.add(new Point(1, 0));
        path.add(new Point(2, 0));
        path.add(new Point(3, 0));
        path.add(new Point(4, 0));
        path.add(new Point(5, 0));
        path.add(new Point(6, 0));

        return path;
    }

    public ArrayList<Point> airMonsterPath(EntityMode mode) {
        ArrayList<Point> path = new ArrayList<Point>();
        path.add(Utils.tile2Pixel(0, 4, mode));
        path.add(Utils.tile2Pixel(1, 3, mode));
        path.add(Utils.tile2Pixel(2, 2, mode));
        path.add(Utils.tile2Pixel(3, 1, mode));
        path.add(Utils.tile2Pixel(4, 0, mode));
        path.add(Utils.tile2Pixel(4.01, 0, mode));
        path.add(Utils.tile2Pixel(5, 0, mode));
        path.add(Utils.tile2Pixel(6, 0, mode));
        return path;
    }


}

