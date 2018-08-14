package high.rivamed.myapplication.http;

import android.util.Log;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.List;

import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;
import high.rivamed.myapplication.views.LoadingDialog;

import static high.rivamed.myapplication.cont.Constants.THING_CODE;

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
   private static String sThingCode;

   public static NetRequest getInstance() {
	sThingCode = SPUtils.getString(UIUtils.getContext(), THING_CODE);
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
    * 预注册和激活的时候获取部件名称和ID
    */
   public void getDeviceInfosDate(String url,List<String> deviceTypes, Object tag, NetResult netResult) {

	OkGo.<String>get(url+NetApi.URL_TEST_FINDDEVICE).tag(tag)
		.addUrlParams("deviceTypes", deviceTypes)
		.execute(new MyCallBack(tag,netResult,true));
   }

   /**
    * 预注册
    */
   public void setSaveRegisteDate(String TBaseThing, Object tag, NetResult netResult) {

	OkGo.<String>post(NetApi.URL_TEST_REGISTE).tag(tag)
		.upJson(TBaseThing)
		.execute(new MyCallBack(tag,netResult,true));
   }

   /**
    * 用户登录
    */
   public void userLogin(String account, Object tag, NetResult netResult) {
	OkGo.<String>post(NetApi.URL_USER_LOGIN).tag(tag)
		.upJson(account)
		.execute(new NetRequest.MyCallBack(tag, netResult, false));
   }
   /**
    * 获取用户信息
    */
   public void findAppAccountInfo(String json, Object tag, NetResult netResult) {
	OkGo.<String>post(NetApi.URL_USER_GET_INFO).tag(tag)
		.upJson(json)
		.execute(new NetRequest.MyCallBack(tag, netResult, false));
   }
   /**
    * 绑定指纹
    */
   public void registerFinger(String json, Object tag, NetResult netResult) {
	OkGo.<String>post(NetApi.URL_USER_REGISTER_FINGER).tag(tag)
		.upJson(json)
		.execute(new NetRequest.MyCallBack(tag, netResult, false));
   }
   /**
    * 绑定腕带
    */
   public void registerIdCard(String json, Object tag, NetResult netResult) {
	OkGo.<String>post(NetApi.URL_USER_REGISTERWAIDAI).tag(tag)
		.upJson(json)
		.execute(new NetRequest.MyCallBack(tag, netResult, false));
   }
   /**
    * 指纹登录
    */
   public void validateLoginFinger(String json, Object tag, NetResult netResult) {
	OkGo.<String>post(NetApi.URL_USER_VALIDATELOGIN_FINGER).tag(tag)
		.upJson(json)
		.execute(new NetRequest.MyCallBack(tag, netResult, false));
   }
   /**
    * IdCard登录
    */
   public void validateLoginIdCard(String json, Object tag, NetResult netResult) {
	OkGo.<String>post(NetApi.URL_USER_VALIDATELOGINWRIST).tag(tag)
		.upJson(json)
		.execute(new NetRequest.MyCallBack(tag, netResult, false));
   }

   /**
    * 重置密码
    */
   public void resetPassword(String json, Object tag, NetResult netResult) {
	OkGo.<String>post(NetApi.URL_USER_RESET_PASSWORD).tag(tag)
		.upJson(json)
		.execute(new NetRequest.MyCallBack(tag, netResult, false));
   }

   /**
    * 获取耗材流水
    */
   public void loadRunWate(String page,String size,
	   String deviceCode, String term, String startTime, String endTime, String status, Object tag,
	   NetResult netResult) {
	LogUtils.i(TAG,"startTime  "+startTime+"     endTime   "+endTime);
	OkGo.<String>get(NetApi.URL_HOME_RUNWATE).tag(tag)
		.params("thingCode", sThingCode)
		.params("pageNo", page)
		.params("pageSize", size)
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
   public void loadBoxSize(Object tag,LoadingDialog.Builder dialog,
				   NetResult netResult) {
	OkGo.<String>get(NetApi.URL_HOME_BOXSIZE).tag(tag)
		.params("thingCode", sThingCode)
		.execute(new MyCallBack(tag,dialog,netResult,true));
   }


   /**
    * 耗材效期监控
    */
   public void materialControl( Object tag,
					  NetResult netResult) {
	OkGo.<String>get(NetApi.URL_STOCKSTATUS_TOP).tag(tag)
		.params("thingCode", sThingCode)
		.execute(new MyCallBack(tag, netResult,true));
   }

   /**
    * 库存详情和耗材库存预警
    */
   public void getStockDown( String nameOrSpecQueryCon, String deviceCode,int mStopFlag,Object tag, NetResult netResult) {
	Log.i("OkGo","nameOrSpecQueryCon   "+nameOrSpecQueryCon);
	Log.i("OkGo","deviceCode   "+deviceCode);
	Log.i("OkGo","mStopFlag   "+mStopFlag);
	Log.i("OkGo","sThingCode   "+sThingCode);
      OkGo.<String>get(NetApi.URL_STOCKSTATUS_DETAILS).tag(tag)
		.params("thingCode", sThingCode)
		.params("nameOrSpecQueryCon", nameOrSpecQueryCon)
		.params("deviceCode", deviceCode)
		.params("StopFlag", mStopFlag)
		.execute(new MyCallBack(tag, netResult, true));
   }

   /**
    * 未确认耗材
    */
   public void getRightUnconfDate(String deviceCode,String mTrim, Object tag, NetResult netResult) {
	OkGo.<String>get(NetApi.URL_STOCKUNCON_RIGHT).tag(tag)
		.params("thingCode", sThingCode)
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
		.params("thingCode", sThingCode)
		.execute(new MyCallBack(tag,netResult, true));
   }

   /**
    * 数据恢复
    */
   public void getRecoverDate(String sn, Object tag, NetResult netResult) {
      LogUtils.i("fff","sn  "+sn  +"   url   "+NetApi.URL_TEST_SNQUERY);
	OkGo.<String>get(NetApi.URL_TEST_SNQUERY).tag(tag)
		.params("sn", sn)
		.execute(new MyCallBack(tag,netResult, true));
   }
   /**
    * 输入医院查询医院信息
    */
   public void getHospNameDate(String hospName, Object tag, NetResult netResult) {
	OkGo.<String>get(NetApi.URL_TEST_FIND_HOSPHOME).tag(tag)
		.params("hospName", hospName)
		.execute(new MyCallBack(tag,netResult, true));
   }
   /**
    * 根据医院id查询院区信息
    */
   public void getHospBranch(String hospIds, Object tag, NetResult netResult) {
	OkGo.<String>get(NetApi.URL_TEST_FIND_BRANCH).tag(tag)
		.params("hospIds", hospIds)
		.execute(new MyCallBack(tag,netResult, true));
   }
   /**
    * 根据院区编码查询科室信息
    */
   public void getHospDept(String branchCode, Object tag, NetResult netResult) {
	OkGo.<String>get(NetApi.URL_TEST_FIND_DEPT).tag(tag)
		.params("branchCode", branchCode)
		.execute(new MyCallBack(tag,netResult, true));
   }
   /**
    * 根据科室查询库房情况
    */
   public void getHospBydept(String deptCode, Object tag, NetResult netResult) {
	OkGo.<String>get(NetApi.URL_TEST_FIND_BYDEPT).tag(tag)
		.params("deptCode", deptCode)
		.execute(new MyCallBack(tag,netResult, true));
   }
   /**
    * 根据科室查询手术室信息
    */
   public void getHospRooms(String deptCode, Object tag, NetResult netResult) {
	OkGo.<String>get(NetApi.URL_TEST_FIND_OPERROOMS).tag(tag)
		.params("deptCode", deptCode)
		.execute(new MyCallBack(tag,netResult, true));
   }

   /**
    * 耗材操作开柜扫描提交数据
    */
   public void putEPCDate(String deviceInventoryVos, Object tag,LoadingDialog.Builder dialog, NetResult netResult) {
	OkGo.<String>post(NetApi.URL_OPERATE_QUERY).tag(tag)
		.upJson(deviceInventoryVos)
		.execute(new MyCallBack(tag,dialog,netResult, false));
   }
   /**
    * 耗材操作确认操作
    */
   public void putOperateYes(String operateTCstInventory,Object tag, LoadingDialog.Builder dialog, NetResult netResult) {
	OkGo.<String>post(NetApi.URL_OPERATE_INOUTBOX_YES).tag(tag)
		.upJson(operateTCstInventory)
		.execute(new MyCallBack(tag,dialog,netResult, false));
   }
   /**
    * 根据科室查询库房情况    移出查
    */
   public void getOperateYcDeptYes(String deptCode,Object tag, LoadingDialog.Builder dialog, NetResult netResult) {
	OkGo.<String>get(NetApi.URL_OPERATE_YC_YES).tag(tag)
		.params("deptCode", deptCode)
		.execute(new MyCallBack(tag,dialog,netResult, false));
   }
   /**
    * 查询非本科室的库房    调拨查
    */
   public void getOperateDbDialog(String deptCode,String branchCode,Object tag, LoadingDialog.Builder dialog, NetResult netResult) {
	OkGo.<String>get(NetApi.URL_OPERATE_DB_YES).tag(tag)
		.params("deptCode", deptCode)
		.params("branchCode", branchCode)
		.execute(new MyCallBack(tag,dialog,netResult, false));
   }
   /**
    * 盘点
    */
   public void startTimelyScan(String tCstInventoryDto,Object tag, LoadingDialog.Builder dialog, NetResult netResult) {
	OkGo.<String>post(NetApi.URL_TIMELY_ONE).tag(tag)
		.upJson(tCstInventoryDto)
		.execute(new MyCallBack(tag,dialog,netResult, false));
   }
   /**
    * 盘亏
    */
   public void getLossesDate(String tCstInventoryDto,Object tag, LoadingDialog.Builder dialog, NetResult netResult) {
	OkGo.<String>post(NetApi.URL_TIMELY_LOSSES).tag(tag)
		.upJson(tCstInventoryDto)
		.execute(new MyCallBack(tag,dialog,netResult, false));
   }

   /**
    * 盘盈
    */
   public void getProfitDate(String tCstInventoryDto,Object tag, LoadingDialog.Builder dialog, NetResult netResult) {
	OkGo.<String>post(NetApi.URL_TIMELY_PROFIT).tag(tag)
		.upJson(tCstInventoryDto)
		.execute(new MyCallBack(tag,dialog,netResult, false));
   }

   /**
    * 盘点后的详情
    */
   public void getDetailDate(String tCstInventoryDto,Object tag, LoadingDialog.Builder dialog, NetResult netResult) {
	OkGo.<String>post(NetApi.URL_TIMELY_DETAIL).tag(tag)
		.upJson(tCstInventoryDto)
		.execute(new MyCallBack(tag,dialog,netResult, false));
   }

   /**
    * 患者绑定
    */
   public void bingPatientsDate(String tCstInventoryVo ,Object tag, LoadingDialog.Builder dialog, NetResult netResult) {
	OkGo.<String>post(NetApi.URL_BIND_PATIENT).tag(tag)
		.upJson(tCstInventoryVo )
		.execute(new MyCallBack(tag,dialog,netResult, false));
   }

   /**
    * 查询患者信息
    */
   public void findSchedulesDate(String optienNameOrId,Object tag, LoadingDialog.Builder dialog, NetResult netResult) {
	OkGo.<String>get(NetApi.URL_PATIENTS_FIND).tag(tag)
		.params("thingCode", sThingCode)
		.params("optienNameOrId", optienNameOrId)
		.execute(new MyCallBack(tag,dialog,netResult, false));
   }

   /**
    * 查询所有的配置项
    */
   public void findThingConfigDate(Object tag, LoadingDialog.Builder dialog, NetResult netResult) {
	OkGo.<String>get(NetApi.URL_THING_CONFIG_FIND).tag(tag)
		.params("thingCode", sThingCode)
		.execute(new MyCallBack(tag,dialog,netResult, false));
   }

   private class MyCallBack extends StringCallback {



	private Object tag;
	private NetResult netResult;
	private LoadingDialog.Builder dialog;
	private boolean isGet;//是否是get请求

	public MyCallBack( Object tag,LoadingDialog.Builder dialog, NetResult netResult,
				 boolean isGet) {
	   super();

	   this.tag = tag;
	   this.netResult = netResult;
	   this.isGet = isGet;
	   this.dialog = dialog;
	}
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
//		netResult.onError("  body:  "+response.body()+"  code:  "+response.code()+"  message:  "+response.message());
		netResult.onError(response.code()+"");
	   }
	   ToastUtils.showShort("请求失败  ("+response.code()+")");

	   if (dialog!=null){
		dialog.mDialog.dismiss();
	   }
//	   ToastUtils.showLong("response.code()= "+response.code()+"\n"+"response.message()="+response.message());
	   Log.i("fff", "response.body()    " + response.body());
	   Log.i("fff","response.code()    "+response.code());
	   Log.i("fff","response.message()    "+response.message());
	}

	@Override
	public void onSuccess(Response<String> response) {
	   if (netResult!=null){
		netResult.onSucceed(response.body());
	   }
	   if (dialog!=null){
		dialog.mDialog.dismiss();
	   }
//	   ToastUtils.showLong("response.code()= "+response.code()+"\n"+"response.message()="+response.message());
	   Log.i("fff", "response.body()    " + response.body());
	   Log.i("fff","response.code()    "+response.code());
	   Log.i("fff","response.message()    "+response.message());
	}
   }
}
