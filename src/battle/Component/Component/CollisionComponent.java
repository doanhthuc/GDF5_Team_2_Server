package battle.Component.Component;

import battle.Config.GameConfig;

public class CollisionComponent extends Component {
    public String name="PathComponent";
    public int width,height;
    public CollisionComponent(int width,int height){
        super(GameConfig.COMPONENT_ID.COLLISION);
        this.width=width;
        this.height=height;
    }
}