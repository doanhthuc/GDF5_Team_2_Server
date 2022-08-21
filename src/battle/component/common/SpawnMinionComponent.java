package battle.component.common;

import battle.component.effect.BuffAttackDamageEffect;
import battle.component.effect.BuffAttackRangeEffect;
import battle.component.effect.BuffAttackSpeedEffect;
import battle.config.GameConfig;
import battle.factory.ComponentFactory;

import java.nio.ByteBuffer;

public class SpawnMinionComponent extends Component {
    private String name = "SpawnMinionComponent";
    public static int typeID = GameConfig.COMPONENT_ID.SPAWN_MINION;
    private double period;
    private int spawnAmount;
    private int maxAmount;

    public SpawnMinionComponent(double period) {
        super(GameConfig.COMPONENT_ID.SPAWN_MINION);
        this.reset(period);
    }

    public void reset(double period) {
        this.period = period;
        this.spawnAmount = 0;
        this.maxAmount = 5;
    }

    public SpawnMinionComponent clone(ComponentFactory componentFactory) {
        try {
            return componentFactory.createSpawnMinionComponent(this.period);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public double getPeriod() {
        return period;
    }

    public void setPeriod(double period) {
        this.period = period;
    }

    public int getSpawnAmount() {
        return spawnAmount;
    }

    public void setSpawnAmount(int spawnAmount) {
        this.spawnAmount = spawnAmount;
    }

    public int getMaxAmount() {
        return this.maxAmount;
    }

    @Override
    public void createData(ByteBuffer bf) {
        super.createData(bf);
        bf.putInt(spawnAmount);
        bf.putDouble(period);
    }
}
