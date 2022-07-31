package model.battle;

import battle.Battle;
import battle.BattleMap;
import battle.common.EntityMode;
import battle.common.FindPathUtils;
import battle.common.Point;
import battle.common.Utils;
import battle.component.common.PathComponent;
import battle.component.common.PositionComponent;
import battle.component.info.MonsterInfoComponent;
import battle.entity.EntityECS;
import bitzero.server.BitZeroServer;
import model.PlayerInfo;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Room implements Runnable {
    private final int roomId;
    private PlayerInBattle player1;
    private PlayerInBattle player2;
    //Create MonsterWave
    private Battle battle;

    public Room(PlayerInfo player1, PlayerInfo player2) throws Exception {
        this.roomId = RoomManager.getInstance().getRoomCount();
        this.player1 = new PlayerInBattle(player1);
        this.player2 = new PlayerInBattle(player2);
        this.battle = new Battle(player1.getId(),player2.getId());

    }

//    public Room(PlayerInfo player1, PlayerInfo player2, BattleMap battleMap1, BattleMap battleMap2) throws Exception {
//        this.roomId = RoomManager.getInstance().getRoomCount();
//        this.player1 = new PlayerInBattle(player1);
//        this.player2 = new PlayerInBattle(player2);
//        this.battle = new Battle(player1.getId(), player2.getId(), battleMap1, battleMap2);
//    }

    @Override
    public void run() {
         BitZeroServer.getInstance().getTaskScheduler().scheduleAtFixedRate(() -> {
             try {
                 this.battle.updateSystem();
                 //Check heets thoif gian -> tao wave quai
                 
             } catch (Exception e) {
                 e.printStackTrace();
             }

             //Check HP
         },0,100, TimeUnit.MILLISECONDS);

    }

    public void handlerPutTower(EntityMode mode) {
        if (mode == EntityMode.PLAYER)
            this.battle.player1ShortestPath = FindPathUtils.findShortestPathForEachTile(battle.player1BattleMap.map);
        List<EntityECS> monsterList = battle.getEntityManager().getEntitiesHasComponents(Arrays.asList(MonsterInfoComponent.typeID, PathComponent.typeID));
        for (EntityECS monster : monsterList) {
            if (monster.getMode() == mode) {
                PathComponent pathComponent = (PathComponent) monster.getComponent(PathComponent.typeID);
                PositionComponent positionComponent = (PositionComponent) monster.getComponent(PositionComponent.typeID);
                if (positionComponent != null) {
                    Point tilePos = Utils.pixel2Tile(positionComponent.getX(), positionComponent.getY(), mode);
                    List<Point> path = this.battle.player1ShortestPath[(int) tilePos.getX()][(int) tilePos.getY()];
                    if (path != null) {
                        List<Point> newPath = Utils.tileArray2PixelCellArray(path, mode);
                        pathComponent.setPath(newPath);
                        pathComponent.setCurrentPathIDx(0);
                    }
                }
            }
        }
    }

    //Function Handler Dat tru, tha spell

    public PlayerInfo getOpponentPlayer(int playerId) {
        if (playerId == player1.getId()) {
            return player1;
        } else {
            return player2;
        }
    }

    public PlayerInfo getOpponentPlayerByMyPlayerId(int playerId) {
        if (playerId == player1.getId()) {
            return player2;
        } else {
            return player1;
        }
    }

    public PlayerInBattle getMyPlayerInBattle(int opponentId) {
        if (opponentId == player1.getId()) {
            return player2;
        } else {
            return player1;
        }
    }

    public int getRoomId() {
        return roomId;
    }

    public PlayerInBattle getPlayer1() {
        return player1;
    }

    public void setPlayer1(PlayerInBattle player1) {
        this.player1 = player1;
    }

    public PlayerInBattle getPlayer2() {
        return player2;
    }

    public void setPlayer2(PlayerInBattle player2) {
        this.player2 = player2;
    }

    public Battle getBattle() {
        return battle;
    }

    public void setBattle(Battle battle) {
        this.battle = battle;
    }

    public BattleMap getPlayerBattleMap(int id){
        return this.battle.getBattleMapByPlayerId(id);
    }
}
