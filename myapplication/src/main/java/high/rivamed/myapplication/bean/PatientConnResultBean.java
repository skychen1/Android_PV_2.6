package high.rivamed.myapplication.bean;

/**
 * @作者: 钱凯
 * @创建时间: 2018/8/29
 * @功能描述:
 */
public class PatientConnResultBean {
    /**
     * operateSuccess : true
     * id : 0
     * msg : 关联成功
     * operation : 0
     * stopFlag : 0
     * countTwoin : 0
     * countMoveIn : 0
     * countBack : 0
     * countTempopary : 0
     * accountId : 8a80cb8164d9b3940164da1bff760005
     * operationScheduleId : 40288299657e506701658027b3550075
     * lPatsInId : 180813lia0334681
     */

    private boolean operateSuccess;
    private int id;
    private String msg;
    private int operation;
    private int stopFlag;
    private int countTwoin;
    private int countMoveIn;
    private int countBack;
    private int countTempopary;
    private String accountId;
    private String operationScheduleId;
    private String lPatsInId;

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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public int getStopFlag() {
        return stopFlag;
    }

    public void setStopFlag(int stopFlag) {
        this.stopFlag = stopFlag;
    }

    public int getCountTwoin() {
        return countTwoin;
    }

    public void setCountTwoin(int countTwoin) {
        this.countTwoin = countTwoin;
    }

    public int getCountMoveIn() {
        return countMoveIn;
    }

    public void setCountMoveIn(int countMoveIn) {
        this.countMoveIn = countMoveIn;
    }

    public int getCountBack() {
        return countBack;
    }

    public void setCountBack(int countBack) {
        this.countBack = countBack;
    }

    public int getCountTempopary() {
        return countTempopary;
    }

    public void setCountTempopary(int countTempopary) {
        this.countTempopary = countTempopary;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getOperationScheduleId() {
        return operationScheduleId;
    }

    public void setOperationScheduleId(String operationScheduleId) {
        this.operationScheduleId = operationScheduleId;
    }

    public String getLPatsInId() {
        return lPatsInId;
    }

    public void setLPatsInId(String lPatsInId) {
        this.lPatsInId = lPatsInId;
    }
}
