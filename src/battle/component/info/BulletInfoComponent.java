package battle.component.info;

import battle.component.effect.EffectComponent;
import battle.config.GameConfig;
import battle.factory.ComponentFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BulletInfoComponent extends InfoComponent {
    private String name = "BulletInfoComponent";
    public static int typeID = GameConfig.COMPONENT_ID.BULLET_INFO;
    private List<EffectComponent> effects;
    private String type;
    private double radius;
    private Map<Long, Integer> hitMonster;

    public BulletInfoComponent(List<EffectComponent> effects, String type) {
        super(GameConfig.COMPONENT_ID.BULLET_INFO);
        this.effects = effects;
        this.type = type;
    }

    public BulletInfoComponent(List<EffectComponent> effects, String type, double radius) {
        super(GameConfig.COMPONENT_ID.BULLET_INFO);
        this.reset(effects, type, radius);
    }

    public Map<Long, Integer> getHitMonster() {
        return hitMonster;
    }

    public void reset(List<EffectComponent> effects, String type, double radius) {
        this.effects = effects;
        this.type = type;
        this.radius = radius;
        this.hitMonster = new HashMap<>();
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getRadius() {
        return radius;
    }

    public void setHitMonster(long monsterId, int bulletState)
    {
        this.hitMonster.remove(monsterId);
        this.hitMonster.put(monsterId,bulletState);
    }
    public void setRadius(double radius) {
        this.radius = radius;
    }
}