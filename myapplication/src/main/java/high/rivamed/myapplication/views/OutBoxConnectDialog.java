package high.rivamed.myapplication.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.adapter.gridviewAdapter;
import high.rivamed.myapplication.bean.DialogBean;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.HospNameBean;
import high.rivamed.myapplication.cont.Constants;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.UIUtils;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/25 19:42
 * 描述:        异常处理--出柜关联
 * 包名:        high.rivamed.myapplication.views
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class OutBoxConnectDialog extends Dialog {

    private static gridviewAdapter sAdapter;
    private static GridView sGridView;

    public OutBoxConnectDialog(Context context) {
        super(context);
    }

    public OutBoxConnectDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {

        private Context mContext;
        private int mNumColumn;
        private List<String> mMsgList;
        private String mMsgTitle;
        private String mLeftText;
        private String mRightText;
        private OnClickListener mLeftBtn;
        private OnClickListener mRightBtn;
        private TextView mTitle;
        private TextView mRigtht;
        private ImageView mLeft;
        private int mLeftTextColor = -1;
        private int mRightTextColor;
        private int mIntentType;
        private String mName;
        private String mCode;
        private boolean hasNext;

        public Builder(
                Context context,boolean hasNext, int NumColumn, int mIntentType) {
            this.mContext = context;
            this.mNumColumn = NumColumn;
            this.mIntentType = mIntentType;
            this.hasNext=hasNext;
        }

        public Builder setList(List title) {
            this.mMsgList = title;
            return this;
        }

        public Builder setTitle(String title) {
            this.mMsgTitle = title;
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

        public OutBoxConnectDialog create() {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            final OutBoxConnectDialog dialog = new OutBoxConnectDialog(mContext, R.style.Dialog);
            dialog.setCancelable(false);
            View layout = inflater.inflate(R.layout.dialog_storeroom_layout, null);
            mLeft = layout.findViewById(R.id.dialog_closs);
            mRigtht = layout.findViewById(R.id.dialog_sure);
            mTitle = layout.findViewById(R.id.storeroom_title);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    UIUtils.getContext().getResources().getDimensionPixelSize(R.dimen.x800),
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            sGridView = layout.findViewById(R.id.storeroom_view);
            sGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
            List<DialogBean> list = new ArrayList<>();
            mTitle.setText(mMsgTitle);
            sGridView.setNumColumns(mNumColumn);

            //异常处理：出柜关联
            for (int i = 0; i < Constants.EXCEPTION_DEAL_OUT_BOX_CONNECT.length; i++) {
                DialogBean dialogBean = new DialogBean();
                dialogBean.setName(Constants.EXCEPTION_DEAL_OUT_BOX_CONNECT[i]);
                dialogBean.setCode("" + i);
                list.add(dialogBean);
            }

            ViewGroup.LayoutParams lps = sGridView.getLayoutParams();
            if (list.size() >= 8) {
                lps.height = UIUtils.getContext().getResources().getDimensionPixelSize(R.dimen.y450);
            } else if (list.size() < 3) {
                lps.height = UIUtils.getContext().getResources().getDimensionPixelSize(R.dimen.y160) *
                        list.size();
            } else {
                lps.height = UIUtils.getContext().getResources().getDimensionPixelSize(R.dimen.y160) *
                        list.size() / 2;
            }
            sGridView.setLayoutParams(lps);
            sAdapter = new gridviewAdapter(mContext, R.layout.item_tag, list);
            sGridView.setAdapter(sAdapter);

            /*
             * 选中
             */
            if (list.size() > 0) {
                mCode = list.get(0).getCode();
                mName = list.get(0).getName();
            }

            LogUtils.i("OutBoxFoutActivity", "默认mName  " + mName + "   mCode " + mCode);
            sGridView.setOnItemClickListener((parent, view, position, id) -> {
                sAdapter.setSelected(position);
                sAdapter.notifyDataSetChanged();
                TextView textView = view.findViewById(R.id.tag);
                TextView goneText = view.findViewById(R.id.gone_tag);
                mName = textView.getText().toString();
                mCode = goneText.getText().toString();
                LogUtils.i("OutBoxFoutActivity", "mName  " + mName + "   mCode " + mCode);
                //异常处理-出柜关联
                EventBusUtils.post(new Event.outBoxEvent("105", mCode, dialog, hasNext?1:0));
            });
            if (mLeftTextColor != -1) {
                mRigtht.setTextColor(mRightTextColor);
            }
            if (hasNext){
                mRigtht.setText("下一步");
            }else {
                mRigtht.setText("确认");
            }
            mLeft.setOnClickListener(view -> mLeftBtn.onClick(dialog, DialogInterface.BUTTON_NEGATIVE));
            mRigtht.setOnClickListener(view -> {
                //根据配置是否需要绑定患者来显示是否有下一步
                //有下一步：点击列表项进行选择后，点击下一步，显示绑定患者操作
                //无下一步：点击列表项进行选择后，对话框消失；
                //          或者不点列表项直接点确定（默认选中第一项），对话框消失
                if (hasNext){
                    EventBusUtils.post(new Event.outBoxEvent("106", mCode, dialog, 0));
                }else {
                    EventBusUtils.post(new Event.outBoxEvent("105", mCode, dialog, hasNext?1:0));
                }
            });
            return dialog;
        }
    }

}
