package high.rivamed.myapplication.base;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.androidpn.utils.XmppEvent;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.activity.LoginActivity;
import high.rivamed.myapplication.activity.LoginInfoActivity;
import high.rivamed.myapplication.activity.MessageActivity;
import high.rivamed.myapplication.activity.MyInfoActivity;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.MusicPlayer;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.UIUtils;
import high.rivamed.myapplication.views.SettingPopupWindow;
import high.rivamed.myapplication.views.TwoDialog;

import static high.rivamed.myapplication.base.App.mTitleConn;
import static high.rivamed.myapplication.base.App.mTitleMsg;
import static high.rivamed.myapplication.cont.Constants.KEY_USER_NAME;
import static high.rivamed.myapplication.cont.Constants.KEY_USER_SEX;

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
    public TextView mBaseTabBack;
    @BindView(R.id.base_tab_btn_left)
    public TextView mBaseTabBtnLeft;
    @BindView(R.id.base_tab_tv_title)
    public TextView mBaseTabTvTitle;
    @BindView(R.id.stock_rdbtn_left)
    public RadioButton mStockRdbtnLeft;
    @BindView(R.id.stock_rdbtn_middle)
    public RadioButton mStockRdbtnMiddle;
    @BindView(R.id.stock_rdbtn_right)
    public RadioButton mStockRdbtnRight;
    @BindView(R.id.rg_group)
    public RadioGroup mRgGroup;
    @BindView(R.id.base_tab_tv_name)
    public TextView mBaseTabTvName;
    @BindView(R.id.base_tab_icon_right)
    public CircleImageView mBaseTabIconRight;
    @BindView(R.id.base_tab_tv_outlogin)
    public ImageView mBaseTabOutLogin;
    @BindView(R.id.base_tab_btn_msg)
    public ImageView mBaseTabBtnMsg;
    @BindView(R.id.base_tab_ll)
    public RelativeLayout mBaseTabLl;
    @BindView(R.id.base_tab_rlayout)
    public RelativeLayout mBaseTabRlayout;

    public ViewStub mStub;
    public SettingPopupWindow mPopupWindow;
    public ImageView mBaseTabBtnConn;
    public static CountDownTimer mStarts;
    public  boolean mIsClick;
    /**
     * 开锁后禁止点击左侧菜单栏按钮(检测没有关门)
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onHomeNoClick(Event.HomeNoClickEvent event) {

        mIsClick = event.isClick;
    }
    /**
     * 设备title连接状态
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTitleConnEvent(XmppEvent.XmmppConnect event) {
        mTitleConn = event.connect;
        selTitleIcon();
        hasNetWork(mTitleConn);
    }

    /**
     * 是否显示消息提醒
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onEventIfHaveMessage(XmppEvent.EventPushMessageNum event) {
        if (Integer.parseInt(event.num) > 0) {
            LogUtils.i("Notifier", "mBaseTabBtnMsg.setActivated(true)  ");
            mTitleMsg=true;
        } else {
            LogUtils.i("Notifier", "mBaseTabBtnMsg.setActivated(false)  ");
            mTitleMsg=false;
        }
                if (Integer.parseInt(event.num) > 0) {
                    LogUtils.i(TAG, "mBaseTabBtnMsg.setActivated(true)  ");
                    if (mBaseTabBtnMsg!=null){
                        mBaseTabBtnMsg.setActivated(true);
                    }
                } else {
                    LogUtils.i(TAG, "mBaseTabBtnMsg.setActivated(false)  ");
                    if (mBaseTabBtnMsg!=null){
                        mBaseTabBtnMsg.setActivated(false);
                    }
                }

    }

    @Override
    protected void onResume() {
        super.onResume();
        selTitleIcon();
        setTitleMsg();
    }
    public  void setTitleMsg(){
        if (mTitleMsg){
            if (mBaseTabBtnMsg!=null){
                mBaseTabBtnMsg.setActivated(true);
            }
        }else {
            if (mBaseTabBtnMsg!=null){
                mBaseTabBtnMsg.setActivated(false);
            }
        }
    }
    public void selTitleIcon() {
        mBaseTabTvTitle.setVisibility(View.VISIBLE);
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
        LogUtils.i(TAG, "mTitleConn  "+mTitleConn);
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
                        MusicPlayer.getInstance().play(MusicPlayer.Type.LOGOUT_SUC);
                    }
                });
                builder.create().show();
                break;
            case R.id.base_tab_btn_msg:
                mContext.startActivity(new Intent(this, MessageActivity.class));

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
    /**
     * 分发触摸事件给所有注册了MyTouchListener的接口
     */


    /* 定义一个倒计时的内部类 */
    protected class TimeCount extends CountDownTimer {

        TextView textView;
        TextView leftText;

        public TimeCount(long millisInFuture, long countDownInterval,TextView leftText, TextView textView) {

            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
            this.textView = textView;
            this.leftText = leftText;
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            LogUtils.i(TAG, "onFinish     " );
            EventBusUtils.post(new Event.EventOverPut(true));

        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            LogUtils.i(TAG, "millisUntilFinished     " + millisUntilFinished);
            if (millisUntilFinished / 1000 <= 35) {
                textView.setText("确认并退出登录 " + "( " + millisUntilFinished / 1000 + " s )");
            } else {
                textView.setText("确认并退出登录");
            }
            if (millisUntilFinished / 1000 <= 2){
                leftText.setEnabled(false);
                textView.setEnabled(false);
            }
        }
    }
}
