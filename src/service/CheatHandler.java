package service;

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

public class CheatHandler extends BaseClientRequestHandler {
    public static short CHEAT_MULTI_IDS = 7000;
    private final Logger logger = LoggerFactory.getLogger("UserHandler");

    public CheatHandler() {
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
                case CmdDefine.CHEAT_USER_INFO:
                    RequestCheatUserInfo rq = new RequestCheatUserInfo(dataCmd);
                    processCheatUserInfo(rq, user);
                    break;
                case CmdDefine.CHEAT_USER_CARD:
                    RequestCheatUserCard rqCheatCard = new RequestCheatUserCard(dataCmd);
                    processCheatUserCard(rqCheatCard, user);
                    break;
                case CmdDefine.CHEAT_USER_LOBBY_CHEST:
                    RequestCheatUserLobbyChest rqCheatChest = new RequestCheatUserLobbyChest(dataCmd);
                    processCheatUserLobbyChest(rqCheatChest, user);
                    break;
            }
        } catch (Exception e) {
            logger.warn("CHEATHANDLER EXCEPTION " + e.getMessage());
            logger.warn(ExceptionUtils.getStackTrace(e));
        }

    }

    private void processCheatUserInfo(RequestCheatUserInfo rq, User user) {
        System.out.println("CheatHandler " + " processCheatUserInfo");
        try {
            PlayerInfo userInfo = (PlayerInfo) user.getProperty(ServerConstant.PLAYER_INFO);
            if (userInfo == null) {
                send( new ResponseRequestCheatUserInfo(CheatError.USER_INFO_NULL.getValue()), user);
                logger.info("PlayerInfo null");
                return;
            }

            userInfo.setGold(rq.getGold());
            userInfo.setGem(rq.getGem());
            userInfo.setTrophy(rq.getTrophy());

            userInfo.saveModel(userInfo.getId());
            send(new ResponseRequestCheatUserInfo(CheatError.SUCCESS.getValue(), userInfo), user);
        } catch (Exception e) {
            logger.info("processGetName exception");
        }
    }

    private void processCheatUserCard(RequestCheatUserCard rq, User user) {
        System.out.println("CheatHandler " + " processCheatUserCard");
        try {
            PlayerInfo userInfo = (PlayerInfo) user.getProperty(ServerConstant.PLAYER_INFO);
            if (userInfo == null) {
                logger.info("PlayerInfo null");
                send(new ResponseRequestCheatUserCard(CheatError.USER_INFO_NULL.getValue()), user);
                return;
            }
            CardCollection userCardCollection = (CardCollection) CardCollection.getModel(userInfo.getId(), CardCollection.class);
            userCardCollection.setCard(rq.getCardType(), rq.getCardLevel(), rq.getCardAmount());
            userCardCollection.cardCollection.get(rq.getCardType()).show();
            userCardCollection.saveModel(userInfo.getId());
            send(new ResponseRequestCheatUserCard(CheatError.SUCCESS.getValue(), new Card(rq.getCardType(), rq.getCardLevel(), rq.getCardAmount())), user);
        } catch (Exception e) {
            logger.info("processcheatUserCard exeption");
        }
    }

    private void processCheatUserLobbyChest(RequestCheatUserLobbyChest rq, User user) {
        System.out.println("CheatHandler " + " processCheatUserLobbyChest");
        try {
            PlayerInfo userInfo = (PlayerInfo) user.getProperty(ServerConstant.PLAYER_INFO);
            if (userInfo == null) {
                logger.info("PlayerInfo null");
            }
            synchronized (userInfo) {
                LobbyChestContainer userLobbyChest = (LobbyChestContainer) LobbyChestContainer.getModel(userInfo.getId(), LobbyChestContainer.class);
                LobbyChest cheatLobbyChest = new LobbyChest(LobbyChestDefine.EMPTY_STATE);
                if (rq.getChestState() == LobbyChestDefine.OPENING_STATE) {
                    cheatLobbyChest = new LobbyChest(rq.getChestState(), rq.getChestRemainingTime());
                } else {
                    cheatLobbyChest = new LobbyChest(rq.getChestState());
                }
                userLobbyChest.setLobbyChest(rq.getChestId(), cheatLobbyChest);

                userLobbyChest.saveModel(userInfo.getId());

                userLobbyChest.show();
                send(new ResponseRequestCheatUserLobbyChest(CheatError.SUCCESS.getValue(), cheatLobbyChest, rq.getChestId()), user);
            }


        } catch (Exception e) {
            logger.info("processGetName exception");
            //send(new ResponseGetName(UserHandler.UserError.EXCEPTION.getValue(), ""), user);
        }
    }


    public enum CheatError {
        SUCCESS((short) 0),
        ERROR((short) 1),
        USER_INFO_NULL((short)2),
        ;


        private final short value;

        private CheatError(short value) {
            this.value = value;
        }

        public short getValue() {
            return this.value;
        }
    }
}
