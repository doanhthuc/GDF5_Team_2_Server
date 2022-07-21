package battle.component.info;

import battle.component.effect.EffectComponent;
import battle.config.GameConfig;
import battle.factory.ComponentFactory;

import java.util.List;

public class BulletInfoComponent extends InfoComponent {
    private String name = "BulletInfoComponent";

    private List<EffectComponent> effects;
    private int type;

    public BulletInfoComponent(List<EffectComponent> effects, int type) {
        super(GameConfig.COMPONENT_ID.BULLET_INFO);
        this.effects = effects;
        this.type = type;
    }

    public void reset(List<EffectComponent> effects, int type) {
        this.effects = effects;
        this.type = type;
    }

    public List<EffectComponent> getEffects() {
        return effects;
    }

    public void setEffects(List<EffectComponent> effects) {
        this.effects = effects;
    }

    public BulletInfoComponent clone() {
        try {
            return ComponentFactory.getInstance().createBulletInfoComponent(this.effects, this.type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}