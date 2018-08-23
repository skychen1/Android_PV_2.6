package high.rivamed.myapplication.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.OnClick;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.App;
import high.rivamed.myapplication.base.BaseTimelyActivity;
import high.rivamed.myapplication.bean.BingFindSchedulesBean;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;
import high.rivamed.myapplication.views.SettingPopupWindow;
import high.rivamed.myapplication.views.TwoDialog;

import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_HCCZ_BING;
import static high.rivamed.myapplication.cont.Constants.KEY_ACCOUNT_ID;
import static high.rivamed.myapplication.cont.Constants.SAVE_STOREHOUSE_CODE;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/28 19:59
 * 描述:       拿出  绑定病人
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class OutBoxBingActivity extends BaseTimelyActivity {

   private static final String TAG = "OutBoxBingActivity";
   private List<BingFindSchedulesBean.PatientInfosBean> mPatientInfos;
   private String                                       mRvEventString;
   private int                                          mIntentType;

   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onEventBing(Event.EventCheckbox event) {
	String patient = event.mString;
	Log.i("ff", "mMovie  " + patient);
	if (event.type != null && event.type.equals("firstBind")) {

	} else {
	   if (patient != null) {
		for (int i = 0; i < mTCstInventoryVos.size(); i++) {
		   mTCstInventoryVos.get(i).setPatientName(patient);
		   mTCstInventoryVos.get(i).setPatientId(event.id);
		}
		mTimelyLeft.setEnabled(true);
		mTimelyRight.setEnabled(true);
		mTypeView.mAfterBingAdapter.notifyDataSetChanged();
	   }
	}

   }

   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onRvEvent(Event.EventString event) {
	mRvEventString = event.mString;
	loadBingDate(mRvEventString);
   }

   @Override
   public int getCompanyType() {
	super.my_id = ACT_TYPE_HCCZ_BING;
	return my_id;
   }

   @OnClick({R.id.base_tab_tv_name, R.id.base_tab_icon_right, R.id.base_tab_btn_msg,
	   R.id.base_tab_back, R.id.timely_left, R.id.timely_right, R.id.timely_start_btn_right,
	   R.id.ly_bing_btn_right})
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
				mContext.startActivity(new Intent(mContext, MyInfoActivity.class));
				break;
			   case 1:
				mContext.startActivity(new Intent(mContext, LoginInfoActivity.class));
				break;
			   case 2:
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
					mContext.startActivity(new Intent(mContext, LoginActivity.class));
					App.getInstance().removeALLActivity_();
					dialog.dismiss();
				   }
				});
				builder.create().show();
				break;
			}
		   }
		});
		break;
	   case R.id.base_tab_btn_msg:
		break;
	   case R.id.base_tab_back:
		finish();
		break;
	   case R.id.timely_start_btn_right:
		break;
	   case R.id.timely_left:
		if (UIUtils.isFastDoubleClick()) {
		   return;
		} else {
		   if (mTCstInventoryDto.getBindType() != null) {//先绑定患者
			mIntentType = 1;//确认
			loadBingFistDate(mIntentType);
		   } else {//后绑定的未绑定
			int mType = 1;//1.8.3未绑定
			DialogUtils.showTwoDialog(mContext, mType, "您还有未绑定患者的耗材，确认领用吗？", "耗材未绑定患者");
		   }
		}
		break;
	   case R.id.timely_right:
		if (UIUtils.isFastDoubleClick()) {
		   return;
		} else {
		   if (mTCstInventoryDto.getBindType() != null) {//先绑定患者
			mIntentType = 2;//2确认并退出
			loadBingFistDate(mIntentType);

		   } else {//后绑定的未绑定
			int mType = 1;//1.8.3未绑定
			DialogUtils.showTwoDialog(mContext, mType, "您还有未绑定患者的耗材，确认领用吗？", "耗材未绑定患者");
		   }
		}
		break;
	   case R.id.ly_bing_btn_right:
		if (UIUtils.isFastDoubleClick()) {
		   return;
		} else {
		   ToastUtils.showShort("绑定");
		   loadBingDate("");
		}
		break;
	}
   }

   private void loadBingFistDate(int mIntentType) {
	mTCstInventoryDto.setStorehouseCode(SPUtils.getString(UIUtils.getContext(), SAVE_STOREHOUSE_CODE));
	mTCstInventoryDto.setAccountId(SPUtils.getString(UIUtils.getContext(), KEY_ACCOUNT_ID));
	String toJson = mGson.toJson(mTCstInventoryDto);
	LogUtils.i(TAG, "toJson  " + toJson);
	NetRequest.getInstance().bingPatientsDate(toJson, this, null, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG, "result   " + result);
		ToastUtils.showShort("操作成功");
		if (mIntentType==2){
		   startActivity(new Intent(OutBoxBingActivity.this, LoginActivity.class));
		   App.getInstance().removeALLActivity_();
		}
		finish();
	   }
	});
   }

   /**
    * 获取需要绑定的患者
    */
   private void loadBingDate(String optienNameOrId) {

	NetRequest.getInstance().findSchedulesDate(optienNameOrId, this, null, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		BingFindSchedulesBean bingFindSchedulesBean = mGson.fromJson(result,
												 BingFindSchedulesBean.class);
		mPatientInfos = bingFindSchedulesBean.getPatientInfos();
		DialogUtils.showRvDialog(OutBoxBingActivity.this, mContext, mPatientInfos, "afterBind",
						 -1, null);
		LogUtils.i(TAG, "result   " + result);
	   }
	});
   }

   @Override
   protected void onDestroy() {
	EventBusUtils.postSticky(new Event.EventFrag("START1"));
	super.onDestroy();
   }
}
