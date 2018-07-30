package high.rivamed.myapplication.adapter;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

import cn.rivamed.DeviceManager;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.RegisteTestBean;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.ToastUtils;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/18 13:57
 * 描述:        工程模式中功能验证adapter
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class RegisteTestAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

   public static final int TYPE_TEST_TITLE_BOX = 0;
   public static final int TYPE_TEST_TITLE     = 1;
   public static final int TYPE_TEST_CONTEXT   = 2;

   public RegisteTestAdapter(
	   List<MultiItemEntity> data) {
	super(data);
	addItemType(TYPE_TEST_TITLE_BOX, R.layout.item_testtitlebox_layout);
	addItemType(TYPE_TEST_TITLE, R.layout.item_testtitle_four_layout);
	addItemType(TYPE_TEST_CONTEXT, R.layout.item_testcontext_four_layout);
   }

   @Override
   protected void convert(
	   final BaseViewHolder helper, final MultiItemEntity item) {
	switch (helper.getItemViewType()) {
	   case TYPE_TEST_TITLE_BOX:
		if (helper.getAdapterPosition() != 0) {
		   Log.i("cc", "xxx+   " + helper.getAdapterPosition());
		   RelativeLayout layout = (RelativeLayout) helper.getView(R.id.rl);
		   RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
			   layout.getLayoutParams());
		   lp.setMargins(0, 10, 0, 0);
		   layout.setLayoutParams(lp);
		}
		final RegisteTestBean item1 = (RegisteTestBean) item;
		helper.setText(R.id.head_left_name, item1.namebox);
		helper.setImageResource(R.id.right_fold,
						item1.isExpanded() ? R.mipmap.top : R.mipmap.gcms_ic_gz_xl);
		helper.itemView.setOnClickListener(new View.OnClickListener() {
		   @Override
		   public void onClick(View v) {
			int pos = helper.getAdapterPosition();
			if (item1.isExpanded()) {
			   collapse(pos, false, true);
			} else {
			   expand(pos, false, true);
			}
		   }
		});
		break;
	   case TYPE_TEST_TITLE:
		RegisteTestBean.RegisteTestTitleBean item2 = (RegisteTestBean.RegisteTestTitleBean) item;

		((TextView) helper.getView(R.id.seven_one)).setText(item2.nametitle);
		((TextView) helper.getView(R.id.seven_two)).setText(item2.testtitle);
		((TextView) helper.getView(R.id.seven_three)).setText(item2.msgtitle);
		((TextView) helper.getView(R.id.seven_four)).setText(item2.commentstitle);

		break;
	   case TYPE_TEST_CONTEXT:
		int position = helper.getAdapterPosition();
		final RegisteTestBean.RegisteTestContextBean item3 = (RegisteTestBean.RegisteTestContextBean) item;
		((TextView) helper.getView(R.id.seven_one)).setText(item3.name);
		TextView twoText = (TextView) helper.getView(R.id.seven_two_text);
		TextView twoTextbg = (TextView) helper.getView(R.id.seven_two_textbg);
		RelativeLayout btnTwo = (RelativeLayout) helper.getView(R.id.seven_two);
		TextView threeText = (TextView) helper.getView(R.id.seven_three_text);
		ProgressBar threeLoading = (ProgressBar) helper.getView(R.id.seven_three_loading);
		RelativeLayout btnThree = (RelativeLayout) helper.getView(R.id.seven_three);
		threeText.setVisibility(View.GONE);
		if (item3.test.equals("开锁")) {
		   twoText.setVisibility(View.GONE);
		   twoTextbg.setText(item3.test);
		   twoTextbg.setVisibility(View.VISIBLE);
		} else if (item3.test.equals("设置功率")) {
		   twoText.setVisibility(View.GONE);
		   twoTextbg.setText(item3.test);
		   twoTextbg.setVisibility(View.VISIBLE);
		   threeText.setVisibility(View.VISIBLE);
		   threeLoading.setVisibility(View.GONE);
		   threeText.setText("当前功率：");
		} else if (item3.test.equals("开始")) {
		   twoText.setVisibility(View.GONE);
		   twoTextbg.setText(item3.test);
		   twoTextbg.setVisibility(View.VISIBLE);
		   threeText.setVisibility(View.VISIBLE);
		   threeLoading.setVisibility(View.GONE);
		   threeText.setText(item3.type);
		   threeText.setTextColor(mContext.getResources().getColor(R.color.color_green));
		} else {
		   twoText.setVisibility(View.VISIBLE);
		   twoText.setText(item3.test);
		   twoTextbg.setVisibility(View.GONE);

		}
		((TextView) helper.getView(R.id.seven_four)).setText(item3.comments);
		btnTwo.setOnClickListener(new View.OnClickListener() {
		   @Override
		   public void onClick(View v) {
			if (item3.test.equals("开锁")) {
			   ToastUtils.showShort("我打开了锁");

			} else if (item3.test.equals("设置功率")) {
			   DialogUtils.showWifiDialog(mContext);
			} else if (item3.test.equals("开始")) {

			   ToastUtils.showShort("开始");
			}
		   }
		});
		btnThree.setOnClickListener(new View.OnClickListener() {
		   @Override
		   public void onClick(View v) {
			if (item3.type.equals("查看读取结果")) {
			   DialogUtils.showEpcDialog(mContext);
			}
		   }
		});
		break;

	}

   }
}
