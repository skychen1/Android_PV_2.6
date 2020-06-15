package high.rivamed.myapplication.bean;

import java.io.Serializable;

/**
 * 项目名称:    Android_PV_2.6.12_spd
 * 创建者:      DanMing
 * 创建时间:    2020/3/11 0011 9:34
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class LoginInfoBean implements Serializable {

   /**
    * operateSuccess : true
    * id : 0
    * pageNo : 1
    * pageSize : 20
    * opFlg : 200
    * userFeatureInfo : {"userId":"10000000000000000000000000000001","type":"2"}
    * appAccountInfoVo : {"clientId":null,"accountName":null,"tenantId":null,"userName":null,"accountId":null,"password":null,"salt":null,"userId":null,"deptId":null,"isFinger":0,"isWaidai":0,"isFace":0,"isEmergency":0,"sex":null,"useState":null,"emergencyPwd":null,"systemType":null,"waidaiName":null,"mobileNum":null,"fingerNames":null,"faceId":null}
    */

   private boolean operateSuccess;
   private int                  id;
   private int                  pageNo;
   private int                  pageSize;
   private String               opFlg;
   private UserFeatureInfoBean  userFeatureInfo;
   private AppAccountInfoVoBean appAccountInfoVo;

   public boolean isOperateSuccess() { return operateSuccess;}

   public void setOperateSuccess(boolean operateSuccess) { this.operateSuccess = operateSuccess;}

   public int getId() { return id;}

   public void setId(int id) { this.id = id;}

   public int getPageNo() { return pageNo;}

   public void setPageNo(int pageNo) { this.pageNo = pageNo;}

   public int getPageSize() { return pageSize;}

   public void setPageSize(int pageSize) { this.pageSize = pageSize;}

   public String getOpFlg() { return opFlg;}

   public void setOpFlg(String opFlg) { this.opFlg = opFlg;}

   public UserFeatureInfoBean getUserFeatureInfo() { return userFeatureInfo;}

   public void setUserFeatureInfo(
	   UserFeatureInfoBean userFeatureInfo) { this.userFeatureInfo = userFeatureInfo;}

   public AppAccountInfoVoBean getAppAccountInfoVo() { return appAccountInfoVo;}

   public void setAppAccountInfoVo(
	   AppAccountInfoVoBean appAccountInfoVo) { this.appAccountInfoVo = appAccountInfoVo;}

   public static class UserFeatureInfoBean {

	/**
	 * userId : 10000000000000000000000000000001
	 * type : 2
	 */

	private String userId;
	private String type;

	public String getUserId() { return userId;}

	public void setUserId(String userId) { this.userId = userId;}

	public String getType() { return type;}

	public void setType(String type) { this.type = type;}
   }

   public static class AppAccountInfoVoBean {

	/**
	 * clientId : null
	 * accountName : null
	 * tenantId : null
	 * userName : null
	 * accountId : null
	 * password : null
	 * salt : null
	 * userId : null
	 * deptId : null
	 * isFinger : 0
	 * isWaidai : 0
	 * isFace : 0
	 * isEmergency : 0
	 * sex : null
	 * useState : null
	 * emergencyPwd : null
	 * systemType : null
	 * waidaiName : null
	 * mobileNum : null
	 * fingerNames : null
	 * faceId : null
	 */

	private String clientId;
	private String accountName;
	private String tenantId;
	private String userName;
	private String accountId;
	private String password;
	private String salt;
	private String userId;
	private String deptId;
	private int    isFinger;
	private int    isWaidai;
	private int    isFace;
	private int    isEmergency;
	private String sex;
	private String useState;
	private String emergencyPwd;
	private String systemType;
	private String waidaiName;
	private String mobileNum;
	private String fingerNames;
	private String faceId;

	public String getClientId() { return clientId;}

	public void setClientId(String clientId) { this.clientId = clientId;}

	public String getAccountName() { return accountName;}

	public void setAccountName(String accountName) { this.accountName = accountName;}

	public String getTenantId() { return tenantId;}

	public void setTenantId(String tenantId) { this.tenantId = tenantId;}

	public String getUserName() { return userName;}

	public void setUserName(String userName) { this.userName = userName;}

	public String getAccountId() { return accountId;}

	public void setAccountId(String accountId) { this.accountId = accountId;}

	public String getPassword() { return password;}

	public void setPassword(String password) { this.password = password;}

	public String getSalt() { return salt;}

	public void setSalt(String salt) { this.salt = salt;}

	public String getUserId() { return userId;}

	public void setUserId(String userId) { this.userId = userId;}

	public String getDeptId() { return deptId;}

	public void setDeptId(String deptId) { this.deptId = deptId;}

	public int getIsFinger() { return isFinger;}

	public void setIsFinger(int isFinger) { this.isFinger = isFinger;}

	public int getIsWaidai() { return isWaidai;}

	public void setIsWaidai(int isWaidai) { this.isWaidai = isWaidai;}

	public int getIsFace() { return isFace;}

	public void setIsFace(int isFace) { this.isFace = isFace;}

	public int getIsEmergency() { return isEmergency;}

	public void setIsEmergency(int isEmergency) { this.isEmergency = isEmergency;}

	public String getSex() { return sex;}

	public void setSex(String sex) { this.sex = sex;}

	public String getUseState() { return useState;}

	public void setUseState(String useState) { this.useState = useState;}

	public String getEmergencyPwd() { return emergencyPwd;}

	public void setEmergencyPwd(String emergencyPwd) { this.emergencyPwd = emergencyPwd;}

	public String getSystemType() { return systemType;}

	public void setSystemType(String systemType) { this.systemType = systemType;}

	public String getWaidaiName() { return waidaiName;}

	public void setWaidaiName(String waidaiName) { this.waidaiName = waidaiName;}

	public String getMobileNum() { return mobileNum;}

	public void setMobileNum(String mobileNum) { this.mobileNum = mobileNum;}

	public String getFingerNames() { return fingerNames;}

	public void setFingerNames(String fingerNames) { this.fingerNames = fingerNames;}

	public String getFaceId() { return faceId;}

	public void setFaceId(String faceId) { this.faceId = faceId;}
   }
}
