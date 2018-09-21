package high.rivamed.myapplication.bean;

import java.io.Serializable;

/**
 * 项目名称:    Android_PV_2.6.1
 * 创建者:      DanMing
 * 创建时间:    2018/9/20 15:21
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class UnRegistBean implements Serializable{

   /**
    * operateSuccess : true
    * id : 0
    * msg : 已成功解绑用户
    * userFeatureInfo : {"userId":"ff80818165ac85b30165ace55a07000d","type":"2"}
    */

   private boolean operateSuccess;
   private int                 id;
   private String              msg;
   private UserFeatureInfoBean userFeatureInfo;

   public boolean isOperateSuccess() { return operateSuccess;}

   public void setOperateSuccess(boolean operateSuccess) { this.operateSuccess = operateSuccess;}

   public int getId() { return id;}

   public void setId(int id) { this.id = id;}

   public String getMsg() { return msg;}

   public void setMsg(String msg) { this.msg = msg;}

   public UserFeatureInfoBean getUserFeatureInfo() { return userFeatureInfo;}

   public void setUserFeatureInfo(
	   UserFeatureInfoBean userFeatureInfo) { this.userFeatureInfo = userFeatureInfo;}

   public static class UserFeatureInfoBean {

	/**
	 * userId : ff80818165ac85b30165ace55a07000d
	 * type : 2
	 */

	private String userId;
	private String type;

	public String getUserId() { return userId;}

	public void setUserId(String userId) { this.userId = userId;}

	public String getType() { return type;}

	public void setType(String type) { this.type = type;}
   }
}
