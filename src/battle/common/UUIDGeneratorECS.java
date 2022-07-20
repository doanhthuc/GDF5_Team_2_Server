package battle.common;

public class UUIDGeneratorECS {
    private static long entityID = 0;
    private static long componentID = 0;
    private static long systemID = 0;

    public static long genEntityID() {
        return ++entityID;
    }

    public static long genComponentID() {
        return ++componentID;
    }

    public static long genSystemID() {
        return ++systemID;
    }
}
