package high.rivamed.myapplication.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.BaseSimpleActivity;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.dto.InventoryDto;
import high.rivamed.myapplication.dto.vo.InventoryVo;
import high.rivamed.myapplication.views.TableTypeView;

import static high.rivamed.myapplication.cont.Constants.ACTIVITY;
import static high.rivamed.myapplication.cont.Constants.STYPE_TIMELY_FIVE_DETAILS;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/22 15:54
 * 描述:        实时盘点 耗材详情
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class TimelyDetailsActivity extends BaseSimpleActivity {
   public InventoryDto mDto;
   @BindView(R.id.timely_name)
   TextView           mTimelyName;
   @BindView(R.id.timely_number)
   TextView           mTimelyNumber;
   @BindView(R.id.timely_ll)
   LinearLayout       mLinearLayout;
   @BindView(R.id.recyclerview)
   RecyclerView       mRecyclerview;
   @BindView(R.id.refreshLayout)
   SmartRefreshLayout mRefreshLayout;

   TableTypeView mTypeView;
   List<String> titeleList = null;
   public  int                   mSize;
   /**
    * 盘点详情、盘亏、盘盈
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onTimelyEvent(Event.timelyDate event) {
	String s = event.type;
	mDto = event.mInventoryDto;
   }
   @Override
   protected int getContentLayoutId() {
	return R.layout.activity_midtype_layout;
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	super.initDataAndEvent(savedInstanceState);
	loadTimelyDetailsDate();
   }
   /**
    * 获取耗材盘点详情
    */
   private void loadTimelyDetailsDate() {
	mBaseTabBack.setVisibility(View.VISIBLE);
	mBaseTabTvTitle.setText("耗材详情");
	List<InventoryVo> inventoryVos = mDto.getInventoryVos();
	int number = 0;
	int Actual = 0;
	for (InventoryVo InventoryVo : inventoryVos) {
	   number += InventoryVo.getCountStock();
	   Actual += InventoryVo.getCountActual();
	}
	if (Actual == number) {
	   mTimelyNumber.setText(Html.fromHtml("实际扫描数：<font color='#262626'><big>" + Actual +
							   "</big>&emsp</font>账面库存数：<font color='#262626'><big>" +
							   number + "</big></font>"));
	} else {
	   mTimelyNumber.setText(Html.fromHtml("实际扫描数：<font color='#F5222D'><big>" + Actual +
							   "</big>&emsp</font>账面库存数：<font color='#262626'><big>" +
							   number + "</big></font>"));
	}

	mTimelyName.setVisibility(View.VISIBLE);
	mTimelyName.setText("耗材名称：" + mDto.getEpcName() + "    规格型号：" + mDto.getCstSpec());
	String[] array = mContext.getResources().getStringArray(R.array.timely_five_arrays);
	titeleList = Arrays.asList(array);
	mSize = array.length;
	mTypeView = new TableTypeView(this, this, inventoryVos, titeleList, mSize, mLinearLayout,
						mRecyclerview, mRefreshLayout, ACTIVITY,
						STYPE_TIMELY_FIVE_DETAILS, -10);
   }
}
