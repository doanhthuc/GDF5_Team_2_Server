package battle.Component;

import battle.Config.GameConfig;
import bitzero.core.G;

public class InfoComponent extends Component{
    public String name= "InfoComponent";
    public InfoComponent(int typeId){
        this.typeID=typeId;
    }
}

class MonsterInfoComponent extends InfoComponent{
    public String name="MonsterInfoComponent";
    String category;
    String classs;
    double weight;
    int damageEnergy;
    int gainEnergy;
    int ability;
    int effects;
    public MonsterInfoComponent(String category, String classs, int weight, int energy, int gainEnergy, int ability, int effects)
    {
        super(GameConfig.COMPONENT_ID.MONSTER_INFO);
        this.category=category;
        this.classs=classs;
        this.weight=weight;
        this.damageEnergy=energy;
        this.gainEnergy=gainEnergy;
        this.ability=ability;
        this.effects=effects;
    }
    public void setCategory(String category){
        if (category.getClass().getSimpleName() != "String") {
            //throw new InvalidArgumentTypeError(category, "string")
        }
        this.category = category;
    }
}

class TowerInfoComponent extends InfoComponent{
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

class BulletInfoComponent extends InfoComponent{
    public String name= "BulletInfoComponent";
    int effects;
    int type;
    public BulletInfoComponent(int effects, int type){
        super(GameConfig.COMPONENT_ID.BULLET_INFO);
        this.effects=effects;
        this.type=type;
    }
}

class LifeComponent extends InfoComponent{
    public String name= "LifeComponent";
    double hp;
    double maxHP;
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
}


















