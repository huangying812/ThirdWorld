package szz.com.baselib.entity;

/**
 * 作者：Ying.Huang on 2017/6/24 15:22
 * Version v1.0
 * 描述：
 */

public class 神水神兽Ⅱ extends BaseRole {
    //血量        攻击    防御  敏捷
    //262.5	225	93.75	206.25
    public 神水神兽Ⅱ(double hp, double attack, double defense, double a, int level) {
        super("天火神兽Ⅱ", WuXing.NON, (long) (hp * level * 262.5), 0, attack * level * 225, defense * level * 93.75, a * level *206.25);
    }

}
