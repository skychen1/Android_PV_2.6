package cn.rivamed.device.Service.UhfService.ColuUhf;

import android.util.Log;

import com.clou.uhf.G3Lib.CLReader;
import com.clou.uhf.G3Lib.ClouInterface.IAsynchronousMessage;
import com.clou.uhf.G3Lib.Models.Tag_Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.rivamed.DeviceManager;
import cn.rivamed.device.ClientHandler.uhfClientHandler.ColuClient.ColuUhfReaderHandler;
import cn.rivamed.device.ClientHandler.DeviceHandler;
import cn.rivamed.device.DeviceType;
import cn.rivamed.device.Service.BaseService;
import cn.rivamed.device.Service.UhfService.UhfService;
import cn.rivamed.model.TagInfo;
import io.netty.util.internal.StringUtil;

/**
 * @Author 郝小鹏
 * @Description
 * @Date: Created in 2018-07-11 13:33
 * @Modyfied By :
 */
public class ColuReaderService extends BaseService implements UhfService {

    String log_tag = "DEV_COLU_S";

    Integer coluPort;

    public ColuReaderService(int port) {
        this.coluPort = port;
    }

    /**
     * 记录已连接的设备的MAC地址和链接ID
     * Key：connid
     * Value：   Mac address  ，其中connid用于Clou内部进行操作，MAC用于Rivamed系统中的设备标识
     */
    Map<String, String> conneIDs = new HashMap<>();


    @Override
    public boolean isAlive() {
        if (CLReader.LISTENER == null) return false;
        return CLReader.LISTENER.isLisStartOrStop;
    }

    @Override
    public boolean StartService(DeviceManager deviceManager) {
        super.StartService(deviceManager);
        Log.d(log_tag, "启动科陆 Reader 服务器模式");
        return CLReader.OpenTcpServer("0.0.0.0", coluPort.toString(), new CLReaderMessageCallback());
    }

    @Override
    public boolean StopService() {
        CLReader.CloseTcpServer();
        if (CLReader.LISTENER == null) return true;
        return !CLReader.LISTENER.isLisStartOrStop;
    }


    private class CLReaderMessageCallback implements IAsynchronousMessage {

        public void WriteDebugMsg(String s) {

        }

        public void WriteLog(String s) {
            Log.d(log_tag, "Colu Reader Logs:" + s);
        }

        public void PortConnecting(String s) {
            Log.i(log_tag, "接收到ColuReader链接:Connid=" + s);
            String mac = StringUtil.EMPTY_STRING;
            try {
                mac = CLReader._Config.GetReaderMacParam(s);
                if (mac.equals("Timeout！") || mac.equals("Parameters error！")) {
                    mac = StringUtil.EMPTY_STRING;
                }
            } catch (InterruptedException e) {
                Log.e(log_tag, "获取MAC地址失败:connid=" + s);
            }
            if (mac.equals(StringUtil.EMPTY_STRING)) { //无法获取，则断开
                CLReader.CloseConn(s);
            } else {
                ColuReaderService.this.conneIDs.put(s, mac);
                ColuUhfReaderHandler handler = new ColuUhfReaderHandler(s, mac);
                handler.RegisterMessageEvent(new ColuUhfReaderHandler.MessageEventCallBack() {
                    @Override
                    public void OnUhfScanRet(boolean success, String deviceId, String userInfo, Map<String, List<TagInfo>> epcs) {
                        if (getDeviceManager() != null) {
                            if (getDeviceManager().getDeviceCallBack() != null) {
                                getDeviceManager().getDeviceCallBack().OnUhfScanRet(success, deviceId, userInfo, epcs);
                            }
                        }
                    }

                    @Override
                    public void OnUhfScanComplete(boolean success, String deviceId) {
                        if (getDeviceManager() != null) {
                            if (getDeviceManager().getDeviceCallBack() != null) {
                                getDeviceManager().getDeviceCallBack().OnUhfScanComplete(success, deviceId);
                            }
                        }
                    }

                    @Override
                    public void OnUhfSetPowerRet(String deviceId, boolean success) {
                        if (getDeviceManager() != null) {
                            if (getDeviceManager().getDeviceCallBack() != null) {
                                getDeviceManager().getDeviceCallBack().OnUhfSetPowerRet( deviceId,success);
                            }
                        }
                    }

                    @Override
                    public void OnUhfQueryPowerRet(String deviceId, boolean success, int power) {
                        if (getDeviceManager() != null) {
                            if (getDeviceManager().getDeviceCallBack() != null) {
                                getDeviceManager().getDeviceCallBack().OnUhfQueryPowerRet(deviceId,success,power);
                            }
                        }
                    }
                });
                if (getDeviceManager() != null) {
                    getDeviceManager().RemoveConnectedDevice(handler.getIdentification());
                    if (getDeviceManager().getDeviceCallBack() != null) {
                        getDeviceManager().getDeviceCallBack().OnDeviceConnected(DeviceType.ColuUhfReader, mac);
                    }
                }
            }
        }

        public void PortClosing(String s) {
            String mac = StringUtil.EMPTY_STRING;
            if (conneIDs.containsKey(s)) {
                mac = conneIDs.get(s);
                conneIDs.remove(s);
            }
            if (!StringUtil.isNullOrEmpty(mac))
                if (getDeviceManager() != null) {
                    if (getDeviceManager() != null) {
                        if (getDeviceManager().getDeviceCallBack() != null) {
                            getDeviceManager().getDeviceCallBack().OnDeviceDisConnected(DeviceType.ColuUhfReader, mac);
                        }
                    }
                }
        }

        public void OutPutTags(Tag_Model tag_model) {
            String connId = tag_model._ReaderName;
            if (getDeviceManager() != null) {
                TagInfo tagInfo = new TagInfo();
                tagInfo.setAnt(tag_model._ANT_NUM);
                tagInfo.setRssi(tag_model._RSSI);
                tagInfo.setPc(tag_model._PC);
                DeviceHandler handler = getDeviceManager().getDeviceClientHandler(conneIDs.get(connId));
                if (handler != null) {
                    if (handler instanceof ColuUhfReaderHandler) {
                        ((ColuUhfReaderHandler) handler).AppendNewTag(tag_model._EPC, tagInfo);
                    }
                }
            }
        }

        public void OutPutTagsOver() {
            //经测试，基本无法触发
        }

        public void GPIControlMsg(int i, int i1, int i2) {

        }
    }
}
