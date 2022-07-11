package cmd.obj.matching;

public class OpponentInfo {
    private int id;
    private String username;
    private int trophy;

    public OpponentInfo(int id, String username, int trophy) {
        this.id = id;
        this.username = username;
        this.trophy = trophy;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public int getTrophy() {
        return trophy;
    }
}
