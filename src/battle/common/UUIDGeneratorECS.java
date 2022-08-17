package battle.common;

public class UUIDGeneratorECS {
    private  long entityID = 0;
    private  long componentID = 0;
    private  long systemID = 0;

    public UUIDGeneratorECS() {

    }

    public  long genEntityID() {
        return ++entityID;
    }

    public  long genComponentID() {
        return ++componentID;
    }

    public  long genSystemID() {
        return ++systemID;
    }
}
