package szz.com.baselib.entity;

import szz.com.baselib.parseUtil;

/**
 * 作者：Ying.Huang on 2017/6/24 13:07
 * Version v1.0
 * 描述：
 */

public class Hero extends BaseRole {

    public BaseRole pet;

    public Hero(String name, WuXing wuXing, int hp, int mp, int attack, int defense, int agility) {
        super(name, wuXing, hp, mp, attack, defense, agility);
    }

    public Hero(String nickName, BaseRole pet, int hp, int mp, int attack, int defense, int agility) {
        this(nickName, WuXing.NON, hp, mp, attack, defense, agility);
        this.pet = pet;
    }

    public static Hero parse(String nickName, String renWuKeHuDuanJiChuShuxing, String chongWuKeHuDuanJiChuShuxing, String shuxingmingxiPet, String shuxingmingxiQianNeng, String shuxingmingxiShenQi, String shuxingmingxiShenShou, String shuxingmingxiXingHun, String shuxingmingxiTuiBian, String shuxingmingxiWuGong, String shuxingmingxiZhuangBei) {
        ValueEnHancePercent per = ValueEnHancePercent.parse(null, shuxingmingxiPet);
        per = ValueEnHancePercent.parse(per, shuxingmingxiQianNeng);
        per = ValueEnHancePercent.parse(per, shuxingmingxiShenQi);
        per = ValueEnHancePercent.parse(per, shuxingmingxiShenShou);
        per = ValueEnHancePercent.parse(per, shuxingmingxiXingHun);
        per = ValueEnHancePercent.parse(per, shuxingmingxiTuiBian);
        per = ValueEnHancePercent.parse(per, shuxingmingxiWuGong);
        per = ValueEnHancePercent.parse(per, shuxingmingxiZhuangBei);

        String[] heroInfos = renWuKeHuDuanJiChuShuxing.split(" ");
        String[] petInfos = chongWuKeHuDuanJiChuShuxing.split(" ");

        BaseRole pet = new BaseRole(petInfos[0], WuXing.parse(petInfos[1]), (int) (parseUtil.str2Int(petInfos[2]) * per.petHp), 0, (int) (parseUtil.str2Int(petInfos[3]) * per.petAttack), (int) (parseUtil.str2Int(petInfos[4]) * per.petDefense), (int) (parseUtil.str2Int(petInfos[5]) * per.petAgility));
        Hero hero = new Hero(nickName, pet, (int) (parseUtil.str2Int(heroInfos[0]) * per.heroHp), 0, (int) (parseUtil.str2Int(heroInfos[2]) * per.heroAttack), (int) (parseUtil.str2Int(heroInfos[3]) * per.heroDefense), (int) (parseUtil.str2Int(heroInfos[4]) * per.heroAgility));
        return hero;
    }

    @Override
    public String toString() {
        return "角色：" +
                "\t宠物：" + pet +
                super.toString();
    }
}
