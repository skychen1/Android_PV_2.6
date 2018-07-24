package high.rivamed.myapplication.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/20 16:55
 * 描述:        预注册和激活的封装对象
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class TBaseThingDto implements Serializable {

   private TBaseThing         tbasething;
   private List<TBaseThingVo> tBaseThingVos;

   public TBaseThing getTbasething() {
	return tbasething;
   }

   public void setTbasething(TBaseThing tbasething) {
	this.tbasething = tbasething;
   }

   public List<TBaseThingVo> gettBaseThingVos() {
	return tBaseThingVos;
   }

   public void settBaseThingVos(
	   List<TBaseThingVo> tBaseThingVos) {
	this.tBaseThingVos = tBaseThingVos;
   }

   public static class TBaseThing {//设备信息
	private String thingName;      //设备名称
	private String thingType;      //设备型号
	private String sn;            //设备序列号
	private String localIp;       //设备IP
	private String serverIp;      //服务器IP

	public String getThingName() {
	   return thingName;
	}

	public void setThingName(String thingName) {
	   this.thingName = thingName;
	}

	public String getThingType() {
	   return thingType;
	}

	public void setThingType(String thingType) {
	   this.thingType = thingType;
	}

	public String getSn() {
	   return sn;
	}

	public void setSn(String sn) {
	   this.sn = sn;
	}

	public String getLocalIp() {
	   return localIp;
	}

	public void setLocalIp(String localIp) {
	   this.localIp = localIp;
	}

	public String getServerIp() {
	   return serverIp;
	}

	public void setServerIp(String serverIp) {
	   this.serverIp = serverIp;
	}
   }

   public static class TBaseThingVo {//柜体信息
	private String            deviceName;     //柜子名称
	private String            deviceCode;      //柜子编码
	private List<TBaseDevice> taBaseDevices;//柜子内部设备

	public String getDeviceName() {
	   return deviceName;
	}

	public void setDeviceName(String deviceName) {
	   this.deviceName = deviceName;
	}

	public String getDeviceCode() {
	   return deviceCode;
	}

	public void setDeviceCode(String deviceCode) {
	   this.deviceCode = deviceCode;
	}

	public List<TBaseDevice> getTaBaseDevices() {
	   return taBaseDevices;
	}

	public void setTaBaseDevices(
		List<TBaseDevice> taBaseDevices) {
	   this.taBaseDevices = taBaseDevices;
	}

	public static class TBaseDevice {//柜子内部设备信息
	   private String deviceName;      //设备名称
	   private String deviceCode;      //设备编码
	   private String ip;      //设备

	   public String getDeviceName() {
		return deviceName;
	   }

	   public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	   }

	   public String getDeviceCode() {
		return deviceCode;
	   }

	   public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	   }

	   public String getIp() {
		return ip;
	   }

	   public void setIp(String ip) {
		this.ip = ip;
	   }
	}
   }
}
