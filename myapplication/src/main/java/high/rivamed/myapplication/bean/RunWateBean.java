package high.rivamed.myapplication.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/25 10:55
 * 描述:        耗材流水
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class RunWateBean implements Serializable{
//````````````````````````````
 private String deviceCode;
 private String term;
 private String startTime;
 private String endTime;
 private String status;

   public String getDeviceCode() {
	return deviceCode;
   }

   public void setDeviceCode(String deviceCode) {
	this.deviceCode = deviceCode;
   }

   public String getTerm() {
	return term;
   }

   public void setTerm(String term) {
	this.term = term;
   }

   public String getStartTime() {
	return startTime;
   }

   public void setStartTime(String startTime) {
	this.startTime = startTime;
   }

   public String getEndTime() {
	return endTime;
   }

   public void setEndTime(String endTime) {
	this.endTime = endTime;
   }

   public String getStatus() {
	return status;
   }

   public void setStatus(String status) {
	this.status = status;
   }
   //``````````````````````````````
   /**
    * pageNo : 1
    * pageSize : 10
    * rows : [{"status":"0","cstName":"弯型和直型腔内吻合器","cstSpec":"支","expirationTime":"2013-12-06 18:15:43","expiration":"已过期","deviceName":"2号柜","deviceCode":"22222","optionDate":"2018-07-25 15:59:33","optionName":"adminUM","type":0,"count":null,"epc":"95564564566457"},{"status":"0","cstName":"手术刀","cstSpec":"把","expirationTime":"2019-11-01 18:15:43","expiration":"2019-11-01","deviceName":"5号柜","deviceCode":"55555","optionDate":"2018-07-25 15:59:33","optionName":"adminUM","type":4,"count":null,"epc":"95564564566456"},{"status":"0","cstName":"弯型和直型腔内吻合器","cstSpec":"支","expirationTime":"2019-11-06 18:15:43","expiration":"2019-11-06","deviceName":"2号柜","deviceCode":"22222","optionDate":"2018-07-25 15:59:33","optionName":"adminUM","type":4,"count":null,"epc":"955645645665464"},{"status":"0","cstName":"弯型和直型腔内吻合器","cstSpec":"支","expirationTime":"2018-10-06 18:15:43","expiration":"≤90","deviceName":"2号柜","deviceCode":"22222","optionDate":"2018-07-25 15:59:33","optionName":"adminUM","type":2,"count":null,"epc":"955645645632"},{"status":"0","cstName":"电动腔直线型血管切割吻合器和钉仓","cstSpec":"支","expirationTime":"2018-03-23 18:15:43","expiration":"已过期","deviceName":"4号柜","deviceCode":"44444","optionDate":"2018-07-25 15:59:33","optionName":"adminUM","type":0,"count":null,"epc":"95564564567645"},{"status":"0","cstName":"弯型和直型腔内吻合器","cstSpec":"支","expirationTime":"2018-01-24 18:15:43","expiration":"已过期","deviceName":"2号柜","deviceCode":"22222","optionDate":"2018-07-25 15:59:33","optionName":"adminUM","type":0,"count":null,"epc":"95564564565354"}]
    * total : 6
    */

   private int pageNo;
   private int            pageSize;
   private int            total;
   private List<RowsBean> rows;

   public int getPageNo() { return pageNo;}

   public void setPageNo(int pageNo) { this.pageNo = pageNo;}

   public int getPageSize() { return pageSize;}

   public void setPageSize(int pageSize) { this.pageSize = pageSize;}

   public int getTotal() { return total;}

   public void setTotal(int total) { this.total = total;}

   public List<RowsBean> getRows() { return rows;}

   public void setRows(List<RowsBean> rows) { this.rows = rows;}

   public static class RowsBean {

	/**
	 * status : 0
	 * cstName : 弯型和直型腔内吻合器
	 * cstSpec : 支
	 * expirationTime : 2013-12-06 18:15:43
	 * expiration : 已过期
	 * deviceName : 2号柜
	 * deviceCode : 22222
	 * optionDate : 2018-07-25 15:59:33
	 * optionName : adminUM
	 * type : 0
	 * count : null
	 * epc : 95564564566457
	 */

	private String status;
	private String cstName;
	private String cstSpec;
	private String expirationTime;
	private String expiration;
	private String deviceName;
	private String deviceCode;
	private String optionDate;
	private String optionName;
	private int    stopFlag;
	private Object count;
	private String epc;

	public String getStatus() { return status;}

	public void setStatus(String status) { this.status = status;}

	public String getCstName() { return cstName;}

	public void setCstName(String cstName) { this.cstName = cstName;}

	public String getCstSpec() { return cstSpec;}

	public void setCstSpec(String cstSpec) { this.cstSpec = cstSpec;}

	public String getExpirationTime() { return expirationTime;}

	public void setExpirationTime(String expirationTime) { this.expirationTime = expirationTime;}

	public String getExpiration() { return expiration;}

	public void setExpiration(String expiration) { this.expiration = expiration;}

	public String getDeviceName() { return deviceName;}

	public void setDeviceName(String deviceName) { this.deviceName = deviceName;}

	public String getDeviceCode() { return deviceCode;}

	public void setDeviceCode(String deviceCode) { this.deviceCode = deviceCode;}

	public String getOptionDate() { return optionDate;}

	public void setOptionDate(String optionDate) { this.optionDate = optionDate;}

	public String getOptionName() { return optionName;}

	public void setOptionName(String optionName) { this.optionName = optionName;}

	public int getStopFlag() { return stopFlag;}

	public void setStopFlag(int stopFlag) { this.stopFlag = stopFlag;}

	public Object getCount() { return count;}

	public void setCount(Object count) { this.count = count;}

	public String getEpc() { return epc;}

	public void setEpc(String epc) { this.epc = epc;}
   }
}
