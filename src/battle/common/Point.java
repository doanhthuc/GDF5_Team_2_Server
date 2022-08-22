package battle.common;

public class Point {
    public double x;
    public double y;
    public Point father = null;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point o) {
        this.x = o.x;
        this.y = o.y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public void setFather(Point father) {
        this.father = father;
    }

    public Point getFather() {
        return this.father;
    }

    public Point oppositePoint() {
        return new Point(-1 * this.x, -1 * this.y);
    }
}

