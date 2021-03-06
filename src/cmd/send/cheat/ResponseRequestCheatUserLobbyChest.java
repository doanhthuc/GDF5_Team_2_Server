package cmd.send.cheat;

import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;
import model.Lobby.LobbyChest;
import model.PlayerInfo;

import java.nio.ByteBuffer;

public class ResponseRequestCheatUserLobbyChest extends BaseMsg {
    public LobbyChest cheatLobbyChest;
    public int chestId;
    public short error;

    public ResponseRequestCheatUserLobbyChest(short _error, LobbyChest lobbyChest, int id) {
        super(CmdDefine.CHEAT_USER_LOBBY_CHEST);
        cheatLobbyChest = lobbyChest;
        this.chestId=id;
        error = _error;
    }
    public ResponseRequestCheatUserLobbyChest(short _error) {
        super(CmdDefine.CHEAT_USER_LOBBY_CHEST);
        error = _error;
    }

    @Override
    public byte[] createData() {
        ByteBuffer bf = makeBuffer();
        bf.putInt(chestId);
        bf.putInt(cheatLobbyChest.getState());
        bf.putLong(cheatLobbyChest.getClaimTime());
        return packBuffer(bf);
    }
}