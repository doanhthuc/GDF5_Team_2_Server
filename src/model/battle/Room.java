package model.battle;

import battle.*;
import battle.common.*;
import battle.config.GameConfig;
import battle.config.ReadTowerConfigUtil;
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

import java.util.*;
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
    private final Logger logger = LoggerFactory.getLogger("Room");
    private final PriorityQueue<ClientCommand> clientCommands = new PriorityQueue(new Comparator<ClientCommand>() {
        @Override
        public int compare(ClientCommand a, ClientCommand b) {
            return (int) (a.executeTime - b.executeTime);
        }
    });

    private final TickManager tickManager;
    private final Queue<Pair<User, DataCmd>> waitingInputQueue = new LinkedList<>();

    public Room(PlayerInfo player1, PlayerInfo player2) throws Exception {
        this.roomId = RoomManager.getInstance().getRoomCount();
        this.player1 = new PlayerInBattle(player1);
        this.player2 = new PlayerInBattle(player2);
        this.battle = new Battle(player1, player2);
        this.endBattle = false;
        this.startTime = System.currentTimeMillis() + GameConfig.BATTLE.START_GAME_AFTER;
        this.battle.setNextWaveTime(this.startTime + GameConfig.BATTLE.WAVE_TIME);
        if (GameConfig.DEBUG) {
            new BattleVisualization(this.battle, this.battle.getEntityModeByPlayerID(this.player2.getId()));
            new BattleVisualization(this.battle, this.battle.getEntityModeByPlayerID(this.player1.getId()));
        }

        this.tickManager= new TickManager(this.startTime);
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

                    // enqueue the waiting inputs
                    while (!this.waitingInputQueue.isEmpty()) {
                        Pair<User, DataCmd> data = this.waitingInputQueue.poll();
                        this.tickManager.addInput(data);
                    }

                    // handle the inputs in the current tick
                    this.tickManager.handleInternalInputTick(currentTick);

                    this.battle.updateMonsterWave();
                    this.battle.updateSystem();
                    if (this.player2.getUserType() != UserType.PLAYER) this.handleBotAction();
                    this.handlerClientCommand();
                    this.checkEndBattle();
                    this.checkAllUserDisconnect();

                    this.tickManager.increaseTick();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, GameConfig.BATTLE.TICK_RATE, TimeUnit.MILLISECONDS);
    }

    private void handlerClientCommand() throws Exception {
        if (this.clientCommands.size() == 0) return;
        ClientCommand cmd = this.clientCommands.peek();
        if (cmd.executeTime <= System.currentTimeMillis()) {
            switch (cmd.cmdID) {
                case CmdDefine.PUT_TOWER:
                    RequestPutTower req = (RequestPutTower) cmd.getBaseCmd();
                    this.battle.buildTowerByTowerID(req.getTowerId(), req.getTilePos().x, req.getTilePos().y, cmd.getMode());
                    break;
            }
            this.clientCommands.remove();
        }
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

        if (this.endBattle) {
            if (winUserID != -1)
                this.sendWinUser(winUserID, loseUserID, Math.max(player1HP, player2HP), Math.min(player1HP, player2HP));
            else this.sendDraw();
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

    // sendBattleResult
    public void handleBotAction() throws Exception {
        int currentTick = this.tickManager.getCurrentTick();
        if (this.battle.getCurrentWave() == -1) return;
        BattleMap botBattleMap = this.battle.player2BattleMap;
        ArrayList<java.awt.Point> monsterPath = botBattleMap.getPath();
        for (int i = monsterPath.size() - 1; i >= 0; i--) {
            List<Integer> dX = Arrays.asList(1, 0, -1, 0);
            List<Integer> dY = Arrays.asList(0, 1, 0, -1);
            java.awt.Point currentPoint = monsterPath.get(i);
            List<Integer> towerListID = GameConfig.GROUP_ID.TOWER_ENTITY;
            int towerID = towerListID.get((int) (Math.random() * (towerListID.size() - 3)));
            int towerEnergy = ReadTowerConfigUtil.towerInfo.get(towerID).getEnergy();

            for (int j = 0; j < dX.size(); j++) {
                int tilePosX = currentPoint.x + dX.get(j);
                int tilePosY = currentPoint.y + dY.get(j);
                if (botBattleMap.isInBound(tilePosX, tilePosY))
                    if (botBattleMap.isMovableTile(botBattleMap.map[tilePosX][tilePosY])
                            && (!monsterPath.contains(new java.awt.Point(tilePosX, tilePosY)))
                            && this.battle.getPlayer2energy() >= towerEnergy) {
                        this.battle.buildTowerByTowerID(towerID, tilePosX, tilePosY, EntityMode.OPPONENT);
                        User player = BitZeroServer.getInstance().getUserManager().getUserById(player1.getId());
                        // IMPORTANT: Handle bot action via Ticks
                        ExtensionUtility.getExtension().send(new ResponseOppentPutTower(BattleHandler.BattleError.SUCCESS.getValue(), towerID, 1, new java.awt.Point(tilePosX, tilePosY), currentTick), player);
                        return;
                    }
            }
        }

    }

    public void sendDraw() throws Exception {
        User user1 = BitZeroServer.getInstance().getUserManager().getUserById(player1.getId());
        User user2 = BitZeroServer.getInstance().getUserManager().getUserById(player2.getId());
        PlayerInfo userInfo1 = (PlayerInfo) PlayerInfo.getModel(player1.getId(), PlayerInfo.class);
        PlayerInfo userInfo2 = (PlayerInfo) PlayerInfo.getModel(player2.getId(), PlayerInfo.class);
        if (userInfo1.getUserType() == UserType.PLAYER)
            ExtensionUtility.getExtension().send(new ResponseEndBattle(RoomHandler.RoomError.END_BATTLE.getValue(), GameConfig.BATTLE_RESULT.DRAW, battle.getPlayer1HP(), battle.getPlayer2HP(), userInfo1.getTrophy(), 0, 0), user1);
        if (userInfo2.getUserType() == UserType.PLAYER)
            ExtensionUtility.getExtension().send(new ResponseEndBattle(RoomHandler.RoomError.END_BATTLE.getValue(), GameConfig.BATTLE_RESULT.DRAW, battle.getPlayer2HP(), battle.getPlayer1HP(), userInfo2.getTrophy(), 0, 0), user2);
    }

    public void sendWinUser(int winUserID, int loseUserID, int winnerHP, int loserHP) throws Exception {
        User user1 = BitZeroServer.getInstance().getUserManager().getUserById(winUserID);
        User user2 = BitZeroServer.getInstance().getUserManager().getUserById(loseUserID);

        PlayerInfo winUser = (PlayerInfo) PlayerInfo.getModel(winUserID, PlayerInfo.class);
        PlayerInfo loseUser = (PlayerInfo) PlayerInfo.getModel(loseUserID, PlayerInfo.class);
        LobbyChestContainer winUserLobbyChest = (LobbyChestContainer) LobbyChestContainer.getModel(winUser.getId(), LobbyChestContainer.class);
        if (winUser.getUserType() == UserType.PLAYER) {
            if (winUserLobbyChest.lobbyChestContainer.size() < LobbyChestDefine.LOBBY_CHEST_AMOUNT) {
                winUserLobbyChest.addLobbyChest();
                winUserLobbyChest.saveModel(winUser.getId());
                ExtensionUtility.getExtension().send(new ResponseEndBattle(RoomHandler.RoomError.END_BATTLE.getValue(), GameConfig.BATTLE_RESULT.WIN, winnerHP, loserHP, winUser.getTrophy(), 10, 1), user1);
            } else
                ExtensionUtility.getExtension().send(new ResponseEndBattle(RoomHandler.RoomError.END_BATTLE.getValue(), GameConfig.BATTLE_RESULT.WIN, winnerHP, loserHP, winUser.getTrophy(), 10, 0), user1);
        }
        winUser.setTrophy(winUser.getTrophy() + 10);
        winUser.saveModel(winUser.getId());
        loseUser.setTrophy(loseUser.getTrophy() - 10);
        loseUser.saveModel(loseUser.getId());
        if (loseUser.getUserType() == UserType.PLAYER)
            ExtensionUtility.getExtension().send(new ResponseEndBattle(RoomHandler.RoomError.END_BATTLE.getValue(), GameConfig.BATTLE_RESULT.LOSE, loserHP, winnerHP, loseUser.getTrophy(), -10, 0), user2);

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

    public void addClientCommand(long executeTime, BaseCmd baseCmd, int cmdID, EntityMode mode) {
        this.clientCommands.add(new ClientCommand(executeTime, baseCmd, cmdID, mode));
    }
}

class ClientCommand {
    long executeTime;
    BaseCmd baseCmd;
    int cmdID;
    EntityMode mode;

    public ClientCommand(long executeTime, BaseCmd baseCmd, int cmdID, EntityMode mode) {
        this.executeTime = executeTime;
        this.baseCmd = baseCmd;
        this.cmdID = cmdID;
        this.mode = mode;
    }

    public long getExecuteTime() {
        return this.executeTime;
    }

    public BaseCmd getBaseCmd() {
        return this.baseCmd;
    }

    public EntityMode getMode() {
        return this.mode;
    }
}