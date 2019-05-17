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
import high.rivamed.myapplication.fragment.PublicExceptionFrag;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.UIUtils;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/25 19:42
 * 描述:        dialog_退货
 * 包名:        high.rivamed.myapplication.views
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class StoreRoomDialog extends Dialog {

   private static gridviewAdapter sAdapter;
   private static GridView        sGridView;

   public StoreRoomDialog(Context context) {
	super(context);
   }

   public StoreRoomDialog(Context context, int theme) {
	super(context, theme);
   }

   public static class Builder {

	private Context         mContext;
	private int             mNumColumn;
	private int             mType;
	private List<String>    mMsgList;
	private String          mMsgTitle;
	private String          mLeftText;
	private String          mRightText;
	private OnClickListener mLeftBtn;
	private OnClickListener mRightBtn;
	private TextView        mTitle;
	private TextView        mRigtht;
	private ImageView       mLeft;
	private int mLeftTextColor = -1;
	private int          mRightTextColor;
	private int          mIntentType;
	private HospNameBean mHospNameBean;
	private String       mName;
	private String       mCode;

	public Builder(
		Context context, int NumColumn, int mType, HospNameBean hospNameBean, int mIntentType) {
	   this.mContext = context;
	   this.mNumColumn = NumColumn;
	   this.mType = mType;
	   this.mHospNameBean = hospNameBean;
	   this.mIntentType = mIntentType;
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

	public StoreRoomDialog create() {
	   LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
		   Context.LAYOUT_INFLATER_SERVICE);
	   final StoreRoomDialog dialog = new StoreRoomDialog(mContext, R.style.Dialog);
	   dialog.setCancelable(false);
	   View layout = inflater.inflate(R.layout.dialog_storeroom_layout, null);
	   mLeft = (ImageView) layout.findViewById(R.id.dialog_closs);
	   mRigtht = (TextView) layout.findViewById(R.id.dialog_sure);
	   mTitle = (TextView) layout.findViewById(R.id.storeroom_title);
	   dialog.addContentView(layout, new ViewGroup.LayoutParams(
		   UIUtils.getContext().getResources().getDimensionPixelSize(R.dimen.x800),
		   ViewGroup.LayoutParams.WRAP_CONTENT));
	   sGridView = (GridView) layout.findViewById(R.id.storeroom_view);
	   sGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
	   List<DialogBean> list = new ArrayList<>();
	   mTitle.setText(mMsgTitle);
	   sGridView.setNumColumns(mNumColumn);

//	   LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//		   LinearLayout.LayoutParams.MATCH_PARENT, mContext.getResources().getDimensionPixelSize(R.dimen.y80));
//	   lp.setMargins(mContext.getResources().getDimensionPixelSize(R.dimen.x80),
//			     mContext.getResources().getDimensionPixelSize(R.dimen.x80),
//			     mContext.getResources().getDimensionPixelSize(R.dimen.x80),
//			     mContext.getResources().getDimensionPixelSize(R.dimen.x80));
//	   mRigtht.setLayoutParams(lp);
	   if(mType==104){
	   	//异常处理：连续移除处理选择
		   for (int i = 0; i < Constants.EXCEPTION_DEAL_REMOVE_JUDGE.length; i++) {
			   DialogBean dialogBean = new DialogBean();
			   dialogBean.setName(Constants.EXCEPTION_DEAL_REMOVE_JUDGE[i]);
			   dialogBean.setCode(""+i);
			   list.add(dialogBean);
		   }
	   } else if (mType == 2) {////1.7和1.8退货
		   for (int i = 0; i < Constants.REASON.length; i++) {
			   DialogBean dialogBean = new DialogBean();
			   dialogBean.setName(Constants.REASON[i]);
			   list.add(dialogBean);
		   }
//		   String one;
//		for (int i = 1; i < 7; i++) {
//		   if (i == 1) {
//			one = "清理库存";
//		   } else if (i == 2) {
//			one = "耗材过期";
//		   } else if (i == 3) {
//			one = "型号错误";
//		   } else if (i == 4) {
//			one = "包装破损";
//		   } else if (i == 5) {
//			one = "厂家召回";
//		   } else {
//			one = "其他";
//		   }
//
//		   DialogBean dialogBean = new DialogBean();
//		   dialogBean.setName(one);
//		   list.add(dialogBean);
//		}

	   } else if (mType == 1) {//1.6移出
		List<HospNameBean.StoreHousesBean> storehouses = mHospNameBean.getStorehouses();

		for (int i = 0; i < storehouses.size(); i++) {
		   DialogBean dialogBean = new DialogBean();
		   dialogBean.setCode(storehouses.get(i).getSthId());
		   dialogBean.setName(storehouses.get(i).getSthName());
		   list.add(dialogBean);
		}

	   } else {//调拨
		List<HospNameBean.StoreHousesBean> storeHouses = mHospNameBean.getStorehouses();
		for (int i = 0; i < storeHouses.size(); i++) {
		   DialogBean dialogBean = new DialogBean();
		   dialogBean.setCode(storeHouses.get(i).getSthId());
		   dialogBean.setName(storeHouses.get(i).getSthName());
		   list.add(dialogBean);
		}
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
	   sGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		   sAdapter.setSelected(position);
		   sAdapter.notifyDataSetChanged();
		   TextView textView = (TextView) view.findViewById(R.id.tag);
		   TextView goneText = (TextView) view.findViewById(R.id.gone_tag);
		   mName = textView.getText().toString();
		   mCode = goneText.getText().toString();
		   LogUtils.i("OutBoxFoutActivity", "mName  " + mName + "   mCode " + mCode);
		}
	   });

	   if (mLeftTextColor != -1) {
		mRigtht.setTextColor(mRightTextColor);
	   }
	   mRigtht.setText("确认");
	   mLeft.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View view) {
		   mLeftBtn.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
		}
	   });
	   mRigtht.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View view) {
		   if (mType == 1) {
			EventBusUtils.post(new Event.outBoxEvent("x", mCode, dialog, mIntentType));
		   } else if (mType == 2) {
			EventBusUtils.post(new Event.outBoxEvent("2", mName, dialog, mIntentType));
		   } else if (mType == 4) {
		   	//异常处理-移除判断
			EventBusUtils.post(new Event.outBoxEvent("104", mCode, dialog, mIntentType));
		   } else {
			EventBusUtils.post(new Event.outBoxEvent("3", mCode, dialog, mIntentType));
		   }
		}
	   });
	   return dialog;
	}
   }

}
