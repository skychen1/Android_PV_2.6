package high.rivamed.myapplication.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.adapter.TimelyPublicAdapter;
import high.rivamed.myapplication.bean.Movie;

import static high.rivamed.myapplication.cont.Constants.ACTIVITY;
import static high.rivamed.myapplication.cont.Constants.STYPE_DIALOG;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/14 16:22
 * 描述:        Recyclerview的2个按钮的dialog
 * 包名:        high.rivamed.myapplication.fragment
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class RvDialog extends Dialog {

   public RvDialog(Context context) {
	super(context);
   }

   public RvDialog(Context context, int theme) {
	super(context, theme);
   }

   public static class Builder {
	EditText           mSearchEt;
	ImageView          mSearchIvDelete;
	FrameLayout        mStockSearch;
	LinearLayout       mLinearLayout;
	RecyclerView       mRecyclerview;
	SmartRefreshLayout mRefreshLayout;
	TextView           mDialogLeft;
	TextView           mDialogRight;
	private int                 mSize; //假数据 举例6个横向格子
	private View                mHeadView;
	private int                 mLayout;
	private TimelyPublicAdapter mPublicAdapter;
	private Context         mContext;
	private String          mMsgTwo;
	private String          mMsgText;
	private String          mLeftText;
	private String          mRightText;
	private OnClickListener mLeftBtn;
	private OnClickListener mRightBtn;
	private TextView        mRigtht;
	private TextView        mLeft;
	private int mLeftTextColor = -1;
	private int      mRightTextColor;
	private String      mType;
	private TextView mDialogMsg;
	private TextView mDialogRed;
	private Activity mActivity;

	public Builder(Activity mActivity,Context context) {
	   this.mContext = context;
	   this.mActivity = mActivity;

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

	public RvDialog create() {
	   LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
		   Context.LAYOUT_INFLATER_SERVICE);
	   final RvDialog dialog = new RvDialog(mContext, R.style.Dialog);
	   dialog.setCancelable(false);
	   View layout = inflater.inflate(R.layout.dialog_rv_layout, null);
	   dialog.addContentView(layout,
					 new ViewGroup.LayoutParams(1536,
									    ViewGroup.LayoutParams.WRAP_CONTENT));

	   mSearchEt = (EditText) layout.findViewById(R.id.search_et);
	   mSearchIvDelete = (ImageView) layout.findViewById(R.id.search_iv_delete);
	   mStockSearch = (FrameLayout) layout.findViewById(R.id.stock_search);
	   mLinearLayout = (LinearLayout) layout.findViewById(R.id.timely_ll);
	   mRecyclerview = (RecyclerView) layout.findViewById(R.id.recyclerview);
	   mRefreshLayout = (SmartRefreshLayout) layout.findViewById(R.id.refreshLayout);
	   mDialogLeft = (TextView) layout.findViewById(R.id.dialog_left);
	   mDialogRight = (TextView) layout.findViewById(R.id.dialog_right);

	   mSearchEt.setHint("请输入患者姓名、患者ID、手术间查询");
	   List<String> titeleList = new ArrayList<String>();
	   titeleList.add(0,"选择");
	   titeleList.add(1,"患者姓名");
	   titeleList.add(2,"患者ID");
	   titeleList.add(3,"手术时间");
	   titeleList.add(4,"医生");
	   titeleList.add(5,"手术间");

//	   String[] array = mContext.getResources().getStringArray(R.array.six_dialog_arrays);
//	   titeleList = Arrays.asList(array);
	   mSize = titeleList.size();

	   new TableTypeView(mContext, mActivity, titeleList, mSize, genData5(), mLinearLayout, mRecyclerview,
				   mRefreshLayout, ACTIVITY,STYPE_DIALOG);
//	   mLayout = R.layout.item_dialog_six_layout;
//	   mHeadView = mActivity.getLayoutInflater().inflate(R.layout.item_dialog_six_title_layout,
//								 (ViewGroup) mLinearLayout.getParent(), false);
//	   ((TextView) mHeadView.findViewById(R.id.seven_one)).setText(titeleList.get(0));
//	   ((TextView) mHeadView.findViewById(R.id.seven_two)).setText(titeleList.get(1));
//	   ((TextView) mHeadView.findViewById(R.id.seven_three)).setText(titeleList.get(2));
//	   ((TextView) mHeadView.findViewById(R.id.seven_four)).setText(titeleList.get(3));
//	   ((TextView) mHeadView.findViewById(R.id.seven_five)).setText(titeleList.get(4));
//	   ((TextView) mHeadView.findViewById(R.id.seven_six)).setText(titeleList.get(5));
//	   mType=STYPE_DIALOG;
//	   mPublicAdapter = new TimelyPublicAdapter(mLayout, genData5(), mSize,mType);
//	   mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
//	   mPublicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//		@Override
//		public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//
//		}
//	   });

	   if (mLeftTextColor != -1) {
		mDialogLeft.setTextColor(mLeftTextColor);
		mDialogRight.setTextColor(mRightTextColor);
	   }
	   mDialogLeft.setText(mLeftText);
	   mDialogRight.setText(mRightText);
	   mDialogLeft.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View view) {
		   mLeftBtn.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
		}
	   });
	   mDialogRight.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View view) {
		   mRightBtn.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
		}
	   });
	   return dialog;
	}
   }

   public static List<Movie> genData5() {

	ArrayList<Movie> list = new ArrayList<>();

	for (int i = 0; i < 15; i++) {
	   String three = "33333" + i;
	   String four = "2019-11-22 13:20";
	   String five = "王麻子"+i;
	   String six = i+"号手术间";
	   String two = "王麻"+i;

	   Movie movie = new Movie(null, two, three, four, five, six, null, null);
	   list.add(movie);
	}
	return list;
   }
}
