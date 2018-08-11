package high.rivamed.myapplication.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/23 16:42
 * 描述:        库存监控 耗材库存预警
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class SocketLeftDownBean implements Serializable{

   /**
    * id : 0
    * tCstInventoryVos : [{"cstName":"手术刀","epc":null,"cstSpec":"把","expirationTime":"2013-12-06 18:15:43","expiration":null,"deviceName":"3号柜","status":null,"stopFlag":1,"storehouseCode":null,"deviceCode":"33333","cstCode":"44a","operation":null,"storehouseRemark":null,"remake":null,"countStock":0,"countActual":0,"count":2,"lastUpdateDate":null,"userName":null,"statusStr":null},{"cstName":"电动腔直线型血管切割吻合器和钉仓","epc":null,"cstSpec":"支","expirationTime":"2018-01-24 18:15:43","expiration":null,"deviceName":"2号柜","status":null,"stopFlag":1,"storehouseCode":null,"deviceCode":"22222","cstCode":"33a","operation":null,"storehouseRemark":null,"remake":null,"countStock":0,"countActual":0,"count":1,"lastUpdateDate":null,"userName":null,"statusStr":null},{"cstName":"弯型和直型腔内吻合器","epc":null,"cstSpec":"支","expirationTime":"2018-03-05 18:15:43","expiration":null,"deviceName":"4号柜","status":null,"stopFlag":1,"storehouseCode":null,"deviceCode":"44444","cstCode":"22a","operation":null,"storehouseRemark":null,"remake":null,"countStock":0,"countActual":0,"count":1,"lastUpdateDate":null,"userName":null,"statusStr":null},{"cstName":"手术刀","epc":null,"cstSpec":"把","expirationTime":"2018-06-01 18:15:43","expiration":null,"deviceName":"1号柜","status":null,"stopFlag":1,"storehouseCode":null,"deviceCode":"11111","cstCode":"44a","operation":null,"storehouseRemark":null,"remake":null,"countStock":0,"countActual":0,"count":1,"lastUpdateDate":null,"userName":null,"statusStr":null},{"cstName":"弯型和直型腔内吻合器","epc":null,"cstSpec":"支","expirationTime":"2018-10-06 18:15:43","expiration":null,"deviceName":"2号柜","status":null,"stopFlag":1,"storehouseCode":null,"deviceCode":"22222","cstCode":"22a","operation":null,"storehouseRemark":null,"remake":null,"countStock":0,"countActual":0,"count":2,"lastUpdateDate":null,"userName":null,"statusStr":null},{"cstName":"电动腔直线型血管切割吻合器和钉仓","epc":null,"cstSpec":"支","expirationTime":"2018-12-06 18:15:43","expiration":null,"deviceName":"5号柜","status":null,"stopFlag":1,"storehouseCode":null,"deviceCode":"55555","cstCode":"33a","operation":null,"storehouseRemark":null,"remake":null,"countStock":0,"countActual":0,"count":1,"lastUpdateDate":null,"userName":null,"statusStr":null},{"cstName":"电动腔直线型血管切割吻合器和钉仓","epc":null,"cstSpec":"支","expirationTime":"2018-12-06 18:15:43","expiration":null,"deviceName":"1号柜","status":null,"stopFlag":1,"storehouseCode":null,"deviceCode":"11111","cstCode":"33a","operation":null,"storehouseRemark":null,"remake":null,"countStock":0,"countActual":0,"count":1,"lastUpdateDate":null,"userName":null,"statusStr":null},{"cstName":"弯型和直型腔内吻合器","epc":null,"cstSpec":"支","expirationTime":"2019-11-01 18:15:43","expiration":null,"deviceName":"1号柜","status":null,"stopFlag":1,"storehouseCode":null,"deviceCode":"11111","cstCode":"22a","operation":null,"storehouseRemark":null,"remake":null,"countStock":0,"countActual":0,"count":1,"lastUpdateDate":null,"userName":null,"statusStr":null},{"cstName":"手术刀","epc":null,"cstSpec":"把","expirationTime":"2019-11-06 18:15:43","expiration":null,"deviceName":"4号柜","status":null,"stopFlag":1,"storehouseCode":null,"deviceCode":"44444","cstCode":"44a","operation":null,"storehouseRemark":null,"remake":null,"countStock":0,"countActual":0,"count":1,"lastUpdateDate":null,"userName":null,"statusStr":null}]
    * thingCode : 23233
    */


   private int id;
   private String thingCode;
   private List<TCstInventoryVosBean> tCstInventoryVos;

   public int getId() { return id;}

   public void setId(int id) { this.id = id;}

   public String getThingCode() { return thingCode;}

   public void setThingCode(String thingCode) { this.thingCode = thingCode;}

   public List<TCstInventoryVosBean> getTCstInventoryVos() { return tCstInventoryVos;}

   public void setTCstInventoryVos(
	   List<TCstInventoryVosBean> tCstInventoryVos) { this.tCstInventoryVos = tCstInventoryVos;}

   public static class TCstInventoryVosBean {

	/**
	 * cstName : 手术刀
	 * epc : null
	 * cstSpec : 把
	 * expirationTime : 2013-12-06 18:15:43
	 * expiration : null
	 * deviceName : 3号柜
	 * status : null
	 * stopFlag : 1
	 * storehouseCode : null
	 * deviceCode : 33333
	 * cstCode : 44a
	 * operation : null
	 * storehouseRemark : null
	 * remake : null
	 * countStock : 0
	 * countActual : 0
	 * count : 2
	 * lastUpdateDate : null
	 * userName : null
	 * statusStr : null
	 */
	//自己添加的带过去的数据----
	private int    size;
	private String name;
	private String type;

	public int getSize() {
	   return size;
	}

	public void setSize(int size) {
	   this.size = size;
	}

	public String getName() {
	   return name;
	}

	public void setName(String name) {
	   this.name = name;
	}

	public String getType() {
	   return type;
	}

	public void setType(String type) {
	   this.type = type;
	}

	//---------
	private String cstName;
	private String epc;
	private String cstSpec;
	private String expirationTime;
	private String expiration;
	private String deviceName;
	private String status;
	private int    stopFlag;
	private Object storehouseCode;
	private String deviceCode;
	private String cstCode;
	private Object operation;
	private Object storehouseRemark;
	private Object remark;
	private int    countStock;
	private int    countActual;
	private int    count;
	private Object lastUpdateDate;
	private Object userName;
	private Object statusStr;

	public String getCstName() { return cstName;}

	public void setCstName(String cstName) { this.cstName = cstName;}

	public String getEpc() { return epc;}

	public void setEpc(String epc) { this.epc = epc;}

	public String getCstSpec() { return cstSpec;}

	public void setCstSpec(String cstSpec) { this.cstSpec = cstSpec;}

	public String getExpirationTime() { return expirationTime;}

	public void setExpirationTime(String expirationTime) { this.expirationTime = expirationTime;}

	public String getExpiration() { return expiration;}

	public void setExpiration(String expiration) { this.expiration = expiration;}

	public String getDeviceName() { return deviceName;}

	public void setDeviceName(String deviceName) { this.deviceName = deviceName;}

	public String getStatus() { return status;}

	public void setStatus(String status) { this.status = status;}

	public int getStopFlag() { return stopFlag;}

	public void setStopFlag(int stopFlag) { this.stopFlag = stopFlag;}

	public Object getStorehouseCode() { return storehouseCode;}

	public void setStorehouseCode(Object storehouseCode) { this.storehouseCode = storehouseCode;}

	public String getDeviceCode() { return deviceCode;}

	public void setDeviceCode(String deviceCode) { this.deviceCode = deviceCode;}

	public String getCstCode() { return cstCode;}

	public void setCstCode(String cstCode) { this.cstCode = cstCode;}

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

	public Object getLastUpdateDate() { return lastUpdateDate;}

	public void setLastUpdateDate(Object lastUpdateDate) { this.lastUpdateDate = lastUpdateDate;}

	public Object getUserName() { return userName;}

	public void setUserName(Object userName) { this.userName = userName;}

	public Object getStatusStr() { return statusStr;}

	public void setStatusStr(Object statusStr) { this.statusStr = statusStr;}
   }
}
