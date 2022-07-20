package battle.component.info;
import battle.config.GameConfig;
import battle.factory.ComponentFactory;

public class TowerInfoComponent extends InfoComponent {
    private String name = "TowerInfoComponent";
    private int energy;
    private String bulletTargetType;
    private String archType;
    private String targetType;
    private String bulletType;

    public TowerInfoComponent(int energy, String bulletTargetType, String archType, String targetType, String bulletType) {
        super(GameConfig.COMPONENT_ID.TOWER_INFO);
        this.reset(energy, bulletTargetType, archType, targetType, bulletType);
    }

    public void reset(int energy, String bulletTargetType, String archType, String targetType, String bulletType) {
        this.energy = energy;
        this.bulletTargetType = bulletTargetType;
        this.archType = archType;
        this.targetType = targetType;
        this.bulletType = bulletType;
    }

    public TowerInfoComponent clone() {
        try {
            return ComponentFactory.getInstance().createTowerInfoComponent(energy, bulletTargetType, archType, targetType, bulletType);
        } catch (Exception e) {
            return null;
        }
    }
}