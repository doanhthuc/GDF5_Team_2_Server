package battle.component.info;
import battle.component.common.Component;
import battle.component.effect.EffectComponent;
import battle.config.GameConfig;
import battle.factory.ComponentFactory;

import java.util.List;

public class MonsterInfoComponent extends InfoComponent {
    private String name = "MonsterInfoComponent";
    public static int typeID = GameConfig.COMPONENT_ID.MONSTER_INFO;

    private String category;
    private String classs;
    private double weight;
    private int energy;
    private int gainEnergy;
    int ability;
    private List<EffectComponent> effects;

    public int getGainEnergy() {
        return gainEnergy;
    }

    public int getEnergy() {
        return energy;
    }

    public MonsterInfoComponent(String category, String classs, int weight, int energy,
                                int gainEnergy, int ability, List<EffectComponent> effects) {
        super(GameConfig.COMPONENT_ID.MONSTER_INFO);
        this.reset(category, classs, weight, energy, gainEnergy, ability, effects);
    }

    public void reset( String category, String classs, double weight, int energy,
                      int gainEnergy, int ability, List<EffectComponent> effects) {
        this.name = name;
        this.weight = weight;
        this.category = category;
        this.classs = classs;
        this.energy = energy;
        this.gainEnergy = gainEnergy;
        this.ability = ability;
        this.effects = effects;
    }

    public MonsterInfoComponent clone() {
        try {
        //    return ComponentFactory.getInstance().createMonsterInfoComponent(name, category, classs, energy, gainEnergy, ability, effects);
        } catch (Exception e) {
        }
        return null;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getClasss() {
        return classs;
    }

    public void setClasss(String classs) {
        this.classs = classs;
    }

    public double getWeight() {
        return this.weight;
    }
}
