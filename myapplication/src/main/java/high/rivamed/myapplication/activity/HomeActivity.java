package high.rivamed.myapplication.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.SimpleActivity;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.fragment.ContentConsumeOperateFrag;
import high.rivamed.myapplication.fragment.ContentRunWateFrag;
import high.rivamed.myapplication.fragment.ContentStockStatusFrag;
import high.rivamed.myapplication.fragment.ContentTakeNotesFrag;
import high.rivamed.myapplication.fragment.ContentTimelyCheckFrag;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.NotificationsUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;
import me.yokeyword.fragmentation.SupportFragment;

import static high.rivamed.myapplication.cont.Constants.CONFIG_007;
import static high.rivamed.myapplication.cont.Constants.CONFIG_019;
import static high.rivamed.myapplication.cont.Constants.LEFT_MENU_HCCZ;
import static high.rivamed.myapplication.cont.Constants.LEFT_MENU_HCLS;
import static high.rivamed.myapplication.cont.Constants.LEFT_MENU_KCZT;
import static high.rivamed.myapplication.cont.Constants.LEFT_MENU_SSPD;
import static high.rivamed.myapplication.cont.Constants.LEFT_MENU_SYJL;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/14 15:20
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class HomeActivity extends SimpleActivity {

   public              String TAG        = "HomeActivity";
   public              long   TOUCH_TIME = 0;
   // 再点一次退出程序时间设置
   public static final long   WAIT_TIME  = 2000L;

   @BindView(R.id.content_syjl)
   RadioButton mContentSyjl;
   @BindView(R.id.home_rg)
   RadioGroup  mHomeRg;
   @BindView(R.id.rg_gone)
   View        mHomeRgGone;
   @BindView(R.id.content_consume_operate)
   RadioButton mContentConsumeOperate;
   @BindView(R.id.content_running_wate)
   RadioButton mContentRunningWate;
   @BindView(R.id.content_stock_status)
   RadioButton mContentStockStatus;
   @BindView(R.id.content_timely_check)
   RadioButton mContentTimelyCheck;
   private SupportFragment[] mFragments = new SupportFragment[5];

   public static final int CONSUME = 0;
   public static final int RUNWATE = 1;
   public static final int STOCK   = 2;
   public static final int CHECK   = 3;
   public static final int SYJL    = 4;
   private int     LastId;

   /**
    * 开锁后禁止点击左侧菜单栏按钮
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onHomeNoClick(Event.HomeNoClickEvent event) {
	LogUtils.i(TAG, "event   " + event.isClick);
	if (event.isClick) {
	   mHomeRgGone.setVisibility(View.VISIBLE);
	   disableRadioGroup(mHomeRg);
	} else {
	   mHomeRgGone.setVisibility(View.GONE);
	   enableRadioGroup(mHomeRg);
	}
   }

   public void disableRadioGroup(RadioGroup testRadioGroup) {
	for (int i = 0; i < testRadioGroup.getChildCount(); i++) {
	   testRadioGroup.getChildAt(i).setEnabled(false);
	}
   }

   public void enableRadioGroup(RadioGroup testRadioGroup) {
	for (int i = 0; i < testRadioGroup.getChildCount(); i++) {
	   testRadioGroup.getChildAt(i).setEnabled(true);
	}
   }

   @Override
   public int getLayoutId() {
	return R.layout.home_layout;
   }

   /**
    * 初始化
    *
    * @param savedInstanceState
    */
   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	EventBusUtils.register(this);

	setMenu();
	initData();
	initListener();
//	initPushService();
//	initMessageIcon();
   }

   /**
    * 设置左侧按钮
    */
   private void setMenu() {
	if (UIUtils.getMenuLeftType(this,LEFT_MENU_HCCZ)){//耗材操作
	   mContentConsumeOperate.setVisibility(View.VISIBLE);
	}else {
	   mContentConsumeOperate.setVisibility(View.GONE);
	}
	if (UIUtils.getMenuLeftType(this,LEFT_MENU_HCLS)){//耗材流水
	   mContentRunningWate.setVisibility(View.VISIBLE);
	}else {
	   mContentRunningWate.setVisibility(View.GONE);
	}
	if (UIUtils.getMenuLeftType(this,LEFT_MENU_KCZT)){//库存状态
	   mContentStockStatus.setVisibility(View.VISIBLE);
	}else {
	   mContentStockStatus.setVisibility(View.GONE);
	}
	if (UIUtils.getMenuLeftType(this,LEFT_MENU_SSPD)){//实时盘点
	   mContentTimelyCheck.setVisibility(View.VISIBLE);
	}else {
	   mContentTimelyCheck.setVisibility(View.GONE);
	}
	if (UIUtils.getMenuLeftType(this,LEFT_MENU_SYJL)&&(UIUtils.getConfigType(mContext, CONFIG_007) ||UIUtils.getConfigType(mContext, CONFIG_019))){//使用记录
	   mContentSyjl.setVisibility(View.VISIBLE);
	}else {
	   mContentSyjl.setVisibility(View.GONE);
	}
   }


   /**
    * 初始化消息推送服务
    */
   private void initPushService() {
	if (!NotificationsUtils.isNotificationEnabled(this)) {
	   Intent localIntent = new Intent();
	   localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	   if (Build.VERSION.SDK_INT >= 9) {
		localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
		localIntent.setData(Uri.fromParts("package", this.getPackageName(), null));
	   } else if (Build.VERSION.SDK_INT <= 8) {
		localIntent.setAction(Intent.ACTION_VIEW);

		localIntent.setClassName("com.android.settings",
						 "com.android.settings.InstalledAppDetails");

		localIntent.putExtra("com.android.settings.ApplicationPkgName", this.getPackageName());
	   }
	   startActivity(localIntent);
	}

   }

   @Override
   public void onBindViewBefore() {

   }

   /**
    * 填充右边fragment
    */
   public void initData() {
	SupportFragment firstFragment = findFragment(ContentConsumeOperateFrag.class);
	if (firstFragment == null) {
	   mFragments[CONSUME] = ContentConsumeOperateFrag.newInstance();
	   mFragments[RUNWATE] = ContentRunWateFrag.newInstance();
	   mFragments[STOCK] = ContentStockStatusFrag.newInstance();
	   mFragments[CHECK] = ContentTimelyCheckFrag.newInstance();
	   mFragments[SYJL] = ContentTakeNotesFrag.newInstance();

	   loadMultipleRootFragment(R.id.fl_tab_container, CONSUME, mFragments[CONSUME],
					    mFragments[RUNWATE], mFragments[STOCK], mFragments[CHECK],
					    mFragments[SYJL]);
	} else {
	   // 拿到mFragments的引用
	   mFragments[CONSUME] = firstFragment;
	   mFragments[RUNWATE] = findFragment(ContentRunWateFrag.class);
	   mFragments[STOCK] = findFragment(ContentStockStatusFrag.class);
	   mFragments[CHECK] = findFragment(ContentTimelyCheckFrag.class);
	   mFragments[SYJL] = findFragment(ContentTakeNotesFrag.class);
	}

	//设置选中的页面
	mHomeRg.check(R.id.content_consume_operate);
	LastId = 0;
   }

   public void initListener() {
	mHomeRgGone.setOnClickListener(new View.OnClickListener() {
	   @Override
	   public void onClick(View v) {
		ToastUtils.showShort("请关闭柜门再进行操作");
	   }
	});
	mHomeRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
	   @Override
	   public void onCheckedChanged(RadioGroup group, int checkedId) {

		switch (checkedId) {
		   case R.id.content_consume_operate://耗材操作
			showHideFragment(mFragments[0], mFragments[LastId]);
			EventBusUtils.postSticky(new Event.EventFrag("START1"));
			LastId = 0;
			break;
		   case R.id.content_running_wate://耗材流水
			showHideFragment(mFragments[1], mFragments[LastId]);
			EventBusUtils.postSticky(new Event.EventFrag("START2"));
			LastId = 1;
			break;
		   case R.id.content_stock_status://库存状态
			showHideFragment(mFragments[2], mFragments[LastId]);
			EventBusUtils.postSticky(new Event.EventFrag("START3"));
			LastId = 2;
			break;
		   case R.id.content_timely_check://实时盘点
			showHideFragment(mFragments[3], mFragments[LastId]);
			EventBusUtils.postSticky(new Event.EventFrag("START4"));
			LastId = 3;
			break;
		   case R.id.content_syjl://
			showHideFragment(mFragments[4], mFragments[LastId]);
			EventBusUtils.postSticky(new Event.EventFrag("START5"));
			LastId = 4;
			break;
		}
	   }
	});

   }

   /**
    * 连续点击退出
    */
   @Override
   public void onBackPressedSupport() {

	if ((System.currentTimeMillis() - TOUCH_TIME > WAIT_TIME)) {
	   TOUCH_TIME = System.currentTimeMillis();
	   ToastUtils.showShortSafe(UIUtils.getString(R.string.press_again_exit));
	} else {
	   super.onBackPressedSupport();
	}
   }

   @Override
   public Object newP() {
	return null;
   }



}
