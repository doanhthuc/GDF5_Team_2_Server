package battle.common;


import battle.config.GameConfig;
import battle.entity.EntityECS;
import bitzero.core.G;

public class Utils {
    private static Utils _instance = null;

    public static Point pixel2Tile(double xx, double yy, EntityMode mode) {
        if (mode.getValue() == EntityMode.PLAYER.getValue()) {
            xx = xx + GameConfig.MAP_WIDTH * GameConfig.TILE_WIDTH / 2;
            yy = yy + GameConfig.MAP_HEIGHT * GameConfig.TILE_HEIGHT / 2;
        } // FIXME add mode== GameConfig.OPPONENT
        else {
            xx = GameConfig.MAP_WIDTH * GameConfig.TILE_WIDTH / 2 - xx;
            yy = GameConfig.MAP_HEIGHT * GameConfig.TILE_HEIGHT / 2 - yy;
        }
        double x = Math.floor(xx / GameConfig.TILE_WIDTH);
        double y = Math.floor(yy / GameConfig.TILE_HEIGHT);
        return new Point(x, y);
    }

    public static Point tile2Pixel(double x, double y, EntityMode mode) {
        double xx, yy;
        if (mode.getValue() == EntityMode.PLAYER.getValue()) {
            xx = x * GameConfig.TILE_WIDTH - GameConfig.MAP_WIDTH * GameConfig.TILE_WIDTH / 2 + GameConfig.TILE_WIDTH / 2;
            yy = y * GameConfig.TILE_HEIGHT - GameConfig.MAP_HEIGHT * GameConfig.TILE_HEIGHT / 2 + GameConfig.TILE_HEIGHT / 2;
        } // FIXME add mode== GameConfig.OPPONENT
        else {
            xx = GameConfig.MAP_WIDTH * GameConfig.TILE_WIDTH / 2 - x * GameConfig.TILE_WIDTH - GameConfig.TILE_WIDTH / 2;
            yy = GameConfig.MAP_HEIGHT * GameConfig.TILE_HEIGHT / 2 - x * GameConfig.TILE_HEIGHT - GameConfig.TILE_HEIGHT / 2;
        }
        return new Point(xx, yy);
    }

    public static Point Tile2Pixel() {
        return null;
    }

    public static double euclidDistance(Point a, Point b) {
        return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    }

    ;

    public static boolean isMonster(EntityECS entity) {
        for (Integer id : GameConfig.GROUP_ID.MONSTER_ENTITY) {
            if (id == entity.getId()) return true;
        }
        return false;
    }

    public static boolean isBullet(EntityECS entity) {
        for (Integer id : GameConfig.GROUP_ID.BULLET_ENTITY) {
            if (id == entity.getId()) return true;
        }
        return false;
    }

    public Point calculateVelocityVector(Point startPos, Point targetPos, double speed) {
        double Xa = startPos.x, Ya = startPos.y, Xb = targetPos.x, Yb = targetPos.y;
        if (Xa - Xb == 0) return new Point(0, Math.signum((Yb - Ya) * speed));
        if (Ya - Yb == 0) return new Point(Math.signum((Xb - Xa) * speed), 0);
        double k = Math.abs(((Ya - Yb) / Xa - Xb));
        double speedX = Math.sqrt(speed * speed / (1 + k * k));
        double speedY = k * speedX;
        return new Point(Math.signum((Xb - Xa) * speedX), Math.signum((Yb - Ya) * speedY));
    }

    public static Utils getInstance() {
        if (_instance == null) _instance = new Utils();
        return _instance;
    }
}
