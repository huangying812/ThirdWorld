package szz.com.baselib;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作者：Ying.Huang on 2017/6/24 13:28
 * Version v1.0
 * 描述：
 */

public class parseUtil {

    public static Pattern NUM_REG = Pattern.compile("\\d+(,\\d{3})*");

    public static double str2Double(String str) {
        return Double.valueOf(str);
    }

    public static long str2Long(String str) {
        return Long.valueOf(str);
    }

    public static int str2Int(String intStr) {
        int integer = 0;
        try {
            Matcher m = NUM_REG.matcher(intStr);
            if (m.find()) {
                integer = Integer.valueOf(m.group());
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return integer;
    }
}
