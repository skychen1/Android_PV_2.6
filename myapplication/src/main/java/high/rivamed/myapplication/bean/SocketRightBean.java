package high.rivamed.myapplication.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/24 18:19
 * 描述:        未确认耗材
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class SocketRightBean implements Serializable{

   /**
    * id : 0
    * tCstInventoryVos : [{"cstName":"弯型和直型腔内吻合器","epc":"95564564566456","cstSpec":"支","expirationTime":"2019-11-01 18:15:43","expiration":null,"deviceName":"1号柜","status":null,"stopFlag":1,"storehouseCode":null,"deviceCode":null,"cstId":null,"operation":null,"storehouseRemark":null,"remake":null,"countStock":0,"countActual":0,"count":0,"lastUpdateDate":"2018-07-18 18:15:56","userName":"超级管理员","statusStr":null},{"cstName":"电动腔直线型血管切割吻合器和钉仓","epc":"955645645664564234","cstSpec":"支","expirationTime":"2018-12-06 18:15:43","expiration":null,"deviceName":"1号柜","status":null,"stopFlag":1,"storehouseCode":null,"deviceCode":null,"cstId":null,"operation":null,"storehouseRemark":null,"remake":null,"countStock":0,"countActual":0,"count":0,"lastUpdateDate":"2018-07-18 18:15:56","userName":"超级管理员","statusStr":null},{"cstName":"弯型和直型腔内吻合器","epc":"955645645632","cstSpec":"支","expirationTime":"2018-10-06 18:15:43","expiration":null,"deviceName":"2号柜","status":null,"stopFlag":1,"storehouseCode":null,"deviceCode":null,"cstId":null,"operation":null,"storehouseRemark":null,"remake":null,"countStock":0,"countActual":0,"count":0,"lastUpdateDate":"2018-07-18 18:15:56","userName":"超级管理员","statusStr":null},{"cstName":"手术刀","epc":"9556456456654644234","cstSpec":"把","expirationTime":"2018-06-01 18:15:43","expiration":null,"deviceName":"1号柜","status":null,"stopFlag":1,"storehouseCode":null,"deviceCode":null,"cstId":null,"operation":null,"storehouseRemark":null,"remake":null,"countStock":0,"countActual":0,"count":0,"lastUpdateDate":"2018-07-18 18:15:56","userName":"超级管理员","statusStr":null},{"cstName":"弯型和直型腔内吻合器","epc":"9556456456324","cstSpec":"支","expirationTime":"2018-11-01 18:15:43","expiration":null,"deviceName":"2号柜","status":null,"stopFlag":1,"storehouseCode":null,"deviceCode":null,"cstId":null,"operation":null,"storehouseRemark":null,"remake":null,"countStock":0,"countActual":0,"count":0,"lastUpdateDate":"2018-07-18 18:15:56","userName":"超级管理员","statusStr":null}]
    * thingCode : 23233
    * nameOrSpecQueryCon :
    * stopFlag : 0
    */

   private int id;
   private String thingCode;
   private String nameOrSpecQueryCon;
   private int stopFlag;
   private List<TCstInventoryVosBean> tCstInventoryVos;

   public int getId() { return id;}

   public void setId(int id) { this.id = id;}

   public String getThingCode() { return thingCode;}

   public void setThingCode(String thingCode) { this.thingCode = thingCode;}

   public String getNameOrSpecQueryCon() { return nameOrSpecQueryCon;}

   public void setNameOrSpecQueryCon(
	   String nameOrSpecQueryCon) { this.nameOrSpecQueryCon = nameOrSpecQueryCon;}

   public int getStopFlag() { return stopFlag;}

   public void setStopFlag(int stopFlag) { this.stopFlag = stopFlag;}

   public List<TCstInventoryVosBean> getTCstInventoryVos() { return tCstInventoryVos;}

   public void setTCstInventoryVos(
	   List<TCstInventoryVosBean> tCstInventoryVos) { this.tCstInventoryVos = tCstInventoryVos;}

   public static class TCstInventoryVosBean {

	/**
	 * cstName : 弯型和直型腔内吻合器
	 * epc : 95564564566456
	 * cstSpec : 支
	 * expirationTime : 2019-11-01 18:15:43
	 * expiration : null
	 * deviceName : 1号柜
	 * status : null
	 * stopFlag : 1
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
	 * userName : 超级管理员
	 * statusStr : null
	 */

	private String cstName;
	private String epc;
	private String cstSpec;
	private String expirationTime;
	private String expiration;
	private String deviceName;
	private Object status;
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
	private String userName;
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

	public Object getStatus() { return status;}

	public void setStatus(Object status) { this.status = status;}

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

	public String getUserName() { return userName;}

	public void setUserName(String userName) { this.userName = userName;}

	public Object getStatusStr() { return statusStr;}

	public void setStatusStr(Object statusStr) { this.statusStr = statusStr;}
   }
}
