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
        this.tick = this.getEclapseTime();
        //Get Movement Entity
        List<Integer> movementEntitylistIds = new ArrayList<>();
        movementEntitylistIds.add(VelocityComponent.typeID);
        movementEntitylistIds.add(PositionComponent.typeID);
        List<EntityECS> entityList = EntityManager.getInstance().getEntitiesHasComponents(movementEntitylistIds);

        for (EntityECS monster : entityList) {
            PositionComponent positionComponent = (PositionComponent) monster.getComponent(PositionComponent.typeID);
            VelocityComponent velocityComponent = (VelocityComponent) monster.getComponent(VelocityComponent.typeID);
            if ((velocityComponent.getDynamicPosition() != null) && velocityComponent.getDynamicPosition().getActive() == true) {
                Point newVelocity = Utils.getInstance().calculateVelocityVector(positionComponent, velocityComponent.getDynamicPosition(), velocityComponent.getOriginSpeed());
                velocityComponent.setSpeedX(newVelocity.x);
                velocityComponent.setSpeedY(newVelocity.y);
            }

            if (velocityComponent.getActive()) {
                double moveDistanceX = velocityComponent.getSpeedX() * tick;
                double moveDistanceY = velocityComponent.getSpeedY() * tick;
                positionComponent.setX(positionComponent.getX() + moveDistanceX);
                positionComponent.setY(positionComponent.getY() + moveDistanceY);
                double moveDistance = Math.sqrt(Math.pow(moveDistanceX, 2) + Math.pow(moveDistanceY, 2));
                positionComponent.setMoveDistance(positionComponent.getMoveDistance() + moveDistance);
            }
        }
    }
}
