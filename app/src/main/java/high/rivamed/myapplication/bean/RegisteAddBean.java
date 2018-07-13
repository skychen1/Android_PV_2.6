package high.rivamed.myapplication.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/13 10:13
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class RegisteAddBean implements Serializable {

   private List<RegisteBean> list;
   public  String            boxname;

   public List<RegisteBean> getList() {
	return list;
   }

   public void setList(List<RegisteBean> list) {
	this.list = list;
   }

   public String getBoxname() {
	return boxname;
   }

   public void setBoxname(String boxname) {
	this.boxname = boxname;
   }


   public static class RegisteBean  {

	public String partsname;
	public String partip;

	public String getPartsname() {
	   return partsname;
	}

	public void setPartsname(String partsname) {
	   this.partsname = partsname;
	}

	public String getPartip() {
	   return partip;
	}

	public void setPartip(String partip) {
	   this.partip = partip;
	}

	public List<partsmacBean> getPartsmac() {
	   return partsmac;
	}

	public void setPartsmac(
		List<partsmacBean> partsmac) {
	   this.partsmac = partsmac;
	}

	public List<partsmacBean> partsmac;


	public static class partsmacBean {

	   public String getPartsmacnumber() {
		return partsmacnumber;
	   }

	   public void setPartsmacnumber(String partsmacnumber) {
		this.partsmacnumber = partsmacnumber;
	   }

	   public String partsmacnumber;


	}
   }
}
