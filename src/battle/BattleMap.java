package battle;

import bitzero.core.P;

import java.awt.*;
import java.util.*;

public class BattleMap {
    public int mapW = 7;
    public int mapH = 5;
    public int buffTileAmount = 3;
    public int[][] map = new int[mapW][mapH + 1];

    public BattleMap() {
        this.reset();
        this.show();
    }

    public void genBuffTile() {

//        for(int i=0;i<buffTileRandom.size();i++)
//            System.out.println(buffTileRandom.get(i).x+" "+buffTileRandom.get(i).y);
//        while (true) {

        Random rd = new Random();
        ArrayList<Point> buffTileRandom;
        boolean finishGenBuffTle = false;
        while (finishGenBuffTle == false) {
            int buffTileType = 1;
            this.reset();
            buffTileRandom = new ArrayList<>();
            for (int i = 1; i <= mapW - 2; i++)
                for (int j = 1; j <= mapH - 2; j++) {
                    if (i == 1 && j == mapH - 2) continue;
                    buffTileRandom.add(new Point(i, j));
                }
            while (buffTileType <= buffTileAmount) {
                if (buffTileRandom.size() == 0) break;
                int rdPoint = rd.nextInt(buffTileRandom.size());
                int x = buffTileRandom.get(rdPoint).x;
                int y = buffTileRandom.get(rdPoint).y;
                System.out.println("Random " + x + " " + y);
                map[x][y] = buffTileType;
                buffTileType++;
                int i = 0;
                while (i < buffTileRandom.size()) {
                    if (checkAround(new Point(x, y), buffTileRandom.get(i))) {
                        buffTileRandom.remove(i);
                        continue;
                    }
                    i++;
                }
            }
            if (buffTileType == buffTileAmount + 1) finishGenBuffTle = true;
        }
    }

    public void reset() {
        for (int j = 0; j < mapH; j++)
            for (int i = 0; i < mapW; i++)
                map[i][j] = 0;
    }

    public boolean checkAround(Point a, Point b) {
        if (Math.abs(a.x - b.x) + Math.abs(a.y - b.y) <= 1) return true;
        if (Math.abs(a.x - b.x) + Math.abs(a.y - b.y) == 2) {
            if (Math.abs(a.x - b.x) == 1) return true;
        }
        return false;
    }

    public void show() {
        for (int j = mapH - 1; j >= 0; j--) {
            for (int i = 0; i < mapW; i++)
                System.out.print(this.map[i][j] + " ");
            System.out.println();
        }
    }

    public void genPath() {
        map[0][mapH - 1] = 4;
        PriorityQueue<TileNode> open = new PriorityQueue(new Comparator<TileNode>() {
            @Override
            public int compare(TileNode a, TileNode b) {
                return ((a.h * 3 - a.g * 30) - (b.h * 3 - b.g * 30));
                //return (a.h-b.h);
                // return (a.g-b.g);
            }
        });
        Map<Point, TileNode> closed = new HashMap<>();

        TileNode startNode = new TileNode(0, 0, new Point(0, mapH - 1));
        TileNode endNode = new TileNode(0, 0, new Point(mapW - 1, 0));
        TileNode top = null;

        open.add(startNode);
        closed.put(startNode.pos, startNode);
        while (open.size() != 0) {
            top = open.remove();
            //map[top.pos.x][top.pos.y]=4;
            //this.show();
            //System.out.println();
            if (compareNode(top.pos, endNode.pos) == true) {
                break;
            }
            for (int dx = 1; dx >= -1; dx--)
                for (int dy = 1; dy >= -1; dy--) {
                    if ((Math.abs(dx) + Math.abs(dy)) == 1) {
                        Point childPos = new Point(top.pos.x + dx, top.pos.y + dy);
                        if (isInBound(childPos.x, childPos.y) == false || map[childPos.x][childPos.y] != 0) continue;
                        TileNode child = new TileNode(nodeValue(childPos), top.g + 1, childPos);
                        if (closed.get(childPos) != null) continue;
                        System.out.println(childPos.x + " " + childPos.y);
                        child.father = top;
                        open.add(child);
                        closed.put(childPos, child);
                    }
                }
        }
        while (top.father != null) {
            map[top.pos.x][top.pos.y] = 4;
            top = top.father;
            // System.out.println(top.g+" "+top.h+" "+top.pos.x+" "+top.pos.y);
        }
    }

    public int nodeValue(Point p) {
        int value = 0;
        for (int i = 0; i < mapW; i++)
            for (int j = 0; j < mapH; j++)
                if (this.map[i][j] != 0)
                    value += Math.abs(i - p.x) + Math.abs(j - p.y);
        return value - Math.abs(p.x - mapW+1)   - Math.abs(p.y);
    }

    public boolean isInBound(int x, int y) {
        return (x >= 0 && x < mapW && y >= 0 && y < mapH);
    }

    public boolean compareNode(Point a, Point b) {
        return (a.x == b.x && a.y == b.y);
    }
}
