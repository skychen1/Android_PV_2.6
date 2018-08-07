package high.rivamed.myapplication.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @作者: 钱凯
 * @创建时间: 2018/8/2
 * @功能描述:账户信息
 */
public class AccountInfoBean implements Serializable {

    /**
     * id : 0
     * account : {"accountId":"8a80cb8164d9b3940164da1bff760002"}
     * appAccountInfoVo : {"accountName":"adminUM","userName":"asdasd","roleNames":["管理员1","管理员2"],"accountId":"8a80cb8164d9b3940164da1bff760002","userId":"8a80cb8164d9b3940164da1bff760003","isFinger":0}
     */

    private int id;
    private AccountBean account;
    private AppAccountInfoVoBean appAccountInfoVo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AccountBean getAccount() {
        return account;
    }

    public void setAccount(AccountBean account) {
        this.account = account;
    }

    public AppAccountInfoVoBean getAppAccountInfoVo() {
        return appAccountInfoVo;
    }

    public void setAppAccountInfoVo(AppAccountInfoVoBean appAccountInfoVo) {
        this.appAccountInfoVo = appAccountInfoVo;
    }

    public static class AccountBean {
        /**
         * accountId : 8a80cb8164d9b3940164da1bff760002
         */

        private String accountId;

        public String getAccountId() {
            return accountId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }
    }

    public static class AppAccountInfoVoBean {
        /**
         * accountName : adminUM
         * userName : asdasd
         * roleNames : ["管理员1","管理员2"]
         * accountId : 8a80cb8164d9b3940164da1bff760002
         * userId : 8a80cb8164d9b3940164da1bff760003
         * isFinger : 0
         */

        private String accountName;
        private String userName;
        private String accountId;
        private String userId;
        private int isFinger;
        private List<String> roleNames;

        public String getAccountName() {
            return accountName;
        }

        public void setAccountName(String accountName) {
            this.accountName = accountName;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getAccountId() {
            return accountId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getIsFinger() {
            return isFinger;
        }

        public void setIsFinger(int isFinger) {
            this.isFinger = isFinger;
        }

        public List<String> getRoleNames() {
            return roleNames;
        }

        public void setRoleNames(List<String> roleNames) {
            this.roleNames = roleNames;
        }
    }
}
