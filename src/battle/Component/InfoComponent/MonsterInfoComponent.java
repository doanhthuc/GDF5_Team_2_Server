package battle.Component.InfoComponent;

import battle.Component.EffectComponent.EffectComponent;
import battle.Config.GameConfig;

public class MonsterInfoComponent extends InfoComponent {
    private String name = "MonsterInfoComponent";
    private String category;
    private String classs;
    private double weight;
    private int damageEnergy;
    private int gainEnergy;
    private int ability;
    private EffectComponent effects;

    public MonsterInfoComponent(String category, String classs, int weight, int energy, int gainEnergy, int ability, EffectComponent effects) {
        super(GameConfig.COMPONENT_ID.MONSTER_INFO);
        this.category = category;
        this.classs = classs;
        this.weight = weight;
        this.damageEnergy = energy;
        this.gainEnergy = gainEnergy;
        this.ability = ability;
        this.effects = effects;
    }

    public void reset(String category, String classs, int weight, int energy, int gainEnergy, int ability, EffectComponent effects) {
        this.category = category;
        this.classs = classs;
        this.weight = weight;
        this.damageEnergy = energy;
        this.gainEnergy = gainEnergy;
        this.ability = ability;
        this.effects = effects;
    }

    public void setCategory(String category) {
        if (category.getClass().getSimpleName() != "String") {
            //throw new InvalidArgumentTypeError(category, "string")
        }
        this.category = category;
    }
}
