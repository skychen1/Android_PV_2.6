package high.rivamed.myapplication.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.rivamed.Eth002Manager;
import cn.rivamed.callback.Eth002CallBack;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.activity.LoginInfoActivity;
import high.rivamed.myapplication.activity.OutFormActivity;
import high.rivamed.myapplication.activity.PatientConnActivity;
import high.rivamed.myapplication.bean.BillStockResultBean;
import high.rivamed.myapplication.bean.BingFindSchedulesBean;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.ExceptionRecordBean;
import high.rivamed.myapplication.bean.HospNameBean;
import high.rivamed.myapplication.bean.LoginResultBean;
import high.rivamed.myapplication.bean.Movie;
import high.rivamed.myapplication.bean.OrderSheetBean;
import high.rivamed.myapplication.bean.UnRegistBean;
import high.rivamed.myapplication.devices.AllDeviceCallBack;
import high.rivamed.myapplication.dto.InventoryDto;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.timeutil.DateListener;
import high.rivamed.myapplication.timeutil.TimeConfig;
import high.rivamed.myapplication.timeutil.TimeSelectorDialog;
import high.rivamed.myapplication.views.BindIdCardDialog;
import high.rivamed.myapplication.views.EmergencyTwoDialog;
import high.rivamed.myapplication.views.EpcTestDialog;
import high.rivamed.myapplication.views.InBoxCountDialog;
import high.rivamed.myapplication.views.LoadingDialog;
import high.rivamed.myapplication.views.LoadingDialogX;
import high.rivamed.myapplication.views.LookUpDetailedListDialog;
import high.rivamed.myapplication.views.LossScuseDialog;
import high.rivamed.myapplication.views.NoDialog;
import high.rivamed.myapplication.views.OneDialog;
import high.rivamed.myapplication.views.OneFingerDialog;
import high.rivamed.myapplication.views.OnePassWordDialog;
import high.rivamed.myapplication.views.OpenDoorDialog;
import high.rivamed.myapplication.views.OutBoxConnectDialog;
import high.rivamed.myapplication.views.RegisteDialog;
import high.rivamed.myapplication.views.RvDialog2;
import high.rivamed.myapplication.views.SelectExceptionOperatorDialog;
import high.rivamed.myapplication.views.SelectOpenCabinetDialog;
import high.rivamed.myapplication.views.StoreRoomDialog;
import high.rivamed.myapplication.views.TempPatientDialog;
import high.rivamed.myapplication.views.TwoDialog;
import high.rivamed.myapplication.views.WifiDialog;

