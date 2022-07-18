package battle.component.Component;

import battle.common.Point;
import battle.config.GameConfig;

import java.util.ArrayList;

public class PathComponent extends Component {
    private String name = "PathComponent";
    private ArrayList<Point> path;
    private int currentPathIDx;

    public PathComponent(ArrayList<Point> path) {
        super(GameConfig.COMPONENT_ID.PATH);
        this.path = path;
        this.currentPathIDx = 0;
    }

    public ArrayList<Point> getPath() {
        return path;
    }

    public void setPath(ArrayList<Point> path) {
        this.path = path;
    }

    public int getCurrentPathIDx() {
        return currentPathIDx;
    }

    public void setCurrentPathIDx(int currentPathIDx) {
        this.currentPathIDx = currentPathIDx;
    }

    public void reset(ArrayList<Point> path) {
        this.path = path;
        this.currentPathIDx = 0;
    }
}