package battle.component.InfoComponent;
import battle.component.EffectComponent.EffectComponent;
import battle.config.GameConfig;

public class MonsterInfoComponent extends InfoComponent {
    public String name="MonsterInfoComponent";
    String category;
    String classs;
    double weight;
    int damageEnergy;
    int gainEnergy;
    int ability;
    EffectComponent effects;
    public MonsterInfoComponent(String category, String classs, int weight, int energy, int gainEnergy, int ability, EffectComponent effects)
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
    public void reset(String category, String classs, int weight, int energy, int gainEnergy, int ability, EffectComponent effects)
    {
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
