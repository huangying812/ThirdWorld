package szz.com.baselib.entity;

import szz.com.baselib.parseUtil;

/**
 * 作者：Ying.Huang on 2017/6/22 14:02
 * Version v1.0
 * 描述：
 */

public class UserInfo {

    //帮派个人资料返回〓hy814〓3※有时很忙〓无〓无〓52491 0 91535 20800 20800〓白发妖姬 土 64000 0 136295 0〓正常〓0.0000〓177966 177966 177966 177966 182966〓18510〓2949654〓0〓140〓5709〓3〓2319〓5418〓568〓5234〓5972〓121〓72〓12〓30〓4296162〓0〓0〓1905.0968〓4160 4472 4192 4284 4080 4456 4180 4276〓6000〓0〓1999〓124〓9588 10345 10196 9947 9588 10345 10196 9947〓2017-05-20〓2288869〓16〓27〓999〓0〓999〓1 1 1〓1442〓3136 3156 3137 3117 3007 3004 2992 3038〓2871〓1〓5〓0 0 0 0 0 0 0 0〓60 60 60 60 60 60 60 60〓155484〓0〓无〓无〓无〓0〓112〓4785 4650 4384 1362 4785 4650 4384 1362〓0 0 0 0 0 0 0 0〓120 60 240 180 120 60 240 180〓未知属性9〓未知属性10〓4〓93198〓80〓114962〓42645〓42645〓110〓2521 120 311
    //帮派个人资料返回〓hy814〓3※有时很忙〓无〓无〓52491 0 91535 20800 20800〓白发妖姬 土 64000 0 136295 0〓正常〓0.0000〓177966 177966 177966 177966 182966〓18510〓2950659〓0〓140〓5709〓3〓2319〓5418〓568〓5234〓5972〓121〓72〓12〓30〓4296162〓0〓0〓1905.0968〓4160 4472 4192 4284 4080 4456 4180 4276〓6000〓0〓1999〓124〓9588 10345 10196 9947 9588 10345 10196 9947〓2017-05-20〓2288869〓16〓27〓999〓0〓999〓1 1 1〓1442〓3136 3156 3137 3117 3007 3004 2992 3038〓2861〓1〓5〓0 0 0 0 0 0 0 0〓60 60 60 60 60 60 60 60〓155484〓0〓无〓无〓无〓1〓112〓4785 4650 4384 1362 4785 4650 4384 1362〓0 0 0 0 0 0 0 0〓120 60 240 180 120 60 240 180〓未知属性9〓未知属性10〓4〓93198〓80〓114962〓42645〓42645〓110〓2521 120 311

