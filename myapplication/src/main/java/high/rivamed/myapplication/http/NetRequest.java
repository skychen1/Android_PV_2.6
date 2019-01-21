package high.rivamed.myapplication.http;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import high.rivamed.myapplication.activity.LoginActivity;
import high.rivamed.myapplication.base.App;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;
import high.rivamed.myapplication.views.LoadingDialog;

import static high.rivamed.myapplication.base.App.MAIN_URL;
import static high.rivamed.myapplication.cont.Constants.ACCESS_TOKEN;
import static high.rivamed.myapplication.cont.Constants.ERROR_1000;
import static high.rivamed.myapplication.cont.Constants.ERROR_1001;
import static high.rivamed.myapplication.cont.Constants.ERROR_1010;
import static high.rivamed.myapplication.cont.Constants.ERROR_200;
import static high.rivamed.myapplication.cont.Constants.REFRESH_TOKEN;
import static high.rivamed.myapplication.cont.Constants.SAVE_DEPT_CODE;
import static high.rivamed.myapplication.cont.Constants.SYSTEMTYPE;
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
   private static String     sThingCode;

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
    * Post请求无token
    */
   private void PostRequest(String url, String date, Object tag, NetResult netResult) {

	OkGo.<String>post(url).tag(tag)
		.upJson(date)
		.execute(new MyCallBack(url, date, tag, netResult, false, false));
   }

   /**
    * Post请求有token
    */
   private void PostTokenRequest(String url, String date, Object tag, NetResult netResult) {

	OkGo.<String>post(url).tag(tag)
		.headers("tokenId", SPUtils.getString(UIUtils.getContext(), ACCESS_TOKEN))
		.upJson(date)
		.execute(new MyCallBack(url, date, tag, netResult, false, true));
   }

   /**
    * Get请求有token
    */
   private void GetTokenRequest(
	   String url, Map<String, String> map, Object tag, NetResult netResult) {

	OkGo.<String>get(url).tag(tag)
		.headers("tokenId", SPUtils.getString(UIUtils.getContext(), ACCESS_TOKEN))
		.params(map)
		.execute(new MyCallBack(url, map, tag, netResult, true, true));
   }

   /**
    * Get请求无token
    */
   private void GetRequest(
	   String url, Map<String, String> map, Object tag, NetResult netResult) {

	OkGo.<String>get(url).tag(tag)
		.params(map)
		.execute(new MyCallBack(url, map, tag, netResult, true, false));
   }

   /**
    * 预注册和激活的时候获取部件名称和ID
    */
   public void getDeviceInfosDate(
	   String url, List<String> deviceTypes, Object tag, NetResult netResult) {
	OkGo.<String>get(url + NetApi.URL_TEST_FINDDEVICE).tag(tag)
		.addUrlParams("deviceTypes", deviceTypes)
		.execute(new MyCallBack(url, deviceTypes, tag, netResult, true, false));
   }

   /**
    * 预注册
    */
   public void setSaveRegisteDate(String TBaseThing, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_TEST_REGISTE;
	PostRequest(urls, TBaseThing, tag, netResult);
   }

   /**
    * 激活
    */
   public void setSaveActiveDate(String TBaseThing, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_TEST_ACTIVE;
	PostRequest(urls, TBaseThing, tag, netResult);
   }

   /**
    * 版本检测
    */
   public void checkVer(Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_GET_VER;
	Map<String, String> map = new HashMap<>();
	map.put("systemType",SYSTEMTYPE);
	GetRequest(urls, map, tag, netResult);
   }

   /**
    * 用户登录
    */
   public void userLogin(String account, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_USER_LOGIN;
	PostRequest(urls, account, tag, netResult);
   }

   /**
    * 绑定指纹
    */
   public void registerFinger(String json, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_USER_REGISTER_FINGER;
	PostRequest(urls, json, tag, netResult);
   }

   /**
    * 绑定腕带
    */
   public void registerIdCard(String json, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_USER_REGISTERWAIDAI;
	PostTokenRequest(urls, json, tag, netResult);
   }

   /**
    * 腕带解绑
    */
   public void unRegisterIdCard(String json, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_USER_UNREGISTERWAIDAI;
	PostTokenRequest(urls, json, tag, netResult);
   }

   /**
    * 指纹登录
    */
   public void validateLoginFinger(String json, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_USER_VALIDATELOGIN_FINGER;
	PostRequest(urls, json, tag, netResult);
   }

   /**
    * 紧急登录密码修改
    */
   public void emergencySetting(String pwd, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_USER_EMERGENCY_PWD;
	Map<String, String> map = new HashMap<>();
	map.put("account.emergencyPwd", pwd);
	GetTokenRequest(urls, map, tag, netResult);

   }

   /**
    * IdCard登录
    */
   public void validateLoginIdCard(String json, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_USER_VALIDATELOGINWRIST;
	PostRequest(urls, json, tag, netResult);
   }

   /**
    * 重置密码
    */
   public void resetPassword(String json, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_USER_RESET_PASSWORD;
	PostTokenRequest(urls, json, tag, netResult);
   }

   /**
    * 获取耗材流水
    */
   public void loadRunWate(
	   String page, String size, String deviceCode, String term, String startTime, String endTime,
	   String status, Object tag, NetResult netResult) {

	String urls = MAIN_URL + NetApi.URL_HOME_RUNWATE;
	Map<String, String> map = new HashMap<>();
	map.put("thingId", sThingCode);
	map.put("pageNo", page);
	map.put("pageSize", size);
	map.put("startTime", startTime);
	map.put("endTime", endTime);
	map.put("status", status);
	map.put("deviceId", deviceCode);
	map.put("term", term);
	String json = mGson.toJson(map);
	PostTokenRequest(urls, json, tag, netResult);
   }

   /**
    * 获取柜子个数
    */
   public void loadBoxSize(Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_HOME_BOXSIZE;
	Map<String, String> map = new HashMap<>();
	map.put("thingId", sThingCode);
	GetRequest(urls, map, tag, netResult);
   }

   /**
    * 耗材效期监控
    */
   public void materialControl(Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_STOCKSTATUS_TOP;
	Map<String, String> map = new HashMap<>();
	map.put("thingId", sThingCode);
	GetRequest(urls, map, tag, netResult);
   }

   /**
    * 库存详情和耗材库存预警
    */
   public void getStockDown(
	   String nameOrSpecQueryCon, String deviceCode, int mStopFlag, Object tag,
	   NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_STOCKSTATUS_DETAILS;
	Map<String, String> map = new HashMap<>();
	map.put("thingId", sThingCode);
	map.put("nameOrSpecQueryCon", nameOrSpecQueryCon);
	map.put("deviceId", deviceCode);
	map.put("expireStatus", mStopFlag + "");
	GetRequest(urls, map, tag, netResult);
   }

   /**
    * 未确认耗材
    */
   public void getRightUnconfDate(
	   String deviceCode, String mTrim, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_STOCKUNCON_RIGHT;
	Map<String, String> map = new HashMap<>();
	map.put("thingId", sThingCode);
	map.put("deviceCode", deviceCode);
	map.put("nameOrSpecQueryCon", mTrim);
	GetTokenRequest(urls, map, tag, netResult);
   }

   /**
    * 查询单个耗材
    */
   public void getStockDetailDate(
	   String deviceCode, String cstId, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_STOCK_DETAIL;
	Map<String, String> map = new HashMap<>();
	map.put("cstId", cstId);
	map.put("deviceId", deviceCode);
	map.put("thingId", sThingCode);
	GetRequest(urls, map, tag, netResult);
   }

   /**
    * 数据恢复
    */
   public void getRecoverDate(String sn, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_TEST_SNQUERY;
	Map<String, String> map = new HashMap<>();
	map.put("sn", sn);
	GetRequest(urls, map, tag, netResult);
   }

   /**
    * 查询院区信息
    */
   public void getHospBranch(Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_TEST_FIND_BRANCH;
	GetRequest(urls, null, tag, netResult);
   }

   /**
    * 根据院区编码查询科室信息
    */
   public void getHospDept(
	   String deptNamePinYin, String branchId, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_TEST_FIND_DEPT;
	Map<String, String> map = new HashMap<>();
	map.put("deptNamePinYin", deptNamePinYin);
	map.put("branchId", branchId);
	GetRequest(urls, map, tag, netResult);
   }

   /**
    * 根据科室查询库房情况
    */
   public void getHospBydept(String deptId, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_TEST_FIND_BYDEPT;
	Map<String, String> map = new HashMap<>();
	map.put("deptId", deptId);
	GetRequest(urls, map, tag, netResult);

   }

   /**
    * 根据科室查询手术室信息
    */
   public void getHospRooms(String deptId, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_TEST_FIND_OPERROOMS;
	Map<String, String> map = new HashMap<>();
	map.put("deptId", deptId);
	GetTokenRequest(urls, map, tag, netResult);
   }

   //   /**
   //    * 耗材操作开柜扫描提交数据(快速开柜入柜)
   //    */
   //   public void putAllInEPCDate(
   //	   String deviceInventoryVos, Object tag, NetResult netResult) {
   //	String urls = MAIN_URL + NetApi.URL_QUERY_ALL_IN;
   //	PostTokenRequest(urls, deviceInventoryVos, tag, netResult);
   //   }

   /**
    * 耗材操作开柜扫描提交数据(快速开柜出柜)
    */
   public void putOutAndInEPCDate(
	   String deviceInventoryVos, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_QUERY_OUTANDIN;
	PostTokenRequest(urls, deviceInventoryVos, tag, netResult);
   }

   /**
    * 耗材操作开柜扫描提交数据
    */
   public void putEPCDate(String deviceInventoryVos, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_OPERATE_QUERY;
	PostTokenRequest(urls, deviceInventoryVos, tag, netResult);
   }

   /**
    * 耗材操作确认操作
    */
   public void putOperateYes(String operateTCstInventory, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_OPERATE_INOUTBOX_YES;
	PostTokenRequest(urls, operateTCstInventory, tag, netResult);
   }

   /**
    * 耗材操作确认操作(快速开柜)
    */
   public void putAllOperateYes(String operateTCstInventory, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_QUERY_ALL_YES;
	PostTokenRequest(urls, operateTCstInventory, tag, netResult);
   }

   /**
    * 查询非本科室的库房    调拨查
    */
   public void getOperateDbDialog(
	   String deptId, String branchCode, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_OPERATE_DB_YES;
	Map<String, String> map = new HashMap<>();
	map.put("deptId", deptId);
	map.put("branchCode", branchCode);
	GetTokenRequest(urls, map, tag, netResult);
   }

   /**
    * 盘点
    */
   public void startTimelyScan(String tCstInventoryDto, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_TIMELY_ONE;
	PostTokenRequest(urls, tCstInventoryDto, tag, netResult);
   }

   /**
    * 盘亏
    */
   public void getLossesDate(
	   String tCstInventoryDto, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_TIMELY_LOSSES;
	PostTokenRequest(urls, tCstInventoryDto, tag, netResult);
   }

   /**
    * 盘盈
    */
   public void getProfitDate(
	   String tCstInventoryDto, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_TIMELY_PROFIT;
	PostTokenRequest(urls, tCstInventoryDto, tag, netResult);
   }

   /**
    * 盘点后的详情
    */
   public void getDetailDate(
	   String tCstInventoryDto, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_TIMELY_DETAIL;
	PostTokenRequest(urls, tCstInventoryDto, tag, netResult);

   }

   /**
    * 查询患者信息（包含临时患者）
    */
   public void findSchedulesDate(
	   String optienNameOrId, int pageNo, int pageSize, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_PATIENTS_FIND;
	Map<String, String> map = new HashMap<>();
	map.put("deptId", SPUtils.getString(UIUtils.getContext(), SAVE_DEPT_CODE));
	map.put("patientNameOrId", optienNameOrId);
	map.put("pageNo", pageNo + "");
	map.put("pageSize", pageSize + "");
	GetTokenRequest(urls, map, tag, netResult);
   }

   /**
    * 查询所有的未绑定临时患者
    */
   public void findTempPatients(
	   String optienNameOrId, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_FIND_TEMP_PATIENTS;
	Map<String, String> map = new HashMap<>();
	map.put("thingId", sThingCode);
	map.put("patientNameOrId", optienNameOrId);
	GetTokenRequest(urls, map, tag, netResult);
   }

   /**
    * 临时患者与在院患者进行关联
    */
   public void tempPatientConnPatient(
	   String json, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_TEMP_PATIENT_CONN_PATIENT;
	PostTokenRequest(urls, json, tag, netResult);
   }

   /**
    * 查询所有在院患者信息
    */
   public void findInPatientPage(
	   String patientNameOrId, int pageNo, int pageSize, Object tag, LoadingDialog.Builder dialog,
	   NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_FIND_IN_PATIENT_PAGE;
	Map<String, String> map = new HashMap<>();
	map.put("patientNameOrId", patientNameOrId);
	map.put("pageNo", pageNo + "");
	map.put("pageSize", pageSize + "");
	GetTokenRequest(urls, map, tag, netResult);
   }

   /**
    * 查询所有的配置项
    */
   public void findThingConfigDate(Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_THING_CONFIG_FIND;
	Map<String, String> map = new HashMap<>();
	map.put("thingId", sThingCode);
	GetRequest(urls, map, tag, netResult);
   }

   /**
    * 使用记录的患者列表
    */
   public void getFindPatientDate(
	   String string, int page, int rows, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_FIND_PATIENT;
	Map<String, String> map = new HashMap<>();
	map.put("patientNameOrId", string);
	map.put("pageSize", rows + "");
	map.put("pageNo", page + "");
	map.put("thingId", sThingCode);
	GetTokenRequest(urls, map, tag, netResult);
   }

   /**
    * 使用记录详情
    */
   public void getFindEpcDetails(String patientId, int status, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_FIND_EPC_DETAILS;
	Map<String, String> map = new HashMap<>();
	map.put("patientId", patientId);
	map.put("status", status + "");
	GetTokenRequest(urls, map, tag, netResult);
   }

   /**
    * 医嘱单领用-顶部医嘱单列表
    */
   public void findPatientOrderSheetDate(
	   String orderId, String pageNo, String pageSize, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_RECEIVEORDER_LISTALL;
	Map<String, String> map = new HashMap<>();
	map.put("orderId", orderId);
	map.put("pageNo", pageNo);
	map.put("pageSize", pageSize);
	GetTokenRequest(urls, map, tag, netResult);
   }

   /**
    * 医嘱单领用-根据医嘱单ID查询单柜耗材的库存
    */
   public void findStockByOrderId(
	   String Id, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_RECEIVEORDER_FINDBYORDERID;
	Map<String, String> map = new HashMap<>();
	map.put("orderId", Id);
	map.put("thingId", SPUtils.getString(UIUtils.getContext(), THING_CODE));
	GetTokenRequest(urls, map, tag, netResult);
   }


   /**
    * 医嘱单领用-根据EPC获取耗材
    */
   public void findBillStockByEpc(
	   String json, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_RECEIVEORDER_CONTAINORDERCST;
	PostTokenRequest(urls, json, tag, netResult);

   }

   /**
    * 医嘱单领用-确认领用耗材
    */
   public void sureReceiveOrder(
	   String json, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_RECEIVEORDER_TWOOUTBYRECEIVEORDER;
	PostTokenRequest(urls, json, tag, netResult);
   }

   /**
    * 套组领用-套组列表
    */
   public void findOrderCstPlanDate(Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_CSTPLAN_LISTFORPAD;
	Map<String, String> map = new HashMap<>();
	map.put("deptId", SPUtils.getString(UIUtils.getContext(), SAVE_DEPT_CODE));
	GetTokenRequest(urls, map, tag, netResult);
   }

   /**
    * 套组领用-查询单个套组的耗材详情
    */
   public void findOrderCstListById(
	   String cstPlanId, String thingId, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_CSTPLAN_FINDCSTLIST;
	Map<String, String> map = new HashMap<>();
	map.put("suiteId", cstPlanId);
	map.put("thingId", thingId);
	GetTokenRequest(urls, map, tag, netResult);
   }

   /**
    * 套组领用-根据EPC查询耗材详情
    */
   public void findOrderCstListByEpc(
	   String json, Object tag, LoadingDialog.Builder dialog, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_CSTPLAN_FINDCSTANDCOMPARETOCSTPLAN;
	PostTokenRequest(urls, json, tag, netResult);
   }

   /**
    * 消息-查询待办任务列表
    */
   public void getPendingTaskList(Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_FIND_MESSAGE_BY_ACCOUNTID;
	GetTokenRequest(urls, null, tag, netResult);
   }

   /**
    * 消息-删除待办任务
    */
   public void deleteMessageById(String id, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_DELETE_MESSAGE_BY_ID;
	Map<String, String> map = new HashMap<>();
	map.put("message.messageId", id);
	GetTokenRequest(urls, map, tag, netResult);
   }

   /**
    * 套组领用-领用套组
    */
   public void useOrderCst(String json, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_CSTPLAN_OPERATETCSTINVENTORY;
	PostTokenRequest(urls, json, tag, netResult);
   }

   /**
    * 请领单领用-在退出应用前提交套组信息生成消息
    */
   public void submitOrderCstInfo(String json, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_RECEIVEORDER_SAVERECEIVEORDERMSG;
	PostTokenRequest(urls, json, tag, netResult);
   }

   /**
    * 获取账号权限菜单（左侧、选择操作）
    */
   public void getAuthorityMenu(Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_AUTHORITY_MENU;
	Map<String, String> map = new HashMap<>();
	map.put("systemType", SYSTEMTYPE);
	OkGo.<String>get(urls).tag(tag)
		.headers("tokenId", SPUtils.getString(UIUtils.getContext(), ACCESS_TOKEN))
		.params(map)
		.execute(new MyCallBack2(urls, map, tag, netResult, true, true));
   }

   /**
    * 换新token
    */
   public void updateToken(Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_REFRESH_TOKEN;
	String json = "{\"systemType\": "+SYSTEMTYPE+",\"accessToken\": {\"refreshToken\": \"" +
			  SPUtils.getString(UIUtils.getContext(), REFRESH_TOKEN) + "\"}}";
	PostRequest(urls, json, tag, netResult);
   }

   /**
    * 获取设备中所有的耗材
    */
   public void getUnEntCstDate(Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_UNENT_GET_ALLCST;
	Map<String, String> map = new HashMap<>();
	map.put("thingId", sThingCode);
	GetRequest(urls, map, tag, netResult);
   }
   /**
    * 获取设备离线账户信息
    */
   public void getUnNetUseDate(Object tag,  NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_UNENT_GET_LIST_ACCOUNT;
	Map<String, String> map = new HashMap<>();
	map.put("systemType", SYSTEMTYPE);
	map.put("deptId", SPUtils.getString(UIUtils.getContext(), SAVE_DEPT_CODE));
	GetRequest(urls, map, tag, netResult);
   }
   /**
    * 获取离线手术间信息
    */
   public void getUnEntFindOperation(Object tag,  NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_UNENT_GET_FIND_OPERATIONROOM;
	Map<String, String> map = new HashMap<>();
	map.put("thingId", sThingCode);
	GetRequest(urls, map, tag, netResult);
   }

   private class MyCallBack extends StringCallback {

	private String    url;
	private Object    date;
	private Object    tag;
	private NetResult netResult;
	private boolean   isGet;//是否是get请求
	private boolean   isToken;//是否有token

	public MyCallBack(
		String url, Object date, Object tag, NetResult netResult, boolean isGet,
		boolean isToken) {
	   super();

	   this.url = url;
	   this.date = date;
	   this.tag = tag;
	   this.netResult = netResult;
	   this.isGet = isGet;
	   this.isToken = isToken;
	}

	@Override
	public void onError(Response<String> response) {
	   if (netResult != null) {
		netResult.onError(response.code() + "");
	   }
	   if (response.code() == -1) {
		ToastUtils.showShortToast("服务器异常，请检查网络！");
	   } else {
		ToastUtils.showShortToast("请求失败  (" + response.code() + ")");
	   }
	   LogUtils.w(TAG,"onError 请求URL： "+url);
	   LogUtils.w(TAG,"onError 请求URL： "+response.code());
	   LogUtils.w(TAG,"onError 请求Body： "+mGson.toJson(date));
	   LogUtils.w(TAG,"onError 返回Body： "+response.body());
	}

	@Override
	public void onSuccess(Response<String> response) {
	   try {
		JSONObject jsonObject = JSON.parseObject(response.body());
		if (null == jsonObject.getString("opFlg") ||
		    jsonObject.getString("opFlg").equals(ERROR_200)) {//正常
		   if (netResult != null) {
			netResult.onSucceed(response.body());
		   }
		} else {
		   String opFlg = jsonObject.getString("opFlg");
		   if (opFlg.equals(ERROR_1010)) {
		      LogUtils.w(TAG,"请求URL： "+url);
		      LogUtils.w(TAG,"请求Body： "+mGson.toJson(date));
		      LogUtils.w(TAG,"返回Body： "+response.body());
			ToastUtils.showShortToast("后台系统异常，请联系实施人员！");
			if (netResult != null) {
			   netResult.onSucceed(response.body());
			}
//			ToastUtils.showClickToast(App.getAppContext(), "后台系统异常 ", Toast.LENGTH_LONG);
		   } else if (opFlg.equals(ERROR_1000)) {//Token过期
			if (!TextUtils.isEmpty(UIUtils.getRefreshToken())) {
			   updateToken(tag, new BaseResult() {
				@Override
				public void onSucceed(String result) {
				   JSONObject jsonObject = JSON.parseObject(response.body());
				   if (null == jsonObject.getString("opFlg")) {//正常
					if (isGet) {
					   if (isToken) {
						GetTokenRequest(url, (Map<String, String>) date, tag,
								    netResult);
					   } else {
						GetRequest(url, (Map<String, String>) date, tag, netResult);
					   }
					} else {
					   if (isToken) {
						PostTokenRequest(url, (String) date, tag, netResult);
					   } else {
						PostRequest(url, (String) date, tag, netResult);
					   }
					}
				   } else {
					if (jsonObject.getString("opFlg").equals(ERROR_1001)) {
					   ToastUtils.showShortToast("登录状态已经过期，请重新登录");
					   if (tag instanceof Activity) {
						UIUtils.putOrderId(tag);
						((Activity) tag).getApplicationContext()
							.startActivity(
								new Intent(((Activity) tag).getApplicationContext(),
									     LoginActivity.class));
						App.getInstance().removeALLActivity_();
						((Activity) tag).finish();
					   }
					}
				   }
				   Log.i(TAG, "result   " + result);
				}
			   });
			}
		   } else if (opFlg.equals(ERROR_1001)) {//刷新TOKEN过期   需要重新登录
			ToastUtils.showShortToast("登录状态已经过期，请重新登录");
			if (tag instanceof Activity) {
			   UIUtils.putOrderId(tag);
			   ((Activity) tag).getApplicationContext()
				   .startActivity(
					   new Intent(((Activity) tag).getApplicationContext(),
							  LoginActivity.class));
			   App.getInstance().removeALLActivity_();
			   ((Activity) tag).finish();
			}
		   }
		}
	   } catch (Exception e) {
		LogUtils.w(TAG,"Exception 请求URL： "+url);
		LogUtils.w(TAG,"Exception 请求Body： "+mGson.toJson(date));
		LogUtils.w(TAG,"Exception 返回Body： "+response.body());
		e.printStackTrace();
	   }
	}
   }

   private class MyCallBack2 extends StringCallback {

	private String    url;
	private Object    date;
	private Object    tag;
	private NetResult netResult;
	private boolean   isGet;//是否是get请求
	private boolean   isToken;//是否有token

	public MyCallBack2(
		String url, Object date, Object tag, NetResult netResult, boolean isGet,
		boolean isToken) {
	   super();

	   this.url = url;
	   this.date = date;
	   this.tag = tag;
	   this.netResult = netResult;
	   this.isGet = isGet;
	   this.isToken = isToken;
	}

	@Override
	public void onError(Response<String> response) {
	   if (netResult != null) {
		netResult.onError(response.code() + "");
	   }
	   LogUtils.w(TAG,"onError 请求URL： "+url);
	   LogUtils.w(TAG,"onError 请求Body： "+mGson.toJson(date));
	   LogUtils.w(TAG,"onError 返回Body： "+response.body());
	}

	@Override
	public void onSuccess(Response<String> response) {
	   if (netResult != null) {
		netResult.onSucceed(response.body());
		LogUtils.w(TAG,"MyCallBack2 请求URL： "+url);
		LogUtils.w(TAG,"MyCallBack2 请求Body： "+mGson.toJson(date));
		LogUtils.w(TAG,"MyCallBack2 返回Body： "+response.body());
	   }
	}
   }

   /**
    * 更新本地Token
    */
   private void updateLocalToken(String data) {
	//	LoginBean loginBean = mGson.fromJson(data, LoginBean.class);
	//	UIUtils.getCache().put(ACCESS_TOKEN, loginBean.getAccess_token(), loginBean.getExpires_in());//登录成功的token
	//	UIUtils.getCache().put(REFRESH_TOKEN, loginBean.getRefresh_token(), ACache.TIME_DAY);//登录成功的refresh_token

   }
}
