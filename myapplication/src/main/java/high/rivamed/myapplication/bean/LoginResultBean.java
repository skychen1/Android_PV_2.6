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
     * msg:"
     * appAccountInfoVo : {"accountName":"adminUM","userName":"asdasd","roleNames":["管理员1","管理员2"],"accountId":"8a80cb8164d9b3940164da1bff760002","userId":"8a80cb8164d9b3940164da1bff760003","isFinger":1}
     */

    private boolean operateSuccess;
    private int id;
    private String msg;

    private AppAccountInfoVoBean appAccountInfoVo;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

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
         * sex:1
         * headIcon
         */

        private String accountName;
        private String userName;
        private String accountId;
        private String userId;
        private int isFinger;
        private int isWaidai;
        private int isEmergency;
        private String sex;
        private String headIcon;

        public int getIsEmergency() {
            return isEmergency;
        }

        public void setIsEmergency(int isEmergency) {
            this.isEmergency = isEmergency;
        }

        public String getHeadIcon() {
            return headIcon;
        }

        public void setHeadIcon(String headIcon) {
            this.headIcon = headIcon;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public int getIsWaidai() {
            return isWaidai;
        }

        public void setIsWaidai(int isWaidai) {
            this.isWaidai = isWaidai;
        }

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
