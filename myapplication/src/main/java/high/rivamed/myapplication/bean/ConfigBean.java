package high.rivamed.myapplication.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      LiangDanMing
 * 创建时间:    2018/8/8 17:25
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class ConfigBean implements Serializable{

   /**
    * id : 0
    * tCstConfigVos : [{"dictId":null,"name":"耗材近效期1","value":"30","code":"001"},{"dictId":null,"name":"耗材近效期2","value":"90","code":"002"},{"dictId":null,"name":"耗材近效期3","value":"120","code":"003"},{"dictId":null,"name":"未确认耗材时间设置（小时）","value":"2","code":"004"},{"dictId":null,"name":"是否启用患者来源于门诊","value":"1","code":"005"},{"dictId":null,"name":"是否启用患者来源于手术室","value":"1","code":"006"},{"dictId":null,"name":"是否启用绑定患者领用耗材","value":"1","code":"007"},{"dictId":null,"name":"是否允许耗材同入同出","value":"1","code":"008"},{"dictId":null,"name":"是否先开柜门后绑定患者","value":"1","code":"009"},{"dictId":null,"name":"是否先绑定患者后开柜门","value":"1","code":"010"}]
    * thingCode : ff808181650e151b01650ec14d070010
    */

   private int id;
   private String thingCode;
   private List<TCstConfigVosBean> tCstConfigVos;

   public int getId() { return id;}

   public void setId(int id) { this.id = id;}

   public String getThingCode() { return thingCode;}

   public void setThingCode(String thingCode) { this.thingCode = thingCode;}

   public List<TCstConfigVosBean> getTCstConfigVos() { return tCstConfigVos;}

   public void setTCstConfigVos(
	   List<TCstConfigVosBean> tCstConfigVos) { this.tCstConfigVos = tCstConfigVos;}

   public static class TCstConfigVosBean {

	/**
	 * dictId : null
	 * name : 耗材近效期1
	 * value : 30
	 * code : 001
	 */

	private Object dictId;
	private String name;
	private String value;
	private String code;

	public Object getDictId() { return dictId;}

	public void setDictId(Object dictId) { this.dictId = dictId;}

	public String getName() { return name;}

	public void setName(String name) { this.name = name;}

	public String getValue() { return value;}

	public void setValue(String value) { this.value = value;}

	public String getCode() { return code;}

	public void setCode(String code) { this.code = code;}
   }
}
