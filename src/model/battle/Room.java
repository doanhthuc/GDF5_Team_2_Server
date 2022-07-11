package model.battle;

import battle.Battle;
import bitzero.server.BitZeroServer;
import model.PlayerInfo;

import java.util.concurrent.TimeUnit;

public class Room implements Runnable{
    private final int roomId;
    PlayerInBattle player1;
    PlayerInBattle player2;
    Battle battle;
    public Room(PlayerInfo player1, PlayerInfo player2) {
        this.roomId = RoomManager.getInstance().getRoomCount();
        this.player1 = new PlayerInBattle(player1);
        this.player2 = new PlayerInBattle(player2);
    }

    public Room(PlayerInfo player1, PlayerInfo player2, Battle battle1, Battle battle2) {
        this.roomId = RoomManager.getInstance().getRoomCount();
        this.player1 = new PlayerInBattle(player1, battle1);
        this.player2 = new PlayerInBattle(player2, battle2);
    }

    @Override
    public void run() {
        System.out.println("Hello AAAA");
        this.battle.attackSystem.run();
    }

    public PlayerInfo getOpponentPlayer(int playerId) {
        if (playerId == player1.getId()) {
            return player1;
        } else {
            return player2;
        }
    }

    public PlayerInBattle getMyPlayerInBattle(int opponentId) {
        if (opponentId == player1.getId()) {
            return player2;
        } else {
            return player1;
        }
    }

    public int getRoomId() {
        return roomId;
    }

    public PlayerInBattle getPlayer1() {
        return player1;
    }

    public void setPlayer1(PlayerInBattle player1) {
        this.player1 = player1;
    }

    public PlayerInBattle getPlayer2() {
        return player2;
    }

    public void setPlayer2(PlayerInBattle player2) {
        this.player2 = player2;
    }
}
