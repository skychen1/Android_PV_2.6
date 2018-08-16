package high.rivamed.myapplication.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/23 18:53
 * 描述:        sn恢复的数据
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class SnRecoverBean implements Serializable{

   /**
    * id : 0
    * tBaseDeviceVos : [{"deviceName":null,"deviceCode":null,"deviceType":0,"ip":null,"parent":null,"tBaseDevice":{"baudrate":null,"com":"ip","deviceCode":"11111","deviceName":"1号柜","deviceType":9,"identification":1,"ip":"127.0.0.1","parent":null,"remark":null,"stopFlag":0,"thingCode":"23233"},"tBaseDevices":[]},{"deviceName":null,"deviceCode":null,"deviceType":0,"ip":null,"parent":null,"tBaseDevice":{"baudrate":null,"com":"ip","deviceCode":"22222","deviceName":"2号柜","deviceType":9,"identification":1,"ip":"127.0.0.1","parent":null,"remark":null,"stopFlag":0,"thingCode":"23233"},"tBaseDevices":[]},{"deviceName":null,"deviceCode":null,"deviceType":0,"ip":null,"parent":null,"tBaseDevice":{"baudrate":null,"com":null,"deviceCode":"33333","deviceName":"3号柜","deviceType":9,"identification":1,"ip":null,"parent":null,"remark":null,"stopFlag":0,"thingCode":"23233"},"tBaseDevices":[]},{"deviceName":null,"deviceCode":null,"deviceType":0,"ip":null,"parent":null,"tBaseDevice":{"baudrate":null,"com":null,"deviceCode":"44444","deviceName":"4号柜","deviceType":9,"identification":1,"ip":null,"parent":null,"remark":null,"stopFlag":0,"thingCode":"23233"},"tBaseDevices":[]},{"deviceName":null,"deviceCode":null,"deviceType":0,"ip":null,"parent":null,"tBaseDevice":{"baudrate":null,"com":null,"deviceCode":"55555","deviceName":"5号柜","deviceType":9,"identification":1,"ip":null,"parent":null,"remark":null,"stopFlag":0,"thingCode":"23233"},"tBaseDevices":[]},{"deviceName":null,"deviceCode":null,"deviceType":0,"ip":null,"parent":null,"tBaseDevice":{"baudrate":null,"com":null,"deviceCode":"66666","deviceName":"6号柜","deviceType":9,"identification":1,"ip":null,"parent":null,"remark":null,"stopFlag":0,"thingCode":"23233"},"tBaseDevices":[]}]
    * sn : 124332431321
    */

   private int id;
   private String                   sn;
   private List<TBaseDeviceVosBean> tBaseDeviceVos;

   public int getId() { return id;}

   public void setId(int id) { this.id = id;}

   public String getSn() { return sn;}

   public void setSn(String sn) { this.sn = sn;}

   public List<TBaseDeviceVosBean> getTBaseDeviceVos() { return tBaseDeviceVos;}

   public void setTBaseDeviceVos(
	   List<TBaseDeviceVosBean> tBaseDeviceVos) { this.tBaseDeviceVos = tBaseDeviceVos;}

   public static class TBaseDeviceVosBean {

	/**
	 * deviceName : null
	 * deviceCode : null
	 * deviceType : 0
	 * ip : null
	 * parent : null
	 * tBaseDevice : {"baudrate":null,"com":"ip","deviceCode":"11111","deviceName":"1号柜","deviceType":9,"identification":1,"ip":"127.0.0.1","parent":null,"remark":null,"stopFlag":0,"thingCode":"23233"}
	 * tBaseDevices : []
	 */

	private String deviceName;
	private String          deviceCode;
	private int             deviceType;
	private String          ip;
	private String          parent;
	private TBaseDeviceBean tBaseDevice;
	private List<?>         tBaseDevices;

	public String getDeviceName() { return deviceName;}

	public void setDeviceName(String deviceName) { this.deviceName = deviceName;}

	public String getDeviceCode() { return deviceCode;}

	public void setDeviceCode(String deviceCode) { this.deviceCode = deviceCode;}

	public int getDeviceType() { return deviceType;}

	public void setDeviceType(int deviceType) { this.deviceType = deviceType;}

	public String getIp() { return ip;}

	public void setIp(String ip) { this.ip = ip;}

	public String getParent() { return parent;}

	public void setParent(String parent) { this.parent = parent;}

	public TBaseDeviceBean getTBaseDevice() { return tBaseDevice;}

	public void setTBaseDevice(TBaseDeviceBean tBaseDevice) { this.tBaseDevice = tBaseDevice;}

	public List<?> gettBaseDevices() { return tBaseDevices;}

	public void settBaseDevices(List<?> tBaseDevices) { this.tBaseDevices = tBaseDevices;}

	public static class TBaseDeviceBean {

	   /**
	    * baudrate : null
	    * com : ip
	    * deviceCode : 11111
	    * deviceName : 1号柜
	    * deviceType : 9
	    * identification : 1
	    * ip : 127.0.0.1
	    * parent : null
	    * remark : null
	    * stopFlag : 0
	    * thingCode : 23233
	    */

	   private String baudrate;
	   private String com;
	   private String deviceCode;
	   private String deviceName;
	   private int    deviceType;
	   private int    identification;
	   private String ip;
	   private String parent;
	   private String remark;
	   private int    stopFlag;
	   private String thingCode;

	   public String getBaudrate() { return baudrate;}

	   public void setBaudrate(String baudrate) { this.baudrate = baudrate;}

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

	   public String getRemark() { return remark;}

	   public void setRemark(String remark) { this.remark = remark;}

	   public int getStopFlag() { return stopFlag;}

	   public void setStopFlag(int stopFlag) { this.stopFlag = stopFlag;}

	   public String getThingCode() { return thingCode;}

	   public void setThingCode(String thingCode) { this.thingCode = thingCode;}
	}
   }
}
