package high.rivamed.myapplication.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.OnClick;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.App;
import high.rivamed.myapplication.base.BaseTimelyActivity;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.LossDateBean;
import high.rivamed.myapplication.bean.PutLossDateBean;
import high.rivamed.myapplication.dto.vo.TCstInventoryVo;
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

import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_TIMELY_LOSS;
import static high.rivamed.myapplication.cont.Constants.KEY_ACCOUNT_ID;
import static high.rivamed.myapplication.cont.Constants.THING_CODE;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/20 14:39
 * 描述:        实时盘点 盘亏
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class TimelyLossActivity extends BaseTimelyActivity{

   private static final String TAG = "TimelyLossActivity";

   String mName;

   /**
    * 获取loss原因
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onEvent(Event.EventLoss event) {
	mName = event.string;
	LogUtils.i(TAG, "  mName    " + mName);
	for (int x =0;x<mTypeView.mTimelyLossAdapter.mLossData.size();x++){
	   if (mTypeView.mTimelyLossAdapter.mLossData.get(x).isSelected()){
		mTypeView.mTimelyLossAdapter.mLossData.get(x).setRemark(mName);
	   }
	   mTypeView.mTimelyLossAdapter.notifyDataSetChanged();
	}
   }
   @Override
   public int getCompanyType() {
	super.my_id = ACT_TYPE_TIMELY_LOSS;
	return my_id;
   }
   @OnClick({R.id.base_tab_tv_name, R.id.base_tab_icon_right, R.id.base_tab_tv_outlogin,
	  R.id.base_tab_back, R.id.timely_start_btn, R.id.timely_open_door,
	   R.id.timely_left, R.id.timely_right})
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
				startActivity(new Intent(TimelyLossActivity.this, MyInfoActivity.class));
				break;
			   case 1:
				startActivity(
					new Intent(TimelyLossActivity.this, LoginInfoActivity.class));
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
			mContext.startActivity(new Intent(mContext, LoginActivity.class));
			App.getInstance().removeALLActivity_();
			dialog.dismiss();
		   }
		});
		builder.create().show();
		break;

	   case R.id.base_tab_back:
		EventBusUtils.postSticky(new Event.EventFrag("START4"));
		finish();
		break;


	   case R.id.timely_left:
		if (UIUtils.isFastDoubleClick()) {
		   return;
		} else {
		   getLossScuse();
		}

		break;
	   case R.id.timely_right:
		if (UIUtils.isFastDoubleClick()) {
		   return;
		} else {
		   putDate();
		}
		break;

	}

   }

   /**
    * 提交数据
    */
   private void putDate() {
	LossDateBean lossDateBean = new LossDateBean();
	List<LossDateBean.CstStockJournalsBean> lossDates = new ArrayList<>();
	LossDateBean.CstStockJournalsBean cstStockJournalsBean = new LossDateBean.CstStockJournalsBean();
	List<TCstInventoryVo> voList = mDto.gettCstInventoryVos();
	for (TCstInventoryVo v:voList){
	   cstStockJournalsBean.setAccountId(SPUtils.getString(UIUtils.getContext(), KEY_ACCOUNT_ID));
	   cstStockJournalsBean.setEpc(v.getEpc());
	   cstStockJournalsBean.setBatchNumber(v.getBatchNumber());
	   cstStockJournalsBean.setThingCode(SPUtils.getString(UIUtils.getContext(), THING_CODE));
	   cstStockJournalsBean.setCstId(v.getCstId());
	   cstStockJournalsBean.setCstName(v.getCstName());
	   cstStockJournalsBean.setCauseRemark(v.getRemark());
	   cstStockJournalsBean.setCstSpec(v.getCstSpec());
	   lossDates.add(cstStockJournalsBean);
	}
	lossDateBean.setCstStockJournals(lossDates);
	String s = mGson.toJson(lossDateBean);
	LogUtils.i(TAG,"s    "+s);

	NetRequest.getInstance().putLossSDate(s,this,null,new BaseResult(){
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG,"result   "+result);
		PutLossDateBean putLossDateBean = mGson.fromJson(result, PutLossDateBean.class);
		if (putLossDateBean.isOperateSuccess()){
		   ToastUtils.showShort("操作成功");
		   finish();
		}else {
		   ToastUtils.showShort("操作失败，请检查！");

		}
	   }
	});
   }

   /**
    * 获取盘亏原因
    */
   private void getLossScuse() {
	NetRequest.getInstance().getLossCauseDate(this,null,new BaseResult(){
	   @Override
	   public void onSucceed(String result) {
		String substring = result.substring(1, result.length()-1);
		String str= substring.replace("\"", "");

		String[] split = str.split(",");
		List<String> strings = Arrays.asList(split);

		for (String a:strings){
		   Log.i(TAG, "strsToList2  " + a);
		}
		DialogUtils.showLossDialog(mContext,strings);
	   }
	});

   }

}
