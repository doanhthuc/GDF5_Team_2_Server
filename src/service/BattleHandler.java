package service;

import battle.BattleMap;
import battle.common.EntityMode;
import battle.newMap.BattleMapObject;
import battle.newMap.TileObject;
import battle.newMap.Tower;
import bitzero.server.BitZeroServer;
import bitzero.server.core.BZEventType;
import bitzero.server.core.IBZEvent;
import bitzero.server.entities.User;
import bitzero.server.extensions.BaseClientRequestHandler;
import bitzero.server.extensions.data.BaseMsg;
import bitzero.server.extensions.data.DataCmd;
import cmd.CmdDefine;
import cmd.HandlerId;
import cmd.receive.battle.spell.RequestDropSpell;
import cmd.receive.battle.tower.RequestChangeTowerStrategy;
import cmd.receive.battle.tower.RequestDestroyTower;
import cmd.receive.battle.tower.RequestPutTower;
import cmd.receive.battle.tower.RequestUpgradeTower;
import cmd.receive.battle.trap.RequestPutTrap;
import cmd.send.battle.opponent.*;
import cmd.send.battle.player.*;
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

import javax.xml.crypto.Data;

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
//                    System.out.println("[BattleHandler.java line 54] GET_BATTLE_MAP cmdId: " + dataCmd.getId());
//                    processGetBattleMap(user);
                    break;
                case CmdDefine.PUT_TOWER: {
                    RequestPutTower requestPutTower = new RequestPutTower(dataCmd);
                    Room room = RoomManager.getInstance().getRoom(requestPutTower.getRoomId());
                    room.addInput(user, dataCmd);
                    break;
                }
                case CmdDefine.UPGRADE_TOWER: {
                    RequestUpgradeTower requestUpgradeTower = new RequestUpgradeTower(dataCmd);
                    Room room = RoomManager.getInstance().getRoom(requestUpgradeTower.getRoomId());
                    room.addInput(user, dataCmd);
                    break;
                }
                case CmdDefine.DROP_SPELL: {
                    RequestDropSpell requestDropSpell = new RequestDropSpell(dataCmd);
                    Room room = RoomManager.getInstance().getRoom(requestDropSpell.getRoomId());
                    room.addInput(user, dataCmd);
                    break;
                }
                case CmdDefine.CHANGE_TOWER_STRATEGY: {
                    RequestChangeTowerStrategy requestChangeTowerStrategy = new RequestChangeTowerStrategy(dataCmd);
                    Room room = RoomManager.getInstance().getRoom(requestChangeTowerStrategy.getRoomId());
                    room.addInput(user, dataCmd);
                    break;
                }
                case CmdDefine.PUT_TRAP: {
                    RequestPutTrap requestPutTrap = new RequestPutTrap(dataCmd);
                    Room room = RoomManager.getInstance().getRoom(requestPutTrap.getRoomId());
                    room.addInput(user, dataCmd);
                    break;
                }
                case CmdDefine.DESTROY_TOWER: {
                    RequestDestroyTower requestDestroyTower = new RequestDestroyTower(dataCmd);
                    Room room = RoomManager.getInstance().getRoom(requestDestroyTower.getRoomId());
                    room.addInput(user, dataCmd);
                    break;
                }
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
