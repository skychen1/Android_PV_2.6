package high.rivamed.myapplication.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.rivamed.DeviceManager;
import cn.rivamed.callback.DeviceCallBack;
import cn.rivamed.device.DeviceType;
import cn.rivamed.model.TagInfo;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.activity.LoginInfoActivity;
import high.rivamed.myapplication.bean.BingFindSchedulesBean;
import high.rivamed.myapplication.bean.BoxSizeBean;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.HospNameBean;
import high.rivamed.myapplication.timeutil.DateListener;
import high.rivamed.myapplication.timeutil.TimeConfig;
import high.rivamed.myapplication.timeutil.TimeSelectorDialog;
import high.rivamed.myapplication.views.BindIdCardDialog;
import high.rivamed.myapplication.views.EpcTestDialog;
import high.rivamed.myapplication.views.LoadingDialog;
import high.rivamed.myapplication.views.NoDialog;
import high.rivamed.myapplication.views.OneDialog;
import high.rivamed.myapplication.views.OneFingerDialog;
import high.rivamed.myapplication.views.OnePassWordDialog;
import high.rivamed.myapplication.views.RegisteDialog;
import high.rivamed.myapplication.views.RvDialog;
import high.rivamed.myapplication.views.StoreRoomDialog;
import high.rivamed.myapplication.views.TempPatientDialog;
import high.rivamed.myapplication.views.TwoDialog;
import high.rivamed.myapplication.views.WifiDialog;

