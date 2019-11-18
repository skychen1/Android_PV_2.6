package high.rivamed.myapplication.dto.vo;

import com.chad.library.adapter.base.entity.SectionEntity;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称:    Android_PV_2.6.10
 * 创建者:      DanMing
 * 创建时间:    2019/11/6 0006 16:40
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.dto.vo
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class InventoryGroupVos extends SectionEntity<InventoryVo> implements Serializable {

   /**
    * cstName : 蔡司人工晶状体AT TORBI 709M
    * countStock : 1
    * countActual : 0
    * inventoryVos : [{"deviceId":"4028ef956e258e19016e25a36ea70084","cstName":"蔡司人工晶状体AT TORBI 709M","cstId":"0024920c13f74b518b3098cc6cc5cd7a","cstSpec":"AT TORBI 709M CN DPT 31.0 CYL 03.0","expiryDate":"2039-11-01","deviceName":"高值柜","countStock":1,"countActual":0,"count":0,"noConfirmCount":0,"operationStatus":0,"isErrorOperation":0,"expirationText":"2039-11-01","expireStatus":4,"deleteCount":0,"sortNum":30}]
    */

   private String cstName;
   private int countStock;
   private int countActual;
   private List<InventoryVo> inventoryVos;

   public InventoryGroupVos(InventoryVo t) {
	super(t);
   }
   public List<InventoryVo> getInventoryVos() {
	return inventoryVos;
   }

   public void setInventoryVos(List<InventoryVo> inventoryVos) {
	this.inventoryVos = inventoryVos;
   }

   public InventoryGroupVos(boolean isHeader, String header) {
	super(isHeader, header);
   }

   public String getCstName() { return cstName;}

   public void setCstName(String cstName) { this.cstName = cstName;}

   public int getCountStock() { return countStock;}

   public void setCountStock(int countStock) { this.countStock = countStock;}

   public int getCountActual() { return countActual;}

   public void setCountActual(int countActual) { this.countActual = countActual;}



   public static class InventoryVosBean {

	/**
	 * deviceId : 4028ef956e258e19016e25a36ea70084
	 * cstName : 蔡司人工晶状体AT TORBI 709M
	 * cstId : 0024920c13f74b518b3098cc6cc5cd7a
	 * cstSpec : AT TORBI 709M CN DPT 31.0 CYL 03.0
	 * expiryDate : 2039-11-01
	 * deviceName : 高值柜
	 * countStock : 1
	 * countActual : 0
	 * count : 0
	 * noConfirmCount : 0
	 * operationStatus : 0
	 * isErrorOperation : 0
	 * expirationText : 2039-11-01
	 * expireStatus : 4
	 * deleteCount : 0
	 * sortNum : 30
	 */

	private String deviceId;
	private String cstName;
	private String cstId;
	private String cstSpec;
	private String expiryDate;
	private String deviceName;
	private int    countStock;
	private int    countActual;
	private int    count;
	private int    noConfirmCount;
	private int    operationStatus;
	private int    isErrorOperation;
	private String expirationText;
	private int    expireStatus;
	private int    deleteCount;
	private int    sortNum;

	public String getDeviceId() { return deviceId;}

	public void setDeviceId(String deviceId) { this.deviceId = deviceId;}

	public String getCstName() { return cstName;}

	public void setCstName(String cstName) { this.cstName = cstName;}

	public String getCstId() { return cstId;}

	public void setCstId(String cstId) { this.cstId = cstId;}

	public String getCstSpec() { return cstSpec;}

	public void setCstSpec(String cstSpec) { this.cstSpec = cstSpec;}

	public String getExpiryDate() { return expiryDate;}

	public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate;}

	public String getDeviceName() { return deviceName;}

	public void setDeviceName(String deviceName) { this.deviceName = deviceName;}

	public int getCountStock() { return countStock;}

	public void setCountStock(int countStock) { this.countStock = countStock;}

	public int getCountActual() { return countActual;}

	public void setCountActual(int countActual) { this.countActual = countActual;}

	public int getCount() { return count;}

	public void setCount(int count) { this.count = count;}

	public int getNoConfirmCount() { return noConfirmCount;}

	public void setNoConfirmCount(int noConfirmCount) { this.noConfirmCount = noConfirmCount;}

	public int getOperationStatus() { return operationStatus;}

	public void setOperationStatus(int operationStatus) { this.operationStatus = operationStatus;}

	public int getIsErrorOperation() { return isErrorOperation;}

	public void setIsErrorOperation(
		int isErrorOperation) { this.isErrorOperation = isErrorOperation;}

	public String getExpirationText() { return expirationText;}

	public void setExpirationText(String expirationText) { this.expirationText = expirationText;}

	public int getExpireStatus() { return expireStatus;}

	public void setExpireStatus(int expireStatus) { this.expireStatus = expireStatus;}

	public int getDeleteCount() { return deleteCount;}

	public void setDeleteCount(int deleteCount) { this.deleteCount = deleteCount;}

	public int getSortNum() { return sortNum;}

	public void setSortNum(int sortNum) { this.sortNum = sortNum;}
   }
}
