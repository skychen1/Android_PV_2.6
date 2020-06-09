package high.rivamed.myapplication.activity;

import android.os.Bundle;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.SimpleActivity;
import high.rivamed.myapplication.fragment.ContentStockStatusFrag;

/**
 * 项目名称:    Android_PV_2.6.4New
 * 创建者:      DanMing
 * 创建时间:    2019/1/4 17:16
 * 描述:       查看库存详情
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class LoginStockStatusActivity extends SimpleActivity {


   @Override
   public int getLayoutId() {
	return R.layout.activity_stock_loginlayout;
   }
   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {

	getSupportFragmentManager()
		.beginTransaction()
		.add(R.id.login_stock_bg, ContentStockStatusFrag.newInstance(false))
		.commit();
   }

   @Override
   public void onBindViewBefore() {

   }

   @Override
   public Object newP() {
	return null;
   }
}