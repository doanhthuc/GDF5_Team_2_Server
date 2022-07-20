package battle.component.infoComponent;

import battle.component.effectComponent.EffectComponent;
import battle.config.GameConfig;

import java.util.List;

public class BulletInfoComponent extends InfoComponent {
    private String name= "BulletInfoComponent";
    private List<EffectComponent> effects;
    private int type;
    public BulletInfoComponent(List<EffectComponent> effects, int type){
        super(GameConfig.COMPONENT_ID.BULLET_INFO);
        this.effects=effects;
        this.type=type;
    }

    public List<EffectComponent> getEffects() {
        return effects;
    }

    public void reset(List<EffectComponent> effects, int type){
        this.effects=effects;
        this.type=type;
    }
}