package high.rivamed.myapplication.bean;

import java.io.Serializable;

/**
 * 项目名称:    Android_PV_2.6.10
 * 创建者:      DanMing
 * 创建时间:    2019/11/13 0013 15:17
 * 描述:       LOGO的
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class LogosBean implements Serializable {

   /**
    * id : 0
    * hospitalFile : {"fileId":"4028efa46d6b6843016d6b8698d30001","hospLogo":"IBAw=","updateTime":"2019-11-11"}
    */

   private int id;
   private HospitalFileBean hospitalFile;

   public int getId() { return id;}

   public void setId(int id) { this.id = id;}

   public HospitalFileBean getHospitalFile() { return hospitalFile;}

   public void setHospitalFile(HospitalFileBean hospitalFile) { this.hospitalFile = hospitalFile;}

   public static class HospitalFileBean {

	/**
	 * fileId : 4028efa46d6b6843016d6b8698d30001
	 * hospLogo : IBAw=
	 * updateTime : 2019-11-11
	 */

	private String fileId;
	private String loginPageLogo;
	private String mainInterfaceLogo;
	private String updateTime;

	public String getFileId() { return fileId;}

	public void setFileId(String fileId) { this.fileId = fileId;}

	public String getLoginPageLogo() {
	   return loginPageLogo;
	}

	public void setLoginPageLogo(String loginPageLogo) {
	   this.loginPageLogo = loginPageLogo;
	}

	public String getMainInterfaceLogo() {
	   return mainInterfaceLogo;
	}

	public void setMainInterfaceLogo(String mainInterfaceLogo) {
	   this.mainInterfaceLogo = mainInterfaceLogo;
	}

	public String getUpdateTime() { return updateTime;}

	public void setUpdateTime(String updateTime) { this.updateTime = updateTime;}
   }
}
