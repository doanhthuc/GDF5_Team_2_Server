package battle.factory;

import battle.Battle;
import battle.common.EntityMode;
import battle.common.Point;
import battle.component.common.*;
import battle.component.effect.*;
import battle.component.info.*;
import battle.config.GameConfig;
import battle.manager.ComponentManager;
import battle.pool.ComponentPool;

import java.util.List;

public class ComponentFactory {
    private ComponentManager componentManager;
    private ComponentPool pool;
    private Battle battle;
    public ComponentFactory(ComponentManager componentManager, ComponentPool pool, Battle battle) {
        this.componentManager = componentManager;
        this.pool = pool;
        this.battle = battle;
    }

    public BulletInfoComponent createBulletInfoComponent(List<EffectComponent> effects, String type, double radius) throws Exception {
        BulletInfoComponent bulletInfoComponent = (BulletInfoComponent) this.pool.checkOut(GameConfig.COMPONENT_ID.BULLET_INFO);
        if (bulletInfoComponent != null) {
            bulletInfoComponent.reset(effects, type, radius);
        } else {
            bulletInfoComponent = new BulletInfoComponent(effects, type, radius);
            bulletInfoComponent.setId(battle.getUuidGeneratorECS().genComponentID());
            this.componentManager.add(bulletInfoComponent);
        }
        return bulletInfoComponent;
    }

    public PositionComponent createPositionComponent(double x, double y) throws Exception {
        PositionComponent positionComponent = (PositionComponent) this.pool.checkOut(GameConfig.COMPONENT_ID.POSITION);
        if (positionComponent != null) {
            positionComponent.reset(x, y);
        } else {
            positionComponent = new PositionComponent(x, y);
            positionComponent.setId(battle.getUuidGeneratorECS().genComponentID());
            this.componentManager.add(positionComponent);
        }
        return positionComponent;
    }

    public VelocityComponent createVelocityComponent(double speedX, double speedY, PositionComponent dynamicPosition) throws Exception {
        VelocityComponent velocityComponent = (VelocityComponent) this.pool.checkOut(GameConfig.COMPONENT_ID.VELOCITY);
        if (velocityComponent != null) {
            velocityComponent.reset(speedX, speedY, dynamicPosition);
        } else {
            velocityComponent = new VelocityComponent(speedX, speedY, dynamicPosition);
            velocityComponent.setId(battle.getUuidGeneratorECS().genComponentID());
            this.componentManager.add(velocityComponent);
        }
        return velocityComponent;
    }

    public VelocityComponent createVelocityComponent(double speedX, double speedY, PositionComponent dynamicPosition, Point staticPostion) throws Exception {
        VelocityComponent velocityComponent = (VelocityComponent) this.pool.checkOut(GameConfig.COMPONENT_ID.VELOCITY);
        if (velocityComponent != null) {
            velocityComponent.reset(speedX, speedY, dynamicPosition, staticPostion);
        } else {
            velocityComponent = new VelocityComponent(speedX, speedY, dynamicPosition, staticPostion);
            velocityComponent.setId(battle.getUuidGeneratorECS().genComponentID());
            this.componentManager.add(velocityComponent);
        }
        return velocityComponent;
    }

    public VelocityComponent createVelocityComponent(double speedX, double speedY) throws Exception {
        VelocityComponent velocityComponent = (VelocityComponent) this.pool.checkOut(GameConfig.COMPONENT_ID.VELOCITY);
        if (velocityComponent != null) {
            velocityComponent.reset(speedX, speedY);
        } else {
            velocityComponent = new VelocityComponent(speedX, speedY);
            velocityComponent.setId(battle.getUuidGeneratorECS().genComponentID());
            this.componentManager.add(velocityComponent);
        }
        return velocityComponent;
    }

    public CollisionComponent createCollisionComponent(double width, double height, double originWidth, double originHeight) throws Exception {
        CollisionComponent collisionComponent = (CollisionComponent) this.pool.checkOut(GameConfig.COMPONENT_ID.COLLISION);
        if (collisionComponent != null) {
            collisionComponent.reset(width, height, originWidth, originHeight);
        } else {
            collisionComponent = new CollisionComponent(width, height, originWidth, originHeight);
            collisionComponent.setId(battle.getUuidGeneratorECS().genComponentID());
            this.componentManager.add(collisionComponent);
        }
        return collisionComponent;
    }

