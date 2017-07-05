package szz.com.baselib.singleton;

import android.support.annotation.IntDef;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.Log;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import szz.com.baselib.EventUtil;
import szz.com.baselib.R;
import szz.com.baselib.application.ContextHolder;
import szz.com.baselib.application.SpUtils;
import szz.com.baselib.entity.GoldenHunterMonster;
import szz.com.baselib.entity.UserInfo;
import szz.com.baselib.entity.ZaHuoItem;
import szz.com.baselib.entity.events.GetServerPack;
import szz.com.baselib.entity.events.LogStr;
import szz.com.baselib.entity.events.LoginSuccess;
import szz.com.baselib.entity.events.MonsterRsp;
import szz.com.baselib.entity.events.ZaHuoItemRsp;
import szz.com.baselib.rest.QuanDi;
import szz.com.baselib.rest.ServerSocket;

import static szz.com.baselib.parseUtil.str2Int;

/**
 * 作者：Ying.Huang on 2017/6/16 13:15
 * Version v1.0
 * 描述：
 */

public class ConnectManager {

    private static final int PET_EXPLORE_TIMES_ZJ = 30;
    private static final int PET_EXPLORE_TIMES_YB = 30;
    private static final int PET_EXPLORE_TIMES_ZZ = 30;
    private static final int USE_HUO_LI_AMOUNT = 3000;
    private static final int USE_ZAN_ZHU_DIAN_AMOUNT = 2000;
    private static final int CHUANG_GUAN_TIMES_PT = 300;
    private static final int CHUANG_GUAN_TIMES_JY = 300;
    private static final int CHUANG_GUAN_TIMES_EM = 80;
    private static final int SHI_ER_GONG_TIMES = 120;
    private static final int SHEN_SHOU_TIMES = 100;
    private static final int GOLDEN_HUNTER_TIMES = 10;


    private static final ConnectManager ourInstance = new ConnectManager();
    private static final String TAG = "ConnectManager";
    public int mChallengeMaxTimes = 48;
    private ExecutorService mExecutorService;
    private String account = SpUtils.getAccount();
    private String pwd = SpUtils.getPwd();

    private ServerSocket mServerSocket;
    private UserInfo mInfo;
    private @AutoType int mCruCmd = -1;
    private int puTongGuanka = SpUtils.getPuTongGuanKa();
    private int jingYingGuanka = SpUtils.getJingYingGuanKa();
    private int eMengGuanka = SpUtils.getEMengGuanKa();
    private int maxChallengeSsTimes = SpUtils.getChallengeSsTimes();
    private int Max12GongTimes = SpUtils.get12GongTimes();
    private int mBaseTaskType;
    private int mBaseTaskLevel;
    private int challengeGuanTimes;
    private String challengeGuanType;
    private int challengeGuanIndex;


    public static ConnectManager getInstance() {
        return ourInstance;
    }

    private ConnectManager() {
        getSocket();
    }

    public void postConnectState() {
        getSocket().postConnectState();
    }

    public ServerSocket getSocket() {
        if (mExecutorService == null || mExecutorService.isShutdown()) {
            mExecutorService = Executors.newFixedThreadPool(4);
        }
        if (mServerSocket == null || !mServerSocket.isConnected()) {
            EventUtil.register(this);
            mServerSocket = new ServerSocket();
            mExecutorService.execute(mServerSocket);
        }
        return mServerSocket;
    }

    public void release() {
        if (mExecutorService != null) {
            mExecutorService.shutdown();
            mExecutorService = null;
        }
        if (mServerSocket != null) {
            mServerSocket.release();
            mServerSocket = null;
            EventUtil.unregister(this);
        }
    }

    /*public void onClick(View v) {
        int index = 1;
        int shuliang = 4;
        switch (v.getId()) {

            case R.id.readzahuo:
                setCmd(R.string.zahuo_read);

                break;
            case R.id.goumaizahuo:
                setCmd(R.string.zahuo_buy);

                break;
            case R.id.chongzhizahuo:
                setCmd(R.string.zahuo_reset);

                break;

            case R.id.readjingsuishangcheng:
                setCmd(R.string.jingsuishangcheng_read);

                break;
            case R.id.goumaimengchongsuipian:
                setCmd(R.string.jingsuishangcheng_buy, index);

                break;
            case R.id.chongzhijingsuishangcheng:
                setCmd(R.string.jingsuishangcheng_reset);

                break;

            case R.id.shuaxinliewu:
                readHunterMonster();
                break;
            case R.id.tiaozhanlieren:
                challegeHunterMoster(index);
                break;

            case R.id.yuanbao:
                zanzhuBuyItems(ZanZhuType.yuanbao, shuliang);
                break;
            case R.id.yuansu:
                zanzhuBuyItems(ZanZhuType.yuansu, shuliang);
                break;
            case id.baoxiang:
                zanzhuBuyItems(ZanZhuType.baoCast, shuliang);
                break;
            case R.id.caixiang:
                zanzhuBuyItems(ZanZhuType.cailiaoCast, shuliang);
                break;
        }
    }*/

    public void resetBaseTask() {
        setCmd(R.string.jibenrenw_reset);
    }

    public void challengeBaseTask() {
        if (mBaseTaskLevel >= 3 || account.equals("hy815")) {
            setCmd(R.string.jibenrenwuchuangguan, mBaseTaskType, mBaseTaskLevel);
        }
    }

    public void challengeBaseTask(String typeStr, String level) {
        switch (typeStr) {//0,活力；1,神兽丹；2,五行元素；3,三界灵丹；
            case "活力":
            default:
                mBaseTaskType = 0;
                break;
            case "神兽丹":
                mBaseTaskType = 1;
                break;
            case "五行元素":
                mBaseTaskType = 2;
                break;
            case "三界灵丹":
                mBaseTaskType = 3;
                break;
        }
        switch (level) {//3,大气关；4,高端关；5,极品关；6,臻品关；7,上古关；
            case "大气关":
            default:
                mBaseTaskLevel = 3;
                break;
            case "高端关":
                mBaseTaskLevel = 4;
                break;
            case "极品关":
                mBaseTaskLevel = 5;
                break;
            case "臻品关":
                mBaseTaskLevel = 6;
                break;
            case "上古关":
                mBaseTaskLevel = 7;
                break;
        }
        challengeBaseTask();
    }

    public void challegeHunterMoster(int index) {
        setCmd(R.string.lieren_tiaozhan_boss, index);
    }

    public void readHunterMonster() {
        setCmd(R.string.lieren_shuaxin);
    }

    public void chaoHeiBuyHuoLi(int times) {
        if (mInfo == null) {
            login(2);
        } else {
            setCmd(R.string.chaoheigoumaihuoli, mInfo.mDanjia * times);
        }
    }

