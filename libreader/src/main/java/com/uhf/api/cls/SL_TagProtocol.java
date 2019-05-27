package com.uhf.api.cls;

/**
 * describe ：
 *
 * @author : Yich
 * date: 2019/4/18
 */
public enum SL_TagProtocol {
    SL_TAG_PROTOCOL_NONE(0),
    SL_TAG_PROTOCOL_ISO180006B(3),
    SL_TAG_PROTOCOL_GEN2(5),
    SL_TAG_PROTOCOL_ISO180006B_UCODE(6),
    SL_TAG_PROTOCOL_IPX64(7),
    SL_TAG_PROTOCOL_IPX256(8);

    int p_v;

    SL_TagProtocol(int v) {
        this.p_v = v;
    }

    public int value() {
        return this.p_v;
    }

    public SL_TagProtocol valueOf(int value) {
        switch (value) {
            case 0:
                return SL_TAG_PROTOCOL_NONE;
            case 1:
            case 2:
            case 4:
            default:
                return null;
            case 3:
                return SL_TAG_PROTOCOL_ISO180006B;
            case 5:
                return SL_TAG_PROTOCOL_GEN2;
            case 6:
                return SL_TAG_PROTOCOL_ISO180006B_UCODE;
            case 7:
                return SL_TAG_PROTOCOL_IPX64;
            case 8:
                return SL_TAG_PROTOCOL_IPX256;
        }
    }
}
