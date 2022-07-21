package battle.component.common;

import battle.config.GameConfig;
import battle.factory.ComponentFactory;

public class UnderGroundComponent extends Component {
    private String name = "UnderGroundComponent";
    private int currentPathIdx;
    private int trigger;
    private boolean isInGround;
    public UnderGroundComponent() {
        super(GameConfig.COMPONENT_ID.UNDER_GROUND);
        this.currentPathIdx = 0;
        this.trigger = 0;
        this.isInGround = false;
    }
    public void reset(){
        this.currentPathIdx = 0;
        this.trigger = 0;
        this.isInGround = false;
    }
    public UnderGroundComponent clone()
    {
        try {
            return ComponentFactory.getInstance().createUnderGroundComponent();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
