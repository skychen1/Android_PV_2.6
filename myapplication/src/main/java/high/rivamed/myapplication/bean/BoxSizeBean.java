package high.rivamed.myapplication.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/22 15:57
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class BoxSizeBean implements Serializable {

   /**
    * id : 0
    * thingId : 40288297677830f50167783478140000
    * devices : [{"deviceId":"40288297677830f50167783478510001","deviceName":"1柜","deviceType":"10","identification":null,"ip":null,"parent":"-1","remark":null,"status":0,"thingId":"40288297677830f50167783478140000","operationTime":"2018-12-04","dictId":null},{"deviceId":"40288297677830f50167783478ca0004","deviceName":"2柜","deviceType":"10","identification":null,"ip":null,"parent":"-1","remark":null,"status":0,"thingId":"40288297677830f50167783478140000","operationTime":"2018-12-04","dictId":null}]
    */

   private int id;
   private String            thingId;
   private List<DevicesBean> devices;

   public int getId() { return id;}

   public void setId(int id) { this.id = id;}

   public String getThingId() { return thingId;}

   public void setThingId(String thingId) { this.thingId = thingId;}

   public List<DevicesBean> getDevices() { return devices;}

   public void setDevices(List<DevicesBean> devices) { this.devices = devices;}

   public static class DevicesBean implements Serializable{

	/**
	 * deviceId : 40288297677830f50167783478510001
	 * deviceName : 1柜
	 * deviceType : 10
	 * identification : null
	 * ip : null
	 * parent : -1
	 * remark : null
	 * status : 0
	 * thingId : 40288297677830f50167783478140000
	 * operationTime : 2018-12-04
	 * dictId : null
	 */

	private String deviceId;
	private String deviceName;
	private String deviceType;
	private Object identification;
	private Object ip;
	private String parent;
	private Object remark;
	private int    status;
	private String thingId;
	private String operationTime;
	private Object dictId;

	public String getDeviceId() { return deviceId;}

	public void setDeviceId(String deviceId) { this.deviceId = deviceId;}

	public String getDeviceName() { return deviceName;}

	public void setDeviceName(String deviceName) { this.deviceName = deviceName;}

	public String getDeviceType() { return deviceType;}

	public void setDeviceType(String deviceType) { this.deviceType = deviceType;}

	public Object getIdentification() { return identification;}

	public void setIdentification(Object identification) { this.identification = identification;}

	public Object getIp() { return ip;}

	public void setIp(Object ip) { this.ip = ip;}

	public String getParent() { return parent;}

	public void setParent(String parent) { this.parent = parent;}

	public Object getRemark() { return remark;}

	public void setRemark(Object remark) { this.remark = remark;}

	public int getStatus() { return status;}

	public void setStatus(int status) { this.status = status;}

	public String getThingId() { return thingId;}

	public void setThingId(String thingId) { this.thingId = thingId;}

	public String getOperationTime() { return operationTime;}

	public void setOperationTime(String operationTime) { this.operationTime = operationTime;}

	public Object getDictId() { return dictId;}

	public void setDictId(Object dictId) { this.dictId = dictId;}
   }
}
