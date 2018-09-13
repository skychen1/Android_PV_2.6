package high.rivamed.myapplication.bean;

/**
 * @作者: 钱凯
 * @创建时间: 2018/8/26
 * @功能描述:	创建临时患者信息上传模型
 */
public class CreatTempPatientBean {


    /**
     * tTransOperationSchedule : {"idNo":"133232324444","name":"刘得到","operatingDeptCode":"245","operatingRoomNo":"1号","scheduleDateTime":"2018-08-26","sex":"男"}
     */

    private TTransOperationScheduleBean tTransOperationSchedule;

    public TTransOperationScheduleBean getTTransOperationSchedule() {
        return tTransOperationSchedule;
    }

    public void setTTransOperationSchedule(TTransOperationScheduleBean tTransOperationSchedule) {
        this.tTransOperationSchedule = tTransOperationSchedule;
    }

    public static class TTransOperationScheduleBean {
        /**
         * idNo : 133232324444
         * name : 刘得到
         * operatingDeptCode : 245
         * operatingRoomNo : 1号
         * scheduleDateTime : 2018-08-26
         * sex : 男
         */

        private String idNo;
        private String name;

        public String getDeptId() {
            return deptId;
        }

        public void setDeptId(String deptId) {
            this.deptId = deptId;
        }

        private String deptId;
        private String operatingRoomNo;
        private String scheduleDateTime;
        private String sex;

        public String getIdNo() {
            return idNo;
        }

        public void setIdNo(String idNo) {
            this.idNo = idNo;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


        public String getOperatingRoomNo() {
            return operatingRoomNo;
        }

        public void setOperatingRoomNo(String operatingRoomNo) {
            this.operatingRoomNo = operatingRoomNo;
        }

        public String getScheduleDateTime() {
            return scheduleDateTime;
        }

        public void setScheduleDateTime(String scheduleDateTime) {
            this.scheduleDateTime = scheduleDateTime;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }
    }
}
