package high.rivamed.myapplication.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.ExceptionRecordBean;
import high.rivamed.myapplication.utils.UIUtils;

/**
 * 项目名称：高值
 * 创建者：chenyanling
 * 创建时间：2019/5/13
 * 描述：异常处理
 */
public class ExceptionDealAdapter
	extends BaseQuickAdapter<ExceptionRecordBean.RowsBean, BaseViewHolder> {

   public ExceptionDealAdapter(@Nullable List<ExceptionRecordBean.RowsBean> data) {
	super(R.layout.item_excp_nine_layout, data);
   }

   @Override
   protected void convert(BaseViewHolder helper, ExceptionRecordBean.RowsBean item) {
	// TODO: 2019/5/17 设置数据
	//        boolean isUnknown = item.getOperator().equals("Unknown");
	        boolean isOperate = "出柜关联 移除处理 绑定患者".contains(item.getOperation());
	        int resIdOperate = item.getOperation().equals("已过期") ? R.color.color_overdue_prompt : (isOperate ? R.color.color_y : R.color.text_color_3);
	//        int resIdOperator = isUnknown ? R.color.white : R.color.text_color_3;
	helper.setChecked(R.id.nine_zero,item.isSelected());
	switch (item.getUnNormalSource()) {//来源
	   case "7":
		helper.setText(R.id.nine_one, "强开出柜");
		break;
	   case "5":
		helper.setText(R.id.nine_one, "强开入柜");
		break;
	   case "3":
		helper.setText(R.id.nine_one, "未绑定患者");
		break;
	   case "1":
		helper.setText(R.id.nine_one, "连续移除");
		break;
	   case "8":
		helper.setText(R.id.nine_one, "unKnown");
		break;
	}
	helper.setText(R.id.nine_two, item.getCstName());
	helper.setText(R.id.nine_three, item.getEpc());
	helper.setText(R.id.nine_four, item.getCstSpec());
	helper.setText(R.id.nine_five, item.getExpiryDate());
	helper.setText(R.id.nine_six, item.getDeviceName());
	helper.setText(R.id.nine_seven1, item.getOperatorName());
	helper.setText(R.id.nine_eight, item.getOperationTime());
	helper.setText(R.id.nine_nine1, item.getOperation());
	if (item.getOperatorName()!=null&&item.getOperatorName().equals("unknown")) {
	   helper.setTextColor(R.id.nine_seven1, UIUtils.getColor(R.color.color_y));
	} else {
	   helper.setTextColor(R.id.nine_seven1, UIUtils.getColor(R.color.text_color_3));
	}

	       helper.setTextColor(R.id.nine_nine1, UIUtils.getColor(resIdOperate));

	//
	//
	//        helper.setText(R.id.nine_seven1, item.getOperator())
	//                .setText(R.id.nine_nine1, item.getOperate())
//	                .setTextColor(R.id.nine_seven1, UIUtils.getColor(resIdOperator))
	//                .setTextColor(R.id.nine_nine1, UIUtils.getColor(resIdOperate));
	//
	//        if (isOperate) {
	//            helper.setBackgroundRes(R.id.nine_nine1, R.drawable.bg_btn_gray_sel)
	//                    .addOnClickListener(R.id.nine_nine1);//操作
	//        } else {
	//            helper.setBackgroundColor(R.id.nine_nine1, UIUtils.getColor(android.R.color.transparent));
	//        }
	//
	//        if (isUnknown) {
	//            helper.setBackgroundRes(R.id.nine_seven1, R.drawable.bg_btn_gray_sel)
	//                    .addOnClickListener(R.id.nine_seven1);//关联操作人
	//        } else {
	//            helper.setBackgroundColor(R.id.nine_seven1, UIUtils.getColor(android.R.color.transparent));
	//        }

   }
}
