package high.rivamed.myapplication.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.adapter.TimelyGroupCstAdapter;
import high.rivamed.myapplication.base.BaseSimpleActivity;
import high.rivamed.myapplication.dto.InventoryDto;
import high.rivamed.myapplication.dto.vo.InventoryGroupVos;
import high.rivamed.myapplication.dto.vo.InventoryVo;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

/**
 * 项目名称:    Android_PV_2.6.10
 * 创建者:      DanMing
 * 创建时间:    2019/11/6 0006 15:16
 * 描述:        实时盘点--耗材组
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class TimelyGroupCstActivity extends BaseSimpleActivity {

   @BindView(R.id.right_text)
   TextView           mRightText;
   @BindView(R.id.recyclerview)
   RecyclerView       mRecyclerview;
   @BindView(R.id.refreshLayout)
   SmartRefreshLayout mRefreshLayout;
   List<InventoryGroupVos> mInventoryGroupVos = new ArrayList<>();
   @BindView(R.id.seven_two)
   TextView mSevenTwo;
   @BindView(R.id.seven_four)
   TextView mSevenFour;
   @BindView(R.id.seven_five)
   TextView mSevenFive;
   @BindView(R.id.seven_six)
   TextView mSevenSix;
   private List<InventoryGroupVos> mGroupVos;
   private List<InventoryVo>       mVos;
   private String                  mDeviceCode;
   private String                  mgroupDto;
   private String                  mCstInventoryDto;

   @Override
   protected int getContentLayoutId() {
	return R.layout.act_group_layout;
   }

   public void initDataAndEvent(Bundle savedInstanceState) {
	super.initDataAndEvent(savedInstanceState);
	mBaseTabBack.setVisibility(View.VISIBLE);
	mBaseTabTvTitle.setVisibility(View.VISIBLE);
	mRightText.setVisibility(View.GONE);
	mBaseTabTvTitle.setText("耗材组详情");
	mSevenTwo.setText("合计数量");
	mSevenFour.setText("柜号");
	mSevenFive.setText("实际扫描");
	mSevenSix.setText("账面库存");
	mCstInventoryDto = getIntent().getStringExtra("mCstInventoryDto");
	mgroupDto = getIntent().getStringExtra("timelygroupdate");
	mDeviceCode = getIntent().getStringExtra("mDeviceCode");
	Log.i("ttttr", " mgroupDto    " + mgroupDto);
	Log.i("ttttr", " mDeviceCode    " + mDeviceCode);
	InventoryDto inventoryDto = mGson.fromJson(mCstInventoryDto, InventoryDto.class);
	mInventoryGroupVos = inventoryDto.getInventoryGroupVos();

	setAdapterDate();
   }

   private void setAdapterDate() {
	TimelyGroupCstAdapter cstAdapter = new TimelyGroupCstAdapter(mInventoryGroupVos, mgroupDto,
											 mDeviceCode);
	mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
	DividerItemDecoration itemDecoration = new DividerItemDecoration(mContext, VERTICAL);
	Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.recyclerdivider);
	itemDecoration.setDrawable(drawable);

	mRecyclerview.addItemDecoration(itemDecoration);
	mRefreshLayout.setEnableAutoLoadMore(false);
	mRefreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
	mRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
	View inflate = LayoutInflater.from(this).inflate(R.layout.recy_null, null);
	cstAdapter.setEmptyView(inflate);
	mRecyclerview.setAdapter(cstAdapter);
   }

}
