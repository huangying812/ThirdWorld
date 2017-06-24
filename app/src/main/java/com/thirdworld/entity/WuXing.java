package com.thirdworld.entity;

/**
 * 作者：Ying.Huang on 2017/6/24 12:19
 * Version v1.0
 * 描述：
 */

public enum WuXing {

    GOLDEN, WOOD, WATER, FIRE, EARTH, NON;

    private static final String[] ENUMS = new String[]{
            "金",
            "木",
            "水",
            "火",
            "土",
            "无"
    };

    public String getValue() {
        return ENUMS[ordinal()];
    }

    public static WuXing parse(String petInfo) {
        WuXing wuXing;
        switch (petInfo) {
            case "金":
            case "jin":
                wuXing = GOLDEN;
                break;
            case "木":
            case "mu":
                wuXing = WOOD;
                break;
            case "水":
            case "shui":
                wuXing = WATER;
                break;
            case "火":
            case "huo":
                wuXing = FIRE;
                break;
            case "土":
            case "tu":
                wuXing = EARTH;
                break;
            case "无":
            default:
                wuXing = NON;
                break;
        }
        return wuXing;
    }
}
