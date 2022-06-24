package service;

import bitzero.server.BitZeroServer;
import bitzero.server.core.BZEventParam;
import bitzero.server.core.BZEventType;
import bitzero.server.core.IBZEvent;
import bitzero.server.entities.User;
import bitzero.server.extensions.BaseClientRequestHandler;
import bitzero.server.extensions.data.DataCmd;
import cmd.CmdDefine;
import cmd.receive.shop.RequestBuyDailyShop;
import cmd.receive.shop.RequestBuyGoldShop;
import cmd.send.inventory.ResponseRequestGetUserInventory;
import cmd.send.shop.ResponseRequestBuyDailyShop;
import cmd.send.shop.ResponseRequestBuyGoldShop;
import cmd.send.shop.ResponseRequestGetUserDailyShop;
import event.eventType.DemoEventParam;
import event.eventType.DemoEventType;
import extension.FresherExtension;
import model.Chest.Chest;
import model.Inventory.Card;
import model.Inventory.CardCollection;
import model.Item.Item;
import model.Item.ItemDefine;
import model.PlayerInfo;
import model.Shop.*;
import model.Shop.ItemList.DailyItemList;
import model.Shop.ItemList.ShopItemDefine;
import model.Shop.ItemList.ShopItemList;
import model.Shop.ShopDTO;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.server.ServerConstant;

import java.util.ArrayList;
import java.util.List;

public class ShopHandler extends BaseClientRequestHandler {
    public static short SHOP_MULTI_IDS = 2000;
    private final Logger logger = LoggerFactory.getLogger("ShopHandler");

    public ShopHandler() {
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

//        if (ibzevent.getType() == BZEventType.USER_DISCONNECT)
//            this.userDisconnect((User) ibzevent.getParameter(BZEventParam.USER));
//        else if (ibzevent.getType() == DemoEventType.CHANGE_NAME)
//            this.userChangeName((User) ibzevent.getParameter(DemoEventParam.USER), (String) ibzevent.getParameter(DemoEventParam.NAME));
    }

    public void handleClientRequest(User user, DataCmd dataCmd) {
        try {
            switch (dataCmd.getId()) {
                case CmdDefine.BUY_GOLD_SHOP:
                    RequestBuyGoldShop rqBuy = new RequestBuyGoldShop(dataCmd);
                    processBuyGoldShop(rqBuy, user);
                    break;
                case CmdDefine.BUY_DAILY_SHOP:
                    RequestBuyDailyShop rq = new RequestBuyDailyShop(dataCmd);
                    processBuyDailyShop(rq, user);
                    break;
                case CmdDefine.GET_DAILY_SHOP:
                    processGetUserDailyShop(user);
                    break;

            }
        } catch (Exception e) {
            logger.warn("USERHANDLER EXCEPTION " + e.getMessage());
            logger.warn(ExceptionUtils.getStackTrace(e));
        }
    }

    private void processBuyDailyShop(RequestBuyDailyShop rq, User user) {
        try {
            PlayerInfo userInfo = (PlayerInfo) user.getProperty(ServerConstant.PLAYER_INFO);
            if (userInfo == null) {
                logger.info("PlayerInfo null");
            }
            System.out.println("ShopHandle ProcessBuyDailyShop");
            DailyItemList DIL = (DailyItemList) DailyItemList.getModel(userInfo.getId(), DailyItemList.class);
            ShopItem item_to_buy = DIL.itemList.get(rq.getId());
            int goldchange = -item_to_buy.getPrice();
            if ((verifyPurchase(userInfo.getGold(), item_to_buy.getPrice()) == true)
                    && (item_to_buy.getState()== ShopItemDefine.CAN_BUY))
            {
                userInfo.addGold(goldchange);
                CardCollection userCardCollection = (CardCollection) CardCollection.getModel(userInfo.getId(), CardCollection.class);

                if (item_to_buy.getItemType() == ItemDefine.CHESTYPE) {
                    Chest ch = new Chest();
                    ArrayList<Item> reward = ch.getChestReward();
                    for (int i = 0; i < reward.size(); i++) {
                        Item item = reward.get(i);
                        item.show();
                        if (item.getItemType() == ItemDefine.GOLDTYPE) userInfo.addGold(item.getQuantity());
                        else userCardCollection.updateCard(item.getItemType(), item.getQuantity());
                    }
                    send(new ResponseRequestBuyDailyShop(ShopError.SUCCESS.getValue(), new ShopDTO(goldchange, 0, reward)), user);
                    }
                else {
                    userCardCollection.updateCard(item_to_buy.getItemType(), item_to_buy.getQuantity());
                    ArrayList<Item> itemList = new ArrayList<Item>();
                    itemList.add(item_to_buy);
                    send(new ResponseRequestBuyDailyShop(ShopError.SUCCESS.getValue(), new ShopDTO(goldchange, 0, itemList)), user);
                    }
                DIL.itemList.get(rq.getId()).setState(ShopItemDefine.CAN_NOT_BUY);
                userInfo.saveModel(userInfo.getId());
                userCardCollection.saveModel(userInfo.getId());
//                userInfo.show();
//                userCardCollection.show();
            } else {
                send(new ResponseRequestBuyDailyShop(ShopError.ERROR.getValue(), new ShopDTO(0, 0)), user);
            }
        } catch (Exception e) {
            logger.info("processBuyDailyShop exception");
        }
    }