    public void read12Gong() {
        setCmd(R.string.shiergong_shuaxin);
    }

    public void challenge12Gong(int index) {
        setCmd(R.string.shiergong_tiaozhan, index);
    }

    public void reset12Gong(int index) {
        postMsg(R.string.ph_msg_reset_12gong,index);
        setCmd(R.string.shiergong_reset, index);
    }

    public void readShenShou() {
        readShenShou(1);
    }

    public void readShenShou(int page) {
        setCmd(R.string.shenshou_shuaxin, page);
    }

    private boolean onReadShenShouRsp(String[] split) {
        if (split.length >= 4) {
            //帮派神兽副本读取地图〓20103 20203 20303 20403 20503 20603 20703 20803 20903 21003〓1〓230
            //帮派神兽副本读取地图〓20106 20206 20306 20406 20506 20606 20706 20806 20906 21004〓1〓230
            //帮派神兽副本读取地图〓21104 21204 21303 21403 21503 21603 21703 21803 21903 22001〓2〓230
            String[] datas = split[1].trim().split(" ");
            int page = Integer.valueOf(split[2]);
            String maxGuan = split[3];
            for (String data : datas) {
                int index = Integer.valueOf(data.substring(2, 3));
                int times = Integer.valueOf(data.substring(3));
                if (index == 0) {
                    index = 10;
                }
                Log.d(TAG, ContextHolder.getFormat("神兽副本: 第%1$d页 第%2$d关 完成了%3$d次", page, index, times));
                if (times < maxChallengeSsTimes) {
                    challengeShenShou(page, index);
                    return true;
                } else {
                        /*index++;
                        if (index > 10) {
                            page++;
                            index = 1;
                        }
                        challengeShenShou(page, index);*/
                }
            }
            readShenShou(page + 1);
        }
        return false;
    }

    /**
     * 盟军神兽副本挑战开始〓hy814〓123〓神兽副本1-1	183.60.204.64	183.60.204.64
     * 帮派神兽副本挑战结束〓战胜3〓230〓201〓1〓1423〓1696〓57309〓1244.7626〓0 3 0 0〓无〓神兽副本1-1
     * 帮派神兽副本挑战结束〓战胜3〓230〓201〓2〓1423〓1702〓57309〓1244.7626〓0 3 0 0〓无〓神兽副本1-1
     * 帮派神兽副本挑战结束〓战胜3〓230〓201〓3〓1423〓1702〓57309〓1244.7626〓0 0 0 0〓兽魂_月狐 1；〓神兽副本1-1
     * 帮派神兽副本挑战结束〓战胜3〓230〓201〓4〓1423〓1702〓56309〓1244.7626〓0 0 0 0〓兽魂_月狐 1；〓神兽副本1-1
     * <p>
     * 盟军神兽副本挑战开始〓hy814〓123〓神兽副本1-2	183.60.204.64	183.60.204.64
     * 帮派神兽副本挑战结束〓战胜3〓230〓202〓1〓1423〓1699〓57309〓1244.7626〓0 3 0 0〓无〓神兽副本1-2
     */
    public void challengeShenShou(int page, int index) {
        postMsg(R.string.ph_msg_challenge_shenshou,page,index);
        setCmd(R.string.shenshou_tiaozhan_boss, page, index);
    }

    private void onChallengeShenShouRsp(String res, String[] split) {
        if (split.length >= 12) {
            //帮派神兽副本挑战结束〓战胜3〓230〓201〓1〓1423〓1696〓57309〓1244.7626〓0 3 0 0〓无〓神兽副本1-1
            //帮派神兽副本挑战结束〓战胜3〓230〓201〓2〓1423〓1702〓57309〓1244.7626〓0 3 0 0〓无〓神兽副本1-1
            //帮派神兽副本挑战结束〓战胜3〓230〓201〓3〓1423〓1702〓57309〓1244.7626〓0 0 0 0〓兽魂_月狐 1；〓神兽副本1-1
            //帮派神兽副本挑战结束〓战胜3〓230〓201〓4〓1423〓1702〓56309〓1244.7626〓0 0 0 0〓兽魂_月狐 1；〓神兽副本1-1
            String result = split[1];
            String guan = split[11];
            int page = Integer.valueOf(guan.substring(4, 5));
            //int index = Integer.valueOf(guan.substring(4, 5));
            String end = result.substring(0, 2);
            String stars;
            if (end.equals("战胜")) {
                stars = result.substring(2, 3);
                readShenShou(page);
                    /*int times = Integer.valueOf(split[4]);
                    String guanStr = split[11];
                    int page = Integer.valueOf(guanStr.substring(4, 5));
                    int index = Integer.valueOf(guanStr.substring(6));
                    if (times < maxChallengeSsTimes) {
                        challengeShenShou(page,index);
                    } else {
                        index++;
                        if (index > 10) {
                            page++;
                            index = 1;
                        }
                        challengeShenShou(page, index);
                    }*/
            } else {
                stars = "0";
                Log.e(TAG, "神兽挑战失败了：" + res);
            }
            postMsg(R.string.ph_msg_challenge_shenshou_rsp,page,guan, end, stars);
        } else {
            Log.e(TAG, "神兽挑战失败了：" + res);
        }
    }

    public void readZaHuo(){
        setCmd(R.string.read_zahuo);
    }

    private void onReadZaHuoRsp(String[] split) {
        if (split.length >= 2) {
            //盟军黑店随机杂货铺读取成功〓※增加活力 50 100 5000 10，兽魂_玄武 20 1000 100000 60，碎片_星魂_巨蟹 10 100 5000 10，潜能_神行+5 5 500 25000 50，潜能_血牛+4 5 300 15000 30，碎片_星魂_射手 3 100 5000 10，※增加活力 50 100 5000 10，碎片_星魂_双子 10 100 5000 10，碎片_星魂_天蝎 5 100 5000 10，兽魂_梼杌 20 500 50000 40，碎片_星魂_水瓶 3 100 5000 10，兽魂_青龙 20 1000 100000 60，宝箱_+2 3 50 2500 5，兽魂_混沌 20 500 50000 40，兽魂_白虎 20 1000 100000 60〓101000110000001〓7
            // 663 前6个是花费固定资金，中6个是花费元宝，最后3个是花费赞助点
            ArrayList<ZaHuoItem> zaHuoItems = new ArrayList<>();
            String[] items = split[1].split("，");
            String[] s = split[2].split("");
            String[] state = Arrays.copyOfRange(s, s.length - 15, s.length);
            Log.e(TAG, "杂货铺读取: " + state.length + Arrays.deepToString(state));
            int resetTimes = str2Int(split[3]);
            for (int i = 0; i < 6; i++) {
                ZaHuoItem item = ZaHuoItem.parse(i + 1, items[i], Integer.valueOf(state[i]), MoneyType.guDingZiJin);
                item.buyIfWorth();
                zaHuoItems.add(item);
            }
            for (int i = 6; i < 12; i++) {
                ZaHuoItem item = ZaHuoItem.parse(i + 1, items[i], Integer.valueOf(state[i]), MoneyType.yuanBao);
                item.buyIfWorth();
                zaHuoItems.add(item);
            }
            for (int i = 12; i < 15; i++) {
                ZaHuoItem item = ZaHuoItem.parse(i + 1, items[i], Integer.valueOf(state[i]), MoneyType.zanZhuDian);
                item.buyIfWorth();
                zaHuoItems.add(item);
            }
            if (!zaHuoItems.isEmpty()) {
                EventUtil.post(new ZaHuoItemRsp(zaHuoItems));
            }
        }
    }

