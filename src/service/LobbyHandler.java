package service;

import bitzero.server.BitZeroServer;
import bitzero.server.core.BZEventParam;
import bitzero.server.core.BZEventType;
import bitzero.server.core.IBZEvent;
import bitzero.server.entities.User;
import bitzero.server.extensions.BaseClientRequestHandler;
import bitzero.server.extensions.data.DataCmd;
import cmd.CmdDefine;
import cmd.receive.lobby.RequestLobbyChest;
import cmd.receive.shop.RequestBuyDailyShop;
import cmd.receive.shop.RequestBuyGoldShop;
import cmd.send.inventory.ResponseRequestGetUserInventory;
import cmd.send.inventory.ResponseRequestUpgradeCard;
import cmd.send.lobby.ResponseRequestClaimLobbyChest;
import cmd.send.lobby.ResponseRequestGetUserLobbyChest;
import cmd.send.lobby.ResponseRequestSpeedUpLobbyChest;
import cmd.send.lobby.ResponseRequestUnlockLobbyChest;
import cmd.send.shop.ResponseRequestBuyDailyShop;
import cmd.send.shop.ResponseRequestBuyGoldShop;
import cmd.send.user.*;

import event.eventType.DemoEventParam;
import event.eventType.DemoEventType;
import extension.FresherExtension;
import model.Chest.ChestDefine;
import model.Inventory.Card;
import model.Inventory.CardCollection;
import model.Inventory.InventoryDTO;
import model.Item.Item;
import model.Item.ItemDefine;
import model.Lobby.LobbyChest;
import model.Lobby.LobbyChestContainer;
import model.Lobby.LobbyChestDefine;
import model.Lobby.LobbyDTO;
import model.PlayerInfo;
import model.Shop.ItemList.DailyItemList;
import model.Shop.ItemList.ShopItemList;
import model.Shop.ShopDTO;
import model.Shop.ShopItem;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.server.ServerConstant;

import java.util.ArrayList;
import java.util.List;

