package service;

import bitzero.server.core.BZEventType;
import bitzero.server.core.IBZEvent;
import bitzero.server.entities.User;
import bitzero.server.extensions.BaseClientRequestHandler;
import bitzero.server.extensions.data.DataCmd;
import bitzero.util.socialcontroller.bean.UserInfo;
import cmd.CmdDefine;
import cmd.HandlerId;
import cmd.receive.lobby.RequestLobbyChest;
import cmd.send.lobby.ResponseRequestClaimLobbyChest;
import cmd.send.lobby.ResponseRequestGetUserLobbyChest;
import cmd.send.lobby.ResponseRequestSpeedUpLobbyChest;
import cmd.send.lobby.ResponseRequestUnlockLobbyChest;
import event.eventType.DemoEventType;
import extension.FresherExtension;
import model.Inventory.Inventory;
import model.Common.Item;
import model.Common.ItemDefine;
import model.Lobby.LobbyChest;
import model.Lobby.UserLobbyChest;
import model.Lobby.LobbyChestDefine;
import model.Lobby.LobbyDTO;
import model.PlayerInfo;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.server.ServerConstant;

import java.util.ArrayList;

public class LobbyHandler extends BaseClientRequestHandler {
    public static short HANDLER_ID = HandlerId.LOBBY.getValue();
    private final Logger logger = LoggerFactory.getLogger("UserHandler");

