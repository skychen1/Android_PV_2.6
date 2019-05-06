package com.uhf.api.cls;

/**
 * describe ：
 *
 * @author : Yich
 * date: 2019/4/18
 */
public class GpiInfo_ST {
    public int gpiCount;
    public GpiState_ST[] gpiStats = new GpiState_ST[8];

    public GpiInfo_ST() {
        for (int i = 0; i < 8; ++i) {
            this.gpiStats[i] = new GpiState_ST();
        }

    }
}
