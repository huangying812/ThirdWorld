package com.thirdworld.entity;

/**
 * 作者：Ying.Huang on 2017/6/24 15:22
 * Version v1.0
 * 描述：
 */

public class 隐土神兽Ⅱ extends BaseRole {
    //血量        攻击    防御  敏捷
    //225	187.5	150	225
    public 隐土神兽Ⅱ(double hp, double attack, double defense, double a, int level) {
        super("天火神兽Ⅱ", WuXing.NON, (long) (hp * level * 225), 0, attack * level * 187.5, defense * level * 150, a * level *225);
    }

}
