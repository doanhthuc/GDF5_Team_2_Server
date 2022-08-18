package battle.common;

public class UUIDGeneratorECS {
    private long entityID = 0;
    private long componentID = 0;
    private long systemID = 0;

    public UUIDGeneratorECS() {

    }

    public long genEntityID() {
        return ++entityID;
    }

    public synchronized long genComponentID() {
        long id = ++componentID;
        System.out.println("UUID generated: " + id);
        return id;
    }

    public long genSystemID() {
        return ++systemID;
    }
}
