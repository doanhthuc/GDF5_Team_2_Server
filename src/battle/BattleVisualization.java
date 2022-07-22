package battle;

import battle.common.EntityMode;
import battle.common.Point;
import battle.component.common.CollisionComponent;
import battle.component.common.PositionComponent;
import battle.component.info.BulletInfoComponent;
import battle.component.info.MonsterInfoComponent;
import battle.component.info.TowerInfoComponent;
import battle.entity.EntityECS;
import battle.factory.EntityFactory;
import battle.manager.EntityManager;
import battle.system.AttackSystem;
import battle.system.CollisionSystem;
import battle.system.MovementSystem;
import battle.system.PathMonsterSystem;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.swing.JFrame;

public class BattleVisualization extends JFrame {

    static int width = 7;
    static int height = 5;
    static int tileWidth = 77;
    static int tileHeight = 77;
    static int paddingX = 20;
    static int paddingY = 20;
    static int screenWidth = tileWidth * width;
    static int screenHeight = tileHeight * height;
    static int centerX = paddingX + screenWidth / 2;
    static int centerY = paddingY + screenHeight / 2;
    static int scale = 2;
    BufferedImage B;
    Graphics G;
    int size;
    EntityManager entityManager;
    AttackSystem attackSystem;
    MovementSystem movementSystem;
    PathMonsterSystem pathMonsterSystem;
    CollisionSystem collisionSystem;
    public static void main(String[] args) throws Exception {
        new BattleVisualization();

    }

    public BattleVisualization() throws Exception {
        this.setTitle("BattleVisualization");
        this.setSize((width * tileWidth + paddingX * 2) / scale, (height * tileHeight + paddingY * 2) / scale);
        this.setDefaultCloseOperation(3);
        B = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        G = B.getGraphics();
        this.setVisible(true);

        this.attackSystem = new AttackSystem();
        this.pathMonsterSystem = new PathMonsterSystem();
        this.movementSystem = new MovementSystem();
        this.collisionSystem = new CollisionSystem();
        this.entityManager = EntityManager.getInstance();
        this.initTower();
    }

    public void paint(Graphics G1) {

        //redraw the Graphic
        G.setColor(Color.LIGHT_GRAY);
        G.fillRect(0, 0, this.getWidth(), this.getHeight());
        G.setColor(Color.BLUE);
        G.drawRect(0, 0, this.getWidth(), this.getHeight());

        this.updateSystem();



        for (int i = 0; i <= height; i++)
            G.drawLine(paddingY / scale, (paddingY + i * tileHeight) / scale,
                    (paddingY + width * tileHeight) / scale, (paddingY + i * tileHeight) / scale);
        for (int i = 0; i <= width; i++)
            G.drawLine((paddingX + i * tileWidth) / scale, paddingX / scale,
                    (paddingX + i * tileWidth) / scale, (paddingX + height * tileWidth) / 2);

        List<EntityECS> monsterList = this.entityManager.getEntitiesHasComponents
                (Collections.singletonList(MonsterInfoComponent.typeID));

        for (EntityECS monster : monsterList) {
            PositionComponent positionComponent = (PositionComponent) monster.getComponent(PositionComponent.typeID);
            CollisionComponent collisionComponent = (CollisionComponent) monster.getComponent(CollisionComponent.typeID);
            G.setColor(Color.BLACK);
            Point p = this.getMonsterPos(positionComponent, collisionComponent);
            G.fillRect((int) p.x, (int) p.y, (int) collisionComponent.getWidth() / scale, (int) collisionComponent.getHeight() / scale);
        }

        List<EntityECS> towerList = this.entityManager.getEntitiesHasComponents(Collections.singletonList(TowerInfoComponent.typeID));
        for (EntityECS tower : towerList) {
            PositionComponent positionComponent = (PositionComponent) tower.getComponent(PositionComponent.typeID);
            G.setColor(Color.RED);
            Point p = this.getTowerPos(positionComponent);
            G.fillRect((int) p.x, (int) p.y, tileWidth / scale, tileHeight / scale);
        }

        List<EntityECS> bulletList = this.entityManager.getEntitiesHasComponents(Collections.singletonList(BulletInfoComponent.typeID));
        //System.out.println(bulletList.size());
        for (EntityECS bullet : bulletList) {
            PositionComponent positionComponent = (PositionComponent) bullet.getComponent(PositionComponent.typeID);
            CollisionComponent collisionComponent = (CollisionComponent) bullet.getComponent(CollisionComponent.typeID);
            G.setColor(Color.GREEN);
            Point p = this.getMonsterPos(positionComponent, collisionComponent);
            G.fillRect((int) p.x, (int) p.y, (int) collisionComponent.getWidth() / scale, (int) collisionComponent.getHeight() / scale);
        }
        G1.drawImage(B, 0, 0, this.getWidth(), this.getHeight(), null);
        this.repaint();
    }

    public void updateSystem() {
        attackSystem.run();
        pathMonsterSystem.run();
        movementSystem.run();
        collisionSystem.run();
    }

    public Point getTowerPos(PositionComponent pos) {
        return new Point((centerX + pos.getX() - tileWidth / 2 + 2) / scale, (centerY - pos.getY() - tileHeight / 2 + 2) / scale);
    }

    public Point getMonsterPos(PositionComponent pos, CollisionComponent col) {
        return new Point((centerX + pos.getX() - col.getWidth() / 2) / scale, (centerY - pos.getY() - col.getHeight() / 2) / scale);
    }

    public void initTower() throws Exception {
        EntityFactory.getInstance().createCannonOwlTower(new Point(3, 3), EntityMode.PLAYER);
        EntityFactory.getInstance().createSwordManMonster(new Point(0, 4), EntityMode.PLAYER);
    }
}
