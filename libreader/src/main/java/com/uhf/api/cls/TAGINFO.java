package com.uhf.api.cls;

/**
 * describe ：
 *
 * @author : Yich
 * date: 2019/4/18
 */
public class TAGINFO {
    public byte AntennaID;
    public int Frequency;
    public int TimeStamp;
    public short EmbededDatalen;
    public byte[] EmbededData = new byte[128];
    public byte[] Res = new byte[2];
    public short Epclen;
    public byte[] PC = new byte[2];
    public byte[] CRC = new byte[2];
    public byte[] EpcId = new byte[62];
    public int Phase;
    public SL_TagProtocol protocol;
    public int ReadCnt;
    public int RSSI;
    public TAGINFO() {
    }

    @Override
    public Object clone() {
        TAGINFO o = null;
        try {
            o = (TAGINFO) super.clone();
            return o;
        } catch (CloneNotSupportedException var3) {
            return null;
        }
    }


}
