package cmd.send.battle.player;

import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;
import model.Inventory.Card;

import java.nio.ByteBuffer;
import java.util.List;

public class ResponseBattleDeckInBattle extends BaseMsg {
    private final short _error;
    private final List<Card> battleDeck;

    public ResponseBattleDeckInBattle(short _error, List<Card> battleDeck) {
        super(CmdDefine.GET_BATTLE_DECK_IN_BATTLE);
        this._error = _error;
        this.battleDeck = battleDeck;
    }

    @Override
    public byte[] createData() {
        ByteBuffer bf = makeBuffer();
        bf.putInt(battleDeck.size());
        for (Card card : battleDeck) {
            int cardType = card.getCardType();
            int level = card.getLevel();
            int amount = card.getAmount();
            bf.putInt(cardType);
            bf.putInt(level);
            bf.putInt(amount);
        }
        return packBuffer(bf);
    }
}

