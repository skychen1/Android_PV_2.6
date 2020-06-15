package high.rivamed.myapplication.bean;

/**
 * @作者: 钱凯
 * @创建时间: 2018/8/26
 * @功能描述:	创建临时患者信息上传模型
 */
public class CreatTempPatientResultBean {

    /**
     * operateSuccess : true
     * id : 0
     * ttransOperationSchedule : {"ackIndicator":null,"anesStartTime":null,"anesthesiaDoctorCode":null,"anesthesiaDoctorName":null,"anesthesiaMethod":null,"arriveDateTime":null,"bedNum":null,"patientId":"virtual","commitIndicator":0,"createDateTime":null,"emergercyIndicator":0,"endDataTime":null,"greenchannelIndicator":1,"hisOperationState":null,"operatingDeptCode":"245","operatingDeptName":null,"operatingRoomNo":"1号","operatingRoomNoName":null,"operationBeginDateTime":null,"operationCatchdrugNo":0,"operationName":null,"operationState":0,"operationSurgeonCode":null,"operationSurgeonName":null,"patientName":"刘得到-男-133232324444","patientNamePinyin":"ldd","scheduleDateTime":"2018-08-26 00:00:00","returnDateTime":null,"roomOperationSequence":null,"scheduleDateTime":null,"scheduleId":null,"updateDateTime":"2018-08-26 13:33:32","visitId":null,"name":"刘得到","idCard":"133232324444","sex":"男","lpatsInId":null,"loperPatsId":null}
     */

    private boolean operateSuccess;
    private int id;
    private TtransOperationScheduleBean ttransOperationSchedule;

    public boolean isOperateSuccess() {
        return operateSuccess;
    }

