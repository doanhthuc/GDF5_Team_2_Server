package battle;

import java.util.Random;

public class MapMatrix {
    public int mapW=7;
    public int mapH=5;
    public int[][] map = new int[mapW][mapH+1];

    public MapMatrix() {
    }

    public void genMap() {
        int buffTileAmount=0;
        Random rd= new Random();
        while (buffTileAmount<3){
            int buffTileX= rd.nextInt(mapW-1)+1;
            int buffTileY= rd.nextInt(mapH-1)+1;
            if ((buffTileX==1)&& (buffTileY==mapH-1)) continue;
        }
    }
}
