/*************************************************************************
 * 2012-7-25   chenshiqiang    1.0    创建
 **************************************************************************/
package szz.com.thirldworldv2;

public class SpUtils {

    private static final String SP_ACCOUNT = "SP_ACCOUNT";

    public static void saveAccount(String account){
        saveString(SP_ACCOUNT, account);
    }

    public static String getAccount(){
        return getString(SP_ACCOUNT,"");
    }

    private static final String SP_PWD = "SP_PWD";

    public static void savePwd(String account){
        saveString(SP_PWD, account);
    }

    public static String getPwd(){
        return getString(SP_PWD,"");
    }

    private static final String SP_SERVER = "SP_SERVER";

    public static void saveServer(String account){
        saveString(SP_SERVER, account);
    }

    public static String getServer(){
        return getString(SP_SERVER,"183.60.204.64");
    }

    private static final String SP_PORT = "SP_PORT";

    public static void savePort(String account){
        saveString(SP_PORT, account);
    }

    public static String getPort(){
        return getString(SP_PORT,"9103");
    }


    private static final String PREFS_NAME = "com.szz.thirdworld.prefs";


    public static boolean saveInt(String key, int value) {
        return saveInt(key, value, PREFS_NAME);
    }

    public static boolean saveInt(String key, int value, String preferenceName) {
        return SharedPreferenceUtil.saveInt(ContextHolder.getContext(), key, value, preferenceName);
    }

    public static int getInt(String key, int defValue) {
        return getInt(key, defValue, PREFS_NAME);
    }

    public static int getInt(String key, int defValue, String preferenceName) {
        return SharedPreferenceUtil.getInt(ContextHolder.getContext(), key, defValue, preferenceName);
    }

    public static boolean saveLong(String key, long value) {
        return saveLong(key, value, PREFS_NAME);
    }

    public static boolean saveLong(String key, long value, String preferenceName) {
        return SharedPreferenceUtil.saveLong(ContextHolder.getContext(), key, value, preferenceName);
    }

    public static long getLong(String key, long defValue) {
        return getLong(key, defValue, PREFS_NAME);
    }

    public static long getLong(String key, long defValue, String preferenceName) {
        return SharedPreferenceUtil.getLong(ContextHolder.getContext(), key, defValue, preferenceName);
    }

    public static boolean saveBoolean(String key, boolean value) {
        return saveBoolean(key, value, PREFS_NAME);
    }

    public static boolean saveBoolean(String key, boolean value, String preferenceName) {
        return SharedPreferenceUtil.saveBoolean(ContextHolder.getContext(), key, value, preferenceName);
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return getBoolean(key, defValue, PREFS_NAME);
    }

    public static boolean getBoolean(String key, boolean defValue, String preferenceName) {
        return SharedPreferenceUtil.getBoolean(ContextHolder.getContext(), key, defValue, preferenceName);
    }

    public static boolean saveString(String key, String value) {
        return saveString(key, value, PREFS_NAME);
    }

    public static boolean saveString(String key, String value, String preferenceName) {
        return SharedPreferenceUtil.saveString(ContextHolder.getContext(), key, value, preferenceName);
    }

    public static String getString(String key, String defValue) {
        return getString(key, defValue, PREFS_NAME);
    }

    public static String getString(String key, String defValue, String preferenceName) {
        return SharedPreferenceUtil.getString(ContextHolder.getContext(), key, defValue, preferenceName);
    }

    public static boolean remove(String key) {
        return remove(key, PREFS_NAME);
    }

    public static boolean remove(String key, String preferenceName) {
        return SharedPreferenceUtil.remove(ContextHolder.getContext(), key, preferenceName);
    }
}

