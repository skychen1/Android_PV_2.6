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
    * pageSize : 20
    * rows : [{"patientId":"4028829366c2ad720166c2e1787d0009","cstNo":null,"patientName":"啦啦啦啦啦啦啦","gender":"未知","deptName":null,"doctorName":"","operationSurgeonCode":null,"scheduleTime":"2018-11-02 18:17:09","operationName":null,"roomName":"","operationBeginDateTime":"2018-10-30 10:55:00","status":null,"loperPatsId":null}]
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
	 * patientId : 4028829366c2ad720166c2e1787d0009
	 * cstNo : null
	 * patientName : 啦啦啦啦啦啦啦
	 * gender : 未知
	 * deptName : null
	 * doctorName :
	 * operationSurgeonCode : null
	 * scheduleTime : 2018-11-02 18:17:09
	 * operationName : null
	 * roomName :
	 * operationBeginDateTime : 2018-10-30 10:55:00
	 * status : null
	 * loperPatsId : null
	 */

	private String patientId;
	private Object cstNo;
	private String patientName;
	private String gender;
	private Object deptName;
	private String doctorName;
	private Object operationSurgeonCode;
	private String scheduleTime;
	private Object operationName;
	private String roomName;
	private String operationBeginDateTime;
	private Object status;
	private Object loperPatsId;
	private String hisPatientId        ;

	public String getHisPatientId() {
	   return hisPatientId;
	}

	public void setHisPatientId(String hisPatientId) {
	   this.hisPatientId = hisPatientId;
	}
	public String getPatientId() { return patientId;}

	public void setPatientId(String patientId) { this.patientId = patientId;}

	public Object getCstNo() { return cstNo;}

	public void setCstNo(Object cstNo) { this.cstNo = cstNo;}

	public String getPatientName() { return patientName;}

	public void setPatientName(String patientName) { this.patientName = patientName;}

	public String getGender() { return gender;}

	public void setGender(String gender) { this.gender = gender;}

	public Object getDeptName() { return deptName;}

	public void setDeptName(Object deptName) { this.deptName = deptName;}

	public String getDoctorName() { return doctorName;}

	public void setDoctorName(
		String doctorName) { this.doctorName = doctorName;}

	public Object getOperationSurgeonCode() { return operationSurgeonCode;}

	public void setOperationSurgeonCode(
		Object operationSurgeonCode) { this.operationSurgeonCode = operationSurgeonCode;}

	public String getScheduleTime() { return scheduleTime;}

	public void setScheduleTime(String scheduleTime) { this.scheduleTime = scheduleTime;}

	public Object getOperationName() { return operationName;}

	public void setOperationName(Object operationName) { this.operationName = operationName;}

	public String getRoomName() { return roomName;}

	public void setRoomName(
		String roomName) { this.roomName = roomName;}

	public String getOperationBeginDateTime() { return operationBeginDateTime;}

	public void setOperationBeginDateTime(
		String operationBeginDateTime) { this.operationBeginDateTime = operationBeginDateTime;}

	public Object getStatus() { return status;}

	public void setStatus(Object status) { this.status = status;}

	public Object getLoperPatsId() { return loperPatsId;}

	public void setLoperPatsId(Object loperPatsId) { this.loperPatsId = loperPatsId;}
   }
}
