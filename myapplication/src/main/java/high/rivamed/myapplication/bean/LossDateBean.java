package high.rivamed.myapplication.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称:    Android_PV_2.6.1
 * 创建者:      DanMing
 * 创建时间:    2018/9/25 14:23
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class LossDateBean implements Serializable{

   private List<CstStockJournalsBean> cstStockJournals;

   public List<CstStockJournalsBean> getCstStockJournals() { return cstStockJournals;}

   public void setCstStockJournals(
	   List<CstStockJournalsBean> cstStockJournals) { this.cstStockJournals = cstStockJournals;}

   public static class CstStockJournalsBean {

	/**
	 * thingCode : 110
	 * accountId : 110
	 * cstName : 110
	 * cstId : 110
	 * epc : *1517011622023467
	 * thingName : 高值柜-1
	 * causeRemark : 领取耗材
	 * batchNumber : 第一批
	 * cstSpec : 30093243278423
	 */

	private String thingCode;
	private String accountId;
	private String cstName;
	private String cstId;
	private String epc;
	private String thingName;
	private String causeRemark;
	private String batchNumber;
	private String cstSpec;

	public String getThingCode() { return thingCode;}

	public void setThingCode(String thingCode) { this.thingCode = thingCode;}

	public String getAccountId() { return accountId;}

	public void setAccountId(String accountId) { this.accountId = accountId;}

	public String getCstName() { return cstName;}

	public void setCstName(String cstName) { this.cstName = cstName;}

	public String getCstId() { return cstId;}

	public void setCstId(String cstId) { this.cstId = cstId;}

	public String getEpc() { return epc;}

	public void setEpc(String epc) { this.epc = epc;}

	public String getThingName() { return thingName;}

	public void setThingName(String thingName) { this.thingName = thingName;}

	public String getCauseRemark() { return causeRemark;}

	public void setCauseRemark(String causeRemark) { this.causeRemark = causeRemark;}

	public String getBatchNumber() { return batchNumber;}

	public void setBatchNumber(String batchNumber) { this.batchNumber = batchNumber;}

	public String getCstSpec() { return cstSpec;}

	public void setCstSpec(String cstSpec) { this.cstSpec = cstSpec;}
   }
}
