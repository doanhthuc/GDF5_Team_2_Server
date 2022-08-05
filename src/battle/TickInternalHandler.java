package battle;

import battle.common.EntityMode;
import bitzero.server.entities.User;
import bitzero.server.extensions.data.DataCmd;
import cmd.CmdDefine;
import cmd.receive.battle.spell.RequestDropSpell;
import cmd.receive.battle.tower.RequestPutTower;
import model.battle.Room;
import model.battle.RoomManager;

public class TickInternalHandler {
    public TickInternalHandler() {

    }

    public void handleCommand(User user, DataCmd dataCmd) throws Exception {
        switch (dataCmd.getId()) {
            case CmdDefine.PUT_TOWER: {
                RequestPutTower req = new RequestPutTower(dataCmd);
                Room room = RoomManager.getInstance().getRoom(req.getRoomId());
                EntityMode entityMode = room.getBattle().getEntityModeByPlayerID(user.getId());
                room.getBattle().buildTowerByTowerID(req.getTowerId(), req.getTilePos().x, req.getTilePos().y, entityMode);
                System.out.println("AAAAAAAAAAAA Handle put tower internal");
                break;
            }
            case CmdDefine.DROP_SPELL: {
                RequestDropSpell req = new RequestDropSpell(dataCmd);
                Room room = RoomManager.getInstance().getRoom(req.getRoomId());
                EntityMode entityMode = room.getBattle().getEntityModeByPlayerID(user.getId());
                room.getBattle().castSpellBySpellID(req.getSpellId(), req.getPixelPos().x, req.getPixelPos().y, entityMode);
                System.out.println("AAAAAAAAAAAA Handle dropSpell internal");

            }
        }
    }
}
