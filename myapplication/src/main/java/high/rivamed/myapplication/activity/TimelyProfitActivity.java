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
import static high.rivamed.myapplication.cont.Constants.STYPE_PROFIT_TYPE;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/20 14:39
 * 描述:        实时盘点 盘盈
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class TimelyProfitActivity extends BaseSimpleActivity{

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
   public int          mSize;
   public InventoryDto mDto;

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
	loadTimelyProfitDate();
   }
   /**
    * 获取盘盈数据
    */
   private void loadTimelyProfitDate() {
	mBaseTabBack.setVisibility(View.VISIBLE);
	mBaseTabTvTitle.setText("盘盈耗材详情");
	List<InventoryVo> inventoryVos = mDto.getInventoryVos();
	mTimelyNumber.setText(
		Html.fromHtml("盘盈数：<font color='#262626'><big>" + mDto.getAdd() + "</big></font>"));
	String[] array = mContext.getResources().getStringArray(R.array.seven_real_time_arrays);
	titeleList = Arrays.asList(array);
	mSize = array.length;
	mTypeView = new TableTypeView(this, this, inventoryVos, titeleList, mSize, mLinearLayout,
						mRecyclerview, mRefreshLayout, ACTIVITY, STYPE_PROFIT_TYPE, -10);
   }
}
