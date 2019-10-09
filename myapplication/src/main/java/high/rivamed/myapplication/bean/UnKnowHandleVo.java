package high.rivamed.myapplication.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称:    Android_PV_2.6.6_514C
 * 创建者:      DanMing
 * 创建时间:    2019/6/28 16:28
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class UnKnowHandleVo implements Serializable{

   private List<UnKnowHandleVosBean> unKnowHandleVos;

   public List<UnKnowHandleVosBean> getUnKnowHandleVos() { return unKnowHandleVos;}

   public void setUnKnowHandleVos(
	   List<UnKnowHandleVosBean> unKnowHandleVos) { this.unKnowHandleVos = unKnowHandleVos;}

   public static class UnKnowHandleVosBean {

	/**
	 * operatorId : ff80818169473ecf016951c38f7200f5
	 * operatorName : 初广秀
	 * unNormalId : 40288ba26b8de4ef016b8df1d49b000d
	 */

	private String operatorId;
	private String operatorName;
	private String unNormalId;
	private String thingId;

	public String getThingId() {
	   return thingId;
	}

	public void setThingId(String thingId) {
	   this.thingId = thingId;
	}

	public String getOperatorId() { return operatorId;}

	public void setOperatorId(String operatorId) { this.operatorId = operatorId;}

	public String getOperatorName() { return operatorName;}

	public void setOperatorName(String operatorName) { this.operatorName = operatorName;}

	public String getUnNormalId() { return unNormalId;}

	public void setUnNormalId(String unNormalId) { this.unNormalId = unNormalId;}
   }
}
