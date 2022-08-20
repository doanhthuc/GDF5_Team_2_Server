package battle.component.effect;

import battle.config.GameConfig;
import battle.factory.ComponentFactory;

import java.nio.ByteBuffer;

public class BuffAttackRangeEffect extends EffectComponent {
    public static String name = "BuffAttackRangeEffect";
    public static int typeID = GameConfig.COMPONENT_ID.BUFF_ATTACK_RANGE;
    private double percent;

    public BuffAttackRangeEffect(double percent) {
        super(GameConfig.COMPONENT_ID.BUFF_ATTACK_RANGE);
        this.percent = percent;
    }

    public void reset(double percent) {
        this.percent = percent;
    }

    public BuffAttackRangeEffect clone(ComponentFactory componentFactory) {
        try {
            return componentFactory.createBuffAttackRangeEffect(percent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    @Override
    public void createData(ByteBuffer bf) {
        super.createData(bf);
        bf.putDouble(percent);
    }
}