package high.rivamed.myapplication.dto;

import android.accounts.Account;

import java.util.List;

import high.rivamed.myapplication.dto.entity.TCstInventory;
import high.rivamed.myapplication.dto.vo.CstExpirationVo;
import high.rivamed.myapplication.dto.vo.DeviceInventoryVo;
import high.rivamed.myapplication.dto.vo.TCstInventoryVo;

/**
 *
 * 描述: TODO<br/>
 * 公司: 北京瑞华康源科技有限公司<br/>
 * 版权: rivamed2018<br/>
 *
 * @author 魏小波
 * @date 2018-07-12 12:14:19
 * @version V1.0
 */
public class TCstInventoryDto {

   private static final long serialVersionUID = 1L;
   int id;
   private TCstInventory           tCstInventory;
   private List<TCstInventory>     tCstInventorys;
   private List<DeviceInventoryVo> deviceInventoryVos;
   private List<TCstInventoryVo>   tCstInventoryVos;
   private List<InventorysBean>         inventorys;
   //效期数量监控
   private List<CstExpirationVo> cstExpirationVos;
   private String   configPatientCollar;
   private String  thingCode;
   private String  deviceCode;
   private int     operation;
   private String  requestResult;
   private int type;	//0 放入 1取出
   private String  cstSpec;
   private Account account;
   //名称及规格查询条件
   private String  nameOrSpecQueryCon;
   private int add;	// 库存情况
   private int reduce;	// 扫描出来的库存
   private String  cstCode;
   private String  remake;	//退货备注
   private String  storehouseRemark;	//移出备注
   private String  storehouseCode;		//调拨库房
   private int     stopFlag;		//效期情况 0过期 1-3近效期 4正常
   private int                          countTwoin;
   private int                          countMoveIn;
   private int                          countBack;
   private int                          countTempopary;
   private String                          epcName;
   private String                          patientId;
   private String                          patientName;

   public String getPatientId() {
	return patientId;
   }

   public void setPatientId(String patientId) {
	this.patientId = patientId;
   }

   public String getPatientName() {
	return patientName;
   }

   public void setPatientName(String patientName) {
	this.patientName = patientName;
   }

   public List<InventorysBean> getInventorys() {
	return inventorys;
   }

   public void setInventorys(
	   List<InventorysBean> inventorys) {
	this.inventorys = inventorys;
   }

   public String getEpcName() {
	return epcName;
   }

   public void setEpcName(String epcName) {
	this.epcName = epcName;
   }

   public String getConfigPatientCollar() {
	return configPatientCollar;
   }

   public void setConfigPatientCollar(String configPatientCollar) {
	this.configPatientCollar = configPatientCollar;
   }

   public static long getSerialVersionUID() {
	return serialVersionUID;
   }

   public int getId() {
	return id;
   }

   public void setId(int id) {
	this.id = id;
   }

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

   public String getRemake() {
	return remake;
   }

   public void setRemake(String remake) {
	this.remake = remake;
   }

   public String getStorehouseRemark() {
	return storehouseRemark;
   }

   public void setStorehouseRemark(String storehouseRemark) {
	this.storehouseRemark = storehouseRemark;
   }

   public String getStorehouseCode() {
	return storehouseCode;
   }

   public void setStorehouseCode(String storehouseCode) {
	this.storehouseCode = storehouseCode;
   }

   public int getOperation() {
	return operation;
   }

   public void setOperation(int operation) {
	this.operation = operation;
   }

   public TCstInventory getTCstInventory() {
	return tCstInventory;
   }

   public void setTCstInventory(TCstInventory tCstInventory) {
	this.tCstInventory = tCstInventory;
   }

   public List<TCstInventory> getTCstInventorys() {
	return tCstInventorys;
   }

   public void setTCstInventorys(List<TCstInventory> tCstInventorys) {
	this.tCstInventorys = tCstInventorys;
   }

   public String getThingCode() {
	return this.thingCode;
   }

