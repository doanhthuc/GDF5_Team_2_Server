package extension;


import bitzero.engine.sessions.ISession;
import bitzero.server.BitZeroServer;
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
import event.handler.LogoutHandler;
import event.handler.NotifyController;
import model.PlayerInfo;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.json.JSONObject;
import service.DemoHandler;
import service.UserHandler;
import util.GuestLogin;
import util.metric.LogObject;
import util.metric.MetricLog;
import util.server.ServerConstant;
import util.server.ServerLoop;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


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


        //System.out.println("123");
        PlayerInfo pInfo= null;
        for (int i = 1; i < 13; i++) {
            pInfo = new PlayerInfo(i, "username" + i,i,i,i);
            try {
                pInfo.saveModel(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //List<User> allUser = ExtensionUtility.globalUserManager.getAllUsers();
        //List<User> allUser = BitZeroServer.getInstance().getUserManager().getAllUsers();
        //System.out.println(allUser.size());
        for (int i = 1; i < 13; i++) {
            try {
                pInfo = (PlayerInfo) PlayerInfo.getModel(i, PlayerInfo.class);
                pInfo.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        trace("  Register Handler ");
        addRequestHandler(UserHandler.USER_MULTI_IDS, UserHandler.class);
        addRequestHandler(DemoHandler.DEMO_MULTI_IDS, DemoHandler.class);
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
     *
     * @param cmdId
     * @param session
     * @param objData
     *
     * the first packet send from client after handshake success will dispatch to doLogin() function
     */
    public void doLogin(short cmdId, ISession session, DataCmd objData) {
        RequestLogin reqGet = new RequestLogin(objData);
        reqGet.unpackData();
        System.out.println("FresherExtension " + reqGet.userId+ " "+ reqGet.sessionKey);
        try {
            if (PlayerInfo.getModel(reqGet.userId, PlayerInfo.class)==null) return;
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            UserInfo uInfo = getUserInfo(reqGet.sessionKey, reqGet.userId, session.getAddress());
            User u = ExtensionUtility.instance().canLogin(uInfo, "", session);
            if (u!=null)
                u.setProperty("userId", uInfo.getUserId());
        } catch (Exception e) {
            Debug.warn("DO LOGIN EXCEPTION " + e.getMessage());
            Debug.warn(ExceptionUtils.getStackTrace(e));
        }

    }

    public UserInfo getUserInfo(String username, int userId, String ipAddress) throws Exception {
        int customLogin = ServerConstant.CUSTOM_LOGIN;
        switch(customLogin){
            case 1: // login zingme
                return ExtensionUtility.getUserInfoFormPortal(username);
            case 2: // set direct userid
                return GuestLogin.setInfo(userId, "username" + userId);
            default: // auto increment
                System.out.println("auto increment id");
                return GuestLogin.newGuest();
        }        
    }

    private void registerHandler(){
        /**
         * register new event
         */
        trace(" Event Handler ");
        addEventHandler(BZEventType.USER_LOGIN, LoginSuccessHandler.class);
        // addEventHandler(BZEventType.USER_LOGOUT, LogoutHandler.class);
        //addEventHandler(BZEventType.USER_DISCONNECT, LogoutHandler.class);
        addEventHandler(DemoEventType.LOGIN_SUCCESS, DemoHandler.class);

    }

}
