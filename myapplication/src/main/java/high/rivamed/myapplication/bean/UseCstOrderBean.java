package high.rivamed.myapplication.bean;

import java.io.Serializable;
import java.util.List;

import high.rivamed.myapplication.dto.vo.InventoryVo;

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
     * thingId
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
    private String thingId;
    public String getThingId() {
        return thingId;
    }

    public void setThingId(String thingId) {
        this.thingId = thingId;
    }

    private List<InventoryVo> inventoryVos;
    private CstTempPatient    cstTempPatient;

    public List<InventoryVo> getInventoryVos() {
        return inventoryVos;
    }

    public void setInventoryVos(List<InventoryVo> inventoryVos) {
        this.inventoryVos = inventoryVos;
    }

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
