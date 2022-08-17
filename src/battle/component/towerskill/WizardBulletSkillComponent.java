package battle.component.towerskill;

import battle.component.common.Component;
import battle.config.GameConfig;
import battle.factory.ComponentFactory;

public class WizardBulletSkillComponent extends Component {
    private String name = "FrogBulletSkillComponent";
    public static int typeID = GameConfig.COMPONENT_ID.WIZARD_BULLET_SKILL;
    private int amountMonster;

    public WizardBulletSkillComponent(int amountMonster) {
        super(GameConfig.COMPONENT_ID.WIZARD_BULLET_SKILL);
        this.reset(amountMonster);
    }

    public WizardBulletSkillComponent clone(ComponentFactory componentFactory) {
        try {
            return componentFactory.createWizardBulletSkillComponent(this.amountMonster);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void reset(int amountMonster) {
        this.amountMonster = amountMonster;
    }
}