import static high.rivamed.myapplication.base.BaseSimpleActivity.mStarts;
import static high.rivamed.myapplication.cont.Constants.KEY_ACCOUNT_DATA;
import static high.rivamed.myapplication.utils.UIUtils.removeAllAct;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/28 9:34
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.utils
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class DialogUtils {

    public static String sTimes;

    /**
     * 紧急登录密码修改
     */
    public static void showEmergencyDialog(Context context) {
        EmergencyTwoDialog.Builder builder = new EmergencyTwoDialog.Builder(context);
        builder.setRight("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setLeft("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 盘亏不提交弹出
     */
    public static void showOneDialog(Context context) {
        OneDialog.Builder builder = new OneDialog.Builder(context);
        builder.setMsg("请完善耗材盘亏原因，再提交数据～");
        builder.setRight("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

//    public static RvDialog.Builder showRvDialog(
//          Activity activity, final Context context,
//          List<BingFindSchedulesBean.PatientInfoVos> patientInfos, String type, int position,
//          List<BoxSizeBean.DevicesBean> mTbaseDevices) {
//        RvDialog.Builder builder = new RvDialog.Builder(activity, context, patientInfos);
//        builder.setMsg("耗材中包含过期耗材，请查看！");
//        builder.setLeft("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int i) {
//                dialog.dismiss();
//            }
//        });
//        builder.setRight("确认", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int i) {
//
//                int checkedPosition = sTableTypeView.mBingOutAdapter.getCheckedPosition();
//                if (type.equals(TEMP_FIRSTBIND)) {//先绑定患者
//                    LogUtils.i("OutBoxBingActivity", "先绑定患者");
//                    if ((patientInfos != null && patientInfos.size() == 0) || patientInfos.get(checkedPosition) == null) {
//                        Toast.makeText(UIUtils.getContext(), "无患者信息，操作无效！", Toast.LENGTH_SHORT).show();
//                    } else {
//                        ContentConsumeOperateFrag.mPause = false;
//                        String operationScheduleId = patientInfos.get(checkedPosition).getOperationScheduleId();
//                        String id = patientInfos.get(checkedPosition).getPatientId();
//                        String name = patientInfos.get(checkedPosition).getPatientName();
//                        String mTempPatientId = patientInfos.get(checkedPosition).getTempPatientId();
//                        String mMedicalId = patientInfos.get(checkedPosition).getMedicalId();
//                        String mSurgeryId = patientInfos.get(checkedPosition).getSurgeryId();
//                        String mHisPatientId = patientInfos.get(checkedPosition).getHisPatientId();
//                        String mOperatingRoomNo = patientInfos.get(checkedPosition).getOperatingRoomNo();
//                        LogUtils.i("OutBoxBingActivity", " name " + name);
////                        String name = ((TextView) sTableTypeView.mRecyclerview.getChildAt(
////                                checkedPosition)
////                                .findViewById(R.id.seven_two)).getText().toString();
////                        String id = ((TextView) sTableTypeView.mRecyclerview.getChildAt(
////                                checkedPosition)
////                                .findViewById(R.id.seven_three)).getText().toString();
//                        EventBusUtils.postSticky(
//                                new Event.EventCheckbox(name, id, mTempPatientId, operationScheduleId, TEMP_FIRSTBIND, position, mTbaseDevices,mMedicalId,mSurgeryId,mHisPatientId,mOperatingRoomNo));
//                    }
//                    dialog.dismiss();
//                } else {//后绑定
//                    if ((patientInfos != null && patientInfos.size() == 0) || patientInfos.get(checkedPosition) == null) {
//                        Toast.makeText(UIUtils.getContext(), "无患者信息，操作无效！", Toast.LENGTH_SHORT).show();
//                    } else {
////                        String name = ((TextView) sTableTypeView.mRecyclerview.getChildAt(
////                                checkedPosition)
////                                .findViewById(R.id.seven_two)).getText().toString();
////                        String id = ((TextView) sTableTypeView.mRecyclerview.getChildAt(
////                                checkedPosition)
////                                .findViewById(R.id.seven_three)).getText().toString();
//                        String operationScheduleId = patientInfos.get(checkedPosition).getOperationScheduleId();
//                        String id = patientInfos.get(checkedPosition).getPatientId();
//                        String name = patientInfos.get(checkedPosition).getPatientName();
//                        String mTempPatientId = patientInfos.get(checkedPosition).getTempPatientId();
//                        String mMedicalId = patientInfos.get(checkedPosition).getMedicalId();
//                        String mSurgeryId = patientInfos.get(checkedPosition).getSurgeryId();
//                        String mHisPatientId = patientInfos.get(checkedPosition).getHisPatientId();
//                        String mOperatingRoomNo = patientInfos.get(checkedPosition).getOperatingRoomNo();
//                        EventBusUtils.postSticky(
//                                new Event.EventCheckbox(name, id, mTempPatientId, operationScheduleId, type, position, mTbaseDevices,mMedicalId,mSurgeryId,mHisPatientId,mOperatingRoomNo));
//                        dialog.dismiss();
//                    }
//                    LogUtils.i("OutBoxBingActivity", "后绑定   " + patientInfos.size() + "type:" + type);
//
//                }
//            }
//        });
//        Log.e("DialogUtils", " RvDialog rvDialog = builder.create();");
//        RvDialog rvDialog = builder.create();
//        rvDialog.show();
//        WindowManager windowManager = activity.getWindowManager();
//        Display display = windowManager.getDefaultDisplay();
//        Window window = rvDialog.getWindow();
//        WindowManager.LayoutParams lp = window.getAttributes();
//        lp.width = (int) (display.getWidth()); //设置宽度
//        window.setBackgroundDrawableResource(android.R.color.transparent);
//        window.setAttributes(lp);
//        return builder;
//    }

    /**
     * 腕带解绑
     *
     * @param context
     * @param title
     */
    public static void showUnRegistDialog(Context context, String title, String date) {
        OneDialog.Builder builder = new OneDialog.Builder(context);
        builder.setMsg(title);
        builder.setRight("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (title.equals("解绑腕带后将无法继续使用，是否确定解绑？")) {
                    NetRequest.getInstance().unRegisterIdCard(date, context, new BaseResult() {
                        @Override
                        public void onSucceed(String result) {
                            LogUtils.i("SHOW", "result   " + result);
                            Gson gson = new Gson();
                            UnRegistBean unRegistBean = gson.fromJson(result, UnRegistBean.class);
                            if (unRegistBean.isOperateSuccess()) {
                                LoginInfoActivity.mIsWaidai = 0;
                                LoginInfoActivity.mSettingIcCardEdit.setText("未绑定");
                                LoginInfoActivity.mSettingIcCardBind.setText("绑定");
                            }
                            Toast.makeText(context, unRegistBean.getMsg(), Toast.LENGTH_SHORT).show();
                            String accountData = SPUtils.getString(context, KEY_ACCOUNT_DATA,
                                    "");
                            LoginResultBean data2 = gson.fromJson(accountData, LoginResultBean.class);
                            data2.getAppAccountInfoVo().setIsWaidai(0);
                            SPUtils.putString(context, KEY_ACCOUNT_DATA, gson.toJson(data2));
                            dialog.dismiss();
                        }
                    });
                } else {
                    dialog.dismiss();
                }
            }
        });

        builder.create().show();

    }

    /**
     * @param context
     * @param title
     * @param mType   1异常类的弹框  黄色   2成功
     * @param nojump  nojump不跳转，out拿出  in 拿入
     * @param bing    是否是绑定病人
     */
    public static NoDialog.Builder showNoDialog(
            final Context context, String title, int mType, final String nojump, final String bing) {
        final NoDialog.Builder builder = new NoDialog.Builder(context, mType, nojump, bing);
        builder.setMsg(title);
        builder.create().show();
        return builder;
    }
    /**
     * @param context
     * @param title 打开柜门
     */
    public static OpenDoorDialog.Builder showOpenDoorDialog(
          final Context context, String title) {
        final OpenDoorDialog.Builder builder = new OpenDoorDialog.Builder(context);
        builder.setMsg(title);
        builder.create().show();
        return builder;
    }

    public static void showStoreDialog(
            Context context, int mNumColumn, int mType, HospNameBean hospNameBean, int mIntentType) {
        StoreRoomDialog.Builder builder = new StoreRoomDialog.Builder(context, mNumColumn, mType,
                hospNameBean, mIntentType);
        if (mType == 104) {
            builder.setTitle("处理选择");
        } else if (mType == 2) {
            builder.setTitle("请选择退货原因");
        } else if (mType == 1) {
            builder.setTitle("请选择库房");
        } else {
            builder.setTitle("请选择目标科室");
        }
        builder.setLeft("", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
            if (mStarts!=null){
                mStarts.start();
            }
                dialog.dismiss();
            }
        });

        builder.create().show();
    }


    /**
     * 出柜关联操作
     * @param context
     * @param hasNext 是否有下一步
     * @param mNumColumn 列数
     * @param mIntentType
     */
    public static void showOutBoxConnectDialog(Context context,boolean hasNext, int mNumColumn,  int mIntentType) {
        OutBoxConnectDialog.Builder builder = new OutBoxConnectDialog.Builder(context,hasNext, mNumColumn, mIntentType);
        builder.setTitle("关联操作");
        builder.setLeft("", (dialog, i) -> {
            if (mStarts != null) {
                mStarts.start();
            }
            dialog.dismiss();
        });

        builder.create().show();
    }

    /**
     * 盘亏原因
     *
     * @param context
     */
    public static void showLossDialog(
            Context context, List<String> strings) {
        LossScuseDialog.Builder builder = new LossScuseDialog.Builder(context, strings);
        builder.setTitle("请选择盘亏原因");

        builder.create().show();
    }

    public static void showTwoDialog(Activity activity, Context context, int mType, String title, String msg) {
        TwoDialog.Builder builder = new TwoDialog.Builder(context, mType);
        if (mType == 1) {
            builder.setTwoMsg(msg);
            builder.setMsg(title);
            builder.setLeft("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.dismiss();
                    activity.finish();
                }
            });
            builder.setRight("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.dismiss();
                    activity.finish();
                }
            });
        } else {
            builder.setTwoMsg(msg);
            builder.setMsg(title);
            builder.setLeft("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.dismiss();
                    activity.finish();
                    activity.startActivity(new Intent(activity, OutFormActivity.class));
                }
            });
            builder.setRight("确认并退出登录", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.dismiss();
                    if (context instanceof Activity) {
                        UIUtils.putOrderId(context);
                        removeAllAct((Activity) context);
                    }
                }
            });
        }
        builder.create().show();
    }

    public static void showOutFormDialog(Context context, int mType, String title, String msg) {
        TwoDialog.Builder builder = new TwoDialog.Builder(context, mType);
            builder.setTwoMsg(msg);
            builder.setMsg(title);
            builder.setLeft("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.dismiss();
                    if (context instanceof Activity){
                        ((Activity) context).finish();
                    }
                }
            });
            builder.setRight("确认并退出登录", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.dismiss();
                    if (context instanceof Activity){
                        UIUtils.putOrderId(context);
                        removeAllAct((Activity) context);
                    }
                }
            });
        builder.create().show();
    }

    public static void showOnePassWordDialog(Context context) {
        OnePassWordDialog.Builder builder = new OnePassWordDialog.Builder(context);
        builder.setRight("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                dialog.dismiss();
            }
        });

        builder.create().show();

    }

    public static OneFingerDialog.Builder showOneFingerDialog(
            Context context,String title, LoginInfoActivity.OnfingerprintBackListener onfingerprintBackListener) {

//	 int[] times = {0};
        OneFingerDialog.Builder builder = new OneFingerDialog.Builder(context);
        builder.setTwoMsg(title);
        builder.setRight("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                onfingerprintBackListener.OnfingerprintBack();
                dialog.dismiss();
//                AllDeviceCallBack.getInstance().initCallBack();
            }
        });
        builder.create().show();
        return builder;
