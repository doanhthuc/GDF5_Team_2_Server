package battle.Component.InfoComponent;
import battle.Config.GameConfig;

public class TowerInfoComponent extends InfoComponent {
    public String name = "TowerInfoComponent";
    public int energy;
    String bulletTargetType;
    String archType;
    String targetType;
    String bulletType;
    public TowerInfoComponent(int energy, String bulletTargetType,String archType, String targetType,  String bulletType){
        super(GameConfig.COMPONENT_ID.MONSTER_INFO);
        this.energy=energy;
        this.bulletTargetType=bulletTargetType;
        this.archType=archType;
        this.targetType=targetType;
        this.bulletType=bulletType;
    }
    public void reset(int energy, String bulletTargetType,String archType, String targetType,  String bulletType){
        this.energy=energy;
        this.bulletTargetType=bulletTargetType;
        this.archType=archType;
        this.targetType=targetType;
        this.bulletType=bulletType;
    }

}