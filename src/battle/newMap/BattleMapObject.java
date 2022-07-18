package battle.newMap;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BattleMapObject {
    private final int width;
    private final int height;
    private final List<List<TileObject>> battleMap = new ArrayList<List<TileObject>>();

    public BattleMapObject(int[][] simpleMap) {
        this.width = simpleMap[0].length;
        this.height = simpleMap.length;
        initBattleMap(simpleMap);
    }

    public void initBattleMap(int[][] map) {
        for (int i = 0; i < this.height; i++) {
            List<TileObject> row = new ArrayList<>();
            for (int j = 0; j < this.width; j++) {
                if (map[i][j] == BuffTileType.NONE.value) {
                    row.add(new TileObject(new Point(i, j), BuffTileType.NONE, new ObjectInTile(ObjectInTileType.NONE)));
                } else if (map[i][j] == BuffTileType.ATTACK_SPEED_UP.value) {
                    row.add(new TileObject(new Point(i, j), BuffTileType.ATTACK_SPEED_UP, new ObjectInTile(ObjectInTileType.NONE)));
                } else if (map[i][j] == BuffTileType.DAMAGE_UP.value) {
                    row.add(new TileObject(new Point(i, j), BuffTileType.DAMAGE_UP, new ObjectInTile(ObjectInTileType.NONE)));
                } else if (map[i][j] == BuffTileType.ATTACK_RANGE_UP.value) {
                    row.add(new TileObject(new Point(i, j), BuffTileType.ATTACK_RANGE_UP, new ObjectInTile(ObjectInTileType.NONE)));
                } else if (map[i][j] == 5) {
                    row.add(new TileObject(new Point(i, j), BuffTileType.NONE, new Tree(100)));
                } else if (map[i][j] == 6) {
                    row.add(new TileObject(new Point(i, j), BuffTileType.NONE, new Pit()));
                }
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
                if (battleMap.get(i).get(j).getBuffCellType().value != BuffTileType.NONE.value) {
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
