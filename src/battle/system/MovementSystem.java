package battle.system;

import battle.common.Point;
import battle.common.Utils;
import battle.component.common.PositionComponent;
import battle.component.common.VelocityComponent;
import battle.component.info.LifeComponent;
import battle.component.info.MonsterInfoComponent;
import battle.config.GameConfig;
import battle.entity.EntityECS;
import battle.manager.EntityManager;

import java.util.ArrayList;
import java.util.List;

public class MovementSystem extends SystemECS {
    public int id = GameConfig.SYSTEM_ID.MONSTER;

    public MovementSystem() {
        super(GameConfig.SYSTEM_ID.LIFE);
        java.lang.System.out.println("new LifeSystem");
    }

    @Override
    public void run() {
        this.tick = this.getElapseTime();
        //Get Movement Entity
        List<Integer> movementEntityListIds = new ArrayList<>();
        movementEntityListIds.add(VelocityComponent.typeID);
        movementEntityListIds.add(PositionComponent.typeID);
        List<EntityECS> entityList = EntityManager.getInstance().getEntitiesHasComponents(movementEntityListIds);

        for (EntityECS monster : entityList) {
            PositionComponent positionComponent = (PositionComponent) monster.getComponent(PositionComponent.typeID);
            VelocityComponent velocityComponent = (VelocityComponent) monster.getComponent(VelocityComponent.typeID);
            if ((velocityComponent.getDynamicPosition() != null) && velocityComponent.getDynamicPosition().getActive() == true) {
                Point newVelocity = Utils.getInstance().calculateVelocityVector(positionComponent.getPos(), velocityComponent.getDynamicPosition().getPos(), velocityComponent.getOriginSpeed());
                velocityComponent.setSpeedX(newVelocity.x);
                velocityComponent.setSpeedY(newVelocity.y);
            }

            if (velocityComponent.getActive()) {
                double moveDistanceX = velocityComponent.getSpeedX() * (tick*1.0 / 1000);
                double moveDistanceY = velocityComponent.getSpeedY() * (tick*1.0 / 1000);
                positionComponent.setX(positionComponent.getX() + moveDistanceX);
                positionComponent.setY(positionComponent.getY() + moveDistanceY);
                double moveDistance = Math.sqrt(Math.pow(moveDistanceX, 2) + Math.pow(moveDistanceY, 2));
                positionComponent.setMoveDistance(positionComponent.getMoveDistance() + moveDistance);
            }
        }
    }
}
