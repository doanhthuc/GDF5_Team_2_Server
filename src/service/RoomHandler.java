package service;

import bitzero.server.BitZeroServer;
import bitzero.server.core.IBZEvent;
import bitzero.server.entities.User;
import bitzero.server.extensions.BaseClientRequestHandler;
import bitzero.server.extensions.data.DataCmd;
import bitzero.util.socialcontroller.bean.UserInfo;
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
import model.battle.PlayerInBattle;
import model.battle.Room;
import model.battle.RoomManager;
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
            PlayerInfo user2 = (PlayerInfo) PlayerInfo.getModel(1, PlayerInfo.class);
            Room room = new Room(userInfo, user2);
            RoomManager.getInstance().addRoom(room);
            new Thread(room).start();
            send(new ResponseRequestGetRoomInfo(RoomError.SUCCESS.getValue(), room, (int) user2.getId()), user);
        } catch (Exception e) {
            logger.info("processAddUserGold exception");
        }
    }

    public enum RoomError {
        SUCCESS((short) 0),
        FAIL((short) 1),
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
