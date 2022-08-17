package battle.system;

import battle.Battle;
import battle.common.Point;
import battle.common.Utils;
import battle.component.common.Component;
import battle.component.common.PathComponent;
import battle.component.common.PositionComponent;
import battle.component.common.VelocityComponent;
import battle.component.info.LifeComponent;
import battle.component.info.MonsterInfoComponent;
import battle.config.GameConfig;
import battle.entity.EntityECS;
import battle.manager.EntityManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PathMonsterSystem extends SystemECS {
    private static final String SYSTEM_NAME = "PathMonsterSystem";

    public PathMonsterSystem(long id) {
        super(GameConfig.SYSTEM_ID.PATH_MONSTER, SYSTEM_NAME,id);
    }

    @Override
    public void run(Battle battle) {
        this.tick = this.getElapseTime();
        List<Integer> pathComponentIds = Arrays.asList(PathComponent.typeID, PositionComponent.typeID);
        List<EntityECS> entityList = battle.getEntityManager().getEntitiesHasComponents(pathComponentIds);

        for (EntityECS entity : entityList) {
            PathComponent pathComponent = (PathComponent) entity.getComponent(PathComponent.typeID);
            PositionComponent positionComponent = (PositionComponent) entity.getComponent(PositionComponent.typeID);
            VelocityComponent velocityComponent = (VelocityComponent) entity.getComponent(VelocityComponent.typeID);
            List<Point> path = pathComponent.getPath();
            if (path == null) continue;
            int currentPathIdx = pathComponent.getCurrentPathIDx();

            int nextPosIdx = this._findNextPath(path, positionComponent, currentPathIdx);
            if (nextPosIdx > 1) pathComponent.setCurrentPathIDx(nextPosIdx - 1);

            Point nextPos = path.get(nextPosIdx);

            double speed = velocityComponent.calculateSpeed(velocityComponent.getSpeedX(), velocityComponent.getSpeedY());
            Point newVelocity = Utils.getInstance().calculateVelocityVector(positionComponent.getPos(), nextPos, speed);
            velocityComponent.setSpeedX(newVelocity.getX());
            velocityComponent.setSpeedY(newVelocity.getY());

        }
    }

    @Override
    public boolean checkEntityCondition(EntityECS entity, Component component) {
        return component.getTypeID() == PathComponent.typeID;
    }

    public int _findNextPath(List<Point> path, PositionComponent positionComponent, int currentPathIdx) {
        int minDisIdx = 0;
        double minDistance = Integer.MAX_VALUE;
        for (int i = currentPathIdx; i < path.size() - 1; i++) {
            double distance = Utils.euclidDistance(positionComponent, path.get(i));
            if (distance < minDistance) {
                minDistance = distance;
                minDisIdx = i;
            }
        }
        return minDisIdx + 1;
    }
}
