package high.rivamed.myapplication.http;

import android.util.Log;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

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
	   String deviceCode, String term, String startTime, String endTime, String status, Object tag,
	   NetResult netResult) {

	OkGo.<String>get(NetApi.URL_HOME_RUNWATE).tag(tag)
		.params("thingCode", thingCode)
		.params("startTime", startTime)
		.params("endTime", endTime)
		.params("status", status)
		.params("deviceCode", deviceCode)
		.params("term", term)
		.execute(new MyCallBack(tag,netResult, true));
   }
   /**
    * 获取柜子个数
    */
   public void loadBoxSize(
	   String thingCode, Object tag,
	   NetResult netResult) {

	OkGo.<String>get(NetApi.URL_HOME_BOXSIZE).tag(tag)
		.params("thingCode", thingCode)
		.execute(new MyCallBack(tag,netResult,true));
   }


   /**
    * 耗材效期监控
    */
   public void materialControl(String thingCode, Object tag,
				   NetResult netResult) {
	OkGo.<String>get(NetApi.URL_STOCKSTATUS_TOP).tag(tag)
		.params("thingCode", thingCode)
		.execute(new MyCallBack(tag, netResult,true));
   }

   /**
    * 库存详情和耗材库存预警
    */
   public void getStockDown(String thingCode, String nameOrSpecQueryCon, String deviceCode,int mStopFlag,Object tag, NetResult netResult) {
	OkGo.<String>get(NetApi.URL_STOCKSTATUS_DETAILS).tag(tag)
		.params("thingCode", thingCode)
		.params("nameOrSpecQueryCon", nameOrSpecQueryCon)
		.params("deviceCode", deviceCode)
		.params("StopFlag", mStopFlag)
		.execute(new MyCallBack(tag, netResult, true));
   }

   /**
    * 未确认耗材
    */
   public void getRightUnconfDate(String thingCode,String deviceCode,String mTrim, Object tag, NetResult netResult) {
	OkGo.<String>get(NetApi.URL_STOCKUNCON_RIGHT).tag(tag)
		.params("thingCode", thingCode)
		.params("deviceCode", deviceCode)
		.params("nameOrSpecQueryCon", mTrim)
		.execute(new MyCallBack(tag,netResult, true));
   }
   /**
    * 查询单个耗材
    */
   public void getStockDetailDate(String deviceCode,String cstCode, Object tag, NetResult netResult) {
	OkGo.<String>get(NetApi.URL_STOCK_DETAIL).tag(tag)
		.params("cstCode", cstCode)
		.params("deviceCode", deviceCode)
		.execute(new MyCallBack(tag,netResult, true));
   }

   /**
    * 数据恢复
    */
   public void getRecoverDate(String sn, Object tag, NetResult netResult) {
	OkGo.<String>get(NetApi.URL_TEST_SNQUERY).tag(tag)
		.params("sn", sn)
		.execute(new MyCallBack(tag,netResult, true));
   }



   private class MyCallBack extends StringCallback {



	private Object tag;
	private NetResult netResult;
	private boolean isGet;//是否是get请求

	public MyCallBack( Object tag, NetResult netResult,
		boolean isGet) {
	   super();

	   this.tag = tag;
	   this.netResult = netResult;
	   this.isGet = isGet;
	}

	@Override
	public void onError(Response<String> response) {
	   if (netResult != null) {
		LogUtils.i(TAG, "网络接口联网失败");
		netResult.onError("  body:  "+response.body()+"  code:  "+response.code()+"  message:  "+response.message());
	   }
	}

	@Override
	public void onSuccess(Response<String> response) {
	   if (netResult!=null){
		netResult.onSucceed(response.body());
	   }
	   Log.i("fff", "response.body()    " + response.body());
	   Log.i("fff","response.code()    "+response.code());
	   Log.i("fff","response.message()    "+response.message());
	}
   }
}
