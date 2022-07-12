package match;

import battle.Battle;
import battle.BattleMap;
import bitzero.server.BitZeroServer;
import bitzero.server.entities.User;
import bitzero.util.ExtensionUtility;
import cmd.obj.matching.MatchingInfo;
import cmd.obj.matching.OpponentInfo;
import cmd.send.matching.ResponseCancelMatching;
import cmd.send.matching.ResponseMatching;
import model.PlayerInfo;
import model.battle.Room;
import model.battle.RoomManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.MatchingHandler;

import java.util.Iterator;
import java.util.Map;
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
        while (waitingQueue.size() >= 2) {
            MatchingInfo matchingInfo1 = waitingQueue.peek();
            MatchingInfo matchingInfo2;

            if (System.currentTimeMillis() - matchingInfo1.getTime() > limitTimeout) {
                matchingInfo1.increaseRank();
                matchingInfo1.setTime(System.currentTimeMillis());
            }

            Iterator<MatchingInfo> it = waitingQueue.iterator();
            it.next();
            while (it.hasNext()) {
                matchingInfo2 = it.next();

                if (System.currentTimeMillis() - matchingInfo2.getTime() > limitTimeout) {
                    matchingInfo2.increaseRank();
                    matchingInfo2.setTime(System.currentTimeMillis());
                }

                if ((matchingInfo1.getTrophy() >= matchingInfo2.getStartRank() && matchingInfo1.getTrophy() <= matchingInfo2.getEndRank())
                        || (matchingInfo2.getTrophy() >= matchingInfo1.getStartRank() && matchingInfo2.getTrophy() <= matchingInfo1.getEndRank())) {
                    processMatching(matchingInfo1, matchingInfo2);
                    break;
                }
            }
        }
    }

    public void addUser(int userId, int trophy) {
        if (waitingMap.containsKey(userId)) {
            logger.warn("matching => user id = " + userId + " is existing in waiting queue");
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
            logger.warn("cancel matching => user id = " + userId + " is existing in waiting queue");
            ExtensionUtility.getExtension().send(new ResponseCancelMatching(), user);
            MatchingInfo matchingInfo = waitingMap.remove(userId);
            waitingQueue.remove(matchingInfo);
        }
        System.out.println("Queue size = " + waitingQueue.size());
    }

    private void processMatching(MatchingInfo matchingInfo1, MatchingInfo matchingInfo2) {
        try {
            PlayerInfo userInfo1 = (PlayerInfo) PlayerInfo.getModel(matchingInfo1.getPlayerId(), PlayerInfo.class);
            PlayerInfo userInfo2 = (PlayerInfo) PlayerInfo.getModel(matchingInfo2.getPlayerId(), PlayerInfo.class);

            User user1 = BitZeroServer.getInstance().getUserManager().getUserById(matchingInfo1.getPlayerId());
            User user2 = BitZeroServer.getInstance().getUserManager().getUserById(matchingInfo2.getPlayerId());

            OpponentInfo opponentInfoOfUser1 = new OpponentInfo(userInfo2.getId(), userInfo2.getUserName(), userInfo2.getTrophy());
            OpponentInfo opponentInfoOfUser2 = new OpponentInfo(userInfo1.getId(), userInfo1.getUserName(), userInfo1.getTrophy());

            BattleMap user1Map = new BattleMap();
            BattleMap user2Map = new BattleMap();

            Room room = new Room(userInfo1, userInfo2, user1Map, user2Map);
            RoomManager.getInstance().addRoom(room);
            new Thread(room).start();
            // add opponent's username, trophy and 8 card

            ExtensionUtility.getExtension().send(new ResponseMatching(MatchingHandler.MatchingStatus.SUCCESS.getValue(),
                    room.getRoomId(), user1Map, user2Map, opponentInfoOfUser1), user1);
            ExtensionUtility.getExtension().send(new ResponseMatching(MatchingHandler.MatchingStatus.SUCCESS.getValue(),
                    room.getRoomId(), user2Map, user1Map, opponentInfoOfUser2), user2);

            waitingQueue.remove(matchingInfo1);
            waitingQueue.remove(matchingInfo2);
            waitingMap.remove(matchingInfo1.getPlayerId());
            waitingMap.remove(matchingInfo2.getPlayerId());

        } catch (Exception e) {
            logger.error("MatchMaking error: " + e.getMessage());
        }
    }
}
