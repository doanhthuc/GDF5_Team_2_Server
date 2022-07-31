package battle.component.common;

import battle.config.GameConfig;
import battle.factory.ComponentFactory;

public class CollisionComponent extends Component {
    private String name = "CollisionComponent";
    public static int typeID = GameConfig.COMPONENT_ID.COLLISION;
    private double width, height, originWidth, originHeight;

    public CollisionComponent(double width, double height, double originWidth, double originHeight) {
        super(GameConfig.COMPONENT_ID.COLLISION);
        this.reset(width, height, originWidth, originHeight);
    }

    public CollisionComponent(double width, double height) {
        super(GameConfig.COMPONENT_ID.COLLISION);
        this.reset(width, height);
    }

    public CollisionComponent clone(ComponentFactory componentFactory) {
        try {
            return componentFactory.createCollisionComponent(this.width, this.height);
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

    public void reset(double width, double height, double originWidth, double originHeight) {
        this.width = width;
        this.height = height;
        this.originWidth = originWidth;
        this.originHeight = originHeight;
    }

    public void reset(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getOriginWidth() {
        return originWidth;
    }

    public void setOriginWidth(double originWidth) {
        this.originWidth = originWidth;
    }

    public double getOriginHeight() {
        return originHeight;
    }

    public void setOriginHeight(double originHeight) {
        this.originHeight = originHeight;
    }
}