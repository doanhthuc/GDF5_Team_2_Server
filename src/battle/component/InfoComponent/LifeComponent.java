package battle.component.InfoComponent;
import battle.config.GameConfig;
public class LifeComponent extends InfoComponent {
    private String name= "LifeComponent";
    private double hp;
    private double maxHP;
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

    public double getHp() {
        return hp;
    }

    public void setHp(double hp) {
        this.hp = hp;
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