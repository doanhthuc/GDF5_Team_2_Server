package battle.factory;

import battle.common.UUIDGeneratorECS;
import battle.manager.SystemManager;
import battle.system.*;

public class SystemFactory {
    private SystemManager systemManager;
    private UUIDGeneratorECS uuid;

    public SystemFactory(SystemManager systemManager, UUIDGeneratorECS uuid) {
        this.systemManager = systemManager;
        this.uuid = uuid;
    }

    public AbilitySystem createAbilitySystem() throws Exception {
        AbilitySystem system = new AbilitySystem(this.uuid.genSystemID());
        this.systemManager.add(system);
        return system;
    }

    public AttackSystem createAttackSystem() throws Exception {
        AttackSystem system = new AttackSystem(this.uuid.genSystemID());
        this.systemManager.add(system);
        return system;
    }

    public BulletSystem createBulletSystem() throws Exception {
        BulletSystem system = new BulletSystem(this.uuid.genSystemID());
        this.systemManager.add(system);
        return system;
    }

    public CollisionSystem createCollisionSystem() throws Exception {
        CollisionSystem system = new CollisionSystem(this.uuid.genSystemID());
        this.systemManager.add(system);
        return system;
    }

    public EffectSystem createEffectSystem() throws Exception {
        EffectSystem system = new EffectSystem(this.uuid.genSystemID());
        this.systemManager.add(system);
        return system;
    }

    public LifeSystem createLifeSystem() throws Exception {
        LifeSystem system = new LifeSystem(this.uuid.genSystemID());
        this.systemManager.add(system);
        return system;
    }

    public MonsterSystem createMonsterSystem() throws Exception {
        MonsterSystem system = new MonsterSystem(this.uuid.genSystemID());
        this.systemManager.add(system);
        return system;
    }

    public MovementSystem createMovementSystem() throws Exception {
        MovementSystem system = new MovementSystem(this.uuid.genSystemID());
        this.systemManager.add(system);
        return system;
    }

    public PathMonsterSystem createPathSystem() throws Exception {
        PathMonsterSystem system = new PathMonsterSystem(this.uuid.genSystemID());
        this.systemManager.add(system);
        return system;
    }

    public ResetSystem createResetSystem() throws Exception {
        ResetSystem system = new ResetSystem(this.uuid.genSystemID());
        this.systemManager.add(system);
        return system;
    }

    public SpellSystem createSpellSystem() throws Exception {
        SpellSystem system = new SpellSystem(this.uuid.genSystemID());
        this.systemManager.add(system);
        return system;
    }

    public TowerSpecialSkillSystem createTowerSpecialSkillSystem() throws Exception {
        TowerSpecialSkillSystem system = new TowerSpecialSkillSystem(this.uuid.genSystemID());
        this.systemManager.add(system);
        return system;
    }
}
