package battle.config.GameStat;

import java.util.HashMap;

public class TowerStat2 {
    private double damage;
    private double attackSpeed;
    private double range;
    private double bulletRadius;
    private double bulletSpeed;

    public TowerStat2(double damage, double attackSpeed, double range, double bulletRadius, double bulletSpeed) {
        this.damage = damage;
        this.attackSpeed = attackSpeed;
        this.range = range;
        this.bulletRadius = bulletRadius;
        this.bulletSpeed = bulletSpeed;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public double getAttackSpeed() {
        return attackSpeed;
    }

    public void setAttackSpeed(double attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public double getBulletRadius() {
        return bulletRadius;
    }

    public void setBulletRadius(double bulletRadius) {
        this.bulletRadius = bulletRadius;
    }

    public double getBulletSpeed() {
        return bulletSpeed;
    }

    public void setBulletSpeed(double bulletSpeed) {
        this.bulletSpeed = bulletSpeed;
    }
}