    public CollisionComponent createCollisionComponent(double width, double height) throws Exception {
        CollisionComponent collisionComponent = (CollisionComponent) this.pool.checkOut(GameConfig.COMPONENT_ID.COLLISION);
        if (collisionComponent != null) {
            collisionComponent.reset(width, height);
        } else {
            collisionComponent = new CollisionComponent(width, height);
            collisionComponent.setId(battle.getUuidGeneratorECS().genComponentID());
            this.componentManager.add(collisionComponent);
        }
        return collisionComponent;
    }

    public PathComponent createPathComponent(List<Point> path, EntityMode mode, boolean isConvert) throws Exception {
        PathComponent pathComponent = (PathComponent) this.pool.checkOut(GameConfig.COMPONENT_ID.PATH);
        if (pathComponent != null) {
            pathComponent.reset(path, mode, isConvert);
        } else {
            pathComponent = new PathComponent(path, mode, isConvert);
            pathComponent.setId(battle.getUuidGeneratorECS().genComponentID());
            this.componentManager.add(pathComponent);
        }
        return pathComponent;
    }

    public MonsterInfoComponent createMonsterInfoComponent(String category, String classs, int weight, int energy, int gainEnergy, int ability, List<EffectComponent> effect) throws Exception {
        MonsterInfoComponent monsterInfoComponent = (MonsterInfoComponent) this.pool.checkOut(GameConfig.COMPONENT_ID.MONSTER_INFO);
        if (monsterInfoComponent != null) {
            monsterInfoComponent.reset(category, classs, weight, energy, gainEnergy, ability, effect);
        } else {
            monsterInfoComponent = new MonsterInfoComponent(category, classs, weight, energy, gainEnergy, ability, effect);
            monsterInfoComponent.setId(battle.getUuidGeneratorECS().genComponentID());
            this.componentManager.add(monsterInfoComponent);
        }
        return monsterInfoComponent;
    }

    public LifeComponent createLifeComponent(double hp) throws Exception {
        LifeComponent lifeComponent = (LifeComponent) this.pool.checkOut(GameConfig.COMPONENT_ID.LIFE);
        if (lifeComponent != null) {
            lifeComponent.reset(hp);
        } else {
            lifeComponent = new LifeComponent(hp);
            lifeComponent.setId(battle.getUuidGeneratorECS().genComponentID());
            this.componentManager.add(lifeComponent);
        }
        return lifeComponent;
    }

    public TowerInfoComponent createTowerInfoComponent(int energy, String bulletTargetType, String archType, String targetType, String bulletType) throws Exception {
        TowerInfoComponent towerInfoComponent = (TowerInfoComponent) this.pool.checkOut(GameConfig.COMPONENT_ID.TOWER_INFO);

        if (towerInfoComponent != null) {
            towerInfoComponent.reset(energy, bulletTargetType, archType, targetType, bulletType);
        } else {
            towerInfoComponent = new TowerInfoComponent(energy, bulletTargetType, archType, targetType, bulletType);
            towerInfoComponent.setId(battle.getUuidGeneratorECS().genComponentID());
            this.componentManager.add(towerInfoComponent);
            // this.pool.checkIn(towerInfoComponent);
        }
        return towerInfoComponent;
    }

    public AttackComponent createAttackComponent(double damage, int targetStrategy, double range, double speed, double countdown, List<EffectComponent> effects, double bulletSpeed, double bulletRadius) throws Exception {
        AttackComponent attackComponent = (AttackComponent) this.pool.checkOut(GameConfig.COMPONENT_ID.ATTACK);
        if (attackComponent != null) {
            attackComponent.reset(damage, targetStrategy, range, speed, countdown, effects, bulletSpeed, bulletRadius);
        } else {
            attackComponent = new AttackComponent(damage, targetStrategy, range, speed, countdown, effects, bulletSpeed, bulletRadius);
            attackComponent.setId(battle.getUuidGeneratorECS().genComponentID());
            this.componentManager.add(attackComponent);
        }
        return attackComponent;
    }

