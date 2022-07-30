package battle.component.effect;

import battle.config.GameConfig;
import battle.factory.ComponentFactory;

public class TrapEffect extends EffectComponent {
    private String name = "TrapEffect";
    public static int typeID = GameConfig.COMPONENT_ID.TRAP_EFFECT;
    private double damage;
    private boolean isExecuted;
    private double countdown;
    public TrapEffect() {
        super(GameConfig.COMPONENT_ID.TRAP_EFFECT);
        this.reset();
    }

    public void reset() {
        this.isExecuted = false;
        this.countdown = 0;
    }
    public TrapEffect clone(ComponentFactory componentFactory) {
        try {
            return componentFactory.createTrapEffect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
