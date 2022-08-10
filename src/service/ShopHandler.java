package service;

import bitzero.server.core.BZEventType;
import bitzero.server.core.IBZEvent;
import bitzero.server.entities.User;
import bitzero.server.extensions.BaseClientRequestHandler;
import bitzero.server.extensions.data.DataCmd;
import cmd.CmdDefine;
import cmd.HandlerId;
import cmd.receive.shop.RequestBuyDailyShop;
import cmd.receive.shop.RequestBuyGoldShop;
import cmd.send.shop.ResponseRequestBuyDailyShop;
import cmd.send.shop.ResponseRequestBuyGoldShop;
import cmd.send.shop.ResponseRequestGetUserDailyShop;
import cmd.send.shop.ResponseRequestGetUserGoldShop;
import event.eventType.DemoEventType;
import extension.FresherExtension;
import model.Common.Chest;
import model.Inventory.Inventory;
import model.Common.Item;
import model.Common.ItemDefine;
import model.PlayerInfo;
import model.Shop.ItemList.DailyShop;
import model.Shop.ItemList.ShopItemDefine;
import model.Shop.ItemList.ShopItemList;
import model.Shop.ShopDTO;
import model.Shop.ShopItem;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.server.ServerConstant;

import java.util.ArrayList;

public class ShopHandler extends BaseClientRequestHandler {
    public static short HANDLER_ID = HandlerId.SHOP.getValue();
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
                case CmdDefine.GET_GOLD_SHOP:
                    processGetUserGoldShop(user);
                    break;
            }
        } catch (Exception e) {
            logger.warn("USERHANDLER EXCEPTION " + e.getMessage());
            logger.warn(ExceptionUtils.getStackTrace(e));
        }
    }

    private void processBuyDailyShop(RequestBuyDailyShop rq, User user) {
        try {
            System.out.println("ShopHandle ProcessBuyDailyShop");
            PlayerInfo userInfo = (PlayerInfo) user.getProperty(ServerConstant.PLAYER_INFO);
            DailyShop userDailyShop = (DailyShop) user.getProperty(ServerConstant.DAILY_SHOP);
            Inventory userInventory = (Inventory) user.getProperty(ServerConstant.INVENTORY);
            if (userInfo == null || userDailyShop == null) {
                System.out.println("PlayerInfo null");
                send(new ResponseRequestBuyDailyShop(ShopError.USER_INFO_NULL.getValue()), user);
            }
            synchronized (userDailyShop) {
                //getDailyShop and the ItemToBuy by ID
                ShopItem itemToBuy;
                itemToBuy = userDailyShop.getItemByID(rq.getId());
                //Verify Item
                if (itemToBuy == null) {
                    System.out.println("item null");
                    send(new ResponseRequestBuyDailyShop(ShopError.ITEM_TO_BUY_NULL.getValue()), user);
                    return;
                }

                // check current time of request with reset time
                if (!userDailyShop.isBeforeResetTime()) {
                    send(new ResponseRequestBuyDailyShop(ShopError.INVALID_RESET_TIME.getValue()), user);
                }

                //Verify Gold
                int goldChange = -itemToBuy.getPrice();
                if ((!verifyPurchase(userInfo.getGold(), itemToBuy.getPrice()))) {
                    System.out.println("not enough gold");
                    send(new ResponseRequestBuyDailyShop(ShopError.NOT_ENOUGH_GOLD.getValue()), user);
                    return;
                }
                //Verify State Can Buy
                if ((itemToBuy.getState() != ShopItemDefine.CAN_BUY)) {
                    System.out.println("cannotbuy");
                    send(new ResponseRequestBuyDailyShop(ShopError.ITEM_ALREADY_BUY.getValue()), user);
                    return;
                }
                synchronized (userInfo) {
                    userInfo.addGold(goldChange);
                    synchronized (userInventory) {
                        if (itemToBuy.getItemType() == ItemDefine.CHESTTYPE) {
                            Chest ch = new Chest();
                            ch.randomRewardItem();
                            ArrayList<Item> reward = ch.getChestReward();
                            for (int i = 0; i < reward.size(); i++) {
                                Item item = reward.get(i);
                                item.show();
                                if (item.getItemType() == ItemDefine.GOLDTYPE) userInfo.addGold(item.getQuantity());
                                else userInventory.updateCard(item.getItemType(), item.getQuantity());
                            }
                            send(new ResponseRequestBuyDailyShop(ShopError.SUCCESS.getValue(), new ShopDTO(goldChange, 0, reward, rq.getId())), user);
                        } else {
                            userInventory.updateCard(itemToBuy.getItemType(), itemToBuy.getQuantity());
                            ArrayList<Item> itemList = new ArrayList<Item>();
                            itemList.add(itemToBuy);
                            send(new ResponseRequestBuyDailyShop(ShopError.SUCCESS.getValue(), new ShopDTO(goldChange, 0, itemList, rq.getId())), user);
                        }
                    }
                }
                //Set Shop Item State and save model
                userDailyShop.itemList.get(rq.getId()).setState(ShopItemDefine.CAN_NOT_BUY);

                userInfo.saveModel(userInfo.getId());
                userInventory.saveModel(userInfo.getId());
                userDailyShop.saveModel(userInfo.getId());
            }
        } catch (
                Exception e) {
            logger.info("processBuyDailyShop exception");
        }
    }

    private void processBuyGoldShop(RequestBuyGoldShop rqBuyGold, User user) {
        try {
            System.out.println("ShopHandler ProcessBuyGoldShop");
            PlayerInfo userInfo = (PlayerInfo) user.getProperty(ServerConstant.PLAYER_INFO);
            if (userInfo == null) {
                logger.info("PlayerInfo null");
                send(new ResponseRequestBuyGoldShop(ShopError.USER_INFO_NULL.getValue()), user);
                return;
            }
            synchronized (userInfo) {
                //getItemByID
                ShopItemList goldShop = (ShopItemList) ShopItemList.getModel(userInfo.getId(), ShopItemList.class);
                ShopItem itemToBuy = goldShop.getItemByID(rqBuyGold.getId());

                //Verify Item To Buy
                if (itemToBuy == null) {
                    send(new ResponseRequestBuyGoldShop(ShopError.ITEM_TO_BUY_NULL.getValue()), user);
                    return;
                }

                //Verify Gem
                int gemChange = itemToBuy.getPrice();
                int goldChange = itemToBuy.getQuantity();
                if (verifyPurchase(userInfo.getGem(), gemChange)) {
                    userInfo.addGem(-gemChange);
                    userInfo.addGold(goldChange);
                    userInfo.saveModel(userInfo.getId());
                    send(new ResponseRequestBuyGoldShop(ShopError.SUCCESS.getValue(), new ShopDTO(goldChange, -gemChange, rqBuyGold.getId())), user);
                } else {
                    send(new ResponseRequestBuyGoldShop(ShopError.NOT_ENOUGH_GEM.getValue()), user);
                }
            }
        } catch (Exception e) {
            logger.info("processBuyGoldShop exception");
        }
    }

    private void processGetUserDailyShop(User user) {
        System.out.println("ShopHandler " + " processGetUserDailyShop");
        try {
            DailyShop userDailyShop = (DailyShop) user.getProperty(ServerConstant.DAILY_SHOP);
            if (userDailyShop == null) {
                logger.info("PlayerInfo null");
                send(new ResponseRequestGetUserDailyShop(ShopError.DALY_SHOP_NULL.getValue()), user);
                return;
            }
            //CheckDailyShop
            synchronized (userDailyShop) {
                if (!userDailyShop.isBeforeResetTime()) {
                    userDailyShop = new DailyShop(user.getId());
                    userDailyShop.saveModel(user.getId());
                    user.setProperty(ServerConstant.DAILY_SHOP, userDailyShop);
                    logger.error("XXXXXXXXXXXXXXXXXXXXXXX");
                }
                send(new ResponseRequestGetUserDailyShop(ShopHandler.ShopError.SUCCESS.getValue(), userDailyShop), user);
            }
        } catch (Exception e) {
            logger.info("processGetUserDailyShop exception");
        }
    }

    private void processGetUserGoldShop(User user) {
        System.out.println("ShopHandler " + " processGetUserGoldShop");
        try {
            ShopItemList userGoldShop = (ShopItemList) user.getProperty(ServerConstant.GOLD_SHOP);
            if (userGoldShop == null) {
                logger.info("PlayerInfo null");
                send(new ResponseRequestGetUserGoldShop(ShopError.USER_INFO_NULL.getValue()), user);
                return;
            }
            logger.info("get inventoryID " + user.getId());
            //CheckGoldShop
            if (userGoldShop == null) {
                send(new ResponseRequestGetUserDailyShop(ShopError.GOLD_SHOP_NULL.getValue()), user);
                return;
            }

            send(new ResponseRequestGetUserGoldShop(ShopHandler.ShopError.SUCCESS.getValue(), userGoldShop), user);
        } catch (Exception e) {
            logger.info("processGetUserGoldShop exception");
        }
    }

    private boolean verifyPurchase(int userResource, int itemPrice) {
        return (userResource >= itemPrice);
    }

    private void userDisconnect(User user) {
    }


    public enum ShopError {
        SUCCESS((short) 0),
        USER_INFO_NULL((short) 1),
        ITEM_TO_BUY_NULL((short) 2),
        NOT_ENOUGH_GEM((short) 3),
        NOT_ENOUGH_GOLD((short) 4),
        ITEM_ALREADY_BUY((short) 5),
        DALY_SHOP_NULL((short) 6),
        GOLD_SHOP_NULL((short) 7),
        INVALID_RESET_TIME((short) 8),
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
