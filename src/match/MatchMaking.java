package match;

import battle.common.EntityMode;
import battle.common.UUIDGeneratorECS;
import battle.config.GameConfig;
import bitzero.server.BitZeroServer;
import bitzero.server.entities.User;
import bitzero.util.ExtensionUtility;
import cmd.obj.matching.MatchingInfo;
import cmd.obj.matching.OpponentInfo;
import cmd.send.battle.player.ResponseBattleDeckInBattle;
import cmd.send.battle.player.ResponseRequestBattleMapObject;
import cmd.send.battle.player.ResponseRequestGetBattleInfo;
import cmd.send.matching.ResponseCancelMatching;
import cmd.send.matching.ResponseMatching;
import extension.FresherExtension;
import model.PlayerID;
import model.PlayerInfo;
import model.UserIncrementID;
import model.battle.PlayerInBattle;
import model.battle.Room;
import model.battle.RoomManager;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.MatchingHandler;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class MatchMaking implements Runnable {
    private static final BlockingQueue<MatchingInfo> waitingQueue = new LinkedBlockingQueue<>();
    private static final Map<Integer, MatchingInfo> waitingMap = new ConcurrentHashMap<>();
    private final Logger logger = LoggerFactory.getLogger("MatchHandler");
    private static final int limitTimeout = 5000;

    @Override
    public void run() {
        this.clearDisconnectUser();
        if (waitingQueue.size() >= 1) {
            MatchingInfo matchingInfo1 = waitingQueue.peek();
            MatchingInfo matchingInfo2;

            if (System.currentTimeMillis() - matchingInfo1.getTime() > limitTimeout) {
                matchingInfo1.increaseRank();
                matchingInfo1.setTime(System.currentTimeMillis());
            }

            Iterator<MatchingInfo> it = waitingQueue.iterator();
            it.next();
            if (System.currentTimeMillis() - matchingInfo1.getStartTime() >= GameConfig.BATTLE.TIME_MATCHING_BOT) {
                processMatchingWithBot(matchingInfo1);
                waitingQueue.remove(matchingInfo1);
                waitingMap.remove(matchingInfo1.getPlayerId());
            } else {
                while (it.hasNext()) {
                    matchingInfo2 = it.next();
                    if (System.currentTimeMillis() - matchingInfo2.getTime() > limitTimeout) {
                        matchingInfo2.increaseRank();
                        matchingInfo2.setTime(System.currentTimeMillis());
                    }


                    if ((matchingInfo1.getTrophy() >= matchingInfo2.getStartRank() && matchingInfo1.getTrophy() <= matchingInfo2.getEndRank())
                            || (matchingInfo2.getTrophy() >= matchingInfo1.getStartRank() && matchingInfo2.getTrophy() <= matchingInfo1.getEndRank())) {
                        processMatching(matchingInfo1, matchingInfo2);
                        waitingMap.remove(matchingInfo1.getPlayerId());
                        waitingMap.remove(matchingInfo2.getPlayerId());
                        waitingQueue.remove(matchingInfo1);
                        waitingQueue.remove(matchingInfo2);
                        break;
                    }
                }
            }

        }

    }

    public void clearDisconnectUser() {
        for (Map.Entry<Integer, MatchingInfo> matchingEntry : waitingMap.entrySet()) {
            MatchingInfo matchingInfo = matchingEntry.getValue();
            int userID = matchingInfo.getPlayerId();
            if (!FresherExtension.checkUserOnline(userID)) {
                waitingMap.remove(matchingInfo.getPlayerId());
                waitingQueue.remove(matchingInfo);
            }
        }
    }

    public void addUser(int userId, int trophy) {
        if (waitingMap.containsKey(userId)) {
            System.out.println("matching => user id = " + userId + " is existing in waiting queue");
            return;
        }
        MatchingInfo matchingInfo = new MatchingInfo(userId, System.currentTimeMillis(), trophy);
        waitingQueue.add(matchingInfo);
        this.waitingMap.put(userId, matchingInfo);
        System.out.println("Queue size = " + waitingQueue.size());
    }

    public void cancelMatching(User user) {
        int userId = user.getId();
        if (waitingMap.containsKey(userId)) {
            System.out.println("cancel matching => user id = " + userId + " is existing in waiting queue");
            ExtensionUtility.getExtension().send(new ResponseCancelMatching(), user);
            MatchingInfo matchingInfo = waitingMap.remove(userId);
            waitingQueue.remove(matchingInfo);
        }
        System.out.println("Queue size = " + waitingQueue.size());
    }

    public void processReEnterRoom(User user, Room room) {
        PlayerInBattle player = room.getMyPlayerInBattle(user.getId());
        PlayerInBattle opponent = room.getOpponentPlayerByMyPlayerId(user.getId());
        OpponentInfo opponentInfoOfUser1 = new OpponentInfo(opponent.getId(), opponent.getUserName(), opponent.getTrophy());
        UUIDGeneratorECS uuidGeneratorECS = room.getBattle().getUuidGeneratorECS();

        EntityMode entityMode = room.getBattle().getEntityModeByPlayerID(user.getId());

        ExtensionUtility.getExtension().send(new ResponseMatching(MatchingHandler.MatchingStatus.SUCCESS.getValue(),
                room.getRoomId(),
                room.getBattle().getEntityModeByPlayerID(user.getId()),
                room.getBattle().getBattleMapByPlayerId(player.getId()),
                room.getBattle().getBattleMapByPlayerId(opponent.getId()),
                opponentInfoOfUser1), user);

        ExtensionUtility.getExtension().send(new ResponseBattleDeckInBattle(MatchingHandler.MatchingStatus.SUCCESS.getValue(),
                room.getPlayerByID(user.getId()).getBattleDeck()), user);

        ExtensionUtility.getExtension().send(new ResponseRequestBattleMapObject(MatchingHandler.MatchingStatus.SUCCESS.getValue(),
                room.getBattle().getBattleMapByPlayerId(player.getId()).battleMapObject,
                room.getBattle().getBattleMapByPlayerId(opponent.getId()).battleMapObject), user);

        ExtensionUtility.getExtension().send(new ResponseRequestGetBattleInfo(MatchingHandler.MatchingStatus.SUCCESS.getValue(),
                room.getStartTime(), room.getWaveAmount(), room.getMonsterWave(), uuidGeneratorECS.getPlayerStartEntityID(entityMode), uuidGeneratorECS.getOpponentStartEntityID(entityMode),room.getBattle().getCurrentWave()), user);
    }

    private void processMatching(MatchingInfo matchingInfo1, MatchingInfo matchingInfo2) {
        try {
            PlayerInfo userInfo1 = (PlayerInfo) PlayerInfo.getModel(matchingInfo1.getPlayerId(), PlayerInfo.class);
            PlayerInfo userInfo2 = (PlayerInfo) PlayerInfo.getModel(matchingInfo2.getPlayerId(), PlayerInfo.class);

            User user1 = BitZeroServer.getInstance().getUserManager().getUserById(matchingInfo1.getPlayerId());
            User user2 = BitZeroServer.getInstance().getUserManager().getUserById(matchingInfo2.getPlayerId());

            OpponentInfo opponentInfoOfUser1 = new OpponentInfo(userInfo2.getId(), userInfo2.getUserName(), userInfo2.getTrophy());
            OpponentInfo opponentInfoOfUser2 = new OpponentInfo(userInfo1.getId(), userInfo1.getUserName(), userInfo1.getTrophy());
            Room room = new Room(userInfo1, userInfo2);
            RoomManager.getInstance().addRoom(room);
//            new Thread(room).start();

            long t = room.getStartTime() - System.currentTimeMillis();
            BitZeroServer.getInstance().getTaskScheduler().schedule(room, (int) t, TimeUnit.MILLISECONDS);
            UUIDGeneratorECS uuidGeneratorECS = room.getBattle().getUuidGeneratorECS();

            // send matching info
            ExtensionUtility.getExtension().send(new ResponseMatching(MatchingHandler.MatchingStatus.SUCCESS.getValue(),
                    room.getRoomId(),
                    EntityMode.PLAYER,
                    room.getBattle().getBattleMapByPlayerId(user1.getId()),
                    room.getBattle().getBattleMapByPlayerId(user2.getId()),
                    opponentInfoOfUser1), user1);
            ExtensionUtility.getExtension().send(new ResponseMatching(MatchingHandler.MatchingStatus.SUCCESS.getValue(),
                    room.getRoomId(),
                    EntityMode.OPPONENT,
                    room.getBattle().getBattleMapByPlayerId(user2.getId()),
                    room.getBattle().getBattleMapByPlayerId(user1.getId()),
                    opponentInfoOfUser2), user2);

            // send card deck
            ExtensionUtility.getExtension().send(new ResponseBattleDeckInBattle(MatchingHandler.MatchingStatus.SUCCESS.getValue(),
                    room.getPlayerByID(user1.getId()).getBattleDeck()), user1);
            ExtensionUtility.getExtension().send(new ResponseBattleDeckInBattle(MatchingHandler.MatchingStatus.SUCCESS.getValue(),
                    room.getPlayerByID(user2.getId()).getBattleDeck()), user2);

            // send map object
            ExtensionUtility.getExtension().send(new ResponseRequestBattleMapObject(MatchingHandler.MatchingStatus.SUCCESS.getValue(),
                    room.getBattle().getBattleMapByPlayerId(user1.getId()).battleMapObject,
                    room.getBattle().getBattleMapByPlayerId(user2.getId()).battleMapObject), user1);
            ExtensionUtility.getExtension().send(new ResponseRequestBattleMapObject(MatchingHandler.MatchingStatus.SUCCESS.getValue(),
                    room.getBattle().getBattleMapByPlayerId(user2.getId()).battleMapObject,
                    room.getBattle().getBattleMapByPlayerId(user1.getId()).battleMapObject), user2);

            // send battle info
            ExtensionUtility.getExtension().send(new ResponseRequestGetBattleInfo(MatchingHandler.MatchingStatus.SUCCESS.getValue(),
                    room.getStartTime(), room.getWaveAmount(), room.getMonsterWave(), uuidGeneratorECS.getPlayerStartEntityID(EntityMode.PLAYER), uuidGeneratorECS.getOpponentStartEntityID(EntityMode.PLAYER) , room.getBattle().getCurrentWave()), user1);
            ExtensionUtility.getExtension().send(new ResponseRequestGetBattleInfo(MatchingHandler.MatchingStatus.SUCCESS.getValue(),
                    room.getStartTime(), room.getWaveAmount(), room.getMonsterWave(), uuidGeneratorECS.getPlayerStartEntityID(EntityMode.OPPONENT), uuidGeneratorECS.getOpponentStartEntityID(EntityMode.OPPONENT), room.getBattle().getCurrentWave()), user2);

        } catch (Exception e) {
            System.out.println(ExceptionUtils.getStackTrace(e));
        }
    }

    private void processMatchingWithBot(MatchingInfo matchingInfo1) {
        System.out.println("ProcessMatchingWithBot");
        try {

            PlayerInfo userInfo1 = (PlayerInfo) PlayerInfo.getModel(matchingInfo1.getPlayerId(), PlayerInfo.class);

            User user1 = BitZeroServer.getInstance().getUserManager().getUserById(matchingInfo1.getPlayerId());

            PlayerInfo dummyBot = createNewBot();
            dummyBot.setUserType(UserType.BOT_TYPE_1);
            Room room = new Room(userInfo1, dummyBot);
            RoomManager.getInstance().addRoom(room);
            new Thread(room).start();
            // add opponent's username, trophy and 8 card
            UUIDGeneratorECS uuidGeneratorECS = room.getBattle().getUuidGeneratorECS();
            OpponentInfo opponentInfoOfUser1 = new OpponentInfo(dummyBot.getId(), dummyBot.getUserName(), dummyBot.getTrophy());

            ExtensionUtility.getExtension().send(new ResponseMatching(MatchingHandler.MatchingStatus.SUCCESS.getValue(),
                    room.getRoomId(), EntityMode.PLAYER, room.getPlayerBattleMap(userInfo1.getId()), room.getPlayerBattleMap(dummyBot.getId()),
                    opponentInfoOfUser1), user1);

            ExtensionUtility.getExtension().send(new ResponseRequestGetBattleInfo(MatchingHandler.MatchingStatus.SUCCESS.getValue(),
                    room.getStartTime(), room.getWaveAmount(), room.getMonsterWave(), uuidGeneratorECS.getPlayerStartEntityID(EntityMode.PLAYER), uuidGeneratorECS.getOpponentStartEntityID(EntityMode.PLAYER),
                    room.getBattle().getCurrentWave()), user1);


            ExtensionUtility.getExtension().send(
                    new ResponseRequestBattleMapObject(MatchingHandler.MatchingStatus.SUCCESS.getValue(),
                            room.getBattle().getBattleMapByPlayerId(user1.getId()).battleMapObject,
                            room.getBattle().getBattleMapByPlayerId(dummyBot.getId()).battleMapObject), user1);

            ExtensionUtility.getExtension().send(new ResponseBattleDeckInBattle(MatchingHandler.MatchingStatus.SUCCESS.getValue(),
                    room.getPlayerByID(user1.getId()).getBattleDeck()), user1);
//            for (int i = 0; i < userMap.battleMapObject.getHeight(); i++) {
//                for (int j = 0; j < userMap.battleMapObject.getWidth(); j++) {
//                    CellObject cellObject = userMap.battleMapObject.getCellObject(i, j);
//                    System.out.println("[ResponseRequestBattleMapObject] cellObject: " + cellObject);
//                }
//                System.out.println();
//            }
        } catch (Exception e) {
            System.out.println(ExceptionUtils.getStackTrace(e));
        }
    }


    public PlayerInfo createNewBot() throws Exception {
        PlayerInfo botInfo = null;
        String botName = "Bot";
        if (PlayerID.getModel(botName, PlayerID.class) == null) {
            UserIncrementID newID = (UserIncrementID) UserIncrementID.getModel(0, UserIncrementID.class);
            int newUserID = newID.genIncrementID();
            newID.saveModel(0);

            botInfo = new PlayerInfo(newUserID, botName, 0, 0, 0);
            botInfo.setUserType(UserType.BOT_TYPE_1);
            botInfo.saveModel(botInfo.getId());

            PlayerID newPID = new PlayerID(newUserID, botName);

            newPID.saveModel(botInfo.getUserName());

            FresherExtension.initUserData(botInfo.getId());
        } else {
            PlayerID pID = (PlayerID) PlayerID.getModel(botName, PlayerID.class);
            botInfo = (PlayerInfo) PlayerInfo.getModel(pID.userID, PlayerInfo.class);
            botInfo.setUserType(UserType.BOT_TYPE_1);
        }
        return botInfo;
    }


}
