package high.rivamed.myapplication.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称:    Android_PV_2.6.1
 * 创建者:      DanMing
 * 创建时间:    2018/11/1 17:31
 * 描述:        使用记录的患者
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class TakeNotesBean implements Serializable {

   /**
    * pageNo : 1
    * pageSize : 10
    * rows : [{"patientId":"576558","cstNo":null,"patientName":"张倩","sex":"女","deptName":null,"operationSurgeonName":"白日星","operationSurgeonCode":null,"createDate":"2018-10-31 13:41:46","operationName":null,"operatingRoomNoName":"导管手术室4","operationBeginDateTime":null,"status":null,"loperPatsId":null}]
    * total : 1
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
	 * patientId : 576558
	 * cstNo : null
	 * patientName : 张倩
	 * sex : 女
	 * deptName : null
	 * operationSurgeonName : 白日星
	 * operationSurgeonCode : null
	 * createDate : 2018-10-31 13:41:46
	 * operationName : null
	 * operatingRoomNoName : 导管手术室4
	 * operationBeginDateTime : null
	 * status : null
	 * loperPatsId : null
	 */

	private String patientId;
	private Object cstNo;
	private String patientName;
	private String sex;
	private Object deptName;
	private String operationSurgeonName;
	private Object operationSurgeonCode;
	private String createDate;
	private Object operationName;
	private String operatingRoomNoName;
	private Object operationBeginDateTime;
	private Object status;
	private Object loperPatsId;

	public String getPatientId() { return patientId;}

	public void setPatientId(String patientId) { this.patientId = patientId;}

	public Object getCstNo() { return cstNo;}

	public void setCstNo(Object cstNo) { this.cstNo = cstNo;}

	public String getPatientName() { return patientName;}

	public void setPatientName(String patientName) { this.patientName = patientName;}

	public String getSex() { return sex;}

	public void setSex(String sex) { this.sex = sex;}

	public Object getDeptName() { return deptName;}

	public void setDeptName(Object deptName) { this.deptName = deptName;}

	public String getOperationSurgeonName() { return operationSurgeonName;}

	public void setOperationSurgeonName(
		String operationSurgeonName) { this.operationSurgeonName = operationSurgeonName;}

	public Object getOperationSurgeonCode() { return operationSurgeonCode;}

	public void setOperationSurgeonCode(
		Object operationSurgeonCode) { this.operationSurgeonCode = operationSurgeonCode;}

	public String getCreateDate() { return createDate;}

	public void setCreateDate(String createDate) { this.createDate = createDate;}

	public Object getOperationName() { return operationName;}

	public void setOperationName(Object operationName) { this.operationName = operationName;}

	public String getOperatingRoomNoName() { return operatingRoomNoName;}

	public void setOperatingRoomNoName(
		String operatingRoomNoName) { this.operatingRoomNoName = operatingRoomNoName;}

	public Object getOperationBeginDateTime() { return operationBeginDateTime;}

	public void setOperationBeginDateTime(
		Object operationBeginDateTime) { this.operationBeginDateTime = operationBeginDateTime;}

	public Object getStatus() { return status;}

	public void setStatus(Object status) { this.status = status;}

	public Object getLoperPatsId() { return loperPatsId;}

	public void setLoperPatsId(Object loperPatsId) { this.loperPatsId = loperPatsId;}
   }
}
