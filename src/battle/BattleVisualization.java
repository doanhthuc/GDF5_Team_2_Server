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
import battle.map.BattleMap;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import javax.swing.*;

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
    static int scale = 1;
    private Battle battle;
    BufferedImage B;
    Graphics G;
    JComboBox entityChoosen;
    EntityMode entityMode;
    String userName, opponentUserName;
    int userHP, opponentHP, userEnergy, opponentEnergy;
//    public static void main(String[] args) throws Exception {
//        new BattleVisualization(1);
//    }

    Map<Integer, Color> colorMap = new HashMap<>();

    public BattleVisualization(Battle battle, EntityMode entityMode) throws Exception {
        this.battle = battle;
        this.entityMode = entityMode;
        this.setTitle("BattleVisualization");
        this.setSize((width * tileWidth + paddingX * 15) * scale, (height * tileHeight + paddingY * 5) * scale);
        this.setDefaultCloseOperation(3);
        this.addMouseListener(this);


        this.initColor();
        B = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        G = B.getGraphics();
        this.setVisible(true);
        String s1[] = {"OWL", "FROG", "WIZARD", "BEAR", "BUNNY", "GOAT", "SNAKE", "FIRE", "FROZEN", "TRAP"};
        entityChoosen = new JComboBox(s1);
        JFrame jFrame = new JFrame();
        jFrame.add(entityChoosen);
        entityChoosen.setBounds(100, 100, 20, 20);

        jFrame.setSize(100, 100);
        jFrame.setVisible(true);
        this.initTower();
    }

    public void paint(Graphics G1) {
        //redraw the Graphic
        if (this.entityMode == EntityMode.PLAYER) {
            this.userName = this.battle.player1.getUserName();
            this.userHP = this.battle.getPlayer1HP();
            this.userEnergy = this.battle.getPlayer1energy();
            this.opponentUserName = this.battle.player2.getUserName();
            this.opponentHP = this.battle.getPlayer2HP();
            this.opponentEnergy = this.battle.getPlayer2energy();
        }
        if (this.entityMode == EntityMode.OPPONENT) {
            this.userName = this.battle.player2.getUserName();
            this.userHP = this.battle.getPlayer2HP();
            this.userEnergy = this.battle.getPlayer2energy();
            this.opponentUserName = this.battle.player1.getUserName();
            this.opponentHP = this.battle.getPlayer1HP();
            this.opponentEnergy = this.battle.getPlayer1energy();
        }
        G.setColor(Color.LIGHT_GRAY);
        G.fillRect(0, 0, this.getWidth(), this.getHeight());
        G.setColor(Color.BLUE);
        G.drawRect(0, 0, this.getWidth(), this.getHeight());

        G.setFont(new Font("Arial Black", Font.BOLD, 20));
        G.drawString("User =" + userName, 600, 300);
        G.drawString("UserHP =" + userHP, 600, 350);
        G.drawString("UserEnergy =" + userEnergy, 600, 400);

        G.drawString("Opponent =" + opponentUserName, 600, 100);
        G.drawString("OpponentHP =" + opponentHP, 600, 150);
        G.drawString("OpponentEnergy =" + opponentEnergy, 600, 200);

        for (int i = 0; i <= height; i++)
            G.drawLine(paddingY * scale, (paddingY + i * tileHeight) * scale,
                    (paddingY + width * tileHeight) * scale, (paddingY + i * tileHeight) * scale);
        for (int i = 0; i <= width; i++)
            G.drawLine((paddingX + i * tileWidth) * scale, paddingX * scale,
                    (paddingX + i * tileWidth) * scale, (paddingX + height * tileWidth) * scale);

        List<EntityECS> monsterList = this.battle.getEntityManager().getEntitiesHasComponents
                (Arrays.asList(MonsterInfoComponent.typeID, PositionComponent.typeID));

        for (int i = 0; i < BattleMap.mapW; i++)
            for (int j = 0; j < BattleMap.mapH; j++) {
                BattleMap battleMap;
                if (this.entityMode == EntityMode.OPPONENT)
                    battleMap = this.battle.player2BattleMap;
                else battleMap = this.battle.player1BattleMap;
                if (!FindPathUtils.findPathAble(battleMap.map[i][j])) {
                    Point tilePos = Utils.tile2Pixel(i, j, this.entityMode);
                    G.setColor(Color.BLUE);
                    Point p = this.getTowerPos(new PositionComponent(tilePos.getX(), tilePos.getY()));
                    G.fillRect((int) p.x, (int) p.y, tileWidth * scale, tileHeight * scale);
                }
            }
//        if (monsterList.size() > 0) {
//            PathComponent path = null;
//            for (EntityECS monster : monsterList) {
//                if (monster.getMode() == this.entityMode) {
//                    path = (PathComponent) monster.getComponent(PathComponent.typeID);
//                    break;
//                    }
//                }
//            List<Point> monsterPath = path.getPath();
//            for (Point i : monsterPath) {
//                G.setColor(Color.BLUE);
//                Point p = getMonsterPos(new PositionComponent(i.x, i.y), new CollisionComponent(5, 5));
//                G.fillRect((int) p.x, (int) p.y, 5, 5);
//                break;
//            }
//
//        }
        //System.out.println(" monsterSize" + monsterList.size());
        for (EntityECS monster : monsterList) {
            if (monster.getMode() != this.entityMode) continue;
            if (!monster._hasComponent(PositionComponent.typeID)) continue;
            if (!monster._hasComponent(CollisionComponent.typeID)) continue;
            if (!monster._hasComponent(LifeComponent.typeID)) continue;
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

        List<EntityECS> towerList = this.battle.getEntityManager().getEntitiesHasComponents(Arrays.asList(TowerInfoComponent.typeID, PositionComponent.typeID));
        for (EntityECS tower : towerList) {
            if (tower.getMode() != this.entityMode) continue;
            PositionComponent positionComponent = (PositionComponent) tower.getComponent(PositionComponent.typeID);
            G.setColor(colorMap.get(tower.getTypeID()));
            Point p = this.getTowerPos(positionComponent);
            G.fillRect((int) p.x, (int) p.y, tileWidth * scale, tileHeight * scale);
        }


        List<EntityECS> bulletList = this.battle.getEntityManager().getEntitiesHasComponents(Collections.singletonList(BulletInfoComponent.typeID));
        //System.out.println(bulletList.size());
        for (EntityECS bullet : bulletList) {
            if (bullet.getMode() != this.entityMode) continue;
            if (!bullet._hasComponent(PositionComponent.typeID)) continue;
            if (!bullet._hasComponent(CollisionComponent.typeID)) continue;
            PositionComponent positionComponent = (PositionComponent) bullet.getComponent(PositionComponent.typeID);
            CollisionComponent collisionComponent = (CollisionComponent) bullet.getComponent(CollisionComponent.typeID);
            PathComponent pathComponent = (PathComponent) bullet.getComponent(PathComponent.typeID);
            G.setColor(Color.GREEN);
            Point p = this.getMonsterPos(positionComponent, collisionComponent);
            G.fillRect((int) p.x, (int) p.y, (int) 5 * scale, (int) 5 * scale);
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
        // battle.getEntityFactory().createGoatAttackDamageTower(new Point(3,3), EntityMode.OPPONENT);
//        battle.getEntityFactory().createSnakeAttackSpeedTower(new Point(3, 3), EntityMode.OPPONENT);
//        battle.getEntityFactory().createWizardTower(new Point(3, 3), EntityMode.OPPONENT);
//         battle.getEntityFactory().createBunnyOilGunTower(new Point(3, 3), EntityMode.OPPONENT);
//        battle.getEntityFactory().createIceGunPolarBearTower(new Point(3, 3), EntityMode.OPPONENT);
//        battle.getEntityFactory().createFrogTower(new Point(1, 3), EntityMode.OPPONENT);

    }

    public void initMonster(int x) throws Exception {
//        if (x == 1) {
//            battle.getEntityFactory().createDemonTreeBoss(Utils.tile2Pixel(0, 4, EntityMode.OPPONENT), EntityMode.OPPONENT);
//        }else {
//            System.out.println("2");
//            battle.getEntityFactory().createSatyrBoss(Utils.tile2Pixel(0, 4, EntityMode.OPPONENT), EntityMode.OPPONENT);
//            battle.getEntityFactory().createSatyrBoss(Utils.tile2Pixel(0, 4, EntityMode.OPPONENT), EntityMode.OPPONENT);
//        }

//        battle.getEntityFactory().createSwordManMonster(Utils.tile2Pixel(0, 4, EntityMode.OPPONENT), EntityMode.OPPONENT);
//        battle.getEntityFactory().createAssassinMonster(Utils.tile2Pixel(0, 4, EntityMode.OPPONENT), EntityMode.OPPONENT);
//        battle.getEntityFactory().createBatMonster(Utils.tile2Pixel(0, 4, EntityMode.OPPONENT), EntityMode.OPPONENT);
//        battle.getEntityFactory().createGiantMonster(Utils.tile2Pixel(0, 4, EntityMode.OPPONENT), EntityMode.OPPONENT);
//        battle.getEntityFactory().createNinjaMonster(Utils.tile2Pixel(0, 4, EntityMode.OPPONENT), EntityMode.OPPONENT);
//        battle.getEntityFactory().createDarkGiantBoss(Utils.tile2Pixel(0, 4, EntityMode.OPPONENT), EntityMode.OPPONENT);
//        battle.getEntityFactory().createSatyrBoss(Utils.tile2Pixel(0, 4, EntityMode.OPPONENT), EntityMode.OPPONENT);
//        battle.getEntityFactory().createDemonTreeBoss(Utils.tile2Pixel(0,4,EntityMode.OPPONENT),EntityMode.OPPONENT);
//    }
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
//        int tilePosX = (e.getX() - paddingX * scale) / (tileWidth * scale);
//        int tilePosY = (e.getY() - paddingY * scale) / (tileHeight * scale);
//        tilePosY = height - 1 - tilePosY;
//        Point pixelPos;
//        try {
//            switch ((String) Objects.requireNonNull(this.entityChoosen.getSelectedItem())) {
//                case "OWL":
//                    this.battle.buildTowerByTowerID(GameConfig.ENTITY_ID.CANNON_TOWER, tilePosX, tilePosY, this.entityMode);
//                    break;
//                case "FROG":
//                    this.battle.buildTowerByTowerID(GameConfig.ENTITY_ID.FROG_TOWER, tilePosX, tilePosY, this.entityMode);
//                    break;
//                case "WIZARD":
//                    this.battle.buildTowerByTowerID(GameConfig.ENTITY_ID.WIZARD_TOWER, tilePosX, tilePosY, this.entityMode);
//                    break;
//                case "BEAR":
//                    this.battle.buildTowerByTowerID(GameConfig.ENTITY_ID.BEAR_TOWER, tilePosX, tilePosY, this.entityMode);
//                    break;
//                case "BUNNY":
//                    battle.getEntityFactory().createBunnyOilGunTower(new Point(tilePosX, tilePosY), this.entityMode);
//                    battle.player2BattleMap.map[tilePosX][tilePosY] = GameConfig.MAP.TOWER;
//                    this.battle.handlerPutTower(this.entityMode);
//                    break;
//                case "SNAKE":
//                    battle.getEntityFactory().createSnakeAttackSpeedTower(new Point(tilePosX, tilePosY), this.entityMode);
//                    battle.player2BattleMap.map[tilePosX][tilePosY] = GameConfig.MAP.TOWER;
//                    this.battle.handlerPutTower(this.entityMode);
//                    break;
//                case "GOAT":
//                    battle.getEntityFactory().createGoatAttackDamageTower(new Point(tilePosX, tilePosY), this.entityMode);
//                    battle.player2BattleMap.map[tilePosX][tilePosY] = GameConfig.MAP.TOWER;
//                    this.battle.handlerPutTower(this.entityMode);
//                    break;
//                case "FIRE":
//                    pixelPos = Utils.tile2Pixel(tilePosX, tilePosY, this.entityMode);
//                    //System.out.println(pixelPos.getX()+ " "+pixelPos.getY());
//                    battle.getEntityFactory().createFireSpell(pixelPos, this.entityMode);
//                    break;
//                case "FROZEN":
//                    pixelPos = Utils.tile2Pixel(tilePosX, tilePosY, this.entityMode);
//                    //System.out.println(pixelPos.getX()+ " "+pixelPos.getY());
//                    battle.getEntityFactory().createFrozenSpell(pixelPos, this.entityMode);
//                    break;
//                case "TRAP":
//                    battle.getEntityFactory().createTrapSpell(new Point(tilePosX, tilePosY), this.entityMode);
//                    break;
//
//            }
//        } catch (Exception exception) {
//            exception.printStackTrace();
//        }


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
