package cn.rivamed;


import android.content.Context;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import cn.rivamed.callback.DeviceCallBack;
import cn.rivamed.device.ClientHandler.DeviceHandler;
import cn.rivamed.device.ClientHandler.eth002Handler.Eth002ClientHandler;
import cn.rivamed.device.ClientHandler.uhfClientHandler.UhfHandler;
import cn.rivamed.device.ConnetedDevices;

import cn.rivamed.device.DeviceType;
import cn.rivamed.device.Service.Eth002Service.Eth002Service;
import cn.rivamed.device.Service.Eth002Service.Eth002ServiceType;
import cn.rivamed.device.Service.UhfService.ColuUhf.ColuReaderService;
import cn.rivamed.device.Service.UhfService.RodinBellUhf.RodinBellService;
import cn.rivamed.device.Service.UhfService.UhfDeviceType;
import cn.rivamed.device.Service.UhfService.UhfService;


public class DeviceManager {

    int uhf_Port = -1;
    int eth002_port = -1;


    public int getUhf_Port() {
        return uhf_Port;
    }

    public int getEth002_port() {
        return this.eth002_port;
    }

    /**
     * UHF 阅读器监听服务
     */
    UhfService uhfService;
    /**
     * Eth002 模块控制服务
     */
    Eth002Service eth002Service;


    /**
     * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
     * 单例设计
     */

    private DeviceManager() {
    }

    private static DeviceManager instance;

    public static DeviceManager getInstance() {
        if (instance == null) {
            instance = new DeviceManager();
        }
        return instance;
    }


    /**
     * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
     * 回调函数
     */


    DeviceCallBack deviceCallBack;
    ConnetedDevices connetedDevices = new ConnetedDevices();

    /**
     * 内部使用，保存已连接的客户端Handler
     */

    public ConnetedDevices getConnetedDevices() {
        return connetedDevices;
    }

    public DeviceHandler getDeviceClientHandler(String identification) {
        return connetedDevices.get(identification);
    }

    /***
     * 注册回调函数
     * @param deviceCallBack
     */
    public void RegisterDeviceCallBack(DeviceCallBack deviceCallBack) {
        this.deviceCallBack = deviceCallBack;
    }

    public DeviceCallBack getDeviceCallBack() {
        return deviceCallBack;
    }

    /***
     *>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
     * 设备管理
     *
     */

    /**
     * 设备管理内部使用
     * <p>
     * 触发设备断开事件
     */
    public void fireDeviceDisconnected(String deviceId) {
        if (this.getConnetedDevices().containsKey(deviceId)) {
            DeviceHandler deviceHandler = this.getDeviceClientHandler(deviceId);
            this.getConnetedDevices().remove(deviceId);
            if (this.deviceCallBack != null) {
                deviceCallBack.OnDeviceDisConnected(deviceHandler.getDeviceType(), deviceId);
            }
        }
    }

    public boolean CheckDeviceConnected(String deviceId) {
        return this.connetedDevices.containsKey(deviceId);
    }

    public class DeviceInfo {

        String identifition;
        DeviceType deviceType;
        String remoteIP;


        /**
         * 生产厂家
         * */
        String Product;
        /**
         * 版本
         * */
        String Version;


        public String getIdentifition() {
            return identifition;
        }

        public void setIdentifition(String identifition) {
            this.identifition = identifition;
        }

        public DeviceType getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(DeviceType deviceType) {
            this.deviceType = deviceType;
        }

        public String getRemoteIP() {
            return remoteIP;
        }

        public void setRemoteIP(String remoteIP) {
            this.remoteIP = remoteIP;
        }


        public String getProduct() {
            return Product;
        }

        public void setProduct(String product) {
            Product = product;
        }

        public String getVersion() {
            return Version;
        }

        public void setVersion(String version) {
            Version = version;
        }

        public DeviceInfo(String identifition, DeviceType deviceType, String remoteIP) {
            this.identifition = identifition;
            this.deviceType = deviceType;
            this.remoteIP = remoteIP;
        }

        public DeviceInfo(String identifition,DeviceType deviceType,String remoteIP,String product,String version)
        {
            this(identifition,deviceType,remoteIP);
            this.Product=product;
            this.Version=version;
        }
    }

    public List<DeviceInfo> QueryConnectedDevice() {
        List<DeviceInfo> deviceInfos = new ArrayList<>();
        for (Map.Entry<String, DeviceHandler> d : this.getConnetedDevices().entrySet()) {
            if (d.getValue() != null)
                deviceInfos.add(new DeviceInfo(d.getKey(), d.getValue().getDeviceType(), d.getValue().getRemoteIP(),d.getValue().getProducer(),d.getValue().getVersion()));
        }
        return deviceInfos;
    }


