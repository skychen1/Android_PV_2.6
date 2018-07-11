package high.rivamed.myapplication.bean;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/15 15:50
 * 描述:        EVENTBUS的bean
 * 包名:        high.rivamed.myapplication.bean;
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class Event {
   public static class EventAct {
	public String mString;

	public EventAct(String trim) {
	   this.mString = trim;
	}
   }
   public static class EventCheckbox {
	public String mString;

	public EventCheckbox(String trim) {
	   this.mString = trim;
	}
   }
   public static class SettingEvent {

	public static final int SETTING_INFO      = 1;
	public static final int SETTING_LOGIN     = 2;
	public static final int SETTING_LOGIN_OUT = 3;
	/**
	 * setting点击的动作
	 **/
	public int RvAction;
	public String RvActionS;

	public SettingEvent(int rvAction,String k) {
	   RvAction = rvAction;
	   RvActionS = k;
	}
   }

   public static class PopupEvent {

	public boolean isMute;
	public String mString;

	public PopupEvent(boolean isMute,String trim) {
	   this.isMute = isMute;
	   this.mString = trim;
	}
   }
}
