package battle.system;

import battle.Battle;
import battle.common.Point;
import battle.common.Utils;
import battle.component.common.AttackComponent;
import battle.component.common.Component;
import battle.component.common.PositionComponent;
import battle.component.effect.BuffAttackDamageEffect;
import battle.component.effect.BuffAttackSpeedEffect;
import battle.component.effect.TowerAbilityComponent;
import battle.component.info.LifeComponent;
import battle.component.info.MonsterInfoComponent;
import battle.component.info.SpellInfoComponent;
import battle.component.info.TowerInfoComponent;
import battle.component.towerskill.SnakeBurnHpAuraComponent;
import battle.config.GameConfig;
import battle.entity.EntityECS;
import battle.map.BattleMap;
import battle.newMap.BattleMapObject;
import battle.newMap.TileObject;
import battle.newMap.Tower;

import java.util.Map;

public class SnapshotSystem extends SystemECS {
    private final static String SYSTEM_NAME = "SnapshotSystem";

    public SnapshotSystem(long id) {
        super(GameConfig.SYSTEM_ID.SNAPSHOT_SYSTEM, SYSTEM_NAME, id);
    }

    @Override
    public void run(Battle battle) throws Exception {
    }

    @Override
    public boolean checkEntityCondition(EntityECS entity, Component component) {
        return component.getTypeID() == TowerInfoComponent.typeID || component.getTypeID() == MonsterInfoComponent.typeID;
    }
}