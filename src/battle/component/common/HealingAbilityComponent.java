package battle.component.common;

import battle.common.Utils;
import battle.config.GameConfig;
import battle.factory.ComponentFactory;

import java.nio.ByteBuffer;

public class HealingAbilityComponent extends Component{
    private String name = "HealingAbilityComponent";
    public static int typeID = GameConfig.COMPONENT_ID.HEALING_ABILITY;
    private double range;
    private double healingRate;
    private double countdown;

    public HealingAbilityComponent(double range, double healingRate) {
        super(GameConfig.COMPONENT_ID.HEALING_ABILITY);
        this.reset(range, healingRate);
    }

    public void reset(double range, double healingRate) {
        this.range = range;
        this.healingRate = healingRate;
        this.countdown = 1;
    }

    public HealingAbilityComponent clone(ComponentFactory componentFactory) {
        try {
            return componentFactory.createHealingAbilityComponent(this.range, this.healingRate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public double getHealingRate() {
        return healingRate;
    }

    public void setHealingRate(double healingRate) {
        this.healingRate = healingRate;
    }

    public double getCountdown() {
        return countdown;
    }

    public void setCountdown(double countdown) {
        this.countdown = countdown;
    }

    public void createData(ByteBuffer bf) {
        super.createData(bf);
        bf.putDouble(countdown);
    }
}