    /**
     * Device内部管理使用
     * <p>
     * 添加一个已连接的设备
     */
    public void AppendConnectedDevice(String identification, DeviceHandler handler) {
        if (this.connetedDevices.containsKey(identification)) {
            this.connetedDevices.get(identification).Close();
        }
        this.connetedDevices.put(identification, handler);
    }

    /**
     * Device内部管理使用
     * <p>
     * 关闭和移除一个已连接的设备
     */
    public void RemoveConnectedDevice(String identification) {
        if (this.connetedDevices.containsKey(identification)) {
            this.connetedDevices.get(identification).Close();
        }
        this.connetedDevices.remove(identification);
    }


    /***
     * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
     * 外部接口   服务管理
     *
     */

    /***
     *  启动UHF READER 服务
     *  @param port 监听端口
     * @return true 启动成功 false 启动失败
     * @exception IllegalArgumentException 参数取值不正确
     */
    public boolean StartUhfReaderService(UhfDeviceType uhfDeviceType, int port) {
        if (port < 0 || port > 65536)
            throw new IllegalArgumentException("参数Port取值不正确，应该在1-65536之间");
        this.uhf_Port = port;
        if (uhfService != null && uhfService.isAlive()) {
            return true;
        } else {
            switch (uhfDeviceType) {
                case UHF_READER_RODINBELL:
                    uhfService = new RodinBellService(this.uhf_Port);
                    break;
                case UHF_READER_COLU:
                    uhfService = new ColuReaderService(this.uhf_Port);
                    break;
            }
            if (uhfService == null) return false;
            return uhfService.StartService(this);
        }
    }

    /***
     *  停止UHF READER 服务
     *  @return true 停止成功 false 停止失败
     */
    public boolean StopUhfReaderService() {
        if (uhfService == null) return true;
        if (!uhfService.isAlive()) return true;
        return uhfService.StopService();
    }

    /***
     *  启动Eth002 模块，该服务进行锁、指纹、读卡器的控制
     *  @param port 监听端口
     * @return true 启动成功 false 启动失败
     * @exception IllegalArgumentException 参数取值不正确
     */
    public boolean StartEth002Service(Eth002ServiceType serviceType, int port) {
        if (port < 0 || port > 65536)
            throw new IllegalArgumentException("参数Port取值不正确，应该在1-65536之间");
        this.eth002_port = port;
        if (eth002Service != null && eth002Service.isAlive()) {
            return true;
        } else {
            eth002Service = new Eth002Service(serviceType, port);
            return eth002Service.StartService(this);
        }
    }

    /***
     *  停止UHF READER 服务
     *  @return true 停止成功 false 停止失败
     */
    public boolean StopEth002Service() {
        if (eth002Service == null) return true;
        if (!eth002Service.isAlive()) return true;
        return eth002Service.StopService();
    }


    /**
     * 启动RFID扫描
     *
     * @param deviceId 设备ID
     */
    public int StartUhfScan(String deviceId) {
        if (!this.getConnetedDevices().containsKey(deviceId)) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        if (!(this.getConnetedDevices().get(deviceId) instanceof UhfHandler)) {
            return FunctionCode.DEVICE_NOT_SUPPORT;
        }
        return ((UhfHandler) (this.getConnetedDevices().get(deviceId))).StartScan();
    }

    /**
     * 停止RFID扫描，目前针对高值耗材部分支持自动停止。该接口为保留接口
     *
     * @param deviceId
     * @return
     */
    public int StopUhfScan(String deviceId) {
        Thread thread=new Thread(()->{


        });
        if (!this.getConnetedDevices().containsKey(deviceId)) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        if (!(this.getConnetedDevices().get(deviceId) instanceof UhfHandler)) {
            return FunctionCode.DEVICE_NOT_SUPPORT;
        }
        return ((UhfHandler) (this.getConnetedDevices().get(deviceId))).StopScan();
    }

    /**
     * 设置UHF READER功率
     * Reader正在扫描时，可能会设置失败
     *
     * @param deviceId 设备ID
     * @param power    功率  1-30
     * @return 返回结果仅表示命令已发送，具体执行结果参照回调函数
     */
    public int setUhfReaderPower(String deviceId, byte power) {

        if (!this.getConnetedDevices().containsKey(deviceId)) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        if (!(this.getConnetedDevices().get(deviceId) instanceof UhfHandler)) {
            return FunctionCode.DEVICE_NOT_SUPPORT;
        }
        return ((UhfHandler) (this.getConnetedDevices().get(deviceId))).SetPower(power);
    }

