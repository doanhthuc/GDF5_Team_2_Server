package battle.newMap;

public enum BuffCellType {
    NONE(0),
    ATTACK_SPEED_UP(1),
    DAMAGE_UP(3),
    ATTACK_RANGE_UP(2);

    public final int value;

    BuffCellType (final int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "BuffCellType{" +
                "value=" + value +
                '}';
    }
}
