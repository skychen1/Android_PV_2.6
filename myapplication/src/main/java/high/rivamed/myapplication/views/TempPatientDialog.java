package high.rivamed.myapplication.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.HospNameBean;
import high.rivamed.myapplication.bean.Movie;
import high.rivamed.myapplication.bean.SelectBean;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.timeutil.PowerDateUtils;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;

import static high.rivamed.myapplication.cont.Constants.SAVE_DEPT_CODE;

/**
 * 创建临时患者弹窗
 */

public class TempPatientDialog extends Dialog {


    public TempPatientDialog(Context context, int theme) {
        super(context, theme);
    }


    @Override
    public void show() {
        super.show();
        //	WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        //
        //	layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        //	layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        //	getWindow().getDecorView().setPadding(530, 200, 0, 0);
        //	getWindow().setAttributes(layoutParams);
    }

    public static class Builder {

        private static final String TAG = "RegisteDialog";
        ImageView mDialogCloss;
        EditText mAddressOne;
        TextView mAddressTwo;
        TextView mAddressThree;
        EditText mAddressFour;
        TextView mAddressFive;
        TextView mDialogLeft;
        TextView mDialogRight;
        private Context mContext;
        private Activity mActivity;
        private String mMsgTwo;
        private String mMsgText;
        private String mLeftText;
        private String mRightText;
        private OnClickListener mLeftBtn;
        private OnClickListener mRightBtn;
        private TextView mRigtht;
        private TextView mLeft;
        private int mLeftTextColor = -1;
        private int mRightTextColor;
        private int mType;
        private TextView mDialogMsg;
        private TextView mDialogBtn;
        //	List<Movie> mMovies;
        List<Movie> mMovies1 = new ArrayList<>();
        private CreatTempPopupWindow mPopWindow;
        private TextView mGoneOneType;
        private List<HospNameBean.TbaseHospitalsBean> mHospitalsName;
        private TextView mGoneFiveType;
        private TextView mGoneFourType;
        private TextView mGoneThreeType;
        private TextView mGoneTwoType;

        public Builder(Context context, Activity activity) {
            this.mContext = context;
            this.mActivity = activity;

        }

        public Builder setTwoMsg(String title) {
            this.mMsgTwo = title;
            return this;
        }

        public Builder setMsg(String msg) {
            this.mMsgText = msg;
            return this;
        }

        public Builder setLeft(String left, int color, OnClickListener listener) {
            this.mLeftText = left;
            this.mLeftTextColor = color;
            this.mLeftBtn = listener;
            return this;
        }

        public Builder setLeft(String left, OnClickListener listener) {
            this.mLeftText = left;
            this.mLeftBtn = listener;
            return this;
        }

        public Builder setRight(String left, int color, OnClickListener listener) {
            this.mRightText = left;
            this.mRightTextColor = color;
            this.mRightBtn = listener;
            return this;
        }

        public Builder setRight(String left, OnClickListener listener) {
            this.mRightText = left;
            this.mRightBtn = listener;
            return this;
        }

