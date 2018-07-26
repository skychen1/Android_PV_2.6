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
    * thingCode : 23233
    * tbaseDevices : [{"baudrate":null,"com":"ip","deviceCode":"243242","deviceName":"柜子01","deviceType":9,"identification":1,"ip":"127.0.0.1","parent":"","remark":null,"stopFlag":0,"thingCode":"23233"}]
    */

   private int id;
   private String thingCode;
   private List<TbaseDevicesBean> tbaseDevices;

   public int getId() { return id;}

   public void setId(int id) { this.id = id;}

   public String getThingCode() { return thingCode;}

   public void setThingCode(String thingCode) { this.thingCode = thingCode;}

   public List<TbaseDevicesBean> getTbaseDevices() { return tbaseDevices;}

   public void setTbaseDevices(
	   List<TbaseDevicesBean> tbaseDevices) { this.tbaseDevices = tbaseDevices;}

   public static class TbaseDevicesBean {

	/**
	 * baudrate : null
	 * com : ip
	 * deviceCode : 243242
	 * deviceName : 柜子01
	 * deviceType : 9
	 * identification : 1
	 * ip : 127.0.0.1
	 * parent :
	 * remark : null
	 * stopFlag : 0
	 * thingCode : 23233
	 */

	private Object baudrate;
	private String com;
	private String deviceCode;
	private String deviceName;
	private int    deviceType;
	private int    identification;
	private String ip;
	private String parent;
	private Object remark;
	private int    stopFlag;
	private String thingCode;

	public Object getBaudrate() { return baudrate;}

	public void setBaudrate(Object baudrate) { this.baudrate = baudrate;}

	public String getCom() { return com;}

	public void setCom(String com) { this.com = com;}

	public String getDeviceCode() { return deviceCode;}

	public void setDeviceCode(String deviceCode) { this.deviceCode = deviceCode;}

	public String getDeviceName() { return deviceName;}

	public void setDeviceName(String deviceName) { this.deviceName = deviceName;}

	public int getDeviceType() { return deviceType;}

	public void setDeviceType(int deviceType) { this.deviceType = deviceType;}

	public int getIdentification() { return identification;}

	public void setIdentification(int identification) { this.identification = identification;}

	public String getIp() { return ip;}

	public void setIp(String ip) { this.ip = ip;}

	public String getParent() { return parent;}

	public void setParent(String parent) { this.parent = parent;}

	public Object getRemark() { return remark;}

	public void setRemark(Object remark) { this.remark = remark;}

	public int getStopFlag() { return stopFlag;}

	public void setStopFlag(int stopFlag) { this.stopFlag = stopFlag;}

	public String getThingCode() { return thingCode;}

	public void setThingCode(String thingCode) { this.thingCode = thingCode;}
   }
}
