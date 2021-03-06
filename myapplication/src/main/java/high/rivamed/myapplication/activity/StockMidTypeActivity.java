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
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.UIUtils;
import high.rivamed.myapplication.views.TableTypeView;

import static high.rivamed.myapplication.cont.Constants.ACTIVITY;
import static high.rivamed.myapplication.cont.Constants.KEY_USER_NAME;
import static high.rivamed.myapplication.cont.Constants.KEY_USER_SEX;
import static high.rivamed.myapplication.cont.Constants.STYPE_STOCK_DETAILS;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/22 15:54
 * 描述:        库存的耗材详情
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class StockMidTypeActivity extends BaseSimpleActivity {

   private static final String TAG = "StockMidTypeActivity";
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
   public  int               mSize;
   private List<InventoryVo> mStockDetailsDownList;
   private InventoryVo       mStockDetailsTopBean;
   private int mExpireStatus;
   /**
    * 拿到耗材详情的数据
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
   public void onMidTypeEvent(Event.EventStockDetailVo event) {
	mStockDetailsTopBean = event.vosBean;

   }
   @Override
   protected int getContentLayoutId() {
	return R.layout.activity_midtype_layout;
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	super.initDataAndEvent(savedInstanceState);
	mExpireStatus = getIntent().getIntExtra("expireStatus", -1);
	loadStockDetails();
   }

   /**
    * 耗材详情
    */
   private void loadStockDetails() {
	mBaseTabBack.setVisibility(View.VISIBLE);
	if (SPUtils.getString(UIUtils.getContext(), KEY_USER_NAME).equals("")&&SPUtils.getString(UIUtils.getContext(), KEY_USER_SEX).equals("")){
	   mBaseTabTvName.setVisibility(View.GONE);
	   mBaseTabBtnMsg.setVisibility(View.GONE);
	   mBaseTabIconRight.setVisibility(View.GONE);
	   mBaseTabOutLogin.setVisibility(View.GONE);
	}
	mBaseTabTvTitle.setText("耗材详情");
	String deviceCode = mStockDetailsTopBean.getDeviceId();
	String cstId = mStockDetailsTopBean.getCstCode();
	String[] array = mContext.getResources().getStringArray(R.array.stock_five_arrays);
	titeleList = Arrays.asList(array);
	mSize = array.length;
	NetRequest.getInstance().getStockDetailDate(mExpireStatus,deviceCode, cstId, mContext, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {

		LogUtils.i(TAG, "result  " + result);
		InventoryDto inventoryDto = mGson.fromJson(result, InventoryDto.class);
		mStockDetailsDownList = inventoryDto.getInventoryVos();
		mTimelyNumber.setText(Html.fromHtml(
			"耗材数量：<font color='#262626'><big>" + mStockDetailsTopBean.getCountStock() +
			"</big></font>"));
		mTimelyName.setVisibility(View.VISIBLE);
		mTimelyName.setText("耗材名称：" + mStockDetailsTopBean.getCstName() + "    规格型号：" +
					  mStockDetailsTopBean.getCstSpec());
		mTypeView = new TableTypeView(mContext, mContext, mStockDetailsDownList, titeleList,
							mSize, mLinearLayout, mRecyclerview, mRefreshLayout,
							ACTIVITY, STYPE_STOCK_DETAILS, -10);
	   }
	});

   }

}