        public TempPatientDialog create() {

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            final TempPatientDialog dialog = new TempPatientDialog(mContext, R.style.Dialog);
            dialog.setCanceledOnTouchOutside(false);
            final View layout = inflater.inflate(R.layout.dialog_temp_layout, null);

            dialog.addContentView(layout, new ViewGroup.LayoutParams(860,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            //		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0000000000));
            dialog.getWindow().setBackgroundDrawable(
                    new ColorDrawable(Color.TRANSPARENT));//取消对话框黑底色

            mDialogCloss = (ImageView) layout.findViewById(R.id.dialog_closs);
            mGoneOneType = (TextView) layout.findViewById(R.id.gone_one_type);
            mAddressOne = (EditText) layout.findViewById(R.id.address_one);
            mAddressTwo = (TextView) layout.findViewById(R.id.address_two);
            mAddressThree = (TextView) layout.findViewById(R.id.address_three);
            mAddressFour = (EditText) layout.findViewById(R.id.address_four);
            mAddressFive = (TextView) layout.findViewById(R.id.address_five);
            mGoneTwoType = (TextView) layout.findViewById(R.id.gone_two_type);
            mGoneThreeType = (TextView) layout.findViewById(R.id.gone_three_type);
            mGoneFourType = (TextView) layout.findViewById(R.id.gone_four_type);
            mGoneFiveType = (TextView) layout.findViewById(R.id.gone_five_type);

            mDialogLeft = (TextView) layout.findViewById(R.id.dialog_left);
            mDialogRight = (TextView) layout.findViewById(R.id.dialog_right);

            mAddressThree.setText("未知");

            UIUtils.setInputLenWithNoBlank(mAddressOne, 16);
            UIUtils.setInputLenWithNoBlank(mAddressFour, 33);

            mAddressTwo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadRoomNum();

                }
            });
            mAddressThree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<SelectBean> list = new ArrayList<>();
                    list.add(new SelectBean("男"));
                    list.add(new SelectBean("女"));
                    list.add(new SelectBean("未知"));
                    setAdapterDate(list, mAddressThree, mGoneThreeType);
                }
            });

            mAddressFive.setText(PowerDateUtils.getNowDateString4());
            mAddressFive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //                    final Calendar calendar = Calendar.getInstance();
                    //                    DatePickerDialog dialog = new DatePickerDialog(mContext,
                    //                            new DatePickerDialog.OnDateSetListener() {
                    //                                @Override
                    //                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    //                                    LogUtils.d(TAG, "onDateSet: year: " + year + ", month: " + month + ", dayOfMonth: " + dayOfMonth);
                    //
                    //                                    calendar.set(Calendar.YEAR, year);
                    //                                    calendar.set(Calendar.MONTH, month);
                    //                                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    //                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    //                                    String str = formatter.format(calendar.getTime());
                    //                                    mAddressFive.setText(str);
                    //                                }
                    //                            },
                    //                            calendar.get(Calendar.YEAR),
                    //                            calendar.get(Calendar.MONTH),
                    //                            calendar.get(Calendar.DAY_OF_MONTH));
                    //                    dialog.setTitle("手术时间");
                    //                    dialog.getDatePicker().setMinDate(System.currentTimeMillis()); //设置日期最小值
                    //                    dialog.show();
                    DialogUtils.showTimeDialog2(mContext, mAddressFive);
                }
            });

            mDialogCloss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            mDialogLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mLeftBtn.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                }
            });
            mDialogRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!UIUtils.isFastDoubleClick()) {
                        if (myListener != null) {
                            String three = mAddressThree.getText().toString().trim();
                            String four = mAddressFour.getText().toString().trim();
                            String five = mAddressFive.getText().toString().trim();
                            String two = mAddressTwo.getText().toString().trim();
                            String one = mAddressOne.getText().toString().trim();
                            if (TextUtils.isEmpty(one)) {
                                ToastUtils.showShort("请输入患者姓名");
                            } else if (TextUtils.isEmpty(two)) {
                                ToastUtils.showShort("请选择手术间号");
                            } else {
                                myListener.getDialogDate(one, two, three, four, five, dialog);
                            }
                        }
                    }
                }
            });

            return dialog;
        }

        /*
         * 加载手术间号
         * */
        private void loadRoomNum() {
            NetRequest.getInstance().getHospRooms(SPUtils.getString(UIUtils.getContext(), SAVE_DEPT_CODE, ""), mActivity, new BaseResult() {
                @Override
                public void onSucceed(String result) {
                    Gson gson = new Gson();
                    HospNameBean hospNameBean = gson.fromJson(result, HospNameBean.class);
                    LogUtils.i(TAG, "result getHospRooms   " + result);

                    List<SelectBean> list = new ArrayList<>();
                    List<HospNameBean.TbaseOperationRoomsBean> tbaseOperationRooms = hospNameBean.getTbaseOperationRooms();
                    for (int i = 0; i < tbaseOperationRooms.size(); i++) {
                        list.add(new SelectBean(tbaseOperationRooms.get(i).getRoomNo()));
                    }
                    setAdapterDate(list, mAddressTwo, mGoneTwoType);
                }
            });

        }


        private SettingListener myListener = null;

        public interface SettingListener {
            public void getDialogDate(String userName, String roomNum, String userSex, String idCard, String time, Dialog dialog);
        }

        public void setOnSettingListener(SettingListener listener) {
            myListener = listener;
        }


        /**
         * 给列表赋值
         *
         * @param textview
         * @param goneview
         */
        private void setAdapterDate(
                List<SelectBean> list, TextView textview, TextView goneview) {
            if (list != null) {
                mPopWindow = new CreatTempPopupWindow(mContext, list);
                mPopWindow.showPopupWindow(textview);
                mPopWindow.mAdar.setOnItemClickListener(
                        new BaseQuickAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(
                                    BaseQuickAdapter adapter, View view, int position) {
                                textview.setText(list.get(position).getContent());
                                goneview.setText(list.get(position).getContent());
                                mPopWindow.dismiss();
                            }
                        });

            } else {
                mPopWindow.dismiss();
            }
        }
    }

}
