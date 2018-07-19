package high.rivamed.myapplication.activity;

import android.os.Bundle;
import android.view.View;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.BaseSimpleActivity;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/15 16:55
 * 描述:        个人信息页面
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class MyInfoActivity extends BaseSimpleActivity {

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
      mBaseTabBack.setVisibility(View.VISIBLE);
      mBaseTabTvTitle.setVisibility(View.VISIBLE);
      mBaseTabTvTitle.setText("个人信息");
}

   @Override
   protected int getContentLayoutId() {
      return R.layout.setting_myinfo_layout;
   }

}
