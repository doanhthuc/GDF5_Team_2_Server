package battle.component.InfoComponent;

import battle.component.EffectComponent.EffectComponent;
import battle.config.GameConfig;

import java.util.ArrayList;

public class BulletInfoComponent extends InfoComponent {
    private String name= "BulletInfoComponent";
    private ArrayList<EffectComponent> effects;
    private int type;
    public BulletInfoComponent(ArrayList<EffectComponent> effects, int type){
        super(GameConfig.COMPONENT_ID.BULLET_INFO);
        this.effects=effects;
        this.type=type;
    }

    public ArrayList<EffectComponent> getEffects() {
        return effects;
    }

    public void reset(ArrayList<EffectComponent> effects, int type){
        this.effects=effects;
        this.type=type;
    }
}