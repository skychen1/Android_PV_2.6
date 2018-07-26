package cn.rivamed.device.ClientHandler.uhfClientHandler.ColuClient;

import android.util.Log;
import android.widget.ThemedSpinnerAdapter;

import com.clou.uhf.G3Lib.CLReader;
import com.clou.uhf.G3Lib.Enumeration.eReadType;
import com.clou.uhf.G3Lib.Param_Set;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.rivamed.FunctionCode;
import cn.rivamed.device.ClientHandler.DeviceHandler;
import cn.rivamed.device.ClientHandler.NettyDeviceClientHandler;
import cn.rivamed.device.ClientHandler.uhfClientHandler.UhfClientMessage;
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

    byte[] ants;


    String identification;

    public String getIdentification() {
        return this.identification;
    }

    public String getConnId() {
        return this.connId;
    }

    protected void setIdentification(String value) {
        this.identification = value;
    }


    public ColuUhfReaderHandler(String connId) {

        this.connId = connId;

        new Thread(() -> {
            //获取mac地址
            try {
                String version = CLReader._Config.GetReaderBaseBandSoftVersion(this.connId);

                Log.d(LOG_TAG, "获取设备版本" + version);

                String mac = CLReader._Config.GetReaderMacParam(this.connId);

                if (mac.equals("Timeout！") || mac.equals("Parameters error！")) {
                    Log.e(LOG_TAG, "获取UHF设备MAC地址失败，将强制断开");
                    Close();
                    return;
                }
                this.setIdentification(mac);
                try {
                    String sAnt = CLReader._Config.GetReaderANT2(this.connId);
                    Log.d(LOG_TAG, "查询使能天线 ret=" + sAnt);
                    String[] sAnts = sAnt.split(",");
                    ants = new byte[sAnts.length];
                    int i = 0;
                    for (String s : sAnts) {
                        byte b = Byte.parseByte(s);
                        ants[i++] = b;
                    }
                } catch (Exception ex) {
                    Log.e(LOG_TAG, "调用SDK获取天线失败，请联系科陆开发工程师进行解决！");
                    Close();
                    return;
                }


                new Thread(() -> {
                    if (ColuUhfReaderHandler.this.messageListener != null) {
                        ColuUhfReaderHandler.this.messageListener.OnConnected();
                    }
                }).start();
            } catch (InterruptedException e) {
            }
            thread = new Thread(new Runnable() {  //启动线程
                @Override
                public void run() {
                    ColuHandlerThreadFun();
                }
            });
            thread.start();
        }).start();
    }

    public synchronized void UpdateLastReadTime() {
        Date now = new Date();
        if (now.getTime() >= lastReciveTime.getTime()) {
            this.lastReciveTime = new Date();
        }
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
                new Thread(() -> {
                    if (this.messageListener != null) {   //发送回调
                        this.messageListener.OnUhfScanRet(true, this.getIdentification(), userInfo, epcs);
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {

                        }
                        this.messageListener.OnUhfScanComplete(true, this.getIdentification());
                    }
                    epcs.clear();
                    scanMode = false;
                }).start();
            }
            if (!scanMode && (current.getTime() - lastReciveTime.getTime()) > 10000) {
                if (!CheckKeepAlive()) {
                    Log.w(LOG_TAG, "Colu Client DEVICEID=" + getIdentification() + "心跳检测异常，连续满3次将强制断开，目前是第" + keepAliveErrorCount + "次");
                    if (keepAliveErrorCount >= 3) {
                        Close();
                    }
                }
            }
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
            }
        }
    }

    /**
     * 心跳错误基数
     * 心跳成功则 归0，失败则+1
     * <p>
     * 总集数满3 则强制断开
     */
    int keepAliveErrorCount = 0;

    /***
     * 检测心跳
     * @return
     */
    private boolean CheckKeepAlive() {
        String s = null;
        try {
            s = CLReader._Config.GetReaderBaseBandSoftVersion(this.connId);
            UpdateLastReadTime();
        } catch (InterruptedException e) {
            s = "";
        }
        boolean ret = StringUtil.isNullOrEmpty(s) || "Timeout！".equals(s) || "Parameters error！".equals(s);
        if (!ret) {
            Log.d(LOG_TAG, "鸿陆RFID CONNID=" + this.connId + "  DEVICEID=" + getIdentification() + " 心跳检测成功");
            keepAliveErrorCount = 0;
        } else {
            Log.e(LOG_TAG, "鸿陆RFID CONNID=" + this.connId + "  DEVICEID=" + getIdentification() + " 心跳检测失败 S=" + s);
            keepAliveErrorCount++;
        }
        return !ret;
    }

    /**
     * 内部方法，用于和 Colu Service 互动；
     */
    public synchronized void AppendNewTag(String epc, TagInfo tagInfo) {
        if (!scanMode) {
            CLReader.Stop(connId);
        }
        if (this.epcs.containsKey(epc)) {
            this.epcs.get(epc).add(tagInfo);
        } else {
            Log.d(LOG_TAG, "鸿陆RFID CONNID=" + this.connId + "  DEVICEID=" + getIdentification() + "扫描到新的EPC ：" + epc);
            UpdateLastReadTime();
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
        return connId.substring(0, connId.indexOf(":"));
    }

    @Override
    public int Close() {
        try {
            threadKeepAlive = false;
            CLReader.CloseConn(connId);
            Log.e(LOG_TAG, "已断开与设备 DeviceId=" + getIdentification() + "的连接");
        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage());
        } finally {
            if (this.messageListener != null) {
                this.messageListener.OnDisconnected();
            }
        }
        return FunctionCode.SUCCESS;
    }

    /**
     * 获取所有天线的合集
     */
    private int getAllAnt() {
        int antNo = 0;
        for (byte b : ants) {
            antNo |= (int) Math.pow(2, b - 1);
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
        Calendar calendar = Calendar.getInstance();    //至少扫描3秒
        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND, 5);
        lastReciveTime = calendar.getTime();

        if (ret == 0) {
            scanMode = true;
            return FunctionCode.SUCCESS;
        } else {
            Log.e(LOG_TAG, "启动鸿陆CONNID=" + this.connId + "  DEVICEID=" + getIdentification() + "RFID扫描失败:errorCode=" + ret);
            return FunctionCode.OPERATION_FAIL;
        }
    }

    /**
     * 停止扫描
     */
    public int StopScan() {
        try {

            scanMode = false;
            int ret = CLReader._Config.Stop(connId);
            Log.i(LOG_TAG, "鸿陆RFID " + this.connId + " 扫描结束:RET=" + (ret==0?"成功":"失败"));
            Thread.sleep(150);//听从鸿陆SDK工程师建议，这样可以避免下一次 启动扫描失败，但实际上依然会失败，目测需要停止1秒以上
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
        boolean success = false;

        HashMap<Integer, Integer> everyAntPower = new HashMap<>();
        for (int i = 0; i < ants.length; i++) {
            everyAntPower.put((int) ants[i], power);
        }


        try {
            int ret = CLReader._Config.SetANTPowerParam(this.connId, everyAntPower);
            if (ret == 0) {
                success = true;
            }
        } catch (InterruptedException e) {
            Log.e(LOG_TAG, "设置功率发生错误;", e);
        }

        boolean finalSuccess = success;
        new Thread(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {

            }
            if (this.messageListener != null) {
                this.messageListener.OnUhfSetPowerRet(this.getIdentification(), finalSuccess);
            }
        }).start();

        return finalSuccess ? FunctionCode.SUCCESS : FunctionCode.OPERATION_FAIL;
    }

    @Override
    public int QueryPower() {

        boolean success = false;
        int power = 100;
        try {
            HashMap<Integer, Integer> everyAntPower = CLReader._Config.GetANTPowerParam(this.connId);
            if (everyAntPower != null && everyAntPower.size() > 0) {
                for (Map.Entry<Integer, Integer> p : everyAntPower.entrySet()) {
                    if (p.getValue() < power) {
                        power = p.getValue();
                    }
                }
                success = true;
            }
        } catch (InterruptedException e) {
            Log.e(LOG_TAG, "获取功率发生错误;", e);
        }

        boolean finalSuccess = success;
        int finalPower = power;

        new Thread(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {

            }
            if (this.messageListener != null) {
                this.messageListener.OnUhfQueryPowerRet(this.getIdentification(), finalSuccess, finalPower);
            }
        }).start();


        return finalSuccess ? FunctionCode.SUCCESS : FunctionCode.OPERATION_FAIL;
    }

    @Override
    public List<Integer> getUhfAnts() {
        List<Integer> ret = new ArrayList<>();
        for (byte ant : ants) {
            ret.add((int) ant);
        }
        return ret;
    }

    @Override
    public int Reset() {
        threadKeepAlive = false;
        Param_Set.ReSetReader(this.connId);
        return FunctionCode.SUCCESS;
    }


    protected UhfClientMessage messageListener;

    public void RegisterMessageListener(UhfClientMessage messageListener) {
        this.messageListener = messageListener;
        this.messageListener.setDeviceHandler(this);
    }
}
