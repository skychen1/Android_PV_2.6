package high.rivamed.myapplication.bean;

/**
 * @作者: 钱凯
 * @创建时间: 2018/8/29
 * @功能描述:
 */
public class PatientConnBean {
    /**
     * patientId : 180810sl02256931
     * operationScheduleId : 40288299657a150301657a24dbdb0009
     * accountId : 8a80cb8164d9b3940164da1bff760005
     * thingId : 8a80cb8164d9b3940164da1bff760005
     */

    private String patientId;
    private String operationScheduleId;
    private String accountId;
    private String thingId;
    private String tempPatientId;

    public String getTempPatientId() {
        return tempPatientId;
    }

    public void setTempPatientId(String tempPatientId) {
        this.tempPatientId = tempPatientId;
    }

    public String getThingId() {
        return thingId;
    }

    public void setThingId(String thingId) {
        this.thingId = thingId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String lPatsInId) {
        this.patientId = lPatsInId;
    }

    public String getOperationScheduleId() {
        return operationScheduleId;
    }

    public void setOperationScheduleId(String operationScheduleId) {
        this.operationScheduleId = operationScheduleId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
