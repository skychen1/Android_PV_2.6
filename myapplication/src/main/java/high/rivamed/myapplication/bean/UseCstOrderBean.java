package high.rivamed.myapplication.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: new2.6.3
 * @Package: high.rivamed.myapplication.bean
 * @ClassName: UseCstOrderBean
 * @Description: 套组领用-使用套组
 * @Author: Amos_Bo
 * @CreateDate: 2018/11/1 9:50
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/11/1 9:50
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class UseCstOrderBean implements Serializable {

    /**" isCreate": false,
     * thingCode
     * accountId : 8a80cb8164d9b3940164da1bff760005
     * patientId : 180723msy02497141
     * tCstInventoryVos : [{"count":0,"countActual":0,"countStock":0,"cstId":"106","cstName":"缝线结扎镊","cstSpec":"直平台","deletetatus":0,"deviceCode":"4028829965ea4d840165ea53f8e60001","deviceName":"柜子","epc":"000000002C10201809040035","expiration":"≤11天","expirationTime":"2018-09-30 00:00:00","isDelete":false,"operationStatus":"1","patientId":"180723msy02497141","patientName":"葛晓洋","size":0,"status":"领用","stopFlag":2,"storehouseCode":"ff80818165b1fb680165b35e320d0056"}]
     * "cstTempPatient":{
     * "patientName": "熊土元",
     * "scheduleDateTime": "2018-11-07 21:30:00",
     * "operatingRoomNoName": "导管手术室3",
     * "operationSurgeonName": "白日星",
     * "operationScheduleId": "170221zyh0765306"
     * }
     */


    private String accountId;
    private String patientId;
    private String thingCode;
    public String getThingCode() {
        return thingCode;
    }

    public void setThingCode(String thingCode) {
        this.thingCode = thingCode;
    }

    private List<TCstInventoryVosBean> tCstInventoryVos;
    private CstTempPatient cstTempPatient;

    public CstTempPatient getCstTempPatient() {
        return cstTempPatient;
    }

    public void setCstTempPatient(CstTempPatient cstTempPatient) {
        this.cstTempPatient = cstTempPatient;
    }



    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public List<TCstInventoryVosBean> getTCstInventoryVos() {
        return tCstInventoryVos;
    }

    public void setTCstInventoryVos(List<TCstInventoryVosBean> tCstInventoryVos) {
        this.tCstInventoryVos = tCstInventoryVos;
    }

    public static class TCstInventoryVosBean implements Serializable {
        /**
         * count : 0
         * countActual : 0
         * countStock : 0
         * cstId : 106
         * cstName : 缝线结扎镊
         * cstSpec : 直平台
         * deletetatus : 0
         * deviceCode : 4028829965ea4d840165ea53f8e60001
         * deviceName : 柜子
         * epc : 000000002C10201809040035
         * expiration : ≤11天
         * expirationTime : 2018-09-30 00:00:00
         * isDelete : false
         * operationStatus : 1
         * patientId : 180723msy02497141
         * patientName : 葛晓洋
         * size : 0
         * status : 领用
         * stopFlag : 2
         * storehouseCode : ff80818165b1fb680165b35e320d0056
         * operatingRoomNo
         * "sex":"女",
         * "idNo":"511304199563557845",
         "scheduleDateTime":"2018-11-06 13:59:09",
         "isCreate":"true"
         */

        private int count;
        private int countActual;
        private int countStock;
        private String cstId;
        private String cstName;
        private String cstSpec;
        private int deletetatus;
        private String deviceCode;
        private String deviceName;
        private String epc;
        private String expiration;
        private String expirationTime;
        private boolean isDelete;
        private String operationStatus;
        private String patientId;
        private String patientName;
        private int size;
        private String status;
        private int stopFlag;
        private String storehouseCode;

        private String operatingRoomNo;
        private String sex;
        private String idNo;
        private String scheduleDateTime;
        private String isCreate;
        private String operatingRoomNoName;
        private String   operationScheduleId;
        private String   tempPatientId;
        public void setTempPatientId(String tempPatientId) {
            this.tempPatientId = tempPatientId;
        }
        public String getTempPatientId() {
            return tempPatientId;
        }
        public String getOperatingRoomNoName() {
            return operatingRoomNoName;
        }

        public void setOperatingRoomNoName(String operatingRoomNoName) {
            this.operatingRoomNoName = operatingRoomNoName;
        }

        public String getOperationScheduleId() {
            return operationScheduleId;
        }

        public void setOperationScheduleId(String operationScheduleId) {
            this.operationScheduleId = operationScheduleId;
        }

        public String getOperatingRoomNo() {
            return operatingRoomNo;
        }

        public void setOperatingRoomNo(String operatingRoomNo) {
            this.operatingRoomNo = operatingRoomNo;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getIdNo() {
            return idNo;
        }

        public void setIdNo(String idNo) {
            this.idNo = idNo;
        }

        public String getScheduleDateTime() {
            return scheduleDateTime;
        }

        public void setScheduleDateTime(String scheduleDateTime) {
            this.scheduleDateTime = scheduleDateTime;
        }

        public String getIsCreate() {
            return isCreate;
        }

        public void setIsCreate(String isCreate) {
            this.isCreate = isCreate;
        }


        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getCountActual() {
            return countActual;
        }

        public void setCountActual(int countActual) {
            this.countActual = countActual;
        }

        public int getCountStock() {
            return countStock;
        }

        public void setCountStock(int countStock) {
            this.countStock = countStock;
        }

        public String getCstId() {
            return cstId;
        }

        public void setCstId(String cstId) {
            this.cstId = cstId;
        }

        public String getCstName() {
            return cstName;
        }

        public void setCstName(String cstName) {
            this.cstName = cstName;
        }

        public String getCstSpec() {
            return cstSpec;
        }

        public void setCstSpec(String cstSpec) {
            this.cstSpec = cstSpec;
        }

        public int getDeletetatus() {
            return deletetatus;
        }

        public void setDeletetatus(int deletetatus) {
            this.deletetatus = deletetatus;
        }

        public String getDeviceCode() {
            return deviceCode;
        }

        public void setDeviceCode(String deviceCode) {
            this.deviceCode = deviceCode;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public String getEpc() {
            return epc;
        }

        public void setEpc(String epc) {
            this.epc = epc;
        }

        public String getExpiration() {
            return expiration;
        }

        public void setExpiration(String expiration) {
            this.expiration = expiration;
        }

        public String getExpirationTime() {
            return expirationTime;
        }

        public void setExpirationTime(String expirationTime) {
            this.expirationTime = expirationTime;
        }

        public boolean isIsDelete() {
            return isDelete;
        }

        public void setIsDelete(boolean isDelete) {
            this.isDelete = isDelete;
        }

        public String getOperationStatus() {
            return operationStatus;
        }

        public void setOperationStatus(String operationStatus) {
            this.operationStatus = operationStatus;
        }

        public String getPatientId() {
            return patientId;
        }

        public void setPatientId(String patientId) {
            this.patientId = patientId;
        }

        public String getPatientName() {
            return patientName;
        }

        public void setPatientName(String patientName) {
            this.patientName = patientName;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getStopFlag() {
            return stopFlag;
        }

        public void setStopFlag(int stopFlag) {
            this.stopFlag = stopFlag;
        }

        public String getStorehouseCode() {
            return storehouseCode;
        }

        public void setStorehouseCode(String storehouseCode) {
            this.storehouseCode = storehouseCode;
        }
    }

    public static class CstTempPatient {

        /**
         * "patientName": "熊土元",
         * "scheduleDateTime": "2018-11-07 21:30:00",
         * "operatingRoomNoName": "导管手术室3",
         * "operationSurgeonName": "白日星",
         * "operationScheduleId": "170221zyh0765306"
         */
        private String tempPatientName;
        private String scheduleDateTime;
        private String operatingRoomNoName;
        private String operationSurgeonName;
        public String idCard;
        public String operatingRoomNo;
        public String sex;

        public String getIdCard() {
            return idCard;
        }

        public void setIdCard(String idNo) {
            this.idCard= idNo;
        }

        public String getOperatingRoomNo() {
            return operatingRoomNo;
        }

        public void setOperatingRoomNo(String operatingRoomNo) {
            this.operatingRoomNo = operatingRoomNo;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }


        public String getTempPatientName() {
            return tempPatientName;
        }

        public void setTempPatientName(String tempPatientName) {
            this.tempPatientName = tempPatientName;
        }

        public String getScheduleDateTime() {
            return scheduleDateTime;
        }

        public void setScheduleDateTime(String scheduleDateTime) {
            this.scheduleDateTime = scheduleDateTime;
        }

        public String getOperatingRoomNoName() {
            return operatingRoomNoName;
        }

        public void setOperatingRoomNoName(String operatingRoomNoName) {
            this.operatingRoomNoName = operatingRoomNoName;
        }

        public String getOperationSurgeonName() {
            return operationSurgeonName;
        }

        public void setOperationSurgeonName(String operationSurgeonName) {
            this.operationSurgeonName = operationSurgeonName;
        }

        public String getOperationScheduleId() {
            return operationScheduleId;
        }

        public void setOperationScheduleId(String operationScheduleId) {
            this.operationScheduleId = operationScheduleId;
        }

        private String operationScheduleId;
    }
}
