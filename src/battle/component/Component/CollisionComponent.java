package battle.component.Component;

import battle.config.GameConfig;

public class CollisionComponent extends Component {
    public String name="CollisionComponent";
    public double width,height;
    public CollisionComponent(double width,double height){
        super(GameConfig.COMPONENT_ID.COLLISION);
        this.width=width;
        this.height=height;
    }
    public void reset(double width,double height)
    {
        this.width=width;
        this.height=height;
    }
}