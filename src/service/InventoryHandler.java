package service;

import bitzero.server.core.BZEventType;
import bitzero.server.core.IBZEvent;
import bitzero.server.entities.User;
import bitzero.server.extensions.BaseClientRequestHandler;
import bitzero.server.extensions.data.DataCmd;
import bitzero.util.socialcontroller.bean.UserInfo;
import cmd.CmdDefine;
import cmd.HandlerId;
import cmd.receive.inventory.RequestSwapCard;
import cmd.receive.inventory.RequestUpgradeCard;
import cmd.send.inventory.ResponseRequestGetUserInventory;
import cmd.send.inventory.ResponseRequestSwapCard;
import cmd.send.inventory.ResponseRequestUpgradeCard;
import cmd.send.user.ResponseRequestUserInfo;
import event.eventType.DemoEventType;
import extension.FresherExtension;
import model.Inventory.Inventory;
import model.Inventory.InventoryDTO;
import model.PlayerInfo;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.server.ServerConstant;

public class InventoryHandler extends BaseClientRequestHandler {
    public static short HANDLER_ID = HandlerId.INVENTORY.getValue();
    private final Logger logger = LoggerFactory.getLogger("UserHandler");

    public InventoryHandler() {
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
                case CmdDefine.GET_USER_INVENTORY:
                    processGetUserInventory(user);
                    break;
                case CmdDefine.UPGRADE_CARD:
                    RequestUpgradeCard rq = new RequestUpgradeCard(dataCmd);
                    processUpgradeCard(rq, user);
                    break;
                case CmdDefine.SWAP_CARD:
                    RequestSwapCard requestSwapCard = new RequestSwapCard(dataCmd);
                    processSwapCard(requestSwapCard, user);
                    break;
            }
        } catch (Exception e) {
            logger.warn("USERHANDLER EXCEPTION " + e.getMessage());
            logger.warn(ExceptionUtils.getStackTrace(e));
        }

    }

    private void processGetUserInventory(User user) {
        System.out.println("InventoryHandler processGetUserInventory");
        try {
            Inventory userInventory = (Inventory) user.getProperty(ServerConstant.INVENTORY);
            if (userInventory == null) {
                logger.info("PlayerInfo null");
                ////send(new ResponseGetName(DemoHandler.DemoError.PLAYERINFO_NULL.getValue(), ""), user);
            }
            synchronized (userInventory) {
                send(new ResponseRequestGetUserInventory(InventoryHandler.InventoryError.SUCCESS.getValue(), userInventory), user);
            }
        } catch (Exception e) {
            logger.info("processGetName exception");
            //send(new ResponseGetName(DemoHandler.DemoError.EXCEPTION.getValue(), ""), user);
        }
    }

    private void processUpgradeCard(RequestUpgradeCard rq, User user) {
        try {
            System.out.println("Inventory Handle ProcessUpgradeCard");
            Inventory userInventory = (Inventory) user.getProperty(ServerConstant.INVENTORY);
            PlayerInfo userInfo = (PlayerInfo) user.getProperty(ServerConstant.PLAYER_INFO);
            if (userInventory == null) {
                logger.info("PlayerInfo null");
                ////send(new ResponseGetName(DemoHandler.DemoError.PLAYERINFO_NULL.getValue(), ""), user);
            }
            synchronized (userInventory) {
                synchronized (userInfo) {
                    int cardType = rq.getcardType();
                    int requireGold = userInventory.getGoldToUpgradeCard(cardType);
                    int requireCard = userInventory.getfragmentToUpgradeCard(cardType);
                    if ((verifyPurchase(userInfo.getGold(), requireGold))
                            && (userInventory.checkFragmentToUpgradeCard(cardType))) {
                        userInfo.addGold(-requireGold);
                        userInventory.upgradeCard(cardType);
                        send(new ResponseRequestUpgradeCard(InventoryError.SUCCESS.getValue(), new InventoryDTO(-requireGold, cardType, -requireCard)), user);
                        userInfo.saveModel(userInfo.getId());
                        userInventory.saveModel(userInfo.getId());
                    } else {
                        System.out.println("not enough gold");
                        send(new ResponseRequestUpgradeCard(InventoryError.ERROR.getValue(), new InventoryDTO(0, 0, 0)), user);
                    }
                }
            }

        } catch (Exception e) {
            logger.info("processGetName exception");
        }
    }

    private void processSwapCard(RequestSwapCard rq, User user) {
        try {
            System.out.println("Inventory Handle ProcessSwap Card");
            Inventory userInventory = (Inventory) user.getProperty(ServerConstant.INVENTORY);
            PlayerInfo userInfo = (PlayerInfo) user.getProperty(ServerConstant.PLAYER_INFO);
            if (userInventory == null) {
                logger.info("PlayerInfo null");
                ////send(new ResponseGetName(DemoHandler.DemoError.PLAYERINFO_NULL.getValue(), ""), user);
            }
            synchronized (userInventory) {
                int cardInID = rq.getCardInID();
                int cardOutID = rq.getCardOutID();
                for (int i = 0; i < userInventory.battleDeck.size(); i++)
                    System.out.print(userInventory.battleDeck.get(i));
                System.out.println("Inventory Handle ProcessSwap Card 1" + cardInID + " " + cardOutID);
                userInventory.swapBattleDeck(cardInID, cardOutID);

                send(new ResponseRequestSwapCard(InventoryError.ERROR.getValue(), cardInID, cardOutID), user);
                userInventory.saveModel(userInfo.getId());
            }
        } catch (Exception e) {
            logger.info("processGetName exception");
        }
    }


    private boolean verifyPurchase(int userGold, int requireGold) {
        return userGold >= requireGold;
    }


    public enum InventoryError {
        SUCCESS((short) 0),
        ERROR((short) 1),
        ;


        private final short value;

        private InventoryError(short value) {
            this.value = value;
        }

        public short getValue() {
            return this.value;
        }
    }
}
