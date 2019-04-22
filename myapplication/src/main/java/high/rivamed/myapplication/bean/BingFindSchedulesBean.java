package high.rivamed.myapplication.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/8/3 12:26
 * 描述:        绑定患者  查询的患者
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class BingFindSchedulesBean implements Serializable {

    /**
     * id : 0
     * thingId : 402882a064f9ab850164f9ad0a470000
     * patientInfos : [{"patientName":"李四","patientId":"3213123","surgeryTime":"","doctorName":"","roomName":""},{"patientName":"张三","surgeryTime":"2018-08-03 11:54:46","doctorName":"王医生","roomName":"312312","patientId":"3231232"}]
     */

    private int    id;
    private String thingId;
    private List<PatientInfoVos> patientInfoVos = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getThingId() {
        return thingId;
    }

    public void setThingId(String thingId) {
        this.thingId = thingId;
    }

    public List<PatientInfoVos> getPatientInfoVos() {
        return patientInfoVos;
    }

    public void setPatientInfoVos(
            List<PatientInfoVos> patientInfoVos) {
        this.patientInfoVos = patientInfoVos;
    }

    public static class PatientInfoVos {

        /**
         * patientName : 李四
         * patientId : 3213123
         * surgeryTime :
         * doctorName :
         * roomName :
         * tempPatientId
         */
        private boolean isSelected;
        private String  patientName;//名字
        private String  patientId;//id
        private String  surgeryTime;//手术时间
        private String  doctorName;//医生名字
        private String  roomName;//手术间
        private String  deptName;//科室
        private String  operationBeginDateTime;
        private String  updateTime;
        private String  deptId;//科室ID
        private String  operatingRoomNo;//手术间ID
        private String  idNo;//身份证
        private String  sex;//性别
        private String  tempPatientId;
        private boolean isCreate;
        private String medicalId;
        private String hisPatientId        ;
        private String deptType;

        public String getDeptType() {
            return deptType;
        }

        public void setDeptType(String deptType) {
            this.deptType = deptType;
        }
        public String getHisPatientId() {
            return hisPatientId;
        }

        public void setHisPatientId(String hisPatientId) {
            this.hisPatientId = hisPatientId;
        }

        public String getMedicalId() {
            return medicalId;
        }

        public void setMedicalId(String medicalId) {
            this.medicalId = medicalId;
        }

        public boolean isCreate() {
            return isCreate;
        }

        public void setCreate(boolean create) {
            isCreate = create;
        }

        public String getTempPatientId() {
            return tempPatientId;
        }

        public void setTempPatientId(String tempPatientId) {
            this.tempPatientId = tempPatientId;
        }

        public String getDeptId() {
            return deptId;
        }

        public void setDeptId(String deptId) {
            this.deptId = deptId;
        }

        public String getOperatingRoomNo() {
            return operatingRoomNo;
        }

        public void setOperatingRoomNo(String operatingRoomNo) {
            this.operatingRoomNo = operatingRoomNo;
        }

        public String getIdNo() {
            return idNo;
        }

        public void setIdNo(String idNo) {
            this.idNo = idNo;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getSurgeryTime() {
            return surgeryTime;
        }

        public void setSurgeryTime(String surgeryTime) {
            this.surgeryTime = surgeryTime;
        }



        public String getDeptName() {
            return deptName;
        }

        public void setDeptName(String deptName) {
            this.deptName = deptName;
        }

        public String getOperationBeginDateTime() {
            return operationBeginDateTime;
        }

        public void setOperationBeginDateTime(String operationBeginDateTime) {
            this.operationBeginDateTime = operationBeginDateTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getSurgeryId() {
            return surgeryId;
        }

        public void setSurgeryId(String surgeryId) {
            this.surgeryId = surgeryId;
        }

        private String surgeryId;

        public String getOperationScheduleId() {
            return operationScheduleId;
        }

        public void setOperationScheduleId(String operationScheduleId) {
            this.operationScheduleId = operationScheduleId;
        }

        private String operationScheduleId;

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public String getPatientName() {
            return patientName;
        }

        public void setPatientName(String patientName) {
            this.patientName = patientName;
        }

        public String getPatientId() {
            return patientId;
        }

        public void setPatientId(String patientId) {
            this.patientId = patientId;
        }


        public String getDoctorName() {
            return doctorName;
        }

        public void setDoctorName(
                String doctorName) {
            this.doctorName = doctorName;
        }

        public String getRoomName() {
            return roomName;
        }

        public void setRoomName(
                String roomName) {
            this.roomName = roomName;
        }
    }
}
