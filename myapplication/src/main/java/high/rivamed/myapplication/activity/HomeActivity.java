package high.rivamed.myapplication.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.gson.JsonSyntaxException;

import org.androidpn.client.Notifier;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.SimpleActivity;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.PendingTaskBean;
import high.rivamed.myapplication.fragment.ContentConsumeOperateFrag2;
import high.rivamed.myapplication.fragment.ContentRunWateFrag;
import high.rivamed.myapplication.fragment.ContentStockStatusFrag;
import high.rivamed.myapplication.fragment.ContentTakeNotesFrag;
import high.rivamed.myapplication.fragment.ContentTimelyCheckFrag;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.NotificationsUtils;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;
import me.yokeyword.fragmentation.SupportFragment;

import static high.rivamed.myapplication.cont.Constants.CONFIG_007;
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

    public String TAG = "HomeActivity";
    public long TOUCH_TIME = 0;
    // 再点一次退出程序时间设置
    public static final long WAIT_TIME = 2000L;
    public static final String JUMP_ID = "jump_id";
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
    @BindView(R.id.content_syjl)
    RadioButton mContentSyjl;
    @BindView(R.id.home_rg)
    RadioGroup mHomeRg;
    @BindView(R.id.rg_gone)
    View mHomeRgGone;
    private SupportFragment[] mFragments = new SupportFragment[5];

    public static final int CONSUME = 0;
    public static final int RUNWATE = 1;
    public static final int STOCK = 2;
    public static final int CHECK = 3;
    public static final int SYJL = 4;
    private int LastId;
    private boolean mIsClick;

    /**
     * 开锁后禁止点击左侧菜单栏按钮
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHomeNoClick(Event.HomeNoClickEvent event) {
        LogUtils.i(TAG, "event   " + event.isClick);
        mIsClick = event.isClick;
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
        LogUtils.i(TAG, "SPUtils   " + SPUtils.getString(mContext, SAVE_SEVER_IP));
        //	EventBusUtils.register(this);
        if (!UIUtils.getConfigType(mContext, CONFIG_007)){
            mContentSyjl.setVisibility(View.GONE);
        }else {
            mContentSyjl.setVisibility(View.VISIBLE);
        }
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
        initPushService();
        initMessageIcon();
    }

    /*
    * 初始化消息图标显示状态
    * */
    private void initMessageIcon() {
        NetRequest.getInstance().getPendingTaskList(this, new BaseResult() {
            @Override
            public void onSucceed(String result) {
                try {
                    PendingTaskBean emergencyBean = mGson.fromJson(result, PendingTaskBean.class);
                    if (emergencyBean.getMessages() != null) {
                        EventBusUtils.post(new Notifier.EventPushMessageNum(emergencyBean.getMessages().size() + ""));
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }
        });
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

                localIntent.putExtra("com.android.settings.ApplicationPkgName",
                        this.getPackageName());
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
        SupportFragment firstFragment = findFragment(ContentConsumeOperateFrag2.class);
        if (firstFragment == null) {
            mFragments[CONSUME] = ContentConsumeOperateFrag2.newInstance();
            mFragments[RUNWATE] = ContentRunWateFrag.newInstance();
            mFragments[STOCK] = ContentStockStatusFrag.newInstance();
            mFragments[CHECK] = ContentTimelyCheckFrag.newInstance();
            mFragments[SYJL] = ContentTakeNotesFrag.newInstance();

            loadMultipleRootFragment(R.id.fl_tab_container, CONSUME, mFragments[CONSUME],
                    mFragments[RUNWATE], mFragments[STOCK], mFragments[CHECK], mFragments[SYJL]);
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
