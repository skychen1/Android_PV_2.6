package high.rivamed.myapplication.http;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.util.HashMap;

import high.rivamed.myapplication.utils.LogUtils;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/4 16:51
 * 描述:        网络接口集合类
 * 包名:        high.rivamed.myapplication.http
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class NetRequest {

   private static final String TAG = "NetRequest";

   // 设置本类为单例模式
   private static NetRequest instance;
   private static Gson       mGson;

   public static NetRequest getInstance() {
	if (instance == null) {
	   synchronized (NetRequest.class) {
		if (instance == null) {
		   instance = new NetRequest();
		   mGson = new Gson();
		}
	   }
	}
	return instance;
   }

   /**
    * 获取耗材流水
    */
   public void loadRunWate(String thingCode,
	   String deviceCode, String term, String startTime, String endTime, Integer status, Object tag,
	   NetResult netResult) {
	HashMap<String, String> map = new HashMap<>();
	map.put("deviceCode", deviceCode);
	map.put("term", term);
	HttpParams params = new HttpParams();
	params.put(map);

	OkGo.<String>get(NetApi.URL_HOME_RUNWATE).tag(tag)
		.params("thingCode", thingCode)
		.params("startTime", startTime)
		.params("endTime", endTime)
		.params("status", status)
		.params("deviceCode", deviceCode)
		.params("term", term)
		.execute(new MyCallBack(tag, netResult, true));
   }
   /**
    * 获取柜子个数
    */
   public void loadBoxSize(
	   String thingCode, Object tag,
	   NetResult netResult) {

	OkGo.<String>get(NetApi.URL_HOME_BOXSIZE).tag(tag)
		.params("thingCode", thingCode)
		.execute(new MyCallBack(tag, netResult, true));
   }

   private class MyCallBack extends StringCallback {


	private File file;
	private Object tag;
	private NetResult netResult;
	private boolean isGet;//是否是get请求

	public MyCallBack( Object tag, NetResult netResult,
		boolean isGet) {
	   super();
	   this.file = file;
	   this.tag = tag;
	   this.netResult = netResult;
	   this.isGet = isGet;
	}

	@Override
	public void onError(Response<String> response) {
	   if (netResult != null) {
		LogUtils.i(TAG, "网络接口联网失败");
		netResult.onNetFailing(null);
	   }
	}

	@Override
	public void onSuccess(Response<String> response) {

	}
   }
}
