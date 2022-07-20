package battle.component.Component;

<<<<<<< HEAD:src/battle/Component/Component/PathComponent.java
import battle.Common.Point;
import battle.Common.Utils;
import battle.Config.GameConfig;
import battle.Factory.ComponentFactory;
=======
import battle.common.Point;
import battle.config.GameConfig;
>>>>>>> master:src/battle/component/Component/PathComponent.java

import java.util.List;

public class PathComponent extends Component {
    private String name = "PathComponent";
    private List<Point> path;
    private int currentPathIDx;
    private int mode;

    public PathComponent(List<Point> pathTile, int mode, boolean isConvert) {
        super(GameConfig.COMPONENT_ID.PATH);
        this.reset(pathTile, mode, isConvert);
    }

    public void reset(List<Point> pathTile, int mode, boolean isConvert) {
        if (isConvert == true) {
            List<Point> pathTile2 = Utils.tileArray2PixelCellArray(pathTile, mode);
            this.path = pathTile2;
        } else {
            this.path = pathTile;
        }
        this.mode = mode;
        this.currentPathIDx = 0;
    }

    public PathComponent clone() {
        return ComponentFactory.getInstance().createPathComponent(this.path, this.mode);
    }

    public List<Point> getPath() {
        return path;
    }

    public void setPath(List<Point> path) {
        this.path = path;
    }

    public void setCurrentPathIDx(int currentPathIDx) {
        this.currentPathIDx = currentPathIDx;
    }

}