package high.rivamed.myapplication.bean;

import android.app.Dialog;
import android.content.DialogInterface;

import high.rivamed.myapplication.dto.TCstInventoryDto;

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
	public int mPos;

	public PopupEvent(boolean isMute,String trim) {
	   this.isMute = isMute;
	   this.mString = trim;
	}
	public PopupEvent(boolean isMute,String trim,int pos) {
	   this.isMute = isMute;
	   this.mString = trim;
	   this.mPos = pos;
	}
   }

   public static class activationEvent{
	public boolean isActivation;
	public DialogInterface dialog;
      public activationEvent(boolean isActivation, DialogInterface dialog){
	   this.isActivation = isActivation;
	   this.dialog = dialog;
	}
   }
   public static class dialogEvent{
	public Dialog dialog;
	public String deptCode;
	public String storehouseCode;
	public String operationRoomNo;
	public String branchCode;
	public String deptName;
	public dialogEvent(String deptName, String branchCode,String deptCode, String storehouseCode, String operationRoomNo, Dialog dialog){

	   this.deptCode = deptCode;
	   this.deptName = deptName;
	   this.storehouseCode = storehouseCode;
	   this.dialog = dialog;
	   this.operationRoomNo = operationRoomNo;
	   this.operationRoomNo = branchCode;
	}
   }
   public static class outBoxEvent{
	public Dialog dialog;
	public String type;
	public String context;
	public outBoxEvent( String type,String text, Dialog dialog){

	   this.dialog = dialog;
	   this.type = type;
	   this.context = text;

	}
   }
   public static class timelyDate{
	public String type;
	public TCstInventoryDto tCstInventoryDto;
	public timelyDate( String type,TCstInventoryDto tCstInventoryDto){

	   this.type = type;
	   this.tCstInventoryDto = tCstInventoryDto;

	}
   }
}
