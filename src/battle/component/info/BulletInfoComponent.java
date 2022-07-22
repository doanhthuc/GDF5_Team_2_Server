package battle.component.info;

import battle.component.effect.EffectComponent;
import battle.config.GameConfig;
import battle.factory.ComponentFactory;

import java.util.List;

public class BulletInfoComponent extends InfoComponent {
    private String name = "BulletInfoComponent";
    public static int typeID = GameConfig.COMPONENT_ID.BULLET_INFO;
    private List<EffectComponent> effects;
    private int type;
    private double radius;

    public BulletInfoComponent(List<EffectComponent> effects, int type) {
        super(GameConfig.COMPONENT_ID.BULLET_INFO);
        this.effects = effects;
        this.type = type;
    }

    public BulletInfoComponent(List<EffectComponent> effects, int type, double radius) {
        super(GameConfig.COMPONENT_ID.BULLET_INFO);
        this.effects = effects;
        this.type = type;
        this.radius = radius;
    }

    public void reset(List<EffectComponent> effects, int type, double radius) {
        this.effects = effects;
        this.type = type;
        this.radius = radius;
    }

    public List<EffectComponent> getEffects() {
        return effects;
    }

    public void setEffects(List<EffectComponent> effects) {
        this.effects = effects;
    }

    public BulletInfoComponent clone() {
        try {
            return ComponentFactory.getInstance().createBulletInfoComponent(this.effects, this.type, this.radius);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}