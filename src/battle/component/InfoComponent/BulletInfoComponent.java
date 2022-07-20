package battle.Component.InfoComponent;

import battle.Component.EffectComponent.EffectComponent;
import battle.Config.GameConfig;

import java.util.ArrayList;

public class BulletInfoComponent extends InfoComponent {
    public String name= "BulletInfoComponent";
    public ArrayList<EffectComponent> effects;
    public int type;
    public BulletInfoComponent(ArrayList<EffectComponent> effects, int type){
        super(GameConfig.COMPONENT_ID.BULLET_INFO);
        this.effects=effects;
        this.type=type;
    }
    public void reset(ArrayList<EffectComponent> effects, int type){
        this.effects=effects;
        this.type=type;
    }
}