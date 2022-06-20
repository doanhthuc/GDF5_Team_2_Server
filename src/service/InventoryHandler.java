package service;

import bitzero.server.BitZeroServer;
import bitzero.server.core.BZEventParam;
import bitzero.server.core.BZEventType;
import bitzero.server.core.IBZEvent;
import bitzero.server.entities.User;
import bitzero.server.extensions.BaseClientRequestHandler;
import bitzero.server.extensions.data.DataCmd;
import cmd.CmdDefine;
import cmd.send.inventory.ResponseRequestGetUserInventory;
import cmd.send.user.*;

import event.eventType.DemoEventParam;
import event.eventType.DemoEventType;
import extension.FresherExtension;
import model.Inventory.CardCollection;
import model.PlayerInfo;
import model.Shop.ItemList.DailyItemList;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.server.ServerConstant;

import java.util.List;

public class InventoryHandler extends BaseClientRequestHandler {
    public static short INVENTORY_MULTI_IDS = 3000;
    private final Logger logger = LoggerFactory.getLogger("UserHandler");

    public InventoryHandler() {
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
            this.userChangeName((User) ibzevent.getParameter(DemoEventParam.USER), (String)ibzevent.getParameter(DemoEventParam.NAME));
    }

    public void handleClientRequest(User user, DataCmd dataCmd) {
        try {
            switch (dataCmd.getId()) {
                case CmdDefine.GET_USER_INVENTORY:
                    processGetUserInventory(user);
                    break;
            }
        } catch (Exception e) {
            logger.warn("USERHANDLER EXCEPTION " + e.getMessage());
            logger.warn(ExceptionUtils.getStackTrace(e));
        }

    }

    private void processGetUserInventory(User user){
        System.out.println("InventoryHandler "+" processGetUserInventory");
        try{
            PlayerInfo userInfo = (PlayerInfo) user.getProperty(ServerConstant.PLAYER_INFO);
            if (userInfo==null){
                logger.info("PlayerInfo null");
                //send(new ResponseGetName(DemoHandler.DemoError.PLAYERINFO_NULL.getValue(), ""), user);
            }
            logger.info("get inventoryID " + userInfo.getId());
            CardCollection usercardCollection= (CardCollection) CardCollection.getModel(userInfo.getId(), CardCollection.class);
            usercardCollection.show();
            send(new ResponseRequestGetUserInventory(InventoryHandler.InventoryError.SUCCESS.getValue(), usercardCollection), user);
        }catch(Exception e){
            logger.info("processGetName exception");
            //send(new ResponseGetName(DemoHandler.DemoError.EXCEPTION.getValue(), ""), user);
        }
    }
    private void userDisconnect(User user) { }
    private void userChangeName(User user, String name){
        List<User> allUser = BitZeroServer.getInstance().getUserManager().getAllUsers();
        for(User aUser : allUser){
        }
    }
    public enum InventoryError{
        SUCCESS((short)0),
        ERROR((short)1),
        PLAYERINFO_NULL((short)2),
        EXCEPTION((short)3),
        INVALID_PARAM((short)4),
        VISITED((short)5),;


        private final short value;
        private InventoryError(short value){
            this.value = value;
        }

        public short getValue(){
            return this.value;
        }
    }
}
