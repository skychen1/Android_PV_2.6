package cn.rivamed.device.ClientHandler.uhfClientHandler.ColuClient;

import android.util.Log;

import com.clou.uhf.G3Lib.CLReader;
import com.clou.uhf.G3Lib.Enumeration.eReadType;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.rivamed.FunctionCode;
import cn.rivamed.device.ClientHandler.DeviceHandler;
import cn.rivamed.device.ClientHandler.NettyDeviceClientHandler;
import cn.rivamed.device.ClientHandler.uhfClientHandler.UhfHandler;
import cn.rivamed.device.DeviceType;
import cn.rivamed.model.TagInfo;
import io.netty.util.internal.StringUtil;

/**
 * @Author 郝小鹏
 * @Description
 * @Date: Created in 2018-07-11 13:37
 * @Modyfied By :
 */
public class ColuUhfReaderHandler extends NettyDeviceClientHandler implements UhfHandler, DeviceHandler {

    static final String LOG_TAG = "DEV_COLU_C";

    /**
     * 科陆的链接ID
     */
    String connId;
    Thread thread;
    boolean threadKeepAlive = false;
    Map<String, List<TagInfo>> epcs = new HashMap<>();
    Date lastReciveTime = new Date();
    String userInfo;
    boolean scanMode = false;
    int antNum = 8;


    String identification;

    public String getIdentification() {
        return this.identification;
    }

    protected void setIdentification(String value) {
        this.identification = value;
    }



    public ColuUhfReaderHandler(String connId, String mac) {
        this.connId = connId;
        this.setIdentification(mac);
        try {
            antNum = CLReader._Config.GetReaderANT(connId);
        } catch (InterruptedException e) {

        }

        thread = new Thread(new Runnable() {  //启动线程
            @Override
            public void run() {
                ColuHandlerThreadFun();
            }
        });
        thread.start();
    }

    public synchronized void UpdateLastReadTime() {
        this.lastReciveTime = new Date();
    }

