package battle.factory;

import battle.common.Point;
import battle.component.Component.*;
import battle.component.EffectComponent.EffectComponent;
import battle.component.InfoComponent.BulletInfoComponent;
import battle.component.InfoComponent.LifeComponent;
import battle.component.InfoComponent.MonsterInfoComponent;
import battle.component.InfoComponent.TowerInfoComponent;
import battle.config.GameConfig;
import battle.manager.ComponentManager;
import battle.pool.ComponentPool;

import java.util.ArrayList;
import java.util.List;

public class ComponentFactory {
    private static ComponentFactory _instance = null;
    ComponentPool pool = new ComponentPool();

    public static ComponentFactory getInstance() {
        if (_instance == null) _instance = new ComponentFactory();
        return _instance;
    }

    public BulletInfoComponent createBulletInfoComponent(List<EffectComponent> effects, int type) throws Exception {
        BulletInfoComponent bulletInfoComponent = (BulletInfoComponent) this.pool.checkOut(GameConfig.COMPONENT_ID.BULLET_INFO);
        if (bulletInfoComponent != null) {
            bulletInfoComponent.reset(effects, type);
        } else {
            bulletInfoComponent = new BulletInfoComponent(effects, type);
            ComponentManager.getInstance().add(bulletInfoComponent);
        }
        return bulletInfoComponent;
    }

    public PositionComponent createPositionComponent(int x, int y) throws Exception {
        PositionComponent positionComponent = (PositionComponent) this.pool.checkOut(GameConfig.COMPONENT_ID.POSITION);
        if (positionComponent != null) {
            positionComponent.reset(x, y);
        } else {
            positionComponent = new PositionComponent(x, y);
            ComponentManager.getInstance().add(positionComponent);
        }
        return positionComponent;
    }

    public VelocityComponent createVelocityComponent(double speedX, double speedY, Point targetPosition) throws Exception {
        VelocityComponent velocityComponent = (VelocityComponent) this.pool.checkOut(GameConfig.COMPONENT_ID.VELOCITY);
        if (velocityComponent != null) {
            velocityComponent.reset(speedX, speedY, targetPosition);
        } else {
            velocityComponent = new VelocityComponent(speedX, speedY, targetPosition);
            ComponentManager.getInstance().add(velocityComponent);
        }
        return velocityComponent;
    }

    public CollisionComponent createCollisionComponent(double width, double height) throws Exception {
        CollisionComponent collisionComponent = (CollisionComponent) this.pool.checkOut(GameConfig.COMPONENT_ID.COLLISION);
        if (collisionComponent != null) {
            collisionComponent.reset(width, height);
        } else {
            collisionComponent = new CollisionComponent(width, height);
            ComponentManager.getInstance().add(collisionComponent);
        }
        return collisionComponent;
    }

    public PathComponent createPathComponent(ArrayList<Point> path) throws Exception {
        PathComponent pathComponent = (PathComponent) this.pool.checkOut(GameConfig.COMPONENT_ID.PATH);
        if (pathComponent != null) {
            pathComponent.reset(path);
        } else {
            pathComponent = new PathComponent(path);
            ComponentManager.getInstance().add(pathComponent);
        }
        return pathComponent;
    }

    public MonsterInfoComponent createMonsterInfoComponent(String category, String classs, int weight, int energy, int gainEnergy, int ability, EffectComponent effect) throws Exception {
        MonsterInfoComponent monsterInfoComponent = (MonsterInfoComponent) this.pool.checkOut(GameConfig.COMPONENT_ID.MONSTER_INFO);
        if (monsterInfoComponent != null) {
            monsterInfoComponent.reset(category, classs, weight, energy, gainEnergy, ability, effect);
        } else {
            monsterInfoComponent = new MonsterInfoComponent(category, classs, weight, energy, gainEnergy, ability, effect);
            ComponentManager.getInstance().add(monsterInfoComponent);
        }
        return monsterInfoComponent;
    }

    public LifeComponent createLifeComponent(double hp) throws Exception {
        LifeComponent lifeComponent = (LifeComponent) this.pool.checkOut(GameConfig.COMPONENT_ID.LIFE);
        if (lifeComponent != null) {
            lifeComponent.reset(hp);
        } else {
            lifeComponent = new LifeComponent(hp);
            ComponentManager.getInstance().add(lifeComponent);
        }
        return lifeComponent;
    }

    public TowerInfoComponent createTowerInfoComponent(int energy, String bulletTargetType, String archType, String targetType, String bulletType) throws Exception {
        TowerInfoComponent towerInfoComponent = (TowerInfoComponent) this.pool.checkOut(GameConfig.COMPONENT_ID.TOWER_INFO);

        if (towerInfoComponent != null) {
            towerInfoComponent.reset(energy, bulletTargetType, archType, targetType, bulletType);
        } else {
            towerInfoComponent = new TowerInfoComponent(energy, bulletTargetType, archType, targetType, bulletType);
            ComponentManager.getInstance().add(towerInfoComponent);
        }
        return towerInfoComponent;
    }

    public AttackComponent createAttackComponent(int damage, int targetStrategy, double range, double speed, double countdown, List<EffectComponent> effects) throws Exception {
        AttackComponent attackComponent = (AttackComponent) this.pool.checkOut(GameConfig.COMPONENT_ID.ATTACK);
        if (attackComponent != null) {
            attackComponent.reset(damage, targetStrategy, range, speed, countdown, effects);
        } else {
            attackComponent = new AttackComponent(damage, targetStrategy, range, speed, countdown, effects);
            ComponentManager.getInstance().add(attackComponent);
        }
        return attackComponent;
    }


}
