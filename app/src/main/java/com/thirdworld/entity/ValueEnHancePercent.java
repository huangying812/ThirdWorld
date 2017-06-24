package com.thirdworld.entity;

import static com.thirdworld.parseUtil.str2Int;

/**
 * 作者：Ying.Huang on 2017/6/24 13:41
 * Version v1.0
 * 描述：
 */

public class ValueEnHancePercent {

    public float heroHp = 1;
    public float heroAttack = 1;
    public float heroDefense = 1;
    public float heroAgility = 1;

    public float petHp = 1;
    public float petAttack = 1;
    public float petDefense = 1;
    public float petAgility = 1;

    public static ValueEnHancePercent parse(ValueEnHancePercent hancePercent, String info) {
        if (hancePercent == null) {
            hancePercent = new ValueEnHancePercent();
        }
        String[] values = info.split(" ");
        int i = 0;
        hancePercent.heroHp += str2Int(values[i++]) / 100f;
        hancePercent.heroAttack += str2Int(values[i++]) / 100f;
        hancePercent.heroDefense += str2Int(values[i++]) / 100f;
        hancePercent.heroAgility += str2Int(values[i++]) / 100f;
        hancePercent.petHp += str2Int(values[i++]) / 100f;
        hancePercent.petAttack += str2Int(values[i++]) / 100f;
        hancePercent.petDefense += str2Int(values[i++]) / 100f;
        hancePercent.petAgility += str2Int(values[i++]) / 100f;
        return hancePercent;
    }
}
