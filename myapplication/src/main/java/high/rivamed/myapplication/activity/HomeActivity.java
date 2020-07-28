package high.rivamed.myapplication.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.litepal.LitePal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.SimpleActivity;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.dbmodel.BoxIdBean;
import high.rivamed.myapplication.fragment.ContentConsumeOperateFrag;
import high.rivamed.myapplication.fragment.ContentExceptionDealFrag;
import high.rivamed.myapplication.fragment.ContentRunWateFrag;
import high.rivamed.myapplication.fragment.ContentStockStatusFrag;
import high.rivamed.myapplication.fragment.ContentTakeNotesFrag;
import high.rivamed.myapplication.fragment.ContentTimelyCheckFrag;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;
import me.yokeyword.fragmentation.SupportFragment;

import static high.rivamed.myapplication.base.App.SYSTEMTYPE;
import static high.rivamed.myapplication.cont.Constants.CONFIG_BPOW01;
import static high.rivamed.myapplication.cont.Constants.CONFIG_BPOW02;
import static high.rivamed.myapplication.cont.Constants.CONFIG_BPOW04;
import static high.rivamed.myapplication.cont.Constants.CONFIG_BPOW05;
import static high.rivamed.myapplication.cont.Constants.CONSUMABLE_TYPE;
import static high.rivamed.myapplication.cont.Constants.LEFT_MENU_HCCZ;
import static high.rivamed.myapplication.cont.Constants.LEFT_MENU_HCLS;
import static high.rivamed.myapplication.cont.Constants.LEFT_MENU_KCZT;
import static high.rivamed.myapplication.cont.Constants.LEFT_MENU_SSPD;
import static high.rivamed.myapplication.cont.Constants.LEFT_MENU_SYJL;
import static high.rivamed.myapplication.cont.Constants.LEFT_MENU_YCCL;
import static high.rivamed.myapplication.utils.DevicesUtils.getDoorStatus;

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
   RadioGroup mHomeRg;
   public static View       mHomeRgGone;
   @BindView(R.id.home_logo)
   ImageView mHomeLogo;
   @BindView(R.id.content_consume_operate)
   RadioButton mContentConsumeOperate;
   @BindView(R.id.content_running_wate)
   RadioButton mContentRunningWate;
   @BindView(R.id.content_stock_status)
   RadioButton mContentStockStatus;
   @BindView(R.id.content_timely_check)
   RadioButton mContentTimelyCheck;
   @BindView(R.id.content_exception_deal)
   RadioButton mContentExceptionDeal;
   private SupportFragment[] mFragments = new SupportFragment[6];

   public static final int CONSUME = 0;
   public static final int RUNWATE = 1;
   public static final int STOCK   = 2;
   public static final int CHECK   = 3;
   public static final int SYJL    = 4;
   public static final int YCCL    = 5;
   private int LastId;
   private boolean           mDoorStatus     = true;
   private ArrayList<String> mEthDevices     = new ArrayList<>();
   private List<String>      mDeviceSizeList = new ArrayList<>();
   private ArrayList<String> mListDevices;
   private MyTouchListener myTouchListener;//实现接口
   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	List<BoxIdBean> boxIdBeans = LitePal.where("name = ?", CONSUMABLE_TYPE).find(BoxIdBean.class);
	for (BoxIdBean idBean : boxIdBeans) {
	   mDeviceSizeList.add(idBean.getDevice_id());
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
	File file = new File(Environment.getExternalStorageDirectory() + "/home_logo" + "/home_logo.png");
//	Glide.with(this).load(file).diskCacheStrategy(DiskCacheStrategy.NONE).error(R.mipmap.hckg_logo).into(mHomeLogo);
	Log.i("eerf","file.getPath()   "+file.getPath());
	Bitmap bitmap= BitmapFactory.decodeFile(file.getPath());
	Log.i("eerf","bitmap   "+(bitmap==null));
	if (bitmap ==null){
	   mHomeLogo.setImageResource(R.mipmap.hckg_logo);
	}else {
	   mHomeLogo.setImageBitmap(bitmap);
	}
	setMenu();
	initData();
	initListener();
	//	initPushService();
	//	initMessageIcon();
   }

   @Override
   public void onStart() {
	super.onStart();
	getDoorStatus(SYSTEMTYPE);
	EventBusUtils.register(this);
   }

   /**
    * 设置左侧按钮
    */
   private void setMenu() {
	if (UIUtils.getMenuLeftType(this, LEFT_MENU_HCCZ)) {//耗材操作
	   mContentConsumeOperate.setVisibility(View.VISIBLE);
	} else {
	   mContentConsumeOperate.setVisibility(View.GONE);
	}
	if (UIUtils.getMenuLeftType(this, LEFT_MENU_HCLS)) {//耗材流水
	   mContentRunningWate.setVisibility(View.VISIBLE);
	} else {
	   mContentRunningWate.setVisibility(View.GONE);
	}
	if (UIUtils.getMenuLeftType(this, LEFT_MENU_KCZT)) {//库存状态
	   mContentStockStatus.setVisibility(View.VISIBLE);
	} else {
	   mContentStockStatus.setVisibility(View.GONE);
	}
	if (UIUtils.getMenuLeftType(this, LEFT_MENU_SSPD)) {//实时盘点
	   mContentTimelyCheck.setVisibility(View.VISIBLE);
	} else {
	   mContentTimelyCheck.setVisibility(View.GONE);
	}
	if (UIUtils.getMenuLeftType(this, LEFT_MENU_SYJL) &&
	    (UIUtils.getConfigType(mContext, CONFIG_BPOW01) ||
	     UIUtils.getConfigType(mContext, CONFIG_BPOW02) ||
	     UIUtils.getConfigType(mContext, CONFIG_BPOW04)||
	     UIUtils.getConfigType(mContext, CONFIG_BPOW05))) {//使用记录
	   mContentSyjl.setVisibility(View.VISIBLE);
	} else {
	   mContentSyjl.setVisibility(View.GONE);
	}
	if (UIUtils.getMenuLeftType(this, LEFT_MENU_YCCL)) {//异常处理
	   mContentExceptionDeal.setVisibility(View.VISIBLE);
	} else {
	   mContentExceptionDeal.setVisibility(View.GONE);
	}
   }

   @Override
   public void onBindViewBefore() {
	mHomeRgGone = findViewById(R.id.rg_gone);
	mHomeRg = findViewById(R.id.home_rg);

   }

   /**
    * 填充右边fragment
    */
   public void initData() {
	SupportFragment firstFragment = findFragment(ContentConsumeOperateFrag.class);
	if (firstFragment == null) {
	   mFragments[CONSUME] = ContentConsumeOperateFrag.newInstance();
	   mFragments[RUNWATE] = ContentRunWateFrag.newInstance();
	   mFragments[STOCK] = ContentStockStatusFrag.newInstance(true);
	   mFragments[CHECK] = ContentTimelyCheckFrag.newInstance();
	   mFragments[SYJL] = ContentTakeNotesFrag.newInstance();
	   mFragments[YCCL] = ContentExceptionDealFrag.newInstance();

	   loadMultipleRootFragment(R.id.fl_tab_container, CONSUME, mFragments[CONSUME],
					    mFragments[RUNWATE], mFragments[STOCK], mFragments[CHECK],
					    mFragments[SYJL], mFragments[YCCL]);
	} else {
	   // 拿到mFragments的引用
	   mFragments[CONSUME] = firstFragment;
	   mFragments[RUNWATE] = findFragment(ContentRunWateFrag.class);
	   mFragments[STOCK] = findFragment(ContentStockStatusFrag.class);
	   mFragments[CHECK] = findFragment(ContentTimelyCheckFrag.class);
	   mFragments[SYJL] = findFragment(ContentTakeNotesFrag.class);
	   mFragments[YCCL] = findFragment(ContentExceptionDealFrag.class);
	}

	//设置选中的页面
	mHomeRg.check(R.id.content_consume_operate);
	LastId = 0;
   }

   public void initListener() {
	mHomeRgGone.setOnClickListener(new View.OnClickListener() {
	   @Override
	   public void onClick(View v) {
		ToastUtils.showShortToast("请关闭柜门再进行操作");
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
		   case R.id.content_exception_deal://异常处理
			showHideFragment(mFragments[5], mFragments[LastId]);
			EventBusUtils.postSticky(new Event.EventFrag("START6"));
			LastId = 5;
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
	   ToastUtils.showShortToast(UIUtils.getString(R.string.press_again_exit));
	} else {
	   super.onBackPressedSupport();
	}
   }
   @Override
   public boolean dispatchTouchEvent(MotionEvent ev) {
	//将触摸事件传递给回调函数
	if (null != myTouchListener) {
	   myTouchListener.onTouch(ev);
	}
	return super.dispatchTouchEvent(ev);
   }

   /**
    * 用于注册回调事件
    */
   public void registerMyTouchListener(MyTouchListener myTouchListener) {
	this.myTouchListener = myTouchListener;
   }

   /**
    * 定义一个接口
    * @author fox
    *
    */
   public interface MyTouchListener {
	public void onTouch(MotionEvent ev);
   }
   @Override
   public Object newP() {
	return null;
   }

   @Override
   protected void onDestroy() {
	Log.i("outtccc",getClass().getName()+"  onDestroy");
	super.onDestroy();
	mHomeRg=null;
	mHomeRgGone=null;
   }
}
