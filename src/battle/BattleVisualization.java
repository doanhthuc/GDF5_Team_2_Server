package battle;

import battle.common.EntityMode;
import battle.common.FindPathUtils;
import battle.common.Point;
import battle.common.Utils;
import battle.component.common.CollisionComponent;
import battle.component.common.PathComponent;
import battle.component.common.PositionComponent;
import battle.component.info.BulletInfoComponent;
import battle.component.info.LifeComponent;
import battle.component.info.MonsterInfoComponent;
import battle.component.info.TowerInfoComponent;
import battle.config.GameConfig;
import battle.entity.EntityECS;
import battle.factory.EntityFactory;
import battle.manager.EntityManager;
import battle.system.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import javax.swing.JFrame;

public class BattleVisualization extends JFrame implements MouseListener {

    static int width = 7;
    static int height = 5;
    static int tileWidth = 77;
    static int tileHeight = 77;
    static int paddingX = 30;
    static int paddingY = 30;
    static int screenWidth = tileWidth * width;
    static int screenHeight = tileHeight * height;
    static int centerX = paddingX + screenWidth / 2;
    static int centerY = paddingY + screenHeight / 2;
    static int scale = 2;
    Battle battle = new Battle();
    BufferedImage B;
    Graphics G;

    //    EntityManager entityManager;
//    AttackSystem attackSystem;
//    MovementSystem movementSystem;
//    PathMonsterSystem pathMonsterSystem;
//    CollisionSystem collisionSystem;
//    EffectSystem effectSystem;
//    LifeSystem lifeSystem;
//    AbilitySystem abilitySystem;
//    BulletSystem bulletSystem;
//    ResetSystem resetSystem;
//    List<Point>[][] playerShortestPath;
    public static void main(String[] args) throws Exception {
        new BattleVisualization();
    }

    Map<Integer, Color> colorMap = new HashMap<>();

    public BattleVisualization() throws Exception {
        this.setTitle("BattleVisualization");
        this.setSize((width * tileWidth + paddingX * 5) * scale, (height * tileHeight + paddingY * 5) * scale);
        this.setDefaultCloseOperation(3);
        this.addMouseListener(this);
        this.initColor();
        B = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        G = B.getGraphics();
        this.setVisible(true);

        this.initTower();


    }