    //帮派个人资料返回	帮派个人资料返回
    //hy814	hy815
    public String account;
    //3※有时很忙	有时候很忙
    public String nickName;
    //无	打杂杂
    public String bangpai;
    //无	普通成员
    public String zhiwei;
    //52491 0 91535 20800 20800	1 0 1000 4838 1150
    //体力 内力 攻击  防御  敏捷
    public String renWuKeHuDuanJiChuShuxing;
    //白发妖姬 土 64000 0 136295 0	※★第1届赛宠 水 64000 45150 43093 0
    //宠物名 五行 体力 攻击 防御 敏捷
    public String chongWuKeHuDuanJiChuShuxing;
    //正常	正常
    public String accoutState;
    //0.0000	0.0000
    public String unKnow1;
    //177966 177966 177966 177966 182966	137329 137624 137342 137363 137377
    //元素数量：金木水火土
    public String yuanSuAmount;
    //18510	23905
    //活力值
    public String huoliAmount;
    //2949654	811382
    //可能是时间值
    public String unKnow2;
    //0	0
    public String unKnow3;
    //140	112
    //普通关卡
    public int putongGuanka;
    //5709	295919
    //元宝
    public String yuanbao;
    //3	0
    //蜕变次数
    public int tuiBianTimes;
    //2319	1418
    //+1元神数量
    public String yuanshen1;
    //5418	1677
    //+2元神数量
    public String yuanshen2;
    //568	3513
    //+3元神数量
    public String yuanshen3;
    //5234	1692
    //+4元神数量
    public String yuanshen4;
    //5972	275
    //+5元神数量
    public String yuanshen5;
    //121	0
    //+6元神数量
    public String yuanshen6;
    //72	0
    //+7元神数量
    public String yuanshen7;
    //12	0
    //+8元神数量
    public String yuanshen8;
    //30	0
    //+9元神数量
    public String yuanshen9;
    //4296162	487423
    //经验值
    public String exp;
    //0	5
    public String unKnow4;
    //0	5
    public String unKnow5;
    //1905.0968	1513.1039
    public String guDingZiJin;
    //4160 4472 4192 4284 4080 4456 4180 4276	1395 1275 1350 1275 1365 1350 1395 1320
    //属性明细1-潜能 人:体攻防敏 宠:体攻防敏
    public String shuxingmingxiQianNeng;
    //6000	0
    //累计赞助点
    public int leiJiZanZhuDian;
    //0	999
    public String unKnow6;
    //1999	18773
    //宝箱数量
    public String baoXiangAmount;
    //124	100
    //精英关卡
    public int jingYingGuan;
    //9588 10345 10196 9947 9588 10345 10196 9947	917 932 1014 918 917 932 1014 918
    //属性明细1-神器 人:体攻防敏 宠:体攻防敏
    public String shuxingmingxiShenQi;
    //2017-05-20	2017-05-23
    // 赞助开通时间
    public String enableZanZhuDate;
    //2288869	76016
    // 荣誉值
    public String honorAmount;
    //16	5
    // 等级
    public String level;
    //27	0
    //聚宝盆可领取天数
    public String juBaoPenUseDays;
    //999	0
    public String unKnow7;
    //0	10
    public String unKnow8;
    //999	999
    public String unKnow9;
    //1 1 1	1 1 0
    public String unKnow10;
    //1442	588
    // 火淬原石
    public String stoneAmount;
    //3136 3156 3137 3117 3007 3004 2992 3038	253 261 267 265 260 265 257 259
    // 属性明细1-神兽 人:体攻防敏 宠:体攻防敏
    public String shuxingmingxiShenShou;
    //2871	284
    // 赞助余点
    public String zanZhuDianRemain;
    //1	0
    public String unKnow11;
    //5	0
    public String unKnow12;
    //0 0 0 0 0 0 0 0	0 0 0 0 0 0 0 0
    // 属性明细2-武功/装备 人:体攻防敏 宠:体攻防敏
    public String shuxingmingxiZhuangBei;
    //60 60 60 60 60 60 60 60	0 0 0 0 0 0 0 0
    // 属性明细1-星魂 人:体攻防敏 宠:体攻防敏
    public String shuxingmingxiXingHun;
    //155484	0
    // 圈地值
    public String quanDiRemain;
    //0	1
    // 神器升级次数
    public String shenQiLelelUpRemain;
    //无	无
    public String unKnow13;
    //无	无
    public String unKnow14;
    //无	无
    public String unKnow15;
    //0	0
    //12宫已重置次数
    public int resetTimes12Gong;
    //112	92
    //噩梦关卡
    public int eMengGuan;
    //4785 4650 4384 1362 4785 4650 4384 1362	3004 2794 593 443 3004 2794 593 443
    // 属性明细1-萌宠 人:体攻防敏 宠:体攻防敏
    public String shuxingmingxiPet;
    //0 0 0 0 0 0 0 0	0 0 0 0 0 0 0 0
    // 属性明细2-武功/装备 人:体攻防敏 宠:体攻防敏
    public String shuxingmingxiWuGong;
    //120 60 240 180 120 60 240 180	0 0 0 0 0 0 0 0
    // 属性明细2-蜕变 人:体攻防敏 宠:体攻防敏
    public String shuxingmingxiTuiBian;
    //未知属性9	未知属性9
    public String unKnow17;
    //未知属性10	未知属性10
    public String unKnow18;
    //4	41
    //排名
    public String paiMing;
    //93198	2015021
    //萌宠经验水
    public String petExpWater;
    //80	0
    //噩梦完成次数
    public int eMengDoneTimes;
    //114962	38050
    //萌宠精髓
    public String petMoney;
    //42645	0
    //功勋
    public String gongXunAmount;
    //42645	0
    //功勋2
    public String gongXunAmount2;
    //110	90
    public String unKnow20;
    //2521 120 311	140 31 50
    //宠物探险数累计 普通/中级/高级
    public String petExploreCounts;

