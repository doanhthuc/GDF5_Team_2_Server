package battle.config.conf.potion;


import java.io.Serializable;

public class PotionConfigItem implements Serializable {
    String name;
    short energy;
    String map;
    double radius;
    boolean adjustUseCardLevel;
    Adjust adjust;
    double damage;

    public class Adjust {
        AdjustItem player;
        AdjustItem enemy;

        public AdjustItem getPlayer() {
            return player;
        }

        public AdjustItem getEnemy() {
            return enemy;
        }
    }

    public class AdjustItem {
        String type;
        int value;

        public String getType() {
            return type;
        }

        public int getValue() {
            return value;
        }
    }

    public String getName() {
        return name;
    }

    public short getEnergy() {
        return energy;
    }

    public String getMap() {
        return map;
    }

    public double getRadius() {
        return radius;
    }

    public boolean isAdjustUseCardLevel() {
        return adjustUseCardLevel;
    }

    public Adjust getAdjust() {
        return adjust;
    }

    public double getDamage() {
        return damage;
    }

    @Override
    public String toString() {
        return "PotionConfigItem{" +
                "name='" + name + '\'' +
                ", energy=" + energy +
                ", map='" + map + '\'' +
                ", radius=" + radius +
                ", adjustUseCardLevel=" + adjustUseCardLevel +
                ", adjust=" + adjust +
                '}';
    }
}
