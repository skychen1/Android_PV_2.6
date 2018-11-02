package high.rivamed.myapplication.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;

import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

/**
 * 项目名称:    Android_PV_2.6.1
 * 创建者:      DanMing
 * 创建时间:    2018/10/8 16:03
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication
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
	NetRequest.getInstance().connectTitle(this, new BaseResult(){
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i("result","result   "+result.equals("true")  );
		if (result.equals("true")){
		   EventBusUtils.post(new Event.EventTitleConn(true));
		}else {
		   EventBusUtils.post(new Event.EventTitleConn(false));
		}
	   }

	   @Override
	   public void onNetFailing(String result) {
		EventBusUtils.post(new Event.EventTitleConn(false));
	   }

	   @Override
	   public void onError(String result) {
		EventBusUtils.post(new Event.EventTitleConn(false));
	   }
	});

	AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
	int anHour = 2 * 1000;  // 这是一小时的毫秒数
	long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
	Intent i = new Intent(this, AlarmReceiver.class);
	PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, FLAG_UPDATE_CURRENT);
	manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
	return super.onStartCommand(intent, flags, startId);
   }
}
