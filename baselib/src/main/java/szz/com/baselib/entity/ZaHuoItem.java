package szz.com.baselib.entity;

import szz.com.baselib.singleton.ConnectManager;

/**
 * 作者：Ying.Huang on 2017/7/4 14:45
 * Version v1.0
 * 描述：
 */

public class ZaHuoItem {

    public static final String[] ZaHuoItemHeader = new String[]{
            "序号",
            "物品",
            "数量",
            "货币",
            "金额",
            "状态"
    };

    public int index;
    public String name;
    public int amount;
    public int price;
    public boolean canBuy;
    public @ConnectManager.MoneyType int moneyType;

    public static String[] GUDING_BUYS = new String[]{
        "",
        "",
        "",
        "",
        "",
        "",
        "",
    };

    public static ZaHuoItem parse(int index,String itemStr,int state,@ConnectManager.MoneyType int moneyType){
        String[] split = itemStr.split(" ");
        ZaHuoItem zaHuoItem = new ZaHuoItem();
        zaHuoItem.index = index;
        zaHuoItem.moneyType = moneyType;
        zaHuoItem.canBuy = state == 0;
        zaHuoItem.name = split[0];
        zaHuoItem.amount = Integer.valueOf(split[1]);
        switch (moneyType) {

            case ConnectManager.MoneyType.guDingZiJin:
                zaHuoItem.price = Integer.valueOf(split[2]);
                break;
            case ConnectManager.MoneyType.yuanBao:
                zaHuoItem.price = Integer.valueOf(split[3]);
                break;
            case ConnectManager.MoneyType.zanZhuDian:
                zaHuoItem.price = Integer.valueOf(split[4]);
                break;
        }
        return zaHuoItem;
    }

    public boolean buyIfWorth(){
        if (canBuy && shouldBuy()) {
            ConnectManager.getInstance().buyZaHuo(index);
            return true;
        }
        return false;
    }

    private boolean shouldBuy() {
        switch (moneyType) {
            case ConnectManager.MoneyType.guDingZiJin:

                break;
            case ConnectManager.MoneyType.yuanBao:
                break;
            case ConnectManager.MoneyType.zanZhuDian:
                break;
        }
        return false;
    }

    public String getMoneyType() {

        String s = "未知";
        switch (moneyType) {
            case ConnectManager.MoneyType.guDingZiJin:
                s = "固定";
                break;
            case ConnectManager.MoneyType.yuanBao:
                s = "元宝";
                break;
            case ConnectManager.MoneyType.zanZhuDian:
                s = "赞助";
                break;
        }
        return s;
    }

    public String getBuyState() {
        if (canBuy) {
            return "可买";
        }
        return "售完";
    }

    public String getBuyTips() {
        if (canBuy) {
            return String.format("将要花费%1$d %2$s 购买 %3$d 个%4$s ,是否继续？", price, getMoneyType(), amount, name);
        } else {
            return String.format("价值%1$d %2$s 的 %3$d 个%4$s 已售完！", price, getMoneyType(), amount, name);
        }
    }
}
