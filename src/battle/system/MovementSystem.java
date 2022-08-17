package battle.system;

import battle.Battle;
import battle.common.Point;
import battle.common.Utils;
import battle.common.ValidatorECS;
import battle.component.common.Component;
import battle.component.common.PathComponent;
import battle.component.common.PositionComponent;
import battle.component.common.VelocityComponent;
import battle.component.effect.FireBallEffect;
import battle.component.info.LifeComponent;
import battle.config.GameConfig;
import battle.entity.EntityECS;

import java.util.ArrayList;
import java.util.List;

public class MovementSystem extends SystemECS {
    private static final String SYSTEM_NAME = "MovementSystem";

    public MovementSystem(long id) {
        super(GameConfig.SYSTEM_ID.MOVEMENT, SYSTEM_NAME, id);
    }

    @Override
    public void run(Battle battle) throws Exception {
        this.tick = this.getElapseTime();
        //Get Movement Entity
        List<Integer> movementEntityListIds = new ArrayList<>();
        movementEntityListIds.add(VelocityComponent.typeID);
        movementEntityListIds.add(PositionComponent.typeID);
        List<EntityECS> entityList = battle.getEntityManager().getEntitiesHasComponents(movementEntityListIds);

        for (EntityECS entity : entityList) {
            PositionComponent positionComponent = (PositionComponent) entity.getComponent(PositionComponent.typeID);
            VelocityComponent velocityComponent = (VelocityComponent) entity.getComponent(VelocityComponent.typeID);
            FireBallEffect fireballEffect = (FireBallEffect) entity.getComponent(FireBallEffect.typeID);

            if ((velocityComponent.getDynamicPosition(battle) != null) && velocityComponent.getDynamicPosition(battle).getActive()) {
                Point newVelocity = Utils.getInstance().calculateVelocityVector(positionComponent.getPos(), velocityComponent.getDynamicPosition(battle).getPos(), velocityComponent.getOriginSpeed());
                velocityComponent.setSpeedX(newVelocity.x);
                velocityComponent.setSpeedY(newVelocity.y);
            }

            // start handle fireball effect
            if (fireballEffect != null) {
                if (fireballEffect.getAccTime() < fireballEffect.getMaxDuration()) {
                    fireballEffect.setAccTime(fireballEffect.getAccTime() + (this.tick / 1000));
                    double newSpeed = -1 * fireballEffect.getA() * fireballEffect.getAccTime() + fireballEffect.getV0();
                    Point newVelocity = Utils.calculateVelocityVector(fireballEffect.getStartPos(),
                            fireballEffect.getEndPos(), newSpeed);
                    velocityComponent.setSpeedX(newVelocity.x);
                    velocityComponent.setSpeedY(newVelocity.y);
                } else {
                    entity.removeComponent(fireballEffect);
                    if (ValidatorECS.isEntityInGroupId(entity, GameConfig.GROUP_ID.MONSTER_ENTITY)) {
                        PositionComponent monsterPos =
                                (PositionComponent) entity.getComponent(PositionComponent.typeID);
                        if (monsterPos != null) {
                            Point tilePos = Utils.pixel2Tile(monsterPos.getX(), monsterPos.getY(), entity.getMode());
                            if (!Utils.validateTilePos(tilePos)) {
                                continue;
                            }
                            int[][] map = battle.getBattleMapByEntityMode(entity.getMode()).map;
                            if (map[(int) tilePos.x][(int) tilePos.y] == GameConfig.MAP.HOLE && entity.getTypeID() != GameConfig.ENTITY_ID.BAT) {
                                LifeComponent lifeComponent = (LifeComponent) entity.getComponent(LifeComponent.typeID);
                                lifeComponent.setHp(0);
                            } else {
                                List<Point> path = battle.getEntityFactory().getShortestPathInTile(entity.getMode(), (int) tilePos.getX(), (int) tilePos.getY());
                                PathComponent newPath = battle.getComponentFactory().createPathComponent(path, entity.getMode(), true);
                                entity.addComponent(newPath);
                            }
                        }
                        VelocityComponent velocityComp = (VelocityComponent) entity.getComponent(VelocityComponent.typeID);
                        velocityComp.setSpeedX(velocityComp.getOriginSpeed());
                        velocityComp.setSpeedY(velocityComp.getOriginSpeed());
                    }
                }
            }
            //end handle fireball effect

            if (velocityComponent.getActive()) {
                double moveDistanceX = velocityComponent.getSpeedX() * (tick / 1000);
                double moveDistanceY = velocityComponent.getSpeedY() * (tick / 1000);

                Point tmpPos = new Point(
                        positionComponent.getPos().x + moveDistanceX,
                        positionComponent.getPos().y + moveDistanceY);
                if (ValidatorECS.isEntityInGroupId(entity, GameConfig.GROUP_ID.MONSTER_ENTITY)
                        && entity.getComponent(FireBallEffect.typeID) != null) {
                    Point currentTilePos = Utils.pixel2Tile(positionComponent.getPos().x, positionComponent.getPos().y, entity.getMode());
                    Point futureTilePos = Utils.pixel2Tile(tmpPos.x, tmpPos.y, entity.getMode());
                    if (Utils.validateTilePos(currentTilePos)
                            && (
                            !Utils.validateTilePos(futureTilePos)
                                    || (battle.getBattleMapByEntityMode(entity.getMode()).map[(int) futureTilePos.x][(int) futureTilePos.y] == GameConfig.MAP.TOWER)
                                    || (battle.getBattleMapByEntityMode(entity.getMode()).map[(int) futureTilePos.x][(int) futureTilePos.y] == GameConfig.MAP.TREE)
                    )
                    ) {
                        // Invalid Position
                    } else {
                        positionComponent.setX(tmpPos.x);
                        positionComponent.setY(tmpPos.y);
                    }
                } else {
                    positionComponent.setX(tmpPos.x);
                    positionComponent.setY(tmpPos.y);
                    double moveDistance = Math.sqrt(Math.pow(moveDistanceX, 2) + Math.pow(moveDistanceY, 2));
                    positionComponent.setMoveDistance(positionComponent.getMoveDistance() + moveDistance);
                }
            }
        }
    }

    @Override
    public boolean checkEntityCondition(EntityECS entity, Component component) {
        return component.getTypeID() == VelocityComponent.typeID;
    }
}
