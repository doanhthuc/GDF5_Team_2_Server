package battle.Factory;

import battle.Common.Point;
import battle.Component.Component.CollisionComponent;
import battle.Component.Component.PathComponent;
import battle.Component.Component.PositionComponent;
import battle.Component.Component.VelocityComponent;
import battle.Component.EffectComponent.EffectComponent;
import battle.Component.InfoComponent.BulletInfoComponent;
import battle.Component.InfoComponent.LifeComponent;
import battle.Component.InfoComponent.MonsterInfoComponent;
import battle.Component.InfoComponent.TowerInfoComponent;
import battle.Config.GameConfig;
import battle.Manager.ComponentManager;
import battle.Pool.ComponentPool;
import bitzero.core.P;

import java.util.ArrayList;

public class ComponentFactory {
    ComponentPool pool = new ComponentPool();
    private static ComponentFactory _instance = null;

    public BulletInfoComponent createBulletInfoComponent(ArrayList<EffectComponent> effects, int type) {
        BulletInfoComponent bulletInfoComponent = (BulletInfoComponent) this.pool.checkOut(GameConfig.COMPONENT_ID.BULLET_INFO);
        if (bulletInfoComponent != null) {
            bulletInfoComponent.reset(effects, type);
        } else {
            bulletInfoComponent = new BulletInfoComponent(effects, type);
            ComponentManager.getInstance().add(bulletInfoComponent);
        }
        return bulletInfoComponent;
    }

    public PositionComponent createPositionComponent(int x, int y) {
        PositionComponent positionComponent = (PositionComponent) this.pool.checkOut(GameConfig.COMPONENT_ID.POSITION);
        if (positionComponent != null) {
            positionComponent.reset(x, y);
        } else {
            positionComponent = new PositionComponent(x, y);
            ComponentManager.getInstance().add(positionComponent);
        }
        return positionComponent;
    }

    public VelocityComponent createVelocityComponent(double speedX, double speedY, Point targetPosition) {
        VelocityComponent velocityComponent = (VelocityComponent) this.pool.checkOut(GameConfig.COMPONENT_ID.VELOCITY);
        if (velocityComponent != null) {
            velocityComponent.reset(speedX, speedY, targetPosition);
        } else {
            velocityComponent = new VelocityComponent(speedX, speedY, targetPosition);
            ComponentManager.getInstance().add(velocityComponent);
        }
        return velocityComponent;
    }

    public CollisionComponent createCollisionComponent(double width, double height) {
        CollisionComponent collisionComponent = (CollisionComponent) this.pool.checkOut(GameConfig.COMPONENT_ID.COLLISION);
        if (collisionComponent != null) {
            collisionComponent.reset(width, height);
        } else {
            collisionComponent = new CollisionComponent(width, height);
            ComponentManager.getInstance().add(collisionComponent);
        }
        return collisionComponent;
    }

    public PathComponent createPathComponent(ArrayList<Point> path) {
        PathComponent pathComponent = (PathComponent) this.pool.checkOut(GameConfig.COMPONENT_ID.PATH);
        if (pathComponent != null) {
            pathComponent.reset(path);
        } else {
            pathComponent = new PathComponent(path);
            ComponentManager.getInstance().add(pathComponent);
        }
        return pathComponent;
    }

    public MonsterInfoComponent createMonsterInfoComponent(String category, String classs, int weight, int energy, int gainEnergy, int ability, EffectComponent effect) {
        MonsterInfoComponent monsterInfoComponent = (MonsterInfoComponent) this.pool.checkOut(GameConfig.COMPONENT_ID.MONSTER_INFO);
        if (monsterInfoComponent != null) {
            monsterInfoComponent.reset(category, classs, weight, energy, gainEnergy, ability, effect);
        } else {
            monsterInfoComponent = new MonsterInfoComponent(category, classs, weight, energy, gainEnergy, ability, effect);
            ComponentManager.getInstance().add(monsterInfoComponent);
        }
        return monsterInfoComponent;
    }

    public LifeComponent createLifeComponent(double hp) {
        LifeComponent lifeComponent = (LifeComponent) this.pool.checkOut(GameConfig.COMPONENT_ID.LIFE);
        if (lifeComponent != null) {
            lifeComponent.reset(hp);
        } else {
            lifeComponent = new LifeComponent(hp);
            ComponentManager.getInstance().add(lifeComponent);
        }
        return lifeComponent;
    }

    public TowerInfoComponent createTowerInfoComponent(){
        return null;
    }
    public static ComponentFactory getInstance() {
        if (_instance == null) _instance = new ComponentFactory();
        return _instance;
    }


}