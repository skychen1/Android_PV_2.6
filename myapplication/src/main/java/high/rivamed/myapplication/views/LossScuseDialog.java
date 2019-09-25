package high.rivamed.myapplication.views;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.adapter.LossCauseAdapter;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.HospNameBean;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.UIUtils;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/25 19:42
 * 描述:        dialog_原因
 * 包名:        high.rivamed.myapplication.views
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class LossScuseDialog extends Dialog {



   public LossScuseDialog(Context context) {
	super(context);
   }

   public LossScuseDialog(Context context, int theme) {
	super(context, theme);
   }

   public static class Builder {
	private  LossCauseAdapter sAdapter;
	private  GridView        sGridView;
	private Context         mContext;
	private int             mNumColumn;
	private int             mType;
	private String          mMsgTitle;
	private OnClickListener mLeftBtn;
	private OnClickListener mRightBtn;
	private TextView        mTitle;
	private TextView        mRigtht;
	private ImageView       mLeft;
	private int mLeftTextColor = -1;
	private HospNameBean mHospNameBean;
	private String mName;
	private String mCode;
	private String          mRightText;
	private List<String>          mStringList;
	public Builder(Context context, List<String> strings) {
	   this.mContext = context;
	   this.mStringList = strings;
	}
	public Builder setRight(OnClickListener listener) {
	   this.mRightBtn = listener;
	   return this;
	}
	public Builder setTitle(String title) {
	   this.mMsgTitle = title;
	   return this;
	}

	public LossScuseDialog create() {
	   LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
		   Context.LAYOUT_INFLATER_SERVICE);
	   final LossScuseDialog dialog = new LossScuseDialog(mContext, R.style.Dialog);
	   dialog.setCancelable(false);
	   View layout = inflater.inflate(R.layout.dialog_storeroom_layout, null);
	   mLeft = (ImageView) layout.findViewById(R.id.dialog_closs);
	   mRigtht = (TextView) layout.findViewById(R.id.dialog_sure);
	   mTitle = (TextView) layout.findViewById(R.id.storeroom_title);
	   dialog.addContentView(layout,
					 new ViewGroup.LayoutParams(
						 UIUtils.getContext().getResources().getDimensionPixelSize(R.dimen.x680),
						 ViewGroup.LayoutParams.WRAP_CONTENT));
	   sGridView = (GridView) layout.findViewById(R.id.storeroom_view);
	   sGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));

	   mTitle.setText(mMsgTitle);
	   sGridView.setNumColumns(2);

	   LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
		   LinearLayout.LayoutParams.FILL_PARENT, 80);
	   lp.setMargins(80, 40, 80, 40);
	   mRigtht.setLayoutParams(lp);

	   ViewGroup.LayoutParams lps = sGridView.getLayoutParams();
	   if (mStringList.size() >= 8) {
		lps.height = 450;
	   } else if (mStringList.size()<3){
		lps.height = 140 * mStringList.size();
	   }else {
		lps.height = 140 * mStringList.size() / 2;
	   }
	   sGridView.setLayoutParams(lps);
	   sAdapter = new LossCauseAdapter(mContext, R.layout.item_tag, mStringList);
	   sGridView.setAdapter(sAdapter);

	   /*
	    * 选中
	    */
	   if (mStringList.size()>0){
		mName =mStringList.get(0);
	   }

	   LogUtils.i("Loss","默认mName  "+mName);
	   sGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		   sAdapter.setSelected(position);
		   sAdapter.notifyDataSetChanged();
		   TextView textView = (TextView)view.findViewById(R.id.tag);
		   mName = textView.getText().toString();
		   LogUtils.i("OutBoxFoutActivity","mName  "+mName);
		}
	   });

	   mRigtht.setText("确认");
	   mLeft.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View view) {
		   dialog.dismiss();
		}
	   });
	   mRigtht.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View view) {
		   EventBusUtils.post(new Event.EventLoss(mName));
		   dialog.dismiss();
		}
	   });
	   return dialog;
	}
   }

}
