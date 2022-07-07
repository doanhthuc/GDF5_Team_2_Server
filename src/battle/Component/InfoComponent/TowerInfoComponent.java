package battle.Component.InfoComponent;
import battle.Config.GameConfig;

public class TowerInfoComponent extends InfoComponent {
    public String name = "TowerInfoComponent";
    int energy;
    int bulletTargetType;
    int archType;
    int targetType;
    int bulletType;
    public TowerInfoComponent(int energy, int bulletTargetType, int bulletEffects, int archType, int targetType, double attackRange, int bulletType){
        super(GameConfig.COMPONENT_ID.MONSTER_INFO);
        this.energy=energy;
        this.bulletTargetType=bulletTargetType;
        this.archType=archType;
        this.targetType=targetType;
        this.bulletType=bulletType;
    }
}