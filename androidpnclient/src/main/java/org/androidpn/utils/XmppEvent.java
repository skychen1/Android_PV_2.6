package org.androidpn.utils;

/**
 * 项目名称:    Android_PV_2.6.1
 * 创建者:      DanMing
 * 创建时间:    2018/11/12 16:41
 * 描述:        eventbus传递的数据
 * 包名:        org.androidpn.utils
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class XmppEvent {
   public static class XmmppConnect {
	public boolean connect;

	public XmmppConnect(boolean connect) {
	   this.connect = connect;
	}
   }
   /**
    * 页面顶部的连接状态改变
    */
   public static class EventPushMessageNum {
	public String num;

	public EventPushMessageNum(String num) {
	   this.num = num;
	}
   }

}
