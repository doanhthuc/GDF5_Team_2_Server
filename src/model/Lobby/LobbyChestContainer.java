package model.Lobby;

import cmd.CmdDefine;
import cmd.receive.shop.RequestBuyDailyShop;
import cmd.receive.shop.RequestBuyGoldShop;
import util.database.DataModel;

import java.util.ArrayList;

public class LobbyChestContainer extends DataModel {
    public ArrayList<LobbyChest> lobbyChestContainer = new ArrayList<LobbyChest>();
    public LobbyChestContainer(){
        this.lobbyChestContainer.add(new LobbyChest(LobbyChestDefine.NOT_OPENING_STATE));
        this.lobbyChestContainer.add(new LobbyChest(LobbyChestDefine.OPENING_STATE,10800*1000));
        this.lobbyChestContainer.add(new LobbyChest(LobbyChestDefine.CLAIMABLE_STATE));
    }
    public void addLobbyChest(LobbyChest lc)
    {
        if (this.lobbyChestContainer.size()>=4) return;
        this.lobbyChestContainer.add(lc);
    }
    public void show()
    {
        for(int i=0;i<this.lobbyChestContainer.size();i++)
        {
            switch (this.lobbyChestContainer.get(i).getState()) {
                case LobbyChestDefine.NOT_OPENING_STATE:
                    System.out.println("NOT OPENING");
                    break;
                case LobbyChestDefine.OPENING_STATE:
                    System.out.println("OPENING IN "+ this.lobbyChestContainer.get(i).getRemainingTime());
                    break;
                case LobbyChestDefine.CLAIMABLE_STATE:
                    System.out.println("CLAIMABLE");
                    break;
            }
        }
    }
}
