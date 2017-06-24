package com.thirdworld.entity;

import android.support.annotation.NonNull;

import static com.thirdworld.parseUtil.str2Double;
import static com.thirdworld.parseUtil.str2Int;
import static com.thirdworld.parseUtil.str2Long;

/**
 * 作者：Ying.Huang on 2017/6/24 15:16
 * Version v1.0
 * 描述：
 */

public class GoldenHunterMonster extends BaseRole {

    public long maxHp;
    public int reward;
    public int level;
    public int index;
    public 天火神兽Ⅱ 天火神兽Ⅱ;//187.5	262.5	150	187.5
    public 耀金神兽Ⅱ 耀金神兽Ⅱ;//187.5	187.5	150	262.5
    public 巨木神兽Ⅱ 巨木神兽Ⅱ;//225	187.5	131.25	243.75
    public 神水神兽Ⅱ 神水神兽Ⅱ;//262.5	225	93.75	206.25
    public 隐土神兽Ⅱ 隐土神兽Ⅱ;//225	187.5	150	225

    public GoldenHunterMonster(String name, int index, int level, long hp, long mp, double hpPer, double attack, double defense, double agility, int reward) {
        super(name, WuXing.NON, hp, 0, 0, 0, 0);
        this.index = index;
        this.level = level;
        this.maxHp = mp;
        this.reward = reward;
        天火神兽Ⅱ = new 天火神兽Ⅱ(hpPer, attack, defense, agility, level);
        耀金神兽Ⅱ = new 耀金神兽Ⅱ(hpPer, attack, defense, agility, level);
        巨木神兽Ⅱ = new 巨木神兽Ⅱ(hpPer, attack, defense, agility, level);
        神水神兽Ⅱ = new 神水神兽Ⅱ(hpPer, attack, defense, agility, level);
        隐土神兽Ⅱ = new 隐土神兽Ⅱ(hpPer, attack, defense, agility, level);
    }

    public static GoldenHunterMonster parse(int index, String info) {
        //大气.血牛.朱币；2303；450.00；0.75；3.00；1.50；；1；1126710000；1126710000；600
        //菜鸟.金刚.侯颜；1070；100.00；0.50；6.00；1.00；；1；116290500；116290500；200
        //菜鸟.神行.杨宜肺；1135；100.00；0.50；2.00；3.00；1※飞羽；0；0；123360000；200

        String[] values = info.split("；");
        String name = values[0].trim();
        int level = str2Int(values[1].trim());
        double hpPer = str2Double(values[2].trim());
        double at = str2Double(values[3].trim());
        double de = str2Double(values[4].trim());
        double al = str2Double(values[5].trim());
        String killer = values[7].trim();//击杀者
        long hp = str2Long(values[8].trim());//当前血量
        long hpMax = str2Long(values[9].trim());//满血量
        int reward = str2Int(values[10].trim());
        return new GoldenHunterMonster(name, index, level, hp, hpMax, hpPer, at, de, al, reward);
    }

    @Override
    public String toString() {
        return "怪物" + index + "{" + "名称：" + name +
//                " 等级：" + level + " 奖励：" + reward +
                " 总血量：" + getHpStr() + " 剩余：" + (hp * 100 / maxHp) + "% 防御：" +
                getDefStr() + "}";
    }

    @NonNull
    private String getDefStr() {

        long def = (long) 天火神兽Ⅱ.defense;
        int count = 0;
        String unit;
        while (def / 10 > 0) {
            count++;
            def /= 10;

        }
        switch (count) {
            case 4:
                unit = " 万";
                break;
            case 5:
                unit = " 十万";
                break;
            case 6:
                unit = " 百万";
                break;
            case 7:
                unit = " 千万";
                break;
            case 8:
                unit = " 亿";
                break;
            case 9:
                unit = " 十亿";
                break;
            case 10:
                unit = " 百亿";
                break;
            case 11:
                unit = " 千亿";
                break;
            case 12:
                unit = " 万亿";
                break;
            case 13:
                unit = " 十万亿";
                break;
            default:
                unit = count + " 个零";
        }
        return def  + unit;
    }

    @NonNull
    private String getHpStr() {

        long hp = maxHp;
        int count = 0;
        String unit;
        while (hp / 10 > 0) {
            count++;
            hp /= 10;

        }
        switch (count) {
            case 8:
                unit = " 亿";
                break;
            case 9:
                unit = " 十亿";
                break;
            case 10:
                unit = " 百亿";
                break;
            case 11:
                unit = " 千亿";
                break;
            case 12:
                unit = " 万亿";
                break;
            case 13:
                unit = " 十万亿";
                break;
            case 14:
                unit = " 百万亿";
                break;
            case 15:
                unit = " 千万亿";
                break;
            case 16:
                unit = " 万万亿";
                break;
            case 17:
                unit = " 十亿亿";
                break;
            default:
                unit = count + " 个零";
        }

        return hp + unit;
    }
}