    public void setOperateSuccess(boolean operateSuccess) {
        this.operateSuccess = operateSuccess;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TtransOperationScheduleBean getTtransOperationSchedule() {
        return ttransOperationSchedule;
    }

    public void setTtransOperationSchedule(TtransOperationScheduleBean ttransOperationSchedule) {
        this.ttransOperationSchedule = ttransOperationSchedule;
    }

    public static class TtransOperationScheduleBean {
        /**
         * ackIndicator : null
         * anesStartTime : null
         * anesthesiaDoctorCode : null
         * anesthesiaDoctorName : null
         * anesthesiaMethod : null
         * arriveDateTime : null
         * bedNum : null
         * patientId : virtual
         * commitIndicator : 0
         * createDateTime : null
         * emergercyIndicator : 0
         * endDataTime : null
         * greenchannelIndicator : 1
         * hisOperationState : null
         * operatingDeptCode : 245
         * operatingDeptName : null
         * operatingRoomNo : 1号
         * operatingRoomNoName : null
         * operationBeginDateTime : null
         * operationCatchdrugNo : 0
         * operationName : null
         * operationState : 0
         * operationSurgeonCode : null
         * operationSurgeonName : null
         * patientName : 刘得到-男-133232324444
         * patientNamePinyin : ldd
         * scheduleDateTime : 2018-08-26 00:00:00
         * returnDateTime : null
         * roomOperationSequence : null
         * scheduleDateTime : null
         * scheduleId : null
         * updateDateTime : 2018-08-26 13:33:32
         * visitId : null
         * name : 刘得到
         * idCard : 133232324444
         * sex : 男
         * lpatsInId : null
         * loperPatsId : null
         */

        private Object ackIndicator;
        private Object anesStartTime;
        private Object anesthesiaDoctorCode;
        private Object anesthesiaDoctorName;
        private Object anesthesiaMethod;
        private Object arriveDateTime;
        private Object bedNum;
        private String patientId;
        private int commitIndicator;
        private Object createDateTime;
        private int emergercyIndicator;
        private Object endDataTime;
        private int greenchannelIndicator;
        private Object hisOperationState;
        private String operatingDeptCode;
        private Object operatingDeptName;
        private String operatingRoomNo;
        private Object operatingRoomNoName;
        private Object operationBeginDateTime;
        private int operationCatchdrugNo;
        private Object operationName;
        private int    operationState;
        private Object operationSurgeonCode;
        private Object operationSurgeonName;
        private String patientName;
        private String patientNamePinyin;
        private Object returnDateTime;
        private Object roomOperationSequence;
        private String scheduleDateTime;
        private Object scheduleId;
        private String updateDateTime;
        private Object visitId;
        private String name;
        private String idCard;
        private String sex;
        private Object lpatsInId;
        private Object loperPatsId;

        public Object getAckIndicator() {
            return ackIndicator;
        }

        public void setAckIndicator(Object ackIndicator) {
            this.ackIndicator = ackIndicator;
        }

        public Object getAnesStartTime() {
            return anesStartTime;
        }

        public void setAnesStartTime(Object anesStartTime) {
            this.anesStartTime = anesStartTime;
        }

        public Object getAnesthesiaDoctorCode() {
            return anesthesiaDoctorCode;
        }

        public void setAnesthesiaDoctorCode(Object anesthesiaDoctorCode) {
            this.anesthesiaDoctorCode = anesthesiaDoctorCode;
        }

        public Object getAnesthesiaDoctorName() {
            return anesthesiaDoctorName;
        }

        public void setAnesthesiaDoctorName(Object anesthesiaDoctorName) {
            this.anesthesiaDoctorName = anesthesiaDoctorName;
        }

        public Object getAnesthesiaMethod() {
            return anesthesiaMethod;
        }

        public void setAnesthesiaMethod(Object anesthesiaMethod) {
            this.anesthesiaMethod = anesthesiaMethod;
        }

        public Object getArriveDateTime() {
            return arriveDateTime;
        }

        public void setArriveDateTime(Object arriveDateTime) {
            this.arriveDateTime = arriveDateTime;
        }

        public Object getBedNum() {
            return bedNum;
        }

        public void setBedNum(Object bedNum) {
            this.bedNum = bedNum;
        }

        public String getPatientId() {
            return patientId;
        }

        public void setPatientId(String patientId) {
            this.patientId = patientId;
        }

        public int getCommitIndicator() {
            return commitIndicator;
        }

        public void setCommitIndicator(int commitIndicator) {
            this.commitIndicator = commitIndicator;
        }

        public Object getCreateDateTime() {
            return createDateTime;
        }

        public void setCreateDateTime(Object createDateTime) {
            this.createDateTime = createDateTime;
        }

        public int getEmergercyIndicator() {
            return emergercyIndicator;
        }

        public void setEmergercyIndicator(int emergercyIndicator) {
            this.emergercyIndicator = emergercyIndicator;
        }

        public Object getEndDataTime() {
            return endDataTime;
        }

        public void setEndDataTime(Object endDataTime) {
            this.endDataTime = endDataTime;
        }

        public int getGreenchannelIndicator() {
            return greenchannelIndicator;
        }

        public void setGreenchannelIndicator(int greenchannelIndicator) {
            this.greenchannelIndicator = greenchannelIndicator;
        }

        public Object getHisOperationState() {
            return hisOperationState;
        }

        public void setHisOperationState(Object hisOperationState) {
            this.hisOperationState = hisOperationState;
        }

        public String getOperatingDeptCode() {
            return operatingDeptCode;
        }

        public void setOperatingDeptCode(String operatingDeptCode) {
            this.operatingDeptCode = operatingDeptCode;
        }

        public Object getOperatingDeptName() {
            return operatingDeptName;
        }

        public void setOperatingDeptName(Object operatingDeptName) {
            this.operatingDeptName = operatingDeptName;
        }

        public String getOperatingRoomNo() {
            return operatingRoomNo;
        }

        public void setOperatingRoomNo(String operatingRoomNo) {
            this.operatingRoomNo = operatingRoomNo;
        }

        public Object getOperatingRoomNoName() {
            return operatingRoomNoName;
        }

        public void setOperatingRoomNoName(Object operatingRoomNoName) {
            this.operatingRoomNoName = operatingRoomNoName;
        }

        public Object getOperationBeginDateTime() {
            return operationBeginDateTime;
        }

        public void setOperationBeginDateTime(Object operationBeginDateTime) {
            this.operationBeginDateTime = operationBeginDateTime;
        }

        public int getOperationCatchdrugNo() {
            return operationCatchdrugNo;
        }

        public void setOperationCatchdrugNo(int operationCatchdrugNo) {
            this.operationCatchdrugNo = operationCatchdrugNo;
        }

        public Object getOperationName() {
            return operationName;
        }

        public void setOperationName(Object operationName) {
            this.operationName = operationName;
        }

        public int getOperationState() {
            return operationState;
        }

        public void setOperationState(int operationState) {
            this.operationState = operationState;
        }

        public Object getOperationSurgeonCode() {
            return operationSurgeonCode;
        }

        public void setOperationSurgeonCode(Object operationSurgeonCode) {
            this.operationSurgeonCode = operationSurgeonCode;
        }

        public Object getOperationSurgeonName() {
            return operationSurgeonName;
        }

        public void setOperationSurgeonName(Object operationSurgeonName) {
            this.operationSurgeonName = operationSurgeonName;
        }

        public String getPatientName() {
            return patientName;
        }

        public void setPatientName(String patientName) {
            this.patientName = patientName;
        }

        public String getPatientNamePinyin() {
            return patientNamePinyin;
        }

        public void setPatientNamePinyin(String patientNamePinyin) {
            this.patientNamePinyin = patientNamePinyin;
        }

        public String getScheduleDateTime() {
            return scheduleDateTime;
        }

        public void setScheduleDateTime(String scheduleDateTime) {
            this.scheduleDateTime = scheduleDateTime;
        }

        public Object getReturnDateTime() {
            return returnDateTime;
        }

        public void setReturnDateTime(Object returnDateTime) {
            this.returnDateTime = returnDateTime;
        }

        public Object getRoomOperationSequence() {
            return roomOperationSequence;
        }

        public void setRoomOperationSequence(Object roomOperationSequence) {
            this.roomOperationSequence = roomOperationSequence;
        }



        public Object getScheduleId() {
            return scheduleId;
        }

        public void setScheduleId(Object scheduleId) {
            this.scheduleId = scheduleId;
        }

        public String getUpdateDateTime() {
            return updateDateTime;
        }

        public void setUpdateDateTime(String updateDateTime) {
            this.updateDateTime = updateDateTime;
        }

        public Object getVisitId() {
            return visitId;
        }

        public void setVisitId(Object visitId) {
            this.visitId = visitId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIdCard() {
            return idCard;
        }

        public void setIdCard(String idCard) {
            this.idCard = idCard;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public Object getLpatsInId() {
            return lpatsInId;
        }

        public void setLpatsInId(Object lpatsInId) {
            this.lpatsInId = lpatsInId;
        }

        public Object getLoperPatsId() {
            return loperPatsId;
        }

        public void setLoperPatsId(Object loperPatsId) {
            this.loperPatsId = loperPatsId;
        }
    }
}
