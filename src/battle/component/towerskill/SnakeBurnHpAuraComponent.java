package battle.component.towerskill;

import battle.component.common.Component;
import battle.config.GameConfig;
import battle.factory.ComponentFactory;

public class SnakeBurnHpAuraComponent extends Component {
    private String name = "SnakeBurnHpAura";
    public static int typeID = GameConfig.COMPONENT_ID.SNAKE_BURN_HP_AURA;
    private double burnRate;
    private double maxBurnHp;
    private double range;


    public SnakeBurnHpAuraComponent(double burnRate, double maxBurnHp, double range) {
        super(GameConfig.COMPONENT_ID.SNAKE_BURN_HP_AURA);
        this.reset(burnRate, maxBurnHp, range);
    }

    public SnakeBurnHpAuraComponent clone(ComponentFactory componentFactory) {
        try {
            return componentFactory.createSnakeBurnHpAuraComponent(burnRate, maxBurnHp,range);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void reset(double burnRate, double maxBurnHp, double range) {
        this.burnRate = burnRate;
        this.maxBurnHp = maxBurnHp;
        this.range = range;

    }

    public double getBurnRate() {
        return burnRate;
    }

    public void setBurnRate(double burnRate) {
        this.burnRate = burnRate;
    }

    public double getMaxBurnHp() {
        return maxBurnHp;
    }

    public void setMaxBurnHp(double maxBurnHp) {
        this.maxBurnHp = maxBurnHp;
    }

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = range;
    }
}
