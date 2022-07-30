package battle.factory;

import battle.common.EntityMode;
import battle.common.Point;
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
    static EntityFactory _instance;
    public EntityPool pool = new EntityPool();

    public EntityECS _createEntity(int typeID, EntityMode mode) {
        EntityECS entity = null;
        if (entity == null) {
            entity = new EntityECS(typeID, mode);
            this.pool.push(entity);
            EntityManager.getInstance().addEntity(entity);
        }
        return entity;
    }

    public EntityECS createBullet(int towerType, PositionComponent startPosition, PositionComponent targetPosition, List<EffectComponent> effects, EntityMode mode) throws Exception {
        int typeID;
        EntityECS entity;
        BulletInfoComponent infoComponent;
        PositionComponent positionComponent;
        CollisionComponent collisionComponent;
        double bulletSpeed;
        switch (towerType) {
            case GameConfig.ENTITY_ID.CANNON_TOWER:
                typeID = GameConfig.ENTITY_ID.BULLET;
                entity = this._createEntity(typeID, mode);
                infoComponent = ComponentFactory.getInstance().createBulletInfoComponent(effects, "cannon", 0);
                collisionComponent = ComponentFactory.getInstance().createCollisionComponent(3, 3, 15, 15);
                bulletSpeed = 5 * GameConfig.TILE_WIDTH;
                Point speed = Utils.getInstance().calculateVelocityVector(startPosition.getPos(), targetPosition.getPos(), bulletSpeed);
                VelocityComponent velocityComponent = ComponentFactory.getInstance().createVelocityComponent(speed.x, speed.y, targetPosition);
                positionComponent = ComponentFactory.getInstance().createPositionComponent(startPosition.getX(), startPosition.getY());

                entity.addComponent(infoComponent);
                entity.addComponent(positionComponent);
                entity.addComponent(velocityComponent);
                entity.addComponent(collisionComponent);
                return entity;

            case GameConfig.ENTITY_ID.BEAR_TOWER:
                typeID = GameConfig.ENTITY_ID.BULLET;
                entity = this._createEntity(typeID, mode);
                bulletSpeed = 4 * GameConfig.TILE_WIDTH;
                speed = Utils.getInstance().calculateVelocityVector(startPosition.getPos(), targetPosition.getPos(), bulletSpeed);

                infoComponent = ComponentFactory.getInstance().createBulletInfoComponent(effects, "bear", 0);
                collisionComponent = ComponentFactory.getInstance().createCollisionComponent(5, 5, 5, 5);
                positionComponent = ComponentFactory.getInstance().createPositionComponent(startPosition.getX(), startPosition.getY());
                velocityComponent = ComponentFactory.getInstance().createVelocityComponent(speed.x, speed.y, targetPosition);

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

                PathComponent pathComponent = ComponentFactory.getInstance().createPathComponent(path, mode, false);

                //create velocityComponent
                bulletSpeed = 4 * GameConfig.TILE_WIDTH;
                speed = Utils.getInstance().calculateVelocityVector(startPosition.getPos(), targetPosition.getPos(), bulletSpeed);
                velocityComponent = ComponentFactory.getInstance().createVelocityComponent(speed.x, speed.y);

                //OtherComponent
                infoComponent = ComponentFactory.getInstance().createBulletInfoComponent(effects, "frog", 0);
                collisionComponent = ComponentFactory.getInstance().createCollisionComponent(20, 20, 20, 20);
                positionComponent = ComponentFactory.getInstance().createPositionComponent(startPosition.getX(), startPosition.getY());

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
                bulletSpeed = 4 * GameConfig.TILE_WIDTH;
                speed = Utils.getInstance().calculateVelocityVector(startPosition.getPos(), targetPosition.getPos(), bulletSpeed);

                infoComponent = ComponentFactory.getInstance().createBulletInfoComponent(effects, "bunny", GameConfig.TILE_WIDTH);
                collisionComponent = ComponentFactory.getInstance().createCollisionComponent(5, 5, 5, 5);
                positionComponent = ComponentFactory.getInstance().createPositionComponent(startPosition.getX(), startPosition.getY());
                velocityComponent = ComponentFactory.getInstance().createVelocityComponent(speed.x, speed.y, null, new Point(targetPosition.getX(), targetPosition.getY()));
                entity.addComponent(infoComponent);
                entity.addComponent(positionComponent);
                entity.addComponent(velocityComponent);
                entity.addComponent(collisionComponent);
                return entity;
            case GameConfig.ENTITY_ID.WIZARD_TOWER:
                typeID = GameConfig.ENTITY_ID.BULLET;
                entity = this._createEntity(typeID, mode);
                bulletSpeed = 3 * GameConfig.TILE_WIDTH;
                speed = Utils.getInstance().calculateVelocityVector(startPosition.getPos(), targetPosition.getPos(), bulletSpeed);

                infoComponent = ComponentFactory.getInstance().createBulletInfoComponent(effects, "wizard", GameConfig.TILE_WIDTH);
                collisionComponent = ComponentFactory.getInstance().createCollisionComponent(5, 5, 5, 5);
                positionComponent = ComponentFactory.getInstance().createPositionComponent(startPosition.getX(), startPosition.getY());
                velocityComponent = ComponentFactory.getInstance().createVelocityComponent(speed.x, speed.y, null, new Point(targetPosition.getX(), targetPosition.getY()));
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
        EntityManager.getInstance().addEntity(entity);

        MonsterInfoComponent monsterInfoComponent = ComponentFactory.getInstance().createMonsterInfoComponent("normal", "land", 30, 1, 1, null, null);
        PositionComponent positionComponent = ComponentFactory.getInstance().createPositionComponent((int) pixelPos.x, (int) pixelPos.y);

        VelocityComponent velocityComponent = ComponentFactory.getInstance().createVelocityComponent(0.8 * GameConfig.TILE_WIDTH, 0, null);
        CollisionComponent collisionComponent = ComponentFactory.getInstance().createCollisionComponent(20, 30);
        LifeComponent lifeComponent = ComponentFactory.getInstance().createLifeComponent(500);

        // FrozenEffect frozenEffect= ComponentFactory.getInstance().createFrozenEffect();
        //Point tilePos = Utils.getInstance().pixel2Tile(pixelPos.x, pixelPos.y, mode);
        //ToDo: find shortest Path with TilePos
        PathComponent pathComponent = ComponentFactory.getInstance().createPathComponent(this.fakeGroundMonsterTilePath(mode), mode, true);

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
        EntityManager.getInstance().addEntity(entity);

        MonsterInfoComponent monsterInfoComponent = ComponentFactory.getInstance().createMonsterInfoComponent("normal", "land", 15, 1, 1, null, null);
        PositionComponent positionComponent = ComponentFactory.getInstance().createPositionComponent((int) pixelPos.x, (int) pixelPos.y);

        VelocityComponent velocityComponent = ComponentFactory.getInstance().createVelocityComponent(1.4 * GameConfig.TILE_WIDTH, 0, null);
        CollisionComponent collisionComponent = ComponentFactory.getInstance().createCollisionComponent(20, 30);
        LifeComponent lifeComponent = ComponentFactory.getInstance().createLifeComponent(120);

        // FrozenEffect frozenEffect= ComponentFactory.getInstance().createFrozenEffect();
        //Point tilePos = Utils.getInstance().pixel2Tile(pixelPos.x, pixelPos.y, mode);
        //ToDo: find shortest Path with TilePos
        PathComponent pathComponent = ComponentFactory.getInstance().createPathComponent(this.fakeGroundMonsterTilePath(mode), mode, true);

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
        EntityManager.getInstance().addEntity(entity);

        MonsterInfoComponent monsterInfoComponent = ComponentFactory.getInstance().createMonsterInfoComponent("normal", "air", 25, 1, 1, null, null);
        PositionComponent positionComponent = ComponentFactory.getInstance().createPositionComponent((int) pixelPos.x, (int) pixelPos.y);

        VelocityComponent velocityComponent = ComponentFactory.getInstance().createVelocityComponent(0.7 * GameConfig.TILE_WIDTH, 0, null);
        CollisionComponent collisionComponent = ComponentFactory.getInstance().createCollisionComponent(20, 30);
        LifeComponent lifeComponent = ComponentFactory.getInstance().createLifeComponent(140);

        // FrozenEffect frozenEffect= ComponentFactory.getInstance().createFrozenEffect();
        //Point tilePos = Utils.getInstance().pixel2Tile(pixelPos.x, pixelPos.y, mode);
        //ToDo: find shortest Path with TilePos
        PathComponent pathComponent = ComponentFactory.getInstance().createPathComponent(this.airMonsterPath(mode), mode, false);

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
        EntityManager.getInstance().addEntity(entity);

        MonsterInfoComponent monsterInfoComponent = ComponentFactory.getInstance().createMonsterInfoComponent("normal", "ground", 200, 1, 1, null, null);
        PositionComponent positionComponent = ComponentFactory.getInstance().createPositionComponent((int) pixelPos.x, (int) pixelPos.y);

        VelocityComponent velocityComponent = ComponentFactory.getInstance().createVelocityComponent(1 * GameConfig.TILE_WIDTH, 0, null);
        CollisionComponent collisionComponent = ComponentFactory.getInstance().createCollisionComponent(30, 30);
        LifeComponent lifeComponent = ComponentFactory.getInstance().createLifeComponent(820);

        // FrozenEffect frozenEffect= ComponentFactory.getInstance().createFrozenEffect();
        //Point tilePos = Utils.getInstance().pixel2Tile(pixelPos.x, pixelPos.y, mode);
        //ToDo: find shortest Path with TilePos
        PathComponent pathComponent = ComponentFactory.getInstance().createPathComponent(this.fakeGroundMonsterTilePath(mode), mode, true);

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
        EntityManager.getInstance().addEntity(entity);

        MonsterInfoComponent monsterInfoComponent = ComponentFactory.getInstance().createMonsterInfoComponent("normal", "ground", 30, 1, 1, null, null);
        PositionComponent positionComponent = ComponentFactory.getInstance().createPositionComponent((int) pixelPos.x, (int) pixelPos.y);

        VelocityComponent velocityComponent = ComponentFactory.getInstance().createVelocityComponent(0.5 * GameConfig.TILE_WIDTH, 0, null);
        CollisionComponent collisionComponent = ComponentFactory.getInstance().createCollisionComponent(20, 30);
        LifeComponent lifeComponent = ComponentFactory.getInstance().createLifeComponent(60);
        UnderGroundComponent underGroundComponent = ComponentFactory.getInstance().createUnderGroundComponent();
        // FrozenEffect frozenEffect= ComponentFactory.getInstance().createFrozenEffect();
        //Point tilePos = Utils.getInstance().pixel2Tile(pixelPos.x, pixelPos.y, mode);
        //ToDo: find shortest Path with TilePos
        PathComponent pathComponent = ComponentFactory.getInstance().createPathComponent(this.fakeGroundMonsterTilePath(mode), mode, true);

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
        EntityManager.getInstance().addEntity(entity);

        MonsterInfoComponent monsterInfoComponent = ComponentFactory.getInstance().createMonsterInfoComponent("boss", "land", 400, 10, 10, null, null);
        PositionComponent positionComponent = ComponentFactory.getInstance().createPositionComponent((int) pixelPos.x, (int) pixelPos.y);

        VelocityComponent velocityComponent = ComponentFactory.getInstance().createVelocityComponent(0.4 * GameConfig.TILE_WIDTH, 0, null);
        CollisionComponent collisionComponent = ComponentFactory.getInstance().createCollisionComponent(20, 30);
        LifeComponent lifeComponent = ComponentFactory.getInstance().createLifeComponent(10000);
        SpawnMinionComponent spawnMinionComponent = ComponentFactory.getInstance().createSpawnMinionComponent(2);

        //ToDo: find shortest Path with TilePos
        PathComponent pathComponent = ComponentFactory.getInstance().createPathComponent(fakeGroundMonsterTilePath(mode), mode, true);

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
        EntityManager.getInstance().addEntity(entity);

        MonsterInfoComponent monsterInfoComponent = ComponentFactory.getInstance().createMonsterInfoComponent("normal", "land", 50, 1, 1, null, null);
        PositionComponent positionComponent = ComponentFactory.getInstance().createPositionComponent((int) pixelPos.x, (int) pixelPos.y);

        VelocityComponent velocityComponent = ComponentFactory.getInstance().createVelocityComponent(0.8 * GameConfig.TILE_WIDTH, 0, null);
        CollisionComponent collisionComponent = ComponentFactory.getInstance().createCollisionComponent(20, 30);
        LifeComponent lifeComponent = ComponentFactory.getInstance().createLifeComponent(30);
        // FrozenEffect frozenEffect= ComponentFactory.getInstance().createFrozenEffect();
        //Point tilePos = Utils.getInstance().pixel2Tile(pixelPos.x, pixelPos.y, mode);
        //ToDo: find shortest Path with TilePos
        PathComponent pathComponent = ComponentFactory.getInstance().createPathComponent(fakeGroundMonsterTilePath(mode), mode, true);

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
        EntityManager.getInstance().addEntity(entity);

        MonsterInfoComponent monsterInfoComponent = ComponentFactory.getInstance().createMonsterInfoComponent("normal", "land", 500, 1, 1, null, null);
        PositionComponent positionComponent = ComponentFactory.getInstance().createPositionComponent((int) pixelPos.x, (int) pixelPos.y);

        VelocityComponent velocityComponent = ComponentFactory.getInstance().createVelocityComponent(0.4 * GameConfig.TILE_WIDTH, 0, null);
        CollisionComponent collisionComponent = ComponentFactory.getInstance().createCollisionComponent(40, 40);
        LifeComponent lifeComponent = ComponentFactory.getInstance().createLifeComponent(10000);
        // FrozenEffect frozenEffect= ComponentFactory.getInstance().createFrozenEffect();
        //Point tilePos = Utils.getInstance().pixel2Tile(pixelPos.x, pixelPos.y, mode);
        //ToDo: find shortest Path with TilePos
        Point tilePos = Utils.pixel2Tile(pixelPos.getX(), pixelPos.getY(), mode);
        PathComponent pathComponent = ComponentFactory.getInstance().createPathComponent(fakeGroundMonsterTilePath(mode), mode, true);

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
        EntityManager.getInstance().addEntity(entity);

        MonsterInfoComponent monsterInfoComponent = ComponentFactory.getInstance().createMonsterInfoComponent("boss", "land", 300, 1, 1, null, null);
        PositionComponent positionComponent = ComponentFactory.getInstance().createPositionComponent((int) pixelPos.x, (int) pixelPos.y);

        VelocityComponent velocityComponent = ComponentFactory.getInstance().createVelocityComponent(0.4 * GameConfig.TILE_WIDTH, 0, null);
        CollisionComponent collisionComponent = ComponentFactory.getInstance().createCollisionComponent(30, 30);
        LifeComponent lifeComponent = ComponentFactory.getInstance().createLifeComponent(10000);

        HealingAbilityComponent healingAbilityComponent = ComponentFactory.getInstance().createHealingAbilityComponent(2 * GameConfig.TILE_WIDTH, 100);
        // FrozenEffect frozenEffect= ComponentFactory.getInstance().createFrozenEffect();
        //Point tilePos = Utils.getInstance().pixel2Tile(pixelPos.x, pixelPos.y, mode);
        //ToDo: find shortest Path with TilePos
        PathComponent pathComponent = ComponentFactory.getInstance().createPathComponent(fakeGroundMonsterTilePath(mode), mode, true);

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

        TowerInfoComponent towerInfoComponent = ComponentFactory.getInstance().createTowerInfoComponent(10, "bulletTargetType", "attack", "monster", "bulletType");
        PositionComponent positionComponent = ComponentFactory.getInstance().createPositionComponent(pixelPos.x, pixelPos.y);
        AttackComponent attackComponent = ComponentFactory.getInstance().createAttackComponent(10, GameConfig.TOWER_TARGET_STRATEGY.MAX_HP, attackRange, 0.6, 0, null);

        entity.addComponent(towerInfoComponent);
        entity.addComponent(positionComponent);
        entity.addComponent(attackComponent);
        return entity;
    }

    public EntityECS createIceGunPolarBearTower(Point tilePos, EntityMode mode) throws Exception {
        int typeID = GameConfig.ENTITY_ID.BEAR_TOWER;
        EntityECS entity = this._createEntity(typeID, mode);

        double attackRange = 1.5 * GameConfig.TILE_WIDTH;
        Point pixelPos = Utils.tile2Pixel(tilePos.x, tilePos.y, mode);

        FrozenEffect frozenEffect = ComponentFactory.getInstance().createFrozenEffect(1.5);
        List<EffectComponent> effectList = Arrays.asList(frozenEffect);
        TowerInfoComponent towerInfoComponent = ComponentFactory.getInstance().createTowerInfoComponent(10, "bulletTargetType", "support", "monster", "bulletType");
        PositionComponent positionComponent = ComponentFactory.getInstance().createPositionComponent(pixelPos.x, pixelPos.y);
        AttackComponent attackComponent = ComponentFactory.getInstance().createAttackComponent(8, GameConfig.TOWER_TARGET_STRATEGY.MAX_HP, attackRange, 3.4, 0, effectList);

        entity.addComponent(towerInfoComponent);
        entity.addComponent(positionComponent);
        entity.addComponent(attackComponent);
        return entity;
    }

    public EntityECS createFrogTower(Point tilePos, EntityMode mode) throws Exception {
        int typeID = GameConfig.ENTITY_ID.FROG_TOWER;
        EntityECS entity = this._createEntity(typeID, mode);

        double attackRange = 2 * GameConfig.TILE_WIDTH;
        Point pixelPos = Utils.tile2Pixel(tilePos.x, tilePos.y, mode);

        TowerInfoComponent towerInfoComponent = ComponentFactory.getInstance().createTowerInfoComponent(10, "bulletTargetType", "attack", "monster", "bulletType");
        PositionComponent positionComponent = ComponentFactory.getInstance().createPositionComponent(pixelPos.x, pixelPos.y);
        AttackComponent attackComponent = ComponentFactory.getInstance().createAttackComponent(10, GameConfig.TOWER_TARGET_STRATEGY.MAX_HP, attackRange, 1.5, 0, null);

        entity.addComponent(towerInfoComponent);
        entity.addComponent(positionComponent);
        entity.addComponent(attackComponent);
        return entity;
    }

    public EntityECS createBunnyOilGunTower(Point tilePos, EntityMode mode) throws Exception {
        int typeID = GameConfig.ENTITY_ID.BUNNY_TOWER;
        EntityECS entity = this._createEntity(typeID, mode);

        double attackRange = 1.5 * GameConfig.TILE_WIDTH;
        Point pixelPos = Utils.tile2Pixel(tilePos.x, tilePos.y, mode);

        SlowEffect slowEffect = ComponentFactory.getInstance().createSlowEffect(5, 0.3);
        List<EffectComponent> effectList = Collections.singletonList(slowEffect);

        TowerInfoComponent towerInfoComponent = ComponentFactory.getInstance().createTowerInfoComponent(10, "bulletTargetType", "attack", "monster", "bulletType");
        PositionComponent positionComponent = ComponentFactory.getInstance().createPositionComponent(pixelPos.x, pixelPos.y);
        AttackComponent attackComponent = ComponentFactory.getInstance().createAttackComponent(5, GameConfig.TOWER_TARGET_STRATEGY.MAX_HP, attackRange, 2.5, 0, effectList);

        entity.addComponent(towerInfoComponent);
        entity.addComponent(positionComponent);
        entity.addComponent(attackComponent);
        return entity;
    }

    public EntityECS createWizardTower(Point tilePos, EntityMode mode) throws Exception {
        int typeID = GameConfig.ENTITY_ID.WIZARD_TOWER;
        EntityECS entity = this._createEntity(typeID, mode);

        double attackRange = 1.5 * GameConfig.TILE_WIDTH;
        Point pixelPos = Utils.tile2Pixel(tilePos.x, tilePos.y, mode);

        TowerInfoComponent towerInfoComponent = ComponentFactory.getInstance().createTowerInfoComponent(10, "bulletTargetType", "attack", "monster", "bulletType");
        PositionComponent positionComponent = ComponentFactory.getInstance().createPositionComponent(pixelPos.x, pixelPos.y);
        AttackComponent attackComponent = ComponentFactory.getInstance().createAttackComponent(30, GameConfig.TOWER_TARGET_STRATEGY.MAX_HP, attackRange, 1.2, 0, null);

        entity.addComponent(towerInfoComponent)
                .addComponent(positionComponent)
                .addComponent(attackComponent);
        return entity;
    }

    public EntityECS createSnakeAttackSpeedTower(Point tilePos, EntityMode mode) throws Exception {
        int typeID = GameConfig.ENTITY_ID.SNAKE_TOWER;
        EntityECS entity = this._createEntity(typeID, mode);

        Point pixelPos = Utils.tile2Pixel(tilePos.x, tilePos.y, mode);

        TowerInfoComponent towerInfoComponent = ComponentFactory.getInstance().createTowerInfoComponent(10, "bulletTargetType", "support", "aura", "none");
        PositionComponent positionComponent = ComponentFactory.getInstance().createPositionComponent(pixelPos.x, pixelPos.y);
        BuffAttackSpeedEffect buffAttackSpeedEffect = ComponentFactory.getInstance().createBuffAttackSpeedEffect(0.5);
        TowerAbilityComponent towerAbilityComponent = ComponentFactory.getInstance().createTowerAbilityComponent(1.5 * GameConfig.TILE_WIDTH, buffAttackSpeedEffect);
        entity.addComponent(towerInfoComponent)
                .addComponent(positionComponent)
                .addComponent(towerAbilityComponent);
        return entity;
    }

    public EntityECS createGoatAttackDamageTower(Point tilePos, EntityMode mode) throws Exception {
        int typeID = GameConfig.ENTITY_ID.GOAT_TOWER;
        EntityECS entity = this._createEntity(typeID, mode);

        Point pixelPos = Utils.tile2Pixel(tilePos.x, tilePos.y, mode);

        TowerInfoComponent towerInfoComponent = ComponentFactory.getInstance().createTowerInfoComponent(10, "bulletTargetType", "support", "aura", "none");
        PositionComponent positionComponent = ComponentFactory.getInstance().createPositionComponent(pixelPos.x, pixelPos.y);
        BuffAttackDamageEffect buffAttackDamageEffect = ComponentFactory.getInstance().createBuffAttackDamageEffect(1.5);
        TowerAbilityComponent towerAbilityComponent = ComponentFactory.getInstance().createTowerAbilityComponent(1.5 * GameConfig.TILE_WIDTH, buffAttackDamageEffect);
        entity.addComponent(towerInfoComponent)
                .addComponent(positionComponent)
                .addComponent(towerAbilityComponent);
        return entity;
    }

    // Create Spell

    public EntityECS createFireSpell(Point pixelPos, EntityMode mode) throws Exception {
        int typeID = GameConfig.ENTITY_ID.FIRE_SPELL;
        EntityECS entity = this._createEntity(typeID, mode);

        double S = 300;
        double V = 1000;
        double T = S / V;

        PositionComponent positionComponent = ComponentFactory.getInstance().createPositionComponent(pixelPos.getX(), pixelPos.getY() + S);

        Point speed = Utils.calculateVelocityVector(new Point(pixelPos.getX(), pixelPos.getY() + S), pixelPos, V);
        VelocityComponent velocityComponent = ComponentFactory.getInstance().createVelocityComponent(speed.getX(), speed.getY());

        DamageEffect damageEffect = ComponentFactory.getInstance().createDamageEffect(100);
        SpellInfoComponent spellInfoComponent = ComponentFactory.getInstance().createSpellInfoComponent(pixelPos, Arrays.asList(damageEffect), 1.2 * GameConfig.TILE_WIDTH, T);

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

        PositionComponent positionComponent = ComponentFactory.getInstance().createPositionComponent(pixelPos.getX(), pixelPos.getY() + S);
        Point speed = Utils.calculateVelocityVector(new Point(pixelPos.getX(), pixelPos.getY() + S), pixelPos, V);
        VelocityComponent velocityComponent = ComponentFactory.getInstance().createVelocityComponent(speed.getX(), speed.getY());

        DamageEffect damageEffect = ComponentFactory.getInstance().createDamageEffect(10);
        FrozenEffect frozenEffect = ComponentFactory.getInstance().createFrozenEffect(5);
        SpellInfoComponent spellInfoComponent = ComponentFactory.getInstance().createSpellInfoComponent(pixelPos, Arrays.asList(damageEffect, frozenEffect), 1.2 * GameConfig.TILE_WIDTH, T);

        entity.addComponent(positionComponent);
        entity.addComponent(velocityComponent);
        entity.addComponent(spellInfoComponent);
        return entity;
    }

    public EntityECS createTrapSpell(Point tilePos, EntityMode mode) throws Exception {
        int typeID = GameConfig.ENTITY_ID.TRAP_SPELL;
        EntityECS entity = this._createEntity(typeID, mode);

        Point pixelPos = Utils.tile2Pixel(tilePos.x, tilePos.y, mode);
        PositionComponent positionComponent = ComponentFactory.getInstance().createPositionComponent(pixelPos.getX(), pixelPos.getY());
        CollisionComponent collisionComponent = ComponentFactory.getInstance().createCollisionComponent(GameConfig.TILE_WIDTH, GameConfig.TILE_HEIGHT);
        TrapInfoComponent trapInfoComponent = ComponentFactory.getInstance().createTrapInfoComponent(0.3);
        entity.addComponent(positionComponent);
        entity.addComponent(trapInfoComponent);
        entity.addComponent(collisionComponent);
        return entity;
    }


    // Other Function
    public static EntityFactory getInstance() {
        if (_instance == null) _instance = new EntityFactory();
        return _instance;
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

