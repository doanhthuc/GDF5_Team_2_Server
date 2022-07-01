package battle.Common;

public class Utils {
    int _incrementID =0;
    public Utils(){
        this._incrementID=0;
    }
    public int genIncrementID(){
        return this._incrementID++;
    }
}
