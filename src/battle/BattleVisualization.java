package battle;

import battle.common.EntityMode;
import battle.common.Point;
import battle.entity.EntityECS;
import battle.factory.EntityFactory;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.List;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;

public class BattleVisualization extends JFrame implements Runnable {

    int width = 7;
    int height = 5;
    int tileWidth = 77;
    int tileHeight = 77;
    int paddingX = 20;
    int paddingY =20;
    int size;

    public static void main(String[] args) throws Exception {
        new BattleStimulation();
        EntityFactory.getInstance().createCannonOwlTower(new battle.common.Point(1, 3), EntityMode.PLAYER);
        EntityFactory.getInstance().createSwordManMonster(new Point(35, 77*3),EntityMode.PLAYER);
    }

    public BattleVisualization() {
        this.setTitle("Snake");
        this.setSize(width*tileWidth, height*tileHeight);
        this.setDefaultCloseOperation(3);
        this.setVisible(true);
    }

    public void paint(Graphics G) {
        G.setColor(Color.WHITE);
        G.fillRect(0, 0, this.getWidth(), this.getHeight());
        G.setColor(Color.BLACK);
        G.setColor(Color.BLUE);
        G.drawRect(paddingX,paddingY, width*tileWidth,height*tileHeight);
        G.setColor(Color.BLACK);
        G.drawRect(paddingX,paddingY, width*tileWidth,height*tileHeight);
        List<EntityECS> entityECSList =
        this.repaint();
    }

    @Override
    public void run() {

    }
}
