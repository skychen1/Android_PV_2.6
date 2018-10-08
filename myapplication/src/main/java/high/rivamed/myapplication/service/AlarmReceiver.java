package high.rivamed.myapplication.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 项目名称:    Android_PV_2.6.1
 * 创建者:      DanMing
 * 创建时间:    2018/10/8 16:09
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.service
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class AlarmReceiver extends BroadcastReceiver {
   @Override
   public void onReceive(Context context, Intent intent) {
	Intent i = new Intent(context,TimerService.class);
	context.startService(i);
   }
}

