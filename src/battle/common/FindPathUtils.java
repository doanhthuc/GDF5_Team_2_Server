package battle.common;

import battle.map.BattleMap;
import battle.config.GameConfig;

import java.util.*;

public class FindPathUtils {
    public static int mapHeight = BattleMap.mapH;
    public static int mapWidth = BattleMap.mapW;

    public static List<Point> findShortestPath(int[][] map, Point start, Point dest) {
        //for BFS
        HashMap<Integer, Boolean> visited = new HashMap();
        visited.clear();
        Deque<Point> deque = new ArrayDeque<>();
        deque.clear();
        //Direction to get neighBor
        List<Integer> dX = Arrays.asList(1, 0, 0, -1);
        List<Integer> dY = Arrays.asList(0, -1, 1, 0);
        //Return StorePath

        List<Point> storePath = new ArrayList<>();
        storePath.clear();
        deque.add(start);
        visited.put((int) (start.getX() * 1000 + start.getY()), true);

        Point top = null;
        boolean find = false;
        while (deque.size() > 0) {
            top = deque.pop();
            if (top.getX() == dest.getX() && top.getY() == dest.getY()) {
                find = true;
                break;
            }
            //get children
            for (int i = 0; i < dX.size(); i++) {
                int childrenX = (int) top.getX() + dX.get(i);
                int childrenY = (int) top.getY() + dY.get(i);
                //Check children in Map
                if ((childrenX < 0) || (childrenX >= mapWidth) || (childrenY < 0) || (childrenY >= mapHeight)) continue;
                //Check Visited
                if (visited.containsKey(childrenX * 1000 + childrenY)) continue;
                //Check Movable
                if ((map[childrenX][childrenY] != GameConfig.MAP.NONE)
                        && (map[childrenX][childrenY] != GameConfig.MAP.ATTACK_SPEED)
                        && (map[childrenX][childrenY] != GameConfig.MAP.ATTACK_RANGE)
                        && (map[childrenX][childrenY] != GameConfig.MAP.ATTACK_DAMAGE)) continue;

                visited.put(childrenX * 1000 + childrenY, true);
                Point children = new Point(childrenX, childrenY);
                deque.add(children);
                children.setFather(top);
            }
        }
        if (find == true) {
            while (top.getFather() != null) {
                storePath.add(top);
                top = top.getFather();
            }
            storePath.add(start);
            List<Point> returnPath = new ArrayList<>();
            for (int i = storePath.size() - 1; i >= 0; i--) {
                returnPath.add(storePath.get(i));
            }
            return returnPath;
        } else {
            //System.out.println("Cannot Find path");
            return null;
        }
    }

    public static List<Point>[][] findShortestPathForEachTile(int[][] map) {
        List<Point>[][] shortestPathForEachTile = new List[mapWidth][mapHeight];
        for (int j = mapHeight - 1; j >= 0; j--)
            for (int i = 0; i < mapWidth; i++) {
                if (findPathAble(map[i][j])) {
                    List<Point> pathForTile = findShortestPath(map, new Point(i, j), new Point(GameConfig.HOUSE_POSITION.x, GameConfig.HOUSE_POSITION.y));
                    if ((pathForTile != null) && pathForTile.size() > 0) shortestPathForEachTile[i][j] = pathForTile;
                }
            }
        return shortestPathForEachTile;
    }

    public static boolean findPathAble(int value) {
        return (value == GameConfig.MAP.NONE || value == GameConfig.MAP.ATTACK_DAMAGE || value == GameConfig.MAP.ATTACK_RANGE || value == GameConfig.MAP.ATTACK_SPEED);
    }

}