    public void paint(Graphics G1) {
        //redraw the Graphic
        G.setColor(Color.LIGHT_GRAY);
        G.fillRect(0, 0, this.getWidth(), this.getHeight());
        G.setColor(Color.BLUE);
        G.drawRect(0, 0, this.getWidth(), this.getHeight());
        this.battle.updateSystem();


        for (int i = 0; i <= height; i++)
            G.drawLine(paddingY * scale, (paddingY + i * tileHeight) * scale,
                    (paddingY + width * tileHeight) * scale, (paddingY + i * tileHeight) * scale);
        for (int i = 0; i <= width; i++)
            G.drawLine((paddingX + i * tileWidth) * scale, paddingX * scale,
                    (paddingX + i * tileWidth) * scale, (paddingX + height * tileWidth) * scale);

        List<EntityECS> monsterList = this.battle.entityManager.getEntitiesHasComponents
                (Collections.singletonList(MonsterInfoComponent.typeID));
        if (monsterList.size() > 0) {
            PathComponent path = (PathComponent) monsterList.get(0).getComponent(PathComponent.typeID);
            List<Point> monsterPath = path.getPath();
            for (Point i : monsterPath) {
                G.setColor(Color.BLUE);
                Point p = getMonsterPos(new PositionComponent(i.x, i.y), new CollisionComponent(5, 5));
                G.fillRect((int) p.x, (int) p.y, 5, 5);
            }
        }

        for (EntityECS monster : monsterList) {
            PositionComponent positionComponent = (PositionComponent) monster.getComponent(PositionComponent.typeID);
            CollisionComponent collisionComponent = (CollisionComponent) monster.getComponent(CollisionComponent.typeID);
            LifeComponent lifeComponent = (LifeComponent) monster.getComponent(LifeComponent.typeID);
            G.setColor(colorMap.get(monster.getTypeID()));
            Point p = this.getMonsterPos(positionComponent, collisionComponent);
            G.fillRect((int) p.x, (int) p.y, (int) collisionComponent.getWidth() * scale, (int) collisionComponent.getHeight() * scale);
            //G.fillRect((int) p.x, (int) p.y, (int) 5 * scale, (int)5 * scale);
            G.setFont(new Font("Arial Black", Font.BOLD, 30));
            G.drawString(Double.toString(lifeComponent.getHp()), (int) p.x, (int) p.y);

        }

        List<EntityECS> towerList = this.battle.entityManager.getEntitiesHasComponents(Collections.singletonList(TowerInfoComponent.typeID));
        for (EntityECS tower : towerList) {
            PositionComponent positionComponent = (PositionComponent) tower.getComponent(PositionComponent.typeID);
            G.setColor(colorMap.get(tower.getTypeID()));
            Point p = this.getTowerPos(positionComponent);
            G.fillRect((int) p.x, (int) p.y, tileWidth * scale, tileHeight * scale);
        }

        for (int i = 0; i < BattleMap.mapW; i++)
            for (int j = 0; j < BattleMap.mapH; j++) {
                if (!FindPathUtils.findPathAble(this.battle.playerBattleMap.map[i][j])) {
                    Point tilePos= Utils.tile2Pixel(i,j,EntityMode.PLAYER);
                    G.setColor(Color.BLUE);
                    Point p = this.getTowerPos(new PositionComponent(tilePos.getX(),tilePos.getY()));
                    G.fillRect((int) p.x, (int) p.y, tileWidth * scale, tileHeight * scale);
                }
            }


        List<EntityECS> bulletList = this.battle.entityManager.getEntitiesHasComponents(Collections.singletonList(BulletInfoComponent.typeID));
        //System.out.println(bulletList.size());
        for (EntityECS bullet : bulletList) {
            PositionComponent positionComponent = (PositionComponent) bullet.getComponent(PositionComponent.typeID);
            CollisionComponent collisionComponent = (CollisionComponent) bullet.getComponent(CollisionComponent.typeID);
            PathComponent pathComponent = (PathComponent) bullet.getComponent(PathComponent.typeID);
            G.setColor(Color.GREEN);
            Point p = this.getMonsterPos(positionComponent, collisionComponent);
            G.fillRect((int) p.x, (int) p.y, (int) collisionComponent.getWidth() * scale, (int) collisionComponent.getHeight() * scale);
        }
        G1.drawImage(B, 0, 0, this.getWidth(), this.getHeight(), null);
        this.repaint();
    }

    public Point getTowerPos(PositionComponent pos) {
        return new Point((centerX + pos.getX() - tileWidth / 2) * scale, (centerY - pos.getY() - tileHeight / 2) * scale);
    }

    public Point getMonsterPos(PositionComponent pos, CollisionComponent col) {
        return new Point((centerX + pos.getX() - col.getWidth() / 2) * scale, (centerY - pos.getY() - col.getHeight() / 2) * scale);
    }

    public void initTower() throws Exception {
        // EntityFactory.getInstance().createGoatAttackDamageTower(new Point(3,3), EntityMode.PLAYER);
//        EntityFactory.getInstance().createSnakeAttackSpeedTower(new Point(3, 3), EntityMode.PLAYER);
//        EntityFactory.getInstance().createWizardTower(new Point(3, 3), EntityMode.PLAYER);
//         EntityFactory.getInstance().createBunnyOilGunTower(new Point(3, 3), EntityMode.PLAYER);
//        EntityFactory.getInstance().createIceGunPolarBearTower(new Point(3, 3), EntityMode.PLAYER);
//        EntityFactory.getInstance().createFrogTower(new Point(1, 3), EntityMode.PLAYER);
//        EntityFactory.getInstance().createSwordManMonster(Utils.tile2Pixel(0, 4, EntityMode.PLAYER), EntityMode.PLAYER);
//        EntityFactory.getInstance().createAssassinMonster(Utils.tile2Pixel(0, 4, EntityMode.PLAYER), EntityMode.PLAYER);
//        EntityFactory.getInstance().createBatMonster(Utils.tile2Pixel(0, 4, EntityMode.PLAYER), EntityMode.PLAYER);
//        EntityFactory.getInstance().createGiantMonster(Utils.tile2Pixel(0, 4, EntityMode.PLAYER), EntityMode.PLAYER);
//        EntityFactory.getInstance().createNinjaMonster(Utils.tile2Pixel(0, 4, EntityMode.PLAYER), EntityMode.PLAYER);
        EntityFactory.getInstance().createDarkGiantBoss(Utils.tile2Pixel(0, 4, EntityMode.PLAYER), EntityMode.PLAYER);
//        EntityFactory.getInstance().createSatyrBoss(Utils.tile2Pixel(0, 4, EntityMode.PLAYER), EntityMode.PLAYER);
//        EntityFactory.getInstance().createDemonTreeBoss(Utils.tile2Pixel(0,4,EntityMode.PLAYER),EntityMode.PLAYER);
    }


