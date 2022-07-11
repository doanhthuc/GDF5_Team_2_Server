package battle.Component.InfoComponent;
import battle.Config.GameConfig;
public class LifeComponent extends InfoComponent {
    public String name= "LifeComponent";
    public double hp;
    public double maxHP;
    public LifeComponent(double hp, double maxHP)
    {
        super(GameConfig.COMPONENT_ID.LIFE);
        this.hp=hp;
        if (maxHP>0){
            this.maxHP=maxHP;
        } else {
            this.maxHP=hp;
        }
    }
    public LifeComponent(double hp)
    {
        super(GameConfig.COMPONENT_ID.LIFE);
        this.hp=hp;
        if (maxHP>0){
            this.maxHP=maxHP;
        } else {
            this.maxHP=hp;
        }
    }
    public void reset(double hp)
    {
        this.hp=hp;
        if (maxHP>0){
            this.maxHP=maxHP;
        } else {
            this.maxHP=hp;
        }
    }
}