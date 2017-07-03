package szz.com.baselib.entity;

import szz.com.baselib.parseUtil;

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
        hancePercent.heroHp += parseUtil.str2Int(values[i++]) / 100f;
        hancePercent.heroAttack += parseUtil.str2Int(values[i++]) / 100f;
        hancePercent.heroDefense += parseUtil.str2Int(values[i++]) / 100f;
        hancePercent.heroAgility += parseUtil.str2Int(values[i++]) / 100f;
        hancePercent.petHp += parseUtil.str2Int(values[i++]) / 100f;
        hancePercent.petAttack += parseUtil.str2Int(values[i++]) / 100f;
        hancePercent.petDefense += parseUtil.str2Int(values[i++]) / 100f;
        hancePercent.petAgility += parseUtil.str2Int(values[i++]) / 100f;
        return hancePercent;
    }
}
