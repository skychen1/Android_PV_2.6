package high.rivamed.myapplication.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @作者: 钱凯
 * @创建时间: 2018/8/29
 * @功能描述:查询在院患者bean
 */
public class FindInPatientBean {

    /**
     * pageNo : 1
     * pageSize : 10
     * rows : [{"patientId":"180810sl02256931","patientName":"陈桂英","deptName":"120急救","roomName":null,"doctorName":null,"operationBeginDateTime":null,"updateTime":null,"surgeryId":null},{"patientId":"180813lia0334681","patientName":"鲍亚靖","deptName":null,"roomName":"骨科手术室","doctorName":"张凯","operationBeginDateTime":null,"updateTime":null,"surgeryId":"1"},{"patientId":"180802zyh0827273","patientName":"李彦佳","deptName":"120急救","roomName":null,"doctorName":null,"operationBeginDateTime":null,"updateTime":null,"surgeryId":"40288299657a150301657a19df440003"},{"patientId":"180723msy0249714","patientName":"苗淑女","deptName":"120急救","roomName":null,"doctorName":null,"operationBeginDateTime":null,"updateTime":null,"surgeryId":"40288299657a150301657a1b8e960004"}]
     * total : 4
     */

    private int pageNo;
    private int pageSize;
    private int total;
    private List<RowsBean> rows = new ArrayList<>();

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<RowsBean> rows) {
        this.rows = rows;
    }

    public static class RowsBean {
        /**
         * patientId : 180810sl02256931
         * patientName : 陈桂英
         * deptName : 120急救
         * tempPatientId:
         * roomName : null
         * doctorName : null
         * operationBeginDateTime : null
         * updateTime : null
         * surgeryId : null
         */

        private String patientId              = "";
        private String medicalId              = "";
        private String patientName            = "";
        private String deptName               = "";
        private String roomName               = "";
        private String doctorName             = "";
        private String operationBeginDateTime = "";
        private String updateTime             = "";
        private String surgeryId              = "";
        private String surgeryTime            = "";
        private String tempPatientId          = "";
        private String hisPatientId          = "";
        private String deptType          = "";
        private String  operatingRoomNo;//手术间ID
        private boolean isSelected;
        private String bedNo;
        private String wardName;
        private String surgeryName;
        private String patientWard ;//病舍
        private String floor ;//楼层
        private String sex ;//性别
        private String age ;//年龄
        private String orderDeptName ;//新加的科室

        public String getOrderDeptName() {
            return orderDeptName;
        }

        public void setOrderDeptName(String orderDeptName) {
            this.orderDeptName = orderDeptName;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }
        public String getPatientWard() {
            return patientWard;
        }

        public void setPatientWard(String patientWard) {
            this.patientWard = patientWard;
        }

        public String getFloor() {
            return floor;
        }

        public void setFloor(String floor) {
            this.floor = floor;
        }
        public String getSurgeryName() {
            return surgeryName;
        }

        public void setSurgeryName(String surgeryName) {
            this.surgeryName = surgeryName;
        }

        public String getBedNo() {
            return bedNo;
        }

        public void setBedNo(String bedNo) {
            this.bedNo = bedNo;
        }

        public String getWardName() {
            return wardName;
        }

        public void setWardName(String wardName) {
            this.wardName = wardName;
        }

        public String getOperatingRoomNo() {
            return operatingRoomNo;
        }

        public void setOperatingRoomNo(String operatingRoomNo) {
            this.operatingRoomNo = operatingRoomNo;
        }

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

        public String getTempPatientId() {
            return tempPatientId;
        }

        public void setTempPatientId(String tempPatientId) {
            this.tempPatientId = tempPatientId;
        }

        public String getSurgeryTime() {
            return surgeryTime;
        }

        public void setSurgeryTime(String surgeryTime) {
            this.surgeryTime = surgeryTime;
        }

        public String getLpatsInId() {
            return lpatsInId;
        }

        public void setLpatsInId(String lpatsInId) {
            this.lpatsInId = lpatsInId;
        }

        private String lpatsInId = "";

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

        public String getDeptName() {
            return deptName;
        }

        public void setDeptName(String deptName) {
            this.deptName = deptName;
        }

        public String getRoomName() {
            return roomName;
        }

        public void setRoomName(String roomName) {
            this.roomName = roomName;
        }

        public String getDoctorName() {
            return doctorName;
        }

        public void setDoctorName(String doctorName) {
            this.doctorName = doctorName;
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

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }




    }
}
