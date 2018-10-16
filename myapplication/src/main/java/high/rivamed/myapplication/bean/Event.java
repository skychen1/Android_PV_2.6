package high.rivamed.myapplication.bean;

import android.app.Dialog;
import android.content.DialogInterface;

import java.util.ArrayList;
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
    * 页面顶部的连接状态改变
    */
   public static class EventDoorList {
	public ArrayList<String> doorList;
	public EventDoorList(ArrayList<String> doorList) {
	   this.doorList = doorList;
	}
   }
   /**
    * 快速开柜的数据
    */
   public static class EventOutDto {
	public TCstInventoryDto cstInventoryDto;
	public String json;

	public EventOutDto(TCstInventoryDto cstInventoryDto) {
	   this.cstInventoryDto = cstInventoryDto;
	}
	public EventOutDto(TCstInventoryDto cstInventoryDto,String json) {
	   this.cstInventoryDto = cstInventoryDto;
	   this.json = json;
	}
   }
   /**
    * 页面顶部的连接状态改变
    */
   public static class EventTitleConn {
	public boolean b;

	public EventTitleConn(boolean b) {
	   this.b = b;
	}
   }
   /**
    * 快速开柜数据的替换
    */
   public static class EventDate {
	public boolean b;
	public boolean moreScan;

	public EventDate(boolean b) {
	   this.b = b;
	}
	public EventDate(boolean b,boolean moreScan) {
	   this.b = b;
	   this.moreScan = moreScan;
	}
   }
   /**
    * 隐藏绑定患者的界面按钮
    */
   public static class EventButGone {
	public boolean b;

	public EventButGone(boolean b) {
	   this.b = b;
	}
   }
   /**
    * 快速开柜入柜提示
    */
   public static class EventOutTitleV {
	public boolean b;

	public EventOutTitleV(boolean b) {
	   this.b = b;
	}
   }
   /**
    * 耗材倒计时结束发起
    */
   public static class EventOverPut {
	public boolean b;

	public EventOverPut(boolean b) {
	   this.b = b;
	}
   }
   /**
    * Loss原因
    */
   public static class EventLoss {
	public String string;

	public EventLoss(String string) {
	   this.string = string;
	}
   }

   public static class EventJump {
	public int jump;

	public EventJump(int jump) {
	   this.jump = jump;
	}
   }
   public static class EventLoading {
	public boolean loading;

	public EventLoading(boolean loading) {
	   this.loading = loading;
	}
   }
   /**
    * 触摸
    */
   public static class EventTouch {
	public boolean touch;

	public EventTouch(boolean touch) {
	   this.touch = touch;
	}
   }
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
	public CreatTempPatientBean creatTempPatientBean;
	public String id;
	public String deptId;
	public String idNo;
	public String scheduleDateTime;
	public String operatingRoomNo;
	public String sex;
	public String operatingRoomNoName;
	public String type;
	public String operationScheduleId;
	public int position;
	public List<BoxSizeBean.TbaseDevicesBean> mTbaseDevices;

	public EventCheckbox(String name,String mId,String idNo,String scheduleDateTime,String operatingRoomNo,String operatingRoomNoName,String sex,String deptId,String type,int position,List<BoxSizeBean.TbaseDevicesBean> mTbaseDevices) {
	   this.deptId = deptId;
	   this.id = mId;
	   this.mString = name;
	   this.idNo = idNo;
	   this.scheduleDateTime = scheduleDateTime;
	   this.operatingRoomNo = operatingRoomNo;
	   this.operatingRoomNoName = operatingRoomNoName;
	   this.sex = sex;
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
	public String door;

	public HomeNoClickEvent(boolean isClick,String door) {
	   this.isClick = isClick;
	   this.door = door;
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
        public String roomNum="";//手术间名字
        public String operatingRoomNo="";//手术间编号

        public tempPatientEvent(String userName, String roomNum,String roomId, String userSex, String idCard, String time, Dialog dialog) {


            this.userName = userName;
            this.roomNum = roomNum;
            this.operatingRoomNo = roomId;
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
	public boolean bing;

	public EventButton(boolean type,boolean bing) {
	   this.type = type;
	   this.bing = bing;
	}
   }
}
