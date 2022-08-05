package battle;

import battle.common.EntityMode;
import battle.newMap.BattleMapObject;
import battle.newMap.Tower;
import bitzero.server.BitZeroServer;
import bitzero.server.entities.User;
import bitzero.server.extensions.data.DataCmd;
import bitzero.util.ExtensionUtility;
import cmd.CmdDefine;
import cmd.receive.battle.tower.RequestPutTower;
import cmd.receive.battle.tower.RequestUpgradeTower;
import cmd.send.battle.opponent.ResponseOppentPutTower;
import cmd.send.battle.opponent.ResponseOpponentUpgradeTower;
import cmd.send.battle.player.ResponseRequestPutTower;
import cmd.send.battle.player.ResponseRequestUpgradeTower;
import model.Inventory.Card;
import model.Inventory.Inventory;
import model.battle.Room;
import model.battle.RoomManager;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.BattleHandler;

public class TickNetworkHandler {
    private final Logger logger = LoggerFactory.getLogger("TickHandler");

    public TickNetworkHandler() {

    }

    public void handleCommand(int tickNumber, User user, DataCmd dataCmd) {
        try {
            switch (dataCmd.getId()) {
                case CmdDefine.PUT_TOWER: {
                    System.out.println("[BattleHandler.java line 55] cmd Put tower: " + CmdDefine.PUT_TOWER);
                    RequestPutTower requestPutTower = new RequestPutTower(dataCmd);
                    processPutTower(tickNumber, user, requestPutTower);
                    break;
                }
                case CmdDefine.UPGRADE_TOWER: {
                    System.out.println("[BattleHandler.java line 56] cmd Upgrade tower: " + CmdDefine.UPGRADE_TOWER);
                    RequestUpgradeTower requestUpgradeTower = new RequestUpgradeTower(dataCmd);
                    processUpgradeTower(tickNumber, user, requestUpgradeTower);
                }
            }
        } catch (Exception e) {
            logger.warn(ExceptionUtils.getStackTrace(e));
        }
    }

    private void processPutTower(int tickNumber, User user, RequestPutTower req) {
        System.out.println("BattleMap processPutTower");
        try {
            Room room = RoomManager.getInstance().getRoom(req.getRoomId());

            EntityMode entityMode = room.getBattle().getEntityModeByPlayerID(user.getId());
            ExtensionUtility.getExtension().send(new ResponseRequestPutTower(BattleHandler.BattleError.SUCCESS.getValue(), req.getTowerId(), 1, req.getTilePos(), tickNumber), user);
            room.getBattle().buildTowerByTowerID(req.getTowerId(), req.getTilePos().x, req.getTilePos().y, entityMode);

            int opponentId = room.getOpponentPlayerByMyPlayerId(user.getId()).getId();
            User opponent = BitZeroServer.getInstance().getUserManager().getUserById(opponentId);
            ExtensionUtility.getExtension().send(new ResponseOppentPutTower(BattleHandler.BattleError.SUCCESS.getValue(), req.getTowerId(), 1, req.getTilePos(), tickNumber), opponent);
        } catch (Exception e) {
            System.out.println(ExceptionUtils.getStackTrace(e));
        }
    }

    private void processUpgradeTower(int tickNumber, User user, RequestUpgradeTower req) {
        try {
            Room room = RoomManager.getInstance().getRoom(req.getRoomId());
            int towerId = req.getTowerId();
            BattleMap battleMap = room.getBattle().getBattleMapByPlayerId(user.getId());
            BattleMapObject battleMapObject = battleMap.battleMapObject;
            Tower tower = (Tower) battleMapObject.getCellObject(req.getTilePos()).getObjectInCell();
            Inventory inventory = (Inventory) Inventory.getModel(user.getId(), Inventory.class);
            Card towerCard = inventory.getCardById(req.getTowerId());
//            if (towerCard.getCardRankNumber() < tower.getLevel()) {
//                tower = tower.upgradeTower();
//            } else {
//                return;
//            }

            if (tower == null) {
                System.out.println("[BattleHandler.java line 103 processUpgradeTower]  tower null");
                return;
            }

            if (tower.getId() != towerId) {
                System.out.println("[BattleHandler.java line 103 processUpgradeTower]  tower id not match");
                return;
            }

            tower = tower.upgradeTower();
            System.out.println("[BattleHandler.java line 103 processUpgradeTower]  cellObject " + battleMapObject.getCellObject(req.getTilePos()));
            ExtensionUtility.getExtension().send(new ResponseRequestUpgradeTower(BattleHandler.BattleError.SUCCESS.getValue(),
                    req.getTowerId(), tower.getLevel(), tower.getTilePos(), tickNumber), user);
            int opponentId = room.getOpponentPlayerByMyPlayerId(user.getId()).getId();
            User opponent = BitZeroServer.getInstance().getUserManager().getUserById(opponentId);
            ExtensionUtility.getExtension().send(new ResponseOpponentUpgradeTower(BattleHandler.BattleError.SUCCESS.getValue(),
                    req.getTowerId(), tower.getLevel(), tower.getTilePos(), tickNumber), opponent);
        } catch (Exception e) {
            System.out.println(ExceptionUtils.getStackTrace(e));
        }
    }
}
