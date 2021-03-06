package high.rivamed.myapplication.base;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
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
import high.rivamed.myapplication.activity.LoginInfoActivity2;
import high.rivamed.myapplication.activity.LoginInfoActivity3;
import high.rivamed.myapplication.activity.MessageActivity;
import high.rivamed.myapplication.activity.MyInfoActivity;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.MusicPlayer;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;
import high.rivamed.myapplication.views.SettingPopupWindow;
import high.rivamed.myapplication.views.TwoDialog;

import static high.rivamed.myapplication.activity.LoginActivity.mConfigType015;
import static high.rivamed.myapplication.base.App.SYSTEMTYPE;
import static high.rivamed.myapplication.base.App.mTitleConn;
import static high.rivamed.myapplication.base.App.mTitleMsg;
import static high.rivamed.myapplication.cont.Constants.KEY_USER_NAME;
import static high.rivamed.myapplication.cont.Constants.KEY_USER_SEX;
import static high.rivamed.myapplication.cont.Constants.SYSTEMTYPES_3;
import static high.rivamed.myapplication.utils.UIUtils.removeAllAct;
import static high.rivamed.myapplication.utils.UIUtils.setMessagersV;

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

   public String          TAG = "BaseSimpleFragment";
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
   @BindView(R.id.rg_group3)
   public RadioGroup      mRgGroup3;
   @BindView(R.id.base_tab_tv_name)
   public TextView        mBaseTabTvName;
   @BindView(R.id.base_tab_icon_right)
   public CircleImageView mBaseTabIconRight;
   @BindView(R.id.base_tab_tv_outlogin)
   public ImageView       mBaseTabOutLogin;

   @BindView(R.id.base_tab_btn_msg)
   public  ImageView          mBaseTabBtnMsg;
   @BindView(R.id.base_tab_ll)
   public  RelativeLayout     mBaseTabLl;
   @BindView(R.id.base_tab_rlayout)
   public  RelativeLayout     mBaseTabRlayout;
   public  ImageView          mBaseTabBtnConn;
   public  ImageView          mBaseTabBtnRobot;
   private ViewStub           mStub;
   public  SettingPopupWindow mPopupWindow;

   /**
    * 设备title连接状态
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onTitleConnEvent(Event.XmmppConnect event) {
	mTitleConn = event.connect;
	//        LogUtils.i(TAG, "Xmmppf  "+mTitleConn);
	selTitleIcon();
   }

   /**
    * 是否显示消息提醒
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onEventIfHaveMessage(Event.EventPushMessageNum event) {

	if (event.num > 0) {
	   mTitleMsg = true;
	   if (mBaseTabBtnMsg != null) {
		mBaseTabBtnMsg.setActivated(true);
	   }
	} else {
	   mTitleMsg = false;
	   if (mBaseTabBtnMsg != null) {
		mBaseTabBtnMsg.setActivated(false);
	   }
	}
   }

   @Override
   public void onResume() {
	selTitleIcon();
	setTitleMsg();
	super.onResume();
   }

   public void setTitleMsg() {
	if (mTitleMsg) {
	   if (mBaseTabBtnMsg != null) {
		mBaseTabBtnMsg.setActivated(true);
	   }
	} else {
	   if (mBaseTabBtnMsg != null) {
		mBaseTabBtnMsg.setActivated(false);
	   }
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

   public void selTitleIcon() {
	if (mTitleConn) {
	   if (mBaseTabBtnConn != null) {
		mBaseTabBtnConn.setEnabled(true);
		//                LogUtils.i(TAG, "XmmppConnect(true)3  ");
	   }
	} else {
	   if (mBaseTabBtnConn != null) {
		mBaseTabBtnConn.setEnabled(false);
		//                LogUtils.i(TAG, "XmmppConnect(false)3  ");
	   }
	}

   }

   @Override
   public int getLayoutId() {
	EventBusUtils.register(this);
	return R.layout.fragment_base_title;
   }

   @Override
   public void onBindViewBefore(View root) {
	mStub = (ViewStub) root.findViewById(R.id.viewstub_layout);
	mBaseTabBtnConn = (ImageView) root.findViewById(R.id.base_tab_conn);
	mBaseTabBtnRobot = (ImageView) root.findViewById(R.id.base_tab_robot);
	mStub.setLayoutResource(getContentLayoutId());
	mStub.inflate();
   }

   protected abstract int getContentLayoutId();

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	if (mConfigType015) {
	   setMessagersV();//获取消息数量
	}
   }

   @OnClick({R.id.base_tab_tv_name, R.id.base_tab_icon_right, R.id.base_tab_tv_outlogin,
	   R.id.base_tab_btn_msg})
   public void onViewClicked(View view) {
	switch (view.getId()) {
	   case R.id.base_tab_icon_right:
	   case R.id.base_tab_tv_name:
		mPopupWindow = new SettingPopupWindow(mContext);
		mPopupWindow.showPopupWindow(mBaseTabIconRight);
		popupClick();
		break;
	   case R.id.base_tab_btn_msg:
		if (mTitleConn) {
		   mContext.startActivity(new Intent(mContext, MessageActivity.class));
		} else {
		   ToastUtils.showShortToast("离线状态，只支持选择操作的领用！");
		}
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
			MusicPlayer.getInstance().play(MusicPlayer.Type.LOGOUT_SUC);
			dialog.dismiss();
			removeAllAct(mContext);
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
			if (SYSTEMTYPE.equals(SYSTEMTYPES_3)){
			   mContext.startActivity(new Intent(mContext, LoginInfoActivity3.class));
			}else {
			   mContext.startActivity(new Intent(mContext, LoginInfoActivity2.class));
			}
			break;

		}
	   }
	});
   }
   /* 定义一个倒计时的内部类 */
   protected class TimeCountOver extends CountDownTimer {

	public TimeCountOver(
		long millisInFuture, long countDownInterval) {
	   super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
	}

	@Override
	public void onFinish() {// 计时完毕时触发
	   EventBusUtils.post(new Event.EventOverHome(true));
	}

	@Override
	public void onTick(long millisUntilFinished) {// 计时过程显示
	   Log.i("343ww", "millisUntilFinished     " + millisUntilFinished);
	}
   }
}

