package org.hellscode.util;

public class StringUtil {
    public static boolean areEqual(String s1, String s2) {
        boolean retVal = false;

        if (s1 == null && s2 == null) {
            retVal = true;
        } else if (s1 != null && s1.equals(s2)) {
            retVal = true;
        }

        return retVal;
    }
}