    public HealingAbilityComponent createHealingAbilityComponent(double range, double healingRate) throws Exception {
        HealingAbilityComponent healingAbilityComponent = (HealingAbilityComponent) this.pool.checkOut(GameConfig.COMPONENT_ID.HEALING_ABILITY);
        if (healingAbilityComponent != null) {
            healingAbilityComponent.reset(range, healingRate);
        } else {
            healingAbilityComponent = new HealingAbilityComponent(range, healingRate);
            healingAbilityComponent.setId(battle.getUuidGeneratorECS().genComponentID());
            this.componentManager.add(healingAbilityComponent);
        }
        return healingAbilityComponent;
    }

    public UnderGroundComponent createUnderGroundComponent() throws Exception {
        UnderGroundComponent underGroundComponent = (UnderGroundComponent) this.pool.checkOut(GameConfig.COMPONENT_ID.UNDER_GROUND);
        if (underGroundComponent != null) {
            underGroundComponent.reset();
        } else {
            underGroundComponent = new UnderGroundComponent();
            underGroundComponent.setId(battle.getUuidGeneratorECS().genComponentID());
            this.componentManager.add(underGroundComponent);
        }
        return underGroundComponent;
    }

    public SpawnMinionComponent createSpawnMinionComponent(double period) throws Exception {
        SpawnMinionComponent spawnMinionComponent = (SpawnMinionComponent) this.pool.checkOut(GameConfig.COMPONENT_ID.SPAWN_MINION);
        if (spawnMinionComponent != null) {
            spawnMinionComponent.reset(period);
        } else {
            spawnMinionComponent = new SpawnMinionComponent(period);
            spawnMinionComponent.setId(battle.getUuidGeneratorECS().genComponentID());
            this.componentManager.add(spawnMinionComponent);
        }
        return spawnMinionComponent;
    }

    public SpellInfoComponent createSpellInfoComponent(Point position, List<EffectComponent> effects, double range, double countdown) throws Exception {
        SpellInfoComponent spellInfoComponent = (SpellInfoComponent) this.pool.checkOut(SpellInfoComponent.typeID);
        if (spellInfoComponent != null) {
            spellInfoComponent.reset(position, effects, range, countdown);
        } else {
            spellInfoComponent = new SpellInfoComponent(position, effects, range, countdown);
            spellInfoComponent.setId(battle.getUuidGeneratorECS().genComponentID());
            this.componentManager.add(spellInfoComponent);
        }
        return spellInfoComponent;
    }

    public TowerAbilityComponent createTowerAbilityComponent(double range, EffectComponent effect) throws Exception {
        TowerAbilityComponent towerAbilityComponent = (TowerAbilityComponent) this.pool.checkOut(GameConfig.COMPONENT_ID.TOWER_ABILITY);
        if (towerAbilityComponent != null) {
            towerAbilityComponent.reset(range, effect);
        } else {
            towerAbilityComponent = new TowerAbilityComponent(range, effect);
            towerAbilityComponent.setId(battle.getUuidGeneratorECS().genComponentID());
            this.componentManager.add(towerAbilityComponent);
        }
        return towerAbilityComponent;
    }

    public DamageEffect createDamageEffect(double damage) throws Exception {
        DamageEffect damageEffect = (DamageEffect) this.pool.checkOut(DamageEffect.typeID);
        if (damageEffect != null) {
            damageEffect.reset(damage);
        } else {
            damageEffect = new DamageEffect(damage);
            damageEffect.setId(battle.getUuidGeneratorECS().genComponentID());
            this.componentManager.add(damageEffect);
        }
        return damageEffect;
    }

    public TrapInfoComponent createTrapInfoComponent(double delayTrigger) throws Exception {
        TrapInfoComponent trapInfoComponent = (TrapInfoComponent) this.pool.checkOut(TrapInfoComponent.typeID);
        if (trapInfoComponent != null) {
            trapInfoComponent.reset(delayTrigger);
        } else {
            trapInfoComponent = new TrapInfoComponent(delayTrigger);
            trapInfoComponent.setId(battle.getUuidGeneratorECS().genComponentID());
            this.componentManager.add(trapInfoComponent);
        }
        return trapInfoComponent;
    }

