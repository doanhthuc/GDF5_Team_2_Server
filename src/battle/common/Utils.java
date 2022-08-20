package battle.common;


import battle.component.common.PositionComponent;
import battle.config.GameConfig;
import battle.entity.EntityECS;
import bitzero.core.P;

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
        if (mode == EntityMode.PLAYER) {
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
            yy = GameConfig.MAP_HEIGHT * GameConfig.TILE_HEIGHT / 2 - y * GameConfig.TILE_HEIGHT - GameConfig.TILE_HEIGHT / 2;
        }
        return new Point(xx, yy);
    }

    public static double euclidDistance(Point pointA, Point pointB) {
        double distance = Math.sqrt(Math.pow(pointA.getX() - pointB.getX(), 2) + Math.pow(pointA.getY() - pointB.getY(), 2));
        return distance;
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


    public static Point calculateVelocityVector(Point startPos, Point targetPos, double speed) {
        double Xa = startPos.getX(), Ya = startPos.getY(), Xb = targetPos.getX(), Yb = targetPos.getY();
        if (Xa - Xb == 0)
            return new Point(0, Math.signum(Yb - Ya) * speed);
        if (Ya - Yb == 0)
            return new Point(Math.signum(Xb - Xa) * speed, 0);

        double k = Math.abs((Ya - Yb) / (Xa - Xb));
        double speedX = Math.sqrt(speed * speed / (1 + k * k));
        double speedY = k * speedX;
        return new Point(
                Math.signum(Xb - Xa) * speedX,
                Math.signum(Yb - Ya) * speedY
        );
    }

    public static Point pixel2Cell(double x, double y, EntityMode mode) {
        Point tilePos = pixel2Tile(x, y, EntityMode.PLAYER);
        int cellX, cellY;
        double paddingLeftX = cell2Pixel(0, 0, mode).getX();
        return null;
    }


    public static Point cell2Pixel(double cellX, double cellY, EntityMode mode) {
        double x = 0, y = 0;
        if (mode == EntityMode.PLAYER) {
            x = (cellX + 1) * cellWidth - mapWidthPixel / 2 - cellWidth / 2;
            y = (cellY + 1) * cellHeight - mapHeightPixel / 2 - cellHeight / 2;
        } else if (mode == EntityMode.OPPONENT) {
            x = mapWidthPixel / 2 - (cellX + 1) * cellWidth + cellWidth / 2;
            y = mapHeightPixel / 2 - (cellY + 1) * cellHeight + cellHeight / 2;
        }
        return new Point(x, y);
    }

    public static int getDirectionOf2Tile(Point pointA, Point pointB) {
        int direction1 = 0;
        int direction2 = 0;
        if (pointA.x != pointB.x) {
            direction1 = (int) ((pointB.x - pointA.x) / Math.abs(pointB.x - pointA.x));
        }

        if (pointA.y != pointB.y) {
            direction2 = (int) ((pointB.y - pointA.y) / Math.abs(pointB.y - pointA.y)) * 3;
        }
        return direction1 + direction2;
    }

    public static List<Point> tileArray2PixelCellArray(List<Point> tileArr, EntityMode mode) {
        List<Point> cellArr = new ArrayList<>();
        double cellX, cellY, beforeCellX = 0, beforeCellY = 0;
        int magicNumber = 27;
        int moduleCellRange = 4;
        int cellBound = 4;
        int divideAmount = 5;
        for (int i = 0; i < tileArr.size() - 1; i++) {
            int direction = Utils.getDirectionOf2Tile(tileArr.get(i), tileArr.get(i + 1));

            if (i == 0) {
                beforeCellX = cellBound;
                beforeCellY = cellBound;
            }

            if (cellArr.size() == 0) cellArr.add(Utils.cell2Pixel(tileArr.get(i).getX() * cellsEachTile + beforeCellX,
                    tileArr.get(i).getY() * cellsEachTile + beforeCellY, mode));
            switch (direction) {
                case Direction.BOTTOM:
                    if (beforeCellX >= cellBound && beforeCellX < cellBound + moduleCellRange) {
                        cellX = tileArr.get(i).getX() * cellsEachTile + beforeCellX;
                        cellY = (tileArr.get(i).getY() - 1) * cellsEachTile + beforeCellY;
                    } else {
                        cellX = tileArr.get(i).getX() * cellsEachTile + (beforeCellY + magicNumber) % moduleCellRange + cellBound;
                        cellY = (tileArr.get(i).getY() - 1) * cellsEachTile + cellsEachTile - 1;
                        beforeCellX = (beforeCellY + magicNumber) % moduleCellRange + cellBound;

                        if (cellArr.size() != 0) {
                            Point lastCell = cellArr.get(cellArr.size() - 1);
                            Point nextCell = Utils.cell2Pixel(cellX, cellY, mode);
                            List<Point> divideGapCellPath = Utils.divideCellPath(lastCell, nextCell, divideAmount);
                            cellArr.addAll(divideGapCellPath);
                        }
                    }
                    cellArr.add(Utils.cell2Pixel(cellX, cellY, mode));
                    beforeCellY = cellsEachTile - 1;
                    break;
                case Direction.RIGHT:
                    if (beforeCellY >= cellBound && beforeCellY < cellBound + moduleCellRange) {
                        cellX = (tileArr.get(i).getX() + 1) * cellsEachTile;
                        cellY = tileArr.get(i).getY() * cellsEachTile + beforeCellY;
                    } else {
                        cellX = (tileArr.get(i).getX() + 1) * cellsEachTile;
                        cellY = tileArr.get(i).getY() * cellsEachTile + (beforeCellX + magicNumber) % moduleCellRange + cellBound;
                        beforeCellY = (beforeCellX + magicNumber) % moduleCellRange + cellBound;

                        if (cellArr.size() != 0) {
                            Point lastCell = cellArr.get(cellArr.size() - 1);
                            Point nextCell = Utils.cell2Pixel(cellX, cellY, mode);
                            List<Point> divideGapCellPath = Utils.divideCellPath(lastCell, nextCell, divideAmount);
                            cellArr.addAll(divideGapCellPath);
                        }
                    }

                    cellArr.add(Utils.cell2Pixel(cellX, cellY, mode));
                    beforeCellX = 0;
                    break;
                case Direction.LEFT:
                    if (beforeCellY >= cellBound && beforeCellY < cellBound + moduleCellRange) {
                        cellX = (tileArr.get(i).getX() - 1) * cellsEachTile + cellsEachTile - 1;
                        cellY = (tileArr.get(i).getY()) * cellsEachTile + beforeCellY;
                    } else {
                        cellX = (tileArr.get(i).getX() - 1) * cellsEachTile + cellsEachTile - 1;
                        cellY = tileArr.get(i).y * cellsEachTile + (beforeCellX + magicNumber) % moduleCellRange + cellBound;
                        beforeCellY = (beforeCellX + magicNumber) % moduleCellRange + cellBound;

                        Point lastCell = cellArr.get(cellArr.size() - 1);
                        Point nextCell = Utils.cell2Pixel(cellX, cellY, mode);

                        List<Point> divideGapCellPath = Utils.divideCellPath(lastCell, nextCell, divideAmount);
                        cellArr.addAll(divideGapCellPath);
                    }
                    cellArr.add(Utils.cell2Pixel(cellX, cellY, mode));
                    beforeCellX = cellsEachTile - 1;
                    break;
                case Direction.TOP:
                    if (beforeCellX >= cellBound && beforeCellX < cellBound + moduleCellRange) {
                        cellX = tileArr.get(i).getX() * cellsEachTile + beforeCellX;
                        cellY = (tileArr.get(i).getY() + 1) * cellsEachTile;
                    } else {
                        cellX = tileArr.get(i).getX() * cellsEachTile + (beforeCellY + magicNumber) % moduleCellRange + cellBound;
                        cellY = (tileArr.get(i).getY() + 1) * cellsEachTile;
                        beforeCellX = (beforeCellY + magicNumber) % moduleCellRange + cellBound;

                        Point lastCell = cellArr.get(cellArr.size() - 1);
                        Point nextCell = Utils.cell2Pixel(cellX, cellY, mode);

                        List<Point> divideGapCellPath = Utils.divideCellPath(lastCell, nextCell, divideAmount);
                        cellArr.addAll(divideGapCellPath);
                    }
                    cellArr.add(Utils.cell2Pixel(cellX, cellY, mode));

                    beforeCellY = 0;

                    break;
            }
        }
        return cellArr;
    }

    public static List<Point> divideCellPath(Point pointA, Point pointB, int divideAmount) {
        List cellArr = new ArrayList();
        for (int i = 1; i < divideAmount; i++) {
            double cellX = pointA.getX() + ((pointB.getX() - pointA.getX()) * i) / divideAmount;
            double cellY = pointA.getY() + ((pointB.getY() - pointA.getY()) * i) / divideAmount;
            cellArr.add(new Point(cellX, cellY));
        }
        return cellArr;
    }

    public static double _distanceFrom(EntityECS tower, EntityECS monster) {
        PositionComponent towerPos = (PositionComponent) tower.getComponent(PositionComponent.typeID);
        PositionComponent monsterPos = (PositionComponent) monster.getComponent(PositionComponent.typeID);
        return Utils.euclidDistance(new Point(towerPos.getX(), towerPos.getY()), new Point(monsterPos.getX(), monsterPos.getY()));
    }


    public static boolean validateTilePos(Point tilePos) {
        return tilePos.getX() >= 0 && tilePos.getX() < GameConfig.MAP_WIDTH
                && tilePos.getY() >= 0 && tilePos.getY() < GameConfig.MAP_HEIGHT;
    }

    public static Utils getInstance() {
        if (_instance == null) _instance = new Utils();
        return _instance;
    }

    public static class Direction {
        public static final int RIGHT = 1;
        public static final int LEFT = -1;
        public static final int TOP = 3;
        public static final int BOTTOM = -3;
        public static final int RIGHT_TOP = 4;
        public static final int RIGHT_BOTTOM = -2;
        public static final int LEFT_TOP = 4;
        public static final int LEFT_BOTTOM = -4;
    }

    public static short convertMode2Short(EntityMode mode) {
        return (short) (mode.getValue() == EntityMode.PLAYER.getValue() ? 1 : 0);
    }

    public static short convertBoolean2Short(Boolean val) {
        return (short) (val ? 1 : 0);
    }
}
