package high.rivamed.myapplication.bean;

import java.io.Serializable;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/31 13:16
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class DialogBean implements Serializable {
   String code;
   String name;

   public String getCode() {
	return code;
   }

   public void setCode(String code) {
	this.code = code;
   }

   public String getName() {
	return name;
   }

   public void setName(String name) {
	this.name = name;
   }
}
