package high.rivamed.myapplication.adapter;

import android.util.SparseBooleanArray;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.dto.vo.InventoryVo;
import high.rivamed.myapplication.utils.UIUtils;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/19 17:08
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class StockLeftDownAdapter extends BaseQuickAdapter<InventoryVo, BaseViewHolder> {

   private TextView mSeven_one;
   private TextView mSeven_two;
   private TextView mSeven_three;
   private TextView mSeven_four;
   private TextView mSeven_five;

   public String TAG = "TimelyPublicAdapter";
   public  int                mSize;
   public  String             mType;
   private SparseBooleanArray mCheckStates2 = new SparseBooleanArray();

   public StockLeftDownAdapter(int layout, List<InventoryVo> inventoryVos, int size) {
	super(layout, inventoryVos);
	this.mSize = size;
	this.mData = inventoryVos;
   }


   public void clear() {

	if (mCheckStates2 != null) {
	   mCheckStates2.clear();
	}
	mData.clear();
	notifyDataSetChanged();
   }

   @Override
   protected void convert(final BaseViewHolder helper, InventoryVo item) {

	   findId(helper, mSize);
	   mSeven_one.setText(item.getCstName());
	   mSeven_two.setText(item.getCstSpec());
	   mSeven_three.setText(item.getCountStock()+"");
	   mSeven_four.setText(item.getDeviceName());

	if(item.getDeviceName()==null||item.getDeviceName().equals("")){
	   mSeven_four.setText("缺货");
	   mSeven_four.setTextColor(mContext.getResources().getColor(R.color.color_red));
	}else {
	   mSeven_four.setText(item.getDeviceName());
	   mSeven_four.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
	}
	if (mSize == 5) {
	   mSeven_five.setText(item.getStockMin());
	}
   }



   private void findId(BaseViewHolder helper, int size) {
	mSeven_one = ((TextView) helper.getView(R.id.seven_one));
	mSeven_two = ((TextView) helper.getView(R.id.seven_two));
	mSeven_three = ((TextView) helper.getView(R.id.seven_three));
	mSeven_four = ((TextView) helper.getView(R.id.seven_four));
	if (size == 5) {
	   mSeven_five = ((TextView) helper.getView(R.id.seven_five));
	}

   }
}
