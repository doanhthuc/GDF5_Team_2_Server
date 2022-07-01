package service;

import battle.BattleMap;
import bitzero.server.BitZeroServer;
import bitzero.server.core.BZEventParam;
import bitzero.server.core.BZEventType;
import bitzero.server.core.IBZEvent;
import bitzero.server.entities.User;
import bitzero.server.extensions.BaseClientRequestHandler;
import bitzero.server.extensions.data.DataCmd;
import cmd.CmdDefine;
import cmd.receive.cheat.RequestCheatUserCard;
import cmd.receive.cheat.RequestCheatUserInfo;
import cmd.receive.cheat.RequestCheatUserLobbyChest;
import cmd.receive.user.RequestAddGem;
import cmd.receive.user.RequestAddGold;
import cmd.send.battle.ResponseRequestGetBattleMap;
import cmd.send.cheat.ResponseRequestCheatUserCard;
import cmd.send.cheat.ResponseRequestCheatUserInfo;
import cmd.send.cheat.ResponseRequestCheatUserLobbyChest;
import cmd.send.user.ResponseAddGem;
import cmd.send.user.ResponseAddGold;
import cmd.send.user.ResponseRequestUserInfo;
import event.eventType.DemoEventParam;
import event.eventType.DemoEventType;
import extension.FresherExtension;
import model.Inventory.Card;
import model.Inventory.CardCollection;
import model.Lobby.LobbyChest;
import model.Lobby.LobbyChestContainer;
import model.Lobby.LobbyChestDefine;
import model.PlayerInfo;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.server.ServerConstant;

import java.util.List;

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
            switch (dataCmd.getId()) {
                case CmdDefine.GET_BATTLE_MAP:
                    processGetBattleMap(user);
                    break;
            }
        } catch (Exception e) {
            logger.warn("BATTLEHANDLER EXCEPTION " + e.getMessage());
            logger.warn(ExceptionUtils.getStackTrace(e));
        }

    }

    private void processGetBattleMap(User user) {
        System.out.println("CheatHandler " + " processCheatUserInfo");
        try {
            PlayerInfo userInfo = (PlayerInfo) user.getProperty(ServerConstant.PLAYER_INFO);
            if (userInfo == null) {
                send( new ResponseRequestGetBattleMap(BattleError.USER_INFO_NULL.getValue()), user);
                logger.info("PlayerInfo null");
                return;
            }
            BattleMap btm= new BattleMap();
            send(new ResponseRequestGetBattleMap(BattleHandler.BattleError.SUCCESS.getValue(), btm), user);
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
