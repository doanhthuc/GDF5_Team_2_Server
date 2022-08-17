package model.battle;

import battle.*;
import battle.common.*;
import battle.component.common.PositionComponent;
import battle.component.info.MonsterInfoComponent;
import battle.config.GameConfig;
import battle.config.conf.potion.PotionConfig;
import battle.config.conf.tower.TowerConfig;
import battle.entity.EntityECS;
import battle.map.BattleMap;
import battle.newMap.Tower;
import battle.tick.TickManager;
import bitzero.server.BitZeroServer;
import bitzero.server.entities.User;
import bitzero.server.exceptions.BZException;
import bitzero.server.extensions.data.DataCmd;

import match.UserType;
import model.Inventory.Card;
import model.PlayerInfo;

import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
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
    private long botActionTime;
    private final TickManager tickManager;
    private int maxTick = 100000;
    private final Queue<Pair<PlayerInfo, DataCmd>> waitingInputQueue = new ConcurrentLinkedQueue<>();
    private double[] checkSum = new double[maxTick];

    public Room(PlayerInfo player1, PlayerInfo player2) throws Exception {
        this.roomId = RoomManager.getInstance().getRoomCount();
        this.player1 = new PlayerInBattle(player1);
        this.player2 = new PlayerInBattle(player2);
        this.battle = new Battle(player1, player2);
        this.endBattle = false;
        this.startTime = System.currentTimeMillis() + GameConfig.BATTLE.START_GAME_AFTER;
        this.botActionTime = this.startTime + 1000;

        if (GameConfig.DEBUG) {
            new BattleVisualization(this.battle, this.battle.getEntityModeByPlayerID(this.player2.getId()));
            new BattleVisualization(this.battle, this.battle.getEntityModeByPlayerID(this.player1.getId()));
        }

        this.tickManager = new TickManager(this.startTime);
        this.battle.setNextWaveTimeTick((int) (GameConfig.BATTLE.WAVE_TIME / tickManager.getTickRate()));

    }

    public void addInput(PlayerInfo playerInfo, DataCmd dataCmd) {
        this.waitingInputQueue.add(new Pair<>(playerInfo, dataCmd));
    }

    public void addBornMonsterCommandToTick() {
//        List<List<Integer>> monsterWave = this.getMonsterWave();
//        for(int i=0;i=)
    }

    @Override
    public void run() {
        this.roomRun = BitZeroServer.getInstance().getTaskScheduler().scheduleAtFixedRate(() -> {
            try {
                if (!this.endBattle) {
                    int currentTick = this.tickManager.getCurrentTick();
                    this.handleBotAction();
                    while (!this.waitingInputQueue.isEmpty()) {
                        Pair<PlayerInfo, DataCmd> data = this.waitingInputQueue.poll();
                        this.tickManager.addInput(data);
                    }
                    // handle the inputs in the current tick
                    this.updateMonsterWave(currentTick);
                    this.battle.updateSystem();
                    this.tickManager.handleInternalInputTick(currentTick);
                    this.updatePlayerCheckSum(currentTick);
                    this.checkEndBattle();
                    this.tickManager.increaseTick();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, GameConfig.BATTLE.TICK_RATE, TimeUnit.MILLISECONDS);
    }


    public void checkEndBattle() throws Exception {
        this.checkAllUserDisconnect();
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
                SendResult.sendWinUser(winUserID, loseUserID, Math.max(player1HP, player2HP), Math.min(player1HP, player2HP), tickManager.getCurrentTick());
            else
                SendResult.sendDrawBattle(player1.getId(), player2.getId(), this.battle.getPlayer1HP(), tickManager.getCurrentTick());
            this.endRoom();
        }

    }

    public void updatePlayerCheckSum(int currentTick) {
        if (currentTick < 0) return;
        //TODO: FIX HARD CODE +400 (TREE)
        this.checkSum[currentTick] = battle.getSumHp() + 400;
        System.out.println("currentTick = " + currentTick + " SumHp = " + this.checkSum[currentTick]);
    }

    public void checkAllUserDisconnect() throws InterruptedException {
        boolean user1Connection = BitZeroServer.getInstance().getUserManager().containsId(player1.getId());
        boolean user2Connection = BitZeroServer.getInstance().getUserManager().containsId(player2.getId());
        if (!user1Connection && !user2Connection) {
            this.endRoom();
            System.out.println("allUserDisconnect");
        }
    }

    public void endRoom() throws InterruptedException {
        Thread.sleep(10000);
        this.roomRun.cancel(true);
        RoomManager.getInstance().removeRoom(this.roomId);
    }

    public void handleBotAction() throws BZException {
        if (this.player2.getUserType() == UserType.PLAYER) return;
        long delayBotCommandTime = 2500;
        if (System.currentTimeMillis() > this.botActionTime) {
            //get Card to use
            int cardToUseID = (int) (Math.random() * player2.getCardDeckSize());
            Card cardToUse = player2.getBattleDeck().get(cardToUseID);
            int cardID = cardToUse.getCardType();
            int energy;
            if (GameConfig.GROUP_ID.TOWER_ENTITY.contains(cardID))
                energy = TowerConfig.INS.getTowerConfig((short) cardID).getEnergy();
            else
                energy = PotionConfig.INS.getSpellIDInConfig(cardID).getEnergy();
            if (this.battle.getPlayer2energy() <= energy) return;
            // get Bot BattleMap
            BattleMap botBattleMap = this.battle.player2BattleMap;
            ArrayList<java.awt.Point> monsterPath = botBattleMap.getPath();
            //handle next bot action
            this.botActionTime = System.currentTimeMillis() + delayBotCommandTime;
            switch (cardToUse.getCardType()) {
                case GameConfig.ENTITY_ID.CANNON_TOWER:
                case GameConfig.ENTITY_ID.BEAR_TOWER:
                case GameConfig.ENTITY_ID.WIZARD_TOWER:
                case GameConfig.ENTITY_ID.FROG_TOWER:
                case GameConfig.ENTITY_ID.BUNNY_TOWER: {
                    //Attack and Magic tower Will be put beside Monster Path
                    int towerID = cardToUse.getCardType();
                    for (int i = monsterPath.size() - 1; i >= 0; i--) {
                        List<Integer> dX = Arrays.asList(1, 0, -1, 0, -1, 1, 1, -1);
                        List<Integer> dY = Arrays.asList(0, 1, 0, -1, 1, -1, 1, -1);
                        java.awt.Point currentPoint = monsterPath.get(i);
//                        botBattleMap.show();
                        for (int j = 0; j < dX.size(); j++) {
                            int tilePosX = currentPoint.x + dX.get(j);
                            int tilePosY = currentPoint.y + dY.get(j);
                            if (botBattleMap.isInBound(tilePosX, tilePosY))
                                if (botBattleMap.isMovableTile(botBattleMap.map[tilePosX][tilePosY])
                                        && (!monsterPath.contains(new java.awt.Point(tilePosX, tilePosY)))
                                        && botBattleMap.map[tilePosX][tilePosY] != GameConfig.MAP.TOWER) {
                                    //Create Bot Command
                                    player2.moveCardToEnd(cardToUseID);
                                    DataCmd reqPutTowerCmd = CmdFactory.createRequestPutTower(roomId, towerID, tilePosX, tilePosY);
                                    this.tickManager.addInput(new Pair<>(player2, reqPutTowerCmd));
                                    return;
                                }
                        }
                    }
                    break;
                }
                case GameConfig.ENTITY_ID.FIRE_SPELL:
                case GameConfig.ENTITY_ID.FROZEN_SPELL: {
                    //Fire Spell and Frozen Spell will be cast to many monster as much as possible
                    int spellID = cardToUse.getCardType();
                    double spellRange = PotionConfig.INS.getPotionConfig(PotionConfig.FIREBALL).getRadius() * GameConfig.TILE_WIDTH;
                    int maxMonsterInSpellRange = 0;
                    double spellX = 0;
                    double spellY = 0;
                    List<EntityECS> monsterList = this.battle.getEntityManager().getEntitiesHasComponents(Arrays.asList(MonsterInfoComponent.typeID, PositionComponent.typeID));
                    for (int i = 0; i < monsterList.size() - 1; i++) {
                        EntityECS targetMonster = monsterList.get(i);
                        if (targetMonster.getMode() == EntityMode.PLAYER) continue;
                        int monsterInSpellRange = 0;
                        PositionComponent monsterPos = (PositionComponent) targetMonster.getComponent(PositionComponent.typeID);
                        for (int j = i + 1; j < monsterList.size() - 1; j++) {
                            EntityECS monsterInRange = monsterList.get(i);
                            if (monsterInRange.getMode() == EntityMode.PLAYER) continue;
                            if (Utils._distanceFrom(targetMonster, monsterList.get(j)) <= spellRange)
                                monsterInSpellRange++;
                        }
                        if (monsterInSpellRange > maxMonsterInSpellRange) {
                            spellX = monsterPos.getX();
                            spellY = monsterPos.getY();
                            maxMonsterInSpellRange = monsterInSpellRange;
                        }
                    }
                    if (maxMonsterInSpellRange < 2) break;
                    //createBotCommand
                    player2.moveCardToEnd(cardToUseID);
                    DataCmd requestDropSpellCmd = CmdFactory.createRequestDropSpell(roomId, spellID, spellX, spellY);
                    this.tickManager.addInput(new Pair<>(player2, requestDropSpellCmd));
                    return;
                }
                case GameConfig.ENTITY_ID.GOAT_TOWER:
                case GameConfig.ENTITY_ID.SNAKE_TOWER: {
                    int towerID = cardToUse.getCardType();
                    List<Integer> dX = Arrays.asList(1, 0, -1, 0);
                    List<Integer> dY = Arrays.asList(0, 1, 0, -1);
                    for (int tilePosX = 0; tilePosX < GameConfig.MAP_WIDTH; tilePosX++) {
                        for (int tilePosY = 0; tilePosY < GameConfig.MAP_HEIGHT; tilePosY++) {
                            java.awt.Point targetPoint = new java.awt.Point(tilePosX, tilePosY);
                            int countTower = 0;
                            if (monsterPath.contains(targetPoint)) continue;
                            if (!botBattleMap.isMovableTile(botBattleMap.map[tilePosX][tilePosY])) continue;
                            for (int k = 0; k < dX.size(); k++) {
                                int i = tilePosX + dX.get(k);
                                int j = tilePosY + dY.get(k);
                                if (botBattleMap.isInBound(i, j))
                                    if (botBattleMap.map[i][j] == GameConfig.MAP.TOWER) {
                                        long id = botBattleMap.battleMapObject.getTowerInTile(i, j).getEntityId();
                                        EntityECS tower = battle.getEntityManager().getEntity(id);
                                        if (battle.isAttackTower(tower)) countTower++;
                                    }
                            }
                            if (countTower >= 1) {
                                player2.moveCardToEnd(cardToUseID);
                                DataCmd reqPutTowerCmd = CmdFactory.createRequestPutTower(roomId, towerID, tilePosX, tilePosY);
                                this.tickManager.addInput(new Pair<>(player2, reqPutTowerCmd));
                                return;
                            }
                        }
                    }
                    break;
                }
            }
            // upgrade tower will be the last choice
            if (GameConfig.GROUP_ID.TOWER_ENTITY.contains(cardID)) {
                for (int tilePosX = 0; tilePosX < GameConfig.MAP_WIDTH; tilePosX++) {
                    for (int tilePosY = 0; tilePosY < GameConfig.MAP_HEIGHT; tilePosY++) {
                        if (botBattleMap.battleMapObject.isHavingTowerInTile(tilePosX, tilePosY)) {
                            Tower tower = botBattleMap.battleMapObject.getTowerInTile(tilePosX, tilePosY);
                            if (tower.getId() == cardID && tower.getLevel() <= 1) {
                                player2.moveCardToEnd(cardToUseID);
                                DataCmd reqUpgradeTowerCmd = CmdFactory.createRequestUpgradeTower(roomId, cardID, tilePosX, tilePosY);
                                this.tickManager.addInput(new Pair<>(player2, reqUpgradeTowerCmd));
                                this.battle.minusPlayerEnergy(energy, EntityMode.OPPONENT);
                                return;
                            }
                        }
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

    public PlayerInfo getOpponentPlayerByMyPlayerId(int playerId) {
        if (playerId == player1.getId()) {
            return player2;
        } else {
            return player1;
        }
    }

    public PlayerInBattle getMyPlayerInBattle(int playerID) {
        if (playerID == player1.getId()) {
            return player1;
        } else {
            return player2;
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

    public void checkClientSumHp(double[] clientSumHpInEachTick, User user) {
        double[] serverSumHpInEachTick = this.checkSum;
        int diffTick = 0,totalTick =0;
        for (int i = 0; i < clientSumHpInEachTick.length; i++) {
            if (serverSumHpInEachTick[i] != clientSumHpInEachTick[i] && serverSumHpInEachTick[i] != 0 && clientSumHpInEachTick[i] != 0) {
                System.out.println("Difference at tick" + i + " ServerSumHp = " + serverSumHpInEachTick[i] + " UserSumHp = " + clientSumHpInEachTick[i]);
                diffTick += 1;
            }
            totalTick+=1;
        }
        System.out.println("---------------------");
        System.out.println("Number of Difference Tick:" + diffTick + " TotalTick:" + totalTick);
        System.out.println("Percent of Difference Tick:" + diffTick * 1.0 / totalTick);
        System.out.println("---------------------");
    }

    // born Monster in tick
    public void addBornMonsterToTickInput(int waveIdx, int currentTick) throws BZException {
        if (waveIdx == -1) return;
        List<Integer> monsterWaveList = this.getMonsterWave().get(waveIdx);
        for (int i = 0; i < monsterWaveList.size(); i++) {
            DataCmd bornMonsterCmd = CmdFactory.createBornMonsterCmd(this.roomId, monsterWaveList.get(i));
            int tickNumber = (i * 1000) / GameConfig.BATTLE.TICK_RATE + currentTick;
            System.out.println("addMonster" + tickNumber + " " + monsterWaveList.get(i));
            this.tickManager.addInputToTick(new Pair<>(null, bornMonsterCmd), tickNumber);
        }
        SendResult.sendNextWave(this.player1.getId(), this.player2.getId(), monsterWaveList, currentTick);
    }

    public void updateMonsterWave(int currentTick) throws BZException {
        System.out.println(currentTick + " " + this.battle.nextWaveTimeTick);
        if (currentTick >= this.battle.nextWaveTimeTick) {
            this.addBornMonsterToTickInput(battle.currentWave, this.battle.nextWaveTimeTick + 10);
            this.battle.nextWaveTimeTick += GameConfig.BATTLE.WAVE_TIME / tickManager.getTickRate();
            this.battle.currentWave += 1;
        }
    }

    public void speedUpNextWave() {
        this.battle.setNextWaveTimeTick(this.tickManager.getCurrentTick());
    }
}
