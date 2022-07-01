package cmd.send.cheat;

import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;
import model.Inventory.Card;
import model.PlayerInfo;

import java.nio.ByteBuffer;

public class ResponseRequestCheatUserCard extends BaseMsg {
    public Card cheatCard;
    public short error;

    public ResponseRequestCheatUserCard(short _error, Card card) {
        super(CmdDefine.CHEAT_USER_CARD);
        this.cheatCard= card;
        error = _error;
    }
    public ResponseRequestCheatUserCard(short _error) {
        super(CmdDefine.CHEAT_USER_CARD);
        error = _error;
    }


    @Override
    public byte[] createData() {
        ByteBuffer bf = makeBuffer();
        bf.putShort(error);
        bf.putInt(this.cheatCard.getCardType());
        bf.putInt(this.cheatCard.getLevel());
        bf.putInt(this.cheatCard.getAmount());
        return packBuffer(bf);
    }
}