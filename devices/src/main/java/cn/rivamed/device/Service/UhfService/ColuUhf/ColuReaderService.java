package cn.rivamed.device.Service.UhfService.ColuUhf;

import android.util.Log;

import com.clou.uhf.G3Lib.CLReader;
import com.clou.uhf.G3Lib.ClouInterface.IAsynchronousMessage;
import com.clou.uhf.G3Lib.Protocol.Tag_Model;

import java.io.Closeable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.rivamed.DeviceManager;
import cn.rivamed.device.ClientHandler.uhfClientHandler.ColuClient.ColuUhfReaderHandler;
import cn.rivamed.device.ClientHandler.DeviceHandler;
import cn.rivamed.device.ClientHandler.uhfClientHandler.UhfClientMessage;
import cn.rivamed.device.ClientHandler.uhfClientHandler.UhfHandler;
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
        Log.d(log_tag, "启动科陆 Reader 服务器模式，Port=" + coluPort);
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

            ColuUhfReaderHandler handler = new ColuUhfReaderHandler(s);
            handler.RegisterMessageListener(new ColuMessageListener());
        }

        public void PortClosing(String s) {
            String mac = StringUtil.EMPTY_STRING;
            if (conneIDs.containsKey(s)) {
                mac = conneIDs.get(s);
                conneIDs.remove(s);
            }
            if(StringUtil.isNullOrEmpty(mac)){
                if(getDeviceManager()!=null){
                    DeviceHandler handler= getDeviceManager().getDeviceClientHandler(mac);
                    if(handler!=null){
                        handler.Close();
                    }
                }
            }
        }

        @Override
        public void OutPutTags(Tag_Model tag_model) {
            String connId = tag_model._ReaderName;
            if (getDeviceManager() != null) {
                TagInfo tagInfo = new TagInfo();
                tagInfo.setAnt(tag_model._ANT_NUM);
                tagInfo.setRssi(tag_model._RSSI);
                tagInfo.setPc(tag_model._PC);
                if (ColuReaderService.this.getDeviceManager() != null) {
                    DeviceHandler handler = ColuReaderService.this.getDeviceManager().getDeviceClientHandler(ColuReaderService.this.conneIDs.get(tag_model._ReaderName));
                    if (handler != null) {
                        if (handler instanceof ColuUhfReaderHandler) {
                            ((ColuUhfReaderHandler) handler).AppendNewTag(tag_model._EPC, tagInfo);
                        }
                    }
                }
            }
        }

        public void OutPutTagsOver() {
            //经测试，基本无法触发
        }

        @Override
        public void GPIControlMsg(int i, int i1, int i2) {

        }
    }

    class ColuMessageListener implements UhfClientMessage {

        DeviceHandler deviceHandler;

        @Override
        public DeviceHandler getDeviceHandler() {
            return this.deviceHandler;
        }

        @Override
        public void setDeviceHandler(DeviceHandler handler) {
            this.deviceHandler = handler;
        }

        @Override
        public void OnDisconnected() {
            if (ColuReaderService.this.conneIDs.containsKey(((ColuUhfReaderHandler) deviceHandler).getConnId())) {
                ColuReaderService.this.conneIDs.remove(((ColuUhfReaderHandler) deviceHandler).getConnId());
            }
            if (ColuReaderService.this.getDeviceManager() != null) {
                ColuReaderService.this.getDeviceManager().fireDeviceDisconnected(deviceHandler.getIdentification());
            }
        }

        @Override
        public void OnConnected() {

            if (deviceHandler instanceof ColuUhfReaderHandler) {
                ColuReaderService.this.conneIDs.put(((ColuUhfReaderHandler) deviceHandler).getConnId(), deviceHandler.getIdentification());
            }


            if (ColuReaderService.this.getDeviceManager() != null) {
                ColuReaderService.this.getDeviceManager().AppendConnectedDevice(deviceHandler.getIdentification(), deviceHandler);
                if (ColuReaderService.this.getDeviceManager().getDeviceCallBack() != null) {
                    ColuReaderService.this.getDeviceManager().getDeviceCallBack().OnDeviceConnected(this.deviceHandler.getDeviceType(), this.deviceHandler.getIdentification());
                }
            }
        }

        @Override
        public void OnUhfScanRet(boolean success, String deviceId, String userInfo, Map<String, List<TagInfo>> epcs) {
            if (ColuReaderService.this.getDeviceManager() != null) {
                if (ColuReaderService.this.getDeviceManager().getDeviceCallBack() != null) {
                    ColuReaderService.this.getDeviceManager().getDeviceCallBack().OnUhfScanRet(success, this.deviceHandler.getIdentification(), null, epcs);
                }
            }
        }

        @Override
        public void OnUhfScanComplete(boolean success, String deviceId) {
            if (ColuReaderService.this.getDeviceManager() != null) {
                if (ColuReaderService.this.getDeviceManager().getDeviceCallBack() != null) {
                    ColuReaderService.this.getDeviceManager().getDeviceCallBack().OnUhfScanComplete(success, this.deviceHandler.getIdentification());
                }
            }
        }

        @Override
        public void OnUhfSetPowerRet(String deviceId, boolean success) {
            if (ColuReaderService.this.getDeviceManager() != null) {
                if (ColuReaderService.this.getDeviceManager().getDeviceCallBack() != null) {
                    ColuReaderService.this.getDeviceManager().getDeviceCallBack().OnUhfSetPowerRet(deviceId, success);
                }
            }
        }

        @Override
        public void OnUhfQueryPowerRet(String deviceId, boolean success, int power) {
            if (ColuReaderService.this.getDeviceManager() != null) {
                if (ColuReaderService.this.getDeviceManager().getDeviceCallBack() != null) {
                    ColuReaderService.this.getDeviceManager().getDeviceCallBack().OnUhfQueryPowerRet(deviceId, success, power);
                }
            }
        }

    }

}
