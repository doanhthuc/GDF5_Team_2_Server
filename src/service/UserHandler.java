package service;

import bitzero.server.BitZeroServer;
import bitzero.server.core.BZEventParam;
import bitzero.server.core.BZEventType;
import bitzero.server.core.IBZEvent;
import bitzero.server.entities.User;
import bitzero.server.extensions.BaseClientRequestHandler;
import bitzero.server.extensions.data.DataCmd;
import cmd.CmdDefine;
import cmd.receive.user.RequestAddGem;
import cmd.receive.user.RequestAddGold;
import cmd.send.user.ResponseAddGem;
import cmd.send.user.ResponseAddGold;
import cmd.send.user.ResponseRequestUserInfo;
import event.eventType.DemoEventParam;
import event.eventType.DemoEventType;
import extension.FresherExtension;
import model.PlayerInfo;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.server.ServerConstant;

import java.util.List;

public class UserHandler extends BaseClientRequestHandler {
    public static short USER_MULTI_IDS = 1000;
    private final Logger logger = LoggerFactory.getLogger("UserHandler");

    public UserHandler() {
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

        if (ibzevent.getType() == BZEventType.USER_DISCONNECT)
            this.userDisconnect((User) ibzevent.getParameter(BZEventParam.USER));
        else if (ibzevent.getType() == DemoEventType.CHANGE_NAME)
            this.userChangeName((User) ibzevent.getParameter(DemoEventParam.USER), (String) ibzevent.getParameter(DemoEventParam.NAME));
    }

    public void handleClientRequest(User user, DataCmd dataCmd) {
        try {
            switch (dataCmd.getId()) {
                case CmdDefine.GET_USER_INFO:
                    processGetUserInfo(user);
                    break;
                case CmdDefine.ADD_USER_GOLD:
                    RequestAddGold addGold = new RequestAddGold(dataCmd);
                    processAddUserGold(addGold, user);
                case CmdDefine.ADD_USER_GEM:
                    RequestAddGem addGem = new RequestAddGem(dataCmd);
                    processAddUserGem(addGem, user);
            }
        } catch (Exception e) {
            logger.warn("USERHANDLER EXCEPTION " + e.getMessage());
            logger.warn(ExceptionUtils.getStackTrace(e));
        }

    }

    private void processGetUserInfo(User user) {
        System.out.println("UserHandler " + " processGetUserInfo");
        try {
            PlayerInfo userInfo = (PlayerInfo) user.getProperty(ServerConstant.PLAYER_INFO);
            if (userInfo == null) {
                logger.info("PlayerInfo null");
                //send(new ResponseGetName(DemoHandler.DemoError.PLAYERINFO_NULL.getValue(), ""), user);
            }
            logger.info("get name = " + userInfo.toString());
            send(new ResponseRequestUserInfo(DemoHandler.DemoError.SUCCESS.getValue(), userInfo), user);
        } catch (Exception e) {
            logger.info("processGetName exception");
            //send(new ResponseGetName(DemoHandler.DemoError.EXCEPTION.getValue(), ""), user);
        }
    }

    private void processAddUserGold(RequestAddGold requestaddGold, User user) {
        try {
            PlayerInfo userInfo = (PlayerInfo) user.getProperty(ServerConstant.PLAYER_INFO);
            if (userInfo == null) {
                logger.info("PlayerInfo null");
                //  send(new ResponseGetName(UserHandler.UserError.PLAYERINFO_NULL.getValue(), ""), user);
            }
            userInfo.addGold(requestaddGold.getGold());
            userInfo.saveModel(userInfo.getId());
            send(new ResponseAddGold(UserHandler.UserError.SUCCESS.getValue(), requestaddGold.getGold()), user);
        } catch (Exception e) {
            logger.info("processGetName exception");
            //send(new ResponseGetName(UserHandler.UserError.EXCEPTION.getValue(), ""), user);
        }
    }

    private void processAddUserGem(RequestAddGem requestaddGem, User user) {
        try {
            PlayerInfo userInfo = (PlayerInfo) user.getProperty(ServerConstant.PLAYER_INFO);
            if (userInfo == null) {
                logger.info("PlayerInfo null");
                //  send(new ResponseGetName(UserHandler.UserError.PLAYERINFO_NULL.getValue(), ""), user);
            }
            userInfo.addGem(requestaddGem.getGem());
            userInfo.saveModel(userInfo.getId());
            send(new ResponseAddGem(UserHandler.UserError.SUCCESS.getValue(), requestaddGem.getGem()), user);
        } catch (Exception e) {
            logger.info("processGetName exception");
            //send(new ResponseGetName(UserHandler.UserError.EXCEPTION.getValue(), ""), user);
        }
    }

    private void userDisconnect(User user) {
    }

    private void userChangeName(User user, String name) {
        List<User> allUser = BitZeroServer.getInstance().getUserManager().getAllUsers();
        for (User aUser : allUser) {
        }
    }

    public enum UserError {
        SUCCESS((short) 0),
        ERROR((short) 1),
        ;


        private final short value;

        private UserError(short value) {
            this.value = value;
        }

        public short getValue() {
            return this.value;
        }
    }
}
