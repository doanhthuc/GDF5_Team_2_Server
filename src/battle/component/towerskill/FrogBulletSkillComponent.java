package battle.component.towerskill;

import battle.component.common.Component;
import battle.component.effect.EffectComponent;
import battle.config.GameConfig;
import battle.factory.ComponentFactory;

public class FrogBulletSkillComponent extends EffectComponent {
    private String name = "FrogBulletSkillComponent";
    public static int typeID = GameConfig.COMPONENT_ID.FROG_BULLET_SKILL;
    private double increaseDamage;

    public FrogBulletSkillComponent(double increaseDamage) {
        super(GameConfig.COMPONENT_ID.FROG_BULLET_SKILL);
        this.reset(increaseDamage);
    }

    public FrogBulletSkillComponent clone(ComponentFactory componentFactory) {
        try {
            return componentFactory.createFrogBulletSkillComponent(this.increaseDamage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void reset(double increaseDamage) {
        this.increaseDamage= increaseDamage;
    }

    public double getIncreaseDamage() {
        return increaseDamage;
    }
}