    /**
     * 内部线程方法；
     * 用于 检测心跳  ；；  科陆自己的心跳基本没用，无法判断设备是否已断开
     * 用于检测扫描是否结束  ；；逻辑为  1000毫秒内无新标签，判断结束
     */
    public void ColuHandlerThreadFun() {
        threadKeepAlive = true;
        while (threadKeepAlive) {
            Date current = new Date();
            if (scanMode && current.getTime() - lastReciveTime.getTime() > 1000) {
                StopScan();
                if (this.messageEventCallBack != null) {   //发送回调
                    this.messageEventCallBack.OnUhfScanRet(true, this.getIdentification(), userInfo, epcs);
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {

                    }
                    this.messageEventCallBack.OnUhfScanComplete(true, this.getIdentification());
                }
                epcs.clear();
                scanMode = false;
            }
            if ((current.getTime() - lastReciveTime.getTime()) > 5000) {
                Log.w(LOG_TAG, "Colu Client心跳检测异常，将强行断开");
                if (CheckKeepAlive()) {
                    Close();
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }

    /***
     * 检测心跳
     * @return
     */
    private boolean CheckKeepAlive() {
        String s = null;
        try {
            s = CLReader._Config.GetReaderProperty(this.connId);
        } catch (InterruptedException e) {
            return false;
        }
        return StringUtil.isNullOrEmpty(s);
    }

    /**
     * 内部方法，用于和 Colu Service 互动；
     */
    public synchronized void AppendNewTag(String epc, TagInfo tagInfo) {
        UpdateLastReadTime();
        if (this.epcs.containsKey(epc)) {
            this.epcs.get(epc).add(tagInfo);
        } else {
            List<TagInfo> tagInfos = new ArrayList<>();
            tagInfos.add(tagInfo);
            this.epcs.put(epc, tagInfos);
        }
    }

    @Override
    public DeviceType getDeviceType() {
        return DeviceType.ColuUhfReader;
    }

    @Override
    public String getRemoteIP() {
        return connId.substring(0,connId.indexOf(":"));
    }

    @Override
    public int Close() {
        threadKeepAlive = false;
        CLReader.CloseConn(connId);
        return FunctionCode.SUCCESS;
    }

    /**
     * 获取所有天线的合集
     */
    private int getAllAnt() {
        int antNo = 0;
        for (int i = 0; i < antNum; i++) {
            if (i == 0) {
                antNo += 2 >> 1;
            } else {
                antNo += 2 << (i - 1);
            }
        }
        return antNo;
    }

    /**
     * 开始扫描
     */

    public int StartScan() {
        if (scanMode) {
            return FunctionCode.DEVICE_BUSY;
        }

        int ret = CLReader._Tag6C.GetEPC(connId, getAllAnt(), eReadType.Inventory);
        if (ret == 0) {
            return FunctionCode.SUCCESS;
        } else {
            Log.e(LOG_TAG, "启动科陆RFID扫描失败:errorCode=" + ret);
            return FunctionCode.SUCCESS;
        }
    }

    /**
     * 停止扫描
     */
    public int StopScan() {
        try {
            CLReader.Stop(connId);
            return FunctionCode.SUCCESS;
        } catch (Exception ex) {
            Log.e(LOG_TAG, "停止扫描发生错误:" + ex.getMessage());
            Close();
        }
        return FunctionCode.OPERATION_FAIL;
    }

    @Override
    public int SetPower(int power) {
        if (power < 0 || power > 33) {
            return FunctionCode.PARAM_ERROR;
        }

        HashMap<Integer, Integer> everyAntPower = new HashMap<>();
        for (int i = 0; i < antNum; i++) {
            everyAntPower.put(i, power);
        }


        try {
            int ret = CLReader._Config.SetANTPowerParam(this.connId, everyAntPower);
            if (ret == 0) {
                if (this.messageEventCallBack != null) {
                    this.messageEventCallBack.OnUhfSetPowerRet(this.getIdentification(), true);
                    return FunctionCode.SUCCESS;
                }

            }
        } catch (InterruptedException e) {
            Log.e(LOG_TAG, "设置功率发生错误;", e);
        }
        this.messageEventCallBack.OnUhfSetPowerRet(this.getIdentification(), false);
        return FunctionCode.OPERATION_FAIL;
    }

    @Override
    public int QueryPower() {
        try {
            HashMap<Integer, Integer> everyAntPower = CLReader._Config.GetANTPowerParam(this.connId);
            if (everyAntPower != null && everyAntPower.size() > 0) {
                int i = 0;
                for (Map.Entry<Integer, Integer> p : everyAntPower.entrySet()) {
                    if (p.getValue() < i) {
                        i = p.getValue();
                    }
                }
                if (this.messageEventCallBack != null) {
                    this.messageEventCallBack.OnUhfQueryPowerRet(this.getIdentification(), true, i);
                }
                return FunctionCode.SUCCESS;
            }

        } catch (InterruptedException e) {
            Log.e(LOG_TAG, "获取功率发生错误;", e);
        }
        if (this.messageEventCallBack != null) {
            this.messageEventCallBack.OnUhfQueryPowerRet(this.getIdentification(), false, -1);
        }
        return FunctionCode.OPERATION_FAIL;

    }

    @Override
    public int Reset() {
        threadKeepAlive = false;
// TODO: 2018-07-18  在SDK中未找到Reset 或类似的方法
        return FunctionCode.DEVICE_NOT_SUPPORT;
    }


    MessageEventCallBack messageEventCallBack;

    public void RegisterMessageEvent(MessageEventCallBack messageEventCallBack) {
        this.messageEventCallBack = messageEventCallBack;
    }

    public interface MessageEventCallBack {
        void OnUhfScanRet(boolean success, String deviceId, String userInfo, Map<String, List<TagInfo>> epcs);

        void OnUhfScanComplete(boolean success, String deviceId);

        /**
         * UHF READER 设置功率结果通知
         *
         * @param success  是否成功
         * @param deviceId 设备ID
         */
        void OnUhfSetPowerRet(String deviceId, boolean success);

        /**
         * UHF READER 获取功率结果通知
         *
         * @param success  是否成功
         * @param deviceId 设备ID
         * @param power    功率
         */
        void OnUhfQueryPowerRet(String deviceId, boolean success, int power);


    }
}
