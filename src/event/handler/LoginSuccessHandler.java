package event.handler;

import bitzero.server.core.BZEvent;
import bitzero.server.core.BZEventParam;
import bitzero.server.core.IBZEvent;
import bitzero.server.entities.User;
import bitzero.server.extensions.BaseServerEventHandler;
import bitzero.util.ExtensionUtility;
import event.eventType.DemoEventParam;
import event.eventType.DemoEventType;
import model.Inventory.Inventory;
import model.Lobby.UserLobbyChest;
import model.PlayerInfo;
import model.Shop.ItemList.DailyShop;
import model.Shop.ItemList.ShopItemDefine;
import model.Shop.ItemList.ShopItemList;
import util.server.ServerConstant;

import java.util.HashMap;
import java.util.Map;

public class LoginSuccessHandler extends BaseServerEventHandler {
    private IBZEvent iBZEvent;

    public LoginSuccessHandler() {
        super();
    }

    public void handleServerEvent(IBZEvent iBZEvent) {
        this.iBZEvent = iBZEvent;
        this.onLoginSuccess((User) iBZEvent.getParameter(BZEventParam.USER));
    }

    /**
     * @param user description: after login successful to server, core framework will dispatch this event
     */
    private void onLoginSuccess(User user) {
        //trace(ExtensionLogLevel.DEBUG, "On Login Success ", user.getName());
        System.out.println("LoginSuccessHandle " + user.getName() + " " + user.getId());
        PlayerInfo pInfo = null;
        Inventory userInventory = null;
        UserLobbyChest userLobbyChest = null;
        DailyShop userDailyShop = null;
        ShopItemList userGoldShop = null;
        try {
            pInfo = (PlayerInfo) PlayerInfo.getModel(user.getId(), PlayerInfo.class);
            userInventory = (Inventory) Inventory.getModel(user.getId(), Inventory.class);
            userLobbyChest = (UserLobbyChest) UserLobbyChest.getModel(user.getId(), UserLobbyChest.class);
            userDailyShop = (DailyShop) DailyShop.getModel(user.getId(), DailyShop.class);
            userGoldShop = (ShopItemList) ShopItemList.getModel(user.getId(), ShopItemList.class);

            if (pInfo == null) {
                pInfo = new PlayerInfo(user.getId(), "username" + user.getId(), 0, 0, 0);
                pInfo.saveModel(user.getId());
            }
            if (userInventory == null) {
                userInventory = new Inventory(user.getId());
                userInventory.saveModel(user.getId());
            }
            if (userLobbyChest == null) {
                userLobbyChest = new UserLobbyChest(user.getId());
                userLobbyChest.saveModel(user.getId());
            }
            if (userDailyShop == null) {
                userDailyShop = new DailyShop(user.getId());
                userDailyShop.saveModel(user.getId());
            }
            if (userGoldShop == null) {
                userGoldShop = new ShopItemList(user.getId(), ShopItemDefine.GoldBanner);
                userGoldShop.saveModel(user.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        /**
         * cache playerinfo in RAM
         */
        // deploy code here
        user.setProperty(ServerConstant.PLAYER_INFO, pInfo);
        user.setProperty(ServerConstant.INVENTORY, userInventory);
        user.setProperty(ServerConstant.DAILY_SHOP, userDailyShop);
        user.setProperty(ServerConstant.LOBBY_CHEST, userLobbyChest);
        user.setProperty(ServerConstant.GOLD_SHOP, userGoldShop);
        /**
         * send login success to client
         * after receive this message, client begin to send game logic packet to server
         */
        System.out.println("send success");
        System.out.println("");
        ExtensionUtility.instance().sendLoginOK(user);

        /**
         * dispatch event here
         */
        Map evtParams = new HashMap();
        evtParams.put(DemoEventParam.USER, user);
        evtParams.put(DemoEventParam.NAME, user.getName());
        ExtensionUtility.dispatchEvent(new BZEvent(DemoEventType.LOGIN_SUCCESS, evtParams));

    }

}
