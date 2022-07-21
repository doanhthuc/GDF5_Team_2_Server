package cmd.send.lobby;

import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;
import model.Common.Item;
import model.Lobby.LobbyDTO;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class ResponseRequestClaimLobbyChest extends BaseMsg {
    public LobbyDTO lobbyDTO;
    public short error;

    public ResponseRequestClaimLobbyChest(short _error, LobbyDTO lobbyDTO) {
        super(CmdDefine.CLAIM_LOBBY_CHEST);
        this.lobbyDTO = lobbyDTO;
        error = _error;
    }
    public ResponseRequestClaimLobbyChest(short _error) {
        super(CmdDefine.CLAIM_LOBBY_CHEST);
        error = _error;
    }

    @Override
    public byte[] createData() {
        ByteBuffer bf = makeBuffer();
        bf.putInt(this.lobbyDTO.getChestId());
        bf.putInt(this.lobbyDTO.getState());
        bf.putInt(this.lobbyDTO.getGemchange());
        ArrayList<Item> reward = this.lobbyDTO.getReward();
        bf.putInt(reward.size());
        for (int i = 0; i < reward.size(); i++) {
            bf.putInt(reward.get(i).getItemType());
            bf.putInt(reward.get(i).getQuantity());
        }
        return packBuffer(bf);
    }
}
