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
import battle.entity.EntityECS;
import battle.map.BattleMap;
import battle.newMap.BattleMapObject;
import battle.newMap.TileObject;
import battle.newMap.Tower;

import java.util.Map;

public class TowerSpecialSkillSystem extends SystemECS {
    public TowerSpecialSkillSystem(int typeId, String name, long systemId) {
        super(typeId, name, systemId);
    }
    private final int[] direction = {0, -1, 0, 1, 0};

    @Override
    public void run(Battle battle) throws Exception {
        this.tick = this.getElapseTime();
        this._handleSnakeSpecialSkill(tick, battle);
        this._handleBuffAbility(tick, battle);
    }

    public void _handleSnakeSpecialSkill(double tick, Battle battle) {
        for (Map.Entry<Long, EntityECS> mapElementTower : this.getEntityStore().entrySet()) {
            EntityECS tower = mapElementTower.getValue();
            if (!tower._hasComponent(SnakeBurnHpAuraComponent.typeID)) continue;

            SnakeBurnHpAuraComponent snakeBurnHpAuraComponent = (SnakeBurnHpAuraComponent) tower.getComponent(SnakeBurnHpAuraComponent.typeID);

            for (Map.Entry<Long, EntityECS> mapElementMonster : this.getEntityStore().entrySet()) {
                EntityECS monster = mapElementMonster.getValue();
                if (!monster._hasComponent(LifeComponent.typeID)) continue;
                if (!monster._hasComponent(PositionComponent.typeID)) continue;
                if (tower.getId() == monster.getId() || tower.getMode() != monster.getMode()) continue;

                if (Utils._distanceFrom(tower, monster) <= snakeBurnHpAuraComponent.getRange()) {
                    LifeComponent lifeComponent = (LifeComponent) monster.getComponent(LifeComponent.typeID);
                    double lostHp = Math.min(snakeBurnHpAuraComponent.getBurnRate() * lifeComponent.getMaxHP(), snakeBurnHpAuraComponent.getMaxBurnHp()) * tick;
                    lifeComponent.setHp(lifeComponent.getHp() - lostHp);
                }
            }
        }
    }

    private void _handleBuffAbility(double tick, Battle battle) {
        for (Map.Entry<Long, EntityECS> mapElement : this.getEntityStore().entrySet()) {
            EntityECS buffTower = mapElement.getValue();
            if (!buffTower._hasComponent(TowerAbilityComponent.typeID)) continue;

            TowerAbilityComponent towerAbilityComponent = (TowerAbilityComponent) buffTower.getComponent(TowerAbilityComponent.typeID);

            PositionComponent towerPosition = (PositionComponent) buffTower.getComponent(PositionComponent.typeID);
            Point tilePos = Utils.pixel2Tile(towerPosition.getX(), towerPosition.getY(), buffTower.getMode());
            BattleMap battleMap = battle.getBattleMapByEntityMode(buffTower.getMode());
            BattleMapObject battleMapObject = battleMap.battleMapObject;

            for (int i = 0; i < direction.length - 1; i++) {
                TileObject tile = battleMapObject.getTileObject((int) tilePos.x + direction[i], (int) tilePos.y + direction[i + 1]);
                if (tile == null) continue;

                Tower towerInTileObject = tile.getTower();
                if (towerInTileObject == null) continue;

                EntityECS towerEntity = battle.getEntityManager().getEntity(towerInTileObject.getId());
                AttackComponent attackComponent = (AttackComponent) towerEntity.getComponent(AttackComponent.typeID);
                if (attackComponent == null) continue;

                int typeId = towerAbilityComponent.getEffect().getTypeID();
                if (typeId == BuffAttackDamageEffect.typeID) {
                    BuffAttackDamageEffect buffAttackDamageEffect = (BuffAttackDamageEffect) towerAbilityComponent.getEffect();
                    attackComponent.setDamage(attackComponent.getDamage() + attackComponent.getOriginDamage() * buffAttackDamageEffect.getPercent());
                } else if (typeId == BuffAttackSpeedEffect.typeID) {
                    BuffAttackSpeedEffect buffAttackSpeedEffect = (BuffAttackSpeedEffect) towerAbilityComponent.getEffect();
                    attackComponent.setSpeed(attackComponent.getSpeed() - (attackComponent.getOriginSpeed() * buffAttackSpeedEffect.getPercent()));
                }
            }
        }
    }

    @Override
    public boolean checkEntityCondition(EntityECS entity, Component component) {
        return component.getTypeID() == TowerInfoComponent.typeID || component.getTypeID() == MonsterInfoComponent.typeID;
    }
}
