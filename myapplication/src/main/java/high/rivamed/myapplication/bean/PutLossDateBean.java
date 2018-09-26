package high.rivamed.myapplication.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称:    Android_PV_2.6.1
 * 创建者:      DanMing
 * 创建时间:    2018/9/25 18:27
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class PutLossDateBean implements Serializable{

   /**
    * operateSuccess : true
    * id : 0
    * cstStockJournals : [{"id":"402882996610260a016610432c660000","accountId":"8a80cb8164d9b3940164da1bff760005","batchNumber":null,"causeRemark":"耗材领用","createdTime":"2018-09-25 18:25:29","cstId":null,"cstName":"角巩膜咬切器","cstSpec":"1.5m","epc":"00021720180412000335","status":0,"thingCode":"4028829965ea4d840165ea53f8dd0000","thingName":null,"inventoryType":1,"inventoryTimes":null,"startTime":null,"endTime":null},{"id":"402882996610260a016610432c680001","accountId":"8a80cb8164d9b3940164da1bff760005","batchNumber":null,"causeRemark":"耗材领用","createdTime":"2018-09-25 18:25:29","cstId":null,"cstName":"角巩膜咬切器","cstSpec":"1.5m","epc":"00021720180412000335","status":0,"thingCode":"4028829965ea4d840165ea53f8dd0000","thingName":null,"inventoryType":1,"inventoryTimes":null,"startTime":null,"endTime":null},{"id":"402882996610260a016610432c680002","accountId":"8a80cb8164d9b3940164da1bff760005","batchNumber":null,"causeRemark":"耗材领用","createdTime":"2018-09-25 18:25:29","cstId":null,"cstName":"角巩膜咬切器","cstSpec":"1.5m","epc":"00021720180412000335","status":0,"thingCode":"4028829965ea4d840165ea53f8dd0000","thingName":null,"inventoryType":1,"inventoryTimes":null,"startTime":null,"endTime":null}]
    */

   private boolean operateSuccess;
   private int id;
   private List<CstStockJournalsBean> cstStockJournals;

   public boolean isOperateSuccess() { return operateSuccess;}

   public void setOperateSuccess(boolean operateSuccess) { this.operateSuccess = operateSuccess;}

   public int getId() { return id;}

   public void setId(int id) { this.id = id;}

   public List<CstStockJournalsBean> getCstStockJournals() { return cstStockJournals;}

   public void setCstStockJournals(
	   List<CstStockJournalsBean> cstStockJournals) { this.cstStockJournals = cstStockJournals;}

   public static class CstStockJournalsBean {

	/**
	 * id : 402882996610260a016610432c660000
	 * accountId : 8a80cb8164d9b3940164da1bff760005
	 * batchNumber : null
	 * causeRemark : 耗材领用
	 * createdTime : 2018-09-25 18:25:29
	 * cstId : null
	 * cstName : 角巩膜咬切器
	 * cstSpec : 1.5m
	 * epc : 00021720180412000335
	 * status : 0
	 * thingCode : 4028829965ea4d840165ea53f8dd0000
	 * thingName : null
	 * inventoryType : 1
	 * inventoryTimes : null
	 * startTime : null
	 * endTime : null
	 */

	private String id;
	private String accountId;
	private Object batchNumber;
	private String causeRemark;
	private String createdTime;
	private Object cstId;
	private String cstName;
	private String cstSpec;
	private String epc;
	private int    status;
	private String thingCode;
	private Object thingName;
	private int    inventoryType;
	private Object inventoryTimes;
	private Object startTime;
	private Object endTime;

	public String getId() { return id;}

	public void setId(String id) { this.id = id;}

	public String getAccountId() { return accountId;}

	public void setAccountId(String accountId) { this.accountId = accountId;}

	public Object getBatchNumber() { return batchNumber;}

	public void setBatchNumber(Object batchNumber) { this.batchNumber = batchNumber;}

	public String getCauseRemark() { return causeRemark;}

	public void setCauseRemark(String causeRemark) { this.causeRemark = causeRemark;}

	public String getCreatedTime() { return createdTime;}

	public void setCreatedTime(String createdTime) { this.createdTime = createdTime;}

	public Object getCstId() { return cstId;}

	public void setCstId(Object cstId) { this.cstId = cstId;}

	public String getCstName() { return cstName;}

	public void setCstName(String cstName) { this.cstName = cstName;}

	public String getCstSpec() { return cstSpec;}

	public void setCstSpec(String cstSpec) { this.cstSpec = cstSpec;}

	public String getEpc() { return epc;}

	public void setEpc(String epc) { this.epc = epc;}

	public int getStatus() { return status;}

	public void setStatus(int status) { this.status = status;}

	public String getThingCode() { return thingCode;}

	public void setThingCode(String thingCode) { this.thingCode = thingCode;}

	public Object getThingName() { return thingName;}

	public void setThingName(Object thingName) { this.thingName = thingName;}

	public int getInventoryType() { return inventoryType;}

	public void setInventoryType(int inventoryType) { this.inventoryType = inventoryType;}

	public Object getInventoryTimes() { return inventoryTimes;}

	public void setInventoryTimes(Object inventoryTimes) { this.inventoryTimes = inventoryTimes;}

	public Object getStartTime() { return startTime;}

	public void setStartTime(Object startTime) { this.startTime = startTime;}

	public Object getEndTime() { return endTime;}

	public void setEndTime(Object endTime) { this.endTime = endTime;}
   }
}
