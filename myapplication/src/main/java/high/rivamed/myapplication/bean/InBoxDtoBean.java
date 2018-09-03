package high.rivamed.myapplication.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/29 17:13
 * 描述:        放入柜子读取数据
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class InBoxDtoBean implements Serializable{

   /**
    * id : 0
    * deviceInventoryVos : [{"cstId":null,"deviceCode":"402882a064e4f6e20164e5253f8d0025","deviceName":null,"tcstInventories":[{"id":null,"barcode":null,"batchNumber":null,"cstId":null,"deviceCode":null,"epc":"00020820180613000011","expirationTime":null,"productionDate":null,"sheetId":null,"status":null,"storehouseCode":null,"createDate":null,"accountId":null,"userName":null,"lastUpdateDate":null,"remake":null,"patientId":null,"storehouseRemark":null},{"id":null,"barcode":null,"batchNumber":null,"cstId":null,"deviceCode":null,"epc":"00022120180612000087","expirationTime":null,"productionDate":null,"sheetId":null,"status":null,"storehouseCode":null,"createDate":null,"accountId":null,"userName":null,"lastUpdateDate":null,"remake":null,"patientId":null,"storehouseRemark":null},{"id":null,"barcode":null,"batchNumber":null,"cstId":null,"deviceCode":null,"epc":"00022120180612000086","expirationTime":null,"productionDate":null,"sheetId":null,"status":null,"storehouseCode":null,"createDate":null,"accountId":null,"userName":null,"lastUpdateDate":null,"remake":null,"patientId":null,"storehouseRemark":null}],"tcstInventoryVos":null}]
    * tCstInventoryVos : [{"cstName":"电动腔直线型血管切割吻合器和钉仓","epc":"00020820180613000011","cstSpec":"支","expirationTime":"2018-06-29 14:26:10","expiration":"已过期","deviceName":"1号柜","status":"禁止入库","stopFlag":0,"storehouseCode":null,"deviceCode":"402882a064e4f6e20164e5253f8d0025","cstId":"33a","operation":null,"storehouseRemark":null,"remake":null,"countStock":0,"countActual":0,"count":0,"lastUpdateDate":null,"userName":null,"statusStr":null},{"cstName":"腔镜关节头直线型切割吻合器和钉仓","epc":"00022120180612000086","cstSpec":"ECR60B","expirationTime":"2022-11-11 14:26:10","expiration":"2022-11-11","deviceName":"1号柜","status":"禁止入库","stopFlag":0,"storehouseCode":null,"deviceCode":"402882a064e4f6e20164e5253f8d0025","cstId":"55a","operation":null,"storehouseRemark":null,"remake":null,"countStock":0,"countActual":0,"count":0,"lastUpdateDate":null,"userName":null,"statusStr":null},{"cstName":"腔镜关节头直线型切割吻合器和钉仓","epc":"00022120180612000087","cstSpec":"ECR60B","expirationTime":"2022-11-11 14:26:10","expiration":"2022-11-11","deviceName":"1号柜","status":"禁止入库","stopFlag":0,"storehouseCode":null,"deviceCode":"402882a064e4f6e20164e5253f8d0025","cstId":"55a","operation":null,"storehouseRemark":null,"remake":null,"countStock":0,"countActual":0,"count":0,"lastUpdateDate":null,"userName":null,"statusStr":null}]
    * thingCode : 402882a064e4f6e20164e52508ff0024
    * operation : 0
    * type : 0
    * stopFlag : 0
    * 	"countTwoin": 0,
    "countMoveIn": 0,
    "countBack": 0,
    "countTempopary": 0
    */

   private int id;
   private String                       thingCode;
   private int                          operation;
   private int                          type;
   private int                          stopFlag;
   private int                          countTwoin;
   private int                          countMoveIn;
   private int                          countBack;
   private int                          countTempopary;
   private List<DeviceInventoryVosBean> deviceInventoryVos;
   private List<TCstInventoryVosBean>   tCstInventoryVos;

   public int getCountTwoin() {
	return countTwoin;
   }

   public void setCountTwoin(int countTwoin) {
	this.countTwoin = countTwoin;
   }

   public int getCountMoveIn() {
	return countMoveIn;
   }

   public void setCountMoveIn(int countMoveIn) {
	this.countMoveIn = countMoveIn;
   }

   public int getCountBack() {
	return countBack;
   }

   public void setCountBack(int countBack) {
	this.countBack = countBack;
   }

   public int getCountTempopary() {
	return countTempopary;
   }

   public void setCountTempopary(int countTempopary) {
	this.countTempopary = countTempopary;
   }

   public List<TCstInventoryVosBean> gettCstInventoryVos() {
	return tCstInventoryVos;
   }

   public void settCstInventoryVos(
	   List<TCstInventoryVosBean> tCstInventoryVos) {
	this.tCstInventoryVos = tCstInventoryVos;
   }

   public int getId() { return id;}

   public void setId(int id) { this.id = id;}

   public String getThingCode() { return thingCode;}

   public void setThingCode(String thingCode) { this.thingCode = thingCode;}

   public int getOperation() { return operation;}

   public void setOperation(int operation) { this.operation = operation;}

   public int getType() { return type;}

   public void setType(int type) { this.type = type;}

   public int getStopFlag() { return stopFlag;}

   public void setStopFlag(int stopFlag) { this.stopFlag = stopFlag;}

   public List<DeviceInventoryVosBean> getDeviceInventoryVos() { return deviceInventoryVos;}

   public void setDeviceInventoryVos(
	   List<DeviceInventoryVosBean> deviceInventoryVos) { this.deviceInventoryVos = deviceInventoryVos;}


   public static class DeviceInventoryVosBean {

	/**
	 * cstId : null
	 * deviceCode : 402882a064e4f6e20164e5253f8d0025
	 * deviceName : null
	 * tcstInventories : [{"id":null,"barcode":null,"batchNumber":null,"cstId":null,"deviceCode":null,"epc":"00020820180613000011","expirationTime":null,"productionDate":null,"sheetId":null,"status":null,"storehouseCode":null,"createDate":null,"accountId":null,"userName":null,"lastUpdateDate":null,"remake":null,"patientId":null,"storehouseRemark":null},{"id":null,"barcode":null,"batchNumber":null,"cstId":null,"deviceCode":null,"epc":"00022120180612000087","expirationTime":null,"productionDate":null,"sheetId":null,"status":null,"storehouseCode":null,"createDate":null,"accountId":null,"userName":null,"lastUpdateDate":null,"remake":null,"patientId":null,"storehouseRemark":null},{"id":null,"barcode":null,"batchNumber":null,"cstId":null,"deviceCode":null,"epc":"00022120180612000086","expirationTime":null,"productionDate":null,"sheetId":null,"status":null,"storehouseCode":null,"createDate":null,"accountId":null,"userName":null,"lastUpdateDate":null,"remake":null,"patientId":null,"storehouseRemark":null}]
	 * tcstInventoryVos : null
	 */

	private Object cstId;
	private String                    deviceCode;
	private Object                    deviceName;
	private Object                    tcstInventoryVos;
	private List<TcstInventoriesBean> tcstInventories;

	public Object getCstCode() { return cstId;}

	public void setCstCode(Object cstId) { this.cstId = cstId;}

	public String getDeviceCode() { return deviceCode;}

	public void setDeviceCode(String deviceCode) { this.deviceCode = deviceCode;}

	public Object getDeviceName() { return deviceName;}

	public void setDeviceName(Object deviceName) { this.deviceName = deviceName;}

	public Object getTcstInventoryVos() { return tcstInventoryVos;}

	public void setTcstInventoryVos(
		Object tcstInventoryVos) { this.tcstInventoryVos = tcstInventoryVos;}

	public List<TcstInventoriesBean> getTcstInventories() { return tcstInventories;}

	public void setTcstInventories(
		List<TcstInventoriesBean> tcstInventories) { this.tcstInventories = tcstInventories;}

	public static class TcstInventoriesBean {

	   /**
	    * id : null
	    * barcode : null
	    * batchNumber : null
	    * cstId : null
	    * deviceCode : null
	    * epc : 00020820180613000011
	    * expirationTime : null
	    * productionDate : null
	    * sheetId : null
	    * status : null
	    * storehouseCode : null
	    * createDate : null
	    * accountId : null
	    * userName : null
	    * lastUpdateDate : null
	    * remake : null
	    * patientId : null
	    * storehouseRemark : null
	    */

	   private Object id;
	   private Object barcode;
	   private Object batchNumber;
	   private Object cstId;
	   private Object deviceCode;
	   private String epc;
	   private Object expirationTime;
	   private Object productionDate;
	   private Object sheetId;
	   private Object status;
	   private Object storehouseCode;
	   private Object createDate;
	   private Object accountId;
	   private Object userName;
	   private Object lastUpdateDate;
	   private Object remark;
	   private Object patientId;
	   private Object storehouseRemark;

	   public Object getId() { return id;}

	   public void setId(Object id) { this.id = id;}

	   public Object getBarcode() { return barcode;}

	   public void setBarcode(Object barcode) { this.barcode = barcode;}

	   public Object getBatchNumber() { return batchNumber;}

	   public void setBatchNumber(Object batchNumber) { this.batchNumber = batchNumber;}

	   public Object getCstCode() { return cstId;}

	   public void setCstCode(Object cstId) { this.cstId = cstId;}

	   public Object getDeviceCode() { return deviceCode;}

	   public void setDeviceCode(Object deviceCode) { this.deviceCode = deviceCode;}

	   public String getEpc() { return epc;}

	   public void setEpc(String epc) { this.epc = epc;}

	   public Object getExpirationTime() { return expirationTime;}

	   public void setExpirationTime(
		   Object expirationTime) { this.expirationTime = expirationTime;}

	   public Object getProductionDate() { return productionDate;}

	   public void setProductionDate(
		   Object productionDate) { this.productionDate = productionDate;}

	   public Object getSheetId() { return sheetId;}

	   public void setSheetId(Object sheetId) { this.sheetId = sheetId;}

	   public Object getStatus() { return status;}

	   public void setStatus(Object status) { this.status = status;}

	   public Object getStorehouseCode() { return storehouseCode;}

	   public void setStorehouseCode(
		   Object storehouseCode) { this.storehouseCode = storehouseCode;}

	   public Object getCreateDate() { return createDate;}

	   public void setCreateDate(Object createDate) { this.createDate = createDate;}

	   public Object getAccountId() { return accountId;}

	   public void setAccountId(Object accountId) { this.accountId = accountId;}

	   public Object getUserName() { return userName;}

	   public void setUserName(Object userName) { this.userName = userName;}

	   public Object getLastUpdateDate() { return lastUpdateDate;}

	   public void setLastUpdateDate(
		   Object lastUpdateDate) { this.lastUpdateDate = lastUpdateDate;}

	   public Object getRemark() { return remark;}

	   public void setRemark(Object remark) { this.remark = remark;}

	   public Object getPatientId() { return patientId;}

	   public void setPatientId(Object patientId) { this.patientId = patientId;}

	   public Object getStorehouseRemark() { return storehouseRemark;}

	   public void setStorehouseRemark(
		   Object storehouseRemark) { this.storehouseRemark = storehouseRemark;}
	}
   }

   public static class TCstInventoryVosBean {

	/**
	 * cstName : 电动腔直线型血管切割吻合器和钉仓
	 * epc : 00020820180613000011
	 * cstSpec : 支
	 * expirationTime : 2018-06-29 14:26:10
	 * expiration : 已过期
	 * deviceName : 1号柜
	 * status : 禁止入库
	 * stopFlag : 0
	 * storehouseCode : null
	 * deviceCode : 402882a064e4f6e20164e5253f8d0025
	 * cstId : 33a
	 * operation : null
	 * storehouseRemark : null
	 * remake : null
	 * countStock : 0
	 * countActual : 0
	 * count : 0
	 * lastUpdateDate : null
	 * userName : null
	 * statusStr : null
	 */

	private String cstName;
	private String epc;
	private String cstSpec;
	private String expirationTime;
	private String expiration;
	private String deviceName;
	private String status;
	private int    stopFlag;
	private String storehouseCode;
	private String deviceCode;
	private String cstId;
	private String operation;
	private String storehouseRemark;
	private String remark;
	private int    countStock;
	private int    countActual;
	private int    count;
	private String lastUpdateDate;
	private String userName;
	private String statusStr;

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

	public String getStorehouseCode() { return storehouseCode;}

	public void setStorehouseCode(String storehouseCode) { this.storehouseCode = storehouseCode;}

	public String getDeviceCode() { return deviceCode;}

	public void setDeviceCode(String deviceCode) { this.deviceCode = deviceCode;}

	public String getCstCode() { return cstId;}

	public void setCstCode(String cstId) { this.cstId = cstId;}

	public String getOperation() { return operation;}

	public void setOperation(String operation) { this.operation = operation;}

	public String getStorehouseRemark() { return storehouseRemark;}

	public void setStorehouseRemark(
		String storehouseRemark) { this.storehouseRemark = storehouseRemark;}

	public String getRemark() { return remark;}

	public void setRemark(String remark) { this.remark = remark;}

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

	public String getStatusStr() { return statusStr;}

	public void setStatusStr(String statusStr) { this.statusStr = statusStr;}
   }
}