    /***
     * 获取 UHF Reader 功率
     * @param deviceId 设备ID
     *
     * @return 返回结果仅表示命令已发送，具体执行结果参照回调函数
     */
    public int getUhfReaderPower(String deviceId) {
        if (!this.getConnetedDevices().containsKey(deviceId)) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        if (!(this.getConnetedDevices().get(deviceId) instanceof UhfHandler)) {
            return FunctionCode.DEVICE_NOT_SUPPORT;
        }
        return ((UhfHandler) (this.getConnetedDevices().get(deviceId))).QueryPower();
    }

    public int getUhfAnts(String deviceId) {
        if (!this.getConnetedDevices().containsKey(deviceId)) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        if (!(this.getConnetedDevices().get(deviceId) instanceof UhfHandler)) {
            return FunctionCode.DEVICE_NOT_SUPPORT;
        }
        List<Integer> ants = ((UhfHandler) (this.getConnetedDevices().get(deviceId))).getUhfAnts();
        new Thread(() -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {

            }
            if (this.deviceCallBack != null) {
                this.deviceCallBack.OnGetAnts(deviceId,true, ants);
            }
        }).start();
        return FunctionCode.SUCCESS;

    }

    /**
     * 复位设备，具体操作为 热重启，而不是恢复出厂设置
     *
     * @param deviceId 设备ID
     * @return 返回结果仅表示命令已发送，设备复位后，会断开连接并重新连接
     */
    public int ResetUhfReader(String deviceId) {
        if (!this.getConnetedDevices().containsKey(deviceId)) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        if (!(this.getConnetedDevices().get(deviceId) instanceof UhfHandler)) {
            return FunctionCode.DEVICE_NOT_SUPPORT;
        }
        return ((UhfHandler) (this.getConnetedDevices().get(deviceId))).Reset();
    }

    /***
     * 开锁
     * @param deviceId 电子锁连接的Eth模块的设备ID ，每个柜子对应一个Eth002模块
     *
     * @return 返回结果仅表示命令已发送，具体执行结果参照回调函数
     */
    public int OpenDoor(String deviceId) {
        if (!this.getConnetedDevices().containsKey(deviceId)) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        if (!(this.getConnetedDevices().get(deviceId) instanceof Eth002ClientHandler)) {
            return FunctionCode.DEVICE_NOT_SUPPORT;
        }
        return ((Eth002ClientHandler) (this.getConnetedDevices().get(deviceId))).OpenDoor();
    }

    /***
     * 检查开门状态开门
     * @param deviceId 电子锁连接的Eth模块的设备ID ，每个柜子对应一个Eth002模块
     * @return
     */
    public int CheckDoorState(String deviceId) {
        if (!this.getConnetedDevices().containsKey(deviceId)) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        if (!(this.getConnetedDevices().get(deviceId) instanceof Eth002ClientHandler)) {
            return FunctionCode.DEVICE_NOT_SUPPORT;
        }
        return ((Eth002ClientHandler) (this.getConnetedDevices().get(deviceId))).CheckLockState();
    }

    public int OpenLight(String deviceId) {
        if (!this.getConnetedDevices().containsKey(deviceId)) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        if (!(this.getConnetedDevices().get(deviceId) instanceof Eth002ClientHandler)) {
            return FunctionCode.DEVICE_NOT_SUPPORT;
        }
        return ((Eth002ClientHandler) (this.getConnetedDevices().get(deviceId))).OpenLight();
    }

    public int CloseLight(String deviceId) {
        if (!this.getConnetedDevices().containsKey(deviceId)) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        if (!(this.getConnetedDevices().get(deviceId) instanceof Eth002ClientHandler)) {
            return FunctionCode.DEVICE_NOT_SUPPORT;
        }
        return ((Eth002ClientHandler) (this.getConnetedDevices().get(deviceId))).CloseLight();
    }

    /***
     * 指纹注册
     * @param deviceId 指纹仪连接的Eth模块的设备ID ，每个柜子对应一个Eth002模块
     * @return
     */
    public int FingerReg(String deviceId) {
        if (!this.getConnetedDevices().containsKey(deviceId)) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        if (!(this.getConnetedDevices().get(deviceId) instanceof Eth002ClientHandler)) {
            return FunctionCode.DEVICE_NOT_SUPPORT;
        }
        return ((Eth002ClientHandler) (this.getConnetedDevices().get(deviceId))).FingerReg();
    }

    /**
     * GC
     */
    public void Release() {
        StopEth002Service();
        StopUhfReaderService();

        for (Map.Entry<String, DeviceHandler> handler : this.getConnetedDevices().entrySet()) {
            if (handler.getValue() != null) {
                try {
                    handler.getValue().Close();
                } catch (Exception ex) {
                }
            }
        }
    }

    public static String getIP(){

        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
                {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address))
                    {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        }
        catch (SocketException ex){
            ex.printStackTrace();
        }
        return null;
    }
}
