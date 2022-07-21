package cmd;

public enum HandlerId {
    USER((short)1000),
    SHOP((short)2000),
    INVENTORY((short)3000),
    LOBBY((short)4000),
    BATTLE((short)5000),
    CHEAT((short)7000),
    MATCHING((short)8000);


    private final short value;

    private HandlerId(short value){
        this.value = value;
    }

    public short getValue(){
        return this.value;
    }
}
