package high.rivamed.myapplication.base;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.activity.LoginActivity;
import high.rivamed.myapplication.activity.LoginInfoActivity;
import high.rivamed.myapplication.activity.MyInfoActivity;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.UIUtils;
import high.rivamed.myapplication.views.SettingPopupWindow;
import high.rivamed.myapplication.views.TwoDialog;

import static high.rivamed.myapplication.cont.Constants.KEY_USER_NAME;
import static high.rivamed.myapplication.cont.Constants.KEY_USER_SEX;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/14 16:22
 * 描述:        有标题栏的抽取的fragment
 * 包名:        high.rivamed.myapplication.base
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public abstract class BaseSimpleFragment extends SimpleFragment {

   public String TAG = "BaseSimpleFragment";
   @BindView(R.id.base_tab_back)
   public TextView        mBaseTabBack;
   @BindView(R.id.base_tab_btn_left)
   public TextView        mBaseTabBtnLeft;
   @BindView(R.id.base_tab_tv_title)
   public TextView        mBaseTabTvTitle;
   @BindView(R.id.stock_rdbtn_left)
   public RadioButton     mStockRdbtnLeft;
   @BindView(R.id.stock_rdbtn_middle)
   public RadioButton     mStockRdbtnMiddle;
   @BindView(R.id.stock_rdbtn_right)
   public RadioButton     mStockRdbtnRight;
   @BindView(R.id.rg_group)
   public RadioGroup      mRgGroup;
   @BindView(R.id.base_tab_tv_name)
   public TextView        mBaseTabTvName;
   @BindView(R.id.base_tab_icon_right)
   public CircleImageView mBaseTabIconRight;
   @BindView(R.id.base_tab_tv_outlogin)
   public ImageView        mBaseTabOutLogin;

   @BindView(R.id.base_tab_btn_msg)
   public ImageView      mBaseTabBtnMsg;
   @BindView(R.id.base_tab_ll)
   public RelativeLayout mBaseTabLl;
   @BindView(R.id.base_tab_rlayout)
   public RelativeLayout mBaseTabRlayout;
   public ImageView      mBaseTabBtnConn;
   private ViewStub           mStub;
   public  SettingPopupWindow mPopupWindow;
   private boolean mTitleConn;
   /**
    * 设备title连接状态
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onTitleConnEvent(Event.EventTitleConn event) {
	mTitleConn = event.b;
	if (mTitleConn){
	   LogUtils.i(TAG,"mBaseTabBtnConn.setEnabled(true)  ");
	   mBaseTabBtnConn.setEnabled(true);
	}else {
	   LogUtils.i(TAG,"mBaseTabBtnConn.setEnabled(false)  ");
	   mBaseTabBtnConn.setEnabled(false);
	}
   }
   @Override
   public void getTitleName() {
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

	super.getTitleName();

   }

   @Override
   public int getLayoutId() {
	EventBusUtils.register(this);
	return R.layout.fragment_base_title;
   }

   @Override
   public void onResume() {
	super.onResume();

   }

   @Override
   public void onBindViewBefore(View root) {
	mStub = (ViewStub) root.findViewById(R.id.viewstub_layout);
	mBaseTabBtnConn = (ImageView) root.findViewById(R.id.base_tab_conn);
	mStub.setLayoutResource(getContentLayoutId());
	mStub.inflate();

	//        String accountData = SPUtils.getString(getActivity(), KEY_ACCOUNT_DATA);
	//
	//        LoginResultBean data = mGson.fromJson(accountData, LoginResultBean.class);
	//
	//        LoginResultBean.AppAccountInfoVoBean appAccountInfoVo = data.getAppAccountInfoVo();

   }

   protected abstract int getContentLayoutId();

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {

	//        UIUtils.runInUIThread(new Runnable() {
	//            @Override
	//            public void run() {
	//                try {
	//
	//                } catch (Exception e) {
	//                    e.printStackTrace();
	//                }
	//            }
	//        }, 500);
   }

   @OnClick({R.id.base_tab_tv_name, R.id.base_tab_icon_right, R.id.base_tab_tv_outlogin,
	   R.id.base_tab_btn_msg})
   public void onViewClicked(View view) {
	switch (view.getId()) {
	   case R.id.base_tab_icon_right:
	   case R.id.base_tab_tv_name:
		mPopupWindow = new SettingPopupWindow(mContext);
		mPopupWindow.showPopupWindow(mBaseTabIconRight);
		LogUtils.i("sss", "base_tab_tv_name");
		popupClick();
		break;
	   case R.id.base_tab_btn_msg:
		LogUtils.i("sss", "base_tab_btn_msg");
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
	}
   }

   public void popupClick() {
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

		}
	   }
	});
   }

}

