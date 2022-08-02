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
import battle.config.GameConfig;
import battle.entity.EntityECS;
import bitzero.server.BitZeroServer;
import bitzero.server.entities.User;
import bitzero.util.ExtensionUtility;
import cmd.send.battle.ResponseEndBattle;
import cmd.send.battle.player.ResponseRequestBattleMapObject;
import jdk.internal.org.objectweb.asm.tree.LocalVariableAnnotationNode;
import model.Inventory.Inventory;
import model.Lobby.LobbyChestContainer;
import model.Lobby.LobbyChestDefine;
import model.PlayerInfo;
import service.MatchingHandler;
import service.RoomHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Room implements Runnable {
    private final int roomId;
    private PlayerInBattle player1;
    private PlayerInBattle player2;
    private Battle battle;
    private long startTime;
    private boolean endBattle;

    public Room(PlayerInfo player1, PlayerInfo player2) throws Exception {
        this.roomId = RoomManager.getInstance().getRoomCount();
        this.player1 = new PlayerInBattle(player1);
        this.player2 = new PlayerInBattle(player2);
        this.battle = new Battle(player1.getId(), player2.getId());
        this.endBattle = false;
        this.startTime = System.currentTimeMillis() + 15000;
        this.battle.setNextWaveTime(this.startTime);
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
                if (this.endBattle == false) {
                    this.battle.updateMonsterWave();
                    this.battle.updateSystem();
                    if (this.player2.getBotType() != 0) this.handleBotAction();
                    this.checkEndBattle();
                    if (this.endBattle) RoomManager.getInstance().removeRoom(this.roomId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 100, TimeUnit.MILLISECONDS);

    }

    public void checkEndBattle() throws Exception {
        int player1HP = this.battle.getPlayer1HP();
        int player2HP = this.battle.getPlayer2HP();
        int winUserID = -1;
        int loseUserID = -1;
        if (player1HP == 0 && player2HP == 0) {
            this.endBattle = true;
        } else if (player2HP == 0) {
            winUserID = this.player1.getId();
            loseUserID = this.player2.getId();
            this.endBattle = true;
        } else if (player1HP == 0) {
            winUserID = this.player2.getId();
            loseUserID = this.player1.getId();
            this.endBattle = true;
        } else if (this.battle.getCurrentWave() > this.battle.getWaveAmount()) {
            if (player1HP > player2HP) {
                winUserID = this.player1.getId();
                loseUserID = this.player2.getId();
            } else {
                winUserID = this.player2.getId();
                loseUserID = this.player1.getId();
            }
            this.endBattle = true;
        }

        if (this.endBattle == true) {
            if (winUserID != -1)
                this.sendWinUser(winUserID, loseUserID, player1HP > player2HP ? player1HP : player2HP, player1HP > player2HP ? player2HP : player1HP);
            else this.sendDraw();
        }

    }

    // sendBattleResult
    public void handleBotAction() {
       List<Point> monsterPath = this.battle.player2ShortestPath[0][4];

    }

    public void sendDraw() throws Exception {
        User user1 = BitZeroServer.getInstance().getUserManager().getUserById(player1.getId());
        User user2 = BitZeroServer.getInstance().getUserManager().getUserById(player2.getId());
        PlayerInfo userInfo1 = (PlayerInfo) PlayerInfo.getModel(player1.getId(), PlayerInfo.class);
        PlayerInfo userInfo2 = (PlayerInfo) PlayerInfo.getModel(player2.getId(), PlayerInfo.class);
        ExtensionUtility.getExtension().send(new ResponseEndBattle(RoomHandler.RoomError.END_BATTLE.getValue(), GameConfig.BATTLE_RESULT.DRAW, battle.getPlayer1HP(), battle.getPlayer2HP(), userInfo1.getTrophy(), 0, 0), user1);
        ExtensionUtility.getExtension().send(new ResponseEndBattle(RoomHandler.RoomError.END_BATTLE.getValue(), GameConfig.BATTLE_RESULT.DRAW, battle.getPlayer2HP(), battle.getPlayer1HP(), userInfo2.getTrophy(), 0, 0), user2);
    }

    public void sendWinUser(int winUserID, int loseUserID, int winnerHP, int loserHP) throws Exception {
        User user1 = BitZeroServer.getInstance().getUserManager().getUserById(winUserID);
        User user2 = BitZeroServer.getInstance().getUserManager().getUserById(loseUserID);

        PlayerInfo winUser = (PlayerInfo) PlayerInfo.getModel(winUserID, PlayerInfo.class);
        PlayerInfo loseUser = (PlayerInfo) PlayerInfo.getModel(loseUserID, PlayerInfo.class);
        LobbyChestContainer winUserLobbyChest = (LobbyChestContainer) LobbyChestContainer.getModel(winUser.getId(), LobbyChestContainer.class);
        if (winUserLobbyChest.lobbyChestContainer.size() < LobbyChestDefine.LOBBY_CHEST_AMOUNT) {
            winUserLobbyChest.addLobbyChest();
            winUserLobbyChest.saveModel(winUser.getId());

            ExtensionUtility.getExtension().send(new ResponseEndBattle(RoomHandler.RoomError.END_BATTLE.getValue(), GameConfig.BATTLE_RESULT.WIN, winnerHP, loserHP, winUser.getTrophy(), 10, 1), user1);
        } else
            ExtensionUtility.getExtension().send(new ResponseEndBattle(RoomHandler.RoomError.END_BATTLE.getValue(), GameConfig.BATTLE_RESULT.WIN, winnerHP, loserHP, winUser.getTrophy(), 10, 0), user1);
        winUser.setTrophy(winUser.getTrophy() + 10);
        winUser.saveModel(winUser.getId());
        loseUser.setTrophy(loseUser.getTrophy() - 10);
        loseUser.saveModel(loseUser.getId());
        ExtensionUtility.getExtension().send(new ResponseEndBattle(RoomHandler.RoomError.END_BATTLE.getValue(), GameConfig.BATTLE_RESULT.LOSE, loserHP, winnerHP, loseUser.getTrophy(), -10, 0), user2);

    }

    public void handlerPutTower(EntityMode mode) {
        if (mode == EntityMode.PLAYER)
            this.battle.player1ShortestPath = FindPathUtils.findShortestPathForEachTile(battle.player1BattleMap.map);
        else
            this.battle.player2ShortestPath = FindPathUtils.findShortestPathForEachTile(battle.player2BattleMap.map);

        List<EntityECS> monsterList = this.battle.getEntityManager().getEntitiesHasComponents(Arrays.asList(MonsterInfoComponent.typeID, PathComponent.typeID));
        for (EntityECS monster : monsterList) {
            if (monster.getMode() == mode) {
                PathComponent pathComponent = (PathComponent) monster.getComponent(PathComponent.typeID);
                PositionComponent positionComponent = (PositionComponent) monster.getComponent(PositionComponent.typeID);
                if (positionComponent != null) {
                    List<Point> path;
                    Point tilePos = Utils.pixel2Tile(positionComponent.getX(), positionComponent.getY(), mode);
                    if (monster.getMode() == EntityMode.PLAYER) {
                        path = this.battle.player1ShortestPath[(int) tilePos.getX()][(int) tilePos.getY()];
                    } else {
                        path = this.battle.player2ShortestPath[(int) tilePos.getX()][(int) tilePos.getY()];
                    }
                    if (path != null) {
                        List<Point> newPath = Utils.tileArray2PixelCellArray(path, mode);
                        pathComponent.setPath(newPath);
                        pathComponent.setCurrentPathIDx(0);
                    }
                }
            }
        }
    }


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

    public BattleMap getPlayerBattleMap(int id) {
        return this.battle.getBattleMapByPlayerId(id);
    }

    public List<List<Integer>> getMonsterWave() {
        return this.battle.getMonsterWave();
    }

    public int getWaveAmount() {
        return this.battle.getWaveAmount();
    }

    public long getStartTime() {
        return startTime;
    }
}
