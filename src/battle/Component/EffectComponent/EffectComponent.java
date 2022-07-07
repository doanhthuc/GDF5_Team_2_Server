package battle.Component.EffectComponent;

import battle.Component.Component.Component;
import battle.Config.GameConfig;

public class EffectComponent extends Component {
    public String name = "EffectComponent";

    public EffectComponent(int typeID) {
        this.typeID = typeID;
    }
    public EffectComponent clone(){
        return null;
    }
}





