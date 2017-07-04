package szz.com.baselib.entity.events;

import java.util.ArrayList;

import szz.com.baselib.entity.ZaHuoItem;

/**
 * 作者：Ying.Huang on 2017/7/4 15:33
 * Version v1.0
 * 描述：
 */

public class ZaHuoItemRsp {

    public final ArrayList<ZaHuoItem> zaHuoItems;

    public ZaHuoItemRsp(ArrayList<ZaHuoItem> zaHuoItems) {
        this.zaHuoItems = zaHuoItems;
    }
}
