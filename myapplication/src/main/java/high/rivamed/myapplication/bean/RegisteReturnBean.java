package high.rivamed.myapplication.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/27 11:08
 * 描述:        预注册返回的
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class RegisteReturnBean implements Serializable{

    /**
     * operateSuccess : true
     * id : 0
     * tBaseThingSnVo : {"thingCode":"4028829965f9a3670165f9b046090000","appScene":null,"deptId":null,"deptName":null,"localIp":"192.168.100.10","macAddress":null,"operationRoomNo":null,"operationRoomNoName":null,"remark":null,"sn":"000000","stopFlag":1,"storehouseCode":null,"storehouseName":null,"thingName":"Xxxxx","thingType":"Dxcccc","createDate":null,"status":0,"branchCode":null}
     * tBaseDeviceVos : [{"deviceName":"00000","deviceCode":"4028829965f9a3670165f9b0461e0001","deviceType":null,"ip":null,"parent":null,"tBaseDevice":null,"tBaseDevices":[{"baudrate":null,"com":null,"deviceCode":"4028829965f9a3670165f9b046230002","deviceName":"网络多功能集控设备","deviceType":"1","identification":"0A0A0000FCAEFFFC","ip":"192.168.100.134","parent":"4028829965f9a3670165f9b0461e0001","remark":null,"stopFlag":0,"thingCode":"4028829965f9a3670165f9b046090000","createDate":"2018-09-21 09:41:49","dictId":"8a80cb8164db4a080164db5ff6650000"},{"baudrate":null,"com":null,"deviceCode":"4028829965f9a3670165f9b046240003","deviceName":"reader","deviceType":"2","identification":"6CECA1FE9977","ip":"192.168.100.103","parent":"4028829965f9a3670165f9b0461e0001","remark":null,"stopFlag":0,"thingCode":"4028829965f9a3670165f9b046090000","createDate":"2018-09-21 09:41:49","dictId":"4028728164f3373c0164f34402a80015"},{"baudrate":null,"com":null,"deviceCode":"4028829965f9a3670165f9b046250004","deviceName":"reader","deviceType":"2","identification":"6CECA1FE9F91","ip":"192.168.100.13","parent":"4028829965f9a3670165f9b0461e0001","remark":null,"stopFlag":0,"thingCode":"4028829965f9a3670165f9b046090000","createDate":"2018-09-21 09:41:49","dictId":"4028728164f3373c0164f34402a80015"}]}]
     * hospitalInfoVo : {"storehouseCode":"","deptId":"1331","operationRoomNo":"10026","branchCode":"2","deptName":"导管手术部(新)"}
     * tbaseThing : {"thingCode":"4028829965f9a3670165f9b046090000","appScene":null,"deptId":"1331","deptName":"导管手术部(新)","localIp":"192.168.100.10","macAddress":null,"operationRoomNo":"10026","remark":null,"sn":"000000","stopFlag":1,"storehouseCode":null,"thingName":"Xxxxx","thingType":"Dxcccc","branchCode":null,"createDate":null,"status":1}
     */
    private String                   msg;
    private boolean operateSuccess;
    private int                      id;
    private TBaseThingSnVoBean       tBaseThingSnVo;
    private HospitalInfoVoBean       hospitalInfoVo;
    private TbaseThingBean           tbaseThing;
    private List<TBaseDeviceVosBean> tBaseDeviceVos;
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public boolean isOperateSuccess() { return operateSuccess;}

    public void setOperateSuccess(boolean operateSuccess) { this.operateSuccess = operateSuccess;}

    public int getId() { return id;}

    public void setId(int id) { this.id = id;}

    public TBaseThingSnVoBean getTBaseThingSnVo() { return tBaseThingSnVo;}

    public void setTBaseThingSnVo(
          TBaseThingSnVoBean tBaseThingSnVo) { this.tBaseThingSnVo = tBaseThingSnVo;}

    public HospitalInfoVoBean getHospitalInfoVo() { return hospitalInfoVo;}

    public void setHospitalInfoVo(
          HospitalInfoVoBean hospitalInfoVo) { this.hospitalInfoVo = hospitalInfoVo;}

    public TbaseThingBean getTbaseThing() { return tbaseThing;}

    public void setTbaseThing(TbaseThingBean tbaseThing) { this.tbaseThing = tbaseThing;}

    public List<TBaseDeviceVosBean> getTBaseDeviceVos() { return tBaseDeviceVos;}

    public void setTBaseDeviceVos(
          List<TBaseDeviceVosBean> tBaseDeviceVos) { this.tBaseDeviceVos = tBaseDeviceVos;}

    public static class TBaseThingSnVoBean {

        /**
         * thingCode : 4028829965f9a3670165f9b046090000
         * appScene : null
         * deptId : null
         * deptName : null
         * localIp : 192.168.100.10
         * macAddress : null
         * operationRoomNo : null
         * operationRoomNoName : null
         * remark : null
         * sn : 000000
         * stopFlag : 1
         * storehouseCode : null
         * storehouseName : null
         * thingName : Xxxxx
         * thingType : Dxcccc
         * createDate : null
         * status : 0
         * branchCode : null
         */

        private String thingCode;
        private String appScene;
        private String deptId;
        private String deptName;
        private String localIp;
        private String macAddress;
        private String operationRoomNo;
        private String operationRoomNoName;
        private String remark;
        private String sn;
        private int    stopFlag;
        private String storehouseCode;
        private String storehouseName;
        private String thingName;
        private String thingType;
        private String createDate;
        private int    status;
        private String branchCode;

        public String getThingCode() { return thingCode;}

        public void setThingCode(String thingCode) { this.thingCode = thingCode;}

        public String getAppScene() { return appScene;}

        public void setAppScene(String appScene) { this.appScene = appScene;}

        public String getDeptId() { return deptId;}

        public void setDeptId(String deptId) { this.deptId = deptId;}

        public String getDeptName() { return deptName;}

        public void setDeptName(String deptName) { this.deptName = deptName;}

        public String getLocalIp() { return localIp;}

        public void setLocalIp(String localIp) { this.localIp = localIp;}

        public String getMacAddress() { return macAddress;}

        public void setMacAddress(String macAddress) { this.macAddress = macAddress;}

        public String getOperationRoomNo() { return operationRoomNo;}

        public void setOperationRoomNo(
              String operationRoomNo) { this.operationRoomNo = operationRoomNo;}

        public String getOperationRoomNoName() { return operationRoomNoName;}

        public void setOperationRoomNoName(
              String operationRoomNoName) { this.operationRoomNoName = operationRoomNoName;}

        public String getRemark() { return remark;}

        public void setRemark(String remark) { this.remark = remark;}

        public String getSn() { return sn;}

        public void setSn(String sn) { this.sn = sn;}

        public int getStopFlag() { return stopFlag;}

        public void setStopFlag(int stopFlag) { this.stopFlag = stopFlag;}

        public String getStorehouseCode() { return storehouseCode;}

        public void setStorehouseCode(
              String storehouseCode) { this.storehouseCode = storehouseCode;}

        public String getStorehouseName() { return storehouseName;}

        public void setStorehouseName(
              String storehouseName) { this.storehouseName = storehouseName;}

        public String getThingName() { return thingName;}

        public void setThingName(String thingName) { this.thingName = thingName;}

        public String getThingType() { return thingType;}

        public void setThingType(String thingType) { this.thingType = thingType;}

        public String getCreateDate() { return createDate;}

        public void setCreateDate(String createDate) { this.createDate = createDate;}

        public int getStatus() { return status;}

        public void setStatus(int status) { this.status = status;}

        public String getBranchCode() { return branchCode;}

        public void setBranchCode(String branchCode) { this.branchCode = branchCode;}
    }

    public static class HospitalInfoVoBean {

        /**
         * storehouseCode :
         * deptId : 1331
         * operationRoomNo : 10026
         * branchCode : 2
         * deptName : 导管手术部(新)
         */

        private String storehouseCode;
        private String deptId;
        private String operationRoomNo;
        private String branchCode;
        private String deptName;

        public String getStorehouseCode() { return storehouseCode;}

        public void setStorehouseCode(
              String storehouseCode) { this.storehouseCode = storehouseCode;}

        public String getDeptId() { return deptId;}

        public void setDeptId(String deptId) { this.deptId = deptId;}

        public String getOperationRoomNo() { return operationRoomNo;}

        public void setOperationRoomNo(
              String operationRoomNo) { this.operationRoomNo = operationRoomNo;}

        public String getBranchCode() { return branchCode;}

        public void setBranchCode(String branchCode) { this.branchCode = branchCode;}

        public String getDeptName() { return deptName;}

        public void setDeptName(String deptName) { this.deptName = deptName;}
    }

    public static class TbaseThingBean {

        /**
         * thingCode : 402882a064d9b19f0164d9b1f78a0000
         * appScene : 0
         * deptId : null
         * localIp : 192.168.2.5
         * serverIp : 192.168.2.32
         * macAddress : null
         * operationRoomNo : null
         * remark : null
         * sn : 123456789
         * stopFlag : 0
         * storehouseCode : null
         * thingName : 2.6柜子
         * thingType : rivamed26
         * status : 0
         * portNumber : 8015
         */

        private String thingCode;
        private int    appScene;
        private String deptId;
        private String deptName;
        private String localIp;
        private String serverIp;
        private String macAddress;
        private String operationRoomNo;
        private String remark;
        private String sn;
        private int    stopFlag;
        private String storehouseCode;
        private String thingName;
        private String thingType;
        private int    status;
        private String portNumber;
        private String branchCode;
        public String getBranchCode() {
            return branchCode;
        }

        public void setBranchCode(String branchCode) {
            this.branchCode = branchCode;
        }


        public String getDeptName() {
            return deptName;
        }

        public void setDeptName(String deptName) {
            this.deptName = deptName;
        }

        public String getThingCode() { return thingCode;}

        public void setThingCode(String thingCode) { this.thingCode = thingCode;}

        public int getAppScene() { return appScene;}

        public void setAppScene(int appScene) { this.appScene = appScene;}

        public String getDeptCode() { return deptId;}

        public void setDeptCode(String deptId) { this.deptId = deptId;}

        public String getLocalIp() { return localIp;}

        public void setLocalIp(String localIp) { this.localIp = localIp;}

        public String getServerIp() { return serverIp;}

        public void setServerIp(String serverIp) { this.serverIp = serverIp;}

        public String getMacAddress() { return macAddress;}

        public void setMacAddress(String macAddress) { this.macAddress = macAddress;}

        public String getOperationRoomNo() { return operationRoomNo;}

        public void setOperationRoomNo(
              String operationRoomNo) { this.operationRoomNo = operationRoomNo;}

        public String getRemark() { return remark;}

        public void setRemark(String remark) { this.remark = remark;}

        public String getSn() { return sn;}

        public void setSn(String sn) { this.sn = sn;}

        public int getStopFlag() { return stopFlag;}

        public void setStopFlag(int stopFlag) { this.stopFlag = stopFlag;}

        public String getStorehouseCode() { return storehouseCode;}

        public void setStorehouseCode(String storehouseCode) { this.storehouseCode = storehouseCode;}

        public String getThingName() { return thingName;}

        public void setThingName(String thingName) { this.thingName = thingName;}

        public String getThingType() { return thingType;}

        public void setThingType(String thingType) { this.thingType = thingType;}

        public int getStatus() { return status;}

        public void setStatus(int status) { this.status = status;}

        public String getPortNumber() { return portNumber;}

        public void setPortNumber(String portNumber) { this.portNumber = portNumber;}
    }

    public static class TBaseDeviceVosBean {

        /**
         * deviceName : 1号柜
         * deviceCode : 402882a064d9b19f0164d9b1f7cc0001
         * deviceType : 0
         * ip : null
         * parent : null
         * tBaseDevice : null
         * taBaseDevices : [{"baudrate":null,"com":null,"deviceCode":"0A0A000000000AEF","deviceName":"串口服务器","deviceType":0,"identification":0,"ip":"192.168.2.9","parent":"402882a064d9b19f0164d9b1f7cc0001","remark":null,"stopFlag":0,"thingCode":"402882a064d9b19f0164d9b1f78a0000"},{"baudrate":null,"com":null,"deviceCode":"6C-EC-A1-FE-99-2E","deviceName":"Reader-红陆","deviceType":0,"identification":0,"ip":"192.168.2.22","parent":"402882a064d9b19f0164d9b1f7cc0001","remark":null,"stopFlag":0,"thingCode":"402882a064d9b19f0164d9b1f78a0000"}]
         */

        private String                 deviceName;
        private String                 deviceCode;
        private int                    deviceType;
        private Object                 ip;
        private Object                 parent;
        private Object                 tBaseDevice;
        private List<TBaseDevicesBean> tBaseDevices;

        public String getDeviceName() { return deviceName;}

        public void setDeviceName(String deviceName) { this.deviceName = deviceName;}

        public String getDeviceCode() { return deviceCode;}

        public void setDeviceCode(String deviceCode) { this.deviceCode = deviceCode;}

        public int getDeviceType() { return deviceType;}

        public void setDeviceType(int deviceType) { this.deviceType = deviceType;}

        public Object getIp() { return ip;}

        public void setIp(Object ip) { this.ip = ip;}

        public Object getParent() { return parent;}

        public void setParent(Object parent) { this.parent = parent;}

        public Object getTBaseDevice() { return tBaseDevice;}

        public void setTBaseDevice(Object tBaseDevice) { this.tBaseDevice = tBaseDevice;}

        public List<TBaseDevicesBean> getTBaseDevices() { return tBaseDevices;}

        public void setTaBaseDevices(
              List<TBaseDevicesBean> tBaseDevices) { this.tBaseDevices = tBaseDevices;}

        public static class TBaseDevicesBean {

            /**
             * baudrate : null
             * com : null
             * deviceCode : 0A0A000000000AEF
             * deviceName : 串口服务器
             * deviceType : 0
             * identification : 0
             * ip : 192.168.2.9
             * parent : 402882a064d9b19f0164d9b1f7cc0001
             * remark : null
             * stopFlag : 0
             * "dictId": "16",
             "name": "串口服务器",
             "deviceType": "Eth002"
             * thingCode : 402882a064d9b19f0164d9b1f78a0000
             */

            private Object baudrate;
            private Object com;
            private String dictId;
            private String deviceType;
            private String deviceCode;
            private String deviceName;
            private String    identification;
            private String ip;
            private String parent;
            private Object remark;
            private int    stopFlag;
            private String thingCode;

            public String getDictId() {
                return dictId;
            }

            public void setDictId(String dictId) {
                this.dictId = dictId;
            }

            public Object getBaudrate() { return baudrate;}

            public void setBaudrate(Object baudrate) { this.baudrate = baudrate;}

            public Object getCom() { return com;}

            public void setCom(Object com) { this.com = com;}

            public String getDeviceCode() { return deviceCode;}

            public void setDeviceCode(String deviceCode) { this.deviceCode = deviceCode;}

            public String getDeviceName() { return deviceName;}

            public void setDeviceName(String deviceName) { this.deviceName = deviceName;}

            public String getDeviceType() { return deviceType;}

            public void setDeviceType(String deviceType) { this.deviceType = deviceType;}

            public String getIdentification() { return identification;}

            public void setIdentification(String identification) { this.identification = identification;}

            public String getIp() { return ip;}

            public void setIp(String ip) { this.ip = ip;}

            public String getParent() { return parent;}

            public void setParent(String parent) { this.parent = parent;}

            public Object getRemark() { return remark;}

            public void setRemark(Object remark) { this.remark = remark;}

            public int getStopFlag() { return stopFlag;}

            public void setStopFlag(int stopFlag) { this.stopFlag = stopFlag;}

            public String getThingCode() { return thingCode;}

            public void setThingCode(String thingCode) { this.thingCode = thingCode;}
        }
    }
}
