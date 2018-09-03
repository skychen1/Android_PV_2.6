package high.rivamed.myapplication.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/24 20:19
 * 描述:       库存状态里面的耗材详情
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class StockDetailsBean implements Serializable {

   /**
    * id : 0
    * tCstInventoryVos : [{"cstName":"手术刀","epc":"95564564566457","cstSpec":null,"expirationTime":"2013-12-06 18:15:43","expiration":"已过期","deviceName":null,"status":"0","stopFlag":0,"storehouseCode":null,"deviceCode":null,"cstId":null,"operation":null,"storehouseRemark":null,"remake":null,"countStock":0,"countActual":0,"count":0,"lastUpdateDate":"2018-07-18 18:15:56","userName":null,"statusStr":null},{"cstName":"手术刀","epc":"95564564566457432","cstSpec":null,"expirationTime":"2018-12-04 18:15:43","expiration":"2018-12-04","deviceName":null,"status":"0","stopFlag":4,"storehouseCode":null,"deviceCode":null,"cstId":null,"operation":null,"storehouseRemark":null,"remake":null,"countStock":0,"countActual":0,"count":0,"lastUpdateDate":"2018-07-18 18:15:56","userName":null,"statusStr":null}]
    * deviceCode : 33333
    * cstId : 44a
    * stopFlag : 0
    */



   private int                        id;
   private String                     deviceCode;
   private String                     cstId;
   private int                        stopFlag;
   private List<TCstInventoryVosBean> tCstInventoryVos;

   public int getId() { return id;}

   public void setId(int id) { this.id = id;}

   public String getDeviceCode() { return deviceCode;}

   public void setDeviceCode(String deviceCode) { this.deviceCode = deviceCode;}

   public String getCstCode() { return cstId;}

   public void setCstCode(String cstId) { this.cstId = cstId;}

   public int getStopFlag() { return stopFlag;}

   public void setStopFlag(int stopFlag) { this.stopFlag = stopFlag;}

   public List<TCstInventoryVosBean> getTCstInventoryVos() { return tCstInventoryVos;}

   public void setTCstInventoryVos(
	   List<TCstInventoryVosBean> tCstInventoryVos) { this.tCstInventoryVos = tCstInventoryVos;}

   public static class TCstInventoryVosBean {

	/**
	 * cstName : 手术刀
	 * epc : 95564564566457
	 * cstSpec : null
	 * expirationTime : 2013-12-06 18:15:43
	 * expiration : 已过期
	 * deviceName : null
	 * status : 0
	 * stopFlag : 0
	 * storehouseCode : null
	 * deviceCode : null
	 * cstId : null
	 * operation : null
	 * storehouseRemark : null
	 * remake : null
	 * countStock : 0
	 * countActual : 0
	 * count : 0
	 * lastUpdateDate : 2018-07-18 18:15:56
	 * userName : null
	 * statusStr : null
	 */

	private String cstName;
	private String epc;
	private Object cstSpec;
	private String expirationTime;
	private String expiration;
	private Object deviceName;
	private String status;
	private int    stopFlag;
	private Object storehouseCode;
	private Object deviceCode;
	private Object cstId;
	private Object operation;
	private Object storehouseRemark;
	private Object remark;
	private int    countStock;
	private int    countActual;
	private int    count;
	private String lastUpdateDate;
	private Object userName;
	private Object statusStr;

	public String getCstName() { return cstName;}

	public void setCstName(String cstName) { this.cstName = cstName;}

	public String getEpc() { return epc;}

	public void setEpc(String epc) { this.epc = epc;}

	public Object getCstSpec() { return cstSpec;}

	public void setCstSpec(Object cstSpec) { this.cstSpec = cstSpec;}

	public String getExpirationTime() { return expirationTime;}

	public void setExpirationTime(String expirationTime) { this.expirationTime = expirationTime;}

	public String getExpiration() { return expiration;}

	public void setExpiration(String expiration) { this.expiration = expiration;}

	public Object getDeviceName() { return deviceName;}

	public void setDeviceName(Object deviceName) { this.deviceName = deviceName;}

	public String getStatus() { return status;}

	public void setStatus(String status) { this.status = status;}

	public int getStopFlag() { return stopFlag;}

	public void setStopFlag(int stopFlag) { this.stopFlag = stopFlag;}

	public Object getStorehouseCode() { return storehouseCode;}

	public void setStorehouseCode(Object storehouseCode) { this.storehouseCode = storehouseCode;}

	public Object getDeviceCode() { return deviceCode;}

	public void setDeviceCode(Object deviceCode) { this.deviceCode = deviceCode;}

	public Object getCstCode() { return cstId;}

	public void setCstCode(Object cstId) { this.cstId = cstId;}

	public Object getOperation() { return operation;}

	public void setOperation(Object operation) { this.operation = operation;}

	public Object getStorehouseRemark() { return storehouseRemark;}

	public void setStorehouseRemark(
		Object storehouseRemark) { this.storehouseRemark = storehouseRemark;}

	public Object getRemark() { return remark;}

	public void setRemark(Object remark) { this.remark = remark;}

	public int getCountStock() { return countStock;}

	public void setCountStock(int countStock) { this.countStock = countStock;}

	public int getCountActual() { return countActual;}

	public void setCountActual(int countActual) { this.countActual = countActual;}

	public int getCount() { return count;}

	public void setCount(int count) { this.count = count;}

	public String getLastUpdateDate() { return lastUpdateDate;}

	public void setLastUpdateDate(String lastUpdateDate) { this.lastUpdateDate = lastUpdateDate;}

	public Object getUserName() { return userName;}

	public void setUserName(Object userName) { this.userName = userName;}

	public Object getStatusStr() { return statusStr;}

	public void setStatusStr(Object statusStr) { this.statusStr = statusStr;}
   }
}
