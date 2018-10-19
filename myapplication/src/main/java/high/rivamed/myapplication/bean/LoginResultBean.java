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
     * userFeatureInfo : {"type":"2","data":"DA40BDC4"}
     * thingCode : 402882a9668b101701668b3427860015
     * appAccountInfoVo : {"accountName":"adminUM","tenantId":"1","userName":"高级管理员","roles":[{"roleCode":"0","roleName":"系统管理员","funcs":[]},{"roleCode":"0","roleName":"医院医生","funcs":[]},{"roleCode":"0","roleName":"医院护士","funcs":[]}],"accountId":"8a80cb8164d9b3940164da1bff760005","userId":"8a80cb8164d9b3940164da1bff760003","isFinger":1,"isWaidai":1,"isEmergency":0,"sex":"女","useState":"1","emergencyPwd":null}
     */

    private boolean operateSuccess;
    private int                  id;
    private UserFeatureInfoBean  userFeatureInfo;
    private String               thingCode;
    private AppAccountInfoVoBean appAccountInfoVo;

    public boolean isOperateSuccess() { return operateSuccess;}

    public void setOperateSuccess(boolean operateSuccess) { this.operateSuccess = operateSuccess;}

    public int getId() { return id;}

    public void setId(int id) { this.id = id;}

    public UserFeatureInfoBean getUserFeatureInfo() { return userFeatureInfo;}

    public void setUserFeatureInfo(
          UserFeatureInfoBean userFeatureInfo) { this.userFeatureInfo = userFeatureInfo;}

    public String getThingCode() { return thingCode;}

    public void setThingCode(String thingCode) { this.thingCode = thingCode;}

    public AppAccountInfoVoBean getAppAccountInfoVo() { return appAccountInfoVo;}

    public void setAppAccountInfoVo(
          AppAccountInfoVoBean appAccountInfoVo) { this.appAccountInfoVo = appAccountInfoVo;}

    public static class UserFeatureInfoBean {

        /**
         * type : 2
         * data : DA40BDC4
         */

        private String type;
        private String data;

        public String getType() { return type;}

        public void setType(String type) { this.type = type;}

        public String getData() { return data;}

        public void setData(String data) { this.data = data;}
    }

    public static class AppAccountInfoVoBean {

        /**
         * accountName : adminUM
         * tenantId : 1
         * userName : 高级管理员
         * roles : [{"roleCode":"0","roleName":"系统管理员","funcs":[]},{"roleCode":"0","roleName":"医院医生","funcs":[]},{"roleCode":"0","roleName":"医院护士","funcs":[]}]
         * accountId : 8a80cb8164d9b3940164da1bff760005
         * userId : 8a80cb8164d9b3940164da1bff760003
         * isFinger : 1
         * isWaidai : 1
         * isEmergency : 0
         * sex : 女
         * useState : 1
         * emergencyPwd : null
         */

        private String accountName;
        private String          tenantId;
        private String          userName;
        private String          accountId;
        private String          userId;
        private int             isFinger;
        private int             isWaidai;
        private int             isEmergency;
        private String          sex;
        private String          useState;
        private Object          emergencyPwd;
        private List<RolesBean> roles;

        public String getAccountName() { return accountName;}

        public void setAccountName(String accountName) { this.accountName = accountName;}

        public String getTenantId() { return tenantId;}

        public void setTenantId(String tenantId) { this.tenantId = tenantId;}

        public String getUserName() { return userName;}

        public void setUserName(String userName) { this.userName = userName;}

        public String getAccountId() { return accountId;}

        public void setAccountId(String accountId) { this.accountId = accountId;}

        public String getUserId() { return userId;}

        public void setUserId(String userId) { this.userId = userId;}

        public int getIsFinger() { return isFinger;}

        public void setIsFinger(int isFinger) { this.isFinger = isFinger;}

        public int getIsWaidai() { return isWaidai;}

        public void setIsWaidai(int isWaidai) { this.isWaidai = isWaidai;}

        public int getIsEmergency() { return isEmergency;}

        public void setIsEmergency(int isEmergency) { this.isEmergency = isEmergency;}

        public String getSex() { return sex;}

        public void setSex(String sex) { this.sex = sex;}

        public String getUseState() { return useState;}

        public void setUseState(String useState) { this.useState = useState;}

        public Object getEmergencyPwd() { return emergencyPwd;}

        public void setEmergencyPwd(Object emergencyPwd) { this.emergencyPwd = emergencyPwd;}

        public List<RolesBean> getRoles() { return roles;}

        public void setRoles(List<RolesBean> roles) { this.roles = roles;}

        public static class RolesBean {

            /**
             * roleCode : 0
             * roleName : 系统管理员
             * funcs : []
             */

            private String roleCode;
            private String  roleName;
            private List<?> funcs;

            public String getRoleCode() { return roleCode;}

            public void setRoleCode(String roleCode) { this.roleCode = roleCode;}

            public String getRoleName() { return roleName;}

            public void setRoleName(String roleName) { this.roleName = roleName;}

            public List<?> getFuncs() { return funcs;}

            public void setFuncs(List<?> funcs) { this.funcs = funcs;}
        }
    }
}
