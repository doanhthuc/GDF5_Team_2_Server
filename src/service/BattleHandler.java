package service;

import battle.map.BattleMap;
import bitzero.server.core.BZEventType;
import bitzero.server.core.IBZEvent;
import bitzero.server.entities.User;
import bitzero.server.extensions.BaseClientRequestHandler;
import bitzero.server.extensions.data.DataCmd;
import cmd.CmdDefine;
import cmd.HandlerId;
import cmd.receive.battle.spell.RequestDropSpell;
import cmd.receive.battle.tower.*;
import cmd.receive.battle.RequestSendCheckSum;
import cmd.receive.battle.tower.RequestSpeedUpNextWave;
import cmd.receive.battle.trap.RequestPutTrap;
import cmd.send.battle.player.*;
import event.eventType.DemoEventType;
import extension.FresherExtension;
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
            PlayerInfo playerInfo = (PlayerInfo) user.getProperty(ServerConstant.PLAYER_INFO);
            switch (dataCmd.getId()) {
                case CmdDefine.GET_BATTLE_MAP:
//                    System.out.println("[BattleHandler.java line 54] GET_BATTLE_MAP cmdId: " + dataCmd.getId());
//                    processGetBattleMap(user);
                    break;
                case CmdDefine.PUT_TOWER: {
                    RequestPutTower requestPutTower = new RequestPutTower(dataCmd);
                    Room room = RoomManager.getInstance().getRoom(requestPutTower.getRoomId());
                    room.addInput(playerInfo, dataCmd);
                    break;
                }
                case CmdDefine.UPGRADE_TOWER: {
                    RequestUpgradeTower requestUpgradeTower = new RequestUpgradeTower(dataCmd);
                    Room room = RoomManager.getInstance().getRoom(requestUpgradeTower.getRoomId());
                    room.addInput(playerInfo, dataCmd);
                    break;
                }
                case CmdDefine.DROP_SPELL: {
                    RequestDropSpell requestDropSpell = new RequestDropSpell(dataCmd);
                    Room room = RoomManager.getInstance().getRoom(requestDropSpell.getRoomId());
                    room.addInput(playerInfo, dataCmd);
                    break;
                }
                case CmdDefine.CHANGE_TOWER_STRATEGY: {
                    RequestChangeTowerStrategy requestChangeTowerStrategy = new RequestChangeTowerStrategy(dataCmd);
                    Room room = RoomManager.getInstance().getRoom(requestChangeTowerStrategy.getRoomId());
                    room.addInput(playerInfo, dataCmd);
                    break;
                }
                case CmdDefine.PUT_TRAP: {
                    RequestPutTrap requestPutTrap = new RequestPutTrap(dataCmd);
                    Room room = RoomManager.getInstance().getRoom(requestPutTrap.getRoomId());
                    room.addInput(playerInfo, dataCmd);
                    break;
                }
                case CmdDefine.DESTROY_TOWER: {
                    RequestDestroyTower requestDestroyTower = new RequestDestroyTower(dataCmd);
                    Room room = RoomManager.getInstance().getRoom(requestDestroyTower.getRoomId());
                    room.addInput(playerInfo, dataCmd);
                    break;
                }
                case CmdDefine.SEND_CHECK_SUM: {
                    RequestSendCheckSum requestSendCheckSum = new RequestSendCheckSum(dataCmd);
                    Room room = RoomManager.getInstance().getRoom(requestSendCheckSum.getRoomId());
                    room.checkClientSumHp(requestSendCheckSum.getSumHpInTick(),user);
                    break;
                }
                case CmdDefine.SPEEDUP_NEXT_WAVE: {
                    RequestSpeedUpNextWave requestSpeedUpNextWave = new RequestSpeedUpNextWave(dataCmd);
                    Room room = RoomManager.getInstance().getRoom(requestSpeedUpNextWave.getRoomId());
                    room.speedUpNextWave();
                    break;
                }
                default:
                    break;

            }
        } catch (Exception e) {
            System.out.println(ExceptionUtils.getStackTrace(e));
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
            System.out.println(ExceptionUtils.getStackTrace(e));
        }
    }


    public enum BattleError {
        SUCCESS((short) 0),
        ERROR((short) 1),
        TOWER_NULL((short) 3),
        USER_INFO_NULL((short) 2),
        TOWER_ID_NOT_MATCH((short) 4);


        private final short value;

        private BattleError(short value) {
            this.value = value;
        }

        public short getValue() {
            return this.value;
        }
    }
}
