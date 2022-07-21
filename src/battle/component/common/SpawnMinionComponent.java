package battle.component.common;

import battle.config.GameConfig;
import battle.factory.ComponentFactory;

public class SpawnMinionComponent extends Component {
    private String name = "SpawnMinionComponent";
    public static int typeID = GameConfig.COMPONENT_ID.SPAWN_MINION;
    private double period;
    private int spawmAmount;
    public SpawnMinionComponent(double period) {
        super(GameConfig.COMPONENT_ID.SPAWN_MINION);
        this.reset(period);
    }

    public void reset(double period) {
        this.period = period;
        this.spawmAmount = 0;
    }
    public SpawnMinionComponent clone(){
        try {
            return ComponentFactory.getInstance().createSpawnMinionComponent(this.period);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
