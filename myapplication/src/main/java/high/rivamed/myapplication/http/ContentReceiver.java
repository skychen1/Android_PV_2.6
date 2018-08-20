package high.rivamed.myapplication.http;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import high.rivamed.myapplication.activity.LoginActivity;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      LiangDanMing
 * 创建时间:    2018/8/20 19:07
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.http
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class ContentReceiver extends BroadcastReceiver {
   @Override
   public void onReceive(Context context, Intent intent) {
	Intent it=new Intent(context,LoginActivity.class);
	it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	context.startActivity(it);
   }
}
