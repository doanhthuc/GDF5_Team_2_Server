package cmd.send.room;

import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;
import model.Inventory.Card;
import model.PlayerInfo;
import model.battle.PlayerInBattle;
import model.battle.Room;

import java.nio.ByteBuffer;

public class ResponseRequestGetRoomInfo extends BaseMsg {
    public short error;
    private Room room;
    private int opponentId;

    public ResponseRequestGetRoomInfo(short error) {
        super(CmdDefine.ENTER_ROOM);
        this.error = error;
    }

    public ResponseRequestGetRoomInfo(short _error, Room room, int opponentId) {
        super(CmdDefine.ENTER_ROOM);
        this.room = room;
        this.opponentId = opponentId;
        error = _error;
    }

    public short getError() {
        return error;
    }

    public void setError(short error) {
        this.error = error;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public byte[] createData() {
        ByteBuffer bf = makeBuffer();

        return packBuffer(bf);
    }
}
