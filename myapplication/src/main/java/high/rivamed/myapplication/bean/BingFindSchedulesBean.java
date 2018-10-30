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
     * thingCode : 402882a064f9ab850164f9ad0a470000
     * patientInfos : [{"patientName":"李四","patientId":"3213123","scheduleDateTime":"","operationSurgeonName":"","operatingRoomNoName":""},{"patientName":"张三","scheduleDateTime":"2018-08-03 11:54:46","operationSurgeonName":"王医生","operatingRoomNoName":"312312","patientId":"3231232"}]
     */

    private int id;
    private String thingCode;
    private List<PatientInfosBean> patientInfos = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getThingCode() {
        return thingCode;
    }

    public void setThingCode(String thingCode) {
        this.thingCode = thingCode;
    }

    public List<PatientInfosBean> getPatientInfos() {
        return patientInfos;
    }

    public void setPatientInfos(
            List<PatientInfosBean> patientInfos) {
        this.patientInfos = patientInfos;
    }

    public static class PatientInfosBean {

        /**
         * patientName : 李四
         * patientId : 3213123
         * scheduleDateTime :
         * operationSurgeonName :
         * operatingRoomNoName :
         * tempPatientId
         */
        private boolean isSelected;
        private String patientName;//名字
        private String patientId;//id
        private String scheduleDateTime;//手术时间
        private String operationSurgeonName;//医生名字
        private String operatingRoomNoName;//手术间
        private String deptName;//科室
        private String operationBeginDateTime;
        private String updateTime;
        private String deptId;//科室ID
        private String operatingRoomNo;//手术间ID
        private String idNo;//身份证
        private String sex;//性别
        private String tempPatientId;
        private boolean isCreate;

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

        public String getScheduleDateTime() {
            return scheduleDateTime;
        }

        public void setScheduleDateTime(String scheduleDateTime) {
            this.scheduleDateTime = scheduleDateTime;
        }

        public String getLpatsInId() {
            return lpatsInId;
        }

        public void setLpatsInId(String lpatsInId) {
            this.lpatsInId = lpatsInId;
        }

        private String lpatsInId;

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

        public String getLoperPatsId() {
            return loperPatsId;
        }

        public void setLoperPatsId(String loperPatsId) {
            this.loperPatsId = loperPatsId;
        }

        private String loperPatsId;

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


        public String getOperationSurgeonName() {
            return operationSurgeonName;
        }

        public void setOperationSurgeonName(
                String operationSurgeonName) {
            this.operationSurgeonName = operationSurgeonName;
        }

        public String getOperatingRoomNoName() {
            return operatingRoomNoName;
        }

        public void setOperatingRoomNoName(
                String operatingRoomNoName) {
            this.operatingRoomNoName = operatingRoomNoName;
        }
    }
}
