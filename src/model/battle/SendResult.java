package model.battle;

import battle.config.GameConfig;
import bitzero.server.BitZeroServer;
import bitzero.server.entities.User;
import bitzero.util.ExtensionUtility;
import cmd.send.battle.ResponseEndBattle;
import match.UserType;
import model.Lobby.LobbyChestContainer;
import model.Lobby.LobbyChestDefine;
import model.PlayerInfo;
import service.RoomHandler;

public class SendResult {

    public static void sendDrawBattle(int player1ID, int player2ID, int hp) throws Exception {
        User user1 = BitZeroServer.getInstance().getUserManager().getUserById(player1ID);
        User user2 = BitZeroServer.getInstance().getUserManager().getUserById(player2ID);
        PlayerInfo userInfo1 = (PlayerInfo) PlayerInfo.getModel(player1ID, PlayerInfo.class);
        PlayerInfo userInfo2 = (PlayerInfo) PlayerInfo.getModel(player2ID, PlayerInfo.class);
        if (userInfo1.getUserType() == UserType.PLAYER)
            ExtensionUtility.getExtension().send(new ResponseEndBattle(RoomHandler.RoomError.END_BATTLE.getValue(), GameConfig.BATTLE_RESULT.DRAW, hp, hp, userInfo1.getTrophy(), 0, 0), user1);
        if (userInfo2.getUserType() == UserType.PLAYER)
            ExtensionUtility.getExtension().send(new ResponseEndBattle(RoomHandler.RoomError.END_BATTLE.getValue(), GameConfig.BATTLE_RESULT.DRAW, hp, hp, userInfo2.getTrophy(), 0, 0), user2);
    }

    public static void sendWinUser(int winUserID, int loseUserID, int winnerHP, int loserHP) throws Exception {

        User user1 = BitZeroServer.getInstance().getUserManager().getUserById(winUserID);
        User user2 = BitZeroServer.getInstance().getUserManager().getUserById(loseUserID);
        PlayerInfo winUser = (PlayerInfo) PlayerInfo.getModel(winUserID, PlayerInfo.class);
        PlayerInfo loseUser = (PlayerInfo) PlayerInfo.getModel(loseUserID, PlayerInfo.class);
        if (loseUser.getUserType() == UserType.PLAYER)
            ExtensionUtility.getExtension().send(new ResponseEndBattle(RoomHandler.RoomError.END_BATTLE.getValue(), GameConfig.BATTLE_RESULT.LOSE, loserHP, winnerHP, loseUser.getTrophy(), -10, 0), user2);
        LobbyChestContainer winUserLobbyChest = (LobbyChestContainer) LobbyChestContainer.getModel(winUser.getId(), LobbyChestContainer.class);
        if (winUser.getUserType() == UserType.PLAYER) {
            if (winUserLobbyChest.lobbyChestContainer.size() < LobbyChestDefine.LOBBY_CHEST_AMOUNT) {
                winUserLobbyChest.addLobbyChest();
                winUserLobbyChest.saveModel(winUser.getId());
                ExtensionUtility.getExtension().send(new ResponseEndBattle(RoomHandler.RoomError.END_BATTLE.getValue(), GameConfig.BATTLE_RESULT.WIN, winnerHP, loserHP, winUser.getTrophy(), 10, 1), user1);
            } else
                ExtensionUtility.getExtension().send(new ResponseEndBattle(RoomHandler.RoomError.END_BATTLE.getValue(), GameConfig.BATTLE_RESULT.WIN, winnerHP, loserHP, winUser.getTrophy(), 10, 0), user1);
        }
        winUser.setTrophy(winUser.getTrophy() + GameConfig.BATTLE.WINNER_TROPHY);
        winUser.saveModel(winUser.getId());
        winUser.show();
        loseUser.setTrophy(loseUser.getTrophy() - GameConfig.BATTLE.LOSER_TROPHY);
        loseUser.saveModel(loseUser.getId());
        return;
    }
}
