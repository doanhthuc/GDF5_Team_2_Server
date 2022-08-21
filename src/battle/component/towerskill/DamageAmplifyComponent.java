package battle.component.towerskill;

import battle.component.common.Component;
import battle.component.effect.EffectComponent;
import battle.config.GameConfig;
import battle.factory.ComponentFactory;

import java.nio.ByteBuffer;

public class DamageAmplifyComponent extends EffectComponent {
    private String name = "DamageAmplify Effect";
    public static int typeID = GameConfig.COMPONENT_ID.DAMAGE_AMPLIFY_COMPONENT;
    private double amplifyRate;

    public DamageAmplifyComponent(double amplifyRate) {
        super(GameConfig.COMPONENT_ID.DAMAGE_AMPLIFY_COMPONENT);
        this.amplifyRate = amplifyRate;
    }

    public DamageAmplifyComponent clone(ComponentFactory componentFactory) {
        try {
            return componentFactory.createDamageAmplifyComponent(this.amplifyRate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public double getAmplifyRate() {
        return amplifyRate;
    }

    public void setAmplifyRate(double amplifyRate) {
        this.amplifyRate = amplifyRate;
    }

    public void reset(double amplifyRate) {
        this.amplifyRate = amplifyRate;
    }

    @Override
    public void createData(ByteBuffer bf) {
        super.createData(bf);
        bf.putDouble(amplifyRate);
    }
}
