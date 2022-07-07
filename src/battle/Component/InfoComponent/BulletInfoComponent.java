package battle.Component;

import battle.Component.InfoComponent;
import battle.Config.GameConfig;

public class BulletInfoComponent extends InfoComponent {
    public String name= "BulletInfoComponent";
    int effects;
    int type;
    public BulletInfoComponent(int effects, int type){
        super(GameConfig.COMPONENT_ID.BULLET_INFO);
        this.effects=effects;
        this.type=type;
    }
}