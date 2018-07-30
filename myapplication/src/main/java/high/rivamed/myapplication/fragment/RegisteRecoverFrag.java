package high.rivamed.myapplication.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.SimpleFragment;
import high.rivamed.myapplication.bean.SnRecoverBean;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.UIUtils;

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

   @BindView(R.id.one)
   TextView     mOne;
   @BindView(R.id.frag_registe_name_edit)
   EditText     mFragRegisteNameEdit;
   @BindView(R.id.frag_registe_name)
   LinearLayout mFragRegisteName;
   @BindView(R.id.fragment_btn_one)
   TextView     mFragmentBtnOne;
   @BindView(R.id.activity_down_btn_one_ll)
   LinearLayout mActivityDownBtnOneLl;

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
	mFragRegisteNameEdit.setText("123456789");

   }

   private void loadDate() {
	String sn = mFragRegisteNameEdit.getText().toString().trim();
	NetRequest.getInstance().getRecoverDate(sn, mContext, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		SnRecoverBean snRecoverBean = mGson.fromJson(result, SnRecoverBean.class);
		EventBusUtils.post(snRecoverBean);
	   }
	});
   }

   @Override
   public void onBindViewBefore(View view) {

   }


   @OnClick(R.id.fragment_btn_one)
   public void onViewClicked() {
	if (UIUtils.isFastDoubleClick()) {
	   return;
	} else {
	   loadDate();
	}

   }
}
