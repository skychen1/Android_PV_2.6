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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.adapter.gridviewAdapter;
import high.rivamed.myapplication.bean.Movie;

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
	private int mRightTextColor;

	public Builder(Context context, int NumColumn,int mType) {
	   this.mContext = context;
	   this.mNumColumn = NumColumn;
	   this.mType = mType;
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
	   dialog.addContentView(layout,
					 new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
									    ViewGroup.LayoutParams.WRAP_CONTENT));
	   sGridView = (GridView) layout.findViewById(R.id.storeroom_view);
	   sGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
	   List<Movie> list = new ArrayList<>();
	   if (mNumColumn == 2) {//1.7和1.8退货
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.FILL_PARENT, 80);
		lp.setMargins(80,40,80,40);
		mRigtht.setLayoutParams(lp);
		if (mType==2){//1.7
		   String one;
		   for (int i = 1; i < 17; i++) {
			if (i == 1) {
			   one = "清理库存";
			} else if (i == 2) {
			   one = "耗材过期";
			} else if (i == 3) {
			   one = "型号错误";
			} else if (i == 4) {
			   one = "包装破损";
			} else if (i == 5) {
			   one = "厂家召回";
			} else {
			   one = "其他";
			}

			Movie movie = new Movie(one);
			list.add(movie);
		   }

		}else {
		   String one;
		   for (int i = 1; i < 26; i++) {
			if (i == 1) {
			   one = "骨科";
			} else if (i == 2) {
			   one = "心内科";
			} else if (i == 3) {
			   one = "呼吸科";
			} else if (i == 4) {
			   one = "放射科";
			} else  {
			   one = "神经内科";
			}

			Movie movie = new Movie(one);
			list.add(movie);
		   }
		}
		mTitle.setText(mMsgTitle);
		sGridView.setNumColumns(2);
		ViewGroup.LayoutParams lps = sGridView.getLayoutParams();
		if (list.size() >=8) {
		   lps.height = 450;
		} else {
		   lps.height = 130 * list.size()/2;
		}
		sGridView.setLayoutParams(lps);

	   } else {//1.6移出
		for (int i = 1; i < 30; i++) {
		   String one = i + "号柜";
		   Movie movie = new Movie(one);
		   list.add(movie);
		}
		ViewGroup.LayoutParams lps = sGridView.getLayoutParams();
		if (list.size() >=12) {
		   lps.height = 450;
		} else {
		   lps.height = 130 * list.size()/2;
		}
		sGridView.setLayoutParams(lps);
	   }


	   sAdapter = new gridviewAdapter(mContext, R.layout.item_tag, list, mNumColumn);
	   sGridView.setAdapter(sAdapter);

	   /*
	    * 选中
	    */
	   sGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		   sAdapter.setSelected(position);
		   sAdapter.notifyDataSetChanged();

		}
	   });

	   if (mLeftTextColor != -1) {
		mRigtht.setTextColor(mRightTextColor);
	   }
	   mRigtht.setText(mRightText);
	   mLeft.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View view) {
		   mLeftBtn.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
		}
	   });
	   mRigtht.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View view) {
		   mRightBtn.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
		}
	   });
	   return dialog;
	}
   }

}
