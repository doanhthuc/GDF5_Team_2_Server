package battle.newMap;

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

    public TileObject getCellObject(int x, int y) {
        return battleMap.get(x).get(y);
    }

    public TileObject getCellObject(Point pos) {
        return battleMap.get(pos.x).get(pos.y);
    }

    public ObjectInTileType setObjectInCellType(int typeId) {
        return ObjectInTileType.getObjectInCellTypeByTypeId(typeId);
    }

    public Tower putTowerIntoMap(Point tilePos, int towerId) {
        TileObject tileObject = this.battleMap.get(tilePos.x).get(tilePos.y);
        return tileObject.buildTower(towerId, 1);
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
                if (battleMap.get(i).get(j).getObjectInCell() == null) {
                    System.out.print("0 ");
                } else {
                    System.out.println(battleMap.get(i).get(j).getObjectInCell() + " ");
                }
            }
            System.out.println();
        }
    }
}
