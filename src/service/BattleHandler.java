package service;

import battle.BattleMap;
import battle.newMap.BattleMapObject;
import battle.newMap.Tower;
import bitzero.server.BitZeroServer;
import bitzero.server.core.BZEventType;
import bitzero.server.core.IBZEvent;
import bitzero.server.entities.User;
import bitzero.server.extensions.BaseClientRequestHandler;
import bitzero.server.extensions.data.DataCmd;
import cmd.CmdDefine;
import cmd.HandlerId;
import cmd.receive.battle.spell.RequestDropSpell;
import cmd.receive.battle.tower.RequestPutTower;
import cmd.receive.battle.tower.RequestUpgradeTower;
import cmd.send.battle.*;
import event.eventType.DemoEventType;
import extension.FresherExtension;
import model.Inventory.Card;
import model.Inventory.Inventory;
import model.PlayerInfo;
import model.battle.Room;
import model.battle.RoomManager;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.server.ServerConstant;

public class BattleHandler extends BaseClientRequestHandler {
    public static short HANDLER_ID = HandlerId.BATTLE.getValue();
    private final Logger logger = LoggerFactory.getLogger("UserHandler");

    public BattleHandler() {
        super();
    }

    public void init() {
        getExtension().addEventListener(BZEventType.USER_DISCONNECT, this);
        getExtension().addEventListener(BZEventType.USER_RECONNECTION_SUCCESS, this);

        /**
         *  register new event, so the core will dispatch event type to this class
         */
        getExtension().addEventListener(DemoEventType.CHANGE_NAME, this);
    }

    private FresherExtension getExtension() {
        return (FresherExtension) getParentExtension();
    }

    public void handleServerEvent(IBZEvent ibzevent) {

    }

    public void handleClientRequest(User user, DataCmd dataCmd) {
        try {
            System.out.println("[BattleHandler.java line 50] cmdId: " + dataCmd.getId());
            switch (dataCmd.getId()) {
                case CmdDefine.GET_BATTLE_MAP:
                    System.out.println("[BattleHandler.java line 54] GET_BATTLE_MAP cmdId: " + dataCmd.getId());
                    processGetBattleMap(user);
                    break;
                case CmdDefine.PUT_TOWER:
                    System.out.println("[BattleHandler.java line 55] cmd Put tower: " + CmdDefine.PUT_TOWER);
                    RequestPutTower requestPutTower = new RequestPutTower(dataCmd);
                    processPutTower(user, requestPutTower);
                    break;
                case CmdDefine.UPGRADE_TOWER:
                    System.out.println("[BattleHandler.java line 56] cmd Upgrade tower: " + CmdDefine.UPGRADE_TOWER);
                    RequestUpgradeTower requestUpgradeTower = new RequestUpgradeTower(dataCmd);
                    processUpgradeTower(user, requestUpgradeTower);
                    break;
                case CmdDefine.DROP_SPELL:
                    System.out.println("[BattleHandler.java line 57] cmd Drop spell: " + CmdDefine.DROP_SPELL);
                    RequestDropSpell requestDropSpell = new RequestDropSpell(dataCmd);
                    processDropSpell(user, requestDropSpell);
                    break;
                default:
                    break;

            }
        } catch (Exception e) {
            logger.warn("BATTLEHANDLER EXCEPTION " + e.getMessage());
            logger.warn(ExceptionUtils.getStackTrace(e));
        }

    }

    private void processGetBattleMap(User user) {
        System.out.println("BattleMap " + " processGetBattleMap");
        try {
            PlayerInfo userInfo = (PlayerInfo) user.getProperty(ServerConstant.PLAYER_INFO);
            if (userInfo == null) {
                send(new ResponseRequestGetBattleMap(BattleError.USER_INFO_NULL.getValue()), user);
                logger.info("PlayerInfo null");
                return;
            }
            BattleMap btm = new BattleMap();
            btm.show();
            send(new ResponseRequestGetBattleMap(BattleHandler.BattleError.SUCCESS.getValue(), btm), user);
        } catch (Exception e) {
            logger.info("processGetName exception");
        }
    }

