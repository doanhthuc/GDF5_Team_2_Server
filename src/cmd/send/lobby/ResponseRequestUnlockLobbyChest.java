package cmd.send.lobby;

import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;
import model.Lobby.LobbyDTO;

import java.nio.ByteBuffer;

public class ResponseRequestUnlockLobbyChest extends BaseMsg {
    public LobbyDTO lobbyDTO;
    public short error;

    public ResponseRequestUnlockLobbyChest(short _error, LobbyDTO lobbyDTO) {
        super(CmdDefine.UNLOCK_LOBBY_CHEST);
        this.lobbyDTO = lobbyDTO;
        error = _error;
    }

    @Override
    public byte[] createData() {
        ByteBuffer bf = makeBuffer();
        bf.putShort(error);
        bf.putInt(this.lobbyDTO.getChestId());
        bf.putInt(this.lobbyDTO.getState());
        bf.putLong(this.lobbyDTO.getclaimTime());
        return packBuffer(bf);
    }
}