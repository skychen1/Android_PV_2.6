package high.rivamed.myapplication.dto.vo;

import java.util.List;

import high.rivamed.myapplication.dto.entity.TCstInventory;

public class DeviceInventoryVo {

	//盘点 耗材详情 cstCode
	private String              cstCode;
	private String              deviceCode;
	private String              deviceName;
	private List<TCstInventory> tCstInventories;
	
	private List<TCstInventoryVo>  tCstInventoryVos;
   private int add;	// 库存情况
   private int reduce;	// 扫描出来的库存

   public int getAdd() {
	return add;
   }

   public void setAdd(int add) {
	this.add = add;
   }

   public int getReduce() {
	return reduce;
   }

   public void setReduce(int reduce) {
	this.reduce = reduce;
   }

   public String getCstCode() {
	return cstCode;
   }

   public void setCstCode(String cstCode) {
	this.cstCode = cstCode;
   }

   public String getDeviceCode() {
	return deviceCode;
   }

   public void setDeviceCode(String deviceCode) {
	this.deviceCode = deviceCode;
   }

   public String getDeviceName() {
	return deviceName;
   }

   public void setDeviceName(String deviceName) {
	this.deviceName = deviceName;
   }

   public List<TCstInventory> gettCstInventories() {
	return tCstInventories;
   }

   public void settCstInventories(
	   List<TCstInventory> tCstInventories) {
	this.tCstInventories = tCstInventories;
   }

   public List<TCstInventoryVo> gettCstInventoryVos() {
	return tCstInventoryVos;
   }

   public void settCstInventoryVos(
	   List<TCstInventoryVo> tCstInventoryVos) {
	this.tCstInventoryVos = tCstInventoryVos;
   }
}
