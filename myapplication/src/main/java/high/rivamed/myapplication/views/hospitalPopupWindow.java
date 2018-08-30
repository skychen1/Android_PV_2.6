package high.rivamed.myapplication.views;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.adapter.HospPopAdapter;
import high.rivamed.myapplication.adapter.HospPopFiveAdapter;
import high.rivamed.myapplication.adapter.HospPopFourAdapter;
import high.rivamed.myapplication.adapter.HospPopThreeAdapter;
import high.rivamed.myapplication.adapter.HospPopTwoAdapter;
import high.rivamed.myapplication.bean.HospNameBean;
import high.rivamed.myapplication.utils.UIUtils;

import static android.widget.LinearLayout.VERTICAL;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/15 15:20
 * 描述:        医院选择的PopupWindow
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class hospitalPopupWindow extends PopupWindow {

   // 坐标的位置（x、y）
   private final int[] mLocation = new int[2];

   private final RecyclerView mRecyclerView;
   private final View         mView;
  TextView mGoneView;

   private String TAG = "SettingPopupWindow";
   private       OnClickListener                       mItemClickListener;
   public        HospPopAdapter                        mHospPopAdar;
   public  HospPopTwoAdapter mHospPopTwoAdapter;
   public HospPopThreeAdapter mThreeAdapter;
   public  HospPopFourAdapter mFourAdapter;
   public  HospPopFiveAdapter mFiveAdapter;

   public hospitalPopupWindow(Context context, HospNameBean hospNameBean,int type) {
	mView = LayoutInflater.from(context).inflate(R.layout.mac_popupwindow, null);
	mRecyclerView = (RecyclerView) mView.findViewById(R.id.search_rv);

	if (type == 1) {
	   List<HospNameBean.TbaseHospitalsBean> tbaseHospitals = hospNameBean.getTbaseHospitals();
	   mHospPopAdar = new HospPopAdapter(R.layout.item_mac_single, tbaseHospitals);
	   mRecyclerView.setAdapter(mHospPopAdar);
	   heightMeth(tbaseHospitals.size());
	}else if (type==2){
	   List<HospNameBean.TbaseInfoBean> tbaseInfo = hospNameBean.getTbaseInfo();
	   mHospPopTwoAdapter = new HospPopTwoAdapter(R.layout.item_mac_single,
								    tbaseInfo);
	   mRecyclerView.setAdapter(mHospPopTwoAdapter);
	   heightMeth(tbaseInfo.size());
	}else if (type ==3){
	   List<HospNameBean.TbaseInfoBean> tbaseInfo = hospNameBean.getTbaseInfo();
	   mThreeAdapter = new HospPopThreeAdapter(R.layout.item_mac_single,
								 tbaseInfo);

	   mRecyclerView.setAdapter(mThreeAdapter);
	   heightMeth(tbaseInfo.size());
	}else if (type ==4){
	   List<HospNameBean.TcstBaseStorehousesBean> baseStorehouses = hospNameBean.getTcstBaseStorehouses();
	   mFourAdapter = new HospPopFourAdapter(R.layout.item_mac_single,
							     baseStorehouses);
	   mRecyclerView.setAdapter(mFourAdapter);
	   heightMeth(baseStorehouses.size());
	}else {
	   List<HospNameBean.TbaseOperationRoomsBean> operationRooms = hospNameBean.getTbaseOperationRooms();
	   mFiveAdapter = new HospPopFiveAdapter(R.layout.item_mac_single,
							     operationRooms);
	   mRecyclerView.setAdapter(mFiveAdapter);
	   heightMeth(operationRooms.size());
	}
	mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
	mRecyclerView.addItemDecoration(new DividerItemDecoration(context, VERTICAL));

	//	mMealPopAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
	//	   @Override
	//	   public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
	//		TextView textView = (TextView) view.findViewById(R.id.item_meal);
	//		String trim = textView.getText().toString().trim();
	//		//		mEditText.setText(trim);
	//		EventBusUtils.postSticky(new Event.PopupEvent(true, trim));
	//	   }
	//	});



   }

   private void heightMeth(int size) {
	ViewGroup.LayoutParams lp = mRecyclerView.getLayoutParams();
	if (size > 8) {
	   lp.height = 370;
	} else {
	   lp.height = 62 * size;
	}
	mRecyclerView.setLayoutParams(lp);
   }

   public void showPopupWindow(View parent) {
	parent.getLocationOnScreen(mLocation);

	this.setContentView(mView);
	this.setWidth(parent.getWidth());
	this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
	this.setFocusable(false);
	this.setOutsideTouchable(true);
	this.update();
	// 实例化一个ColorDrawable颜色为半透明
	ColorDrawable dw = new ColorDrawable(0000000000);
	// 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
	this.setBackgroundDrawable(dw);
	this.setAnimationStyle(R.style.social_pop_anim);

	if (!this.isShowing()) {
	   int dimensionPixelSize = UIUtils.getContext()
		   .getResources()
		   .getDimensionPixelSize(R.dimen.y62);
	   showAtLocation(parent, Gravity.NO_GRAVITY, mLocation[0] - 5, mLocation[1]+dimensionPixelSize );
	   //	   	   showAtLocation(parent, Gravity.NO_GRAVITY, 190,
	   //					485);
	} else {
	   dismiss();
	}
   }

}
