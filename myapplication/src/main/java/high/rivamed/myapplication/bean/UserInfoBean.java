package high.rivamed.myapplication.bean;

import java.io.Serializable;

/**
 * 项目名称:    Android_PV_2.6.12_spd
 * 创建者:      DanMing
 * 创建时间:    2020/3/11 0011 10:11
 * 描述:        个人信息
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class UserInfoBean implements Serializable {

   /**
    * operateSuccess : true
    * id : 0
    * pageNo : 1
    * pageSize : 20
    * opFlg : 200
    * supportRefreshToken : 0
    * userName : 系统管理员
    * accountName : admin
    * roleNames : 系统管理员/系统管理员(pad)/护士（高值耗材柜终端）/护士长/低值系统管理员/护士（高值耗材管理系统）
    * sex : 男
    */

   private boolean operateSuccess;
   private int id;
   private int pageNo;
   private int pageSize;
   private String opFlg;
   private int supportRefreshToken;
   private String userName;
   private String accountName;
   private String roleNames;
   private String sex;

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

   public int getSupportRefreshToken() { return supportRefreshToken;}

   public void setSupportRefreshToken(
	   int supportRefreshToken) { this.supportRefreshToken = supportRefreshToken;}

   public String getUserName() { return userName;}

   public void setUserName(String userName) { this.userName = userName;}

   public String getAccountName() { return accountName;}

   public void setAccountName(String accountName) { this.accountName = accountName;}

   public String getRoleNames() { return roleNames;}

   public void setRoleNames(String roleNames) { this.roleNames = roleNames;}

   public String getSex() { return sex;}

   public void setSex(String sex) { this.sex = sex;}
}