    public LobbyHandler() {
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
                case CmdDefine.GET_USER_LOBBY_CHEST:
                    processGetUserLobbyChest(user);
                    break;
                case CmdDefine.UNLOCK_LOBBY_CHEST:
                    RequestLobbyChest rq = new RequestLobbyChest(dataCmd);
                    processUnlockLobbyChest(rq, user);
                    break;
                case CmdDefine.SPEEDUP_LOBBY_CHEST:
                    RequestLobbyChest rqSpeedUp = new RequestLobbyChest(dataCmd);
                    processSpeedUpLobbyChest(rqSpeedUp, user);
                    break;
                case CmdDefine.CLAIM_LOBBY_CHEST:
                    RequestLobbyChest rqClaim = new RequestLobbyChest(dataCmd);
                    processClaimLobbyChest(rqClaim, user);
                    break;
            }
        } catch (Exception e) {
            logger.warn("USERHANDLER EXCEPTION " + e.getMessage());
            logger.warn(ExceptionUtils.getStackTrace(e));
        }

    }

    private void processGetUserLobbyChest(User user) {
        System.out.println("Lobby Handler processGetUserLobbyChest");
        try {
            UserLobbyChest userLobbyChest = (UserLobbyChest) user.getProperty(ServerConstant.LOBBY_CHEST);
            if (userLobbyChest == null) {
                logger.info("PlayerInfo null");
                send(new ResponseRequestGetUserLobbyChest(LobbyError.USERINFO_NULL.getValue()), user);
                return;
            }
            synchronized (userLobbyChest) {
                send(new ResponseRequestGetUserLobbyChest(LobbyHandler.LobbyError.SUCCESS.getValue(), userLobbyChest), user);
            }
        } catch (Exception e) {
            logger.info("processGetUserLobbyChest exception");
        }
    }

    private void processUnlockLobbyChest(RequestLobbyChest rq, User user) {
        try {
            System.out.println("Inventory Handle ProcessUnlockLobbyChest");
            UserLobbyChest userLobbyChest = (UserLobbyChest) user.getProperty(ServerConstant.LOBBY_CHEST);
            if (userLobbyChest == null) {
                logger.info("PlayerInfo null");
                send(new ResponseRequestUnlockLobbyChest(LobbyError.USERINFO_NULL.getValue()), user);
                return;
            }
            synchronized (userLobbyChest) {
                int lobbyChestID = rq.getLobbyChestId();
                //verify Chest
                if (userLobbyChest.lobbyChestContainer.get(lobbyChestID) == null) {
                    send(new ResponseRequestUnlockLobbyChest(LobbyError.CHEST_NULL_ERROR.getValue()), user);
                    return;
                }
                //verify State
                if (userLobbyChest.lobbyChestContainer.get(lobbyChestID).getState() != LobbyChestDefine.NOT_OPENING_STATE || userLobbyChest.haveOpeningSlot()) {
                    send(new ResponseRequestUnlockLobbyChest(LobbyError.CHEST_STATE_ERROR.getValue()), user);
                    return;
                }
                userLobbyChest.lobbyChestContainer.get(lobbyChestID).unlock();
                userLobbyChest.saveModel(user.getId());
                send(new ResponseRequestUnlockLobbyChest(LobbyError.SUCCESS.getValue(), new LobbyDTO(lobbyChestID, LobbyChestDefine.OPENING_STATE, userLobbyChest.lobbyChestContainer.get(lobbyChestID).getClaimTime())), user);
            }
        } catch (Exception e) {
            logger.info("processUnlockLobbyChest exception");
        }
    }

    private void processSpeedUpLobbyChest(RequestLobbyChest rq, User user) {
        try {
            System.out.println("Lobby Handle ProcessSpeedUpLobbyChest");
            PlayerInfo userInfo = (PlayerInfo) user.getProperty(ServerConstant.PLAYER_INFO);
            UserLobbyChest userLobbyChest = (UserLobbyChest) user.getProperty(ServerConstant.LOBBY_CHEST);
            Inventory userInventory = (Inventory) user.getProperty(ServerConstant.INVENTORY);
            //verify user
            if (userInfo == null || userLobbyChest == null) {
                logger.info("PlayerInfo null");
                send(new ResponseRequestSpeedUpLobbyChest(LobbyError.USERINFO_NULL.getValue()), user);
                return;
            }
            synchronized (userLobbyChest) {
                userLobbyChest.update();
                int lobbyChestId = rq.getLobbyChestId();
                LobbyChest lobbyChestToSpeedup = userLobbyChest.lobbyChestContainer.get(lobbyChestId);
                //verify Chest
                if (lobbyChestToSpeedup == null) {
                    send(new ResponseRequestUnlockLobbyChest(LobbyError.CHEST_NULL_ERROR.getValue()), user);
                    return;
                }
                //verify State
                if ((lobbyChestToSpeedup.getState() != LobbyChestDefine.OPENING_STATE)
                        && !(userLobbyChest.checkSpeedUpChest() && lobbyChestToSpeedup.getState() == LobbyChestDefine.NOT_OPENING_STATE)) {
                    send(new ResponseRequestUnlockLobbyChest(LobbyError.CHEST_STATE_ERROR.getValue()), user);
                    return;
                }
                //If LobbyChest.State== NOT OPENING STATE
                if (lobbyChestToSpeedup.getState() == LobbyChestDefine.NOT_OPENING_STATE)
                    lobbyChestToSpeedup.unlock();

                long remainingTime = lobbyChestToSpeedup.getRemainingTime();
                int gemRequire = (int) Math.ceil(remainingTime * 1.0 / LobbyChestDefine.MILLISECOND_PER_GEM);
                //verify Gem
                if ((verifyPurchase(userInfo.getGem(), gemRequire))) {
                    userInfo.addGem(-gemRequire);
                    userLobbyChest.lobbyChestContainer.get(lobbyChestId).setEmpty();
                    userLobbyChest.saveModel(userInfo.getId());

                    lobbyChestToSpeedup.randomRewardItem();
                    synchronized (userInventory) {
                        updateInventory(lobbyChestToSpeedup.getChestReward(), userInfo, userInventory);
                    }
                    send(new ResponseRequestSpeedUpLobbyChest(LobbyError.SUCCESS.getValue(),
                            new LobbyDTO(lobbyChestId, LobbyChestDefine.EMPTY_STATE, lobbyChestToSpeedup.getChestReward(), -gemRequire)), user);
                } else {
                    send(new ResponseRequestSpeedUpLobbyChest(LobbyError.NOT_ENOUGH_GEM.getValue()), user);
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void processClaimLobbyChest(RequestLobbyChest rq, User user) {
        try {
            System.out.println("Lobby Handle ProcessClaimLobbyChest");

            UserLobbyChest userLobbyChest = (UserLobbyChest) user.getProperty(ServerConstant.LOBBY_CHEST);
            Inventory userInventory = (Inventory) user.getProperty(ServerConstant.INVENTORY);
            PlayerInfo userInfo = (PlayerInfo) user.getProperty(ServerConstant.PLAYER_INFO);
            if (userLobbyChest == null) {
                logger.info("PlayerInfo null");
                send(new ResponseRequestClaimLobbyChest(LobbyError.USERINFO_NULL.getValue()), user);
                return;
            }
            synchronized (userLobbyChest) {
                userLobbyChest.update();
                int lobbyChestId = rq.getLobbyChestId();
                LobbyChest lobbyChestToClaim = userLobbyChest.lobbyChestContainer.get(lobbyChestId);
                //verify Chest
                if (lobbyChestToClaim == null) {
                    send(new ResponseRequestUnlockLobbyChest(LobbyError.CHEST_NULL_ERROR.getValue()), user);
                    return;
                }
                //verify State
                if (lobbyChestToClaim.getState() != LobbyChestDefine.CLAIMABLE_STATE) {
                    send(new ResponseRequestUnlockLobbyChest(LobbyError.CHEST_STATE_ERROR.getValue()), user);
                    return;
                }
                int gemRequire = 0;
                userLobbyChest.lobbyChestContainer.get(lobbyChestId).setEmpty();
                lobbyChestToClaim.randomRewardItem();
                userLobbyChest.saveModel(user.getId());
                synchronized (userInventory) {
                    updateInventory(lobbyChestToClaim.getChestReward(), userInfo, userInventory);
                }
                send(new ResponseRequestClaimLobbyChest(LobbyError.SUCCESS.getValue(),
                        new LobbyDTO(lobbyChestId, LobbyChestDefine.EMPTY_STATE, lobbyChestToClaim.getChestReward(), gemRequire)), user);
            }
        } catch (Exception exception) {

        }
    }

    private boolean verifyPurchase(int userResource, int itemPrice) {
        return userResource >= itemPrice;
    }

    private void updateInventory(ArrayList<Item> reward, PlayerInfo userInfo, Inventory userInventory) throws Exception {

        for (int i = 0; i < reward.size(); i++) {
            Item item = reward.get(i);
            if (item.getItemType() == ItemDefine.GOLDTYPE) userInfo.addGold(item.getQuantity());
            else userInventory.updateCard(item.getItemType(), item.getQuantity());
        }
        userInfo.saveModel(userInfo.getId());
        userInventory.saveModel(userInfo.getId());
    }

    public enum LobbyError {
        SUCCESS((short) 0),
        USERINFO_NULL((short) 1),
        CHEST_NULL_ERROR((short) 2),
        CHEST_STATE_ERROR((short) 3),
        NOT_ENOUGH_GEM((short) 4);
        private final short value;

        private LobbyError(short value) {
            this.value = value;
        }

        public short getValue() {
            return this.value;
        }
    }
}
