package high.rivamed.myapplication.dto;

/**
 * @作者: 钱凯
 * @创建时间: 2018/8/2
 * @功能描述:登录dto
 */
public class UserLoginDto {

    /**
     * account : {"password":"000000","accountName":"adminUM"}
     */

    private String thingId;
    private String systemType;

    public String getThingId() {
        return thingId;
    }

    public void setThingId(String thingId) {
        this.thingId = thingId;
    }

    private AccountBean account;

    public String getSystemType() {
        return systemType;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }

    public AccountBean getAccount() {
        return account;
    }

    public void setAccount(AccountBean account) {
        this.account = account;
    }

    public static class AccountBean {
        /**
         * password : 000000
         * accountName : adminUM
         */

        private String password;
        private String accountName;

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getAccountName() {
            return accountName;
        }

        public void setAccountName(String accountName) {
            this.accountName = accountName;
        }
    }
}
