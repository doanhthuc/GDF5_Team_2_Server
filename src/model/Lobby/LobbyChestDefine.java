package model.Lobby;

public class LobbyChestDefine {
    public static final int NOT_OPENING_STATE = 0;
    public static final int OPENING_STATE = 1;
    public static final int CLAIMABLE_STATE = 2;
    public static final int EMPTY_STATE = 3;

    public static final int unlockTime = 3 * 3600 * 1000;
    public static final int MILLISECOND_PER_GEM = 600 * 1000;
}
