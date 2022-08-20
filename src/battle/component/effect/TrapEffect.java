package battle.component.effect;

import battle.common.Utils;
import battle.config.GameConfig;
import battle.factory.ComponentFactory;

import java.nio.ByteBuffer;

public class TrapEffect extends EffectComponent {
    private String name = "TrapEffect";
    public static int typeID = GameConfig.COMPONENT_ID.TRAP_EFFECT;

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

    public void setCountdown(double countdown) {
        this.countdown = countdown;
        this.isExecuted = true;
    }

    public boolean isExecuted() {
        return isExecuted;
    }

    public double getCountdown() {
        return countdown;
    }

    public TrapEffect clone(ComponentFactory componentFactory) {
        try {
            return componentFactory.createTrapEffect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void createData(ByteBuffer bf) {
        super.createData(bf);

        bf.putDouble(countdown);
        bf.putShort(Utils.getInstance().convertBoolean2Short(isExecuted));
    }
}
