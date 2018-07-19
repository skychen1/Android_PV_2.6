package high.rivamed.myapplication.adapter;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.Movie;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/25 14:09
 * 描述:        请领单
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class OutFormAdapter extends BaseQuickAdapter<Movie, BaseViewHolder>{

   TextView mFormCardNumber;
   TextView mFormCardName;
   TextView mFormCardId;
   CardView mFormCardview;
   LinearLayout mFormCardLL;
   public int selectedPosition = -5; //默认一个参数
   public OutFormAdapter(int layout, List<Movie> data) {
      super(layout, data);
   }

   @Override
   protected void convert(final BaseViewHolder helper, Movie item) {
      findId(helper);

	mFormCardNumber.setText("请领单:"+item.one);
	mFormCardName.setText("患者姓名：" + item.two);
	mFormCardId.setText("患者ID：" + item.three);
	clickItem(helper);

   }

   public void clickItem(final BaseViewHolder helper){
	mFormCardLL.setSelected(selectedPosition == helper.getAdapterPosition());
	if (selectedPosition ==  helper.getAdapterPosition()) {
	   mFormCardLL.setSelected(true);
	} else {
	   mFormCardLL.setSelected(false);
	}
	mFormCardLL.setOnClickListener(new View.OnClickListener() {
	   @Override
	   public void onClick(View v) {
		onItemClickListener.OnItemClick(v, helper,helper.getAdapterPosition());
		selectedPosition = helper.getAdapterPosition(); //选择的position赋值给参数，
		//		notifyItemChanged(selectedPosition);//刷新当前点击item
		notifyDataSetChanged();
	   }
	});
   }
   private void findId(BaseViewHolder helper) {

	mFormCardNumber = ((TextView) helper.getView(R.id.form_card_number));
	mFormCardName = ((TextView) helper.getView(R.id.form_card_name));
	mFormCardId = ((TextView) helper.getView(R.id.form_card_id));
	mFormCardview = ((CardView) helper.getView(R.id.form_cardview));
	mFormCardLL = ((LinearLayout) helper.getView(R.id.form_card_ll));


   }
   private OnItemClickListener onItemClickListener;

   public interface OnItemClickListener {  //定义接口，实现Recyclerview点击事件
	void OnItemClick(View view,BaseViewHolder helper, int position);
   }

   public void setOnItemClickListener(OnItemClickListener onItemClickListener) {   //实现点击
	this.onItemClickListener = onItemClickListener;
   }

}
