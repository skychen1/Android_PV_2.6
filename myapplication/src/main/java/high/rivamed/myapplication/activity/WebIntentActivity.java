package high.rivamed.myapplication.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;

import butterknife.BindView;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.BaseSimpleActivity;

/**
 * 项目名称:    Android_PV_2.6.12_spd
 * 创建者:      DanMing
 * 创建时间:    2020/1/15 0015 10:41
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class WebIntentActivity extends BaseSimpleActivity {

   @BindView(R.id.agentweb)
   LinearLayout mAgentweb;

   @Override
   protected int getContentLayoutId() {
	return R.layout.act_webintent_layout;
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	super.initDataAndEvent(savedInstanceState);
	mBaseTabBack.setVisibility(View.VISIBLE);
	mBaseTabTvTitle.setVisibility(View.VISIBLE);
	mBaseTabTvTitle.setText("管理端");
	AgentWeb.with(this)
		.setAgentWebParent((LinearLayout) mAgentweb, new LinearLayout.LayoutParams(-1, -1))
		.useDefaultIndicator()
		.createAgentWeb()
		.ready()
		.go("http://192.168.10.126:8040/#/login");
   }
}
