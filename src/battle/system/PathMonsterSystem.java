package battle.system;

import battle.common.Point;
import battle.common.Utils;
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
    public int id = GameConfig.SYSTEM_ID.PATH_MONSTER;

    public PathMonsterSystem() {
        super(GameConfig.SYSTEM_ID.PATH_MONSTER);
        java.lang.System.out.println("new PathMonsterSystem");
    }

    @Override
    public void run() {
        this.tick = this.getEclapseTime();
        List<Integer> pathComponentIds = Arrays.asList(PathComponent.typeID);
        List<EntityECS> entityList = EntityManager.getInstance().getEntitiesHasComponents(pathComponentIds);

        for (EntityECS entity : entityList) {
            PathComponent pathComponent = (PathComponent) entity.getComponent(PathComponent.typeID);
            PositionComponent positionComponent = (PositionComponent) entity.getComponent(PositionComponent.typeID);
            VelocityComponent velocityComponent = (VelocityComponent) entity.getComponent(VelocityComponent.typeID);
            List<Point> path = pathComponent.getPath();
            int currentPathIdx = pathComponent.getCurrentPathIDx();

            int nextPosIdx = this._findNextPath(path, positionComponent, currentPathIdx);
            if (nextPosIdx != 0) pathComponent.setCurrentPathIDx(nextPosIdx - 1);

            Point nextPos = path.get(nextPosIdx);
            double speed = velocityComponent.calculateSpeed(velocityComponent.getSpeedX(), velocityComponent.getSpeedY());
            Point newVelocity = Utils.getInstance().calculateVelocityVector(positionComponent.getPos(), nextPos, speed);
            velocityComponent.setSpeedX(newVelocity.getX());
            velocityComponent.setSpeedY(newVelocity.getY());
        }
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
