package model.battle;

import battle.*;
import battle.common.*;
import battle.config.GameConfig;
import battle.config.ReadConfigUtil;
import bitzero.server.BitZeroServer;
import bitzero.server.entities.User;
import bitzero.server.extensions.data.BaseCmd;
import bitzero.server.extensions.data.DataCmd;
import bitzero.util.ExtensionUtility;
import cmd.CmdDefine;
import cmd.receive.battle.tower.RequestPutTower;
import cmd.send.battle.ResponseEndBattle;

import cmd.send.battle.opponent.ResponseOppentPutTower;
import match.UserType;
import model.Lobby.LobbyChestContainer;
import model.Lobby.LobbyChestDefine;
import model.PlayerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.BattleHandler;
import service.RoomHandler;

import java.awt.*;
import java.awt.Point;
import java.util.*;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Room implements Runnable {
    private final int roomId;
    private PlayerInBattle player1;
    private PlayerInBattle player2;
    private Battle battle;
    private final long startTime;
    private boolean endBattle;
    private ScheduledFuture roomRun;
    private long botCommandTime = 0;
    private final TickManager tickManager;
    private final Queue<Pair<User, DataCmd>> waitingInputQueue = new LinkedList<>();

    public Room(PlayerInfo player1, PlayerInfo player2) throws Exception {
        this.roomId = RoomManager.getInstance().getRoomCount();
        this.player1 = new PlayerInBattle(player1);
        this.player2 = new PlayerInBattle(player2);
        this.battle = new Battle(player1, player2);
        this.endBattle = false;
        this.startTime = System.currentTimeMillis() + GameConfig.BATTLE.START_GAME_AFTER;
        this.botCommandTime = this.startTime + GameConfig.BATTLE.START_GAME_AFTER;
        this.battle.setNextWaveTime(this.startTime + GameConfig.BATTLE.WAVE_TIME);
        if (GameConfig.DEBUG) {
            new BattleVisualization(this.battle, this.battle.getEntityModeByPlayerID(this.player2.getId()));
            new BattleVisualization(this.battle, this.battle.getEntityModeByPlayerID(this.player1.getId()));
        }

        this.tickManager = new TickManager(this.startTime);
    }

    public void addInput(User user, DataCmd dataCmd) {
        this.waitingInputQueue.add(new Pair<>(user, dataCmd));
    }

    @Override
    public void run() {
        this.roomRun = BitZeroServer.getInstance().getTaskScheduler().scheduleAtFixedRate(() -> {
            try {
                if (!this.endBattle) {
                    int currentTick = this.tickManager.getCurrentTick();
//                    System.out.println(currentTick);
                    // enqueue the waiting inputs
                    while (!this.waitingInputQueue.isEmpty()) {
                        Pair<User, DataCmd> data = this.waitingInputQueue.poll();
                        this.tickManager.addInput(data);
                    }

                    // handle the inputs in the current tick
                    this.tickManager.handleInternalInputTick(currentTick);

                    this.battle.updateMonsterWave();
                    this.battle.updateSystem();
                    this.handleBotAction(this.tickManager.getCurrentTick());
                    //this.handlerClientCommand();
                    this.checkEndBattle();
                    this.checkAllUserDisconnect();

                    this.tickManager.increaseTick();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, GameConfig.BATTLE.TICK_RATE, TimeUnit.MILLISECONDS);
    }


    public void checkEndBattle() throws Exception {
        int player1HP = this.battle.getPlayer1HP();
        int player2HP = this.battle.getPlayer2HP();
        int winUserID = -1;
        int loseUserID = -1;
        if (player1HP <= 0 && player2HP <= 0) {
            this.endBattle = true;
        } else if (player2HP <= 0) {
            winUserID = this.player1.getId();
            loseUserID = this.player2.getId();
            this.endBattle = true;
        } else if (player1HP <= 0) {
            winUserID = this.player2.getId();
            loseUserID = this.player1.getId();
            this.endBattle = true;
        } else if (this.battle.getCurrentWave() >= this.battle.getWaveAmount()) {
            if (player1HP > player2HP) {
                winUserID = this.player1.getId();
                loseUserID = this.player2.getId();
            } else {
                winUserID = this.player2.getId();
                loseUserID = this.player1.getId();
            }
            this.endBattle = true;
        }

        if (this.endBattle) {
            if (winUserID != -1)
                SendResult.sendWinUser(winUserID, loseUserID, Math.max(player1HP, player2HP), Math.min(player1HP, player2HP));
            else SendResult.sendDrawBattle(player1.getId(), player2.getId(), this.battle.getPlayer1HP());
            RoomManager.getInstance().removeRoom(this.roomId);
            this.endRoom();
        }

    }

    public void checkAllUserDisconnect() {
        boolean user1Connection = BitZeroServer.getInstance().getUserManager().containsId(player1.getId());
        boolean user2Connection = BitZeroServer.getInstance().getUserManager().containsId(player2.getId());
        if (!user1Connection && !user2Connection) {
            this.endRoom();
            System.out.println("allUserDisconnect");
        }
    }

    public void endRoom() {
        this.roomRun.cancel(true);
    }

    public void handleBotAction(int tickNumber) throws Exception {

        long countDownBotCommandTime = 1500;

        if (this.player2.getUserType() == UserType.PLAYER) return;
        if (System.currentTimeMillis() > this.botCommandTime) {
            if (this.battle.getCurrentWave() == -1) return;
            BattleMap botBattleMap = this.battle.player2BattleMap;
            ArrayList<java.awt.Point> monsterPath = botBattleMap.getPath();
            for (int i = monsterPath.size() - 1; i >= 0; i--) {
                List<Integer> dX = Arrays.asList(1, 0, -1, 0, -1, 1, 1, -1);
                List<Integer> dY = Arrays.asList(0, 1, 0, -1, 1, -1, 1, -1);
                java.awt.Point currentPoint = monsterPath.get(i);
                List<Integer> towerListID = GameConfig.GROUP_ID.TOWER_ENTITY;
                int towerID = towerListID.get((int) (Math.random() * (towerListID.size() - 3)));
                int towerEnergy = ReadConfigUtil.towerInfo.get(towerID).getEnergy();

                for (int j = 0; j < dX.size(); j++) {
                    int tilePosX = currentPoint.x + dX.get(j);
                    int tilePosY = currentPoint.y + dY.get(j);
                    if (botBattleMap.isInBound(tilePosX, tilePosY))
                        if (botBattleMap.isMovableTile(botBattleMap.map[tilePosX][tilePosY])
                                && (!monsterPath.contains(new java.awt.Point(tilePosX, tilePosY)))
                                && this.battle.getPlayer2energy() >= towerEnergy) {
                            RequestPutTower botReq = new RequestPutTower(this.roomId, towerID, new Point(tilePosX, tilePosY));
                            this.botCommandTime = System.currentTimeMillis() + countDownBotCommandTime;
                            //Send To User
//                            this.tickManager.addInput();
                            User player = BitZeroServer.getInstance().getUserManager().getUserById(player1.getId());
                            ExtensionUtility.getExtension().send(new ResponseOppentPutTower(BattleHandler.BattleError.SUCCESS.getValue(), towerID, 1, new java.awt.Point(tilePosX, tilePosY), tickNumber + 20), player);
                            return;
                        }
                }
            }
        }
    }


    public PlayerInBattle getPlayerByID(int playerID) {
        if (player1.getId() == playerID)
            return player1;
        else
            return player2;
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
