package battle.Common;

public class Utils {
    public static class UUID {
        static int _instanceID =0;
        static int _componentTypeID =0;
        public static int genIncrementID(){
            return ++_instanceID;
        }
    }


}
