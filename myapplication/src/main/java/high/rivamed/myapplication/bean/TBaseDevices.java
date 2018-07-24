package high.rivamed.myapplication.bean;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

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

public class TBaseDevices extends AbstractExpandableItem<TBaseDevices.tBaseDevices>
	implements MultiItemEntity {

   private List<tBaseDevices>     list;//柜子list信息
   public  String                 boxname;//柜子名字
   private List<RegisteTitleBean> list2;//柜子list信息

   public List<RegisteTitleBean> getList2() {
	return list2;
   }

   public void setList2(List<RegisteTitleBean> list2) {
	this.list2 = list2;
   }



   public List<tBaseDevices> getList() {
	return list;
   }

   public void setList(List<tBaseDevices> list) {
	this.list = list;
   }

   public String getBoxname() {
	return boxname;
   }

   public void setBoxname(String boxname) {
	this.boxname = boxname;
   }

   public static class RegisteTitleBean extends tBaseDevices implements MultiItemEntity {

	public RegisteTitleBean(String name, String mac, String ip, String type) {
	   this.mName = name;
	   this.mMac = mac;
	   this.mIp = ip;
	   this.mType = type;
	}

	public String getName() {
	   return mName;
	}

	public void setName(String name) {
	   mName = name;
	}

	public String getMac() {
	   return mMac;
	}

	public void setMac(String mac) {
	   mMac = mac;
	}

	public String getIp() {
	   return mIp;
	}

	public void setIp(String ip) {
	   mIp = ip;
	}

	public String getType() {
	   return mType;
	}

	public void setType(String type) {
	   mType = type;
	}

	public String mName;
	public String mMac;
	public String mIp;
	public String mType;

	@Override
	public int getItemType() {
	   return 0;
	}


   }

   @Override
   public int getLevel() {
	return 0;
   }

   @Override
   public int getItemType() {
	return 0;
   }

   public static class tBaseDevices implements MultiItemEntity {

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

	public String getPartsmactext() {
	   return partsmactext;
	}

	public void setPartsmactext(String partsmactext) {
	   this.partsmactext = partsmactext;
	}

	public String partsmactext;

	@Override
	public int getItemType() {
	   return 2;
	}

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
