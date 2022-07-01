package battle.Component;

import battle.Config.GameConfig;

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
    public MonsterInfoComponent(String category, String classs, int weight, int energy, int gainEnergy, int ability, int effects)
    {
        super(GameConfig.COMPONENT_ID.MONSTER_INFO);
        this.category=category;
        this.classs=classs;
        this.weight=weight;
        this.damageEnergy=energy;
        this.gainEnergy=gainEnergy;
    }
}