package model;

import util.database.DataModel;

public class PlayerID extends DataModel {
    public int userID;
    public String userIDStr;

    public PlayerID(int userID, String userIDStr) {
        this.userID = userID;
        this.userIDStr = userIDStr;
    }
}