    public void resetZaHuo(){
        setCmd(R.string.reset_zahuo);
    }

    public void buyZaHuo(int index){
        setCmd(R.string.buy_zahuo,index);
    }

    private void onBuyZaHuoRsp(String[] split) {
//盟军黑店随机杂货铺已经购买完成〓101000111000001〓1219〓3418〓3605〓3184〓8809〓165〓4〓0〓28〓3815〓442〓2201〓4447.989〓227435〓碎片_星魂_天蝎
//                                  购买状态    | -------------元神+1到+9----------------|  宝箱  火淬 赞助点  固定     元宝     购买物品
        if (split.length >= 12) {
        }
    }

    /**
     * guanStr 示例参数： 1-1     1-2     2-5     等
     * <p>
     * public void challengeShenShou(String guanStr) {
     * setCmd(R.string.shenshou_tiaozhan_boss2, guanStr);
     * }
     */

    public void challengeGuan(String type, int times, int index) {
        if (challengeGuanTimes > 0) {
            postMsg("别着急，还有关没闯完呢");
        } else {
            challengeGuanTimes = times;
            challengeGuanType = type;
            challengeGuanIndex = index;
        }
        challengeGuan();
    }

    public void challengeGuan() {
        int times;
        if (challengeGuanTimes > mChallengeMaxTimes) {
            challengeGuanTimes -= mChallengeMaxTimes;
            times = mChallengeMaxTimes;
        } else {
            times = challengeGuanTimes;
            challengeGuanTimes = 0;
        }
        setCmd(R.string.chuangguan, challengeGuanType, times, challengeGuanIndex);
    }

    private void onChallengeGuanRsp(String[] split) {
        if (split.length >= 6) {
            //帮派个人闯关结束〓866.0115000012〓139803 140098 139816 139837 139851〓25552〓战胜1〓112〓110〓普通〓489098〓1418 1677 3513 1705 275 0 0 0 0〓18809〓1〓备用13〓120866〓0 0 0 0 0 0 0 0 0 0 0 0 0 0〓无〓101〓42994〓93〓0
            //帮派个人闯关结束〓865.4514999738〓140214 140509 140227 140248 140262〓25554〓战胜1〓112〓110〓普通〓489102〓1418 1677 3513 1705 275 0 0 0 0〓18809〓3〓备用13〓120866〓0 0 0 0 0 0 0 0 0 0 0 0 0 0〓无〓101〓42994〓93〓0
            //帮派个人闯关结束〓865.2635000124〓140394 140689 140407 140428 140442〓25556〓战胜3〓112〓48〓普通〓489105〓1418 1677 3513 1705 275 0 0 0 0〓18809〓3〓备用13〓120866〓0 0 0 0 0 0 0 0 0 0 0 0 0 0〓无〓101〓42994〓93〓0
            String result = split[4];
            if (challengeGuanTimes > 0) {
                if (result.startsWith("战胜")) {
                } else {
                    challengeGuanIndex--;
                }
                challengeGuan();
            }
            //闯 %1$s 第 %2$s 关 %3$s 次 %4$s 了！
            String star = result.substring(2);
            if (TextUtils.isEmpty(star)) {
                star = "0";
            }
            postMsg(R.string.ph_challenge_guan_suc,split[7],split[6],split[11],result.substring(0,2), star,split[3]);
        }
    }

    public void postMsg(String msg) {
        postMsg(LogStr.newInstance(msg));
    }

