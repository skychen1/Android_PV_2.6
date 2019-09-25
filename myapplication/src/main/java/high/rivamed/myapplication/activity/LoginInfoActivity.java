package high.rivamed.myapplication.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rivamed.libdevicesbase.base.FunctionCode;
import com.ruihua.face.recognition.FaceManager;
import com.ruihua.face.recognition.config.FaceCode;
import com.ruihua.face.recognition.ui.RgbDetectActivity;
import com.ruihua.face.recognition.utils.GlobalFaceTypeModel;
import com.ruihua.face.recognition.utils.PreferencesUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.BaseSimpleActivity;
import high.rivamed.myapplication.bean.LoginResultBean;
import high.rivamed.myapplication.bean.RegisterFingerBean;
import high.rivamed.myapplication.dto.RegisterFingerDto;
import high.rivamed.myapplication.dto.RegisterWandaiDto;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.FileEncoder;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.MusicPlayer;
import high.rivamed.myapplication.utils.RxPermissionUtils;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;
import high.rivamed.myapplication.views.LoadingDialog;
import high.rivamed.myapplication.views.SettingPopupWindow;
import high.rivamed.myapplication.views.TwoDialog;

import static high.rivamed.myapplication.cont.Constants.KEY_ACCOUNT_DATA;
import static high.rivamed.myapplication.cont.Constants.KEY_ACCOUNT_ID;
import static high.rivamed.myapplication.cont.Constants.KEY_ACCOUNT_NAME;
import static high.rivamed.myapplication.cont.Constants.KEY_USER_NAME;
import static high.rivamed.myapplication.cont.Constants.KEY_USER_SEX;
import static high.rivamed.myapplication.utils.UIUtils.removeAllAct;

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
public class LoginInfoActivity extends BaseSimpleActivity {

   private static final String TAG = "LoginInfoActivity";
   @BindView(R.id.setting_password)
   TextView     mSettingPassword;
   @BindView(R.id.setting_password_edit)
   TextView     mSettingPasswordEdit;
   @BindView(R.id.setting_revise_password)
   ImageView    mSettingRevisePassword;
   @BindView(R.id.setting_fingerprint)
   TextView     mSettingFingerprint;
   @BindView(R.id.setting_fingerprint_edit)
   TextView     mSettingFingerprintEdit;
   @BindView(R.id.setting_fingerprint_bind)
   TextView     mSettingFingerprintBind;
   @BindView(R.id.setting_ic_card)
   TextView     mSettingIcCard;
   @BindView(R.id.setting_pass_edit)
   TextView     mSettingPassEdit;
   @BindView(R.id.setting_pass_setting)
   TextView     mSettingPassSetting;
   @BindView(R.id.setting_pass_ll)
   LinearLayout mSettingPassLL;
   TextView mSettingIcCardEdit;
   TextView mSettingIcCardBind;
   @BindView(R.id.top_icon)
   ImageView mTopIcon;
   private LoadingDialog.Builder mBuilder;
   private String mUserId = "";
   private       LoginResultBean.AppAccountInfoVoBean mAppAccountInfoVo;
   public int                                  mIsWaidai;
	private boolean isTakeFacePhoto;

