package battle.component.Component;

import battle.config.GameConfig;
import battle.factory.ComponentFactory;

public class CollisionComponent extends Component {
    private String name = "CollisionComponent";
    private double width, height;

    public CollisionComponent(double width, double height) {
        super(GameConfig.COMPONENT_ID.COLLISION);
        this.width = width;
        this.height = height;
    }

    public CollisionComponent clone() {
        try {
            return ComponentFactory.getInstance().createCollisionComponent(this.width, this.height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public void reset(double width, double height) {
        this.width = width;
        this.height = height;
    }
}