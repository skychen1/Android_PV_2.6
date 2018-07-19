package high.rivamed.myapplication.fragment;

import android.os.Bundle;
import android.view.View;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.SimpleFragment;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/19 11:26
 * 描述:        数据恢复界面
 * 包名:        high.rivamed.myapplication.fragment
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class RegisteRecoverFrag extends SimpleFragment {

   public static RegisteRecoverFrag newInstance() {
	Bundle args = new Bundle();
	RegisteRecoverFrag fragment = new RegisteRecoverFrag();
	//	args.putInt(TYPE_SIZE, param);
	//	args.putString(TYPE_PAGE, type);
	//	fragment.setArguments(args);
	return fragment;

   }

   @Override
   public int getLayoutId() {
	return R.layout.frg_recover_layout;
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {

   }

   @Override
   public void onBindViewBefore(View view) {

   }
}
