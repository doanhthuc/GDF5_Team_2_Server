package service;

import bitzero.server.core.BZEvent;
import bitzero.server.core.IBZEvent;
import bitzero.server.entities.User;
import bitzero.server.extensions.BaseClientRequestHandler;
import bitzero.server.extensions.IServerEventHandler;
import bitzero.server.extensions.data.DataCmd;
import bitzero.util.ExtensionUtility;
import bitzero.util.common.business.Debug;
import cmd.CmdDefine;
import cmd.receive.demo.ChangePosition;
import cmd.receive.demo.RequestSetName;
import cmd.send.demo.*;
import event.eventType.DemoEventParam;
import event.eventType.DemoEventType;
import model.PlayerInfo;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.server.ServerConstant;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DemoHandler extends BaseClientRequestHandler implements IServerEventHandler {
    
    public static short DEMO_MULTI_IDS = 2000;

    /**
     * log4j level
     * ALL < DEBUG < INFO < WARN < ERROR < FATAL < OFF
     */

    private final Logger logger = LoggerFactory.getLogger("DemoHandler");
    
    public DemoHandler() {
        super();
    }

    /**
     *  this method automatically loaded when run the program
     *  register new event, so the core will dispatch event type to this class
     */
    public void init() {
        getParentExtension().addEventListener(DemoEventType.LOGIN_SUCCESS, this);
    }

    @Override
    /**
     * this method handle all client requests with cmdId in range [1000:2999]
     *
     */
    public void handleClientRequest(User user, DataCmd dataCmd) {
        System.out.println(dataCmd.getId());
        try {
            switch (dataCmd.getId()) {
                case CmdDefine.GET_USER_INFO:
                    processGetUserInfo(user);
                    break;
                case CmdDefine.SET_NAME:
                    RequestSetName set = new RequestSetName(dataCmd);
                    processSetName(set, user);
                    break;
                case CmdDefine.MOVE:
                    break;
                case CmdDefine.RESET_MAP:
                    break;
                default:
                    break;
            }

        } catch (Exception e) {
            logger.warn("DEMO HANDLER EXCEPTION " + e.getMessage());
            logger.warn(ExceptionUtils.getStackTrace(e));
        }

    }

    /**
     * events will be dispatch here
     */
    public void handleServerEvent(IBZEvent ibzevent) {        
        if (ibzevent.getType() == DemoEventType.LOGIN_SUCCESS) {
            this.processUserLoginSuccess((User)ibzevent.getParameter(DemoEventParam.USER), (String)ibzevent.getParameter(DemoEventParam.NAME));
        }
    }


    
    private void processUserLoginSuccess(User user, String name){
        /**
         * process event
         */
        processSendUpdateStatusToUser(user);
    }

    private void processSendUpdateStatusToUser(User user){
    }

    private void processGetUserInfo(User user){
        System.out.println("Demohandler"+"processGetUserInfo");
        try{
            PlayerInfo userInfo = (PlayerInfo) user.getProperty(ServerConstant.PLAYER_INFO);
            if (userInfo==null){
                logger.info("PlayerInfo null");
                send(new ResponseGetName(DemoError.PLAYERINFO_NULL.getValue(), ""), user);
            }
            logger.info("get name = " + userInfo.toString());
            send(new ResponseRequestUserInfo(DemoError.SUCCESS.getValue(), userInfo), user);
        }catch(Exception e){
            logger.info("processGetName exception");
            send(new ResponseGetName(DemoError.EXCEPTION.getValue(), ""), user);
        }
    }

    private void processSetName(RequestSetName requestSet, User user){
        try{
            PlayerInfo userInfo = (PlayerInfo) user.getProperty(ServerConstant.PLAYER_INFO);
            if (userInfo==null)
                send(new ResponseSetName(DemoError.PLAYERINFO_NULL.getValue(), ""), user);
            String name = userInfo.setName(requestSet.getName());
            send(new ResponseSetName(DemoError.SUCCESS.getValue(), name), user);
            logger.info("set new name = " + name);
            /**
             * dispatch event for another handler
             */
            Map evtParams = new HashMap();
            evtParams.put(DemoEventParam.USER, user);
            evtParams.put(DemoEventParam.NAME, requestSet.getName());
            ExtensionUtility.dispatchEvent(new BZEvent(DemoEventType.CHANGE_NAME, evtParams));
        }catch(Exception e){
            send(new ResponseSetName(DemoError.EXCEPTION.getValue(), ""), user);
        }
    }

    public enum DemoError{
        SUCCESS((short)0),
        ERROR((short)1),
        PLAYERINFO_NULL((short)2),
        EXCEPTION((short)3),
        INVALID_PARAM((short)4),
        VISITED((short)5),;

        
        private final short value;
        private DemoError(short value){
            this.value = value;
        }
        
        public short getValue(){
            return this.value;
        }
    }
}
