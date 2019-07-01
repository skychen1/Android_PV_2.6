package high.rivamed.myapplication.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称:    Android_PV_2.6.6_514C
 * 创建者:      DanMing
 * 创建时间:    2019/6/26 14:34
 * 描述:        异常处理提交
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class inventoryUnNormalHandleVo implements Serializable {

   private List<InventoryUnNormalHandleVosBean> inventoryUnNormalHandleVos;

   public List<InventoryUnNormalHandleVosBean> getInventoryUnNormalHandleVos() { return inventoryUnNormalHandleVos;}

   public void setInventoryUnNormalHandleVos(
	   List<InventoryUnNormalHandleVosBean> inventoryUnNormalHandleVos) { this.inventoryUnNormalHandleVos = inventoryUnNormalHandleVos;}

   public static class InventoryUnNormalHandleVosBean {

	/**
	 * unNormalId : 2
	 * operationStatue : 3
	 * patientId : 0002
	 * patientName : 陶玉璞
	 * medicalId : 002
	 * surgeryId : 002
	 * deptId : 002
	 * thingId : 40288b3f6a47d0d2016a47e956180000
	 */

	private String unNormalId;
	private String operationStatue;
	private String patientId;
	private String patientName;
	private String medicalId;
	private String surgeryId;
	private String deptId;
	private String thingId;
	private String removeStatue;
	private String outStatue;
	private String operatorId;
	private String operatorName;
	private String targetSthId;
	private String targetDeptId;
	private String returnReason;

	public String getTargetSthId() {
	   return targetSthId;
	}

	public void setTargetSthId(String targetSthId) {
	   this.targetSthId = targetSthId;
	}

	public String getTargetDeptId() {
	   return targetDeptId;
	}

	public void setTargetDeptId(String targetDeptId) {
	   this.targetDeptId = targetDeptId;
	}

	public String getReturnReason() {
	   return returnReason;
	}

	public void setReturnReason(String returnReason) {
	   this.returnReason = returnReason;
	}

	public String getOperatorId() {
	   return operatorId;
	}

	public void setOperatorId(String operatorId) {
	   this.operatorId = operatorId;
	}

	public String getOperatorName() {
	   return operatorName;
	}

	public void setOperatorName(String operatorName) {
	   this.operatorName = operatorName;
	}

	public String getRemoveStatue() {
	   return removeStatue;
	}

	public void setRemoveStatue(String removeStatue) {
	   this.removeStatue = removeStatue;
	}

	public String getOutStatue() {
	   return outStatue;
	}

	public void setOutStatue(String outStatue) {
	   this.outStatue = outStatue;
	}

	public String getUnNormalId() { return unNormalId;}

	public void setUnNormalId(String unNormalId) { this.unNormalId = unNormalId;}

	public String getOperationStatue() { return operationStatue;}

	public void setOperationStatue(
		String operationStatue) { this.operationStatue = operationStatue;}

	public String getPatientId() { return patientId;}

	public void setPatientId(String patientId) { this.patientId = patientId;}

	public String getPatientName() { return patientName;}

	public void setPatientName(String patientName) { this.patientName = patientName;}

	public String getMedicalId() { return medicalId;}

	public void setMedicalId(String medicalId) { this.medicalId = medicalId;}

	public String getSurgeryId() { return surgeryId;}

	public void setSurgeryId(String surgeryId) { this.surgeryId = surgeryId;}

	public String getDeptId() { return deptId;}

	public void setDeptId(String deptId) { this.deptId = deptId;}

	public String getThingId() { return thingId;}

	public void setThingId(String thingId) { this.thingId = thingId;}
   }
   //   	"unNormalId":"",
//		"operationStatue":"7",
//		"outStatue":""   出柜关联

   /**
    * "unNormalId":"2",
    "operationStatue":"3",
    "patientId":"0002",
    "patientName":"陶玉璞",
    "medicalId":"002",
    "surgeryId":"002",
    "deptId":"002",
    "thingId":"40288b3f6a47d0d2016a47e956180000"    绑定患者

    "unNormalId":"1",
    "operationStatue":"1",
    "removeStatue":"1"     连续移除需
    */


}
