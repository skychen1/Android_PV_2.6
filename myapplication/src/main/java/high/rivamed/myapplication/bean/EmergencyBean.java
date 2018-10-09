package high.rivamed.myapplication.bean;

import java.io.Serializable;

/**
 * 项目名称:    Android_PV_2.6.1
 * 创建者:      DanMing
 * 创建时间:    2018/10/9 17:11
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class EmergencyBean implements Serializable{

   /**
    * operateSuccess : true
    * id : 0
    * msg : 成功绑定紧急登录密码
    * account : {"accountId":"4028829965564a5f016556695fc10000","accountName":"zy","loginNum":0,"password":"43bdfdf2890b7e6e5a9fa66a5ab279028344ec75","regTime":"2018-08-28 14:04:54","tenantId":"4028829965564a5f016556695fc10000","updateTime":"2018-09-08 14:14:33","useState":"0","emergencyPwd":"111111"}
    */

   private boolean operateSuccess;
   private int         id;
   private String      msg;
   private AccountBean account;

   public boolean isOperateSuccess() { return operateSuccess;}

   public void setOperateSuccess(boolean operateSuccess) { this.operateSuccess = operateSuccess;}

   public int getId() { return id;}

   public void setId(int id) { this.id = id;}

   public String getMsg() { return msg;}

   public void setMsg(String msg) { this.msg = msg;}

   public AccountBean getAccount() { return account;}

   public void setAccount(AccountBean account) { this.account = account;}

   public static class AccountBean {

	/**
	 * accountId : 4028829965564a5f016556695fc10000
	 * accountName : zy
	 * loginNum : 0
	 * password : 43bdfdf2890b7e6e5a9fa66a5ab279028344ec75
	 * regTime : 2018-08-28 14:04:54
	 * tenantId : 4028829965564a5f016556695fc10000
	 * updateTime : 2018-09-08 14:14:33
	 * useState : 0
	 * emergencyPwd : 111111
	 */

	private String accountId;
	private String accountName;
	private int    loginNum;
	private String password;
	private String regTime;
	private String tenantId;
	private String updateTime;
	private String useState;
	private String emergencyPwd;

	public String getAccountId() { return accountId;}

	public void setAccountId(String accountId) { this.accountId = accountId;}

	public String getAccountName() { return accountName;}

	public void setAccountName(String accountName) { this.accountName = accountName;}

	public int getLoginNum() { return loginNum;}

	public void setLoginNum(int loginNum) { this.loginNum = loginNum;}

	public String getPassword() { return password;}

	public void setPassword(String password) { this.password = password;}

	public String getRegTime() { return regTime;}

	public void setRegTime(String regTime) { this.regTime = regTime;}

	public String getTenantId() { return tenantId;}

	public void setTenantId(String tenantId) { this.tenantId = tenantId;}

	public String getUpdateTime() { return updateTime;}

	public void setUpdateTime(String updateTime) { this.updateTime = updateTime;}

	public String getUseState() { return useState;}

	public void setUseState(String useState) { this.useState = useState;}

	public String getEmergencyPwd() { return emergencyPwd;}

	public void setEmergencyPwd(String emergencyPwd) { this.emergencyPwd = emergencyPwd;}
   }
}
