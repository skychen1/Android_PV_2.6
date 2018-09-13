package high.rivamed.myapplication.bean;

/**
 * @作者: 钱凯
 * @创建时间: 2018/8/29
 * @功能描述:
 */
public class PatientConnBean {
    /**
     * lPatsInId : 180810sl02256931
     * operationScheduleId : 40288299657a150301657a24dbdb0009
     * accountId : 8a80cb8164d9b3940164da1bff760005
     * thingCode : 8a80cb8164d9b3940164da1bff760005
     */

    private String lPatsInId;
    private String operationScheduleId;
    private String accountId;
    private String thingCode;

    public String getThingCode() {
        return thingCode;
    }

    public void setThingCode(String thingCode) {
        this.thingCode = thingCode;
    }

    public String getLPatsInId() {
        return lPatsInId;
    }

    public void setLPatsInId(String lPatsInId) {
        this.lPatsInId = lPatsInId;
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
