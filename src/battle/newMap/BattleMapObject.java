package battle.newMap;

import battle.common.Utils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BattleMapObject {
    private final int width;
    private final int height;
    private final List<List<TileObject>> battleMap = new ArrayList<>();

    public BattleMapObject(int[][] simpleMap) {
        this.width = simpleMap[0].length;
        this.height = simpleMap.length;
        System.out.println(this.getWidth() + " " + this.getHeight());
        initBattleMap(simpleMap);
    }

    public void initBattleMap(int[][] map) {
        for (int i = 0; i < this.height; i++) {
            List<TileObject> row = new ArrayList<>();
            for (int j = 0; j < this.width; j++) {
                Point point = new Point(i, j);
                TileType tileType = TileType.getTileTypeByValue(map[i][j]);
                ObjectInTile objectInTile = null;
                switch (tileType) {
                    case NONE:
                    case ATTACK_SPEED_UP:
                    case ATTACK_RANGE_UP:
                    case DAMAGE_UP:
                    case PATH:
                        objectInTile = new ObjectInTile(ObjectInTileType.NONE);
                        break;
                    case TREE:
                        objectInTile = new Tree(100);
                        break;
                    case PIT:
                        objectInTile = new Pit();
                        break;
                }
                row.add(new TileObject(point, tileType, objectInTile));
            }
            battleMap.add(row);
        }
    }

    public TileObject getTileObject(int x, int y) {
        if (x < 0 || x >= this.height || y < 0 || y >= this.width) {
            return null;
        }
        return battleMap.get(x).get(y);
    }

    public TileObject getTileObject(Point pos) {
        return battleMap.get(pos.x).get(pos.y);
    }

    public ObjectInTileType setObjectInCellType(int typeId) {
        return ObjectInTileType.getObjectInCellTypeByTypeId(typeId);
    }

    public Tower putTowerIntoMap(long entityId, Point tilePos, int towerId) {
        return this.battleMap.get(tilePos.x).get(tilePos.y).buildTower(entityId, towerId, 1);
    }

    public boolean isHavingTowerInTile(int x, int y) {
        if (!Utils.validateTilePos(new battle.common.Point(x, y))) {
            try {
                throw new Exception("Invalid tile pos");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        TileObject tileObject = this.battleMap.get(x).get(y);
        return tileObject.isHavingTower();
    }

    public Tower getTowerInTile(int x, int y) {
        TileObject tileObject = this.battleMap.get(x).get(y);
        return (Tower) tileObject.getObjectInTile();
    }

    public TileType getTileTypeByTilePos(int x, int y) {
        return this.battleMap.get(x).get(y).getBuffCellType();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<List<TileObject>> getBattleMap() {
        return battleMap;
    }

    public void showConsole() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (battleMap.get(i).get(j).getBuffCellType().value != TileType.NONE.value) {
                    System.out.print(battleMap.get(i).get(j).getBuffCellType().value + " ");
                    continue;
                }
                if (battleMap.get(i).get(j).getObjectInTile() == null) {
                    System.out.print("0 ");
                } else {
                    System.out.println(battleMap.get(i).get(j).getObjectInTile() + " ");
                }
            }
            System.out.println();
        }
    }
}