    public int mDanjia;

    public Hero mHero;

    public String breifToString() {
        return "用户信息：" +
                "\r\n昵称='" + nickName + '\'' +
                "\t12宫重置次数='" + resetTimes12Gong + '\'' +
                "\r\n普通关='" + putongGuanka + '\'' +
                "\t精英关='" + jingYingGuan + '\'' +
                "\t噩梦关='" + eMengGuan + '\'' +
                "\t噩梦关已闯次数='" + eMengDoneTimes + '\'' +
                "\r\n活力='" + huoliAmount + '\'' +
                "\t元宝='" + yuanbao + '\'' +
                "\t固定资金='" + guDingZiJin + '\'' +
                "\t宝箱='" + baoXiangAmount + '\'' +
                "\r\n赞助余点='" + zanZhuDianRemain + '\'' +
                "\t圈地值='" + quanDiRemain + '\'' +
                "\t神器升级次数='" + shenQiLelelUpRemain + '\''
                ;
    }

    @Override
    public String toString() {
        return "用户信息：" +
                "\r\n账号='" + account + '\'' +
                "\r\n昵称='" + nickName + '\'' +
                "\r\n帮派='" + bangpai + '\'' +
                "\r\n职务='" + zhiwei + '\'' +
                "\r\n客户端人物体攻防敏='" + renWuKeHuDuanJiChuShuxing + '\'' +
                "\r\n客户端宠物体攻防敏='" + chongWuKeHuDuanJiChuShuxing + '\'' +
                "\r\n账号状态='" + accoutState + '\'' +
                "\r\n未知字段1='" + unKnow1 + '\'' +
                "\r\n元素数量='" + yuanSuAmount + '\'' +
                "\r\n活力='" + huoliAmount + '\'' +
                "\r\n未知字段2='" + unKnow2 + '\'' +
                "\r\n未知字段3='" + unKnow3 + '\'' +
                "\r\n普通关='" + putongGuanka + '\'' +
                "\r\n元宝='" + yuanbao + '\'' +
                "\r\n蜕变='" + tuiBianTimes + '\'' +
                "\r\n元神+1='" + yuanshen1 + '\'' +
                "\r\n元神+2='" + yuanshen2 + '\'' +
                "\r\n元神+3='" + yuanshen3 + '\'' +
                "\r\n元神+4='" + yuanshen4 + '\'' +
                "\r\n元神+5='" + yuanshen5 + '\'' +
                "\r\n元神+6='" + yuanshen6 + '\'' +
                "\r\n元神+7='" + yuanshen7 + '\'' +
                "\r\n元神+8='" + yuanshen8 + '\'' +
                "\r\n元神+9='" + yuanshen9 + '\'' +
                "\r\n经验值='" + exp + '\'' +
                "\r\n未知字段4='" + unKnow4 + '\'' +
                "\r\n未知字段5='" + unKnow5 + '\'' +
                "\r\n固定资金='" + guDingZiJin + '\'' +
                "\r\n潜能='" + shuxingmingxiQianNeng + '\'' +
                "\r\n累计赞助点='" + leiJiZanZhuDian + '\'' +
                "\r\n未知字段6='" + unKnow6 + '\'' +
                "\r\n宝箱='" + baoXiangAmount + '\'' +
                "\r\n精英关='" + jingYingGuan + '\'' +
                "\r\n神器='" + shuxingmingxiShenQi + '\'' +
                "\r\n开通日期='" + enableZanZhuDate + '\'' +
                "\r\n荣誉='" + honorAmount + '\'' +
                "\r\n等级='" + level + '\'' +
                "\r\n聚宝盆可领天数='" + juBaoPenUseDays + '\'' +
                "\r\n未知字段7='" + unKnow7 + '\'' +
                "\r\n未知字段8='" + unKnow8 + '\'' +
                "\r\n未知字段9='" + unKnow9 + '\'' +
                "\r\n未知字段10='" + unKnow10 + '\'' +
                "\r\n火淬原石='" + stoneAmount + '\'' +
                "\r\n神兽='" + shuxingmingxiShenShou + '\'' +
                "\r\n赞助余点='" + zanZhuDianRemain + '\'' +
                "\r\n未知字段11='" + unKnow11 + '\'' +
                "\r\n未知字段12='" + unKnow12 + '\'' +
                "\r\n装备='" + shuxingmingxiZhuangBei + '\'' +
                "\r\n星魂='" + shuxingmingxiXingHun + '\'' +
                "\r\n圈地值='" + quanDiRemain + '\'' +
                "\r\n神器升级次数='" + shenQiLelelUpRemain + '\'' +
                "\r\n未知字段13='" + unKnow13 + '\'' +
                "\r\n未知字段14='" + unKnow14 + '\'' +
                "\r\n未知字段15='" + unKnow15 + '\'' +
                "\r\n12宫重置次数='" + resetTimes12Gong + '\'' +
                "\r\n噩梦关='" + eMengGuan + '\'' +
                "\r\n萌宠='" + shuxingmingxiPet + '\'' +
                "\r\n武功='" + shuxingmingxiWuGong + '\'' +
                "\r\n蜕变='" + shuxingmingxiTuiBian + '\'' +
                "\r\n未知字段17='" + unKnow17 + '\'' +
                "\r\n未知字段18='" + unKnow18 + '\'' +
                "\r\n排名='" + paiMing + '\'' +
                "\r\n宠物经验水='" + petExpWater + '\'' +
                "\r\n噩梦关已闯次数='" + eMengDoneTimes + '\'' +
                "\r\n精髓='" + petMoney + '\'' +
                "\r\n功勋='" + gongXunAmount + '\'' +
                "\r\n功勋2='" + gongXunAmount2 + '\'' +
                "\r\n未知字段20='" + unKnow20 + '\'' +
                "\r\n萌宠探险总计='" + petExploreCounts + '\'' +
                mHero.toString()
                ;
    }

