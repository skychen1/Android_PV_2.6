package high.rivamed.myapplication.bean;

/**
 * @作者: 钱凯
 * @创建时间: 2018/8/3
 * @功能描述:
 */
public class ResetPassBean {


    /**
     * operateSuccess : true
     * id : 0
     * msg : 密码重置成功
     * opFlg : 1
     * account : {"accountId":"8a80cb8164d9b3940164da1bff760002","password":"19df4831beff5a74cf0c6f73e69e955cf786ef73","salt":"C2AJkLAMMh9nRXxB","updateTime":"2018-08-03 08:41:18"}
     */

    private boolean operateSuccess;
    private int id;
    private String msg;
    private String opFlg;
    private AccountBean account;

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

    public String getOpFlg() {
        return opFlg;
    }

    public void setOpFlg(String opFlg) {
        this.opFlg = opFlg;
    }

    public AccountBean getAccount() {
        return account;
    }

    public void setAccount(AccountBean account) {
        this.account = account;
    }

    public static class AccountBean {
        /**
         * accountId : 8a80cb8164d9b3940164da1bff760002
         * password : 19df4831beff5a74cf0c6f73e69e955cf786ef73
         * salt : C2AJkLAMMh9nRXxB
         * updateTime : 2018-08-03 08:41:18
         */

        private String accountId;
        private String password;
        private String salt;
        private String updateTime;

        public String getAccountId() {
            return accountId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getSalt() {
            return salt;
        }

        public void setSalt(String salt) {
            this.salt = salt;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }
    }
}
