package high.rivamed.myapplication.bean;

import android.app.Dialog;
import android.content.DialogInterface;

import java.util.List;
import java.util.Map;

import cn.rivamed.model.TagInfo;
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

   /**
    * 关门 后给按钮设置为可以点击，InoutBoxTwoActivity
    */
   public static class EventGoneBtn {
	public String mGoneBtn;

	public EventGoneBtn(String mGoneBtn) {
	   this.mGoneBtn = mGoneBtn;
	}
   }
   /**
    * Frag的跳转
    */
   public static class EventFrag {
	public String type;

	public EventFrag(String type) {
	   this.type = type;
	}
   }
   public static class EventAct {
	public String mString = "";

	public EventAct(String trim) {
	   this.mString = trim;
	}
   }

   public static class EventClickBack {
	public String mString;

	public EventClickBack(String trim) {
	   this.mString = trim;
	}
   }
   public static class EventCheckbox {
	public String mString;
	public String id;
	public String type;
	public String operationScheduleId;
	public int position;
	public List<BoxSizeBean.TbaseDevicesBean> mTbaseDevices;

	public EventCheckbox(String trim) {
	   this.mString = trim;
	}
	public EventCheckbox(String trim,String id) {
	   this.mString = trim;
	   this.id = id;
	}
	public EventCheckbox(String trim,String id,String type) {
	   this.mString = trim;
	   this.id = id;
	   this.type = type;
	}
	public EventCheckbox(String name,String id,String type,int position,List<BoxSizeBean.TbaseDevicesBean> mTbaseDevices) {
	   this.mString = name;
	   this.id = id;
	   this.type = type;
	   this.position = position;
	   this.mTbaseDevices = mTbaseDevices;
	}
	public EventCheckbox(String name,String id,String operationScheduleId,String type,int position,List<BoxSizeBean.TbaseDevicesBean> mTbaseDevices) {
	   this.mString = name;
	   this.id = id;
	   this.type = type;
	   this.position = position;
	   this.mTbaseDevices = mTbaseDevices;
	   this.operationScheduleId = operationScheduleId;
	}
   }

   /**
    * 禁止首页的左侧菜单栏
    */
   public static class HomeNoClickEvent {
	public boolean isClick;

	public HomeNoClickEvent(boolean isClick) {
	   this.isClick = isClick;
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
	public boolean         isActivation;
	public DialogInterface dialog;
      public activationEvent(boolean isActivation, DialogInterface dialog){
	   this.isActivation = isActivation;
	   this.dialog = dialog;
	}
   }
   public static class dialogEvent{
	public Dialog dialog;
	public String deptId;
	public String storehouseCode;
	public String operationRoomNo;
	public String branchCode;
	public String deptName;
	public dialogEvent(String deptName, String branchCode,String deptId, String storehouseCode, String operationRoomNo, Dialog dialog){

	   this.deptId = deptId;
	   this.deptName = deptName;
	   this.storehouseCode = storehouseCode;
	   this.dialog = dialog;
	   this.operationRoomNo = operationRoomNo;
	   this.branchCode = branchCode;
	}
   }
   public static class outBoxEvent{
	public Dialog dialog;
	public String type;
	public String context;
	public int mIntentType;
	public outBoxEvent( String type,String text, Dialog dialog,int mIntentType){

	   this.dialog = dialog;
	   this.type = type;
	   this.context = text;
	   this.mIntentType = mIntentType;

	}
   }
   public static class timelyDate{
	public String           type;
	public TCstInventoryDto tCstInventoryDto;
	public timelyDate( String type,TCstInventoryDto tCstInventoryDto){

	   this.type = type;
	   this.tCstInventoryDto = tCstInventoryDto;

        }
    }
    public static class tempPatientEvent {
        public Dialog dialog;
        public String userName="";
        public String userSex="";
        public String idCard="";
        public String time="";
        public String roomNum="";

        public tempPatientEvent(String userName, String roomNum, String userSex, String idCard, String time, Dialog dialog) {


            this.userName = userName;
            this.roomNum = roomNum;
            this.userSex = userSex;
            this.dialog = dialog;
            this.idCard = idCard;
            this.time = time;
        }
    }

    public static class EventString {
        public String mString;

	public EventString(String text) {
	   this.mString = text;
	}
   }

   public static class EventTimelyCode {
	public String mCode;

	public EventTimelyCode(String mCode) {
	   this.mCode = mCode;
	}
   }

   public static class EventBoolean {
	public boolean mBoolean;
	public String mId;

	public EventBoolean(boolean booleans,String id) {
	   this.mBoolean = booleans;
	   this.mId = id;
	}
   }
   public static class EventToast {
	public String mString;

	public EventToast(String text) {
	   this.mString = text;
	}
   }


   public static class EventRg {
	public String mString;

	public EventRg(String text) {
	   this.mString = text;
	}
   }

   /**
    * 发送是否开门
    */
   public static class EventOppenDoor {
	public String mString;

	public EventOppenDoor(String text) {
	   this.mString = text;
	}
   }

   /**
    * 硬件
    */
   public static class EventDeviceCallBack {
	public String deviceId;
	public Map<String, List<TagInfo>> epcs;

	public EventDeviceCallBack(String deviceId, Map<String, List<TagInfo>> epcs) {
	   this.deviceId = deviceId;
	   this.epcs = epcs;
	}
   }
   /**
    * 刷新按钮
    */
   public static class EventButton {
	public boolean type;

	public EventButton(boolean type) {
	   this.type = type;
	}
   }
}
