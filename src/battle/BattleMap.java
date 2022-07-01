package battle;

import bitzero.core.P;

import java.awt.*;
import java.util.*;

public class BattleMap {
    public int mapW = 7;
    public int mapH = 5;
    public int buffTileAmount = 3;
    public int[][] map = new int[mapW][mapH + 1];
    public ArrayList<Point> path = new ArrayList<>();

    public BattleMap() {
        this.reset();
        this.genBuffTile();
        this.genPath();
        this.genTree();
        this.genPitCell();
        this.removePath();
        //this.show();
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
    public void removePath(){
        for(int i=0;i<this.mapW;i++)
            for(int j=0;j<this.mapH;j++)
                if (this.map[i][j]==4) this.map[i][j]=0;
    }

    public void genPath() {
        map[0][mapH - 1] = 4;
        PriorityQueue<TileNode> open = new PriorityQueue(new Comparator<TileNode>() {
            @Override
            public int compare(TileNode a, TileNode b) {
                return ((a.h - a.g * 6) - (b.h - b.g * 6));
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
                        child.father = top;
                        open.add(child);
                        closed.put(childPos, child);
                    }
                }
        }
        while (top.father != null) {
            map[top.pos.x][top.pos.y] = 4;
            path.add(new Point(top.pos));
            top = top.father;
        }
        path.add(new Point(0,4));
    }

    public int nodeValue(Point p) {
        int value = 0;
        for (int i = -1; i <= 1; i++)
            for (int j = -1; j <= 1; j++) {
                if (isInBound(p.x + i, p.y + j))
                    if (map[p.x + i][p.y + j] == 1) {
                        value -= 200;
                    }
            }
        return value - (6 - p.x) * 7 - (p.y) * 5;
    }

    public void genTree() {
        ArrayList<Point> turnTileArray = new ArrayList<>();

        for (int i = 0; i < path.size() - 2; i++) {
            Point currentPoint = path.get(i);
            Point diagonPoint = path.get(i + 2);

            if (currentPoint.x != diagonPoint.x && currentPoint.y != diagonPoint.y)
                turnTileArray.add(path.get(i + 1));
        }
        int countTree = 0;
        while (countTree < 2) {
            Point startPoint= new Point(0,5);
            Point endPoint= new Point(6,0);
            Random rd = new Random();
            if (turnTileArray.size() == 0) break;
            int treeindex = rd.nextInt(turnTileArray.size());
            Point turnTile = turnTileArray.get(treeindex);
            turnTileArray.remove(treeindex);
            for (int h = -1; h <= 1; h++)
                for (int k = -1; k <= 1; k++) {
                    if ((Math.abs(h) + Math.abs(k)) == 1) {
                        Point treeTile = new Point(turnTile.x + h, turnTile.y + k);
                        if (isInBound(treeTile) && map[treeTile.x][treeTile.y] != 4)
                            if (checkBuffTileAround(treeTile) == false && map[treeTile.x][treeTile.y] != 5)
                              {
                                map[treeTile.x][treeTile.y] = 5;
                                countTree++;
                                break;
                            }
                    }
                }
        }
    }

    public void genPitCell() {
        boolean finded=false;
        for (int i = 0; i < mapW; i++) {
            for (int j = 0; j < mapH; j++) {
                if ((checkBuffTileAround(new Point(i, j)) == false) && map[i][j] == 0)
                    if (checkPathAround(new Point(i, j))) {
                        map[i][j] = 6;
                        finded = true;
                        break;
                    }
            }
            if (finded == true) break;
        }
    }

    public boolean isInBound(int x, int y) {
        return (x >= 0 && x < mapW && y >= 0 && y < mapH);
    }

    public boolean isInBound(Point p) {
        return (p.x >= 0 && p.x < mapW && p.y >= 0 && p.y < mapH);
    }

    public boolean checkBuffTileAround(Point p) {
        for (int h = -1; h <= 1; h++)
            for (int k = -1; k <= 1; k++) {
                if (isInBound(new Point(p.x + h, p.y + k))) {
                    if (isValuedTile(map[p.x + h][p.y + k])) return true;
                }
            }
        return false;
    }
    public boolean checkPathAround(Point p) {
        for (int h = -1; h <= 1; h++)
            for (int k = -1; k <= 1; k++) {
                if (isInBound(new Point(p.x + h, p.y + k))) {
                    if (map[p.x+h][p.y+k]==4) return true;
                }
            }
        return false;
    }

    public boolean isValuedTile(int value) {
        if (value == 1 || value == 2 || value == 3 || value == 5) return true;
        return false;
    }

    public boolean compareNode(Point a, Point b) {
        return (a.x == b.x && a.y == b.y);
    }
}
