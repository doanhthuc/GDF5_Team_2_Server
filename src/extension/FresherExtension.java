package extension;


import battle.BattleMap;
import bitzero.engine.sessions.ISession;
import bitzero.server.config.ConfigHandle;
import bitzero.server.core.BZEventType;
import bitzero.server.entities.User;
import bitzero.server.entities.managers.ConnectionStats;
import bitzero.server.extensions.BZExtension;
import bitzero.server.extensions.data.DataCmd;
import bitzero.util.ExtensionUtility;
import bitzero.util.common.business.Debug;
import bitzero.util.datacontroller.business.DataController;
import bitzero.util.socialcontroller.bean.UserInfo;
import cmd.receive.authen.RequestLogin;
import event.eventType.DemoEventType;
import event.handler.LoginSuccessHandler;
import model.Inventory.CardCollection;
import model.Lobby.LobbyChestContainer;
import model.PlayerInfo;
import model.Shop.ItemList.DailyItemList;
import model.Shop.ItemList.ShopItemDefine;
import model.Shop.ItemList.ShopItemList;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.json.JSONObject;
import service.*;
import util.GuestLogin;
import util.metric.LogObject;
import util.metric.MetricLog;
import util.server.ServerConstant;
import util.server.ServerLoop;

import java.util.List;


public class FresherExtension extends BZExtension {
    private static String SERVERS_INFO =
            ConfigHandle.instance().get("servers_key") == null ? "servers" : ConfigHandle.instance().get("servers_key");

    private ServerLoop svrLoop;

    public FresherExtension() {
        super();
        setName("Fresher");
        svrLoop = new ServerLoop();
    }

    public void init() {

        /**
         * register new handler to catch client's packet
         */
//        BattleMap btm = new BattleMap();
//        btm.show();
        trace("  Register Handler ");
        addRequestHandler(UserHandler.USER_MULTI_IDS, UserHandler.class);
        addRequestHandler(ShopHandler.SHOP_MULTI_IDS, ShopHandler.class);
        addRequestHandler(InventoryHandler.INVENTORY_MULTI_IDS, InventoryHandler.class);
        addRequestHandler(LobbyHandler.LOBBY_MULTI_IDS, LobbyHandler.class);
        addRequestHandler(CheatHandler.CHEAT_MULTI_IDS, CheatHandler.class);
        addRequestHandler(BattleHandler.BATTLE_MULTI_IDS, BattleHandler.class);
        registerHandler();
    }

    public ServerLoop getServerLoop() {
        return svrLoop;
    }

    @Override
    public void monitor() {
        try {
            ConnectionStats connStats = bz.getStatsManager().getUserStats();
            JSONObject data = new JSONObject();

            data.put("totalInPacket", bz.getStatsManager().getTotalInPackets());
            data.put("totalOutPacket", bz.getStatsManager().getTotalOutPackets());
            data.put("totalInBytes", bz.getStatsManager().getTotalInBytes());
            data.put("totalOutBytes", bz.getStatsManager().getTotalOutBytes());

            data.put("connectionCount", connStats.getSocketCount());
            data.put("totalUserCount", bz.getUserManager().getUserCount());

            DataController.getController().setCache(SERVERS_INFO, 60 * 5, data.toString());
        } catch (Exception e) {
            trace("Ex monitor");
        }
    }

    public void initUserData(long userID) {
        System.out.println("initUserdata");
        ShopItemList goldShop = new ShopItemList(userID, ShopItemDefine.GoldBanner);
        System.out.println(goldShop.itemList.get(0).getItemID());
        DailyItemList dailyShop = new DailyItemList(userID);
        CardCollection userCardCollection = new CardCollection(userID);
        LobbyChestContainer userLobbyChest = new LobbyChestContainer(userID);
        try {
            goldShop.saveModel(userID);
            dailyShop.saveModel(userID);
            userCardCollection.saveModel(userID);
            userLobbyChest.saveModel(userID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void showUserData(int userId) {
        try {
            DailyItemList dailyShop = (DailyItemList) DailyItemList.getModel(userId, DailyItemList.class);
            dailyShop.show();
            CardCollection userCardCollection = (CardCollection) CardCollection.getModel(userId, CardCollection.class);
            userCardCollection.show();
            LobbyChestContainer userLobbyChest = (LobbyChestContainer) LobbyChestContainer.getModel(userId, LobbyChestContainer.class);
            userLobbyChest.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        List<User> allUser = ExtensionUtility.globalUserManager.getAllUsers();
        if (allUser.size() == 0)
            return;

        User obj = null;

        for (int i = 0; i < allUser.size(); i++) {
            obj = allUser.get(i);
            // do sth with user
            LogObject logObject = new LogObject(LogObject.ACTION_LOGOUT);
            logObject.zingId = obj.getId();
            logObject.zingName = obj.getName();
            System.out.println(obj.getName());
            //System.out.println("Log logout = " + logObject.getLogMessage());
            MetricLog.writeActionLog(logObject);
        }
    }

    /**
     * @param cmdId
     * @param session
     * @param objData the first packet send from client after handshake success will dispatch to doLogin() function
     */
    public void doLogin(short cmdId, ISession session, DataCmd objData) {
        RequestLogin reqGet = new RequestLogin(objData);
        reqGet.unpackData();
        System.out.println(reqGet.userId);
        try {
            PlayerInfo userInfo;
            if (PlayerInfo.getModel(reqGet.userId, PlayerInfo.class) == null) {
                userInfo = new PlayerInfo(reqGet.userId, "username" + reqGet.userId, 2000, 2000, 0);
                userInfo.show();
                userInfo.saveModel(userInfo.getId());
                initUserData(userInfo.getId());
            } else {
                userInfo = (PlayerInfo) PlayerInfo.getModel(reqGet.userId, PlayerInfo.class);
            }
            UserInfo uInfo = getUserInfo(reqGet.sessionKey, userInfo.getId(), session.getAddress());
            User u = ExtensionUtility.instance().canLogin(uInfo, "", session);
            if (u != null)
                u.setProperty("userId", uInfo.getUserId());
        } catch (Exception e) {
            e.printStackTrace();
            Debug.warn("DO LOGIN EXCEPTION " + e.getMessage());
            Debug.warn(ExceptionUtils.getStackTrace(e));
        }
    }

    public UserInfo getUserInfo(String username, long userId, String ipAddress) throws Exception {
        int customLogin = ServerConstant.CUSTOM_LOGIN;
        switch (customLogin) {
            case 1: // login zingme
                return ExtensionUtility.getUserInfoFormPortal(username);
            case 2: // set direct userid
                return GuestLogin.setInfo(userId, "username" + userId);
            default: // auto increment
                System.out.println("auto increment id");
                return GuestLogin.newGuest();
        }
    }

    private void registerHandler() {
        /**
         * register new event
         */
        trace(" Event Handler ");
        addEventHandler(BZEventType.USER_LOGIN, LoginSuccessHandler.class);
        addEventHandler(DemoEventType.LOGIN_SUCCESS, DemoHandler.class);
    }

}
// addEventHandler(BZEventType.USER_LOGOUT, LogoutHandler.class);
//addEventHandler(BZEventType.USER_DISCONNECT, LogoutHandler.class);