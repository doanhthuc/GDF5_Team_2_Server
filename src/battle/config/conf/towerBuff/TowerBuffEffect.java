package battle.config.conf.towerBuff;

import java.io.Serializable;

public class TowerBuffEffect implements Serializable {
    private String name;
    private String type;
    private double value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