    public static UserInfo parse(String rsp){
        UserInfo info = new UserInfo();
        String[] strings = rsp.split("〓");
        int i = 1;
        info.account = strings[i++];
        info.nickName = strings[i++];
        info.bangpai = strings[i++];
        info.zhiwei = strings[i++];
        info.renWuKeHuDuanJiChuShuxing = strings[i++];
        info.chongWuKeHuDuanJiChuShuxing = strings[i++];
        info.accoutState = strings[i++];
        info.unKnow1 = strings[i++];
        info.yuanSuAmount = strings[i++];
        info.huoliAmount = strings[i++];
        info.unKnow2 = strings[i++];
        info.unKnow3 = strings[i++];
        info.putongGuanka = Integer.valueOf(strings[i++]);
        info.yuanbao = strings[i++];
        info.tuiBianTimes = Integer.valueOf(strings[i++]);
        info.mDanjia =  100 + info.tuiBianTimes * 10;
        info.yuanshen1 = strings[i++];
        info.yuanshen2 = strings[i++];
        info.yuanshen3 = strings[i++];
        info.yuanshen4 = strings[i++];
        info.yuanshen5 = strings[i++];
        info.yuanshen6 = strings[i++];
        info.yuanshen7 = strings[i++];
        info.yuanshen8 = strings[i++];
        info.yuanshen9 = strings[i++];
        info.exp = strings[i++];
        info.unKnow4 = strings[i++];
        info.unKnow5 = strings[i++];
        info.guDingZiJin = strings[i++];
        info.shuxingmingxiQianNeng = strings[i++];
        info.leiJiZanZhuDian = parseUtil.str2Int(strings[i++]);
        info.unKnow6 = strings[i++];
        info.baoXiangAmount = strings[i++];
        info.jingYingGuan = Integer.valueOf(strings[i++]);
        info.shuxingmingxiShenQi = strings[i++];
        info.enableZanZhuDate = strings[i++];
        info.honorAmount = strings[i++];
        info.level = strings[i++];
        info.juBaoPenUseDays = strings[i++];
        info.unKnow7 = strings[i++];
        info.unKnow8 = strings[i++];
        info.unKnow9 = strings[i++];
        info.unKnow10 = strings[i++];
        info.stoneAmount = strings[i++];
        info.shuxingmingxiShenShou = strings[i++];
        info.zanZhuDianRemain = strings[i++];
        info.unKnow11 = strings[i++];
        info.unKnow12 = strings[i++];
        info.shuxingmingxiZhuangBei = strings[i++];
        info.shuxingmingxiXingHun = strings[i++];
        info.quanDiRemain = strings[i++];
        info.shenQiLelelUpRemain = strings[i++];
        info.unKnow13 = strings[i++];
        info.unKnow14 = strings[i++];
        info.unKnow15 = strings[i++];
        info.resetTimes12Gong = Integer.valueOf(strings[i++]);
        info.eMengGuan = Integer.valueOf(strings[i++]);
        info.shuxingmingxiPet = strings[i++];
        info.shuxingmingxiWuGong = strings[i++];
        info.shuxingmingxiTuiBian = strings[i++];
        info.unKnow17 = strings[i++];
        info.unKnow18 = strings[i++];
        info.paiMing = strings[i++];
        info.petExpWater = strings[i++];
        info.eMengDoneTimes = Integer.valueOf(strings[i++]);
        info.petMoney = strings[i++];
        info.gongXunAmount = strings[i++];
        info.gongXunAmount2 = strings[i++];
        info.unKnow20 = strings[i++];
        info.petExploreCounts = strings[i++];
        info.mHero = Hero.parse(info.nickName,info.renWuKeHuDuanJiChuShuxing,
                info.chongWuKeHuDuanJiChuShuxing,
                info.shuxingmingxiPet,
                info.shuxingmingxiQianNeng,
                info.shuxingmingxiShenQi,
                info.shuxingmingxiShenShou,
                info.shuxingmingxiXingHun,
                info.shuxingmingxiTuiBian,
                info.shuxingmingxiWuGong,
                info.shuxingmingxiZhuangBei
        );
        return info;
    }

