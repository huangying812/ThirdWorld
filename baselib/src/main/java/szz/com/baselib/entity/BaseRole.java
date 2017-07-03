package szz.com.baselib.entity;

import static szz.com.baselib.entity.WuXing.NON;

/**
 * 作者：Ying.Huang on 2017/6/24 12:18
 * Version v1.0
 * 描述：基础角色，包含：名称、五行属性、体力、内力、攻击、防御、敏捷、被攻击优先级、生死状态 属性
 */

public class BaseRole {

    public String name = "";
    public WuXing wuXing = NON;
    public double hp;
    public double mp;
    public double attack;
    public double defense;
    public double agility;
    public int priorityOfGotAttacked;
    public boolean alive = true;//生

    public BaseRole(String name, WuXing wuXing, long hp, double mp, double attack, double defense, double agility) {
        this.name = name;
        this.wuXing = wuXing;
        this.hp = hp;
        this.mp = mp;
        this.attack = attack;
        this.defense = defense;
        this.agility = agility;
    }

    /**
     * 掉血公式：
     * ---------------------
     * 攻击者  克  被攻击者：
     * 掉血值 ＝ 攻击者的攻 × 1.5 － 被攻击者的防
     * 攻击者 被克 被攻击者：
     * 掉血值 ＝ 攻击者的攻 － 被攻击者的防
     * 攻击者 不克 被攻击者：
     * 掉血值 ＝ 攻击者的攻 － 被攻击者的防/1.5
     */
    public long attack(BaseRole other) {
        if (other instanceof Hero && ((Hero) other).pet.alive) {
            attack(((Hero) other).pet);
            return 0;
        } else {
            double lost;
            KeZhiType type = compareWuXing(other.wuXing);
            switch (type) {
                case enhance:
                    lost = attack * 1.5f - other.defense;
                    break;
                case debuff:
                    lost = attack - other.defense;
                    break;
                default:
                    lost = attack - other.defense / 1.5f;
                    break;
            }
            lost = Math.max(lost, 1);
            if (other.hp > lost) {
                other.hp -= lost;
            } else {
                other.hp = 0;
                alive = false;
            }
            return Math.round(lost);
        }
    }

    public int gotAttack(BaseRole... others) {
        if (others == null || others.length == 0) {
            return 0;
        }
        float lost = 0;
        for (BaseRole other : others) {
            lost += other.attack(this);
        }
        return Math.round(lost);
    }

    public KeZhiType compareWuXing(WuXing wuXing) {
        switch (this.wuXing) {
            case GOLDEN:
                switch (wuXing) {
                    case WOOD:
                        return KeZhiType.enhance;
                    case FIRE:
                        return KeZhiType.debuff;
                }
                break;
            case WOOD:
                switch (wuXing) {
                    case GOLDEN:
                        return KeZhiType.debuff;
                    case EARTH:
                        return KeZhiType.enhance;
                }
                break;
            case WATER:
                switch (wuXing) {
                    case FIRE:
                        return KeZhiType.enhance;
                    case EARTH:
                        return KeZhiType.debuff;
                }
                break;
            case FIRE:
                switch (wuXing) {
                    case GOLDEN:
                        return KeZhiType.enhance;
                    case WATER:
                        return KeZhiType.debuff;
                }
                break;
            case EARTH:
                switch (wuXing) {
                    case WOOD:
                        return KeZhiType.debuff;
                    case WATER:
                        return KeZhiType.enhance;
                }
                break;
        }
        return KeZhiType.non;
    }

    @Override
    public String toString() {
        return "基本角色：" +
                "\r\n 名称='" + name + '\'' +
                "\r\n 五行=" + wuXing.getValue() +
                "\r\n 血量=" + hp +
                "\r\n 攻击=" + attack +
                "\r\n 防御=" + defense +
                "\r\n 敏捷=" + agility +
                "\r\n 顺序=" + priorityOfGotAttacked
                ;
    }

    public static double compareWuXing(OprationType type, WuXing wuXing) {
        switch (type) {
            case attark:
                break;
            case gotAttark:
                break;

        }

        return 0;
    }

}
