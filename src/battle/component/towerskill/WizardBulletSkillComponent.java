package battle.component.towerskill;

import battle.component.common.Component;
import battle.component.effect.EffectComponent;
import battle.config.GameConfig;
import battle.factory.ComponentFactory;

public class WizardBulletSkillComponent extends EffectComponent {
    private String name = "FrogBulletSkillComponent";
    public static int typeID = GameConfig.COMPONENT_ID.WIZARD_BULLET_SKILL;
    private int amountMonster;
    private int increaseDamage;

    public WizardBulletSkillComponent(int amountMonster, int increaseDamage) {
        super(GameConfig.COMPONENT_ID.WIZARD_BULLET_SKILL);
        this.reset(amountMonster,increaseDamage);
    }

    public WizardBulletSkillComponent clone(ComponentFactory componentFactory) {
        try {
            return componentFactory.createWizardBulletSkillComponent(this.amountMonster, this.increaseDamage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getAmountMonster() {
        return amountMonster;
    }

    public int getIncreaseDamage() {
        return increaseDamage;
    }

    public void reset(int amountMonster, int increaseDamage) {
        this.amountMonster = amountMonster;
        this.increaseDamage = increaseDamage;
    }
}
