package high.rivamed.myapplication.adapter;

import android.support.annotation.Nullable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.TakeNotesDetailsBean;
import high.rivamed.myapplication.views.OnlyCodePopupWindow;

import static high.rivamed.myapplication.base.App.mDm;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/10/31 11:23
 * 描述:       使用记录明细
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class TakeNotesEpcAdapter
	extends BaseQuickAdapter<TakeNotesDetailsBean.JournalUseRecordDetailVos, BaseViewHolder> {

   private TextView mSeven_one;
   private TextView mSeven_two;
   private TextView mSeven_three;
   private TextView mSeven_four;
   private TextView mSeven_five;
   private TextView mSeven_six;
   private TextView mSeven_seven;
   private TextView mSeven_eight;

   public TakeNotesEpcAdapter(
	   int layoutResId, @Nullable List<TakeNotesDetailsBean.JournalUseRecordDetailVos> data) {
	super(layoutResId, data);
   }

   @Override
   protected void convert(
	   BaseViewHolder helper, TakeNotesDetailsBean.JournalUseRecordDetailVos item) {
	((LinearLayout) helper.getView(R.id.seven_ll)).setBackgroundResource(R.color.bg_f);
	helper.setText(R.id.seven_one, item.getEpc());
	helper.setText(R.id.seven_two, item.getCstName());
	helper.setText(R.id.seven_three, item.getCstSpec());
	helper.setText(R.id.seven_four, item.getOperationTime());
	helper.setText(R.id.seven_five, item.getDeviceName());
	helper.setText(R.id.seven_six, item.getUserName());
	helper.setText(R.id.seven_seven, item.getStatus());
	helper.setText(R.id.seven_eight,"展开");
	helper.getView(R.id.seven_eight).setOnClickListener(view ->{
	   OnlyCodePopupWindow window = new OnlyCodePopupWindow(mContext, item.getBarcode());
	   window.showPopupWindow(helper.getView(R.id.seven_eight),mDm.widthPixels);
	});
   }

}
