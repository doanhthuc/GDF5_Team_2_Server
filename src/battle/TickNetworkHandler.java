package battle;

import battle.common.EntityMode;
import bitzero.server.BitZeroServer;
import bitzero.server.entities.User;
import bitzero.server.extensions.data.DataCmd;
import bitzero.util.ExtensionUtility;
import cmd.CmdDefine;
import cmd.receive.battle.tower.RequestPutTower;
import cmd.send.battle.opponent.ResponseOppentPutTower;
import cmd.send.battle.player.ResponseRequestPutTower;
import model.battle.Room;
import model.battle.RoomManager;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.BattleHandler;

public class TickNetworkHandler {
    private final Logger logger = LoggerFactory.getLogger("TickHandler");

    public void TickHandler () {

    }

    public void handleCommand(int tickNumber, User user, DataCmd dataCmd) {
        try {
            switch (dataCmd.getId()) {
                case CmdDefine.PUT_TOWER:
                    System.out.println("[BattleHandler.java line 55] cmd Put tower: " + CmdDefine.PUT_TOWER);
                    RequestPutTower requestPutTower = new RequestPutTower(dataCmd);
                    processPutTower(tickNumber, user, requestPutTower);
                    break;
            }
        } catch (Exception e) {
            logger.warn(ExceptionUtils.getStackTrace(e));
        }
    }

    private void processPutTower(int tickNumber, User user, RequestPutTower req) {
        System.out.println("BattleMap processPutTower");
        try {
            Room room = RoomManager.getInstance().getRoom(req.getRoomId());

            ExtensionUtility.getExtension().send(new ResponseRequestPutTower(BattleHandler.BattleError.SUCCESS.getValue(), req.getTowerId(), 1, req.getTilePos(), tickNumber), user);

            int opponentId = room.getOpponentPlayerByMyPlayerId(user.getId()).getId();
            User opponent = BitZeroServer.getInstance().getUserManager().getUserById(opponentId);
            ExtensionUtility.getExtension().send(new ResponseOppentPutTower(BattleHandler.BattleError.SUCCESS.getValue(), req.getTowerId(), 1, req.getTilePos(), tickNumber), opponent);
        } catch (Exception e) {
            logger.info("processGetName exception");
        }
    }
}
