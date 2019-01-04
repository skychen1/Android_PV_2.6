package high.rivamed.myapplication.activity;

import android.os.Bundle;
import android.view.View;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.BaseSimpleActivity;
import high.rivamed.myapplication.fragment.StockMiddleInforFrag;

/**
 * 项目名称:    Android_PV_2.6.4New
 * 创建者:      DanMing
 * 创建时间:    2019/1/4 17:16
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class LoginStockStatusActivity extends BaseSimpleActivity {


   @Override
   protected int getContentLayoutId() {
	return R.layout.activity_stock_loginlayout;
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	super.initDataAndEvent(savedInstanceState);
	mBaseTabBack.setVisibility(View.VISIBLE);
	mBaseTabTvName.setVisibility(View.GONE);
	mBaseTabBtnMsg.setVisibility(View.GONE);
	mBaseTabIconRight.setVisibility(View.GONE);
	mBaseTabOutLogin.setVisibility(View.GONE);
	mBaseTabTvTitle.setText("库存详情");
	getSupportFragmentManager()
		.beginTransaction()
		.add(R.id.login_stock_bg,new StockMiddleInforFrag())
		.commit();
   }


}