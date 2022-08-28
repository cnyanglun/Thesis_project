package util;

public class commonUtil {
    public static String compareStrings(String s1, String s2) {

        int comparedResult = s1.compareTo(s2);
        String result = null;

        if (comparedResult > 0) {
            result = s2 + "_" + s1;
        } else if (comparedResult < 0) {
            result = s1 + "_" + s2;
        } else {
            result = s1 + "_" + s2;
        }

        return result;
    }

}
