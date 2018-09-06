package high.rivamed.myapplication.http;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.List;

import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.SPUtils;
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
    private static Gson mGson;
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
    public void getDeviceInfosDate(String url, List<String> deviceTypes, Object tag, NetResult netResult) {

        OkGo.<String>get(url + NetApi.URL_TEST_FINDDEVICE).tag(tag)
                .addUrlParams("deviceTypes", deviceTypes)
                .execute(new MyCallBack(tag, netResult, true));
    }

    /**
     * 预注册
     */
    public void setSaveRegisteDate(String TBaseThing, Object tag, NetResult netResult) {
        Log.e(TAG, TBaseThing);
        Log.e(TAG, NetApi.URL_TEST_REGISTE);

        OkGo.<String>post(NetApi.URL_TEST_REGISTE).tag(tag)
                .upJson(TBaseThing)
                .execute(new MyCallBack(tag, netResult, true));
    }

    /**
     * 激活
     */
    public void setSaveActiveDate(String TBaseThing, Object tag, NetResult netResult) {

        OkGo.<String>post(NetApi.URL_TEST_ACTIVE).tag(tag)
                .upJson(TBaseThing)
                .execute(new MyCallBack(tag, netResult, true));
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
    public void loadRunWate(String page, String size,
                            String deviceCode, String term, String startTime, String endTime, String status, Object tag,
                            NetResult netResult) {
        LogUtils.i(TAG, "startTime  " + startTime + "     endTime   " + endTime);
        OkGo.<String>get(NetApi.URL_HOME_RUNWATE).tag(tag)
                .params("thingCode", sThingCode)
                .params("pageNo", page)
                .params("pageSize", size)
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
    public void loadBoxSize(Object tag, LoadingDialog.Builder dialog,
                            NetResult netResult) {
        OkGo.<String>get(NetApi.URL_HOME_BOXSIZE).tag(tag)
                .params("thingCode", sThingCode)
                .execute(new MyCallBack(tag, dialog, netResult, true));
    }


    /**
     * 耗材效期监控
     */
    public void materialControl(Object tag,
                                NetResult netResult) {
        OkGo.<String>get(NetApi.URL_STOCKSTATUS_TOP).tag(tag)
                .params("thingCode", sThingCode)
                .execute(new MyCallBack(tag, netResult, true));
    }

    /**
     * 库存详情和耗材库存预警
     */
    public void getStockDown(String nameOrSpecQueryCon, String deviceCode, int mStopFlag, Object tag, NetResult netResult) {
        Log.i("OkGo", "nameOrSpecQueryCon   " + nameOrSpecQueryCon);
        Log.i("OkGo", "deviceCode   " + deviceCode);
        Log.i("OkGo", "mStopFlag   " + mStopFlag);
        Log.i("OkGo", "sThingCode   " + sThingCode);
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
    public void getRightUnconfDate(String deviceCode, String mTrim, Object tag, NetResult netResult) {
        OkGo.<String>get(NetApi.URL_STOCKUNCON_RIGHT).tag(tag)
                .params("thingCode", sThingCode)
                .params("deviceCode", deviceCode)
                .params("nameOrSpecQueryCon", mTrim)
                .execute(new MyCallBack(tag, netResult, true));
    }

    /**
     * 查询单个耗材
     */
    public void getStockDetailDate(String deviceCode, String cstId, Object tag, NetResult netResult) {
        OkGo.<String>get(NetApi.URL_STOCK_DETAIL).tag(tag)
                .params("cstId", cstId)
                .params("deviceCode", deviceCode)
                .params("thingCode", sThingCode)
                .execute(new MyCallBack(tag, netResult, true));
    }

    /**
     * 数据恢复
     */
    public void getRecoverDate(String sn, Object tag, NetResult netResult) {
        LogUtils.i("fff", "sn  " + sn + "   url   " + NetApi.URL_TEST_SNQUERY);
        OkGo.<String>get(NetApi.URL_TEST_SNQUERY).tag(tag)
                .params("sn", sn)
                .execute(new MyCallBack(tag, netResult, true));
    }

    /**
     * 输入医院查询医院信息
     */
    public void getHospNameDate(String hospName, Object tag, NetResult netResult) {
        OkGo.<String>get(NetApi.URL_TEST_FIND_HOSPHOME).tag(tag)
                .params("hospName", hospName)
                .execute(new MyCallBack(tag, netResult, true));
    }

    /**
     * 根据医院id查询院区信息
     */
    public void getHospBranch(String hospIds, Object tag, NetResult netResult) {
        OkGo.<String>get(NetApi.URL_TEST_FIND_BRANCH).tag(tag)
                .params("hospIds", hospIds)
                .execute(new MyCallBack(tag, netResult, true));
    }

    /**
     * 根据院区编码查询科室信息
     */
    public void getHospDept(String deptNamePinYin,String branchCode, Object tag, NetResult netResult) {
        OkGo.<String>get(NetApi.URL_TEST_FIND_DEPT).tag(tag)
                .params("deptNamePinYin", deptNamePinYin)
                .params("branchCode", branchCode)
                .execute(new MyCallBack(tag, netResult, true));
    }

    /**
     * 根据科室查询库房情况
     */
    public void getHospBydept(String deptId, Object tag, NetResult netResult) {
        OkGo.<String>get(NetApi.URL_TEST_FIND_BYDEPT).tag(tag)
                .params("deptId", deptId)
                .execute(new MyCallBack(tag, netResult, true));
    }

    /**
     * 根据科室查询手术室信息
     */
    public void getHospRooms(String deptId, Object tag, NetResult netResult) {
        OkGo.<String>get(NetApi.URL_TEST_FIND_OPERROOMS).tag(tag)
                .params("deptId", deptId)
                .execute(new MyCallBack(tag, netResult, true));
    }

    /**
     * 耗材操作开柜扫描提交数据
     */
    public void putEPCDate(String deviceInventoryVos, Object tag, LoadingDialog.Builder dialog, NetResult netResult) {
        OkGo.<String>post(NetApi.URL_OPERATE_QUERY).tag(tag)
                .upJson(deviceInventoryVos)
                .execute(new MyCallBack(tag, dialog, netResult, false));
    }

    /**
     * 耗材操作确认操作
     */
    public void putOperateYes(String operateTCstInventory, Object tag, LoadingDialog.Builder dialog, NetResult netResult) {
        OkGo.<String>post(NetApi.URL_OPERATE_INOUTBOX_YES).tag(tag)
                .upJson(operateTCstInventory)
                .execute(new MyCallBack(tag, dialog, netResult, false));
    }

    /**
     * 根据科室查询库房情况    移出查
     */
    public void getOperateYcDeptYes(String deptId, Object tag, LoadingDialog.Builder dialog, NetResult netResult) {
        OkGo.<String>get(NetApi.URL_OPERATE_YC_YES).tag(tag)
                .params("deptId", deptId)
                .execute(new MyCallBack(tag, dialog, netResult, false));
    }

    /**
     * 查询非本科室的库房    调拨查
     */
    public void getOperateDbDialog(String deptId, String branchCode, Object tag, LoadingDialog.Builder dialog, NetResult netResult) {
        OkGo.<String>get(NetApi.URL_OPERATE_DB_YES).tag(tag)
                .params("deptId", deptId)
                .params("branchCode", branchCode)
                .execute(new MyCallBack(tag, dialog, netResult, false));
    }

    /**
     * 盘点
     */
    public void startTimelyScan(String tCstInventoryDto, Object tag, LoadingDialog.Builder dialog, NetResult netResult) {
        OkGo.<String>post(NetApi.URL_TIMELY_ONE).tag(tag)
                .upJson(tCstInventoryDto)
                .execute(new MyCallBack(tag, dialog, netResult, false));
    }

    /**
     * 盘亏
     */
    public void getLossesDate(String tCstInventoryDto, Object tag, LoadingDialog.Builder dialog, NetResult netResult) {
        OkGo.<String>post(NetApi.URL_TIMELY_LOSSES).tag(tag)
                .upJson(tCstInventoryDto)
                .execute(new MyCallBack(tag, dialog, netResult, false));
    }

    /**
     * 盘盈
     */
    public void getProfitDate(String tCstInventoryDto, Object tag, LoadingDialog.Builder dialog, NetResult netResult) {
        OkGo.<String>post(NetApi.URL_TIMELY_PROFIT).tag(tag)
                .upJson(tCstInventoryDto)
                .execute(new MyCallBack(tag, dialog, netResult, false));
    }

    /**
     * 盘点后的详情
     */
    public void getDetailDate(String tCstInventoryDto, Object tag, LoadingDialog.Builder dialog, NetResult netResult) {
        OkGo.<String>post(NetApi.URL_TIMELY_DETAIL).tag(tag)
                .upJson(tCstInventoryDto)
                .execute(new MyCallBack(tag, dialog, netResult, false));
    }

    /**
     * 患者绑定
     */
    public void bingPatientsDate(String tCstInventoryVo, Object tag, LoadingDialog.Builder dialog, NetResult netResult) {
        OkGo.<String>post(NetApi.URL_BIND_PATIENT).tag(tag)
                .upJson(tCstInventoryVo)
                .execute(new MyCallBack(tag, dialog, netResult, false));
    }

    /**
     * 创建临时患者信息
     */
    public void saveTempPatient(String json, Object tag, LoadingDialog.Builder dialog, NetResult netResult) {
        OkGo.<String>post(NetApi.URL_SAVE_TEMP_PATIENT).tag(tag)
                .upJson(json)
                .execute(new MyCallBack(tag, dialog, netResult, false));
    }

    /**
     * 查询患者信息（包含临时患者）
     */
    public void findSchedulesDate(String optienNameOrId, Object tag, LoadingDialog.Builder dialog, NetResult netResult) {
        OkGo.<String>get(NetApi.URL_PATIENTS_FIND).tag(tag)
                .params("thingCode", sThingCode)
                .params("patientNameOrId", optienNameOrId)
                .execute(new MyCallBack(tag, dialog, netResult, false));
    }

    /**
     * 查询本科室下24小时的手术预约患者（不包含临时患者）
     */
    public void findSchedulesDateNoTemp(String optienNameOrId, Object tag, LoadingDialog.Builder dialog, NetResult netResult) {
        OkGo.<String>get(NetApi.URL_PATIENTS_FIND_NO_TEMP).tag(tag)
                .params("thingCode", sThingCode)
                .params("patientNameOrId", optienNameOrId)
                .execute(new MyCallBack(tag, dialog, netResult, false));
    }

    /**
     * 查询所有的未绑定临时患者
     */
    public void findTempPatients(String optienNameOrId, Object tag, LoadingDialog.Builder dialog, NetResult netResult) {
        OkGo.<String>get(NetApi.URL_FIND_TEMP_PATIENTS).tag(tag)
                .params("thingCode", sThingCode)
                .params("patientNameOrId", optienNameOrId)
                .execute(new MyCallBack(tag, dialog, netResult, false));
    }

   /**
     * 临时患者与在院患者进行关联
     */
    public void tempPatientConnPatient(String json, Object tag, LoadingDialog.Builder dialog, NetResult netResult) {
        OkGo.<String>post(NetApi.URL_TEMP_PATIENT_CONN_PATIENT).tag(tag)
                . upJson(json)
                .execute(new MyCallBack(tag, dialog, netResult, false));
    }

    /**
     * 查询所有在院患者信息
     */
    public void findInPatientPage(String patientNameOrId, int pageNo, int pageSize, Object tag, LoadingDialog.Builder dialog, NetResult netResult) {
        OkGo.<String>get(NetApi.URL_FIND_IN_PATIENT_PAGE).tag(tag)
                .params("patientNameOrId", patientNameOrId)
                .params("pageNo", pageNo)
                .params("pageSize", pageSize)
                .execute(new MyCallBack(tag, dialog, netResult, false));
    }

    /**
     * 查询所有的配置项
     */
    public void findThingConfigDate(Object tag, LoadingDialog.Builder dialog, NetResult netResult) {
        OkGo.<String>get(NetApi.URL_THING_CONFIG_FIND).tag(tag)
                .params("thingCode", sThingCode)
                .execute(new MyCallBack(tag, dialog, netResult, false));
    }

    private class MyCallBack extends StringCallback {


        private Object tag;
        private NetResult netResult;
        private LoadingDialog.Builder dialog;
        private boolean isGet;//是否是get请求

        public MyCallBack(Object tag, LoadingDialog.Builder dialog, NetResult netResult,
                          boolean isGet) {
            super();

            this.tag = tag;
            this.netResult = netResult;
            this.isGet = isGet;
            this.dialog = dialog;
        }

        public MyCallBack(Object tag, NetResult netResult,
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
                netResult.onError(response.code() + "");
            }
            Log.i("fff", "response.body()    " + response.body());
            Log.i("fff", "response.code()    " + response.code());
            Log.i("fff", "response.message()    " + response.message());
            Toast.makeText(UIUtils.getContext(), "请求失败  (" + response.code() + ")", Toast.LENGTH_SHORT).show();
            if (dialog != null) {
                dialog.mDialog.dismiss();
            }
        }

        @Override
        public void onSuccess(Response<String> response) {
            if (netResult != null) {
                netResult.onSucceed(response.body());
            }
            if (dialog != null) {
                dialog.mDialog.dismiss();
            }
            Log.i("fff", "response.body()    " + response.body());
            Log.i("fff", "response.code()    " + response.code());
            Log.i("fff", "response.message()    " + response.message());
        }
    }
}
