package battle.component.Component;

import battle.common.Point;
import battle.config.GameConfig;

import java.util.ArrayList;

public class PathComponent extends Component {
    public String name = "PathComponent";
    public ArrayList<Point> path;
    public int currentPathIDx;

    public PathComponent(ArrayList<Point> path) {
        super(GameConfig.COMPONENT_ID.PATH);
        this.path = path;
        this.currentPathIDx = 0;
    }

    public void reset(ArrayList<Point> path) {
        this.path = path;
        this.currentPathIDx = 0;
    }
}