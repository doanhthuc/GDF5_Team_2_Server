package service;

import bitzero.server.BitZeroServer;
import bitzero.server.core.IBZEvent;
import bitzero.server.entities.User;
import bitzero.server.extensions.BaseClientRequestHandler;
import bitzero.server.extensions.data.DataCmd;
import cmd.CmdDefine;
import cmd.receive.cheat.RequestCheatUserInfo;
import cmd.receive.room.RequestRoomInfo;
import cmd.receive.user.RequestAddGem;
import cmd.receive.user.RequestAddGold;
import cmd.send.room.ResponseRequestGetRoomInfo;
import cmd.send.user.ResponseAddGem;
import cmd.send.user.ResponseAddGold;
import cmd.send.user.ResponseLogout;
import cmd.send.user.ResponseRequestUserInfo;
import extension.FresherExtension;
import model.PlayerInfo;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.server.ServerConstant;

import java.util.List;

public class RoomHandler extends BaseClientRequestHandler {
    public static short ROOM_MULTI_IDS = 6000;
    private final Logger logger = LoggerFactory.getLogger("UserHandler");

    public RoomHandler() {
        super();
    }

    public void init() {
    }

    private FresherExtension getExtension() {
        return (FresherExtension) getParentExtension();
    }

    public void handleServerEvent(IBZEvent ibzevent) {
    }

    public void handleClientRequest(User user, DataCmd dataCmd) {
        try {
            switch (dataCmd.getId()) {
                case CmdDefine.ENTER_ROOM:
                    RequestRoomInfo rq = new RequestRoomInfo(dataCmd);
                    processGetRoomInfo(user, rq);
                    break;

            }
        } catch (Exception e) {
            logger.warn("USERHANDLER EXCEPTION " + e.getMessage());
            logger.warn(ExceptionUtils.getStackTrace(e));
        }

    }

    private void processGetRoomInfo(User user, RequestRoomInfo rq) {
        try {
            PlayerInfo userInfo = (PlayerInfo) user.getProperty(ServerConstant.PLAYER_INFO);
            if (userInfo == null) {
                logger.info("PlayerInfo null");
                send(new ResponseRequestGetRoomInfo(RoomError.FAIL.getValue()), user);
            }
            int roomId = rq.getRoomID();
            send(new ResponseRequestGetRoomInfo(RoomError.SUCCESS.getValue(), user.getId()), user);
        } catch (Exception e) {
            logger.info("processAddUserGold exception");
        }
    }

    private void processAddUserGem(RequestAddGem requestaddGem, User user) {
        try {
            PlayerInfo userInfo = (PlayerInfo) user.getProperty(ServerConstant.PLAYER_INFO);
            if (userInfo == null) {
                logger.info("PlayerInfo null");
                send(new ResponseRequestUserInfo(UserHandler.UserError.USERINFO_NULL.getValue()), user);
            }
            userInfo.addGem(requestaddGem.getGem());
            userInfo.saveModel(userInfo.getId());
            send(new ResponseAddGem(UserHandler.UserError.SUCCESS.getValue(), requestaddGem.getGem()), user);
        } catch (Exception e) {
            logger.info("processAddUserGem exception");
        }
    }
    private void processLogoutUser(User user){
        System.out.println("loggout");
        send(new ResponseLogout(UserHandler.UserError.SUCCESS.getValue()),user);
        BitZeroServer.getInstance().getSessionManager().removeSession(user.getSession());
        BitZeroServer.getInstance().getUserManager().disconnectUser(user);
        //      ExtensionUtility.dispatchEvent(new BZEvent(BZEventType.USER_DISCONNECT));
    }

    private void userDisconnect(User user) {
        System.out.println("UserDisconnect");
    }

    private void userChangeName(User user, String name) {
        List<User> allUser = BitZeroServer.getInstance().getUserManager().getAllUsers();
        for (User aUser : allUser) {
        }
    }

    public enum RoomError {
        SUCCESS((short) 0),
        FAIL((short)1),
        ;


        private final short value;

        private RoomError(short value) {
            this.value = value;
        }

        public short getValue() {
            return this.value;
        }
    }
}
