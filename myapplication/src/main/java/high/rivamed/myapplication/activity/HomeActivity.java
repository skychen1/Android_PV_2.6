package high.rivamed.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import butterknife.BindView;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.SimpleActivity;
import high.rivamed.myapplication.fragment.ContentConsumeOperateFrag;
import high.rivamed.myapplication.fragment.ContentRunWateFrag;
import high.rivamed.myapplication.fragment.ContentStockStatusFrag;
import high.rivamed.myapplication.fragment.ContentTimelyCheckFrag;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;
import me.yokeyword.fragmentation.SupportFragment;

import static high.rivamed.myapplication.cont.Constants.SAVE_SEVER_IP;

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
   public static final String JUMP_ID    = "jump_id";
   @BindView(R.id.home_logo)
   ImageView   mHomeLogo;
   @BindView(R.id.content_consume_operate)
   RadioButton mContentConsumeOperate;
   @BindView(R.id.content_running_wate)
   RadioButton mContentRunningWate;
   @BindView(R.id.content_stock_status)
   RadioButton mContentStockStatus;
   @BindView(R.id.content_timely_check)
   RadioButton mContentTimelyCheck;
   @BindView(R.id.home_rg)
   RadioGroup  mHomeRg;

   private SupportFragment[] mFragments = new SupportFragment[4];

   public static final int CONSUME = 0;
   public static final int RUNWATE = 1;
   public static final int STOCK   = 2;
   public static final int CHECK   = 3;
   private int LastId;

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
	LogUtils.i(TAG, "SPUtils   " + SPUtils.getString(mContext, SAVE_SEVER_IP));
	//	EventBusUtils.register(this);

	Intent intent = getIntent();
	Bundle extras = intent.getExtras();
	if (extras != null) {
	   String type = extras.getString("type");
	   int number = extras.getInt("number");
	   mHomeRg.check(R.id.content_stock_status);
	   LastId = 2;

	}

	initData();
	initListener();
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

	   loadMultipleRootFragment(R.id.fl_tab_container, CONSUME, mFragments[CONSUME],
					    mFragments[RUNWATE], mFragments[STOCK], mFragments[CHECK]);
	} else {
	   // 拿到mFragments的引用
	   mFragments[CONSUME] = firstFragment;
	   mFragments[RUNWATE] = findFragment(ContentRunWateFrag.class);
	   mFragments[STOCK] = findFragment(ContentStockStatusFrag.class);
	   mFragments[CHECK] = findFragment(ContentTimelyCheckFrag.class);
	}

	//设置选中的页面
	mHomeRg.check(R.id.content_consume_operate);
	LastId = 0;
   }

   public void initListener() {
	mHomeRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
	   @Override
	   public void onCheckedChanged(RadioGroup group, int checkedId) {

		switch (checkedId) {
		   case R.id.content_consume_operate://耗材操作
			showHideFragment(mFragments[0], mFragments[LastId]);
			LastId = 0;
			break;
		   case R.id.content_running_wate://耗材流水
			showHideFragment(mFragments[1], mFragments[LastId]);
			LastId = 1;
			break;
		   case R.id.content_stock_status://库存状态
			showHideFragment(mFragments[2], mFragments[LastId]);
			LastId = 2;
			break;
		   case R.id.content_timely_check://实时盘点
			showHideFragment(mFragments[3], mFragments[LastId]);
			LastId = 3;
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
