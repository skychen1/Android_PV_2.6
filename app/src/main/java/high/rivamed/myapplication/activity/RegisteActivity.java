package high.rivamed.myapplication.activity;

import android.os.Bundle;
import android.view.View;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.BaseSimpleActivity;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/11 10:59
 * 描述:        注册界面
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class RegisteActivity extends BaseSimpleActivity {

   @Override
   protected int getContentLayoutId() {
	return R.layout.activity_registe_layout;
   }
   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	mBaseTabBack.setVisibility(View.VISIBLE);
	mBaseTabTvTitle.setVisibility(View.VISIBLE);
	mBaseTabTvTitle.setText("设备注册");
	mBaseTabTvName.setVisibility(View.GONE);
	mBaseTabIconRight.setVisibility(View.GONE);
   }
}
