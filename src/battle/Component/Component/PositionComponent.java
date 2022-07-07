package battle.Component.Component;

import battle.Config.GameConfig;

public class PositionComponent extends Component {
    public String name = "PositionComponent";
    public int x;
    public int y;
    public PositionComponent(int x, int y) {
        super(GameConfig.COMPONENT_ID.POSITION);
        this.x=x;
        this.y=y;
    }
    public void reset(int x,int y){
        this.x=x;
        this.y=y;
    }

}