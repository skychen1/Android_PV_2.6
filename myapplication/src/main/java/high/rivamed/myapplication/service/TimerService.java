package high.rivamed.myapplication.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.androidpn.utils.XmppEvent;

import high.rivamed.myapplication.http.NetApi;
import high.rivamed.myapplication.receiver.AlarmReceiver;
import high.rivamed.myapplication.utils.EventBusUtils;

import static high.rivamed.myapplication.base.App.MAIN_URL;
import static high.rivamed.myapplication.base.App.mTitleConn;

/**
 * 项目名称:    Android_PV_2.6.5New
 * 创建者:      DanMing
 * 创建时间:    2019/4/9 18:12
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.service
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class TimerService extends Service {

   @Override
   public IBinder onBind(Intent intent) {
	return null;
   }

   @Override
   public int onStartCommand(Intent intent, int flags, int startId) {
	String urls = MAIN_URL + NetApi.URL_CONNECT;
	OkGo.<String>get(urls).tag(this).execute(new StringCallback() {
	   @Override
	   public void onSuccess(Response<String> response) {
		String body = response.body();
		if (body.equals("true")) {
		   if (!mTitleConn){
			EventBusUtils.post(new XmppEvent.XmmppConnect(true));
		   }
		} else {
		   if (mTitleConn){
			EventBusUtils.post(new XmppEvent.XmmppConnect(false));
		   }
		}
	   }

	   @Override
	   public void onError(Response<String> response) {
		super.onError(response);
		if (mTitleConn) {
		   EventBusUtils.post(new XmppEvent.XmmppConnect(false));
		}
	   }
	});

	new Thread(new Runnable() {
	   @Override
	   public void run() {
	   }
	}).start();
	AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
	int anHour = 15 * 1000;  // 这是一小时的毫秒数
	long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
	Intent i = new Intent(this, AlarmReceiver.class);
	PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
	manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
	return super.onStartCommand(intent, flags, startId);
   }
}
