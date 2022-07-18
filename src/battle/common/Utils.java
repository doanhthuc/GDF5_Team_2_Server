package battle.common;


import battle.config.GameConfig;
import battle.entity.EntityECS;

public class Utils {
    private static Utils _instance = null;

    public Point calculateVelocityVector(Point startPos, Point targetPos, double speed) {
        double Xa = startPos.x, Ya = startPos.y, Xb = targetPos.x, Yb = targetPos.y;
        if (Xa - Xb == 0) return new Point(0, Math.signum((Yb - Ya) * speed));
        if (Ya - Yb == 0) return new Point(Math.signum((Xb - Xa) * speed), 0);
        double k = Math.abs(((Ya - Yb) / Xa - Xb));
        double speedX = Math.sqrt(speed * speed / (1 + k * k));
        double speedY = k * speedX;
        return new Point(Math.signum((Xb - Xa) * speedX), Math.signum((Yb - Ya) * speedY));
    }

    public static Point pixel2Tile(double xx, double yy, String mode) {
        if (mode == "") mode = GameConfig.PLAYER;
        if (mode == GameConfig.PLAYER) {
            int x = (int) Math.floor(xx / GameConfig.TILE_WIDTH);
            int y = (int) Math.floor(yy / GameConfig.TILE_HEIGHT);
            return new Point(x, y);
        }
        return null;
    }

    public static Point tile2Pixel(double xx, double yy) {
        double x = GameConfig.TILE_WIDTH * xx + GameConfig.TILE_WIDTH / 2;
        double y = GameConfig.TILE_HEIGHT * yy + GameConfig.TILE_HEIGHT / 2;
        return new Point(x, y);
    }

    public static Point Tile2Pixel() {
        return null;
    }

    ;

    public static double euclidDistance(Point a, Point b) {
        return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    }

    public static boolean isMonster(EntityECS entity) {
        for (Integer id : GameConfig.GROUP_ID.MONSTER_ENTITY) {
            if (id == entity.id) return true;
        }
        return false;
    }

    public static boolean isBullet(EntityECS entity) {
        for (Integer id : GameConfig.GROUP_ID.BULLET_ENTITY) {
            if (id == entity.id) return true;
        }
        return false;
    }
}