package service;

import bitzero.server.core.BZEventType;
import bitzero.server.core.IBZEvent;
import bitzero.server.entities.User;
import bitzero.server.extensions.BaseClientRequestHandler;
import bitzero.server.extensions.data.DataCmd;
import cmd.CmdDefine;
import cmd.HandlerId;
import cmd.receive.inventory.RequestUpgradeCard;
import cmd.send.inventory.ResponseRequestGetUserInventory;
import cmd.send.inventory.ResponseRequestUpgradeCard;
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
            }
        } catch (Exception e) {
            logger.warn("USERHANDLER EXCEPTION " + e.getMessage());
            logger.warn(ExceptionUtils.getStackTrace(e));
        }

    }

    private void processGetUserInventory(User user) {
        System.out.println("InventoryHandler processGetUserInventory");
        try {
            PlayerInfo userInfo = (PlayerInfo) user.getProperty(ServerConstant.PLAYER_INFO);
            if (userInfo == null) {
                logger.info("PlayerInfo null");
                //send(new ResponseGetName(DemoHandler.DemoError.PLAYERINFO_NULL.getValue(), ""), user);
            }
            logger.info("get inventoryID " + userInfo.getId());
            Inventory userInventory = (Inventory) Inventory.getModel(userInfo.getId(), Inventory.class);
            //userCardCollection.show();
            send(new ResponseRequestGetUserInventory(InventoryHandler.InventoryError.SUCCESS.getValue(), userInventory), user);
        } catch (Exception e) {
            logger.info("processGetName exception");
            //send(new ResponseGetName(DemoHandler.DemoError.EXCEPTION.getValue(), ""), user);
        }
    }

    private void processUpgradeCard(RequestUpgradeCard rq, User user) {
        try {
            PlayerInfo userInfo = (PlayerInfo) user.getProperty(ServerConstant.PLAYER_INFO);
            if (userInfo == null) {
                logger.info("PlayerInfo null");
                //send(new ResponseGetName(UserHandler.UserError.PLAYERINFO_NULL.getValue(), ""), user);
            }
            System.out.println("Inventory Handle ProcessUpgradeCard");
            Inventory userInventory = (Inventory) Inventory.getModel(userInfo.getId(), Inventory.class);
            int cardType = rq.getcardType();
            int requireGold = userInventory.getGoldToUpgradeCard(cardType);
            int requireCard = userInventory.getfragmentToUpgradeCard(cardType);
            if ((verifyPurchase(userInfo.getGold(), requireGold) == true)
                    && (userInventory.checkFragmentToUpgradeCard(cardType) == true)) {
                userInfo.addGold(-requireGold);
                userInventory.upgradeCard(cardType);
                send(new ResponseRequestUpgradeCard(InventoryError.SUCCESS.getValue(), new InventoryDTO(-requireGold, cardType, -requireCard)), user);
                userInfo.saveModel(userInfo.getId());
                userInventory.saveModel(userInfo.getId());
                //userCardCollection.show();
            } else {
                send(new ResponseRequestUpgradeCard(InventoryError.ERROR.getValue(), new InventoryDTO(0, 0, 0)), user);
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
