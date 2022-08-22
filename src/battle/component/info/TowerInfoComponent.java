package battle.component.info;

import battle.common.Utils;
import battle.component.effect.EffectComponent;
import battle.config.GameConfig;
import battle.factory.ComponentFactory;

import java.nio.ByteBuffer;

public class TowerInfoComponent extends InfoComponent {
    private String name = "TowerInfoComponent";
    public static int typeID = GameConfig.COMPONENT_ID.TOWER_INFO;
    private int energy;
    private String bulletTargetType;
    private String archType;
    private String targetType;
    private String bulletType;
    private short level;

    public TowerInfoComponent(int energy, String bulletTargetType, String archType, String targetType, String bulletType) {
        super(GameConfig.COMPONENT_ID.TOWER_INFO);
        this.reset(energy, bulletTargetType, archType, targetType, bulletType);
    }

    public void reset(int energy, String bulletTargetType, String archType, String targetType, String bulletType) {
        this.energy = energy;
        this.bulletTargetType = bulletTargetType;
        this.archType = archType;
        this.targetType = targetType;
        this.bulletType = bulletType;
        this.level = 1;
    }

    public TowerInfoComponent clone(ComponentFactory componentFactory) throws Exception {
        try {
            return componentFactory.createTowerInfoComponent(energy, bulletTargetType, archType, targetType, bulletType);
        } catch (Exception e) {
            return null;
        }
    }

    public void setLevel(short level) {
        this.level = level;
    }

    public int getEnergy() {
        return this.energy;
    }

    public void createData(ByteBuffer bf) {
        super.createData(bf);
        bf.putShort(level);
    }
}