    private void processBuyGoldShop(RequestBuyGoldShop rqBuyGold, User user) {
        try {
            PlayerInfo userInfo = (PlayerInfo) user.getProperty(ServerConstant.PLAYER_INFO);
            if (userInfo == null) {
                logger.info("PlayerInfo null");
                //send(new ResponseGetName(UserHandler.UserError.PLAYERINFO_NULL.getValue(), ""), user);
            }
            System.out.println("Shophandler ProcessBuyShopGold");
            ShopItemList SIL = (ShopItemList) ShopItemList.getModel(userInfo.getId(), ShopItemList.class);
            ShopItem item_to_buy = SIL.itemList.get(rqBuyGold.getId());
            int gemchange = -item_to_buy.getPrice();
            int goldchange = item_to_buy.getQuantity();
            if (verifyPurchase(userInfo.getGem(), item_to_buy.getPrice()) == true) {
                userInfo.addGem(gemchange);
                userInfo.addGold(goldchange);
                userInfo.saveModel(userInfo.getId());
                send(new ResponseRequestBuyGoldShop(ShopError.SUCCESS.getValue(), new ShopDTO(goldchange, gemchange)), user);
            } else {
                send(new ResponseRequestBuyGoldShop(ShopError.ERROR.getValue(), new ShopDTO(0, 0)), user);
            }
        } catch (Exception e) {
            logger.info("processGetName exception");
        }
    }

    private void processGetUserDailyShop(User user) {
        System.out.println("InventoryHandler " + " processGetUserInventory");
        try {
            PlayerInfo userInfo = (PlayerInfo) user.getProperty(ServerConstant.PLAYER_INFO);
            if (userInfo == null) {
                logger.info("PlayerInfo null");
                //send(new ResponseGetName(DemoHandler.DemoError.PLAYERINFO_NULL.getValue(), ""), user);
            }
            logger.info("get inventoryID " + userInfo.getId());
            DailyItemList DIL = (DailyItemList) DailyItemList.getModel(userInfo.getId(), DailyItemList.class);
            //usercardCollection.show();
            send(new ResponseRequestGetUserDailyShop(ShopHandler.ShopError.SUCCESS.getValue(), DIL), user);
        } catch (Exception e) {
            logger.info("processGetName exception");
            //send(new ResponseGetName(DemoHandler.DemoError.EXCEPTION.getValue(), ""), user);
        }
    }

    private boolean verifyPurchase(int userResource, int itemprice) {
        return (userResource >= itemprice);
    }

    private void userDisconnect(User user) {
    }

    private void userChangeName(User user, String name) {
        List<User> allUser = BitZeroServer.getInstance().getUserManager().getAllUsers();
        for (User aUser : allUser) {
        }
    }

    public enum ShopError {
        SUCCESS((short) 0),
        ERROR((short) 1),
        PLAYERINFO_NULL((short) 2),
        EXCEPTION((short) 3),
        INVALID_PARAM((short) 4),
        VISITED((short) 5),
        ;


        private final short value;

        private ShopError(short value) {
            this.value = value;
        }

        public short getValue() {
            return this.value;
        }
    }
}