    public void handlerPutTower(EntityMode mode) {
        if (mode == EntityMode.PLAYER)
            this.battle.playerShortestPath = FindPathUtils.findShortestPathForEachTile(battle.playerBattleMap.map);
        List<EntityECS> monsterList = EntityManager.getInstance().getEntitiesHasComponents(Arrays.asList(MonsterInfoComponent.typeID, PathComponent.typeID));
        for (EntityECS monster : monsterList) {
            if (monster.getMode() == mode) {
                PathComponent pathComponent = (PathComponent) monster.getComponent(PathComponent.typeID);
                PositionComponent positionComponent = (PositionComponent) monster.getComponent(PositionComponent.typeID);
                if (positionComponent != null) {
                    Point tilePos = Utils.pixel2Tile(positionComponent.getX(), positionComponent.getY(), mode);
                    List<Point> path = this.battle.playerShortestPath[(int) tilePos.getX()][(int) tilePos.getY()];
                    if (path != null) {
                        List<Point> newPath = Utils.tileArray2PixelCellArray(path, mode);
                        pathComponent.setPath(newPath);
                        pathComponent.setCurrentPathIDx(0);
                    }
                }
            }
        }
    }

    public void initColor() {
        this.colorMap.put(GameConfig.ENTITY_ID.SWORD_MAN, Color.YELLOW);
        this.colorMap.put(GameConfig.ENTITY_ID.ASSASSIN, Color.RED);
        this.colorMap.put(GameConfig.ENTITY_ID.BAT, Color.BLACK);
        this.colorMap.put(GameConfig.ENTITY_ID.GIANT, Color.BLUE);
        this.colorMap.put(GameConfig.ENTITY_ID.NINJA, Color.CYAN);
        this.colorMap.put(GameConfig.ENTITY_ID.DARK_GIANT, Color.PINK);
        this.colorMap.put(GameConfig.ENTITY_ID.SATYR, Color.GREEN);
        this.colorMap.put(GameConfig.ENTITY_ID.DEMON_TREE, Color.ORANGE);


        this.colorMap.put(GameConfig.ENTITY_ID.CANNON_TOWER, Color.BLACK);
        this.colorMap.put(GameConfig.ENTITY_ID.BEAR_TOWER, Color.CYAN);
        this.colorMap.put(GameConfig.ENTITY_ID.FROG_TOWER, Color.RED);
        this.colorMap.put(GameConfig.ENTITY_ID.BUNNY_TOWER, Color.GREEN);
        this.colorMap.put(GameConfig.ENTITY_ID.WIZARD_TOWER, Color.PINK);
        this.colorMap.put(GameConfig.ENTITY_ID.SNAKE_TOWER, Color.ORANGE);
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        int tilePosX = (e.getX() - paddingX * scale) / (tileWidth * scale);
        int tilePosY = (e.getY() - paddingY * scale) / (tileHeight * scale);
        tilePosY = height - 1 - tilePosY;
        try {
            EntityFactory.getInstance().createCannonOwlTower(new Point(tilePosX, tilePosY), EntityMode.PLAYER);
            battle.playerBattleMap.map[tilePosX][tilePosY] = GameConfig.MAP.TOWER;
            this.handlerPutTower(EntityMode.PLAYER);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
