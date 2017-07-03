package com.thirdworld.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import szz.com.baselib.singleton.ConnectManager;

import szz.com.baselib.rest.ServerSocket;
import szz.com.thirdworld.R;


public class ThirdWoldAct extends BaseActivity implements View.OnClickListener {

    private Button jubaopen;
    private Button qianghuoli;
    private Button putongguanka;
    private Button jingyingguanka;
    private Button emengguanka;
    private Button gudinggoumaihuoli;
    private Button yuanbaogoumaihuoli;
    private Button chaoheigoumaihuoli;
    private Button gudingmengchongtanxian;
    private Button yuanbaomengchongtanxian;
    private Button zanzhudianmengchongtanxian;
    private Button goumaimengchongsuipian;
    private Button chongzhijingsuishangcheng;
    private Button goumaizahuo;
    private Button chongzhizahuo;
    private Button jibenrenwuchuangguan;
    private Button chongzhijibenrenwu;
    private Button tiaozhanshouguanboss;
    private Button genghuanzhushou;
    private Button jiesuan;
    private Button tiaozhanshenshou;
    private Button tiaozhan12gong;
    private Button chongzhi12gong;
    private Button shuaxinliewu;
    private Button tiaozhanlieren;
    private Button yuanbao;
    private Button baoxiang;
    private Button yuansu;
    private Button caixiang;
    private Button shengjicishu;
    private Button rongyu;
    private Button huocui;
    private Button qinglong;
    private Button baihu;
    private Button zhuque;
    private Button xuanwu;
    private Button readjingsuishangcheng;
    private Button readzahuo;
    ServerSocket mServerSocket;
    private String account = "hy815";
    private String pwd = "123";
    private String curCmd = "";
    private int tuiBianCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_wold);
        initView();
        mServerSocket = ConnectManager.getInstance().getSocket();
    }

    private void setCmd(int cmdStrResId) {
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

    private void sendCmd(int cmdStrResId, Object... args) {
        curCmd = getFormat(cmdStrResId, args);
        send(curCmd);
    }

    private void send(String... cmd) {
        String data = cmd[0];
//        String op = cmd[1];
        if (TextUtils.isEmpty(data)
//                || TextUtils.isEmpty(op)
                ) {
            return;
        }
        mServerSocket.addAndSend(data);
    }

    private void initView() {
        jubaopen = (Button) findViewById(R.id.jubaopen);
        qianghuoli = (Button) findViewById(R.id.qianghuoli);
        putongguanka = (Button) findViewById(R.id.putongguanka);
        jingyingguanka = (Button) findViewById(R.id.jingyingguanka);
        emengguanka = (Button) findViewById(R.id.emengguanka);
        gudinggoumaihuoli = (Button) findViewById(R.id.gudinggoumaihuoli);
        yuanbaogoumaihuoli = (Button) findViewById(R.id.yuanbaogoumaihuoli);
        chaoheigoumaihuoli = (Button) findViewById(R.id.chaoheigoumaihuoli);
        gudingmengchongtanxian = (Button) findViewById(R.id.gudingmengchongtanxian);
        yuanbaomengchongtanxian = (Button) findViewById(R.id.yuanbaomengchongtanxian);
        zanzhudianmengchongtanxian = (Button) findViewById(R.id.zanzhudianmengchongtanxian);
        goumaimengchongsuipian = (Button) findViewById(R.id.goumaimengchongsuipian);
        chongzhijingsuishangcheng = (Button) findViewById(R.id.chongzhijingsuishangcheng);
        goumaizahuo = (Button) findViewById(R.id.goumaizahuo);
        chongzhizahuo = (Button) findViewById(R.id.chongzhizahuo);
        jibenrenwuchuangguan = (Button) findViewById(R.id.jibenrenwuchuangguan);
        chongzhijibenrenwu = (Button) findViewById(R.id.chongzhijibenrenwu);
        tiaozhanshouguanboss = (Button) findViewById(R.id.tiaozhanshouguanboss);
        genghuanzhushou = (Button) findViewById(R.id.genghuanzhushou);
        jiesuan = (Button) findViewById(R.id.jiesuan);
        tiaozhanshenshou = (Button) findViewById(R.id.tiaozhanshenshou);
        tiaozhan12gong = (Button) findViewById(R.id.tiaozhan12gong);
        chongzhi12gong = (Button) findViewById(R.id.chongzhi12gong);
        shuaxinliewu = (Button) findViewById(R.id.shuaxinliewu);
        tiaozhanlieren = (Button) findViewById(R.id.tiaozhanlieren);
        yuanbao = (Button) findViewById(R.id.yuanbao);
        baoxiang = (Button) findViewById(R.id.baoxiang);
        yuansu = (Button) findViewById(R.id.yuansu);
        caixiang = (Button) findViewById(R.id.caixiang);
        shengjicishu = (Button) findViewById(R.id.shengjicishu);
        rongyu = (Button) findViewById(R.id.rongyu);
        huocui = (Button) findViewById(R.id.huocui);
        qinglong = (Button) findViewById(R.id.qinglong);
        baihu = (Button) findViewById(R.id.baihu);
        zhuque = (Button) findViewById(R.id.zhuque);
        xuanwu = (Button) findViewById(R.id.xuanwu);

        yuansu.setOnClickListener(this);
        jubaopen.setOnClickListener(this);
        qianghuoli.setOnClickListener(this);
        putongguanka.setOnClickListener(this);
        jingyingguanka.setOnClickListener(this);
        emengguanka.setOnClickListener(this);
        gudinggoumaihuoli.setOnClickListener(this);
        yuanbaogoumaihuoli.setOnClickListener(this);
        chaoheigoumaihuoli.setOnClickListener(this);
        gudingmengchongtanxian.setOnClickListener(this);
        yuanbaomengchongtanxian.setOnClickListener(this);
        zanzhudianmengchongtanxian.setOnClickListener(this);
        goumaimengchongsuipian.setOnClickListener(this);
        chongzhijingsuishangcheng.setOnClickListener(this);
        goumaizahuo.setOnClickListener(this);
        chongzhizahuo.setOnClickListener(this);
        jibenrenwuchuangguan.setOnClickListener(this);
        chongzhijibenrenwu.setOnClickListener(this);
        tiaozhanshouguanboss.setOnClickListener(this);
        genghuanzhushou.setOnClickListener(this);
        jiesuan.setOnClickListener(this);
        tiaozhanshenshou.setOnClickListener(this);
        tiaozhan12gong.setOnClickListener(this);
        chongzhi12gong.setOnClickListener(this);
        shuaxinliewu.setOnClickListener(this);
        tiaozhanlieren.setOnClickListener(this);
        yuanbao.setOnClickListener(this);
        baoxiang.setOnClickListener(this);
        caixiang.setOnClickListener(this);
        shengjicishu.setOnClickListener(this);
        rongyu.setOnClickListener(this);
        huocui.setOnClickListener(this);
        qinglong.setOnClickListener(this);
        baihu.setOnClickListener(this);
        zhuque.setOnClickListener(this);
        xuanwu.setOnClickListener(this);
        readjingsuishangcheng = (Button) findViewById(R.id.readjingsuishangcheng);
        readjingsuishangcheng.setOnClickListener(this);
        readzahuo = (Button) findViewById(R.id.readzahuo);
        readzahuo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int times = 1;
        int index = 1;
        int page = 4;
        int shuliang = 4;
        switch (v.getId()) {
            case R.id.jubaopen:
                setCmd(R.string.jubaopen);
                break;
            case R.id.qianghuoli:
                setCmd(R.string.qianghuoli);
                break;
            case R.id.putongguanka:
                setCmd(R.string.chuangguan,"普通",times,index);
                /*帮战个人开始闯关〓hy815〓123〓普通〓1〓1〓〓〓〓设备验证	183.60.204.64	183.60.204.64
                帮派个人闯关结束〓1892.9147〓137078 137078 137078 137078 137078〓24061〓战胜3〓112〓1〓普通〓484647〓1418 1675 3513 1692 275 0 0 0 0〓18425〓1〓备用13〓373849〓0 0 0 0 0 0 0 0 0 0 0 0 0 0〓无〓100〓32986〓92〓0*/
                /*帮战个人开始闯关〓hy815〓123〓普通〓30〓1〓〓〓〓设备验证	183.60.204.64	183.60.204.64
                帮派个人闯关结束〓1892.854700001〓137108 137108 137108 137108 137108〓24031〓战胜3〓112〓1〓普通〓484685〓1418 1675 3513 1692 275 0 0 0 0〓18425〓30〓备用13〓373849〓0 0 0 0 0 0 0 0 0 0 0 0 0 0〓无〓100〓32986〓92〓0
                                    固定资金        金      木    水   火       土    活力 胜败+星级 普通 关卡 类别  总经验     元神                          宝箱  次数          元宝                                  精英关  精髓  噩梦关 */
                /*帮战个人开始闯关〓hy815〓123〓普通〓19〓1〓〓〓〓设备验证	183.60.204.64	183.60.204.64
                帮派个人闯关结束〓1892.816700001〓137127 137127 137127 137127 137127〓24012〓战胜3〓112〓1〓普通〓484708〓1418 1675 3513 1692 275 0 0 0 0〓18425〓19〓备用13〓373849〓0 0 0 0 0 0 0 0 0 0 0 0 0 0〓无〓100〓32986〓92〓0*/
                break;
            case R.id.jingyingguanka:
                setCmd(R.string.chuangguan,"精英",times,index);
                /*帮战个人开始闯关〓hy815〓123〓精英〓1〓4〓〓〓〓设备验证	183.60.204.64	183.60.204.64
                帮派个人闯关结束〓1892.8117〓137127 137127 137127 137127 137127〓24009〓战胜3〓112〓4〓精英〓484726〓1418 1675 3513 1692 275 0 0 0 0〓18425〓1〓备用13〓373864〓0 0 0 0 0 0 0 0 0 0 0 15 0 0〓无〓100〓32986〓92〓0*/
                /*帮战个人开始闯关〓hy815〓123〓精英〓29〓4〓〓〓〓设备验证	183.60.204.64	183.60.204.64
                帮派个人闯关结束〓1893.866700004〓137127 137127 137127 137127 137127〓23922〓战胜3〓112〓4〓精英〓485269〓1418 1677 3513 1692 275 0 0 0 0〓18434〓29〓备用13〓374089〓0 2 0 0 0 0 0 0 0 0 0 225 12000 9〓潜能_血牛 1；潜能_血牛 1；潜能_血牛 1；〓100〓32986〓92〓0*/
                /*帮战个人开始闯关〓hy815〓123〓精英〓21〓99〓〓〓〓设备验证	183.60.204.64	183.60.204.64
                帮派个人闯关结束〓1905.968900015〓137127 137127 137127 137127 137127〓23859〓战胜3〓112〓99〓精英〓485662〓1418 1677 3513 1692 275 0 0 0 0〓18443〓21〓备用13〓375389〓0 0 0 0 0 0 0 0 0 0 0 1300 204182 9〓潜能_神行+4 1；〓100〓32986〓92〓0*/
                break;
            case R.id.emengguanka:
                setCmd(R.string.chuangguan,"噩梦",times,index);
                /*帮战个人开始闯关〓hy815〓123〓噩梦〓30〓89〓〓〓〓设备验证	183.60.204.64	183.60.204.64
                帮派个人闯关结束〓1789.1559〓137127 137127 137127 137127 137127〓23559〓战胜3〓112〓89〓噩梦〓487349〓1418 1677 3513 1692 275 0 0 0 0〓18503〓30〓备用13〓375389〓0 0 0 0 0 0 0 0 0 0 0 0 166870 60〓※盟宠经验水_少许 1 9920；〓100〓33010〓92〓24*/
                break;
            case R.id.gudinggoumaihuoli:
                setCmd(R.string.gudinggoumaihuoli);
                break;
            case R.id.yuanbaogoumaihuoli:
                setCmd(R.string.yuanbaogoumaihuoli);
                break;
            case R.id.chaoheigoumaihuoli:
                setCmd(R.string.chaoheigoumaihuoli, 100 + tuiBianCount * 10);
                break;
            case R.id.gudingmengchongtanxian:
                setCmd(R.string.mengchongtanxian_zijin);
                break;
            case R.id.yuanbaomengchongtanxian:
                setCmd(R.string.mengchongtanxian_yuanbao);

                break;
            case R.id.zanzhudianmengchongtanxian:
                setCmd(R.string.mengchongtanxian_zanzhudian);

                break;
            case R.id.readjingsuishangcheng:
                setCmd(R.string.jingsuishangcheng_read);

                break;
            case R.id.goumaimengchongsuipian:
                setCmd(R.string.jingsuishangcheng_buy,index);

                break;
            case R.id.chongzhijingsuishangcheng:
                setCmd(R.string.jingsuishangcheng_reset);

                break;
            case R.id.readzahuo:
                setCmd(R.string.zahuo_read);

                break;
            case R.id.goumaizahuo:
                setCmd(R.string.zahuo_buy);

                break;
            case R.id.chongzhizahuo:
                setCmd(R.string.zahuo_reset);

                break;
            case R.id.jibenrenwuchuangguan:
                int type = 0;//0,活力；1,神兽丹；2,五行元素；3,三界灵丹；
                int jibie = 3;//1,菜鸟关；2,加强关；3,大气关；4,高端关；5,极品关；6,臻品关；7,上古关；
                setCmd(R.string.jibenrenwuchuangguan,type,jibie);
                /*盟军基本任务挑战开始〓hy815〓123〓301	183.60.204.64	183.60.204.64
                帮派副本基本任务挑战结束〓战胜3〓※增加活力 62；〓活力_菜鸟关〓1*/
                /*盟军基本任务挑战开始〓hy815〓123〓302	183.60.204.64	183.60.204.64
                帮派副本基本任务挑战结束〓战胜3〓※增加活力 75；〓活力_加强关〓2*/
                /*盟军基本任务挑战开始〓hy815〓123〓312	183.60.204.64	183.60.204.64
                帮派副本基本任务挑战结束〓战胜3〓神兽丹_一星 1；〓神兽丹_加强关〓3*/
                /*盟军基本任务挑战开始〓hy815〓123〓322	183.60.204.64	183.60.204.64
                帮派副本基本任务挑战结束〓战胜3〓※增加五行元素 7500；※增加五行元素 7500；〓五行元素_加强关〓4*/
                /*盟军基本任务挑战开始〓hy815〓123〓334	183.60.204.64	183.60.204.64
                帮派副本基本任务挑战结束〓战败〓〓三界灵丹_高端关〓5*/

                break;
            case R.id.chongzhijibenrenwu:
                setCmd(R.string.jibenrenw_reset);
                /*基本任务重置次数〓hy815〓123	183.60.204.64	183.60.204.64
                帮派基本任务重置成功〓1540〓1*/
                break;
            case R.id.tiaozhanshouguanboss:
                sendCmd(R.string.wujin_tiaozhan_boss,account,index);
                /*盟军无尽关发起BOSS挑战〓hy815〓3	183.60.204.64	183.60.204.64
                错误，阁下在无尽 [3] 关卡挑战BOSS失败了！*/
                break;
            case R.id.genghuanzhushou:
                sendCmd(R.string.wujin_genghuan,account,index);
                /*盟军无尽关更换关卡〓hy815〓3	183.60.204.64	183.60.204.64
                帮派无尽关变更关卡返回〓3〓2017-06-21 18:20:30〓1224368 12243 0 222 0 0 0*/

                break;
            case R.id.jiesuan:
                /*盟军无尽关开始结算〓hy815〓3	183.60.204.64	183.60.204.64
                帮派无尽关结算返回〓75 0 0 0 1 0 0*/
                break;
            case R.id.shuaxinshenshou:
                setCmd(R.string.shenshou_shuaxin,index);
                /*盟军神兽副本读取地图〓hy814〓123〓3	183.60.204.64	183.60.204.64
                帮派神兽副本读取地图〓22104 22204 22304 22404 22504 22604 22704 22803 22903 23003〓3〓230*/
                /*盟军神兽副本读取地图〓hy814〓123〓1	183.60.204.64	183.60.204.64
                帮派神兽副本读取地图〓20103 20203 20303 20403 20503 20603 20703 20803 20903 21003〓1〓230
                           2abcd   第a+1层第b关挑战次数cd                                     当前层数  最高关卡 */
                break;
            case R.id.tiaozhanshenshou:
                setCmd(R.string.shenshou_tiaozhan_boss, page, index);
                /*盟军神兽副本挑战开始〓hy814〓123〓神兽副本3-10	183.60.204.64	183.60.204.64
                帮派神兽副本挑战结束〓战胜1〓230〓230〓4〓1423〓827〓372728〓11346.5808〓0 0 100 0〓无〓神兽副本3-10*/

                break;
            case R.id.tiaozhan12gong:

                break;
            case R.id.read12gong:
                setCmd(R.string.shiergong_shuaxin);
                /*盟军副本十二宫读取地图〓hy814〓123	183.60.204.64	183.60.204.64
                帮派副本十二宫地图〓323232323200000000000000*/

                break;
            case R.id.chongzhi12gong:
                break;
            case R.id.shuaxinliewu:
                setCmd(R.string.lieren_shuaxin);
                /*盟军读取猎物〓hy814〓123	183.60.204.64	183.60.204.64
                盟军读取猎物成功〓上古.金刚.徐岳；4511152；250.00；1.25；15.00；2.50；；1；1226469270000；1226469270000；15600
                上古.神行.蔡谇；1820201；250.00；1.25；5.00；7.50；；1；494866968750；494866968750；13200
                高端.金刚.苏曳倌；909241；175.00；0.88；10.50；1.75；；1；173039803125；173039803125；11000
                上古.金刚.雷撬厮；453641；250.00；1.25；15.00；2.50；；1；123333468750；123333468750；9000
                菜鸟.血牛.皇甫傻先；198018；300.00；0.50；2.00；1.00；；1；64603156500；64603156500；7200
                大气.金刚.颛孙纫叶；90562；150.00；0.75；9.00；1.50；；1；14772818250；14772818250；5600
                极品.血牛.余尤钡；56615；600.00；1.00；4.00；2.00；；1；36940860000；36940860000；4200
                大气.金刚.方圃；19086；150.00；0.75；9.00；1.50；；1；3113295750；3113295750；3000
                菜鸟.神行.袁推吵；9074；100.00；0.50；2.00；3.00；；1；986725500；986725500；2000
                上古.莽夫.刘险；4786；250.00；3.75；5.00；2.50；；1；1301013750；1301013750；1200
                臻品.金刚.孔等；2264；225.00；1.13；13.50；2.25；；1；553810500；553810500；600
                大气.血牛.令狐难；1018；450.00；0.75；3.00；1.50；2※幻影Oo；0；0；497859750；200*/
                break;
            case R.id.tiaozhanlieren:
                setCmd(R.string.lieren_tiaozhan_boss,index);
                /*帮战赏金猎人开始挑战〓hy814〓123〓11	183.60.204.64	183.60.204.64
                错误，无效猎物，请刷新后在试试看！*/
                break;
            case R.id.yuanbao:
                setCmd(R.string.zanzhu_buy_yuanbao,shuliang);
                /*赞助点兑换元宝〓hy815〓123〓13	183.60.204.64	183.60.204.64
                赞助点兑换元宝成功〓1527〓381889*/
                break;
            case R.id.yuansu:
                setCmd(R.string.zanzhu_buy_yuansu,shuliang,shuliang,shuliang,shuliang,shuliang);
                /*赞助点购买元素〓hy815〓123〓122〓417〓135〓156〓170	183.60.204.64	183.60.204.64
                赞助点购买元素成功〓1526〓137249 137544 137262 137283 137297*/
                break;
            case R.id.baoxiang:
                setCmd(R.string.zanzhu_buy_baoxiang,shuliang);
                /*赞助点购买宝箱〓hy815〓123〓50	183.60.204.64	183.60.204.64
                赞助点兑换宝箱成功〓1476〓18753*/
                break;
            case R.id.caixiang:
                setCmd(R.string.zanzhu_buy_cailiao_xiangzi,shuliang);
                /*赞助点兑换材料箱〓hy815〓123〓50	183.60.204.64	183.60.204.64
                赞助点兑换材料箱子成功〓1431〓250*/
                break;
            case R.id.shengjicishu:
                setCmd(R.string.zanzhu_buy_shengjicishu);
                /*赞助点兑换升级次数〓hy815〓123	183.60.204.64	183.60.204.64
                赞助点兑换神器升级次数成功〓431〓1*/
                break;
            case R.id.rongyu:
                setCmd(R.string.zanzhu_buy_honor);
                /*赞助点购买超市物品〓hy815〓123〓※增加荣誉〓0	183.60.204.64	183.60.204.64
                成功，阁下本次消耗[100]赞助点，购买[※增加荣誉]*50000！*/
                break;
            case R.id.huocui:
                setCmd(R.string.zanzhu_buy_huocui);
                /*赞助点购买超市物品〓hy815〓123〓※火淬原石〓0	183.60.204.64	183.60.204.64
                成功，阁下本次消耗[100]赞助点，购买[※火淬原石]*200！*/
                break;
            case R.id.qinglong:
                setCmd(R.string.zanzhu_buy_qinglong);
                /*赞助点购买超市物品〓hy815〓123〓兽魂_青龙〓0	183.60.204.64	183.60.204.64
                错误，4-2账号[hy815]本日的赞助超市购买次数已使用过了！*/
                break;
            case R.id.baihu:
                setCmd(R.string.zanzhu_buy_baihu);
                /*赞助点购买超市物品〓hy815〓123〓兽魂_白虎〓0	183.60.204.64	183.60.204.64
                错误，4-2账号[hy815]本日的赞助超市购买次数已使用过了！*/

                break;
            case R.id.zhuque:
                setCmd(R.string.zanzhu_buy_zhuque);
                /*赞助点购买超市物品〓hy815〓123〓兽魂_朱雀〓0	183.60.204.64	183.60.204.64
                错误，4-2账号[hy815]本日的赞助超市购买次数已使用过了！*/

                break;
            case R.id.xuanwu:
                setCmd(R.string.zanzhu_buy_xuanwu);
                /*赞助点购买超市物品〓hy815〓123〓兽魂_玄武〓0	183.60.204.64	183.60.204.64
                错误，4-2账号[hy815]本日的赞助超市购买次数已使用过了！*/

                break;
        }
    }
}
