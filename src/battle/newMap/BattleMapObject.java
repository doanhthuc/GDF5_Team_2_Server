package battle.newMap;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BattleMapObject {
    private final int width;
    private final int height;
    private final List<List<CellObject>> battleMap = new ArrayList<List<CellObject>>();

    public BattleMapObject(int[][] simpleMap) {
        this.width = simpleMap[0].length;
        this.height = simpleMap.length;
        initBattleMap(simpleMap);
    }

    public void initBattleMap(int[][] map) {
        for (int i = 0; i < this.height; i++) {
            List<CellObject> row = new ArrayList<>();
            for (int j = 0; j < this.width; j++) {
                if (map[i][j] == BuffCellType.NONE.value) {
                    row.add(new CellObject(new Point(i, j), BuffCellType.NONE, new ObjectInCell(ObjectInCellType.NONE)));
                } else if (map[i][j] == BuffCellType.ATTACK_SPEED_UP.value) {
                    row.add(new CellObject(new Point(i, j), BuffCellType.ATTACK_SPEED_UP, new ObjectInCell(ObjectInCellType.NONE)));
                } else if (map[i][j] == BuffCellType.DAMAGE_UP.value) {
                    row.add(new CellObject(new Point(i, j), BuffCellType.DAMAGE_UP, new ObjectInCell(ObjectInCellType.NONE)));
                } else if (map[i][j] == BuffCellType.ATTACK_RANGE_UP.value) {
                    row.add(new CellObject(new Point(i, j), BuffCellType.ATTACK_RANGE_UP, new ObjectInCell(ObjectInCellType.NONE)));
                } else if (map[i][j] == 5) {
                    row.add(new CellObject(new Point(i, j), BuffCellType.NONE, new Tree(100)));
                } else if (map[i][j] == 6) {
                    row.add(new CellObject(new Point(i, j), BuffCellType.NONE, new Pit()));
                }
            }
            battleMap.add(row);
        }
    }

    public CellObject getCellObject(int x, int y) {
        return battleMap.get(x).get(y);
    }

    public CellObject getCellObject(Point pos) {
        return battleMap.get(pos.x).get(pos.y);
    }

    public ObjectInCellType setObjectInCellType(int typeId) {
        return ObjectInCellType.getObjectInCellTypeByTypeId(typeId);
    }

    public Tower putTowerIntoMap(Point tilePos, int towerId) {
        CellObject cellObject = this.battleMap.get(tilePos.x).get(tilePos.y);
        return cellObject.buildTower(towerId, 1);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<List<CellObject>> getBattleMap() {
        return battleMap;
    }

    public void showConsole() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (battleMap.get(i).get(j).getBuffCellType().value != BuffCellType.NONE.value) {
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