	@Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	super.initDataAndEvent(savedInstanceState);
	mSettingPassLL.setVisibility(View.GONE);//隐藏底部紧急登录修改密码
	mSettingIcCardBind = findViewById(R.id.setting_ic_card_bind);
	mSettingIcCardEdit = findViewById(R.id.setting_ic_card_edit);
	mBaseTabBack.setVisibility(View.VISIBLE);
	mBaseTabTvTitle.setVisibility(View.VISIBLE);
	mBaseTabTvTitle.setText("登录设置");
	mBaseTabTvName.setText(SPUtils.getString(UIUtils.getContext(), KEY_USER_NAME));
	if (SPUtils.getString(UIUtils.getContext(), KEY_USER_SEX) != null &&
	    SPUtils.getString(UIUtils.getContext(), KEY_USER_SEX).equals("男")) {
	   Glide.with(this)
		   .load(R.mipmap.hccz_mrtx_nan)
		   .error(R.mipmap.hccz_mrtx_nan)
		   .into(mBaseTabIconRight);
	} else {
	   Glide.with(this)
		   .load(R.mipmap.hccz_mrtx_nv)
		   .error(R.mipmap.hccz_mrtx_nv)
		   .into(mBaseTabIconRight);
	}
	initData();
   }

   @Override
   protected void onPause() {
	super.onPause();
	if (!isTakeFacePhoto)
	finish();
   }

	@Override
	protected void onResume() {
		super.onResume();
		isTakeFacePhoto = false;
	}

	private void initData() {
	try {
	   String accountData = SPUtils.getString(getApplicationContext(), KEY_ACCOUNT_DATA, "");
	   LoginResultBean data = mGson.fromJson(accountData, LoginResultBean.class);
	   mAppAccountInfoVo = data.getAppAccountInfoVo();
	   mUserId = mAppAccountInfoVo.getUserId();
	   mIsWaidai = mAppAccountInfoVo.getIsWaidai();
	   if (mAppAccountInfoVo.getIsFinger() == 0) {
		//指纹未绑定
		mSettingFingerprintEdit.setText("未绑定");
		mSettingFingerprintBind.setText("绑定");
	   } else {
		//已绑定
		mSettingFingerprintEdit.setText("已绑定");
		mSettingFingerprintBind.setText("重新绑定");
	   }
	   if (mAppAccountInfoVo.getIsEmergency() == 0) {
		//紧急登录未绑定
		mSettingPassEdit.setText("未设置");
		mSettingPassSetting.setText("设置");
	   } else {
		//已绑定
		mSettingPassEdit.setText("已设置");
		mSettingPassSetting.setText("设置");
	   }
	   if (mIsWaidai == 0) {
		//指纹未绑定
		mSettingIcCardEdit.setText("未绑定");
		mSettingIcCardBind.setText("绑定");
	   } else {
		//已绑定
		mSettingIcCardEdit.setText("已绑定");
		mSettingIcCardBind.setText("解除绑定");
	   }
	   if (SPUtils.getString(UIUtils.getContext(), KEY_USER_SEX) != null &&
		 SPUtils.getString(UIUtils.getContext(), KEY_USER_SEX).equals("男")) {
		Glide.with(this)
			.load(R.mipmap.hccz_mrtx_nan)
			.error(R.mipmap.hccz_mrtx_nan)
			.into(mTopIcon);
	   } else {
		Glide.with(this)
			.load(R.mipmap.hccz_mrtx_nv)
			.error(R.mipmap.hccz_mrtx_nv)
			.into(mTopIcon);
	   }
	} catch (Exception e) {
	   e.printStackTrace();
	}
   }

   @Override
   protected int getContentLayoutId() {
	return R.layout.setting_logininfo_layout;
   }

   @OnClick({R.id.setting_ic_card_bind, R.id.base_tab_tv_name, R.id.base_tab_icon_right,
	   R.id.base_tab_tv_outlogin, R.id.base_tab_btn_msg, R.id.base_tab_back,
	   R.id.setting_revise_password, R.id.setting_fingerprint_bind, R.id.setting_pass_setting})
   public void onViewClicked(View view) {
	switch (view.getId()) {
	   case R.id.base_tab_icon_right:
	   case R.id.base_tab_tv_name:
		mPopupWindow = new SettingPopupWindow(mContext);
		mPopupWindow.showPopupWindow(view);
		mPopupWindow.setmItemClickListener(new SettingPopupWindow.OnClickListener() {
		   @Override
		   public void onItemClick(int position) {
			switch (position) {
			   case 0:
				startActivity(new Intent(LoginInfoActivity.this, MyInfoActivity.class));
				break;
			   case 1:
				startActivity(new Intent(LoginInfoActivity.this, LoginInfoActivity.class));
				break;
			}
		   }
		});
		break;
	   case R.id.base_tab_tv_outlogin:
		TwoDialog.Builder builder = new TwoDialog.Builder(mContext, 1);
		builder.setTwoMsg("您确认要退出登录吗?");
		builder.setMsg("温馨提示");
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
			removeAllAct(LoginInfoActivity.this);
			MusicPlayer.getInstance().play(MusicPlayer.Type.LOGOUT_SUC);
		   }
		});
		builder.create().show();
		break;
	   case R.id.setting_pass_setting://紧急登录密码修改
		DialogUtils.showEmergencyDialog(mContext);
		break;
	   case R.id.base_tab_back:
		finish();
		break;
	   case R.id.setting_revise_password:
		DialogUtils.showOnePassWordDialog(mContext);
		break;
	   case R.id.setting_fingerprint_bind:
		DialogUtils.showOneFingerDialog(mContext, new OnfingerprintBackListener() {
		   @Override
		   public void OnfingerprintBack(List<String> list) {
			if (list.size() == 3) {
			   bindFingerPrint(list);
			} else {
			   ToastUtils.showShort("采集失败,请重试");
			}
		   }
		});
		break;
	   case R.id.setting_ic_card_bind:
		if (mIsWaidai == 0) {
		   DialogUtils.showBindIdCardDialog(mContext, new OnBindIdCardListener() {
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
		   DialogUtils.showUnRegistDialog(this, "解绑腕带后将无法继续使用，是否确定解绑？", mGson.toJson(dto),
							    new SetEditTextListener() {
								 @Override
								 public void OnSetEditText() {
								    mIsWaidai = 0;
								    mSettingIcCardEdit.setText("未绑定");
								    mSettingIcCardBind.setText("绑定");
								 }
							    });
		}
		break;
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
			mSettingIcCardEdit.setText("已绑定");
			mSettingIcCardBind.setText("解除绑定");
			String accountData = SPUtils.getString(getApplicationContext(), KEY_ACCOUNT_DATA,
									   "");
			LoginResultBean data2 = mGson.fromJson(accountData, LoginResultBean.class);
			data2.getAppAccountInfoVo().setIsWaidai(1);
			SPUtils.putString(getApplicationContext(), KEY_ACCOUNT_DATA, mGson.toJson(data2));
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
   private void bindFingerPrint(List<String> list) {
	mBuilder = DialogUtils.showLoading(mContext);
	RegisterFingerDto dto = new RegisterFingerDto();
	List<RegisterFingerDto.UserFeatureInfosBean> userFeatureInfos = new ArrayList<>();
	for (int i = 0; i < list.size(); i++) {
	   RegisterFingerDto.UserFeatureInfosBean bean = new RegisterFingerDto.UserFeatureInfosBean();
	   bean.setUserId(mUserId);
	   bean.setType("1");
	   bean.setData(list.get(i));
	   userFeatureInfos.add(bean);
	}
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
			mSettingFingerprintEdit.setText("已绑定");
			mSettingFingerprintBind.setText("重新绑定");

			String accountData = SPUtils.getString(getApplicationContext(), KEY_ACCOUNT_DATA, "");
			LoginResultBean data2 = mGson.fromJson(accountData, LoginResultBean.class);
			data2.getAppAccountInfoVo().setIsFinger(1);
			SPUtils.putString(getApplicationContext(), KEY_ACCOUNT_DATA, mGson.toJson(data2));
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

	private String faceImagePath;

	@OnClick(R.id.top_icon)
	void onFacePhoto() {
		//绑定人脸照
		// TODO: 2019/6/14 检测权限申请
		RxPermissionUtils.checkCameraPermission(this, hasPermission -> {
			if (hasPermission) {
				int initStatus = FaceManager.getManager().getInitStatus();
				if (initStatus == FaceCode.SDK_NOT_ACTIVE) {
					ToastUtils.showShort("人脸识别SDK还未激活，请先激活");
					return;
				} else if (initStatus == FaceCode.SDK_NOT_INIT) {
					ToastUtils.showShort("人脸识别SDK还未初始化，请先初始化");
					return;
				} else if (initStatus == FaceCode.SDK_INITING) {
					ToastUtils.showShort("人脸识别SDK正在初始化，请稍后再试");
					return;
				} else if (initStatus == FaceCode.SDK_INIT_FAIL) {
					ToastUtils.showShort("人脸识别SDK初始化失败，请重新初始化");
					return;
				}
				faceImagePath = "";
				int type = PreferencesUtil.getInt(GlobalFaceTypeModel.TYPE_LIVENSS, GlobalFaceTypeModel.TYPE_NO_LIVENSS);
				if (type == GlobalFaceTypeModel.TYPE_NO_LIVENSS || type == GlobalFaceTypeModel.TYPE_RGB_LIVENSS) {
					isTakeFacePhoto=true;
					FaceManager.getManager().getFacePicture(LoginInfoActivity.this, SPUtils.getString(UIUtils.getContext(), KEY_ACCOUNT_ID)+".jpg");
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.e(TAG, "onActivityResult resultCode : :"+resultCode+",requestCode : :"+requestCode );
		if (resultCode == RgbDetectActivity.CODE_PICK_PHOTO && data != null) {
			//人脸照结果
			faceImagePath = data.getStringExtra(RgbDetectActivity.FILE_PATH);
			Log.e(TAG, "onActivityResult: "+faceImagePath );
			if(TextUtils.isEmpty(faceImagePath))
				return;
			bindFace(faceImagePath);
		}
	}

	public void bindFace(String path) {
		try {
			String base64 = FileEncoder.encodeFileToBase64String(path);
			NetRequest.getInstance().bindFace(base64, this, new BaseResult() {
				@Override
				public void onSucceed(String result) {
					// TODO 此处应该先判断是否已经绑定人脸照且已经本地注册过了人脸，需要删除后再重新注册
					//更新：需要先删除本地已注册人脸，再重新注册
					if (FaceManager.getManager().getUserById(mUserId)!=null) {
						boolean b = FaceManager.getManager().deleteFace(mUserId);
						LogUtils.d(TAG, "删除人脸底照："+b+",userId：" + mUserId);
					}
					FaceManager.getManager().registerFace(mUserId,
							SPUtils.getString(UIUtils.getContext(), KEY_ACCOUNT_NAME),
							faceImagePath, (code, msg) -> {
								LogUtils.d("Face", "人脸注册结果：：code=" + code + ":::msg=" + msg);
								if (code== FunctionCode.SUCCESS){
									//刪除本地缓存人脸照
									new File(faceImagePath).delete();
									ToastUtils.showShortToast("人脸绑定成功");
								}
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

	void OnfingerprintBack(List<String> list);
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
	if (mBuilder!=null){
	   if (mBuilder.mHandler!=null){
		mBuilder.mHandler.removeCallbacksAndMessages(null);
	   }
	   mBuilder.mDialog.dismiss();
	   mBuilder=null;
	}
   }
}
