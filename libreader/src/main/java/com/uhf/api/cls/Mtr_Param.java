package com.uhf.api.cls;

/**
 * describe ：
 *
 * @author : Yich
 * date: 2019/4/18
 */
public enum Mtr_Param {
    MTR_PARAM_POTL_GEN2_SESSION(0),
    MTR_PARAM_POTL_GEN2_Q(1),
    MTR_PARAM_POTL_GEN2_TAGENCODING(2),
    MTR_PARAM_POTL_GEN2_MAXEPCLEN(3),
    MTR_PARAM_RF_ANTPOWER(4),
    MTR_PARAM_RF_MAXPOWER(5),
    MTR_PARAM_RF_MINPOWER(6),
    MTR_PARAM_TAG_FILTER(7),
    MTR_PARAM_TAG_EMBEDEDDATA(8),
    MTR_PARAM_TAG_INVPOTL(9),
    MTR_PARAM_READER_CONN_ANTS(10),
    MTR_PARAM_READER_AVAILABLE_ANTPORTS(11),
    MTR_PARAM_READER_IS_CHK_ANT(12),
    MTR_PARAM_READER_VERSION(13),
    MTR_PARAM_READER_IP(14),
    MTR_PARAM_FREQUENCY_REGION(15),
    MTR_PARAM_FREQUENCY_HOPTABLE(16),
    MTR_PARAM_POTL_GEN2_BLF(17),
    MTR_PARAM_POTL_GEN2_WRITEMODE(18),
    MTR_PARAM_POTL_GEN2_TARGET(19),
    MTR_PARAM_TAGDATA_UNIQUEBYANT(20),
    MTR_PARAM_TAGDATA_UNIQUEBYEMDDATA(21),
    MTR_PARAM_TAGDATA_RECORDHIGHESTRSSI(22),
    MTR_PARAM_RF_TEMPERATURE(23),
    MTR_PARAM_RF_HOPTIME(24),
    MTR_PARAM_RF_LBT_ENABLE(25),
    MTR_PARAM_RF_SUPPORTEDREGIONS(26),
    MTR_PARAM_POTL_SUPPORTEDPROTOCOLS(27),
    MTR_PARAM_POTL_ISO180006B_BLF(28),
    MTR_PARAM_POTL_GEN2_TARI(29),
    MTR_PARAM_TRANS_TIMEOUT(30),
    MTR_PARAM_TAG_EMDSECUREREAD(31),
    MTR_PARAM_TRANSMIT_MODE(32),
    MTR_PARAM_POWERSAVE_MODE(33),
    MTR_PARAM_TAG_SEARCH_MODE(34),
    MTR_PARAM_POTL_ISO180006B_MODULATION_DEPTH(35),
    MTR_PARAM_POTL_ISO180006B_DELIMITER(36),
    MTR_PARAM_READER_WATCHDOG(42),
    MTR_PARAM_READER_ERRORDATA(43),
    MTR_PARAM_RF_HOPANTTIME(44);

    private int value = 0;

    private Mtr_Param(int value) {
        this.value = value;
    }

    public Mtr_Param valueOf(int value) {
        switch (value) {
            case 0:
                return MTR_PARAM_POTL_GEN2_SESSION;
            case 1:
                return MTR_PARAM_POTL_GEN2_Q;
            case 2:
                return MTR_PARAM_POTL_GEN2_TAGENCODING;
            case 3:
                return MTR_PARAM_POTL_GEN2_MAXEPCLEN;
            case 4:
                return MTR_PARAM_RF_ANTPOWER;
            case 5:
                return MTR_PARAM_RF_MAXPOWER;
            case 6:
                return MTR_PARAM_RF_MINPOWER;
            case 7:
                return MTR_PARAM_TAG_FILTER;
            case 8:
                return MTR_PARAM_TAG_EMBEDEDDATA;
            case 9:
                return MTR_PARAM_TAG_INVPOTL;
            case 10:
                return MTR_PARAM_READER_CONN_ANTS;
            case 11:
                return MTR_PARAM_READER_AVAILABLE_ANTPORTS;
            case 12:
                return MTR_PARAM_READER_IS_CHK_ANT;
            case 13:
                return MTR_PARAM_READER_VERSION;
            case 14:
                return MTR_PARAM_READER_IP;
            case 15:
                return MTR_PARAM_FREQUENCY_REGION;
            case 16:
                return MTR_PARAM_FREQUENCY_HOPTABLE;
            case 17:
                return MTR_PARAM_POTL_GEN2_BLF;
            case 18:
                return MTR_PARAM_POTL_GEN2_WRITEMODE;
            case 19:
                return MTR_PARAM_POTL_GEN2_TARGET;
            case 20:
                return MTR_PARAM_TAGDATA_UNIQUEBYANT;
            case 21:
                return MTR_PARAM_TAGDATA_UNIQUEBYEMDDATA;
            case 22:
                return MTR_PARAM_TAGDATA_RECORDHIGHESTRSSI;
            case 23:
                return MTR_PARAM_RF_TEMPERATURE;
            case 24:
                return MTR_PARAM_RF_HOPTIME;
            case 25:
                return MTR_PARAM_RF_LBT_ENABLE;
            case 26:
                return MTR_PARAM_RF_SUPPORTEDREGIONS;
            case 27:
                return MTR_PARAM_POTL_SUPPORTEDPROTOCOLS;
            case 28:
                return MTR_PARAM_POTL_ISO180006B_BLF;
            case 29:
                return MTR_PARAM_POTL_GEN2_TARI;
            case 30:
                return MTR_PARAM_TRANS_TIMEOUT;
            case 31:
                return MTR_PARAM_TAG_EMDSECUREREAD;
            case 32:
                return MTR_PARAM_TRANSMIT_MODE;
            case 33:
                return MTR_PARAM_POWERSAVE_MODE;
            case 34:
                return MTR_PARAM_TAG_SEARCH_MODE;
            case 35:
                return MTR_PARAM_POTL_ISO180006B_MODULATION_DEPTH;
            case 36:
                return MTR_PARAM_POTL_ISO180006B_DELIMITER;
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            default:
                return null;
            case 42:
                return MTR_PARAM_READER_WATCHDOG;
            case 43:
                return MTR_PARAM_READER_ERRORDATA;
            case 44:
                return MTR_PARAM_RF_HOPANTTIME;
        }
    }

    public int value() {
        return this.value;
    }
}
