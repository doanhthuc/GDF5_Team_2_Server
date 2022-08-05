package battle.common;

enum QuadTreeDirection {

    TOP_RIGHT(0),
    TOP_LEFT(1),
    BOTTOM_LEFT(2),
    BOTTOM_RIGHT(3);

    public int value;

    QuadTreeDirection(int value) {
        this.value = value;
    }
}
