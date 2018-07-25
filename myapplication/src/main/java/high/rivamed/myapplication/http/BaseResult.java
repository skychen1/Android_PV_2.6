package high.rivamed.myapplication.http;

import android.text.TextUtils;

import high.rivamed.myapplication.BuildConfig;
import high.rivamed.myapplication.utils.ToastUtils;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/4 16:51
 * 描述:
 * 包名:        high.rivamed.myapplication.http
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class BaseResult implements NetResult {
	
	@Override
	public void onSucceed(String result) {
	
	}
   @Override
   public void onError(String result) {

   }
	@Override
	public void onSucceedButNoData() {
	
	}
	
	@Override
	public void onNetFailing(String result) {
		
		if(TextUtils.isEmpty(result)){
			ToastUtils.showShort("链接失败，请检查网络！");
		}else{
		   ToastUtils.showShort(result);
		}
		
		
	}
	
	@Override
	public void onDataError(int errorCode, String msg) {
		
		if (BuildConfig.API_ENV) {
		   ToastUtils.showShort("测试环境错误原因：" + errorCode + "==" + msg);
		}
		
	}
	

}
