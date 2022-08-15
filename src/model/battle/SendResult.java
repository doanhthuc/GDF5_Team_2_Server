package model.battle;

import battle.config.GameConfig;
import bitzero.server.BitZeroServer;
import bitzero.server.entities.User;
import bitzero.util.ExtensionUtility;
import cmd.send.battle.ResponseEndBattle;
import match.UserType;
import model.Lobby.UserLobbyChest;
import model.Lobby.LobbyChestDefine;
import model.PlayerInfo;
import service.RoomHandler;
import util.server.ServerConstant;

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

        User winUser = BitZeroServer.getInstance().getUserManager().getUserById(winUserID);
        PlayerInfo winUserInfo = (PlayerInfo) PlayerInfo.getModel(winUserID, PlayerInfo.class);
        UserLobbyChest winUserLobbyChest = (UserLobbyChest) UserLobbyChest.getModel(winUserID, UserLobbyChest.class);

        if (winUserInfo.getUserType() == UserType.PLAYER) {
            winUserInfo = (PlayerInfo) winUser.getProperty(ServerConstant.PLAYER_INFO);
            winUserLobbyChest = (UserLobbyChest) winUser.getProperty(ServerConstant.LOBBY_CHEST);
        }

        synchronized (winUserInfo) {
            synchronized (winUserLobbyChest) {
                if (winUserLobbyChest.haveEmptySlot()) {
                    winUserLobbyChest.addLobbyChest();
                    winUserLobbyChest.saveModel(winUserID);
                    if (winUserInfo.getUserType() == UserType.PLAYER)
                        ExtensionUtility.getExtension().send(new ResponseEndBattle(RoomHandler.RoomError.END_BATTLE.getValue(), GameConfig.BATTLE_RESULT.WIN, winnerHP, loserHP, winUserInfo.getTrophy(), 10, 1), winUser);
                } else if (winUserInfo.getUserType() == UserType.PLAYER)
                    ExtensionUtility.getExtension().send(new ResponseEndBattle(RoomHandler.RoomError.END_BATTLE.getValue(), GameConfig.BATTLE_RESULT.WIN, winnerHP, loserHP, winUserInfo.getTrophy(), 10, 0), winUser);
            }
            winUserInfo.setTrophy(winUserInfo.getTrophy() + GameConfig.BATTLE.WINNER_TROPHY);
            winUserInfo.saveModel(winUserID);
        }


        User loseUser = BitZeroServer.getInstance().getUserManager().getUserById(loseUserID);
        PlayerInfo loseUserInfo = (PlayerInfo) PlayerInfo.getModel(loseUserID, PlayerInfo.class);

        if (loseUserInfo.getUserType() == UserType.PLAYER) {
            loseUserInfo = (PlayerInfo) loseUser.getProperty(ServerConstant.PLAYER_INFO);
        }
        synchronized (loseUserInfo) {
            if (loseUserInfo.getUserType() == UserType.PLAYER)
                ExtensionUtility.getExtension().send(new ResponseEndBattle(RoomHandler.RoomError.END_BATTLE.getValue(), GameConfig.BATTLE_RESULT.LOSE, loserHP, winnerHP, loseUserInfo.getTrophy(), -10, 0), loseUser);
            loseUserInfo.setTrophy(loseUserInfo.getTrophy() - GameConfig.BATTLE.LOSER_TROPHY);
            loseUserInfo.saveModel(loseUserID);
        }
        return;
    }
}
