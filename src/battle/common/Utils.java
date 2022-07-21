package battle.common;


import battle.component.common.PositionComponent;
import battle.config.GameConfig;
import battle.entity.EntityECS;
import bitzero.core.G;
import bitzero.core.P;
import com.sun.org.apache.bcel.internal.generic.DCONST;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    private static Utils _instance = null;
    public static int cellsEachTile = 11;
    public static double cellWidth = GameConfig.TILE_WIDTH / cellsEachTile;
    public static double cellHeight = GameConfig.TILE_HEIGHT / cellsEachTile;
    public static int cellsX = GameConfig.MAP_WIDTH * cellsEachTile;
    public static int cellsY = GameConfig.MAP_HEIGHT * cellsEachTile;
    public static double mapWidthPixel = GameConfig.MAP_WIDTH * GameConfig.TILE_WIDTH;
    public static double mapHeightPixel = GameConfig.MAP_HEIGHT * GameConfig.TILE_HEIGHT;


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

    public static double euclidDistance(Point pointA, Point pointB) {
        return Math.sqrt(Math.pow(pointA.getX() - pointB.getX(), 2) + Math.pow(pointA.getX() - pointB.getY(), 2));
    }

    public static double euclidDistance(PositionComponent pointA, PositionComponent pointB) {
        return Math.sqrt(Math.pow(pointA.getX() - pointB.getX(), 2) + Math.pow(pointA.getY() - pointB.getY(), 2));
    }

    public static double euclidDistance(PositionComponent pointA, Point pointB) {
        return Math.sqrt(Math.pow(pointA.getX() - pointB.getX(), 2) + Math.pow(pointA.getY() - pointB.getY(), 2));
    }

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
        double Xa = startPos.getX(), Ya = startPos.getY(), Xb = targetPos.getX(), Yb = targetPos.getY();
        if (Xa - Xb == 0)
            return new Point(0, Math.signum((Yb - Ya) * speed));
        if (Ya - Yb == 0)
            return new Point(Math.signum((Xb - Xa) * speed), 0);

        double k = Math.abs(((Ya - Yb) / Xa - Xb));
        double speedX = Math.sqrt(speed * speed / (1 + k * k));
        double speedY = k * speedX;
        return new Point(Math.signum((Xb - Xa) * speedX), Math.signum((Yb - Ya) * speedY));
    }

    public Point pixel2Cell(double x, double y, EntityMode mode) {
        Point tilePos = pixel2Tile(x, y, EntityMode.PLAYER);
        int cellX, cellY;
        double paddingLeftX = cell2Pixel(0, 0, mode).getX();
        return null;
    }

    public Point cell2Pixel(int cellX, int cellY, EntityMode mode) {
        double x = 0, y = 0;
        if (mode == EntityMode.PLAYER) {
            x = (cellX + 1) * cellWidth - mapWidthPixel / 2 - cellWidth / 2;
            y = (cellY + 1) * cellHeight - mapWidthPixel / 2 - cellHeight / 2;
        } else if (mode == EntityMode.OPPONENT) {
            x = mapWidthPixel / 2 - (cellX + 1) * cellWidth + cellWidth / 2;
            y = mapHeightPixel / 2 - (cellX + 1) * cellHeight + cellHeight / 2;
        }
        return new Point(x, y);
    }

    public List<Point> tileArray2PixelCellArray(List<Point> tileArr, EntityMode mode) {
        if (tileArr.size() < 2) return null;
        List<Point> cellArr = new ArrayList<>();
        int cellX, cellY, beforeCellX, beforeCellY;
        int magicNumber = 27;
        int moduleCellRange = 4;
        int cellBound = 4;
        int divideAmount = 3;
        for (int i = 0; i < tileArr.size() - 1; i++) {
            
        }
    }

    public static Utils getInstance() {
        if (_instance == null) _instance = new Utils();
        return _instance;
    }
}