    public TrapEffect createTrapEffect() throws Exception {
        TrapEffect trapEffect = (TrapEffect) this.pool.checkOut(TrapEffect.typeID);
        if (trapEffect != null) {
            trapEffect.reset();
        } else {
            trapEffect = new TrapEffect();
            trapEffect.setId(battle.getUuidGeneratorECS().genComponentID());
            this.componentManager.add(trapEffect);
        }
        return trapEffect;
    }


    public SlowEffect createSlowEffect(double duration, double percent) throws Exception {
        SlowEffect slowEffect = (SlowEffect) this.pool.checkOut(SlowEffect.typeID);
        if (slowEffect != null) {
            slowEffect.reset(duration, percent);
        } else {
            slowEffect = new SlowEffect(duration, percent);
            slowEffect.setId(battle.getUuidGeneratorECS().genComponentID());
            this.componentManager.add(slowEffect);
        }
        return slowEffect;
    }

    public FrozenEffect createFrozenEffect(double duration) throws Exception {
        FrozenEffect frozenEffect = (FrozenEffect) this.pool.checkOut(FrozenEffect.typeID);
        if (frozenEffect != null) {
            frozenEffect.reset(duration);
        } else {
            frozenEffect = new FrozenEffect(duration);
            frozenEffect.setId(battle.getUuidGeneratorECS().genComponentID());
            this.componentManager.add(frozenEffect);
        }
        return frozenEffect;
    }

    public BuffAttackRangeEffect createBuffAttackRangeEffect(double percent) throws Exception {
        BuffAttackRangeEffect buffAttackRangeEffect = (BuffAttackRangeEffect) this.pool.checkOut(BuffAttackRangeEffect.typeID);
        if (buffAttackRangeEffect != null) {
            buffAttackRangeEffect.reset(percent);
        } else {
            buffAttackRangeEffect = new BuffAttackRangeEffect(percent);
            buffAttackRangeEffect.setId(battle.getUuidGeneratorECS().genComponentID());
            this.componentManager.add(buffAttackRangeEffect);
        }
        return buffAttackRangeEffect;
    }

    public BuffAttackSpeedEffect createBuffAttackSpeedEffect(double percent) throws Exception {
        BuffAttackSpeedEffect buffAttackSpeedEffect = (BuffAttackSpeedEffect) this.pool.checkOut(BuffAttackSpeedEffect.typeID);
        if (buffAttackSpeedEffect != null) {
            buffAttackSpeedEffect.reset(percent);
        } else {
            buffAttackSpeedEffect = new BuffAttackSpeedEffect(percent);
            buffAttackSpeedEffect.setId(battle.getUuidGeneratorECS().genComponentID());
            this.componentManager.add(buffAttackSpeedEffect);
        }
        return buffAttackSpeedEffect;
    }

    public BuffAttackDamageEffect createBuffAttackDamageEffect(double percent) throws Exception {
        BuffAttackDamageEffect buffAttackDamageEffect = (BuffAttackDamageEffect) this.pool.checkOut(BuffAttackDamageEffect.typeID);
        if (buffAttackDamageEffect != null) {
            buffAttackDamageEffect.reset(percent);
        } else {
            buffAttackDamageEffect = new BuffAttackDamageEffect(percent);
            buffAttackDamageEffect.setId(battle.getUuidGeneratorECS().genComponentID());
            this.componentManager.add(buffAttackDamageEffect);
        }
        return buffAttackDamageEffect;
    }

    public FireBallEffect createFireBallEffect(double a, double maxDuration, Point startPos, Point endPos, double v0) throws Exception {
        FireBallEffect fireBallEffect = (FireBallEffect) this.pool.checkOut(FireBallEffect.typeID);
        if (fireBallEffect != null) {
            fireBallEffect.reset(a, maxDuration, startPos, endPos, v0);
        } else {
            fireBallEffect = new FireBallEffect(a, maxDuration, startPos, endPos, v0);
            fireBallEffect.setId(battle.getUuidGeneratorECS().genComponentID());
            this.componentManager.add(fireBallEffect);
        }
        return fireBallEffect;
    }
}
