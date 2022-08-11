package util.validate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateString {
    public static boolean isContainSpecialChar(String str) {
        Pattern p = Pattern.compile("[^A-Za-z0-9]");
        Matcher m = p.matcher(str);
        return m.find();
    }
}
