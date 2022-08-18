package battle.factory;

import battle.Battle;
import battle.common.EntityMode;
import battle.common.Point;
import battle.common.UUIDGeneratorECS;
import battle.common.Utils;
import battle.component.common.*;
import battle.component.effect.*;
import battle.component.info.*;
import battle.component.towerskill.*;
import battle.config.GameConfig;
import battle.config.GameStat.TargetBuffConfigItem2;
import battle.config.GameStat.TowerConfigItem2;
import battle.config.GameStat.TowerStat2;
import battle.config.conf.monster.MonsterConfig;
import battle.config.conf.monster.MonsterConfigItem;
import battle.config.conf.targetBuff.TargetBuffConfig;
import battle.config.conf.targetBuff.TargetBuffConfigItem;
import battle.config.conf.tower.TowerConfig;
import battle.config.conf.tower.TowerConfigItem;
import battle.config.conf.tower.TowerStat;
import battle.config.conf.towerBuff.TowerBuffConfig;
import battle.entity.EntityECS;
import battle.manager.EntityManager;
import battle.manager.SystemManager;
import battle.pool.EntityPool;
import bitzero.server.BitZeroServer;
import bitzero.server.entities.User;
import bitzero.util.ExtensionUtility;
import cmd.send.error.ResponseError;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EntityFactory {
    public EntityPool pool;
    private EntityManager entityManager;
    private ComponentFactory componentFactory;
    private Battle battle;

    public EntityFactory(EntityManager entityManager, ComponentFactory componentFactory, EntityPool entityPool, Battle battle) {
        this.pool = entityPool;
        this.entityManager = entityManager;
        this.componentFactory = componentFactory;
        this.battle = battle;
    }

    public EntityECS _createEntity(int typeID, EntityMode mode) {
        EntityECS entity = null;
        if (entity == null) {
            long id = this.battle.getUuidGeneratorECS().genEntityID();
            entity = new EntityECS(typeID, mode, id, this.battle.getComponentManager(), this.battle.getSystemManager());
//            this.pool.push(entity);
            this.entityManager.addEntity(entity);
        }
        return entity;
    }

    public EntityECS createBullet(int towerType, PositionComponent startPosition, EntityECS targetEntity, Point staticPosition, List<EffectComponent> effects, EntityMode mode, double bulletSpeed, double bulletRadius) throws Exception {
        int typeID;
        EntityECS entity;
        BulletInfoComponent infoComponent;
        PositionComponent positionComponent;
        CollisionComponent collisionComponent;
        PositionComponent chasingPosition;
        switch (towerType) {
            case GameConfig.ENTITY_ID.CANNON_TOWER:
                typeID = GameConfig.ENTITY_ID.BULLET;
                entity = this._createEntity(typeID, mode);
                infoComponent = this.componentFactory.createBulletInfoComponent(effects, "cannon", bulletRadius);
                collisionComponent = this.componentFactory.createCollisionComponent(0, 0, 10, 10);
                chasingPosition = (PositionComponent) targetEntity.getComponent(PositionComponent.typeID);

                Point speed = Utils.calculateVelocityVector(startPosition.getPos(), chasingPosition.getPos(), bulletSpeed);
                VelocityComponent velocityComponent = this.componentFactory.createVelocityComponent(speed.x, speed.y, targetEntity.getId());
                positionComponent = this.componentFactory.createPositionComponent(startPosition.getX(), startPosition.getY());

                entity.addComponent(infoComponent);
                entity.addComponent(positionComponent);
                entity.addComponent(velocityComponent);
                entity.addComponent(collisionComponent);
                return entity;

            case GameConfig.ENTITY_ID.BEAR_TOWER:
                typeID = GameConfig.ENTITY_ID.BULLET;
                entity = this._createEntity(typeID, mode);
                chasingPosition = (PositionComponent) targetEntity.getComponent(PositionComponent.typeID);
                speed = Utils.calculateVelocityVector(startPosition.getPos(), chasingPosition.getPos(), bulletSpeed);

                infoComponent = this.componentFactory.createBulletInfoComponent(effects, "bear", bulletRadius);
                collisionComponent = this.componentFactory.createCollisionComponent(0, 0, 10, 10);
                positionComponent = this.componentFactory.createPositionComponent(startPosition.getX(), startPosition.getY());
                velocityComponent = this.componentFactory.createVelocityComponent(speed.x, speed.y, targetEntity.getId());

                entity.addComponent(infoComponent);
                entity.addComponent(positionComponent);
                entity.addComponent(velocityComponent);
                entity.addComponent(collisionComponent);
                return entity;
            case GameConfig.ENTITY_ID.FROG_TOWER:
                typeID = GameConfig.ENTITY_ID.BULLET;
                entity = this._createEntity(typeID, mode);
                List<Point> dividePath = Utils.divideCellPath(new Point(startPosition.getX(), startPosition.getY()),
                        staticPosition, 5);

                //create PathComponent for frog Bullet
                List<Point> path = new ArrayList<>();
                path.add(startPosition.getPos());
                for (int i = 0; i < dividePath.size(); i++) path.add(dividePath.get(i));
                path.add(staticPosition);
                for (int i = dividePath.size() - 1; i >= 0; i--) path.add(dividePath.get(i));
                path.add(startPosition.getPos());

                PathComponent pathComponent = this.componentFactory.createPathComponent(path, mode, false);

                //create velocityComponent
                speed = Utils.calculateVelocityVector(startPosition.getPos(), staticPosition, bulletSpeed);
                velocityComponent = this.componentFactory.createVelocityComponent(speed.x, speed.y);

                //OtherComponent
                infoComponent = this.componentFactory.createBulletInfoComponent(effects, "frog", bulletRadius);
                collisionComponent = this.componentFactory.createCollisionComponent(40, 40, 40, 40);
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

                speed = Utils.calculateVelocityVector(startPosition.getPos(), staticPosition, bulletSpeed);

                infoComponent = this.componentFactory.createBulletInfoComponent(effects, "bunny", bulletRadius);
                collisionComponent = this.componentFactory.createCollisionComponent(0, 0, 10, 10);
                positionComponent = this.componentFactory.createPositionComponent(startPosition.getX(), startPosition.getY());
                velocityComponent = this.componentFactory.createVelocityComponent(speed.x, speed.y, staticPosition);
                entity.addComponent(infoComponent);
                entity.addComponent(positionComponent);
                entity.addComponent(velocityComponent);
                entity.addComponent(collisionComponent);
                return entity;
            case GameConfig.ENTITY_ID.WIZARD_TOWER:
                typeID = GameConfig.ENTITY_ID.BULLET;
                entity = this._createEntity(typeID, mode);

                speed = Utils.getInstance().calculateVelocityVector(startPosition.getPos(), staticPosition, bulletSpeed);

                infoComponent = this.componentFactory.createBulletInfoComponent(effects, "wizard", bulletRadius);
                collisionComponent = this.componentFactory.createCollisionComponent(0, 0, 20, 20);
                positionComponent = this.componentFactory.createPositionComponent(startPosition.getX(), startPosition.getY());
                velocityComponent = this.componentFactory.createVelocityComponent(speed.x, speed.y, staticPosition);
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
        EntityECS entity = this._createEntity(typeID, mode);
//        this.pool.push(entity);
//        this.entityManager.addEntity(entity);

        MonsterConfigItem SwordManStat = MonsterConfig.INS.getMonsterConfig(MonsterConfig.SWORDSMAN);
        String category = SwordManStat.getCategory();
        String classs = SwordManStat.getMonsterClass();
        int weight = (int) SwordManStat.getWeight();
        int gainEnergy = SwordManStat.getGainEnergy();
        int ability = SwordManStat.getAbility();
        double speed = SwordManStat.getSpeed() * GameConfig.TILE_WIDTH;
        int hitRadius = (int) (SwordManStat.getHitRadius() * GameConfig.TILE_WIDTH);

        MonsterInfoComponent monsterInfoComponent = this.componentFactory.createMonsterInfoComponent(category, classs, weight, 1, gainEnergy, ability, null);
        PositionComponent positionComponent = this.componentFactory.createPositionComponent((int) pixelPos.x, (int) pixelPos.y);

        VelocityComponent velocityComponent = this.componentFactory.createVelocityComponent(speed, 0, null);
        CollisionComponent collisionComponent = this.componentFactory.createCollisionComponent(hitRadius, hitRadius);
        LifeComponent lifeComponent = this.componentFactory.createLifeComponent(SwordManStat.getHp());

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

    public EntityECS createAssassinMonster(Point pixelPos, EntityMode mode) throws Exception {
        int typeID = GameConfig.ENTITY_ID.ASSASSIN;
        EntityECS entity = this._createEntity(typeID, mode);
//        this.pool.push(entity);
//        this.entityManager.addEntity(entity);

        MonsterConfigItem assassinStat = MonsterConfig.INS.getMonsterConfig(MonsterConfig.ASSASSIN);
        String category = assassinStat.getCategory();
        String classs = assassinStat.getMonsterClass();
        int weight = (int) assassinStat.getWeight();
        int gainEnergy = assassinStat.getGainEnergy();
        int ability = assassinStat.getAbility();
        double speed = assassinStat.getSpeed() * GameConfig.TILE_WIDTH;
        int hitRadius = (int) (assassinStat.getHitRadius() * GameConfig.TILE_WIDTH);


        MonsterInfoComponent monsterInfoComponent = this.componentFactory.createMonsterInfoComponent(category, classs, weight, 1, gainEnergy, ability, null);
        PositionComponent positionComponent = this.componentFactory.createPositionComponent((int) pixelPos.x, (int) pixelPos.y);

        VelocityComponent velocityComponent = this.componentFactory.createVelocityComponent(speed, 0, null);
        CollisionComponent collisionComponent = this.componentFactory.createCollisionComponent(hitRadius, hitRadius);
        LifeComponent lifeComponent = this.componentFactory.createLifeComponent(assassinStat.getHp());

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

    public EntityECS createBatMonster(Point pixelPos, EntityMode mode) throws Exception {
        int typeID = GameConfig.ENTITY_ID.BAT;
        EntityECS entity = this._createEntity(typeID, mode);
//        this.pool.push(entity);
//        this.entityManager.addEntity(entity);

        MonsterConfigItem batMonsterStat = MonsterConfig.INS.getMonsterConfig(MonsterConfig.BAT);
        String category = batMonsterStat.getCategory();
        String classs = batMonsterStat.getMonsterClass();
        int weight = (int) batMonsterStat.getWeight();
        int gainEnergy = batMonsterStat.getGainEnergy();
        int ability = batMonsterStat.getAbility();
        double speed = batMonsterStat.getSpeed() * GameConfig.TILE_WIDTH;
        int hitRadius = (int) (batMonsterStat.getHitRadius() * GameConfig.TILE_WIDTH);

        MonsterInfoComponent monsterInfoComponent = this.componentFactory.createMonsterInfoComponent(category, classs, weight, 1, gainEnergy, ability, null);
        PositionComponent positionComponent = this.componentFactory.createPositionComponent((int) pixelPos.x, (int) pixelPos.y);

        VelocityComponent velocityComponent = this.componentFactory.createVelocityComponent(speed, 0, null);
        CollisionComponent collisionComponent = this.componentFactory.createCollisionComponent(hitRadius, hitRadius);
        LifeComponent lifeComponent = this.componentFactory.createLifeComponent(batMonsterStat.getHp());

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

        return entity;
    }

    public EntityECS createGiantMonster(Point pixelPos, EntityMode mode) throws Exception {
        int typeID = GameConfig.ENTITY_ID.GIANT;
        EntityECS entity = this._createEntity(typeID, mode);
//        this.pool.push(entity);
//        this.entityManager.addEntity(entity);

        MonsterConfigItem giant = MonsterConfig.INS.getMonsterConfig(MonsterConfig.GIANT);
        String category = giant.getCategory();
        String classs = giant.getMonsterClass();
        int weight = (int) giant.getWeight();
        int gainEnergy = giant.getGainEnergy();
        int ability = giant.getAbility();
        double speed = giant.getSpeed() * GameConfig.TILE_WIDTH;
        int hitRadius = (int) (giant.getHitRadius() * GameConfig.TILE_WIDTH);

        MonsterInfoComponent monsterInfoComponent = this.componentFactory.createMonsterInfoComponent(category, classs, weight, 1, gainEnergy, ability, null);
        PositionComponent positionComponent = this.componentFactory.createPositionComponent((int) pixelPos.x, (int) pixelPos.y);

        VelocityComponent velocityComponent = this.componentFactory.createVelocityComponent(speed, 0, null);
        CollisionComponent collisionComponent = this.componentFactory.createCollisionComponent(hitRadius, hitRadius);
        LifeComponent lifeComponent = this.componentFactory.createLifeComponent(giant.getHp());

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

    public EntityECS createNinjaMonster(Point pixelPos, EntityMode mode) throws Exception {
        int typeID = GameConfig.ENTITY_ID.NINJA;
        EntityECS entity = this._createEntity(typeID, mode);
//        this.pool.push(entity);
//        this.entityManager.addEntity(entity);

        MonsterConfigItem ninjaMonsterStat = MonsterConfig.INS.getMonsterConfig(MonsterConfig.NINJA);
        String category = ninjaMonsterStat.getCategory();
        String classs = ninjaMonsterStat.getMonsterClass();
        int weight = (int) ninjaMonsterStat.getWeight();
        int gainEnergy = ninjaMonsterStat.getGainEnergy();
        int ability = ninjaMonsterStat.getAbility();
        double speed = ninjaMonsterStat.getSpeed() * GameConfig.TILE_WIDTH;
        int hitRadius = (int) (ninjaMonsterStat.getHitRadius() * GameConfig.TILE_WIDTH);

        MonsterInfoComponent monsterInfoComponent = this.componentFactory.createMonsterInfoComponent(category, classs, weight, 1, gainEnergy, ability, null);
        PositionComponent positionComponent = this.componentFactory.createPositionComponent((int) pixelPos.x, (int) pixelPos.y);

        VelocityComponent velocityComponent = this.componentFactory.createVelocityComponent(speed, 0, null);
        CollisionComponent collisionComponent = this.componentFactory.createCollisionComponent(hitRadius, hitRadius);
        LifeComponent lifeComponent = this.componentFactory.createLifeComponent(ninjaMonsterStat.getHp());
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

        return entity;
    }

    //Create Boss
    public EntityECS createDemonTreeBoss(Point pixelPos, EntityMode mode) throws Exception {
        int typeID = GameConfig.ENTITY_ID.DEMON_TREE;
        EntityECS entity = this._createEntity(typeID, mode);
//        this.pool.push(entity);
//        this.entityManager.addEntity(entity);

        MonsterConfigItem demonTreeStat = MonsterConfig.INS.getMonsterConfig(MonsterConfig.DEMON_TREE);
        String category = demonTreeStat.getCategory();
        String classs = demonTreeStat.getMonsterClass();
        int weight = (int) demonTreeStat.getWeight();
        int gainEnergy = demonTreeStat.getGainEnergy();
        int ability = demonTreeStat.getAbility();
        double speed = demonTreeStat.getSpeed() * GameConfig.TILE_WIDTH;
        int hitRadius = (int) (demonTreeStat.getHitRadius() * GameConfig.TILE_WIDTH);

        MonsterInfoComponent monsterInfoComponent = this.componentFactory.createMonsterInfoComponent(category, classs, weight, 5, gainEnergy, ability, null);
        PositionComponent positionComponent = this.componentFactory.createPositionComponent((int) pixelPos.x, (int) pixelPos.y);

        VelocityComponent velocityComponent = this.componentFactory.createVelocityComponent(speed, 0, null);
        CollisionComponent collisionComponent = this.componentFactory.createCollisionComponent(hitRadius, hitRadius);
        LifeComponent lifeComponent = this.componentFactory.createLifeComponent(demonTreeStat.getHp());
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
        EntityECS entity = this._createEntity(typeID, mode);
//        this.pool.push(entity);
//        this.entityManager.addEntity(entity);

        MonsterConfigItem demonTreeMinionStat = MonsterConfig.INS.getMonsterConfig(MonsterConfig.DEMON_TREE_MINION);
        String category = demonTreeMinionStat.getCategory();
        String classs = demonTreeMinionStat.getMonsterClass();
        int weight = (int) demonTreeMinionStat.getWeight();
        int gainEnergy = demonTreeMinionStat.getGainEnergy();
        int ability = demonTreeMinionStat.getAbility();
        double speed = demonTreeMinionStat.getSpeed() * GameConfig.TILE_WIDTH;
        int hitRadius = (int) (demonTreeMinionStat.getHitRadius() * GameConfig.TILE_WIDTH);

        MonsterInfoComponent monsterInfoComponent = this.componentFactory.createMonsterInfoComponent(category, classs, weight, 1, gainEnergy, ability, null);
        PositionComponent positionComponent = this.componentFactory.createPositionComponent((int) pixelPos.x, (int) pixelPos.y);

        VelocityComponent velocityComponent = this.componentFactory.createVelocityComponent(speed, 0, null);
        CollisionComponent collisionComponent = this.componentFactory.createCollisionComponent(hitRadius, hitRadius);
        LifeComponent lifeComponent = this.componentFactory.createLifeComponent(demonTreeMinionStat.getHp());
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
        EntityECS entity = this._createEntity(typeID, mode);
//        this.pool.push(entity);
//        this.entityManager.addEntity(entity);

        MonsterConfigItem darkGiantStat = MonsterConfig.INS.getMonsterConfig(MonsterConfig.DARK_GIANT);
        String category = darkGiantStat.getCategory();
        String classs = darkGiantStat.getMonsterClass();
        int weight = (int) darkGiantStat.getWeight();
        int gainEnergy = darkGiantStat.getGainEnergy();
        int ability = darkGiantStat.getAbility();
        double speed = darkGiantStat.getSpeed() * GameConfig.TILE_WIDTH;
        int hitRadius = (int) (darkGiantStat.getHitRadius() * GameConfig.TILE_WIDTH);

        MonsterInfoComponent monsterInfoComponent = this.componentFactory.createMonsterInfoComponent(category, classs, weight, 5, gainEnergy, ability, null);
        PositionComponent positionComponent = this.componentFactory.createPositionComponent((int) pixelPos.x, (int) pixelPos.y);

        VelocityComponent velocityComponent = this.componentFactory.createVelocityComponent(speed, 0, null);
        CollisionComponent collisionComponent = this.componentFactory.createCollisionComponent(hitRadius, hitRadius);
        LifeComponent lifeComponent = this.componentFactory.createLifeComponent(darkGiantStat.getHp());
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
        EntityECS entity = this._createEntity(typeID, mode);
//        this.pool.push(entity);
//        this.entityManager.addEntity(entity);

        MonsterConfigItem satyrStat = MonsterConfig.INS.getMonsterConfig(MonsterConfig.SATYR);
        String category = satyrStat.getCategory();
        String classs = satyrStat.getMonsterClass();
        int weight = (int) satyrStat.getWeight();
        int gainEnergy = satyrStat.getGainEnergy();
        int ability = satyrStat.getAbility();
        double speed = satyrStat.getSpeed() * GameConfig.TILE_WIDTH;
        int hitRadius = (int) (satyrStat.getHitRadius() * GameConfig.TILE_WIDTH);

        MonsterInfoComponent monsterInfoComponent = this.componentFactory.createMonsterInfoComponent(category, classs, weight, 5, gainEnergy, ability, null);
        PositionComponent positionComponent = this.componentFactory.createPositionComponent((int) pixelPos.x, (int) pixelPos.y);

        VelocityComponent velocityComponent = this.componentFactory.createVelocityComponent(speed, 0, null);
        CollisionComponent collisionComponent = this.componentFactory.createCollisionComponent(hitRadius, hitRadius);
        LifeComponent lifeComponent = this.componentFactory.createLifeComponent(satyrStat.getHp());

        HealingAbilityComponent healingAbilityComponent = this.componentFactory.createHealingAbilityComponent(2 * GameConfig.TILE_WIDTH, 0.03);
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
        String breakPoint = "";
        User user = BitZeroServer.getInstance().getUserManager().getUserById(battle.user1.getId());
        short level = 1;
        TowerConfigItem towerConfigItem = TowerConfig.INS.getTowerConfig(TowerConfig.OWL);
        String targetType = "", archType = "", bulletType = "";
        int energy = 0;
        try {
            //Debug
            breakPoint = "breakpoint1";
            targetType = towerConfigItem.getTargetType();
            breakPoint = "breakpoint2";
            archType = towerConfigItem.getArchetype();
            breakPoint = "breakpoint3";
            bulletType = towerConfigItem.getBulletType();
            breakPoint = "breakpoint4";
            energy = towerConfigItem.getEnergy();
            breakPoint = "breakpoint5";

        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            ExtensionUtility.getExtension().send(new ResponseError((short) 0, breakPoint + " " + exceptionAsString), user);
        }


        TowerStat towerStat = towerConfigItem.getStat().get(level);

        double attackRange = towerStat.getRange() * GameConfig.TILE_WIDTH;
        double bulletSpeed = towerStat.getBulletSpeed() * (GameConfig.TILE_WIDTH / 10.0);
        double attackSpeed = towerStat.getAttackSpeed() / 1000.0;
        double bulletRadius = towerStat.getBulletRadius() * GameConfig.TILE_WIDTH;
        double damage = towerStat.getDamage();

        Point pixelPos = Utils.tile2Pixel(tilePos.x, tilePos.y, mode);


        TowerInfoComponent towerInfoComponent = this.componentFactory.createTowerInfoComponent(energy, "bulletTargetType", archType, targetType, bulletType);
        PositionComponent positionComponent = this.componentFactory.createPositionComponent(pixelPos.x, pixelPos.y);
        AttackComponent attackComponent = this.componentFactory.createAttackComponent(damage, GameConfig.TOWER_TARGET_STRATEGY.MAX_HP, attackRange, attackSpeed, 0, null, bulletSpeed, bulletRadius);

        entity.addComponent(towerInfoComponent);
        entity.addComponent(positionComponent);
        entity.addComponent(attackComponent);

        this.battle.minusPlayerEnergy(towerInfoComponent.getEnergy(), mode);
        return entity;
    }

    public EntityECS createIceGunPolarBearTower(Point tilePos, EntityMode mode) throws Exception {
        int typeID = GameConfig.ENTITY_ID.BEAR_TOWER;
        EntityECS entity = this._createEntity(typeID, mode);

        short level = 1;
        TowerConfigItem bearIceGunConfig = TowerConfig.INS.getTowerConfig(TowerConfig.POLAR_BEAR);

        String targetType = bearIceGunConfig.getTargetType();
        String archType = bearIceGunConfig.getArchetype();
        String bulletType = bearIceGunConfig.getBulletType();
        int energy = bearIceGunConfig.getEnergy();

        TowerStat towerStat = bearIceGunConfig.getStat().get(level);
        double attackRange = towerStat.getRange() * GameConfig.TILE_WIDTH;
        double bulletSpeed = towerStat.getBulletSpeed() * (GameConfig.TILE_WIDTH / 10.0);
        double attackSpeed = towerStat.getAttackSpeed() / 1000.0;
        double bulletRadius = towerStat.getBulletRadius() * GameConfig.TILE_WIDTH;
        double damage = towerStat.getDamage();


        Point pixelPos = Utils.tile2Pixel(tilePos.x, tilePos.y, mode);

        double frozenDuration = TargetBuffConfig.INS.getTargetBuffConfig(TargetBuffConfig.BULLET_ICE_GUN).getDuration().get(level) / 1000.0;
        FrozenEffect frozenEffect = this.componentFactory.createFrozenEffect(frozenDuration);

        List<EffectComponent> effectList = Arrays.asList(frozenEffect);
        TowerInfoComponent towerInfoComponent = this.componentFactory.createTowerInfoComponent(energy, "bulletTargetType", archType, targetType, bulletType);
        PositionComponent positionComponent = this.componentFactory.createPositionComponent(pixelPos.x, pixelPos.y);
        AttackComponent attackComponent = this.componentFactory.createAttackComponent(damage, GameConfig.TOWER_TARGET_STRATEGY.MAX_HP, attackRange, attackSpeed, 0, effectList, bulletSpeed, bulletRadius);
        attackComponent.setCanTargetAirMonster(false);

        entity.addComponent(towerInfoComponent);
        entity.addComponent(positionComponent);
        entity.addComponent(attackComponent);

        this.battle.minusPlayerEnergy(towerInfoComponent.getEnergy(), mode);

        return entity;
    }

    public EntityECS createFrogTower(Point tilePos, EntityMode mode) throws Exception {
        int typeID = GameConfig.ENTITY_ID.FROG_TOWER;
        EntityECS entity = this._createEntity(typeID, mode);

        short level = 1;
        TowerConfigItem towerConfigItem = TowerConfig.INS.getTowerConfig(TowerConfig.FROG);

        String targetType = towerConfigItem.getTargetType();
        String archType = towerConfigItem.getArchetype();
        String bulletType = towerConfigItem.getBulletType();
        int energy = towerConfigItem.getEnergy();

        TowerStat towerStat = towerConfigItem.getStat().get(level);
        double attackRange = towerStat.getRange() * GameConfig.TILE_WIDTH;
        double bulletSpeed = towerStat.getBulletSpeed() * (GameConfig.TILE_WIDTH / 10.0);
        double attackSpeed = towerStat.getAttackSpeed() / 1000.0;
        double bulletRadius = towerStat.getBulletRadius() * GameConfig.TILE_WIDTH;
        double damage = towerStat.getDamage();

        Point pixelPos = Utils.tile2Pixel(tilePos.x, tilePos.y, mode);

        TowerInfoComponent towerInfoComponent = this.componentFactory.createTowerInfoComponent(energy, "bulletTargetType", archType, targetType, bulletType);
        PositionComponent positionComponent = this.componentFactory.createPositionComponent(pixelPos.x, pixelPos.y);
        AttackComponent attackComponent = this.componentFactory.createAttackComponent(damage, GameConfig.TOWER_TARGET_STRATEGY.MAX_HP, attackRange, attackSpeed, 0, null, bulletSpeed, bulletRadius);


        entity.addComponent(towerInfoComponent);
        entity.addComponent(positionComponent);
        entity.addComponent(attackComponent);

        this.battle.minusPlayerEnergy(towerInfoComponent.getEnergy(), mode);

        return entity;
    }

    public EntityECS createBunnyOilGunTower(Point tilePos, EntityMode mode) throws Exception {
        int typeID = GameConfig.ENTITY_ID.BUNNY_TOWER;
        EntityECS entity = this._createEntity(typeID, mode);

        short level = 1;
        TowerConfigItem towerConfigItem = TowerConfig.INS.getTowerConfig(TowerConfig.BUNNY);

        String targetType = towerConfigItem.getTargetType();
        String archType = towerConfigItem.getArchetype();
        String bulletType = towerConfigItem.getBulletType();
        int energy = towerConfigItem.getEnergy();

        TowerStat towerStat = towerConfigItem.getStat().get(level);
        double attackRange = towerStat.getRange() * GameConfig.TILE_WIDTH;
        double bulletSpeed = towerStat.getBulletSpeed() * (GameConfig.TILE_WIDTH / 10.0);
        double attackSpeed = towerStat.getAttackSpeed() / 1000.0;
        double bulletRadius = towerStat.getBulletRadius() * GameConfig.TILE_WIDTH;
        double damage = towerStat.getDamage();
        TargetBuffConfigItem targetBuffConfigItem = TargetBuffConfig.INS.getTargetBuffConfig(TargetBuffConfig.BULLET_OIL_GUN);

        Point pixelPos = Utils.tile2Pixel(tilePos.x, tilePos.y, mode);
        double duration = targetBuffConfigItem.getDuration().get(level) / 1000.0;
        double slowPercentage = targetBuffConfigItem.getEffects().get(level).get(0).getValue();
        SlowEffect slowEffect = this.componentFactory.createSlowEffect(duration, slowPercentage);
        List<EffectComponent> effectList = Collections.singletonList(slowEffect);

        TowerInfoComponent towerInfoComponent = this.componentFactory.createTowerInfoComponent(energy, "bulletTargetType", archType, targetType, bulletType);
        PositionComponent positionComponent = this.componentFactory.createPositionComponent(pixelPos.x, pixelPos.y);
        AttackComponent attackComponent = this.componentFactory.createAttackComponent(damage, GameConfig.TOWER_TARGET_STRATEGY.MAX_HP, attackRange, attackSpeed, 0, effectList, bulletSpeed, bulletRadius);

        entity.addComponent(towerInfoComponent);
        entity.addComponent(positionComponent);
        entity.addComponent(attackComponent);

        this.battle.minusPlayerEnergy(towerInfoComponent.getEnergy(), mode);

        return entity;
    }

    public EntityECS createWizardTower(Point tilePos, EntityMode mode) throws Exception {
        int typeID = GameConfig.ENTITY_ID.WIZARD_TOWER;
        EntityECS entity = this._createEntity(typeID, mode);

        short level = 1;
        TowerConfigItem towerConfigItem = TowerConfig.INS.getTowerConfig(TowerConfig.WIZARD);

        String targetType = towerConfigItem.getTargetType();
        String archType = towerConfigItem.getArchetype();
        String bulletType = towerConfigItem.getBulletType();
        int energy = towerConfigItem.getEnergy();

        TowerStat towerStat = towerConfigItem.getStat().get(level);
        double attackRange = towerStat.getRange() * GameConfig.TILE_WIDTH;
        double bulletSpeed = towerStat.getBulletSpeed() * (GameConfig.TILE_WIDTH / 10.0);
        double attackSpeed = towerStat.getAttackSpeed() / 1000.0;
        double bulletRadius = towerStat.getBulletRadius() * GameConfig.TILE_WIDTH;
        double damage = towerStat.getDamage();

        Point pixelPos = Utils.tile2Pixel(tilePos.x, tilePos.y, mode);
        TowerInfoComponent towerInfoComponent = this.componentFactory.createTowerInfoComponent(energy, "bulletTargetType", archType, targetType, bulletType);
        PositionComponent positionComponent = this.componentFactory.createPositionComponent(pixelPos.x, pixelPos.y);
        AttackComponent attackComponent = this.componentFactory.createAttackComponent(damage, GameConfig.TOWER_TARGET_STRATEGY.MAX_HP, attackRange, attackSpeed, 0, null, bulletSpeed, bulletRadius);
        attackComponent.setCanTargetAirMonster(false);

        entity.addComponent(towerInfoComponent)
                .addComponent(positionComponent)
                .addComponent(attackComponent);

        this.battle.minusPlayerEnergy(towerInfoComponent.getEnergy(), mode);
        return entity;
    }

    public EntityECS createSnakeAttackSpeedTower(Point tilePos, EntityMode mode) throws Exception {
        int typeID = GameConfig.ENTITY_ID.SNAKE_TOWER;
        EntityECS entity = this._createEntity(typeID, mode);

        short level = 1;
        TowerConfigItem snakeAttackSpeedConfig = TowerConfig.INS.getTowerConfig(TowerConfig.SNAKE);
        ;

        String targetType = snakeAttackSpeedConfig.getTargetType();
        String archType = snakeAttackSpeedConfig.getArchetype();

        int energy = snakeAttackSpeedConfig.getEnergy();

        TowerStat towerStat = snakeAttackSpeedConfig.getStat().get(level);
        double attackRange = towerStat.getRange() * GameConfig.TILE_WIDTH;

        Point pixelPos = Utils.tile2Pixel(tilePos.x, tilePos.y, mode);

        double buffAttackSpeedEffectPercentage = TowerBuffConfig.INS.getTowerBuffConfig(TowerBuffConfig.ATTACK_SPEED_AURA).getEffects().get(level).get(0).getValue();

        TowerInfoComponent towerInfoComponent = this.componentFactory.createTowerInfoComponent(energy, "bulletTargetType", archType, targetType, "none");
        PositionComponent positionComponent = this.componentFactory.createPositionComponent(pixelPos.x, pixelPos.y);
        BuffAttackSpeedEffect buffAttackSpeedEffect = this.componentFactory.createBuffAttackSpeedEffect(buffAttackSpeedEffectPercentage);
        TowerAbilityComponent towerAbilityComponent = this.componentFactory.createTowerAbilityComponent(attackRange, buffAttackSpeedEffect);
        entity.addComponent(towerInfoComponent)
                .addComponent(positionComponent)
                .addComponent(towerAbilityComponent);

        this.battle.minusPlayerEnergy(towerInfoComponent.getEnergy(), mode);

        return entity;
    }

    public EntityECS createGoatAttackDamageTower(Point tilePos, EntityMode mode) throws Exception {
        int typeID = GameConfig.ENTITY_ID.GOAT_TOWER;
        EntityECS entity = this._createEntity(typeID, mode);

        short level = 1;
        TowerConfigItem goatDamageConfig = TowerConfig.INS.getTowerConfig(TowerConfig.GOAT);

        String targetType = goatDamageConfig.getTargetType();
        String archType = goatDamageConfig.getArchetype();

        int energy = goatDamageConfig.getEnergy();

        TowerStat towerStat = goatDamageConfig.getStat().get(level);
        double attackRange = towerStat.getRange() * GameConfig.TILE_WIDTH;
        double buffAttackDamageEffectPercentage = TowerBuffConfig.INS.getTowerBuffConfig(TowerBuffConfig.ATTACK_AURA).getEffects().get(level).get(0).getValue();

        Point pixelPos = Utils.tile2Pixel(tilePos.x, tilePos.y, mode);


        TowerInfoComponent towerInfoComponent = this.componentFactory.createTowerInfoComponent(energy, "bulletTargetType", archType, targetType, "none");
        PositionComponent positionComponent = this.componentFactory.createPositionComponent(pixelPos.x, pixelPos.y);
        BuffAttackDamageEffect buffAttackDamageEffect = this.componentFactory.createBuffAttackDamageEffect(buffAttackDamageEffectPercentage);
        TowerAbilityComponent towerAbilityComponent = this.componentFactory.createTowerAbilityComponent(attackRange, buffAttackDamageEffect);
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

        DamageEffect damageEffect = this.componentFactory.createDamageEffect(5);
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

    public void onUpgradeTower(long entityId, int towerLevel) throws Exception {
        System.out.println("[Battle.java line 456] onUpgradeTower: " + entityId + " " + towerLevel);
        short level = (short) towerLevel;
        EntityECS entity = this.entityManager.getEntity(entityId);
        TowerInfoComponent towerInfoComponent = (TowerInfoComponent) entity.getComponent(TowerInfoComponent.typeID);
        towerInfoComponent.setLevel(level);
        switch (entity.getTypeID()) {
            case GameConfig.ENTITY_ID.CANNON_TOWER: {
                AttackComponent attackComponent = (AttackComponent) entity.getComponent(AttackComponent.typeID);
                TowerConfigItem towerConfigItem = TowerConfig.INS.getTowerConfig((short) entity.getTypeID());
                TowerStat towerStat = towerConfigItem.getStat().get(level);
                double attackRange = towerStat.getRange() * GameConfig.TILE_WIDTH;
                double bulletSpeed = towerStat.getBulletSpeed() * (GameConfig.TILE_WIDTH / 10.0);
                double attackSpeed = towerStat.getAttackSpeed() / 1000.0;
                double bulletRadius = towerStat.getBulletRadius() * GameConfig.TILE_WIDTH;
                double damage = towerStat.getDamage();
                List<EffectComponent> effectComponents = new ArrayList<>();
                attackComponent.updateAttackStatistic(damage, attackRange, attackSpeed, effectComponents, bulletSpeed, bulletRadius);
                if (towerLevel == GameConfig.TOWER_MAX_LEVEL) {
                    FrozenEffect frozenEffect = componentFactory.createFrozenEffect(0.2);
                    attackComponent.addEffect(frozenEffect);
                }
                break;
            }
            case GameConfig.ENTITY_ID.FROG_TOWER: {
                AttackComponent attackComponent = (AttackComponent) entity.getComponent(AttackComponent.typeID);
                TowerConfigItem towerConfigItem = TowerConfig.INS.getTowerConfig((short) entity.getTypeID());
                TowerStat towerStat = towerConfigItem.getStat().get(level);
                double attackRange = towerStat.getRange() * GameConfig.TILE_WIDTH;
                double bulletSpeed = towerStat.getBulletSpeed() * (GameConfig.TILE_WIDTH / 10.0);
                double attackSpeed = towerStat.getAttackSpeed() / 1000.0;
                double bulletRadius = towerStat.getBulletRadius() * GameConfig.TILE_WIDTH;
                double damage = towerStat.getDamage();
                List<EffectComponent> effectComponents = new ArrayList<>();
                attackComponent.updateAttackStatistic(damage, attackRange, attackSpeed, effectComponents, bulletSpeed, bulletRadius);
                if (towerLevel == GameConfig.TOWER_MAX_LEVEL) {
                    FrogBulletSkillComponent frogBulletSkillComponent = componentFactory.createFrogBulletSkillComponent(1.5);
                    attackComponent.addEffect(frogBulletSkillComponent);
                }
                break;
            }
            case GameConfig.ENTITY_ID.WIZARD_TOWER: {
                AttackComponent attackComponent = (AttackComponent) entity.getComponent(AttackComponent.typeID);
                TowerConfigItem towerConfigItem = TowerConfig.INS.getTowerConfig((short) entity.getTypeID());
                TowerStat towerStat = towerConfigItem.getStat().get(level);
                double attackRange = towerStat.getRange() * GameConfig.TILE_WIDTH;
                double bulletSpeed = towerStat.getBulletSpeed() * (GameConfig.TILE_WIDTH / 10.0);
                double attackSpeed = towerStat.getAttackSpeed() / 1000.0;
                double bulletRadius = towerStat.getBulletRadius() * GameConfig.TILE_WIDTH;
                double damage = towerStat.getDamage();
                List<EffectComponent> effectComponents = new ArrayList<>();
                attackComponent.updateAttackStatistic(damage, attackRange, attackSpeed, effectComponents, bulletSpeed, bulletRadius);
                if (towerLevel == GameConfig.TOWER_MAX_LEVEL) {
                    WizardBulletSkillComponent wizardBulletSkillComponent = componentFactory.createWizardBulletSkillComponent(5, 10);
                    attackComponent.addEffect(wizardBulletSkillComponent);
                }
                break;
            }
            case GameConfig.ENTITY_ID.BEAR_TOWER: {
                AttackComponent attackComponent = (AttackComponent) entity.getComponent(AttackComponent.typeID);
                TowerConfigItem towerConfigItem = TowerConfig.INS.getTowerConfig((short) entity.getTypeID());
                TowerStat towerStat = towerConfigItem.getStat().get(level);
                double attackRange = towerStat.getRange() * GameConfig.TILE_WIDTH;
                double bulletSpeed = towerStat.getBulletSpeed() * (GameConfig.TILE_WIDTH / 10.0);
                double attackSpeed = towerStat.getAttackSpeed() / 1000.0;
                double bulletRadius = towerStat.getBulletRadius() * GameConfig.TILE_WIDTH;
                double damage = towerStat.getDamage();

                TargetBuffConfigItem targetBuffConfigItem = TargetBuffConfig.INS.getTargetBuffConfig(TargetBuffConfig.BULLET_OIL_GUN);
                double duration = targetBuffConfigItem.getDuration().get(level) / 1000.0;

                FrozenEffect frozenEffect = this.componentFactory.createFrozenEffect(duration);

                List<EffectComponent> effectComponents = Arrays.asList(frozenEffect);

                attackComponent.updateAttackStatistic(damage, attackRange, attackSpeed, effectComponents, bulletSpeed, bulletRadius);
                if (towerLevel == GameConfig.TOWER_MAX_LEVEL) {
                    DamageAmplifyComponent damageAmplifyComponent = componentFactory.createDamageAmplifyComponent(1.5);
                    attackComponent.addEffect(damageAmplifyComponent);
                }
                break;
            }
            case GameConfig.ENTITY_ID.BUNNY_TOWER: {
                AttackComponent attackComponent = (AttackComponent) entity.getComponent(AttackComponent.typeID);
                TowerConfigItem towerConfigItem = TowerConfig.INS.getTowerConfig((short) entity.getTypeID());
                TowerStat towerStat = towerConfigItem.getStat().get(level);
                double attackRange = towerStat.getRange() * GameConfig.TILE_WIDTH;
                double bulletSpeed = towerStat.getBulletSpeed() * (GameConfig.TILE_WIDTH / 10.0);
                double attackSpeed = towerStat.getAttackSpeed() / 1000.0;
                double bulletRadius = towerStat.getBulletRadius() * GameConfig.TILE_WIDTH;
                double damage = towerStat.getDamage();

                TargetBuffConfigItem targetBuffConfigItem = TargetBuffConfig.INS.getTargetBuffConfig(TargetBuffConfig.BULLET_OIL_GUN);
                double duration = targetBuffConfigItem.getDuration().get(level) / 1000.0;
                double slowPercentage = targetBuffConfigItem.getEffects().get(level).get(0).getValue();
                SlowEffect slowEffect = this.componentFactory.createSlowEffect(duration, slowPercentage);
                List<EffectComponent> effectComponents = Arrays.asList(slowEffect);
                attackComponent.updateAttackStatistic(damage, attackRange, attackSpeed, effectComponents, bulletSpeed, bulletRadius);
                if (towerLevel == GameConfig.TOWER_MAX_LEVEL) {
                    PoisonEffect poisonEffect = componentFactory.createPoisonEffect(2, 3);
                    attackComponent.addEffect(poisonEffect);
                }
                break;
            }
            case GameConfig.ENTITY_ID.GOAT_TOWER: {
                TowerAbilityComponent towerAbilityComponent = (TowerAbilityComponent) entity.getComponent(TowerAbilityComponent.typeID);
                TowerConfigItem goatDamageConfig = TowerConfig.INS.getTowerConfig(TowerConfig.GOAT);

                TowerStat towerStat = goatDamageConfig.getStat().get(level);
                double attackRange = towerStat.getRange() * GameConfig.TILE_WIDTH;
                double buffAttackDamageEffectPercentage = TowerBuffConfig.INS.getTowerBuffConfig(TowerBuffConfig.ATTACK_AURA).getEffects().get(level).get(0).getValue();

                EffectComponent buffAttackDamageEffect = this.componentFactory.createBuffAttackDamageEffect(buffAttackDamageEffectPercentage);
                towerAbilityComponent.reset(attackRange, buffAttackDamageEffect);

                if (towerLevel == GameConfig.TOWER_MAX_LEVEL) {
                    GoatSlowAuraComponent goatSlowAuraComponent = componentFactory.createGoatSlowAuraComponent(0.2, attackRange);
                    entity.addComponent(goatSlowAuraComponent);
                }
                break;
            }
            case GameConfig.ENTITY_ID.SNAKE_TOWER: {
                TowerAbilityComponent towerAbilityComponent = (TowerAbilityComponent) entity.getComponent(TowerAbilityComponent.typeID);
                TowerConfigItem snakeAttackSpeedConfig = TowerConfig.INS.getTowerConfig(TowerConfig.SNAKE);

                TowerStat towerStat = snakeAttackSpeedConfig.getStat().get(level);
                double attackRange = towerStat.getRange() * GameConfig.TILE_WIDTH;
                double buffAttackSpeedEffectPercentage = TowerBuffConfig.INS.getTowerBuffConfig(TowerBuffConfig.ATTACK_SPEED_AURA).getEffects().get(level).get(0).getValue();

                EffectComponent buffAttackSpeedEffect = this.componentFactory.createBuffAttackSpeedEffect(buffAttackSpeedEffectPercentage);
                towerAbilityComponent.reset(attackRange, buffAttackSpeedEffect);

                if (towerLevel == GameConfig.TOWER_MAX_LEVEL) {
                    SnakeBurnHpAuraComponent snakeBurnHpAuraComponent = componentFactory.createSnakeBurnHpAuraComponent(0.01, 5, attackRange);
                    entity.addComponent(snakeBurnHpAuraComponent);
                }
                break;
            }
        }
    }

}

