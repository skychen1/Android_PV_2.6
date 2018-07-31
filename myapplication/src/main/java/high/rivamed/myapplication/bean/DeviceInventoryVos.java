package high.rivamed.myapplication.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/28 15:44
 * 描述:        封装epc扫描
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class DeviceInventoryVos implements Serializable {

   private String            thingCode;
   private List<DeviceInventoryVo> deviceInventoryVos;


   public List<DeviceInventoryVo> getDeviceInventoryVos() {
	return deviceInventoryVos;
   }

   public void setDeviceInventoryVos(
	   List<DeviceInventoryVo> deviceInventoryVos) {
	this.deviceInventoryVos = deviceInventoryVos;
   }


   public String getThingCode() {
	return thingCode;
   }

   public void setThingCode(String thingCode) {
	this.thingCode = thingCode;
   }



   public static class DeviceInventoryVo {

	private String deviceCode;

	private List<TCstInventory> tCstInventories;

	public String getDeviceCode() {
	   return deviceCode;
	}

	public void setDeviceCode(String deviceCode) {
	   this.deviceCode = deviceCode;
	}

	public List<TCstInventory> gettCstInventories() {
	   return tCstInventories;
	}

	public void settCstInventories(List<TCstInventory> tCstInventories) {
	   this.tCstInventories = tCstInventories;
	}

	public static class TCstInventory {

	   private String epc;

	   public String getEpc() {
		return epc;
	   }

	   public void setEpc(String epc) {
		this.epc = epc;
	   }
	}
   }
}