//        Eth002Manager.getEth002Manager().registerCallBack(new Eth002CallBack() {
//            @Override
//            public void onConnectState(String deviceId, boolean isConnect) {
//
//            }
//
//            @Override
//            public void onFingerFea(String deviceId, String fingerFea) {
//                times[0]++;
//                String myfingerFea = fingerFea.trim().replaceAll("\n", "");
//                if (times[0] == 1) {
//                    fingerList.add(myfingerFea);
//                    //					ToastUtils.showShort("请再次按下");
//                } else if (times[0] == 2) {
//                    fingerList.add(myfingerFea);
//                    //					ToastUtils.showShort("请第三次按下");
//                } else if (times[0] == 3) {
//                    fingerList.add(myfingerFea);
//                    //					ToastUtils.showShort("采集成功!请按确定键!");
//                    UIUtils.runInUIThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            builder.setSuccess();
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onFingerRegExcuted(String deviceId, boolean success) {
////		   String type ;
//		   if(success){
////			type ="成功";
//		   }else {
////			type="失败";
//			UIUtils.runInUIThread(()->builder.setError());
//		   }
//
////		   AppendLog("指纹注册命令已执行：设备ID:   " + deviceId + "   ;   操作状态 = " + type);
//            }
//
//            @Override
//            public void onFingerRegisterRet(String deviceId, boolean success, String fingerData) {
////		   String type ;
//		   if(success){
////			type ="成功";
//			fingerList.add(fingerData);
//			UIUtils.runInUIThread(()->builder.setSuccess());
//
//		   }else {
////			type="失败";
//			UIUtils.runInUIThread(()->builder.setError());
//		   }
////		   AppendLog("接收到指纹注册结果：设备ID:   " + deviceId + "   ;   操作状态 = " + type + "   ;   FingerData = " + fingerData);
//
//            }
//
//            @Override
//            public void onIDCard(String deviceId, String idCard) {
//            }
//
//            @Override
//            public void onDoorOpened(String deviceIndentify, boolean success) {
//
//            }
//
//            @Override
//            public void onDoorClosed(String deviceIndentify, boolean success) {
//
//            }
//
//            @Override
//            public void onDoorCheckedState(String deviceIndentify, boolean opened) {
//
//            }
//        });
    }

    public static void showBindIdCardDialog(
            Context context, LoginInfoActivity.OnBindIdCardListener onBindIdCardListener) {
        final String[] mIdCard = {""};
        BindIdCardDialog.Builder builder = new BindIdCardDialog.Builder(context);
        builder.setRight("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                onBindIdCardListener.OnBindIdCard(mIdCard[0]);
                dialog.dismiss();
                AllDeviceCallBack.getInstance().initCallBack();
            }
        });

        builder.create().show();
        Eth002Manager.getEth002Manager().registerCallBack(new Eth002CallBack() {
            @Override
            public void onConnectState(String deviceId, boolean isConnect) {

            }

            @Override
            public void onFingerFea(String deviceId, String fingerFea) {

            }

            @Override
            public void onFingerRegExcuted(String deviceId, boolean success) {

            }

            @Override
            public void onFingerRegisterRet(String deviceId, boolean success, String fingerData) {

            }

            @Override
            public void onIDCard(String deviceId, String idCard) {
                mIdCard[0] = idCard;
                UIUtils.runInUIThread(new Runnable() {
                    @Override
                    public void run() {
                        builder.setSuccess("卡号: " + idCard);
                    }
                });
            }

            @Override
            public void onDoorOpened(String deviceIndentify, boolean success) {

            }

            @Override
            public void onDoorClosed(String deviceIndentify, boolean success) {

            }

            @Override
            public void onDoorCheckedState(String deviceIndentify, boolean opened) {

            }
        });

    }

    public static void showTimeDialog(final Context context, final TextView textView) {
        Date date = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String format = sdf.format(date);
        Log.i("cc", "    " + format);
        TimeSelectorDialog dialog = new TimeSelectorDialog(context);
        //设置标题
        dialog.setTimeTitle("选择时间:  ");
        //显示类型
        dialog.setIsShowtype(TimeConfig.YEAR_MONTH_DAY_HOUR_MINUTE);
        //默认时间
        dialog.setCurrentDate(format);
        //隐藏清除按钮
        dialog.setEmptyIsShow(false);
        //设置起始时间
        dialog.setStartYear(2000);
        dialog.setDateListener(new DateListener() {
            @Override
            public void onReturnDate(
                    String time, int year, int month, int day, int hour, int minute, int isShowType,
                    long times) {
                textView.setText(time);
                textView.setTextColor(context.getResources().getColor(R.color.text_color_3));
            }

            @Override
            public void onReturnDate(String empty) {
            }
        });
        dialog.show();

    }

    public static void showTimeDialog2(final Context context, final TextView textView) {
        Date date = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String format = sdf.format(date);
        Log.i("cc", "    " + format);
        TimeSelectorDialog dialog = new TimeSelectorDialog(context);
        //设置标题
        dialog.setTimeTitle("选择时间:  ");
        //显示类型
        dialog.setIsShowtype(TimeConfig.YEAR_MONTH_DAY_HOUR_MINUTE);
        //默认时间
        dialog.setCurrentDate(format);
        //隐藏清除按钮
        dialog.setEmptyIsShow(false);
        //设置起始时间
        dialog.setStartYear(2000);
        dialog.setIsconfirmCancelable(false);
        dialog.setDateListener(new DateListener() {
            @Override
            public void onReturnDate(
                    String time, int year, int month, int day, int hour, int minute, int isShowType,
                    long times) {
                Date date1 = new Date();
                Date date2 = new Date(year - 1900, month, day, hour, minute);
                if ((date2.getTime() - date1.getTime()) <= 300 * 1000 | (date2.getTime() - date1.getTime()) > (24 * 3600000)) {
                    Toast.makeText(context, "请选择24小时之内的时间", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.setIsconfirmCancelable(true);
                    textView.setText(time);
                    textView.setTextColor(context.getResources().getColor(R.color.text_color_3));
                }
            }

            @Override
            public void onReturnDate(String empty) {
            }
        });
        dialog.show();

    }

    /**
     * 激活医院信息的
     * @param context
     * @param activity
     */
    public static void showRegisteDialog(final Context context, Activity activity) {

        RegisteDialog.Builder builder = new RegisteDialog.Builder(context, activity);
        builder.setLeft("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });

        builder.setOnSettingListener(new RegisteDialog.Builder.SettingListener() {
            @Override
            public void getDialogDate(
                    String deptName, String branchCode, String deptId, String storehouseCode, Dialog dialog) {
                LogUtils.i("RegisteDialog", "deptName  " + deptName);
                LogUtils.i("RegisteDialog", "branchCode  " + branchCode);
                LogUtils.i("RegisteDialog", "deptId  " + deptId);
                LogUtils.i("RegisteDialog", "storehouseCode  " + storehouseCode);
                EventBusUtils.postSticky(
                        new Event.dialogEvent(deptName, branchCode, deptId, storehouseCode,dialog));
            }
        });

        builder.create().show();
//        builder.create().getWindow().setGravity(Gravity.CENTER);
    }

    /*
     * 显示创建临时患者弹窗
     * */
    public static void showCreatTempPatientDialog(final Context context, Activity activity, TempPatientDialog.Builder.SettingListener listener) {

        TempPatientDialog.Builder builder = new TempPatientDialog.Builder(context, activity);
        builder.setLeft("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });

        builder.setOnSettingListener(listener);
        builder.create().show();
    }

    /**
     * 选择关联患者弹窗
     *
     * @param activity
     * @param context
     * @param patientInfos
     * @param onClickBackListener
     */
    public static RvDialog2.Builder showRvDialog2(Activity activity, final Context context, List<BingFindSchedulesBean.PatientInfoVos> patientInfos, PatientConnActivity.OnClickBackListener onClickBackListener) {
        RvDialog2.Builder builder = new RvDialog2.Builder(activity, context, patientInfos);
        builder.setLeft("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });
        builder.setRight("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                for (int x = 0; x < patientInfos.size(); x++) {
                    if (patientInfos.get(x).isSelected()) {
                        onClickBackListener.OnClickBack(x, dialog);
                    }
                }
            }
        });
        RvDialog2 rvDialog = builder.create();
        rvDialog.show();
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window window = rvDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        lp.height = (int) (display.getHeight()); //设置宽度
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(lp);
        return builder;
    }

    /**
     * 设置功率
     *
     * @param context
     */
    public static void showWifiDialog(final Context context) {
        WifiDialog.Builder builder = new WifiDialog.Builder(context);
        builder.create().show();
    }

    /**
     * 设置功率
     *
     * @param context
     */
    public static void showEpcDialog(final Context context) {
        EpcTestDialog.Builder builder = new EpcTestDialog.Builder(context);
        builder.create().show();
    }

    public static LoadingDialog.Builder showLoading(final Context context) {
        LoadingDialog.Builder builder = new LoadingDialog.Builder(context);
        builder.create().show();
        return builder;
    }

    /**
     * 医嘱领用-确实-选择耗材柜
     */
    public static void showSelectOpenCabinetDialog(Context context, List<Movie> list) {
        SelectOpenCabinetDialog.Builder builder = new SelectOpenCabinetDialog.Builder(context, 0);
        builder.setDate(list);
        builder.setRightListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setLeftListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 医嘱领用-确认-查看请领单
     */
    public static void showLookUpDetailedListDialog(Context context, boolean isShowLeftTopView, List<BillStockResultBean.OrderDetailVo> list, OrderSheetBean.RowsBean prePageDate) {
        LookUpDetailedListDialog.Builder builder = new LookUpDetailedListDialog.Builder(context);
        builder.setDate(list);
        builder.setCstNumber(prePageDate.cstNumber);
        builder.setCstType(prePageDate.cstType);
        builder.setPatientName(prePageDate.getPatientName());
        builder.setLeftTopViewShow(isShowLeftTopView);
        builder.setRightListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setLeftListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 异常处理：关联操作人
     */
    public static void showSelectOperatorDialog(Context context, List<ExceptionRecordBean.RowsBean> Rowslist, SelectExceptionOperatorDialog.Builder.OnSelectOperatorListener listener) {
        new SelectExceptionOperatorDialog.Builder(context)
                .setDate(Rowslist)
                .setOnSelectListener(listener).create().show();
    }

    /**
     * 入库统计
     */
    public static InBoxCountDialog.Builder showInBoxCountDialog(Context context, InventoryDto dto,TextView mTimelyRight) {
        InBoxCountDialog.Builder builder = new InBoxCountDialog.Builder(context, dto);
        builder.create().show();
        if (mStarts != null) {
            mStarts.cancel();
            mTimelyRight.setText("确认并退出登录");
        }
        return builder;
    }

    /**
     * 雷达
     */
    public static LoadingDialogX.Builder showRader(Context context) {
        LoadingDialogX.Builder builder = new LoadingDialogX.Builder(context);
        builder.create().show();
        return builder;
    }
}
