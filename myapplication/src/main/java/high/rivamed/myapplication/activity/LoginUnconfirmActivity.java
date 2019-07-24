package high.rivamed.myapplication.activity;

import android.os.Bundle;
import android.view.View;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.BaseSimpleActivity;
import high.rivamed.myapplication.fragment.PublicStockFrag;

import static high.rivamed.myapplication.cont.Constants.STYPE_STOCK_RIGHT;

/**
 * 项目名称:    Android_PV_2.6.4New
 * 创建者:      DanMing
 * 创建时间:    2019/1/4 17:16
 * 描述:       查看未确认耗材
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class LoginUnconfirmActivity extends BaseSimpleActivity {


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
	mBaseTabTvTitle.setText("未确认耗材");
	getSupportFragmentManager()
		.beginTransaction()
		.add(R.id.login_stock_bg,
		     PublicStockFrag.newInstance(9, STYPE_STOCK_RIGHT, ""))//9是数列量
		.commit();
   }


}