    public static final int oneK = 1000;

    public int getZanZhuLevel() {
        if (leiJiZanZhuDian >= 200 * oneK) {
            return 15;
        } else if (leiJiZanZhuDian >= 150 * oneK) {
            return 14;
        } else if (leiJiZanZhuDian >= 120 * oneK) {
            return 13;
        } else if (leiJiZanZhuDian >= 100 * oneK) {
            return 12;
        } else if (leiJiZanZhuDian >= 80 * oneK) {
            return 11;
        } else if (leiJiZanZhuDian >= 50 * oneK) {
            return 10;
        } else if (leiJiZanZhuDian >= 30 * oneK) {
            return 9;
        } else if (leiJiZanZhuDian >= 20 * oneK) {
            return 8;
        } else if (leiJiZanZhuDian >= 10 * oneK) {
            return 7;
        } else if (leiJiZanZhuDian >= 5 * oneK) {
            return 6;
        } else if (leiJiZanZhuDian >= 3 * oneK) {
            return 5;
        } else if (leiJiZanZhuDian >= 1 * oneK) {
            return 4;
        } else if (leiJiZanZhuDian >= 500) {
            return 3;
        } else if (leiJiZanZhuDian >= 200) {
            return 2;
        } else if (leiJiZanZhuDian >= 100) {
            return 1;
        }
        return 0;
    }
}
