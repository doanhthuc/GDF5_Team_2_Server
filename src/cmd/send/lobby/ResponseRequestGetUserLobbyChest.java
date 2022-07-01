package cmd.send.lobby;

import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;
import model.Lobby.LobbyChestContainer;

import java.nio.ByteBuffer;

public class ResponseRequestGetUserLobbyChest extends BaseMsg {
    public LobbyChestContainer lcc;
    public short error;

    public ResponseRequestGetUserLobbyChest(short _error, LobbyChestContainer userLobbyChest) {
        super(CmdDefine.GET_USER_LOBBY_CHEST);
        this.lcc = userLobbyChest;
        error = _error;
    }
    public ResponseRequestGetUserLobbyChest(short _error) {
        super(CmdDefine.GET_USER_LOBBY_CHEST);
        error = _error;
    }


    @Override
    public byte[] createData() {
        ByteBuffer bf = makeBuffer();
        bf.putShort(error);
        bf.putInt(this.lcc.lobbyChestContainer.size());
        for (int i = 0; i < this.lcc.lobbyChestContainer.size(); i++) {
            int state = this.lcc.lobbyChestContainer.get(i).getState();
            long claimTime = this.lcc.lobbyChestContainer.get(i).getClaimTime();
            System.out.println(state + " " + claimTime);
            bf.putInt(state);
            bf.putLong(claimTime);
        }
        return packBuffer(bf);
    }
}