   public void setThingCode(String thingCode) {
	this.thingCode = thingCode;
   }

   public List<TCstInventoryVo> gettCstInventoryVos() {
	return tCstInventoryVos;
   }

   public void settCstInventoryVos(List<TCstInventoryVo> tCstInventoryVos) {
	this.tCstInventoryVos = tCstInventoryVos;
   }

   public String getRequestResult() {
	return requestResult;
   }

   public void setRequestResult(String requestResult) {
	this.requestResult = requestResult;
   }

   public int getType() {
	return type;
   }

   public void setType(int type) {
	this.type = type;
   }

   public TCstInventory gettCstInventory() {
	return tCstInventory;
   }

   public void settCstInventory(TCstInventory tCstInventory) {
	this.tCstInventory = tCstInventory;
   }


   public String getNameOrSpecQueryCon() {
	return nameOrSpecQueryCon;
   }

   public List<CstExpirationVo> getCstExpirationVos() {
	return cstExpirationVos;
   }

   public void setCstExpirationVos(List<CstExpirationVo> cstExpirationVos) {
	this.cstExpirationVos = cstExpirationVos;
   }
   public void setNameOrSpecQueryCon(String nameOrSpecQueryCon) {
	this.nameOrSpecQueryCon = nameOrSpecQueryCon;
   }

   public String getCstSpec() {
	return cstSpec;
   }

   public void setCstSpec(String cstSpec) {
	this.cstSpec = cstSpec;
   }

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

   public List<DeviceInventoryVo> getDeviceInventoryVos() {
	return deviceInventoryVos;
   }

   public void setDeviceInventoryVos(List<DeviceInventoryVo> deviceInventoryVos) {
	this.deviceInventoryVos = deviceInventoryVos;
   }
   public List<TCstInventory> gettCstInventorys() {
	return tCstInventorys;
   }

   public void settCstInventorys(List<TCstInventory> tCstInventorys) {
	this.tCstInventorys = tCstInventorys;
   }

   public Account getAccount() {
	return account;
   }

   public void setAccount(Account account) {
	this.account = account;
   }

   public String getDeviceCode() {
	return deviceCode;
   }

   public void setDeviceCode(String deviceCode) {
	this.deviceCode = deviceCode;
   }

   public String getCstCode() {
	return cstCode;
   }

   public void setCstCode(String cstCode) {
	this.cstCode = cstCode;
   }

   public int getStopFlag() {
	return stopFlag;
   }

   public void setStopFlag(int stopFlag) {
	this.stopFlag = stopFlag;
   }

   public static class InventorysBean {

	/**
	 * epc : 00020820180613000014
	 * cstName : 弯型和直型腔内吻合器
	 * cstSpec : 支
	 * expiration : 已过期
	 * deviceName : 1号柜
	 * stopFlag : 0
	 */

	private String epc;
	private String cstName;
	private String cstSpec;
	private String expiration;
	private String deviceName;
	private String stopFlag;
	private String countStock;
	private String countActual;

	public String getCountStock() {
	   return countStock;
	}

	public void setCountStock(String countStock) {
	   this.countStock = countStock;
	}

	public String getCountActual() {
	   return countActual;
	}

	public void setCountActual(String countActual) {
	   this.countActual = countActual;
	}

	public String getEpc() { return epc;}

	public void setEpc(String epc) { this.epc = epc;}

	public String getCstName() { return cstName;}

	public void setCstName(String cstName) { this.cstName = cstName;}

	public String getCstSpec() { return cstSpec;}

	public void setCstSpec(String cstSpec) { this.cstSpec = cstSpec;}

	public String getExpiration() { return expiration;}

	public void setExpiration(String expiration) { this.expiration = expiration;}

	public String getDeviceName() { return deviceName;}

	public void setDeviceName(String deviceName) { this.deviceName = deviceName;}

	public String getStopFlag() { return stopFlag;}

	public void setStopFlag(String stopFlag) { this.stopFlag = stopFlag;}
   }
}