    public void postMsg(LogStr event) {
        EventUtil.post(event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginSuccess(LoginSuccess event){
        account = event.account;
        pwd = event.password;
        login(2);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void update(GetServerPack event) {
        if (event != null) {
            String res = event.msg;
            String[] split = res.split("〓");
            if (split.length > 0) {
                boolean needRepeat = processResponse(res, split,event.cmd);
                if (auto) {
                    if (needRepeat) {
                        repeatCmd();
                    } else {
                        doNextAuto();
                    }
                }

            }
        }
    }

    /**
     *  赞助2014获取〓hy814〓123	183.60.204.64	183.60.204.64
     *  赞获〓hy814〓5※有时很忙〓4021447〓52379〓32376〓107421〓27132〓123304〓6000〓2198〓1〓232435〓928651 550874 550874 550874 550874〓3815〓2017-05-20〓999〓57484〓2017-05-20〓0〓2017-05-20〓111111000000000〓999〓2211〓2017-5-20〓2008〓4〓27〓0〓0
     *  赞助系统读取房产信息〓hy814〓123	183.60.204.64	183.60.204.64
     *  赞助系统读取房产信息返回〓1000007 56 37
     *  赞助系统读取房屋NPC〓hy814〓123〓1000007	183.60.204.64	183.60.204.64
     *  赞助系统读取房屋NPC〓储物箱 43 36 1000034 201；储物箱 44 36 1000035 201；储物箱 45 36 1000036 201；储物箱 43 37 1000037 201；储物箱 44 37 1000038 201；储物箱 45 37 1000039 201；仙子屋 43 39 1000041 202；美人屋 43 40 1000042 202
     *  赞助系统房屋查询存宠明细〓hy814〓123〓1000007〓1000041	183.60.204.64	183.60.204.64
     *  赞助查询房屋存宠NPC明细〓绿衣仙子 122964，绿衣仙子 122960，绿衣仙子 122962，绿衣仙子 122965，绿衣仙子 122966，绿衣仙子 122961，绿衣仙子 122963，
     *  赞助系统房屋新增绿衣仙子〓hy814〓123〓1000007〓1000041〓1	183.60.204.64	183.60.204.64
     *  赞助查询房屋存宠NPC明细〓绿衣仙子 123379，绿衣仙子 122964，绿衣仙子 122960，绿衣仙子 122962，绿衣仙子 122965，绿衣仙子 122966，绿衣仙子 122961，绿衣仙子 122963，〓2197
     *
     *  摆摊使用特殊〓hy811〓123〓100018203	183.60.204.64	183.60.204.64
     *  摆摊使用特殊〓hy811〓123〓100018208	183.60.204.64	183.60.204.64
     *  摆摊使用特殊〓hy811〓123〓100018209	183.60.204.64	183.60.204.64
     *  摆摊使用特殊〓hy811〓123〓100018210	183.60.204.64	183.60.204.64
     *  摆摊使用特殊〓hy811〓123〓100018211	183.60.204.64	183.60.204.64
     *  摆摊使用特殊〓hy811〓123〓100018212	183.60.204.64	183.60.204.64
     *  摆摊使用特殊〓hy811〓123〓100018213	183.60.204.64	183.60.204.64
     *  成功！阁下使用 [※红利金丹] 提取到了红利 [36]
     *
     *  摆摊刷新携带〓hy811〓123	183.60.204.64	183.60.204.64
     *  btxd〓*16044〓73119〓0〓0〓0〓3〓2〓0〓0〓01.※乾坤袋 100018200〓02.※百宝囊 100018201〓03.甘露酒 100018203〓04.甘露酒 100018208〓05.甘露酒 100018209〓06.甘露酒 100018210〓07.甘露酒 100018211〓08.甘露酒 100018212〓09.甘露酒 100018213〓10.甘露酒 100018214〓
     * */
    private boolean processResponse(String res, String[] split, String cmd) {
        boolean needRepeat = false;
        int gap;
        outer:
        switch (split[0]) {
            case "赞助查询房屋存宠NPC明细":
                if (split.length == 2) {
                    //赞助查询房屋存宠NPC明细〓绿衣仙子 122964，绿衣仙子 122960，绿衣仙子 122962，绿衣仙子 122965，绿衣仙子 122966，绿衣仙子 122961，绿衣仙子 122963，
                    int counts = 0;
                    String[] xianZis = split[1].split("，");
                    for (String xianZi : xianZis) {
                        if (xianZi.contains("绿衣仙子")) {
                            counts++;
                        }
                    }
                    // 赞助系统房屋新增绿衣仙子〓hy814〓123〓1000007〓1000041〓1	183.60.204.64	183.60.204.64
                    postMsg(String.format("房子（%1$s）里NPC（%2$s）中的仙子数量是%3$d", houseId, npcId, counts));
                    if (counts < 8) {
                        int i = 8 - counts;
                        setCmd(R.string.zanzhu_buy_xianzi, houseId, npcId, i);
                   } else {
                        postMsg("无需购买仙子");
                    }
                } else if (split.length >= 3) {
                    //赞助查询房屋存宠NPC明细〓绿衣仙子 123379，绿衣仙子 122964，绿衣仙子 122960，绿衣仙子 122962，绿衣仙子 122965，绿衣仙子 122966，绿衣仙子 122961，绿衣仙子 122963，〓2197
                    int counts = 0;
                    String[] xianZis = split[1].split("，");
                    for (String xianZi : xianZis) {
                        if (xianZi.contains("绿衣仙子")) {
                            counts++;
                        }
                    }
                    postMsg(String.format("购买完成，房子（%1$s）里NPC（%2$s）中的仙子数量是%3$d，赞助点还剩%4$s", houseId, npcId, counts,split[2]));
                }
                break;
            case "帮派读取无尽关卡返回":
                onReadWuJinRsp(split);
                break;
            case "帮派无尽关打败BOSS返回":
                onChallengeWuJinBossRsp(split);
                break;
            case "帮派副本十二宫重置成功":
                //帮派副本十二宫重置成功〓003232323200000000000000〓2866〓2
                onReset12GongRsp(split[2]);
            case "帮派副本十二宫地图":
                if (onRead12GongRsp(split)) {
                    break outer;
                }
                break;
            case "帮派副本十二宫挑战结束":
                onChallenge12GongRsp(res, split);
                break;
            case "盟军每日任务领奖完成":
                if (split.length >= 5) {
                    //盟军个人每日活力任务领取〓hy814〓123〓9	183.60.204.64	183.60.204.64
                    //盟军每日任务领奖完成〓33333333100〓9〓通用星魂Ⅰ〓1
                    readDaylyTask();
                }
                break;
            case "盟军个人每日活力任务查询":
                needRepeat = onReadDaylyTaskRsp(split, needRepeat);
                break;
            case "盟军每日任务累计次数奖励完成":
                onLingquDaylyRewardRsp(split);
                break;
            case "帮派个人资料返回":
                if (split.length >= 2) {
                    mInfo = UserInfo.parse(res);
                    mChallengeMaxTimes = 30 + mInfo.getZanZhuLevel() * 3;
                    postMsg(mInfo.breifToString());
                }
                break;
            case "盟军黑店盟宠探险完成":
                if (split.length >= 2) {
                    //盟军黑店盟宠探险完成〓粉衣仙子 〓1230.7695〓207819〓409〓140〓41〓50〓※盟宠经验水_少许〓40490〓2440
                    readDaylyTask();
                }
                break;
            case "盟军读取猎物成功":
                if (split.length >= 2) {
                    //盟军读取猎物成功〓上古.莽夫.方衅；4501843；250.00；3.75；5.00；2.50；；1；1223938387500；1223938387500；15600
                    //上古.血牛.薛押；1831883；750.00；1.25；5.00；2.50；；1；1494129037500；1494129037500；13200
                    //臻品.莽夫.陈降腔；901418；225.00；3.38；4.50；2.25；；1；220565554875；220565554875；11000
                    //高端.金刚.宋被；458631；175.00；0.88；10.50；1.75；；1；87283087500；87283087500；9000
                    //菜鸟.神行.单于捣；200580；100.00；0.50；2.00；3.00；；1；21813003000；21813003000；7200
                    //大气.血牛.田诱；105389；450.00；0.75；3.00；1.50；；1；51574421250；51574421250；5600
                    //菜鸟.金刚.彭卫；46898；100.00；0.50；6.00；1.00；；1；5100085500；5100085500；4200
                    //菜鸟.血牛.颛孙只俏；21352；300.00；0.50；2.00；1.00；；1；6965874000；6965874000；3000
                    //高端.金刚.东方究；9509；175.00；0.88；10.50；1.75；；1；1809556875；1809556875；2000
                    //极品.莽夫.孙冈；4824；200.00；3.00；4.00；2.00；；1；1049076000；1049076000；1200
                    //大气.血牛.朱币；2303；450.00；0.75；3.00；1.50；；1；1126710000；1126710000；600
                    //菜鸟.金刚.侯颜；1070；100.00；0.50；6.00；1.00；；1；116290500；116290500；200
                    //187.5	262.5	150	187.5
                    //187.5	187.5	150	262.5
                    //225	187.5	131.25	243.75
                    //262.5	225	93.75	206.25
                    //225	187.5	150	225
                    String[] monsters = split[1].split("\r\n");
                    ArrayList<GoldenHunterMonster> monsterItems = new ArrayList<>();
                    int count = 1;
                    for (String monster : monsters) {
                        GoldenHunterMonster hunterMonster = GoldenHunterMonster.parse(count++, monster);
                        postMsg(hunterMonster.toString());
                        monsterItems.add(hunterMonster);
                    }
                    if (!monsterItems.isEmpty()) {
                        EventUtil.post(new MonsterRsp(monsterItems));
                    }
                }
                break;
            case "帮派副本基本任务挑战结束":
                if (split.length >= 4) {
                    //盟军基本任务挑战开始〓hy815〓123〓303	183.60.204.64	183.60.204.64
                    //帮派副本基本任务挑战结束〓战败〓〓活力_大气关〓1
                    String result = split[1];
                    if (result.equals("战败")) {
                        postMsg(res);
                        mBaseTaskLevel--;
                    } else {
                        postMsg(split[2]+split[1]);
                    }
                    challengeBaseTask();
                }
                break;
            case "帮派个人购买活力成功":
                if (split.length >= 2) {
                    //帮派个人购买活力成功〓资金〓881.1315〓25198〓3
                    //帮派个人购买活力成功〓元宝〓121166〓25248〓3
                    //%2$s 购买活力成功!%2$s还剩:%3$d，当前活力点数：%4$d 购买次数还剩%1$d次
                    int timesRemain = str2Int(split[4]);
                    String method = split[1];
                    int resRemain = str2Int(split[2]);
                    int huoliAmount = str2Int(split[3]);
                    postMsg(R.string.ph_buy_huoli_suc, timesRemain,method, resRemain, huoliAmount);
                    if (method.equals("资金")) {
                        lingquItem(LingQuType.gudinggoumaihuoli);
                    } else {
                        lingquItem(LingQuType.yuanbaogoumaihuoli);
                    }
                }
                break;
            case "帮派个人闯关结束":
                onChallengeGuanRsp(split);
                break;
            case "盟军杂货铺重置":
                if (split.length >= 2) {
                    //
                    readZaHuo();
                }
                break;
            case "盟军黑店随机杂货铺读取成功":
                onReadZaHuoRsp(split);
                break;
            case "盟军黑店随机杂货铺已经购买完成":
                onBuyZaHuoRsp(split);
                break;
            case "":
                if (split.length >= 2) {
                    //
                }
                break;
            case "赞获":
                if (split.length >= 2) {
                    //赞助2014获取〓hy815〓123	183.60.204.64	183.60.204.64
                    //赞获〓hy815〓有时候很忙〓1731574〓7848〓0〓0〓0〓0〓0〓1505〓1〓436943〓593470 215988 215706 215727 215741〓22499〓2017-05-23〓0〓231〓2017-05-23〓0〓2017-05-20〓000000000000000〓0〓1100〓2017-5-20〓0〓0〓0〓0〓0
                }
                break;
            case "赞助点兑换元宝成功":
                //赞助点兑换元宝〓hy815〓123〓100	183.60.204.64	183.60.204.64
                //赞助点兑换元宝成功〓1405〓486943
            case "赞助点购买元素成功":
                //赞助点购买元素成功〓1388〓593770 216288 216006 216027 216041
            case "赞助点兑换材料箱子成功":
            case "赞助点兑换宝箱成功":
                if (split.length >= 2) {
                    String type = split[0].substring(5, split[0].length() - 2);
                    postMsg(String.format("赞助点兑换%3$s成功,当前%3$s数量 %1$s，剩余赞助点  %2$s",split[2],split[1],type));
                }
                break;
            case "赞助查询日消费累计":
                if (split.length >= 2) {
                    //赞助查询每日消费累计〓hy815	183.60.204.64	183.60.204.64
                    //赞助查询日消费累计〓hy815〓100〓0
                }
                break;
            case "赞助本日消费累计领奖完成":
                if (split.length >= 3) {
                    //赞助点每日消费累计领奖〓hy815	183.60.204.64	183.60.204.64
                    //赞助本日消费累计领奖完成〓100〓1
                    lingquItem(LingQuType.xiaofeifuli);
                }
                break;
            case "人获":
                //人获〓hy814〓3※有时很忙〓3096220〓979〓天仙[73]〓0〓36491〓0〓0〓0〓3912〓35330〓0〓108〓0
                if (mInfo == null) {
                    login(2);
                }
                break;
            case "帮派神兽副本读取地图":
                if (onReadShenShouRsp(split)) {

                    break outer;
                }
                break;
            case "帮派神兽副本挑战结束":
                onChallengeShenShouRsp(res, split);
                break;
            case "错误":
            default:
                postMsg(res);
                needRepeat = false;
                break;
        }
        return needRepeat;
    }

    private void onReset12GongRsp(String s) {
        mInfo.resetTimes12Gong++;
        int i = s.indexOf("00");
        String guan;
        switch (i / 2) {
            case 0:
            default:
                guan = "白羊宫";
                break;
            case 1:
                guan = "金牛宫";
                break;
            case 2:
                guan = "双子宫";
                break;
            case 3:
                guan = "巨蟹宫";
                break;
            case 4:
                guan = "狮子宫";
                break;
            case 5:
                guan = "处女宫";
                break;
            case 6:
                guan = "天秤宫";
                break;
            case 7:
                guan = "天蝎宫";
                break;
            case 8:
                guan = "射手宫";
                break;
            case 9:
                guan = "山羊宫";
                break;
            case 10:
                guan = "水瓶宫";
                break;
            case 11:
                guan = "双鱼宫";
                break;
        }
        postMsg(R.string.ph_reset_12gong,guan,mInfo.resetTimes12Gong+"");
    }

    private void postMsg(@StringRes int resId, Object... args) {
        postMsg(LogStr.newInstance(resId,args));
    }

    private void onLingquDaylyRewardRsp(String[] split) {
        if (split.length >= 2) {
            //盟军个人每日活力任务累计额外领取〓hy814〓123	183.60.204.64	183.60.204.64
            //盟军每日任务累计次数奖励完成〓3
            //盟军个人每日活力任务累计额外领取〓hy814〓123	183.60.204.64	183.60.204.64
            //盟军每日任务累计次数奖励完成〓4
            int times = Integer.valueOf(split[1]);
            if (times < 4) {
                lingquDaylyReward();
            } else {
                Log.e(TAG, "盟军每日任务累计次数奖励领取完成,共计 " + times + " 次");
            }
        }
    }

    private boolean onReadDaylyTaskRsp(String[] split, boolean needRepeat) {
        int gap;
        if (split.length >= 4) {
            //盟军个人每日活力任务查询〓hy814〓123	183.60.204.64	183.60.204.64
            //盟军个人每日活力任务查询〓33333333100〓40 30 30 4610 2158 338 1104 80 160 122 10〓2
            //盟军个人每日活力任务查询〓33333333333〓30 30 30 49971 2400 495 16140 80 256 111 10〓4
            char[] chars = split[1].toCharArray();
            String[] datas = split[2].trim().split(" ");
            int i = 0;
            int count = 0;
            middle:
            for (; i < chars.length; ) {
                int doneTimes = Integer.valueOf(datas[i]);
                char c = chars[i++];
                if ('3' != c) {
                    switch (i) {
                        case 1://盟宠集市普通探险	 	 	30
                            if (doneTimes >= PET_EXPLORE_TIMES_ZJ) {
                                lingquDaylyReward(i);
                            } else {
                                petExplore(MoneyType.guDingZiJin);
                                needRepeat = true;
                            }
                            break;
                        case 2://盟宠集市中级探险	 	 	30
                            if (doneTimes >= PET_EXPLORE_TIMES_YB) {
                                lingquDaylyReward(i);
                            } else {
                                petExplore(MoneyType.yuanBao);
                                needRepeat = true;
                            }
                            break;
                        case 3://盟宠集市高级探险	 	 	30
                            if (doneTimes >= PET_EXPLORE_TIMES_ZZ) {
                                lingquDaylyReward(i);
                            } else {
//                                Log.e(TAG, "update: 要赞助点萌宠探险，停止了");
                                petExplore(MoneyType.zanZhuDian);
                                needRepeat = true;
                            }
                            break;
                        case 4://使用活力	 	 	目标3000

                            if (doneTimes >= USE_HUO_LI_AMOUNT) {
                                lingquDaylyReward(i);
                            } else {
                                gap = USE_HUO_LI_AMOUNT - doneTimes;
                                int times = (int) Math.ceil(gap / 3f);
                                postMsg(R.string.ph_msg_dayly_task_huoli_unreach,doneTimes, gap, times);
//                                Log.e(TAG, "update: 要闯精英关卡，停止了，目标" + times + " 次 " + jingYingGuanka + " 关");
                                challengeGuan("精英", times, jingYingGuanka);
                            }
                            break;
                        case 5://使用赞助点	 	 	目标2000

                            if (doneTimes >= USE_ZAN_ZHU_DIAN_AMOUNT) {
                                lingquDaylyReward(i);
                            } else {
                                gap = USE_ZAN_ZHU_DIAN_AMOUNT - doneTimes;
                                Log.e(TAG, "update: 赞助点消费不够2000，停止了");
                                postMsg(R.string.ph_msg_dayly_task_zanzhu_unreach,doneTimes, gap);
                                continue middle;
                            }
                        case 6://进行普通关卡次数	 	 	目标300
                            if (doneTimes >= CHUANG_GUAN_TIMES_PT) {
                                lingquDaylyReward(i);
                            } else {
                                gap = CHUANG_GUAN_TIMES_PT - doneTimes;
                                postMsg(R.string.ph_msg_dayly_task_putong_chuang_guan_unreach, doneTimes, gap);
//                                Log.e(TAG, "update: 要闯普通关卡，停止了，目标" + gap + " 次 " + puTongGuanka + " 关");
                                  challengeGuan("普通", gap, puTongGuanka);
                            }
                            break;
                        case 7://进行精英关卡次数	 	 	目标300
                            if (doneTimes >= CHUANG_GUAN_TIMES_JY) {
                                lingquDaylyReward(i);
                            } else {
                                gap = CHUANG_GUAN_TIMES_JY - doneTimes;
                                postMsg(R.string.ph_msg_dayly_task_jingying_chuang_guan_unreach, doneTimes, gap);
//                                Log.e(TAG, "update: 要闯精英关卡，停止了，目标" + gap + " 次 " + puTongGuanka + " 关");
                                  challengeGuan("精英", gap, jingYingGuanka);
                            }
                            break;
                        case 8://进行噩梦关卡次数	 	 	目标80

                            if (doneTimes >= CHUANG_GUAN_TIMES_EM) {
                                lingquDaylyReward(i);
                            } else {
                                gap = CHUANG_GUAN_TIMES_EM - doneTimes;
                                postMsg(R.string.ph_msg_dayly_task_emeng_chuang_guan_unreach, doneTimes, gap);
                                Log.e(TAG, "update: 要闯噩梦关卡，停止了，目标" + gap + " 次 " + eMengGuanka + " 关");
                                challengeGuan("噩梦", gap, eMengGuanka);
                            }
                            break;
                        case 9://进行十二宫副本次数	 	目标100
                            if (doneTimes >= SHI_ER_GONG_TIMES) {
                                lingquDaylyReward(i);
                            } else {
                                gap = SHI_ER_GONG_TIMES - doneTimes;
                                postMsg(R.string.ph_msg_dayly_task_12gong_unreach, doneTimes, gap);
                                Log.e(TAG, "update: 要进行十二宫副本，停止了，还差" + gap + " 次");
                                read12Gong();
                            }

                            break;
                        case 10://进行神兽副本次数	 	 	目标100
                            if (doneTimes >= SHEN_SHOU_TIMES) {
                                lingquDaylyReward(i);
                            } else {
                                gap = SHEN_SHOU_TIMES - doneTimes;
                                postMsg(R.string.ph_msg_dayly_task_shenshou_unreach, doneTimes, gap);
                                Log.e(TAG, "update: 要进行神兽副本，停止了，还差" + gap + " 次");
                                readShenShou();
                            }
                            break;
                        case 11://进行赏金猎人副本次数	 目标10
                            if (doneTimes >= GOLDEN_HUNTER_TIMES) {
                                lingquDaylyReward(i);
                            } else {
                                gap = GOLDEN_HUNTER_TIMES - doneTimes;
                                postMsg(R.string.ph_msg_dayly_task_hunter_unreach, doneTimes, gap);
                                Log.e(TAG, "update: 要进行赏金猎人副本，停止了，还差" + gap + " 次");
                                continue middle;
                            }
                    }
                    break;
                } else {
                    postMsg(R.string.ph_msg_dayly_task_done,i);
                }
                count += Character.digit(c,10);
            }
            postMsg(R.string.ph_msg_dayly_task_done_counts,count);
            if (count > 8) {
                lingquDaylyReward();
            }
        }
        return needRepeat;
    }

    private void onChallenge12GongRsp(String res, String[] split) {
        if (split.length >= 13) {
            //帮派副本十二宫挑战结束〓战胜3〓18606〓261〓1〓1442〓1813〓509〓1498.5351〓0 3 0 0〓无〓白羊宫〓4291074.850001
            //帮派副本十二宫挑战结束〓战胜3〓18605〓263〓1〓1442〓1813〓509〓1507.1746〓0 0 0 86395〓无〓双子宫〓4291106.850001
            String result = split[1];
            if (result.startsWith("战胜")) {
                read12Gong();
            } else {
                Log.e(TAG, "十二宫挑战失败了：" + res);
            }
            //12宫副本-%1$s宫挑战%2$s了，获得%3$s星评分！
            String star = result.substring(2);
            if (TextUtils.isEmpty(star)) {
                star = "0";
            }
            postMsg(R.string.ph_challenge_12gong_rsp,split[11],result.substring(0,2), star);
        } else {
            Log.e(TAG, "十二宫挑战失败了：" + res);
        }
    }

    private boolean onRead12GongRsp(String[] split) {
        if (split.length >= 2) {
            //帮派副本十二宫地图〓323232323200000000000000
            //帮派副本十二宫地图〓000000000000000000000000
            String s = split[1];
            int length = Math.min(s.length() - 2, getJingYingGuanLimit());
            for (int i = 0; i <= length; ) {
                int times = Integer.valueOf(s.substring(i, i += 2));
                int index = i / 2;
                Log.e(TAG, ContextHolder.getFormat("十二宫: 第%1$d宫 完成了%2$d次", index, times));
                if (times < Max12GongTimes) {
                    challenge12Gong(index);
                    return true;
                }
            }
            if (mInfo != null && mInfo.resetTimes12Gong < SpUtils.get12GongResetTimes()) {
                reset12Gong(SpUtils.get12GongResetIndex());
            }
            postMsg("12宫任务都完成了！");
        }
        return false;
    }

    private void doNextAuto() {
        switch (mCruCmd) {
            case AutoType.init:
                mCruCmd = LingQuType.qianghuoli;
                lingquItem(ConnectManager.LingQuType.qianghuoli);
                break;
            case LingQuType.qianghuoli:
                lingquItem(ConnectManager.LingQuType.jubaopen);
                mCruCmd = LingQuType.jubaopen;
                break;
            case LingQuType.jubaopen:
                lingquItem(ConnectManager.LingQuType.gudinggoumaihuoli);
                mCruCmd = LingQuType.gudinggoumaihuoli;
                break;
            case LingQuType.gudinggoumaihuoli:
                lingquItem(ConnectManager.LingQuType.yuanbaogoumaihuoli);
                mCruCmd = LingQuType.yuanbaogoumaihuoli;
                break;
            case LingQuType.yuanbaogoumaihuoli:
                readWuJin();
                mCruCmd = AutoType.wujin;
                break;
            case AutoType.wujin:
                readDaylyTask();
                mCruCmd = AutoType.dayly;
                break;
            case AutoType.dayly:
                lingquItem(LingQuType.xiaofeifuli);
                mCruCmd = LingQuType.xiaofeifuli;
                auto = false;
                break;
            case LingQuType.xiaofeifuli:
                break;
            case AutoType.yuanbao:
                break;
            case AutoType.yuansu:
                break;
        }
    }

    private void readWuJin() {
        postMsg(R.string.msg_read_wujin);
        setCmd(R.string.wujin_read);
    }

    private void onReadWuJinRsp(String[] split) {
        if (split.length >= 5) {
            //帮派读取无尽关卡返回〓32〓31〓2017-06-21 12:28:33〓2017年6月21日21时30分36秒
            int curGuan = Integer.valueOf(split[1]);
            int passedGuan = Integer.valueOf(split[2]);
            if (curGuan > passedGuan) {
                challengeWuJinBoss(curGuan);
            } else {
                changeWuJinGuan(passedGuan + 1);
            }
        }
    }

    private void challengeWuJinBoss(int curGuan) {
        postMsg(R.string.ph_msg_challenge_wujin,curGuan);
        sendCmd(R.string.wujin_tiaozhan_boss, account, curGuan);
    }

    private void onChallengeWuJinBossRsp(String[] split) {
        if (split.length >= 2) {
            //帮派无尽关打败BOSS返回〓32
            int curGuan = Integer.valueOf(split[1]);
            changeWuJinGuan(curGuan + 1);
        }
    }

    private void changeWuJinGuan(int tarGuan) {
        postMsg(R.string.ph_msg_change_wujin,tarGuan);
        sendCmd(R.string.wujin_genghuan, account, tarGuan);
    }

    private int getJingYingGuanLimit() {
        if (mInfo == null) {
            return 8;
        }
        int jingYingGuan = mInfo.jingYingGuan;
        if (jingYingGuan>=180) {
            return 22;
        } else if (jingYingGuan>=170) {
            return 20;
        } else if (jingYingGuan>=160) {
            return 18;
        } else if (jingYingGuan>=150) {
            return 16;
        } else if (jingYingGuan>=140) {
            return 14;
        } else if (jingYingGuan>=130) {
            return 12;
        } else if (jingYingGuan>=125) {
            return 10;
        } else {
            return 8;
        }
    }

    public void readDaylyTask() {
        postMsg(R.string.msg_read_dayly_task);
        setCmd(R.string.dayly_query);
    }

    private void repeatCmd() {
        switch (mCruCmd) {
            case LingQuType.qianghuoli:
                lingquItem(ConnectManager.LingQuType.qianghuoli);
                break;
            case LingQuType.jubaopen:
                lingquItem(ConnectManager.LingQuType.jubaopen);
                break;
            case LingQuType.gudinggoumaihuoli:
                lingquItem(ConnectManager.LingQuType.gudinggoumaihuoli);
                break;
            case LingQuType.yuanbaogoumaihuoli:
                lingquItem(ConnectManager.LingQuType.yuanbaogoumaihuoli);
                break;
            case AutoType.wujin:
                readWuJin();
                break;
            case AutoType.dayly:
                readDaylyTask();
                break;
            case LingQuType.xiaofeifuli:
                lingquItem(ConnectManager.LingQuType.xiaofeifuli);
                break;
            case AutoType.yuanbao:
                break;
            case AutoType.yuansu:
                break;
        }
    }

    private boolean auto;

    public static final int houseId = 1000007;
    public static final int npcId = 1000041;
    public void autoBuyXianZi() {
    // 赞助系统房屋查询存宠明细〓hy814〓123〓1000007〓1000041	183.60.204.64	183.60.204.64
        setCmd(R.string.zanzhu_qurey_xianzi,houseId,npcId);
    }

    public void autoStart() {
        auto = true;
        if (mInfo == null) {
            mCruCmd = AutoType.init;
            login(2);
        } else {
            mCruCmd = LingQuType.qianghuoli;
            lingquItem(ConnectManager.LingQuType.qianghuoli);
        }
    }

    /** 登陆，两种：
     *  1，获取客户端人物信息
     *  2，盟军系统人物信息
     *  */
    public void login(int method) {
        switch (method) {
            case 1:
            default:
                setCmd(R.string.checkincom);
                break;
            case 2:
                setCmd(R.string.mengjun_denglu);
                break;
        }
    }

    public void startQuanDi() {

        QuanDi quanDi = new QuanDi(account, pwd);
        mExecutorService.execute(quanDi);
    }

    @IntDef({
            MoneyType.guDingZiJin,
            MoneyType.yuanBao,
            MoneyType.zanZhuDian,
    })
    public @interface MoneyType {
        int guDingZiJin = 35;
        int yuanBao = 36;
        int zanZhuDian = 37;
    }


    /**
     * 购买 宠物探险 抽奖
     */
    public void petExplore(@MoneyType int type) {
        switch (type) {
            case MoneyType.guDingZiJin:
                setCmd(R.string.mengchongtanxian_zijin);
                break;
            case MoneyType.yuanBao:
                setCmd(R.string.mengchongtanxian_yuanbao);
                break;
            case MoneyType.zanZhuDian:
                setCmd(R.string.mengchongtanxian_zanzhudian);
                break;
        }
    }

    @IntDef({LingQuType.jubaopen,
            LingQuType.qianghuoli,
            LingQuType.gudinggoumaihuoli,
            LingQuType.yuanbaogoumaihuoli,
            LingQuType.xiaofeifuli,
    })
    public @interface LingQuType {
        int jubaopen = 1;
        int qianghuoli = 2;
        int gudinggoumaihuoli = 3;
        int yuanbaogoumaihuoli = 4;
        int xiaofeifuli = 5;
    }

    /**
     * 领取 每日任务的奖励 的命令
     */
    public void lingquDaylyReward(int index) {
        postMsg(R.string.ph_msg_lingqu_dayly_reward,index);
        setCmd(R.string.dayly_reward_lingqu, index);
    }

    /**
     * 领取 每日活力任务累计额外的奖励 的命令
     */
    public void lingquDaylyReward() {
        postMsg(R.string.ph_msg_lingqu_dayly_extra_reward);
        setCmd(R.string.dayly_extra_lingqu);
    }

    /**
     * 领取资源类的命令
     */
    public void lingquItem(@LingQuType int type) {

        String s = null;
        switch (type) {

            case LingQuType.yuanbaogoumaihuoli:
                s = "元宝购买活力";
                setCmd(R.string.yuanbaogoumaihuoli);
                break;
            case LingQuType.jubaopen:
                s = "元宝聚宝盆";
                setCmd(R.string.jubaopen);
                break;
            case LingQuType.qianghuoli:
                s = "抢活力";
                setCmd(R.string.qianghuoli);
                break;
            case LingQuType.gudinggoumaihuoli:
                s = "固定资金购买活力";
                setCmd(R.string.gudinggoumaihuoli);
                break;
            case LingQuType.xiaofeifuli:
                s = "消费福利";
                setCmd(R.string.xiaofeifuli);
                break;
        }

        postMsg(R.string.ph_msg_lingqu_items,s);
    }

    @IntDef({ZanZhuType.cailiaoCast,
            ZanZhuType.baoCast,
            ZanZhuType.yuansu,
            ZanZhuType.yuanbao})
    public @interface ZanZhuType {
        int cailiaoCast = 21;
        int baoCast = 22;
        int yuansu = 23;
        int yuanbao = 24;
    }

    @IntDef({LingQuType.jubaopen,
            LingQuType.qianghuoli,
            LingQuType.gudinggoumaihuoli,
            LingQuType.yuanbaogoumaihuoli,
            LingQuType.xiaofeifuli,
            AutoType.wujin,
            AutoType.dayly,
            AutoType.yuansu,
            AutoType.yuanbao,
            AutoType.init,
    })
    public @interface AutoType {
        int wujin = 41;
        int dayly = 42;
        int yuansu = 43;
        int yuanbao = 44;
        int init = 45;
    }

    /**
     * 赞助点购买物品
     */
    public void zanzhuBuyItems(@ZanZhuType int type, int amount) {
        switch (type) {

            case ZanZhuType.baoCast:
                setCmd(R.string.zanzhu_buy_baoxiang, amount);
                break;
            case ZanZhuType.cailiaoCast:
                setCmd(R.string.zanzhu_buy_cailiao_xiangzi, amount);
                break;
            case ZanZhuType.yuanbao:
                setCmd(R.string.zanzhu_buy_yuanbao, amount);
                break;
            case ZanZhuType.yuansu:
                setCmd(R.string.zanzhu_buy_yuansu, amount, amount, amount, amount, amount);
                break;
        }
    }

    @IntDef({SuperMarketType.honor,
            SuperMarketType.huocui,
            SuperMarketType.qinglong,
            SuperMarketType.baihu,
            SuperMarketType.zhuque,
            SuperMarketType.xuanwu,
            SuperMarketType.shengjicishu})
    public @interface SuperMarketType {
        int honor = 11;
        int huocui = 12;
        int qinglong = 13;
        int baihu = 14;
        int zhuque = 15;
        int xuanwu = 16;
        int shengjicishu = 17;
    }

    /**
     * 赞助点超市购买物品，另外加上神器升级次数
     */
    public void zanzhuBuyItems(@SuperMarketType int type) {
        switch (type) {

            case SuperMarketType.baihu:
                setCmd(R.string.zanzhu_buy_baihu);
                break;
            case SuperMarketType.honor:
                setCmd(R.string.zanzhu_buy_honor);
                break;
            case SuperMarketType.huocui:
                setCmd(R.string.zanzhu_buy_huocui);
                break;
            case SuperMarketType.qinglong:
                setCmd(R.string.zanzhu_buy_qinglong);
                break;
            case SuperMarketType.xuanwu:
                setCmd(R.string.zanzhu_buy_xuanwu);
                break;
            case SuperMarketType.zhuque:
                setCmd(R.string.zanzhu_buy_zhuque);
                break;
            case SuperMarketType.shengjicishu:
                setCmd(R.string.zanzhu_buy_shengjicishu);
                break;
        }
    }

    public void setCmd(int cmdStrResId) {
        sendCmd(cmdStrResId, account, pwd);
    }

    private void setCmd(int cmdStrResId, Object... args) {
        Object[] strings = new Object[args.length + 2];
        int i = 0;
        strings[i++] = account;
        strings[i++] = pwd;
        for (Object arg : args) {
            strings[i++] = arg;
        }
        sendCmd(cmdStrResId, strings);
    }

    public void sendCmd(int cmdStrResId, Object... args) {
        String string = ContextHolder.getContext().getString(cmdStrResId);
        String curCmd = ContextHolder.getFormat(string, args);
        send(curCmd);
    }

    private void send(String... cmd) {
        String data = cmd[0];
        if (TextUtils.isEmpty(data)) {
            return;
        }
        mServerSocket.addAndSend(data);
    }
}
