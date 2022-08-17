package battle.component.towerskill;

import battle.component.common.Component;
import battle.config.GameConfig;
import battle.factory.ComponentFactory;

public class FrogBulletSkillComponent extends Component {
    private String name = "FrogBulletSkillComponent";
    public static int typeID = GameConfig.COMPONENT_ID.FROG_BULLET_SKILL;

    public FrogBulletSkillComponent() {
        super(GameConfig.COMPONENT_ID.FROG_BULLET_SKILL);
    }

    public FrogBulletSkillComponent clone(ComponentFactory componentFactory) {
        try {
            return componentFactory.createFrogBulletSkillComponent();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void reset() {
    }
}
