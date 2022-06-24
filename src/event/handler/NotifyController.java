package event.handler;

import bitzero.server.BitZeroServer;
import bitzero.server.core.IBZEvent;
import bitzero.server.entities.User;
import bitzero.server.exceptions.BZException;
import bitzero.server.extensions.BaseServerEventHandler;
import bitzero.util.common.business.Debug;
import event.eventType.DemoEventParam;
import event.eventType.DemoEventType;


import java.awt.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class NotifyController extends BaseServerEventHandler {
    @Override
    public void handleServerEvent(IBZEvent ibzEvent) throws BZException {

//        if (ibzEvent.getType() == DemoEventType.CHANGE_POSITION)
//            processChangePosition((User) ibzEvent.getParameter(DemoEventParam.USER),
//                    (Point) ibzEvent.getParameter(DemoEventParam.POSITION));
    }

    private void processChangePosition(User user, Point pos){
        List<User> allUser = BitZeroServer.getInstance().getUserManager().getAllUsers();
        for(User aUser : allUser){
            // notify user's change

        }
    }
}
