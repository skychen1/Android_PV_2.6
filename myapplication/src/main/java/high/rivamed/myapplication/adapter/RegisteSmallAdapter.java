package high.rivamed.myapplication.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.TBaseDevice;
import high.rivamed.myapplication.utils.RotateUtils;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.UIUtils;

import static android.widget.GridLayout.VERTICAL;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/13 9:53
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class RegisteSmallAdapter extends BaseQuickAdapter<TBaseDevice, BaseViewHolder> {

   private boolean            mType         = false;
   private SparseBooleanArray mExpanded = new SparseBooleanArray();
   public RegisteContextAdapter mHeadAdapter;
   private List<TBaseDevice.tBaseDevice> mList;

   public RegisteSmallAdapter(
	   int layoutResId, @Nullable List<TBaseDevice> data) {
	super(layoutResId, data);
   }

   @Override
   protected void convert(final BaseViewHolder holder, TBaseDevice item) {

	final TBaseDevice item1 = (TBaseDevice) item;
	EditText leftName = (EditText) holder.getView(R.id.head_left_name);
	final RecyclerView recyclerView2 = (RecyclerView) holder.getView(R.id.recyclerview2);
	ImageView rightDelete = (ImageView) holder.getView(R.id.right_delete);
	if (holder.getAdapterPosition() == 0) {
	   leftName.setText("1号柜");
	   rightDelete.setVisibility(View.GONE);
	} else if (item1.boxname.equals("")) {
	   leftName.setText("");
	   rightDelete.setVisibility(View.VISIBLE);
	}

	final ImageView rightFold = (ImageView) holder.getView(R.id.right_fold);
	rightFold.setOnClickListener(new View.OnClickListener() {
	   @Override
	   public void onClick(View v) {
		if (mExpanded.get(holder.getAdapterPosition())) {
		   ViewGroup.LayoutParams lps = recyclerView2.getLayoutParams();
		   lps.height = ViewGroup.LayoutParams.MATCH_PARENT;
		   recyclerView2.setLayoutParams(lps);
		   RotateUtils.rotateArrow(rightFold, true);
		   mExpanded.put(holder.getAdapterPosition(),false);
		} else {
		   ViewGroup.LayoutParams lps = recyclerView2.getLayoutParams();
		   lps.height = 0;
		   recyclerView2.setLayoutParams(lps);
		   RotateUtils.rotateArrow(rightFold, false);
		   mExpanded.put(holder.getAdapterPosition(),true);
		}
	   }
	});
	rightDelete.setOnClickListener(new View.OnClickListener() {
	   @Override
	   public void onClick(View v) {
	      mData.remove(holder.getAdapterPosition());
		notifyItemRemoved(holder.getAdapterPosition());
	   }
	});

	mList = item1.getList();
	mHeadAdapter = new RegisteContextAdapter(R.layout.item_foot_small_layout, mList, recyclerView2);
	recyclerView2.setLayoutManager(new LinearLayoutManager(mContext));
	recyclerView2.addItemDecoration(new DividerItemDecoration(mContext, VERTICAL));
	recyclerView2.setAdapter(mHeadAdapter);
	View view = mLayoutInflater.inflate(R.layout.item_head_small_layout,
							(ViewGroup) recyclerView2.getParent(), false);
	if (SPUtils.getBoolean(UIUtils.getContext(), "activationRegiste")){
	   view.findViewById(R.id.type_de).setVisibility(View.GONE);
	}else {
	   view.findViewById(R.id.type_de).setVisibility(View.VISIBLE);
	}
	mHeadAdapter.addHeaderView(view);
	mHeadAdapter.setOnItemClickListener(new OnItemClickListener() {
	   @Override
	   public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
		Log.i("xxa", position + "   我是多少条");
	   }
	});

   }
//   private RegisteContextAdapter.DeleteClickListener mDeleteListener =new RegisteContextAdapter.DeleteClickListener() {
//	@Override
//	public void myOnClick(int position, View v) {
//
//	   Log.i("xxa", position + "   条");
//	   mList.remove(position);
//	   mHeadAdapter.notifyItemRemoved(position+1);
//	}
//   };

}