    private void processPutTower(User user, RequestPutTower req) {
        System.out.println("BattleMap processPutTower");
        try {
            Room room = RoomManager.getInstance().getRoom(req.getRoomId());
            BattleMap battleMap = room.getBattle().getBattleMapByPlayerId(user.getId());
            BattleMapObject battleMapObject = battleMap.battleMapObject;
            System.out.println("Battle Handler line 107 towerId: " + req.getTowerId());
            Tower tower = battleMapObject.putTowerIntoMap(req.getTilePos(), req.getTowerId());
            if (tower == null) {
                System.out.println("[BattleHandler.java line 103 processPutTower]  tower null");
                return;
            }
            send(new ResponseRequestPutTower(BattleHandler.BattleError.SUCCESS.getValue(), req.getTowerId(), tower.getLevel(), tower.getTilePos()), user);
            int opponentId = room.getOpponentPlayerByMyPlayerId(user.getId()).getId();
            User opponent = BitZeroServer.getInstance().getUserManager().getUserById(opponentId);
            send(new ResponseOppentPutTower(BattleHandler.BattleError.SUCCESS.getValue(), req.getTowerId(), tower.getLevel(), tower.getTilePos()), opponent);
        } catch (Exception e) {
            logger.info("processGetName exception");
        }
    }

    private void processUpgradeTower(User user, RequestUpgradeTower req) {
        System.out.println("BattleMap processUpgradeTower");
        try {
            Room room = RoomManager.getInstance().getRoom(req.getRoomId());
            BattleMap battleMap = room.getBattle().getBattleMapByPlayerId(user.getId());
            BattleMapObject battleMapObject = battleMap.battleMapObject;
            Tower tower = (Tower) battleMapObject.getCellObject(req.getTilePos()).getObjectInCell();
            Inventory inventory = (Inventory) Inventory.getModel(user.getId(), Inventory.class);
            Card towerCard = inventory.getCardById(req.getTowerId());
            if (towerCard.getCardRankNumber() > tower.getLevel()) {
                tower = tower.upgradeTower();
            } else {
                return;
            }
            if (tower == null) {
                System.out.println("[BattleHandler.java line 103 processUpgradeTower]  tower null");
                return;
            }
            System.out.println("[BattleHandler.java line 103 processUpgradeTower]  cellObject " + battleMapObject.getCellObject(req.getTilePos()));
            send(new ResponseRequestUpgradeTower(BattleHandler.BattleError.SUCCESS.getValue(),
                    req.getTowerId(), tower.getLevel(), tower.getTilePos()), user);
            int opponentId = room.getOpponentPlayerByMyPlayerId(user.getId()).getId();
            User opponent = BitZeroServer.getInstance().getUserManager().getUserById(opponentId);
            send(new ResponseOpponentUpgradeTower(BattleHandler.BattleError.SUCCESS.getValue(),
                    req.getTowerId(), tower.getLevel(), tower.getTilePos()), opponent);
        } catch (Exception e) {
            logger.info("processGetName exception");
        }
    }

    private void processDropSpell(User user, RequestDropSpell req) {
        System.out.println("BattleMap processDropSpell");
        try {
            Room room = RoomManager.getInstance().getRoom(req.getRoomId());
            Inventory inventory = (Inventory) Inventory.getModel(user.getId(), Inventory.class);
            Card spellCard = inventory.getCardById(req.getSpellId());
            send(new ResponseRequestDropSpell(BattleHandler.BattleError.SUCCESS.getValue(),
                    req.getSpellId(), spellCard.getLevel(), req.getPixelPos()), user);
            int opponentId = room.getOpponentPlayerByMyPlayerId(user.getId()).getId();
            User opponent = BitZeroServer.getInstance().getUserManager().getUserById(opponentId);
            send(new ResponseOpponentDropSpell(BattleHandler.BattleError.SUCCESS.getValue(),
                    req.getSpellId(), spellCard.getLevel(), req.getPixelPos()), opponent);
        } catch (Exception e) {
            logger.info("BattleMap processDropSpell exception");
        }
    }

    public enum BattleError {
        SUCCESS((short) 0),
        ERROR((short) 1),
        USER_INFO_NULL((short) 2);


        private final short value;

        private BattleError(short value) {
            this.value = value;
        }

        public short getValue() {
            return this.value;
        }
    }
}
