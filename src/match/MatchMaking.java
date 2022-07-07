package match;

import battle.BattleMap;
import bitzero.server.BitZeroServer;
import bitzero.server.entities.User;
import bitzero.util.ExtensionUtility;
import cmd.obj.matching.MatchingInfo;
import cmd.send.matching.ResponseMatching;
import model.PlayerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.MatchingHandler;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MatchMaking implements Runnable {
    private static Set<MatchingInfo> queue = new HashSet<>();
    private final Logger logger = LoggerFactory.getLogger("MatchHandler");

    @Override
    public void run() {
        while (!queue.isEmpty()) {
            if (queue.size() >= 2) {
                Iterator<MatchingInfo> it = queue.iterator();
                MatchingInfo matchingInfo1 = it.next();
                MatchingInfo matchingInfo2 = it.next();

                processMatching(matchingInfo1, matchingInfo2);
            }
        }
    }

    public void addUser(long userId) {
        synchronized (queue) {
            MatchingInfo matchingInfo = new MatchingInfo(userId, System.currentTimeMillis());
            queue.add(matchingInfo);
        }
    }

    private void processMatching(MatchingInfo matchingInfo1, MatchingInfo matchingInfo2) {
        try {
            PlayerInfo userInfo1 = (PlayerInfo) PlayerInfo.getModel(matchingInfo1.playerId, PlayerInfo.class);
            PlayerInfo userInfo2 = (PlayerInfo) PlayerInfo.getModel(matchingInfo2.playerId, PlayerInfo.class);

            User user1 = BitZeroServer.getInstance().getUserManager().getUserById(matchingInfo1.playerId);
            User user2 = BitZeroServer.getInstance().getUserManager().getUserById(matchingInfo2.playerId);

            BattleMap user1Map = new BattleMap();
            BattleMap user2Map = new BattleMap();

            // add opponent's username, trophy and 8 card

            ExtensionUtility.getExtension().send(new ResponseMatching(MatchingHandler.MatchingStatus.SUCCESS.getValue(),
                    user1Map, user2Map), user1);
            ExtensionUtility.getExtension().send(new ResponseMatching(MatchingHandler.MatchingStatus.SUCCESS.getValue(),
                    user2Map, user1Map), user2);

        } catch (Exception e) {
            logger.error("MatchMaking error: " + e.getMessage());
        }
    }
}
