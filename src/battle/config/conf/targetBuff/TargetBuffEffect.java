package battle.config.conf.targetBuff;


import java.io.Serializable;

public class TargetBuffEffect implements Serializable {
    private String name;
    private String type;
    private double value;
    private Long delay;
    private int hpPerDelay;
    private double evasionRate;

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "TargetBuffEffect{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", value=" + value +
                ", delay=" + delay +
                ", hpPerDelay=" + hpPerDelay +
                ", evasionRate=" + evasionRate +
                '}';
    }
}
