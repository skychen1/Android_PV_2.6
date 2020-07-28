package high.rivamed.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rivamed.FingerManager;
import com.rivamed.libdevicesbase.base.DeviceInfo;
import com.rivamed.libdevicesbase.base.FunctionCode;
import com.rivamed.libdevicesbase.utils.FilesUtils;
import com.ruihua.libfacerecognitionv3.main.manager.FaceSDKManager;
import com.ruihua.libfacerecognitionv3.main.presenter.FaceManager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.BaseSimpleActivity;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.LoginInfoBean;
import high.rivamed.myapplication.bean.RegisterFingerBean;
import high.rivamed.myapplication.dto.RegisterFingerDto;
import high.rivamed.myapplication.dto.RegisterWandaiDto;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.FileEncoder;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.RxPermissionUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;
import high.rivamed.myapplication.views.LoadingDialog;
import high.rivamed.myapplication.views.OneFingerDialog;

import static com.rivamed.FingerType.TYPE_NET_ZHI_ANG;
import static high.rivamed.myapplication.cont.Constants.FINGER_VERSION;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/18 16:53
 * 描述:        登录信息
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class LoginInfoActivity3 extends BaseSimpleActivity {

   private static final String TAG = "LoginInfoActivity3";
   @BindView(R.id.setting_password)
   TextView  mSettingPassword;
   @BindView(R.id.setting_password_edit)
   TextView  mSettingPasswordEdit;
   @BindView(R.id.setting_revise_password)
   ImageView mSettingRevisePassword;
   @BindView(R.id.setting_fingerprint_edit_one)
   TextView  mSettingFingerprintEditOne;
   @BindView(R.id.setting_fingerprint_bind_one)
   TextView  mSettingFingerprintBindOne;
   @BindView(R.id.setting_fingerprint_edit_two)
   TextView  mSettingFingerprintEditTwo;
   @BindView(R.id.setting_fingerprint_bind_two)
   TextView  mSettingFingerprintBindTwo;
   @BindView(R.id.setting_fingerprint_edit_three)
   TextView  mSettingFingerprintEditThree;
   @BindView(R.id.setting_fingerprint_bind_three)
   TextView  mSettingFingerprintBindThree;
   //   @BindView(R.id.setting_pass_edit)
   //   TextView     mSettingPassEdit;
   //   @BindView(R.id.setting_pass_setting)
   //   TextView     mSettingPassSetting;
   //   @BindView(R.id.setting_pass_ll)
   //   LinearLayout mSettingPassLL;
   @BindView(R.id.top_icon)
   ImageView mTopIcon;
   @BindView(R.id.setting_name)
   TextView  mSettingName;
   @BindView(R.id.setting_face_bind_text)
   TextView  mSettingFaceBindText;
   @BindView(R.id.setting_face_bind_btn)
   TextView  mSettingFaceBindBtn;
   @BindView(R.id.setting_ic_card_text)
   TextView  mSettingIcCardText;
   @BindView(R.id.setting_ic_card_bind)
   TextView  mSettingIcCardBind;
   private LoadingDialog.Builder              mBuilder;
   private String                             mUserId = "";
   private String                             mFaceId = "";
   private LoginInfoBean.AppAccountInfoVoBean mAppAccountInfoVo;
   public  int                                mIsWaidai;
   private LoadingDialog.Builder              mLoading;
   private String                             mFingerTxt;
   private OneFingerDialog.Builder            mOneFingerDialog;
   private String                             mFingerData;
   private int                                mIsFace;
   private String                             mFingerNames;
   private String                             mSex;

   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onEventLoading(Event.EventLoading event) {
	if (event.loading) {
	   if (mLoading == null) {
		mLoading = DialogUtils.showLoading(mContext);
	   } else {
		if (!mLoading.mDialog.isShowing()) {
		   mLoading.create().show();
		}
	   }
	} else {
	   if (mLoading != null) {
		mLoading.mAnimationDrawable.stop();
		mLoading.mDialog.dismiss();
		mLoading = null;
	   }
	}
   }

   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onEventFingerTime(Event.EventFingerTime event) {
	//	EventBusUtils.postSticky(new Event.EventLoading(false));
	if (mOneFingerDialog != null) {
	   mOneFingerDialog.mFingerTime.setText(event.time / 1000 + "");
	}
   }

   /**
    * 指纹注册弹出窗
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onEventFingerDialog(Event.EventFingerReg event) {
	//	EventBusUtils.postSticky(new Event.EventLoading(false));
	if (mOneFingerDialog != null) {
	   if (event.msg == 0) {
		mOneFingerDialog.mFingerTitle2.setText("请抬起手指");
	   } else {
		mOneFingerDialog.mFingerTitle2.setText("将识别手指2次，请勿离开");
	   }

	}
   }

   /**
    * 接收指纹合并后的数据
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onEventFingerReg(Event.EventFingerRegEnter event) {
	mFingerData = event.fingerData;
	if (event.type == 0) {
	   if (mOneFingerDialog != null) {
		mOneFingerDialog.setSuccess();
	   }
	} else {
	   if (mOneFingerDialog != null) {
		mOneFingerDialog.setError(event.msg);
	   }
	}
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	super.initDataAndEvent(savedInstanceState);
	//	mSettingPassLL.setVisibility(View.GONE);//隐藏底部紧急登录修改密码
	FingerManager.getManager().connectFinger(this, TYPE_NET_ZHI_ANG);
	mBaseTabBack.setVisibility(View.VISIBLE);
	mBaseTabTvTitle.setVisibility(View.VISIBLE);
	mBaseTabTvTitle.setText("登录设置");

	initData();
   }

   private void initData() {
	NetRequest.getInstance().userLoginInfo(this, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LoginInfoBean loginInfoBean = mGson.fromJson(result, LoginInfoBean.class);
		if (loginInfoBean.isOperateSuccess()) {
		   setInfoDate(loginInfoBean);
		} else {
		   ToastUtils.showShortToast("数据返回异常");
		}
	   }

	   @Override
	   public void onError(String result) {

	   }
	});
   }

   private void setInfoDate(LoginInfoBean loginInfoBean) {
	mAppAccountInfoVo = loginInfoBean.getAppAccountInfoVo();
	mUserId = mAppAccountInfoVo.getUserId();
	mFaceId = mAppAccountInfoVo.getFaceId();
	mIsWaidai = mAppAccountInfoVo.getIsWaidai();
	mIsFace = mAppAccountInfoVo.getIsFace();
	mSex = mAppAccountInfoVo.getSex();
	mSettingName.setText(mAppAccountInfoVo.getUserName());
	String fingerNames = mAppAccountInfoVo.getFingerNames();

	if (mIsFace == 1) {
	   mSettingFaceBindText.setText("已绑定");
	   mSettingFaceBindText.setTextColor(getResources().getColor(R.color.color_text_g));
	   mSettingFaceBindBtn.setText("重新绑定");
	} else {
	   mSettingFaceBindText.setText("未绑定");
	   mSettingFaceBindText.setTextColor(getResources().getColor(R.color.text_color_6));
	   mSettingFaceBindBtn.setText("绑定");
	}
	setFingerType(fingerNames);
	if (mIsWaidai == 1) {
	   //已绑定
	   mSettingIcCardText.setText("已绑定");
	   mSettingIcCardText.setTextColor(getResources().getColor(R.color.color_text_g));
	   mSettingIcCardBind.setText("解除绑定");
	} else {
	   //指纹未绑定
	   mSettingIcCardText.setText("未绑定");
	   mSettingIcCardText.setTextColor(getResources().getColor(R.color.text_color_6));
	   mSettingIcCardBind.setText("绑定");
	}
	if (mSex != null && mSex.equals("男")) {
	   Glide.with(this).load(R.mipmap.hccz_mrtx_nan).error(R.mipmap.hccz_mrtx_nan).into(mTopIcon);
	} else {
	   Glide.with(this).load(R.mipmap.hccz_mrtx_nv).error(R.mipmap.hccz_mrtx_nv).into(mTopIcon);
	}
   }


   private void setFingerType(String fingerNames) {
	if (fingerNames != null && fingerNames.contains("指纹1")) {
	   mSettingFingerprintEditOne.setTextColor(getResources().getColor(R.color.color_text_g));
	   mSettingFingerprintBindOne.setText("重新绑定");
	} else {
	   mSettingFingerprintEditOne.setTextColor(getResources().getColor(R.color.text_color_6));
	   mSettingFingerprintBindOne.setText("绑定");
	}
	if (fingerNames != null && fingerNames.contains("指纹2")) {
	   mSettingFingerprintEditTwo.setTextColor(getResources().getColor(R.color.color_text_g));
	   mSettingFingerprintBindTwo.setText("重新绑定");
	} else {
	   mSettingFingerprintEditTwo.setTextColor(getResources().getColor(R.color.text_color_6));
	   mSettingFingerprintBindTwo.setText("绑定");
	}
	if (fingerNames != null && fingerNames.contains("指纹3")) {
	   mSettingFingerprintEditThree.setTextColor(getResources().getColor(R.color.color_text_g));
	   mSettingFingerprintBindThree.setText("重新绑定");
	} else {
	   mSettingFingerprintEditThree.setTextColor(getResources().getColor(R.color.text_color_6));
	   mSettingFingerprintBindThree.setText("绑定");
	}
   }

   @Override
   protected int getContentLayoutId() {
	return R.layout.setting_logininfo_layout3;
   }

   @OnClick({R.id.setting_face_bind_btn, R.id.setting_ic_card_bind, R.id.base_tab_back,
	   R.id.setting_revise_password, R.id.setting_fingerprint_bind_one,
	   R.id.setting_fingerprint_bind_two, R.id.setting_fingerprint_bind_three})
   public void onViewClicked(View view) {
	super.onViewClicked(view);
	switch (view.getId()) {
	   //	   case R.id.setting_pass_setting://紧急登录密码修改
	   //		DialogUtils.showEmergencyDialog(mContext);
	   //		break;
	   case R.id.setting_face_bind_btn:
		onFacePhoto();
		break;
	   case R.id.setting_revise_password:
		DialogUtils.showOnePassWordDialog(mContext);
		break;
	   case R.id.setting_fingerprint_bind_one:
		mFingerTxt = "指纹1";
		setEth002FingerReg();
		break;
	   case R.id.setting_fingerprint_bind_two:
		mFingerTxt = "指纹2";
		setEth002FingerReg();
		break;
	   case R.id.setting_fingerprint_bind_three:
		mFingerTxt = "指纹3";
		setEth002FingerReg();
		break;
	   case R.id.setting_ic_card_bind:
		if (mIsWaidai == 0) {
		   DialogUtils.showBindIdCardDialog3(mContext, new OnBindIdCardListener() {
			@Override
			public void OnBindIdCard(String idCard) {
			   if (!TextUtils.isEmpty(idCard)) {
				bindIdCrad(idCard);
			   } else {
				ToastUtils.showShort("采集失败,请重试");
			   }
			}
		   });
		} else {
		   RegisterWandaiDto dto = new RegisterWandaiDto();
		   RegisterWandaiDto.UserFeatureInfoBean bean = new RegisterWandaiDto.UserFeatureInfoBean();
		   bean.setUserId(mUserId);
		   bean.setType("2");
		   dto.setUserFeatureInfo(bean);
		   DialogUtils.showUnRegistDialog3(this, "解绑腕带后将无法继续使用，是否确定解绑？", mGson.toJson(dto),
							    new SetEditTextListener() {
								 @Override
								 public void OnSetEditText() {
								    mIsWaidai = 0;
								    mSettingIcCardText.setText("未绑定");
								    mSettingIcCardText.setTextColor(
									    getResources().getColor(
										    R.color.text_color_6));
								    mSettingIcCardBind.setText("绑定");
								 }
							    });
		}
		break;
	}
   }

   /**
    * 发起注册指纹
    */
   private void setEth002FingerReg() {
	List<DeviceInfo> deviceInfos = FingerManager.getManager().getConnectedFinger();
	if (deviceInfos == null || deviceInfos.size() == 0) {
	   ToastUtils.showShortToast("设备未连接，请稍后重试！");
	}
	for (DeviceInfo info : deviceInfos) {
	   String identification = info.getIdentification();
	   Log.i("appSatus", "appSatus   " + identification);
	   FingerManager.getManager().stopReadFinger(identification);
	   int ret = FingerManager.getManager()
		   .startRegisterFinger(identification, 10 * 1000, FilesUtils.getFilePath(mContext));
	   Log.i("appSatus", "ret   " + ret);
	   if (ret == FunctionCode.SUCCESS) {
		mOneFingerDialog = DialogUtils.showOneFingerDialog3(mContext, mFingerTxt,
										   new OnfingerprintBackListener() {
											@Override
											public void OnfingerprintBack() {
											   Log.i("fadeee",
												   "OnfingerprintBack   ");
											   if (mOneFingerDialog.mDialogBtn
												   .getText()
												   .equals("重试")) {
												mOneFingerDialog.mErrorText.setVisibility(
													View.GONE);
												mOneFingerDialog.mDialog.dismiss();
												setEth002FingerReg();

											   } else {
												List<String> list = new ArrayList<>();
												if (mFingerData != null) {
												   list.add(mFingerData);
												}
												Log.i("fadeee",
													"list.size()   " +
													list.size());
												if (list.size() == 1) {
												   bindFingerPrint(list,identification);
												} else {
												   ToastUtils.showShort(
													   "采集失败,请重试");
												}
												mOneFingerDialog.mDialog.dismiss();
											   }

											}
										   });
	   } else if (ret == FunctionCode.DEVICE_BUSY) {
		ToastUtils.showShortToast("设备正在运行，请稍后重试！");
		FingerManager.getManager().stopRegisterFinger(identification);
	   } else {
		ToastUtils.showShortToast("操作失败，请重试！");
	   }
	}

   }

   /*
    * 腕带绑定
    * */
   private void bindIdCrad(String idCard) {
	mBuilder = DialogUtils.showLoading(mContext);
	RegisterWandaiDto dto = new RegisterWandaiDto();
	RegisterWandaiDto.UserFeatureInfoBean bean = new RegisterWandaiDto.UserFeatureInfoBean();
	bean.setUserId(mUserId);
	bean.setData(idCard);
	bean.setType("2");
	dto.setUserFeatureInfo(bean);
	NetRequest.getInstance().registerIdCard(mGson.toJson(dto), this, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG, "result   " + result);
		try {
		   RegisterFingerBean data = mGson.fromJson(result, RegisterFingerBean.class);
		   if (data.isOperateSuccess()) {
			ToastUtils.showShort(data.getMsg());
			//指纹未绑定
			mSettingIcCardText.setText("已绑定");
			mSettingIcCardText.setTextColor(getResources().getColor(R.color.color_text_g));
			mSettingIcCardBind.setText("解除绑定");
			mIsWaidai = 1;
		   } else {
			ToastUtils.showShort(data.getMsg());
		   }
		} catch (Exception e) {
		   e.printStackTrace();
		   ToastUtils.showShort("绑定失败");
		}
		mBuilder.mDialog.dismiss();
	   }

	   @Override
	   public void onError(String result) {
		super.onError(result);
		mBuilder.mDialog.dismiss();
		ToastUtils.showShort("绑定失败");
	   }
	});

   }

   /*
    * 绑定指纹
    * */
   private void bindFingerPrint(List<String> list, String identification) {
	mBuilder = DialogUtils.showLoading(mContext);
	RegisterFingerDto dto = new RegisterFingerDto();
	List<RegisterFingerDto.UserFeatureInfosBean> userFeatureInfos = new ArrayList<>();
	for (int i = 0; i < list.size(); i++) {
	   RegisterFingerDto.UserFeatureInfosBean bean = new RegisterFingerDto.UserFeatureInfosBean();
	   bean.setUserId(mUserId);
	   bean.setType("1");
	   bean.setFeatureName(mFingerTxt);
	   bean.setData(list.get(i));
	   userFeatureInfos.add(bean);
	}
	dto.setDeviceType(FINGER_VERSION);
	dto.setUserFeatureInfos(userFeatureInfos);
	NetRequest.getInstance().registerFinger(mGson.toJson(dto), this, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG, "result   " + result);
		try {
		   RegisterFingerBean data = mGson.fromJson(result, RegisterFingerBean.class);
		   if (data.isOperateSuccess()) {
			ToastUtils.showShort(data.getMsg());
			//指纹未绑定
			String fingerNames = data.getAppAccountInfoVo().getFingerNames();
			setFingerType(fingerNames);
		   } else {
			ToastUtils.showShort(data.getMsg());
		   }

		} catch (Exception e) {
		   e.printStackTrace();
		   ToastUtils.showShort("绑定失败");
		}
		FingerManager.getManager().stopRegisterFinger(identification);
		mBuilder.mDialog.dismiss();
	   }

	   @Override
	   public void onError(String result) {
		super.onError(result);
		mBuilder.mDialog.dismiss();
		ToastUtils.showShort("绑定失败");
	   }
	});
   }