import static high.rivamed.myapplication.views.RvDialog.sTableTypeView;

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

    public static RvDialog.Builder showRvDialog(
            Activity activity, final Context context,
            List<BingFindSchedulesBean.PatientInfosBean> patientInfos, String type, int position,
            List<BoxSizeBean.TbaseDevicesBean> mTbaseDevices) {
        RvDialog.Builder builder = new RvDialog.Builder(activity, context, patientInfos);
        builder.setMsg("耗材中包含过期耗材，请查看！");
        builder.setLeft("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });
        builder.setRight("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                if (type.equals("firstBind")) {//先绑定患者
                    if ((sTableTypeView.mRecyclerview.getChildAt(
                            sTableTypeView.mBingOutAdapter.getCheckedPosition()))==null){
                        Toast.makeText(UIUtils.getContext(),"无患者信息，操作无效！",Toast.LENGTH_SHORT).show();
                    }else {
                        String name = ((TextView) sTableTypeView.mRecyclerview.getChildAt(
                                sTableTypeView.mBingOutAdapter.getCheckedPosition())
                                .findViewById(R.id.seven_two)).getText().toString();
                        String id = ((TextView) sTableTypeView.mRecyclerview.getChildAt(
                                sTableTypeView.mBingOutAdapter.getCheckedPosition())
                                .findViewById(R.id.seven_three)).getText().toString();
                        EventBusUtils.postSticky(
                                new Event.EventCheckbox(name, id, "firstBind", position, mTbaseDevices));
                    }
                    dialog.dismiss();
                } else {//后绑定
                    for (int x = 0; x <= patientInfos.size(); x++) {
                        if (sTableTypeView.mCheckStates.get(x)) {
                            RvDialog.patientInfosBean = patientInfos.get(x);
                            EventBusUtils.postSticky(
                                    new Event.EventCheckbox(RvDialog.patientInfosBean.getPatientName(),
                                            RvDialog.patientInfosBean.getPatientId()));
                            String title = "患者绑定成功";
                            DialogUtils.showNoDialog(context, title, 2, "nojump", null);
                            dialog.dismiss();
                        }
                    }
                }
            }
        });
        builder.create().show();
        return builder;
    }

    public static void showOneDialog(Context context, String title) {
        OneDialog.Builder builder = new OneDialog.Builder(context);
        builder.setMsg(title);
        builder.setRight("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                dialog.dismiss();
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
        builder.setLeft("", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                //		Log.i("TT", " nojump  " +nojump);
                //	      if(nojump.equals("out")){
                //		   //TODO:换成关门后触发跳转柜子的扫描界面。拿出
                //		   if (bing==null){  //没有绑定病人
                //			context.startActivity(new Intent(context, OutBoxFoutActivity.class));
                //		   }else {
                //			context.startActivity(new Intent(context, OutBoxBingActivity.class));
                //
                //		   }
                //		}else if (nojump.equals("in")){
                //		   Log.i("TT", " EventAct  " );
                //		   //TODO:换成关门后触发跳转柜子的扫描界面。拿入
                ////		   EventBusUtils.postSticky(new Event.EventAct("all"));
                ////		   Intent intent2 = new Intent(context, InOutBoxTwoActivity.class);
                ////		   context.startActivity(intent2);
                //
                //		}else if (nojump.equals("form")){
                //		   if (bing ==null){
                //			context.startActivity(new Intent(context, OutFormConfirmActivity.class));
                //		   }else {//绑定患者的套餐
                //			context.startActivity(new Intent(context, OutMealBingConfirmActivity.class));
                //		   }
                //
                //		}

                dialog.dismiss();
            }
        });

        builder.create().show();
        return builder;
    }

    public static void showStoreDialog(
            Context context, int mNumColumn, int mType, HospNameBean hospNameBean,int mIntentType) {
        StoreRoomDialog.Builder builder = new StoreRoomDialog.Builder(context, mNumColumn, mType,
                hospNameBean,mIntentType);
        if (mType == 2) {
            builder.setTitle("请选择退货原因");
        } else if (mType == 1) {
            builder.setTitle("请选择库房");
        } else {
            builder.setTitle("请选择目标科室");
        }
        builder.setLeft("", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    public static void showTwoDialog(Context context, int mType, String title, String msg) {
        TwoDialog.Builder builder = new TwoDialog.Builder(context, mType);
        if (mType == 1) {
            builder.setTwoMsg(msg);
            builder.setMsg(title);
            builder.setLeft("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.dismiss();
                }
            });
            builder.setRight("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.dismiss();
                }
            });
        } else {
            builder.setTwoMsg(msg);
            builder.setMsg(title);
            builder.setLeft("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.dismiss();
                }
            });
            builder.setRight("确认并退出登录", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.dismiss();
                }
            });
        }
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

    public static void showOneFingerDialog(
            Context context, LoginInfoActivity.OnfingerprintBackListener onfingerprintBackListener) {
        int[] times = {0};
        List<String> fingerList = new ArrayList<String>();
        OneFingerDialog.Builder builder = new OneFingerDialog.Builder(context);
        builder.setRight("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                onfingerprintBackListener.OnfingerprintBack(fingerList);
                dialog.dismiss();
            }
        });

        builder.create().show();
        DeviceManager.getInstance().RegisterDeviceCallBack(new DeviceCallBack() {
            @Override
            public void OnDeviceConnected(DeviceType deviceType, String deviceIndentify) {

            }

            @Override
            public void OnDeviceDisConnected(DeviceType deviceType, String deviceIndentify) {

            }

            @Override
            public void OnCheckState(DeviceType deviceType, String deviceId, Integer code) {

            }

            @Override
            public void OnIDCard(String deviceId, String idCard) {

            }

            @Override
            public void OnFingerFea(String deviceId, String fingerFea) {
                times[0]++;
                String myfingerFea = fingerFea.trim().replaceAll("\n", "");
                if (times[0] == 1) {
                    fingerList.add(myfingerFea);
                    //					ToastUtils.showShort("请再次按下");
                } else if (times[0] == 2) {
                    fingerList.add(myfingerFea);
                    //					ToastUtils.showShort("请第三次按下");
                } else if (times[0] == 3) {
                    fingerList.add(myfingerFea);
                    //					ToastUtils.showShort("采集成功!请按确定键!");
                    UIUtils.runInUIThread(new Runnable() {
                        @Override
                        public void run() {
                            builder.setSuccess();
                        }
                    });
                }
            }

            @Override
            public void OnFingerRegExcuted(String deviceId, boolean success) {

            }

            @Override
            public void OnFingerRegisterRet(String deviceId, boolean success, String fingerData) {

            }

            @Override
            public void OnDoorOpened(String deviceIndentify, boolean success) {

            }

            @Override
            public void OnDoorClosed(String deviceIndentify, boolean success) {

            }

            @Override
            public void OnDoorCheckedState(String deviceIndentify, boolean opened) {

            }

            @Override
            public void OnUhfScanRet(
                    boolean success, String deviceId, String userInfo, Map<String, List<TagInfo>> epcs) {

            }

            @Override
            public void OnUhfScanComplete(boolean success, String deviceId) {

            }

            @Override
            public void OnGetAnts(String deviceId, boolean success, List<Integer> ants) {

            }

            @Override
            public void OnUhfSetPowerRet(String deviceId, boolean success) {

            }

            @Override
            public void OnUhfQueryPowerRet(String deviceId, boolean success, int power) {

            }
        });
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
            }
        });

        builder.create().show();
        DeviceManager.getInstance().RegisterDeviceCallBack(new DeviceCallBack() {
            @Override
            public void OnDeviceConnected(DeviceType deviceType, String deviceIndentify) {

            }

            @Override
            public void OnDeviceDisConnected(DeviceType deviceType, String deviceIndentify) {

            }

            @Override
            public void OnCheckState(DeviceType deviceType, String deviceId, Integer code) {

            }

            @Override
            public void OnIDCard(String deviceId, String idCard) {
                mIdCard[0] = idCard;
                UIUtils.runInUIThread(new Runnable() {
                    @Override
                    public void run() {
                        builder.setSuccess("卡号: " + idCard);
                    }
                });
            }

            @Override
            public void OnFingerFea(String deviceId, String fingerFea) {

            }

            @Override
            public void OnFingerRegExcuted(String deviceId, boolean success) {

            }

            @Override
            public void OnFingerRegisterRet(String deviceId, boolean success, String fingerData) {

            }

            @Override
            public void OnDoorOpened(String deviceIndentify, boolean success) {

            }

            @Override
            public void OnDoorClosed(String deviceIndentify, boolean success) {

            }

            @Override
            public void OnDoorCheckedState(String deviceIndentify, boolean opened) {

            }

            @Override
            public void OnUhfScanRet(
                    boolean success, String deviceId, String userInfo, Map<String, List<TagInfo>> epcs) {

            }

	   @Override
	   public void OnUhfScanComplete(boolean success, String deviceId) {

	   }

	   @Override
	   public void OnGetAnts(String deviceId, boolean success, List<Integer> ants) {

	   }

	   @Override
	   public void OnUhfSetPowerRet(String deviceId, boolean success) {

	   }

	   @Override
	   public void OnUhfQueryPowerRet(String deviceId, boolean success, int power) {

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
                if ((date2.getTime() - date1.getTime()) <=0|(date2.getTime() - date1.getTime()) >(24* 3600000)) {
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
		   String deptName, String branchCode, String deptCode, String storehouseCode,
		   String operationRoomNo, Dialog dialog) {
	      LogUtils.i("RegisteDialog","deptName  "+deptName);
	      LogUtils.i("RegisteDialog","branchCode  "+branchCode);
	      LogUtils.i("RegisteDialog","deptCode  "+deptCode);
	      LogUtils.i("RegisteDialog","storehouseCode  "+storehouseCode);
	      LogUtils.i("RegisteDialog","operationRoomNo  "+operationRoomNo);
		EventBusUtils.postSticky(
			new Event.dialogEvent(deptName, branchCode, deptCode, storehouseCode,
						    operationRoomNo, dialog));
	   }
	});

	builder.create().show();
   }
    /*
     * 显示创建临时患者弹窗
     * */
    public static void showCreatTempPatientDialog(final Context context, Activity activity) {

        TempPatientDialog.Builder builder = new TempPatientDialog.Builder(context, activity);
        builder.setLeft("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });

        builder.setOnSettingListener(new TempPatientDialog.Builder.SettingListener() {
            @Override
            public void getDialogDate(String userName, String roomNum, String userSex, String idCard, String time, Dialog dialog) {
                EventBusUtils.postSticky(new Event.tempPatientEvent(userName, roomNum, userSex, idCard, time, dialog));
            }
        });
        builder.create().show();
    }
    /**
     * 选择关联患者弹窗
     * @param activity
     * @param context
     * @param patientInfos
     */
    public static void showRvDialog2(Activity activity, final Context context, List<BingFindSchedulesBean.PatientInfosBean> patientInfos) {
        RvDialog.Builder builder = new RvDialog.Builder(activity, context, patientInfos);
        builder.setLeft("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });
        builder.setRight("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                for (int x = 0; x <patientInfos.size(); x++) {
                    if (patientInfos.get(x).isSelected()) {
                        RvDialog.patientInfosBean = patientInfos.get(x);
                        DialogUtils.showNoDialog(context, "关联患者成功!", 2, "form", null);
                        dialog.dismiss();
                    }
                }
            }
        });
        builder.create().show();
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
}
