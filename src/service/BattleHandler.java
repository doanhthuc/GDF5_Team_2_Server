package service;

import battle.BattleMap;
import bitzero.server.core.BZEventType;
import bitzero.server.core.IBZEvent;
import bitzero.server.entities.User;
import bitzero.server.extensions.BaseClientRequestHandler;
import bitzero.server.extensions.data.DataCmd;
import cmd.CmdDefine;
import cmd.receive.battle.tower.RequestPutTower;
import cmd.send.battle.ResponseRequestGetBattleMap;
import cmd.send.battle.ResponseRequestPutTower;
import event.eventType.DemoEventType;
import extension.FresherExtension;
import model.PlayerInfo;
import model.battle.RoomManager;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.server.ServerConstant;

public class BattleHandler extends BaseClientRequestHandler {
    public static short BATTLE_MULTI_IDS = 5000;
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
                    processGetBattleMap(user);
                    break;
                case CmdDefine.PUT_TOWER:
                    System.out.println("[BattleHandler.java line 55] cmd Put tower: " + CmdDefine.PUT_TOWER);
                    RequestPutTower req = new RequestPutTower(dataCmd);
                    processPutTower(user, req);

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
                send( new ResponseRequestGetBattleMap(BattleError.USER_INFO_NULL.getValue()), user);
                logger.info("PlayerInfo null");
                return;
            }
            BattleMap btm= new BattleMap();
            btm.show();
            send(new ResponseRequestGetBattleMap(BattleHandler.BattleError.SUCCESS.getValue(), btm), user);
        } catch (Exception e) {
            logger.info("processGetName exception");
        }
    }

    private void processPutTower(User user, RequestPutTower req) {
        System.out.println("BattleMap " + " processPutTower");
        try {
            BattleMap battleMap = RoomManager.getInstance().getRoom(req.getRoomId()).getBattle().getBattleMapByPlayerId(user.getId());
            System.out.println("[BattleHandler.java processPutTower] battleMap " + battleMap.mapH);
            System.out.println("[BattleHandler.java line 90 processPutTower]  roomID: " + req.getRoomId());
            System.out.println("[BattleHandler.java line 90 processPutTower]  TowerID: " + req.getTowerId());
            System.out.println("[BattleHandler.java line 90 processPutTower]  x: " + req.getTilePos().x);
            System.out.println("[BattleHandler.java line 90 processPutTower]  y: " + req.getTilePos().y);
            System.out.println("[BattleHandler.java line 90 processPutTower]  x: " + req.getPixelPos().x);
            System.out.println("[BattleHandler.java line 90 processPutTower]  y: " + req.getPixelPos().y);
            send(new ResponseRequestPutTower(BattleHandler.BattleError.SUCCESS.getValue(), req.getTowerId(), req.getTilePos(), req.getPixelPos()), user);
        } catch (Exception e) {
            logger.info("processGetName exception");
        }
    }

    public enum BattleError {
        SUCCESS((short) 0),
        ERROR((short) 1),
        USER_INFO_NULL((short)2);


        private final short value;

        private BattleError(short value) {
            this.value = value;
        }

        public short getValue() {
            return this.value;
        }
    }
}
