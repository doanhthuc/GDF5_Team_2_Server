package model;

import service.UserHandler;
import util.database.DataModel;

public class UserIncrementID extends DataModel {
    int incrementID;
    public UserIncrementID(){
        this.incrementID=0;
    }
    public int genIncrementID(){
        return ++this.incrementID;
    }
}