//	private String faceImagePath;

	void onFacePhoto() {
		//绑定人脸照
		RxPermissionUtils.checkCameraPermission(this, hasPermission -> {
			if (hasPermission) {
				int initStatus = FaceSDKManager.initStatus;
				if (initStatus == FaceSDKManager.SDK_UNACTIVATION) {
					ToastUtils.showShortToast("SDK还未激活，请先激活");
				} else if (initStatus == FaceSDKManager.SDK_INIT_FAIL) {
					ToastUtils.showShortToast("SDK初始化失败，请重新激活初始化");
				} else if (initStatus == FaceSDKManager.SDK_INIT_SUCCESS) {
					ToastUtils.showShortToast("SDK正在加载模型，请稍后再试");
				} else if (initStatus == FaceSDKManager.SDK_MODEL_LOAD_SUCCESS) {
				   Log.i("faceddddd","status    --------------");
					FaceManager.getManager().startActivityFaceRegister(this, mFaceId, mFaceId,(code, msg) -> {
						//其他信息提示
								ToastUtils.showShort(msg);
						Log.e("Face", "error callback: "+code+":::" + msg);
					});
				}
			}
		});
	}

   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);
	FaceManager.getManager().onActivityResultIn(requestCode, resultCode, data, (code, msg) -> {
	   //这里返回注册人脸的图片路径，此处上传至服务器
	   //人脸照结果
	   Log.e("Face", "onActivityResult success: " + msg);
	   if (code == 100) {
		if (TextUtils.isEmpty(msg)) {
		   return;
		}
		bindFace(msg);
	   }
	});
   }

   public void bindFace(String path) {
	try {
	   String base64 = FileEncoder.encodeFileToBase64String(path);
	   NetRequest.getInstance().bindFace(base64, this, new BaseResult() {
		@Override
		public void onSucceed(String result) {
		   //刪除本地缓存人脸照
		   new File(path).delete();
		   UIUtils.runInUIThread(() -> {
			ToastUtils.showShortToast("人脸绑定成功");
			mSettingFaceBindText.setText("已绑定");
			mSettingFaceBindText.setTextColor(getResources().getColor(R.color.color_text_g));
			mSettingFaceBindBtn.setText("重新绑定");
		   });
		}

		@Override
		public void onError(String result) {
		   ToastUtils.showShort(result);
		}
	   });
	} catch (Exception e) {
	   e.printStackTrace();
	}
   }

//提供接口
public interface OnfingerprintBackListener {

   void OnfingerprintBack();
}

public interface OnBindIdCardListener {

   void OnBindIdCard(String idCard);
}

//提供接口
public interface SetEditTextListener {

   void OnSetEditText();

}

   @Override
   protected void onDestroy() {
	super.onDestroy();
	if (mBuilder != null) {
	   if (mBuilder.mHandler != null) {
		mBuilder.mHandler.removeCallbacksAndMessages(null);
	   }
	   mBuilder.mDialog.dismiss();
	   mBuilder = null;
	}
   }
}
