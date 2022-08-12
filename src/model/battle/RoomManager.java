package model.battle;

import model.PlayerInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class RoomManager {
    private static final RoomManager instance = new RoomManager();
    private final Map<Integer, Room> roomMap;
    private static int roomCount = 0;

    private RoomManager() {
        roomMap = new HashMap<Integer, Room>();
    }

    public static RoomManager getInstance() {
        return instance;
    }

    public void addRoom(Room room) {
        roomMap.put(room.getRoomId(), room);
    }

    public void removeRoom(int roomId) {
        roomMap.remove(roomId);
    }

    public Room getRoom(int roomId) {
        return roomMap.get(roomId);
    }

    public Room getRoomByUserId(int userId) {
        for (Room room : roomMap.values()) {
            if (room.getPlayer1().getId() == userId || room.getPlayer2().getId() == userId) {
                return room;
            }
        }
        return null;
    }

    public void clear() {
        roomMap.clear();
    }

    public void removeRoom(Room room) {
        roomMap.remove(room.getRoomId());
    }

    public int getRoomCount() {
        return roomCount++;
    }

    public void clearRoom() {
        this.roomMap.clear();
    }


}
