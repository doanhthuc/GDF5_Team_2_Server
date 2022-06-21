package event.handler;

import bitzero.server.core.BZEvent;
import bitzero.server.core.BZEventParam;
import bitzero.server.core.IBZEvent;
import bitzero.server.entities.User;
import bitzero.server.extensions.BaseServerEventHandler;
import bitzero.server.extensions.ExtensionLogLevel;
import bitzero.util.ExtensionUtility;
import event.eventType.DemoEventParam;
import event.eventType.DemoEventType;
import model.PlayerInfo;
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
     * @param user
     * description: after login successful to server, core framework will dispatch this event
     */
    private void onLoginSuccess(User user) {
        //trace(ExtensionLogLevel.DEBUG, "On Login Success ", user.getName());
        System.out.println("LoginSuccessHandle "+user.getName()+" "+user.getId());
        PlayerInfo pInfo = null;
        try {
            pInfo = (PlayerInfo) PlayerInfo.getModel(user.getId(), PlayerInfo.class);
            if (pInfo==null){
                pInfo = new PlayerInfo(user.getId(), "username" + user.getId(),0,0,0);
                pInfo.saveModel(user.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        /**
         * cache playerinfo in RAM
         */
        // deploy code here
        user.setProperty(ServerConstant.PLAYER_INFO, pInfo);
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
