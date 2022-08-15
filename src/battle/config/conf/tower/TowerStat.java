package battle.config.conf.tower;

import java.io.Serializable;

public class TowerStat implements Serializable {
    public final Double damage;
    public final Integer attackSpeed;
    public final double range;
    public final Double bulletRadius;
    public final Double bulletSpeed;
    public final short key;

    public Double getDamage() {
        return damage;
    }

    public Integer getAttackSpeed() {
        return attackSpeed;
    }

    public double getRange() {
        return range;
    }

    public Double getBulletRadius() {
        return bulletRadius;
    }

    public Double getBulletSpeed() {
        return bulletSpeed;
    }

    public short getKey() {
        return key;
    }

    public TowerStat(short key, double damage, int attackSpeed, double range, double bulletRadius, double bulletSpeed) {
        this.key = key;
        this.damage = damage;
        this.attackSpeed = attackSpeed;
        this.range = range;
        this.bulletRadius = bulletRadius;
        this.bulletSpeed = bulletSpeed;
    }

    @Override
    public String toString() {
        return "TowerStat{" +
                "damage=" + damage +
                ", attackSpeed=" + attackSpeed +
                ", range=" + range +
                ", bulletRadius=" + bulletRadius +
                ", bulletSpeed=" + bulletSpeed +
                ", key=" + key +
                '}';
    }
}
