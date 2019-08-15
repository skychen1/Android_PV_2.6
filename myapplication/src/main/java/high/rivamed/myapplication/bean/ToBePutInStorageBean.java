package high.rivamed.myapplication.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称:    Android_PV_2.6.8.0
 * 创建者:      DanMing
 * 创建时间:    2019/7/18 0018 18:15
 * 描述:        二级库待入库耗材
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class ToBePutInStorageBean implements Serializable {

   /**
    * operateSuccess : true
    * pageModel : {"pageNo":1,"pageSize":10,"rows":[{"cstName":"人类免疫缺陷病毒P24抗原检测试剂盒","cstId":"402882a06753298a0167532aba4a0002","cstSpec":"1234","epc":"00020920180919000050","expiryDate":"2019-11-29","updateDateTime":"2019-05-16 15:07","barcode":"000050","countStock":0,"countActual":0,"count":0,"operationStatus":0,"isErrorOperation":0,"deleteCount":0,"sortNum":30},{"cstName":"人类免疫缺陷病毒P24抗原检测试剂盒","cstId":"402882a06753298a0167532aba4a0002","cstSpec":"1234","epc":"00021520171215000017","expiryDate":"2019-11-29","updateDateTime":"2019-05-16 15:07","barcode":"000017","countStock":0,"countActual":0,"count":0,"operationStatus":0,"isErrorOperation":0,"deleteCount":0,"sortNum":30},{"cstName":"人类免疫缺陷病毒P24抗原检测试剂盒","cstId":"402882a06753298a0167532aba4a0002","cstSpec":"1234","epc":"000000008C10201809040117","expiryDate":"2019-11-29","updateDateTime":"2019-05-16 15:07","barcode":"040117","countStock":0,"countActual":0,"count":0,"operationStatus":0,"isErrorOperation":0,"deleteCount":0,"sortNum":30},{"cstName":"人类免疫缺陷病毒P24抗原检测试剂盒","cstId":"402882a06753298a0167532aba4a0002","cstSpec":"1234","epc":"00021320180104000038","expiryDate":"2019-11-29","updateDateTime":"2019-07-05 09:51","barcode":"000102","countStock":0,"countActual":0,"count":0,"operationStatus":0,"isErrorOperation":0,"deleteCount":0,"sortNum":30},{"cstName":"人类免疫缺陷病毒P24抗原检测试剂盒","cstId":"402882a06753298a0167532aba4a0002","cstSpec":"1234","epc":"00022120180612000112","expiryDate":"2019-11-29","updateDateTime":"2019-07-02 14:29","barcode":"000112","countStock":0,"countActual":0,"count":0,"operationStatus":0,"isErrorOperation":0,"deleteCount":0,"sortNum":30}],"total":29}
    * id : 0
    * opFlg : 200
    * pageNo : 1
    * pageSize : 5
    * account : 0
    * sthId : ff80818168f0b9b501690481ac4d0015
    * inInventoryVos : []
    * outInventoryVos : []
    * inTypeCount : 0
    * outTypeCount : 0
    * inTotalCount : 0
    * outTotalCount : 0
    * errorEpcs : []
    * notInStoreCstEpcs : []
    * fuWai : false
    */

   private boolean operateSuccess;
   private PageModelBean pageModel;
   private int           id;
   private String        opFlg;
   private int           pageNo;
   private int           pageSize;
   private int           account;
   private String        sthId;
   private int           inTypeCount;
   private int           outTypeCount;
   private int           inTotalCount;
   private int           outTotalCount;
   private boolean       fuWai;
   private List<?>       inInventoryVos;
   private List<?>       outInventoryVos;
   private List<?>       errorEpcs;
   private List<?>       notInStoreCstEpcs;
   private int waitInStoreNumber;
   private int outStoreNumber;

   public int getWaitInStoreNumber() {
	return waitInStoreNumber;
   }

   public void setWaitInStoreNumber(int waitInStoreNumber) {
	this.waitInStoreNumber = waitInStoreNumber;
   }

   public int getOutStoreNumber() {
	return outStoreNumber;
   }

   public void setOutStoreNumber(int outStoreNumber) {
	this.outStoreNumber = outStoreNumber;
   }

   public boolean isOperateSuccess() { return operateSuccess;}

   public void setOperateSuccess(boolean operateSuccess) { this.operateSuccess = operateSuccess;}

   public PageModelBean getPageModel() { return pageModel;}

   public void setPageModel(PageModelBean pageModel) { this.pageModel = pageModel;}

   public int getId() { return id;}

   public void setId(int id) { this.id = id;}

   public String getOpFlg() { return opFlg;}

   public void setOpFlg(String opFlg) { this.opFlg = opFlg;}

   public int getPageNo() { return pageNo;}

   public void setPageNo(int pageNo) { this.pageNo = pageNo;}

   public int getPageSize() { return pageSize;}

   public void setPageSize(int pageSize) { this.pageSize = pageSize;}

   public int getAccount() { return account;}

   public void setAccount(int account) { this.account = account;}

   public String getSthId() { return sthId;}

   public void setSthId(String sthId) { this.sthId = sthId;}

   public int getInTypeCount() { return inTypeCount;}

   public void setInTypeCount(int inTypeCount) { this.inTypeCount = inTypeCount;}

   public int getOutTypeCount() { return outTypeCount;}

   public void setOutTypeCount(int outTypeCount) { this.outTypeCount = outTypeCount;}

   public int getInTotalCount() { return inTotalCount;}

   public void setInTotalCount(int inTotalCount) { this.inTotalCount = inTotalCount;}

   public int getOutTotalCount() { return outTotalCount;}

   public void setOutTotalCount(int outTotalCount) { this.outTotalCount = outTotalCount;}

   public boolean isFuWai() { return fuWai;}

   public void setFuWai(boolean fuWai) { this.fuWai = fuWai;}

   public List<?> getInInventoryVos() { return inInventoryVos;}

   public void setInInventoryVos(List<?> inInventoryVos) { this.inInventoryVos = inInventoryVos;}

   public List<?> getOutInventoryVos() { return outInventoryVos;}

   public void setOutInventoryVos(
	   List<?> outInventoryVos) { this.outInventoryVos = outInventoryVos;}

   public List<?> getErrorEpcs() { return errorEpcs;}

   public void setErrorEpcs(List<?> errorEpcs) { this.errorEpcs = errorEpcs;}

   public List<?> getNotInStoreCstEpcs() { return notInStoreCstEpcs;}

   public void setNotInStoreCstEpcs(
	   List<?> notInStoreCstEpcs) { this.notInStoreCstEpcs = notInStoreCstEpcs;}

   public static class PageModelBean {

	/**
	 * pageNo : 1
	 * pageSize : 10
	 * rows : [{"cstName":"人类免疫缺陷病毒P24抗原检测试剂盒","cstId":"402882a06753298a0167532aba4a0002","cstSpec":"1234","epc":"00020920180919000050","expiryDate":"2019-11-29","updateDateTime":"2019-05-16 15:07","barcode":"000050","countStock":0,"countActual":0,"count":0,"operationStatus":0,"isErrorOperation":0,"deleteCount":0,"sortNum":30},{"cstName":"人类免疫缺陷病毒P24抗原检测试剂盒","cstId":"402882a06753298a0167532aba4a0002","cstSpec":"1234","epc":"00021520171215000017","expiryDate":"2019-11-29","updateDateTime":"2019-05-16 15:07","barcode":"000017","countStock":0,"countActual":0,"count":0,"operationStatus":0,"isErrorOperation":0,"deleteCount":0,"sortNum":30},{"cstName":"人类免疫缺陷病毒P24抗原检测试剂盒","cstId":"402882a06753298a0167532aba4a0002","cstSpec":"1234","epc":"000000008C10201809040117","expiryDate":"2019-11-29","updateDateTime":"2019-05-16 15:07","barcode":"040117","countStock":0,"countActual":0,"count":0,"operationStatus":0,"isErrorOperation":0,"deleteCount":0,"sortNum":30},{"cstName":"人类免疫缺陷病毒P24抗原检测试剂盒","cstId":"402882a06753298a0167532aba4a0002","cstSpec":"1234","epc":"00021320180104000038","expiryDate":"2019-11-29","updateDateTime":"2019-07-05 09:51","barcode":"000102","countStock":0,"countActual":0,"count":0,"operationStatus":0,"isErrorOperation":0,"deleteCount":0,"sortNum":30},{"cstName":"人类免疫缺陷病毒P24抗原检测试剂盒","cstId":"402882a06753298a0167532aba4a0002","cstSpec":"1234","epc":"00022120180612000112","expiryDate":"2019-11-29","updateDateTime":"2019-07-02 14:29","barcode":"000112","countStock":0,"countActual":0,"count":0,"operationStatus":0,"isErrorOperation":0,"deleteCount":0,"sortNum":30}]
	 * total : 29
	 */

	private int pageNo;
	private int pageSize;
	private int total;
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
	    * cstName : 人类免疫缺陷病毒P24抗原检测试剂盒
	    * cstId : 402882a06753298a0167532aba4a0002
	    * cstSpec : 1234
	    * epc : 00020920180919000050
	    * expiryDate : 2019-11-29
	    * updateDateTime : 2019-05-16 15:07
	    * barcode : 000050
	    * countStock : 0
	    * countActual : 0
	    * count : 0
	    * operationStatus : 0
	    * isErrorOperation : 0
	    * deleteCount : 0
	    * sortNum : 30
	    */

	   private String cstName;
	   private String cstId;
	   private String cstSpec;
	   private String epc;
	   private String expiryDate;
	   private String updateDateTime;
	   private String barcode;
	   private int    countStock;
	   private int    countActual;
	   private int    count;
	   private int    operationStatus;
	   private int    isErrorOperation;
	   private int    deleteCount;
	   private int    sortNum;
	   private String       status;
	   private String       waitInStoreNo;

	   public String getWaitInStoreNo() {
		return waitInStoreNo;
	   }

	   public void setWaitInStoreNo(String waitInStoreNo) {
		this.waitInStoreNo = waitInStoreNo;
	   }

	   public String getStatus() {
		return status;
	   }

	   public void setStatus(String status) {
		this.status = status;
	   }

	   public String getCstName() { return cstName;}

	   public void setCstName(String cstName) { this.cstName = cstName;}

	   public String getCstId() { return cstId;}

	   public void setCstId(String cstId) { this.cstId = cstId;}

	   public String getCstSpec() { return cstSpec;}

	   public void setCstSpec(String cstSpec) { this.cstSpec = cstSpec;}

	   public String getEpc() { return epc;}

	   public void setEpc(String epc) { this.epc = epc;}

	   public String getExpiryDate() { return expiryDate;}

	   public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate;}

	   public String getUpdateDateTime() { return updateDateTime;}

	   public void setUpdateDateTime(
		   String updateDateTime) { this.updateDateTime = updateDateTime;}

	   public String getBarcode() { return barcode;}

	   public void setBarcode(String barcode) { this.barcode = barcode;}

	   public int getCountStock() { return countStock;}

	   public void setCountStock(int countStock) { this.countStock = countStock;}

	   public int getCountActual() { return countActual;}

	   public void setCountActual(int countActual) { this.countActual = countActual;}

	   public int getCount() { return count;}

	   public void setCount(int count) { this.count = count;}

	   public int getOperationStatus() { return operationStatus;}

	   public void setOperationStatus(
		   int operationStatus) { this.operationStatus = operationStatus;}

	   public int getIsErrorOperation() { return isErrorOperation;}

	   public void setIsErrorOperation(
		   int isErrorOperation) { this.isErrorOperation = isErrorOperation;}

	   public int getDeleteCount() { return deleteCount;}

	   public void setDeleteCount(int deleteCount) { this.deleteCount = deleteCount;}

	   public int getSortNum() { return sortNum;}

	   public void setSortNum(int sortNum) { this.sortNum = sortNum;}
	}
   }
}
