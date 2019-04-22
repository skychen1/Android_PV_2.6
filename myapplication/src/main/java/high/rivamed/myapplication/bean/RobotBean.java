package high.rivamed.myapplication.bean;

import java.io.Serializable;

/**
 * 项目名称:    Android_PV_2.6.5NewLast
 * 创建者:      DanMing
 * 创建时间:    2019/3/27 13:45
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class RobotBean implements Serializable{

   /**
    * operateSuccess : true
    * id : 0
    * msg : 操作成功
    * sthId : 4028829468596e0c0168597dcbd20000
    * reciveMessage : {"resultCode":0,"resultContent":"接收成功","robotId":"1","robotName":"A区机器人"}
    */

   private boolean operateSuccess;
   private int               id;
   private String            msg;
   private String            sthId;
   private ReciveMessageBean reciveMessage;

   public boolean isOperateSuccess() { return operateSuccess;}

   public void setOperateSuccess(boolean operateSuccess) { this.operateSuccess = operateSuccess;}

   public int getId() { return id;}

   public void setId(int id) { this.id = id;}

   public String getMsg() { return msg;}

   public void setMsg(String msg) { this.msg = msg;}

   public String getSthId() { return sthId;}

   public void setSthId(String sthId) { this.sthId = sthId;}

   public ReciveMessageBean getReciveMessage() { return reciveMessage;}

   public void setReciveMessage(
	   ReciveMessageBean reciveMessage) { this.reciveMessage = reciveMessage;}

   public static class ReciveMessageBean {

	/**
	 * resultCode : 0
	 * resultContent : 接收成功
	 * robotId : 1
	 * robotName : A区机器人
	 */

	private int resultCode;
	private String resultContent;
	private String robotId;
	private String robotName;

	public int getResultCode() { return resultCode;}

	public void setResultCode(int resultCode) { this.resultCode = resultCode;}

	public String getResultContent() { return resultContent;}

	public void setResultContent(String resultContent) { this.resultContent = resultContent;}

	public String getRobotId() { return robotId;}

	public void setRobotId(String robotId) { this.robotId = robotId;}

	public String getRobotName() { return robotName;}

	public void setRobotName(String robotName) { this.robotName = robotName;}
   }
}
