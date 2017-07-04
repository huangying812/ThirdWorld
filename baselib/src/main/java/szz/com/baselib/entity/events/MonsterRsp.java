package szz.com.baselib.entity.events;

import java.util.ArrayList;

import szz.com.baselib.entity.GoldenHunterMonster;

/**
 * 作者：Ying.Huang on 2017/7/4 17:22
 * Version v1.0
 * 描述：
 */

public class MonsterRsp {

    public ArrayList<GoldenHunterMonster> monsterItems;

    public MonsterRsp(ArrayList<GoldenHunterMonster> monsterItems) {
        this.monsterItems = monsterItems;
    }
}
