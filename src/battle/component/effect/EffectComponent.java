package battle.component.effect;

import battle.component.common.Component;
import battle.factory.ComponentFactory;

public class EffectComponent extends Component {
    private String name = "EffectComponent";

    public EffectComponent(int typeID) {
        super(typeID);
    }

    public EffectComponent clone(ComponentFactory componentFactory) throws Exception {
        return null;
    }
}





