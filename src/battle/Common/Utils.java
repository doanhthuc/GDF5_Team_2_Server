package battle.Common;


import battle.Config.GameConfig;

public class Utils {
    private static Utils _instance = null;

    public static class UUID {
        static int _instanceID = 0;
        static int _componentTypeID = 0;

        public static int genIncrementID() {
            return ++_instanceID;
        }
    }

    public static Utils getInstance() {
        if (_instance == null) _instance = new Utils();
        return _instance;
    }

    public Point calculateVelocityVector(Point startPos, Point targetPos, double speed) {
        double Xa = startPos.x, Ya = startPos.y, Xb = targetPos.x, Yb = targetPos.y;
        if (Xa - Xb == 0) return new Point(0, Math.signum((Yb - Ya) * speed));
        if (Ya - Yb == 0) return new Point(Math.signum((Xb - Xa) * speed), 0);
        double k = Math.abs(((Ya - Yb) / Xa - Xb));
        double speedX = Math.sqrt(speed * speed / (1 + k * k));
        double speedY = k * speedX;
        return new Point(Math.signum((Xb - Xa) * speedX), Math.signum((Yb - Ya) * speedY));
    }

    public static Point pixel2Tile(double xx, double yy, String mode) {
        if (mode == "") mode = GameConfig.PLAYER;
        if (mode == GameConfig.PLAYER) {
            int x = (int) Math.floor(xx / GameConfig.TILE_WIDTH);
            int y = (int) Math.floor(yy / GameConfig.TILE_HEIGHT);
            return new Point(x, y);
        }
        return null;
    }
    public static Point Tile2Pixel(){
      return null;
    };

    public static double euclidDistance(Point a, Point b)
    {
        return Math.sqrt(Math.pow(a.x-b.x,2)+Math.pow(a.y-b.y,2));
    }
}
