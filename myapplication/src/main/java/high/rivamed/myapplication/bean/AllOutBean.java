package high.rivamed.myapplication.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称:    Android_PV_2.6.1
 * 创建者:      DanMing
 * 创建时间:    2018/9/27 14:08
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class AllOutBean implements Serializable {

   /**
    * storehouseCode : ff80818165b1fb680165b35e320d0056
    * tCstInventoryVos : [{"deviceCode":"4028829965ea4d840165ea53f8e60001","epc":"00021720180412000331"},{"deviceCode":"4028829965ea4d840165ea53f8e60001","epc":"00021720180412000332"},{"deviceCode":"4028829965ea4d840165ea53f8f60005","epc":"00021720180412000334"},{"deviceCode":"4028829965ea4d840165ea53f8e60001","epc":"00021720180412000335"},{"deviceCode":"4028829965ea4d840165ea53f8e60001","epc":"00021720180412000336"},{"deviceCode":"4028829965ea4d840165ea53f8e60001","epc":"00021720180412000339"},{"deviceCode":"4028829965ea4d840165ea53f8e60001","epc":"00021720180412000340"}]
    */

   private String storehouseCode;
   private List<TCstInventoryVos> tCstInventoryVos;

   public String getStorehouseCode() { return storehouseCode;}

   public void setStorehouseCode(String storehouseCode) { this.storehouseCode = storehouseCode;}

   public List<TCstInventoryVos> getTCstInventoryVos() { return tCstInventoryVos;}

   public void setTCstInventoryVos(
	   List<TCstInventoryVos> tCstInventoryVos) { this.tCstInventoryVos = tCstInventoryVos;}

   public static class TCstInventoryVos{

	/**
	 * deviceCode : 4028829965ea4d840165ea53f8e60001
	 * epc : 00021720180412000331
	 */

	private String deviceCode;
	private String epc;

	public String getDeviceCode() { return deviceCode;}

	public void setDeviceCode(String deviceCode) { this.deviceCode = deviceCode;}

	public String getEpc() { return epc;}

	public void setEpc(String epc) { this.epc = epc;}
   }
}
