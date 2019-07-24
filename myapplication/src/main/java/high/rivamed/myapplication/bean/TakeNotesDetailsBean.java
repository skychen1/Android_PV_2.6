package high.rivamed.myapplication.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称:    Android_PV_2.6.1
 * 创建者:      DanMing
 * 创建时间:    2018/11/5 18:11
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class TakeNotesDetailsBean implements Serializable{

   /**
    * id : 0
    * journalUseRecordDetailVos : [{"epc":"00021720180412000332","cstName":"开睑器","cstSpec":"钢丝","manuFactory":null,"vendorName":null,"deptName":null,"storeHouseName":null,"operationTime":null,"userName":"高级管理员","status":"二级库入库"},{"epc":"00021720180412000333","cstName":"缝线结扎镊","cstSpec":"直平台","manuFactory":null,"vendorName":null,"deptName":null,"storeHouseName":null,"operationTime":null,"userName":"高级管理员","status":"已计费"},{"epc":"00021720180412000334","cstName":"角巩膜咬切器","cstSpec":"1.5m","manuFactory":null,"vendorName":null,"deptName":null,"storeHouseName":null,"operationTime":null,"userName":"高级管理员","status":"领用"},{"epc":"00021720180412000335","cstName":"角巩膜咬切器","cstSpec":"1.5m","manuFactory":null,"vendorName":null,"deptName":null,"storeHouseName":null,"operationTime":null,"userName":"高级管理员","status":"领用"},{"epc":"00021720180412000336","cstName":"虹膜剪","cstSpec":"双尖角型","manuFactory":null,"vendorName":null,"deptName":null,"storeHouseName":null,"operationTime":null,"userName":"高级管理员","status":"二级库入库"},{"epc":"00021720180412000337","cstName":"虹膜剪","cstSpec":"双尖角型","manuFactory":null,"vendorName":null,"deptName":null,"storeHouseName":null,"operationTime":null,"userName":"高级管理员","status":"二级库入库"},{"epc":"00021720180412000339","cstName":"角巩膜咬切器","cstSpec":"1.5m","manuFactory":null,"vendorName":null,"deptName":null,"storeHouseName":null,"operationTime":null,"userName":"高级管理员","status":"二级库入库"},{"epc":"00021720180412000340","cstName":"虹膜恢复器","cstSpec":"单头","manuFactory":null,"vendorName":null,"deptName":null,"storeHouseName":null,"operationTime":null,"userName":"高级管理员","status":"二级库入库"},{"epc":"00021720180412000345","cstName":"晶体植入镊","cstSpec":"鸭嘴式","manuFactory":null,"vendorName":null,"deptName":null,"storeHouseName":null,"operationTime":null,"userName":"高级管理员","status":"已计费"},{"epc":"00021720180412000346","cstName":"真空采血管3","cstSpec":"4ml(454029)绿管","manuFactory":null,"vendorName":null,"deptName":null,"storeHouseName":null,"operationTime":null,"userName":"高级管理员","status":"已计费"},{"epc":"00021720180412000349","cstName":"真空采血管2","cstSpec":"3ML(454334) 蓝管","manuFactory":null,"vendorName":null,"deptName":null,"storeHouseName":null,"operationTime":null,"userName":"高级管理员","status":"已计费"},{"epc":"00021720180412000350","cstName":"真空采血管2","cstSpec":"3ML(454334) 蓝管","manuFactory":null,"vendorName":null,"deptName":null,"storeHouseName":null,"operationTime":null,"userName":"高级管理员","status":"已计费"},{"epc":"00021720180412000354","cstName":"虹膜恢复器","cstSpec":"单头","manuFactory":null,"vendorName":null,"deptName":null,"storeHouseName":null,"operationTime":null,"userName":"高级管理员","status":"已计费"}]
    * pageNo : 0
    * pageSize : 0
    * count : 0
    * status : 0
    */

   private int                             id;
   private int                             pageNo;
   private int                             pageSize;
   private int                             count;
   private String                          status;
   private List<JournalUseRecordDetailVos> journalUseRecordDetailVos;

   public int getId() { return id;}

   public void setId(int id) { this.id = id;}

   public int getPageNo() { return pageNo;}

   public void setPageNo(int pageNo) { this.pageNo = pageNo;}

   public int getPageSize() { return pageSize;}

   public void setPageSize(int pageSize) { this.pageSize = pageSize;}

   public int getCount() { return count;}

   public void setCount(int count) { this.count = count;}

   public String getStatus() { return status;}

   public void setStatus(String status) { this.status = status;}

   public List<JournalUseRecordDetailVos> getJournalUseRecordDetailVos() { return journalUseRecordDetailVos;}

   public void setJournalUseRecordDetailVos(
	   List<JournalUseRecordDetailVos> tTransInPatientInfoUseDetailVos) { this.journalUseRecordDetailVos = tTransInPatientInfoUseDetailVos;}

   public static class JournalUseRecordDetailVos {

	/**
	 * epc : 00021720180412000332
	 * cstName : 开睑器
	 * cstSpec : 钢丝
	 * manuFactory : null
	 * vendorName : null
	 * deptName : null
	 * storeHouseName : null
	 * operationTime : null
	 * userName : 高级管理员
	 * status : 二级库入库
	 */

	private String epc;
	private String cstName;
	private String cstSpec;
	private String deviceName;
	private Object manuFactory;
	private Object vendorName;
	private Object deptName;
	private Object storeHouseName;
	private String operationTime;
	private String userName;
	private String status;
	private String barcode;

	public String getBarcode() {
	   return barcode;
	}

	public void setBarcode(String barcode) {
	   this.barcode = barcode;
	}

	public String getDeviceName() {
	   return deviceName;
	}

	public void setDeviceName(String deviceName) {
	   this.deviceName = deviceName;
	}

	public String getEpc() { return epc;}

	public void setEpc(String epc) { this.epc = epc;}

	public String getCstName() { return cstName;}

	public void setCstName(String cstName) { this.cstName = cstName;}

	public String getCstSpec() { return cstSpec;}

	public void setCstSpec(String cstSpec) { this.cstSpec = cstSpec;}

	public Object getManuFactory() { return manuFactory;}

	public void setManuFactory(Object manuFactory) { this.manuFactory = manuFactory;}

	public Object getVendorName() { return vendorName;}

	public void setVendorName(Object vendorName) { this.vendorName = vendorName;}

	public Object getDeptName() { return deptName;}

	public void setDeptName(Object deptName) { this.deptName = deptName;}

	public Object getStoreHouseName() { return storeHouseName;}

	public void setStoreHouseName(Object storeHouseName) { this.storeHouseName = storeHouseName;}

	public String getOperationTime() { return operationTime;}

	public void setOperationTime(String operationTime) { this.operationTime = operationTime;}

	public String getUserName() { return userName;}

	public void setUserName(String userName) { this.userName = userName;}

	public String getStatus() { return status;}

	public void setStatus(String status) { this.status = status;}
   }
}
