package high.rivamed.myapplication.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/8/3 12:26
 * 描述:        绑定患者  查询的患者
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class BingFindSchedulesBean implements Serializable{

   /**
    * id : 0
    * thingCode : 402882a064f9ab850164f9ad0a470000
    * patientInfos : [{"patientName":"李四","patientId":"3213123","requestDateTime":"","operationSurgeonName":"","operatingRoomNoName":""},{"patientName":"张三","requestDateTime":"2018-08-03 11:54:46","operationSurgeonName":"王医生","operatingRoomNoName":"312312","patientId":"3231232"}]
    */

   private int id;
   private String thingCode;
   private List<PatientInfosBean> patientInfos;

   public int getId() { return id;}

   public void setId(int id) { this.id = id;}

   public String getThingCode() { return thingCode;}

   public void setThingCode(String thingCode) { this.thingCode = thingCode;}

   public List<PatientInfosBean> getPatientInfos() { return patientInfos;}

   public void setPatientInfos(
	   List<PatientInfosBean> patientInfos) { this.patientInfos = patientInfos;}

   public static class PatientInfosBean {

	/**
	 * patientName : 李四
	 * patientId : 3213123
	 * requestDateTime :
	 * operationSurgeonName :
	 * operatingRoomNoName :
	 */

	private String patientName;
	private String patientId;
	private String requestDateTime;
	private String operationSurgeonName;
	private String operatingRoomNoName;

	public String getPatientName() { return patientName;}

	public void setPatientName(String patientName) { this.patientName = patientName;}

	public String getPatientId() { return patientId;}

	public void setPatientId(String patientId) { this.patientId = patientId;}

	public String getRequestDateTime() { return requestDateTime;}

	public void setRequestDateTime(
		String requestDateTime) { this.requestDateTime = requestDateTime;}

	public String getOperationSurgeonName() { return operationSurgeonName;}

	public void setOperationSurgeonName(
		String operationSurgeonName) { this.operationSurgeonName = operationSurgeonName;}

	public String getOperatingRoomNoName() { return operatingRoomNoName;}

	public void setOperatingRoomNoName(
		String operatingRoomNoName) { this.operatingRoomNoName = operatingRoomNoName;}
   }
}
