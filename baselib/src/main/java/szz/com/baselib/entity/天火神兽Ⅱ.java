package szz.com.baselib.entity;

/**
 * 作者：Ying.Huang on 2017/6/24 15:22
 * Version v1.0
 * 描述：
 */

public class 天火神兽Ⅱ extends BaseRole {
    //血量        攻击    防御  敏捷
    //187.5     262.5	 150	187.5
    public 天火神兽Ⅱ(double hp, double attack, double defense, double a, int level) {
        super("天火神兽Ⅱ", WuXing.NON, (long) (hp * level * 187.5), 0, attack * level * 262.5, defense * level * 150, a * level * 187.5);
    }

}
