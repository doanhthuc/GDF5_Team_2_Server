<<<<<<< HEAD:src/battle/Component/InfoComponent/TowerInfoComponent.java
package battle.Component.InfoComponent;

import battle.Config.GameConfig;
=======
package battle.component.InfoComponent;
import battle.config.GameConfig;
>>>>>>> master:src/battle/component/InfoComponent/TowerInfoComponent.java

public class TowerInfoComponent extends InfoComponent {
    public String name = "TowerInfoComponent";
    public int energy;
    String bulletTargetType;
    String archType;
    String targetType;
    String bulletType;

    public TowerInfoComponent(int energy, String bulletTargetType, String archType, String targetType, String bulletType) {
        super(GameConfig.COMPONENT_ID.TOWER_INFO);
        this.energy = energy;
        this.bulletTargetType = bulletTargetType;
        this.archType = archType;
        this.targetType = targetType;
        this.bulletType = bulletType;
    }

    public void reset(int energy, String bulletTargetType, String archType, String targetType, String bulletType) {
        this.energy = energy;
        this.bulletTargetType = bulletTargetType;
        this.archType = archType;
        this.targetType = targetType;
        this.bulletType = bulletType;
    }

}