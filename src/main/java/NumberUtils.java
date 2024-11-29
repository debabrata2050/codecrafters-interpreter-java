public class NumberUtils {
    public static String removeTrailingZeroes(String s) {
        int index;
        for (index = s.length() - 1; index >= 0; index--) {
            if (s.charAt(index) != '0') {
                break;
            }
        }
        if (index == 0 && s.charAt(index) == '.') {
            return "";
        }
        return s.substring(0, index + 1);
    }
} 