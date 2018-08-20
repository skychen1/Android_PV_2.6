package high.rivamed.myapplication.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import butterknife.OnClick;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.App;
import high.rivamed.myapplication.base.BaseTimelyActivity;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.views.SettingPopupWindow;
import high.rivamed.myapplication.views.TwoDialog;

import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_CONFIRM_RECEIVE;

/*
 识别耗材页面
* */
public class RecognizeActivity extends BaseTimelyActivity {

    private CountDownTimer mStart;

    @Override
    public int getCompanyType() {
        super.my_id = ACT_TYPE_CONFIRM_RECEIVE;
        return my_id;
    }

    @Override
    public void initDataAndEvent(Bundle savedInstanceState) {
        super.initDataAndEvent(savedInstanceState);
        DialogUtils.showNoDialog(mContext, "柜门已开!", 2, "form", null);
        mStart = new TimeCount(15000, 1000, mTimelyRight);
//        mStart.start();
    }

    /* 定义一个倒计时的内部类 */
    private class TimeCount extends CountDownTimer {

        TextView textView;

        public TimeCount(long millisInFuture, long countDownInterval, TextView textView) {

            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
            this.textView = textView;
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            mContext.startActivity(new Intent(mContext, LoginActivity.class));
            App.getInstance().removeALLActivity_();
            ToastUtils.showShort("耗材领用成功");
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            textView.setText("确认并退出登录  " + "(" + millisUntilFinished / 1000 + "s)");
        }
    }

    @OnClick({R.id.timely_left, R.id.timely_right, R.id.base_tab_tv_name, R.id.base_tab_icon_right, R.id.base_tab_btn_msg,
            R.id.base_tab_back, R.id.timely_start_btn, R.id.activity_btn_one})
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
                mStart.cancel();
                finish();
                break;
            case R.id.timely_start_btn:
                break;
            case R.id.timely_left:
                //                DialogUtils.showTwoDialog(mContext, 2, "耗材领用成功", "");
                finish();
                ToastUtils.showShort("耗材领用成功");
                break;
            case R.id.timely_right:
                mStart.cancel();
                mContext.startActivity(new Intent(mContext, LoginActivity.class));
                App.getInstance().removeALLActivity_();
                ToastUtils.showShort("耗材领用成功");
                break;

        }
    }
}
