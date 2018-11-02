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

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

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
import high.rivamed.myapplication.views.SettingPopupWindow;
import high.rivamed.myapplication.views.TwoDialog;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      LiangDanMing
 * 创建时间:    2018/6/18 9:31
 * 描述:        有标题栏的activity基类
 * 包名:        high.rivamed.myapplication.base
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public abstract class BaseSimpleActivity extends SimpleActivity {

   private static final String TAG = "BaseSimpleActivity";
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
   public ImageView       mBaseTabBtnMsg;
   @BindView(R.id.base_tab_ll)
   public RelativeLayout  mBaseTabLl;
   @BindView(R.id.base_tab_rlayout)
   public RelativeLayout  mBaseTabRlayout;

   public List<String>       mReaderDeviceId;
   public List<String>       eth002DeviceIdList;
   public ViewStub           mStub;
   public SettingPopupWindow mPopupWindow;
   public ImageView      mBaseTabBtnConn;
   private boolean mTitleConn;

   /**
    * 设备title连接状态
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onTitleConnEvent(Event.EventTitleConn event) {
	mTitleConn = event.b;
	LogUtils.i(TAG, "mTitleConn   " + mTitleConn);
	if (mTitleConn){
	   LogUtils.i(TAG,"mBaseTabBtnConn.setEnabled(true)  ");
	   mBaseTabBtnConn.setEnabled(true);
//	   Glide.with(this)
//		   .load(R.mipmap.connect_yes)
//		   .error(R.mipmap.connect_no)
//		   .into(mBaseTabBtnConn);
	}else {
	   LogUtils.i(TAG,"mBaseTabBtnConn.setEnabled(false)  ");
	   mBaseTabBtnConn.setEnabled(false);
	}
   }
   @Override
   public int getLayoutId() {
	EventBusUtils.register(this);
	return R.layout.fragment_base_title;
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {

   }

   @Override
   public void onBindViewBefore() {

	mStub = (ViewStub) findViewById(R.id.viewstub_layout);
	mBaseTabBtnConn = (ImageView) findViewById(R.id.base_tab_conn);
	mStub.setLayoutResource(getContentLayoutId());
	mStub.inflate();

   }

   protected abstract int getContentLayoutId();

   @OnClick({R.id.base_tab_tv_name, R.id.base_tab_icon_right, R.id.base_tab_tv_outlogin,
	   R.id.base_tab_btn_msg, R.id.base_tab_back})
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
				startActivity(new Intent(BaseSimpleActivity.this, MyInfoActivity.class));
				break;
			   case 1:
				startActivity(new Intent(BaseSimpleActivity.this, LoginInfoActivity.class));
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
			startActivity(new Intent(mContext, LoginActivity.class));
			App.getInstance().removeALLActivity_();
		   }
		});
		builder.create().show();
		break;
	   case R.id.base_tab_btn_msg:
		break;
	   case R.id.base_tab_back:
		finish();
		break;
	}
   }

   @Override
   public Object newP() {
	return null;
   }

}
