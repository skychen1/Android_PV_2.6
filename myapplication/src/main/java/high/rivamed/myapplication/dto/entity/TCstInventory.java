package high.rivamed.myapplication.dto.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * The persistent class for the t_cst_inventory database table.
 * 
 */
/**
* <p>标题: TCstInventory.java</p>
* <p>业务描述:耗材库存信息 </p>
* <p>公司:北京瑞华康源科技有限公司</p>
* <p>版权:rivamed-2018</p>
* @author 魏小波
* @date 2018年7月17日
* @version V1.0 
*/

public class TCstInventory implements Serializable {


	private static final long serialVersionUID = 1L;

	/**
	 * 状态:一级库出库(目前北肿有此状态)
	 */
	public static final int STATUS_ONEOUT = 1;
	/**
	 * 状态： 二级库入库
	 */
	public static final int STATUS_TWOIN = 2;
	/**
	 * 状态： 领用
	 */
	public static final int STATUS_TWOOUT = 3;

	/**
	 * 状态： 已使用
	 */
	public static final int STATUS_USE = 4;

	/**
	 * 状态： 已计费
	 */
	public static final int STATUS_CHARG_SUCCESS = 5;

	/**
	 * 状态： 作废
	 */
	public static final int STATUS_CANCEL = 6;
	/**
	 * 状态 ： 退回  (与病人解除绑定, 目前就流水记录此状态, 未改变耗材状态, 执行此操作时,
	 * 耗材状态为领用)
	 * 低值为二级库退回出库
	 */
	public static final int STATUS_BACK = 7;
	/**
	 * 状态： 退货
	 */
	public static final int STATUS_RECALL = 8;
	/***
	 * 移出状态
	 */
	public static final int MOVE_OUT = 9;

	/***
	 * 移入状态
	 */
	public static final int MOVE_IN = 10;
	/**
	 * 调拨
	 */
	public static final int STATUS_ALLOCAT = 11;
	/**
	 * 退货暂存
	 */
	public static final int STATUS_TEMPORARY = 12;



	private String id;

	//条码号
	private String barcode;

	//生产批号

	private String batchNumber;

	//耗材编码
	private String cstId;

	//耗材所在设备部件编码
	private String deviceCode;

	private String epc;

	//有效期
	private Date expirationTime;

	//生产日期
	private Date productionDate;

	//单号
	private String sheetId;

	private String status;

	//库房编码
	private String storehouseCode;
	
	private Date createDate;
	
	private String accountId;
	
	private String userName;
	
	private Date lastUpdateDate;
	
	private String remark;
	
	private String patientId;

   public static long getSerialVersionUID() {
	return serialVersionUID;
   }

   public static int getStatusOneout() {
	return STATUS_ONEOUT;
   }

   public static int getStatusTwoin() {
	return STATUS_TWOIN;
   }

   public static int getStatusTwoout() {
	return STATUS_TWOOUT;
   }

   public static int getStatusUse() {
	return STATUS_USE;
   }

   public static int getStatusChargSuccess() {
	return STATUS_CHARG_SUCCESS;
   }

   public static int getStatusCancel() {
	return STATUS_CANCEL;
   }

   public static int getStatusBack() {
	return STATUS_BACK;
   }

   public static int getStatusRecall() {
	return STATUS_RECALL;
   }

   public static int getMoveOut() {
	return MOVE_OUT;
   }

   public static int getMoveIn() {
	return MOVE_IN;
   }

   public static int getStatusAllocat() {
	return STATUS_ALLOCAT;
   }

   public static int getStatusTemporary() {
	return STATUS_TEMPORARY;
   }

   public String getId() {
	return id;
   }

   public void setId(String id) {
	this.id = id;
   }

   public String getBarcode() {
	return barcode;
   }

   public void setBarcode(String barcode) {
	this.barcode = barcode;
   }

   public String getBatchNumber() {
	return batchNumber;
   }

   public void setBatchNumber(String batchNumber) {
	this.batchNumber = batchNumber;
   }

   public String getCstCode() {
	return cstId;
   }

   public void setCstCode(String cstId) {
	this.cstId = cstId;
   }

   public String getDeviceCode() {
	return deviceCode;
   }

   public void setDeviceCode(String deviceCode) {
	this.deviceCode = deviceCode;
   }

   public String getEpc() {
	return epc;
   }

   public void setEpc(String epc) {
	this.epc = epc;
   }

   public Date getExpirationTime() {
	return expirationTime;
   }

   public void setExpirationTime(Date expirationTime) {
	this.expirationTime = expirationTime;
   }

   public Date getProductionDate() {
	return productionDate;
   }

   public void setProductionDate(Date productionDate) {
	this.productionDate = productionDate;
   }

   public String getSheetId() {
	return sheetId;
   }

   public void setSheetId(String sheetId) {
	this.sheetId = sheetId;
   }

   public String getStatus() {
	return status;
   }

   public void setStatus(String status) {
	this.status = status;
   }

   public String getStorehouseCode() {
	return storehouseCode;
   }

   public void setStorehouseCode(String storehouseCode) {
	this.storehouseCode = storehouseCode;
   }

   public Date getCreateDate() {
	return createDate;
   }

   public void setCreateDate(Date createDate) {
	this.createDate = createDate;
   }

   public String getAccountId() {
	return accountId;
   }

   public void setAccountId(String accountId) {
	this.accountId = accountId;
   }

   public String getUserName() {
	return userName;
   }

   public void setUserName(String userName) {
	this.userName = userName;
   }

   public Date getLastUpdateDate() {
	return lastUpdateDate;
   }

   public void setLastUpdateDate(Date lastUpdateDate) {
	this.lastUpdateDate = lastUpdateDate;
   }

   public String getRemark() {
	return remark;
   }

   public void setRemark(String remark) {
	this.remark = remark;
   }

   public String getPatientId() {
	return patientId;
   }

   public void setPatientId(String patientId) {
	this.patientId = patientId;
   }

   public String getStorehouseRemark() {
	return storehouseRemark;
   }

   public void setStorehouseRemark(String storehouseRemark) {
	this.storehouseRemark = storehouseRemark;
   }

   //移出目标库房
	private String storehouseRemark;
	
	

}