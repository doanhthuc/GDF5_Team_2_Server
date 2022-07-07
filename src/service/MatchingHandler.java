package service;

import bitzero.server.BitZeroServer;
import bitzero.server.entities.User;
import bitzero.server.extensions.BaseClientRequestHandler;
import bitzero.server.extensions.data.DataCmd;
import cmd.receive.matching.RequestMatching;
import cmd.send.cheat.ResponseRequestCheatUserInfo;
import match.MatchMaking;
import model.PlayerInfo;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cmd.CmdDefine;
import util.server.ServerConstant;

import java.util.concurrent.TimeUnit;

public class MatchingHandler extends BaseClientRequestHandler {
    public static short MATCH_MULTI_IDS = 8000;
    private final Logger logger = LoggerFactory.getLogger("MatchHandler");
    private MatchMaking matchMaking;

    @Override
    public void init() {
        super.init();
        BitZeroServer.getInstance().getTaskScheduler().scheduleAtFixedRate(matchMaking, 0, 100, TimeUnit.MILLISECONDS);
    }

    @Override
    public void handleClientRequest(User user, DataCmd dataCmd) {
        try {
            switch (dataCmd.getId()) {
                case CmdDefine.MATCHING:
                    RequestMatching request = new RequestMatching(dataCmd);
                    processMatching(request, user);
                    break;
            }
        } catch (Exception e) {
            logger.error("Matching Handler Exception: " + e.getMessage());
            logger.error(ExceptionUtils.getStackTrace(e));
        }
    }

    private void processMatching(RequestMatching request, User user) {
        try {
            PlayerInfo userInfo = getUserInfo(user);
            if (userInfo == null) {
                return;
            }

        } catch (Exception e) {
            logger.error("processMatching exception");
        }
    }

    private PlayerInfo getUserInfo(User user) {
        PlayerInfo playerInfo = (PlayerInfo) user.getProperty(ServerConstant.PLAYER_INFO);
        if (playerInfo == null) {
            send( new ResponseRequestCheatUserInfo(UserHandler.UserError.USERINFO_NULL.getValue()), user);
            logger.error("PlayerInfo null");
            return null;
        }
        return playerInfo;
    }

    public enum MatchingStatus {
        SUCCESS((short) 0),
        ERROR((short) 1);

        private final short value;

        private MatchingStatus(short value) {
            this.value = value;
        }

        public short getValue() {
            return this.value;
        }
    }
}