public class LobbyHandler extends BaseClientRequestHandler {
    public static short LOBBY_MULTI_IDS = 4000;
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
                    RequestLobbyChest rqspeedup = new RequestLobbyChest(dataCmd);
                    processSpeedUpLobbyChest(rqspeedup, user);
                    break;
                case CmdDefine.CLAIM_LOBBY_CHEST:
                    RequestLobbyChest rqclaim = new RequestLobbyChest(dataCmd);
                    processClaimLobbyChest(rqclaim, user);
                    break;
            }
        } catch (Exception e) {
            logger.warn("USERHANDLER EXCEPTION " + e.getMessage());
            logger.warn(ExceptionUtils.getStackTrace(e));
        }

    }

    private void processGetUserLobbyChest(User user) {
        System.out.println("InventoryHandler  processGetUserInventory");
        try {
            PlayerInfo userInfo = (PlayerInfo) user.getProperty(ServerConstant.PLAYER_INFO);
            if (userInfo == null) {
                logger.info("PlayerInfo null");
                //send(new ResponseGetName(DemoHandler.DemoError.PLAYERINFO_NULL.getValue(), ""), user);
            }
            logger.info("get inventoryID " + userInfo.getId());
            LobbyChestContainer userLobbyChest = (LobbyChestContainer) LobbyChestContainer.getModel(userInfo.getId(), LobbyChestContainer.class);
            userLobbyChest.show();
            send(new ResponseRequestGetUserLobbyChest(LobbyHandler.LobbyError.SUCCESS.getValue(), userLobbyChest), user);
        } catch (Exception e) {
            logger.info("processGetName exception");
            //send(new ResponseGetName(DemoHandler.DemoError.EXCEPTION.getValue(), ""), user);
        }
    }

    private void processUnlockLobbyChest(RequestLobbyChest rq, User user) {
        try {
            PlayerInfo userInfo = (PlayerInfo) user.getProperty(ServerConstant.PLAYER_INFO);
            if (userInfo == null) {
                logger.info("PlayerInfo null");
                //send(new ResponseGetName(UserHandler.UserError.PLAYERINFO_NULL.getValue(), ""), user);
            }
            System.out.println("Inventory Handle ProcessUpgradeCard");
            LobbyChestContainer userLobbyChest = (LobbyChestContainer) LobbyChestContainer.getModel(userInfo.getId(), LobbyChestContainer.class);
            int lobbychestId = rq.getLobbyChestId();
            if (userLobbyChest.lobbyChestContainer.get(lobbychestId).getState() == LobbyChestDefine.NOT_OPENING_STATE) {
                userLobbyChest.lobbyChestContainer.get(lobbychestId).unlock();
                userLobbyChest.saveModel(userInfo.getId());
                send(new ResponseRequestUnlockLobbyChest(LobbyError.SUCCESS.getValue(),
                        new LobbyDTO(lobbychestId, LobbyChestDefine.OPENING_STATE, userLobbyChest.lobbyChestContainer.get(lobbychestId).getClaimTime())), user);
            } else {
                send(new ResponseRequestUnlockLobbyChest(LobbyError.ERROR.getValue(),
                        new LobbyDTO(-1, -1, -1)), user);
            }
        } catch (Exception e) {
            logger.info("processGetName exception");
        }
    }

    private void processSpeedUpLobbyChest(RequestLobbyChest rq, User user) {
        try {
            PlayerInfo userInfo = (PlayerInfo) user.getProperty(ServerConstant.PLAYER_INFO);
            if (userInfo == null) {
                logger.info("PlayerInfo null");
                //send(new ResponseGetName(UserHandler.UserError.PLAYERINFO_NULL.getValue(), ""), user);
            }
            System.out.println("Lobby Handle ProcessSpeedUpLobbyChest");
            LobbyChestContainer userLobbyChest = (LobbyChestContainer) LobbyChestContainer.getModel(userInfo.getId(), LobbyChestContainer.class);
            userLobbyChest.update();
            int lobbychestId = rq.getLobbyChestId();
            LobbyChest lobbyChest_to_speedup = userLobbyChest.lobbyChestContainer.get(lobbychestId);
            if (lobbyChest_to_speedup.getState() == LobbyChestDefine.OPENING_STATE) {
               long remainingTime = lobbyChest_to_speedup.getRemainingTime();
                int gemRequire = (int) remainingTime / LobbyChestDefine.MILLISECOND_PER_GEM;
                if ((verifyPurchase(userInfo.getGem(), gemRequire)) == true) {
                    userInfo.addGem(-gemRequire);
                    userLobbyChest.lobbyChestContainer.get(lobbychestId).setState(LobbyChestDefine.EMPTY_STATE);
                    userLobbyChest.saveModel(userInfo.getId());
                    updateInventory(lobbyChest_to_speedup.getChestReward(),userInfo);
                    send(new ResponseRequestSpeedUpLobbyChest(LobbyError.SUCCESS.getValue(),
                            new LobbyDTO(lobbychestId, LobbyChestDefine.EMPTY_STATE, lobbyChest_to_speedup.getChestReward(), -gemRequire)), user);
                }
                else {
                    send(new ResponseRequestSpeedUpLobbyChest(LobbyError.ERROR.getValue(),
                            new LobbyDTO(-1,-1,-1)), user);
                }
            } else {
                send(new ResponseRequestUnlockLobbyChest(LobbyError.ERROR.getValue(),
                        new LobbyDTO(-1, -1, -1)), user);
            }
        } catch (Exception e) {
            logger.info("processGetName exception");
        }
    }
    private void processClaimLobbyChest(RequestLobbyChest rq, User user) {
        try {
            PlayerInfo userInfo = (PlayerInfo) user.getProperty(ServerConstant.PLAYER_INFO);
            if (userInfo == null) {
                logger.info("PlayerInfo null");
                //send(new ResponseGetName(UserHandler.UserError.PLAYERINFO_NULL.getValue(), ""), user);
            }
            System.out.println("Lobby Handle ProcessClaimLobbyChest");
            LobbyChestContainer userLobbyChest = (LobbyChestContainer) LobbyChestContainer.getModel(userInfo.getId(), LobbyChestContainer.class);
            userLobbyChest.update();
            int lobbychestId = rq.getLobbyChestId();
            LobbyChest lobbyChest_to_claim=userLobbyChest.lobbyChestContainer.get(lobbychestId);
            if (lobbyChest_to_claim.getState() == LobbyChestDefine.CLAIMABLE_STATE) {
                int gemRequire = 0;
                userLobbyChest.lobbyChestContainer.get(lobbychestId).setState(LobbyChestDefine.EMPTY_STATE);
                updateInventory(lobbyChest_to_claim.getChestReward(),userInfo);
                send(new ResponseRequestClaimLobbyChest(LobbyError.SUCCESS.getValue(),
                        new LobbyDTO(lobbychestId, LobbyChestDefine.EMPTY_STATE, lobbyChest_to_claim.getChestReward(), gemRequire)), user);
                }
                else {
                    send(new ResponseRequestClaimLobbyChest(LobbyError.ERROR.getValue(),
                            new LobbyDTO(-1,-1,-1)), user);
                }
            } catch (Exception exception) {

        }
    }

    private boolean verifyPurchase(int userGold, int requireGold) {
        return userGold >= requireGold;
    }

    private void userDisconnect(User user) {
    }

    private void userChangeName(User user, String name) {
        List<User> allUser = BitZeroServer.getInstance().getUserManager().getAllUsers();
        for (User aUser : allUser) {
        }
    }
    private void updateInventory(ArrayList<Item> reward,PlayerInfo userInfo) throws Exception {
        CardCollection userCardCollection = (CardCollection) CardCollection.getModel(userInfo.getId(), CardCollection.class);
        for (int i = 0; i < reward.size(); i++) {
            Item item = reward.get(i);
            if (item.getItemType() == ItemDefine.GOLDTYPE) userInfo.addGold(item.getQuantity());
            else userCardCollection.updateCard(item.getItemType(), item.getQuantity());
        }
        userInfo.saveModel(userInfo.getId());
        userCardCollection.saveModel(userInfo.getId());
    }
    public enum LobbyError {
        SUCCESS((short) 0),
        ERROR((short) 1),
        PLAYERINFO_NULL((short) 2),
        EXCEPTION((short) 3),
        INVALID_PARAM((short) 4),
        VISITED((short) 5),
        ;
        private final short value;

        private LobbyError(short value) {
            this.value = value;
        }

        public short getValue() {
            return this.value;
        }
    }
}
