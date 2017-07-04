/*************************************************************************
 * 2012-7-25   chenshiqiang    1.0    创建
 **************************************************************************/
package szz.com.baselib.application;


import szz.com.baselib.entity.ChuangGuanProf;

public class SpUtils {

    private static final String SP_ACCOUNT = "SP_ACCOUNT";

    public static void saveAccount(String account) {
        saveString(SP_ACCOUNT, account);
    }

    public static String getAccount() {
        return getString(SP_ACCOUNT, "hy812");
    }

    private static final String SP_PWD = "SP_PWD";

    public static void savePwd(String pwd) {
        saveString(SP_PWD, pwd);
    }

    public static String getPwd() {
        return getString(SP_PWD, "123");
    }

    private static final String SP_SERVER = "SP_SERVER";

    public static void saveServer(String serverIp) {
        saveString(SP_SERVER, serverIp);
    }

    public static String getServer() {
        return getString(SP_SERVER, "183.60.204.64");
    }

    private static final String SP_PORT = "SP_PORT";

    public static void savePort(String port) {
        saveString(SP_PORT, port);
    }

    public static String getPort() {
        return getString(SP_PORT, "9103");
    }

    private static final String SP_BASE_TASK_TYPE = "SP_BASE_TASK_TYPE";

    public static void saveLastBaseTaskType(String type) {
        saveString(getAccount() + SP_BASE_TASK_TYPE, type);
    }

    public static String getLastBaseTaskType() {
        return getString(getAccount() + SP_BASE_TASK_TYPE, "活力");
    }

    private static final String SP_BASE_TASK_LELEL = "SP_BASE_TASK_LELEL";

    public static void saveLastBaseTaskLevel(String level) {
        saveString(getAccount() + SP_BASE_TASK_LELEL, level);
    }

    public static String getLastBaseTaskLevel() {
        return getString(getAccount() + SP_BASE_TASK_LELEL, "大气");
    }

    private static final String SP_PU_TONG_GUAN_KA = "SP_PU_TONG_GUAN_KA";

    public static void savePuTongGuanka(int guanKa) {
        saveInt(getAccount() + SP_PU_TONG_GUAN_KA, guanKa);
    }

    public static int getPuTongGuanKa() {
        return getInt(getAccount() + SP_PU_TONG_GUAN_KA, 140);
    }

    private static final String SP_E_MENG_GUAN_KA = "SP_E_MENG_GUAN_KA";

    public static void saveEMengGuanka(int guanKa) {
        saveInt(getAccount() + SP_E_MENG_GUAN_KA, guanKa);
    }

    public static int getEMengGuanKa() {
        return getInt(getAccount() + SP_E_MENG_GUAN_KA, 85);
    }

    private static final String SP_JING_YING_GUAN_KA = "SP_JING_YING_GUAN_KA";

    public static void saveJingYingGuanka(int guanKa) {
        saveInt(getAccount() + SP_JING_YING_GUAN_KA, guanKa);
    }

    public static int getJingYingGuanKa() {
        return getInt(getAccount() + SP_JING_YING_GUAN_KA, 120);
    }

    private static final String SP_CHALLENGE_SS_TIMES = "SP_CHALLENGE_SS_TIMES";

    public static void saveChallengeSsTimes(int times) {
        saveInt(getAccount() + SP_CHALLENGE_SS_TIMES, times);
    }

    public static int getChallengeSsTimes() {
        return getInt(getAccount() + SP_CHALLENGE_SS_TIMES, 3);
    }

    private static final String SP_PU_TONG_GUAN_KA_TIMES = "SP_PU_TONG_GUAN_KA_TIMES";

    public static void savePuTongGuanKaTimes(int times) {
        saveInt(getAccount() + SP_PU_TONG_GUAN_KA_TIMES, times);
    }

    public static int getPuTongGuanKaTimes() {
        return getInt(getAccount() + SP_PU_TONG_GUAN_KA_TIMES, 48);
    }

    private static final String SP_JINGYING_GUAN_KA_TIMES = "SP_JINGYING_GUAN_KA_TIMES";

    public static void saveJingYingGuanKaTimes(int times) {
        saveInt(getAccount() + SP_JINGYING_GUAN_KA_TIMES, times);
    }

    public static int getJingYingGuanKaTimes() {
        return getInt(getAccount() + SP_JINGYING_GUAN_KA_TIMES, 48);
    }

    private static final String SP_EMENG_GUAN_KA_TIMES = "SP_EMENG_GUAN_KA_TIMES";

    public static void saveEMengGuanKaTimes(int times) {
        saveInt(getAccount() + SP_EMENG_GUAN_KA_TIMES, times);
    }

    public static int getEMengGuanKaTimes() {
        return getInt(getAccount() + SP_EMENG_GUAN_KA_TIMES, 48);
    }


    private static final String SP_12GONG_TIMES = "SP_12GONG_TIMES";

    public static void save12GongTimes(int times) {
        saveInt(getAccount() + SP_12GONG_TIMES, times);
    }

    public static int get12GongTimes() {
        return getInt(getAccount() + SP_12GONG_TIMES, 32);
    }

    private static final String SP_12GONG_RESET_TIMES = "SP_12GONG_RESET_TIMES";

    public static void save12GongResetTimes(int times) {
        saveInt(getAccount() + SP_12GONG_RESET_TIMES, times);
    }

    public static int get12GongResetTimes() {
        return getInt(getAccount() + SP_12GONG_RESET_TIMES, 2);
    }

    private static final String SP_12GONG_RESET_INDEX = "SP_12GONG_RESET_INDEX";

    public static void save12GongResetIndex(int index) {
        saveInt(getAccount() + SP_12GONG_RESET_INDEX, index);
    }

    public static int get12GongResetIndex() {
        return getInt(getAccount() + SP_12GONG_RESET_INDEX, 1);
    }

    private static final String SP_LAST_CHUANG_GUAN_PROF = "SP_LAST_CHUANG_GUAN_PROF";

    public static ChuangGuanProf getLastChuangGuanProf() {
        String type = getString(getAccount() + SP_LAST_CHUANG_GUAN_PROF, "精英");
        int index;
        int times;
        switch (type) {
            case "普通":
                index = getPuTongGuanKa();
                times = getPuTongGuanKaTimes();
                break;
            case "精英":
            default:
                index = getJingYingGuanKa();
                times = getJingYingGuanKaTimes();
                break;
            case "噩梦":
                index = getEMengGuanKa();
                times = getEMengGuanKaTimes();
                break;
        }
        return new ChuangGuanProf(type, index, times);
    }

    public static void saveLastChuangGuanProf(ChuangGuanProf prof) {
        saveString(getAccount() + SP_LAST_CHUANG_GUAN_PROF, prof.type);
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

