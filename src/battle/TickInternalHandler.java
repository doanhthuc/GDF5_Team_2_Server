package battle;

import bitzero.server.entities.User;
import bitzero.server.extensions.data.DataCmd;
import cmd.CmdDefine;
import cmd.receive.battle.tower.RequestPutTower;

public class TickInternalHandler {
    public TickInternalHandler () {

    }

    public void handleCommand (User user, DataCmd dataCmd) {
        switch (dataCmd.getId()) {
            case CmdDefine.PUT_TOWER: {
                System.out.println("AAAAAAAAAAAA Handle put tower internal");
                break;
            }
        }
    }
}
