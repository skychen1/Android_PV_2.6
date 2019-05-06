package com.ruihua.reader.local.corelinks;

import com.rivamed.libdevicesbase.base.FunctionCode;
import com.ruihua.reader.local.LocalReaderManager;
import com.ruihua.reader.local.callback.LocalReaderOperate;
import com.uhf.api.cls.Inv_Potl;
import com.uhf.api.cls.Inv_Potls_ST;
import com.uhf.api.cls.JniModuleAPI;
import com.uhf.api.cls.SL_TagProtocol;

/**
 * describe ： 芯联USB连接reader管理类
 *
 * @author : Yich
 * date: 2019/4/18
 */
public class CoreLinksManager implements LocalReaderOperate {

    /**
     * deviceId 设备id，唯一标识
     * hReader 设备打开的标识，操作需要用
     */
    private LocalReaderManager localReaderManager;
    private String deviceId;
    private int[] hReader = new int[1];

    public CoreLinksManager(LocalReaderManager manager) {
        localReaderManager = manager;
    }

    @Override
    public int connect() {
        //调用本地方法进行连接
        int ret = JniModuleAPI.InitReader_Notype(hReader, CoreLinksConfig.READER_COM_STR, CoreLinksConfig.PORT_NUM);
        //如果连接失败
        if (ret != 0) {
            return FunctionCode.CONNECT_FAILED;
        }
        Inv_Potls_ST ipst = new Inv_Potls_ST();
        ipst.potlcnt = 1;
        ipst.potls = new Inv_Potl[ipst.potlcnt];
        for (int i = 0; i < ipst.potlcnt; i++) {
            Inv_Potl ipl = new Inv_Potl();
            ipl.weight = 30;
            ipl.potl = SL_TagProtocol.SL_TAG_PROTOCOL_GEN2;
            ipst.potls[i] = ipl;
        }
        byte[] data = new byte[31];
        data[0] = (byte) ipst.potlcnt;
        for (int ilen = 0; ilen < data[0]; ++ilen) {
            data[ilen * 5 + 1] = (byte) ipst.potls[ilen].potl.value();
            data[ilen * 5 + 2] = (byte) ((ipst.potls[ilen].weight & -16777216) >> 24);
            data[ilen * 5 + 3] = (byte) ((ipst.potls[ilen].weight & 16711680) >> 16);
            data[ilen * 5 + 4] = (byte) ((ipst.potls[ilen].weight & '\uff00') >> 8);
            data[ilen * 5 + 5] = (byte) (ipst.potls[ilen].weight & 255);
        }
        return 0;

    }

    @Override
    public int disConnect() {
        return 0;
    }

    @Override
    public int setPower(int powerInt) {
        return 0;
    }

    @Override
    public int getPower() {
        return 0;
    }

    @Override
    public int getFrequency() {
        return 0;
    }

    @Override
    public int startScan() {
        return 0;
    }

    @Override
    public int stopScan() {
        return 0;
    }

    @Override
    public int reScan() {
        return 0;
    }

    @Override
    public int reset() {
        return 0;
    }

    @Override
    public int delOneEpc(String epc) {
        return 0;
    }

    @Override
    public String getProducer() {
        return null;
    }

    @Override
    public String getVersion() {
        return null;
    }
}
