package high.rivamed.myapplication.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @作者: 钱凯
 * @创建时间: 2018/8/2
 * @功能描述:登录返回结果
 */
public class LoginResultBean implements Serializable {
    /**
     * operateSuccess : true
     * id : 0
     * appAccountInfoVo : {"accountName":"adminUM","userName":"asdasd","roleNames":["管理员1","管理员2"],"accountId":"8a80cb8164d9b3940164da1bff760002","userId":"8a80cb8164d9b3940164da1bff760003","isFinger":1}
     */

    private boolean operateSuccess;
    private int id;
    private AppAccountInfoVoBean appAccountInfoVo;

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

    public AppAccountInfoVoBean getAppAccountInfoVo() {
        return appAccountInfoVo;
    }

    public void setAppAccountInfoVo(AppAccountInfoVoBean appAccountInfoVo) {
        this.appAccountInfoVo = appAccountInfoVo;
    }

    public static class AppAccountInfoVoBean {
        /**
         * accountName : adminUM
         * userName : asdasd
         * roleNames : ["管理员1","管理员2"]
         * accountId : 8a80cb8164d9b3940164da1bff760002
         * userId : 8a80cb8164d9b3940164da1bff760003
         * isFinger : 1
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
