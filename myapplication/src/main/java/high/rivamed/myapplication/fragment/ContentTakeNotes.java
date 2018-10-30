package high.rivamed.myapplication.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.Unbinder;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.BaseSimpleFragment;
import high.rivamed.myapplication.bean.Event;

/**
 * 项目名称:    Android_PV_2.6.1
 * 创建者:      DanMing
 * 创建时间:    2018/10/30 15:40
 * 描述:        使用记录
 * 包名:        high.rivamed.myapplication.fragment
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class ContentTakeNotes extends BaseSimpleFragment {

   @BindView(R.id.search_et)
   EditText           mSearchEt;
   @BindView(R.id.search_iv_delete)
   ImageView          mSearchIvDelete;
   @BindView(R.id.timely_ll)
   LinearLayout       mTimelyLl;
   @BindView(R.id.header)
   MaterialHeader     mHeader;
   @BindView(R.id.recyclerview)
   RecyclerView       mRecyclerview;
   @BindView(R.id.refreshLayout)
   SmartRefreshLayout mRefreshLayout;
   Unbinder unbinder;

   public static ContentTakeNotes newInstance() {
	Bundle args = new Bundle();
	ContentTakeNotes fragment = new ContentTakeNotes();
	fragment.setArguments(args);
	return fragment;
   }

   /**
    * 重新加载数据
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onStartFrag(Event.EventFrag event) {
	if (event.type.equals("START5")) {
	   initData();
	}
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	initData();

   }

   @Override
   protected int getContentLayoutId() {
	return R.layout.content_takenotes_layout;
   }

   private void initData() {
	mBaseTabBtnLeft.setVisibility(View.VISIBLE);
	mBaseTabTvTitle.setVisibility(View.VISIBLE);
	mBaseTabTvTitle.setText("使用记录");
	mSearchEt.setText("请输入耗材名称、型号规格查询");
   }


}
