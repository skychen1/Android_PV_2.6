package high.rivamed.myapplication.fragment;

import android.os.Bundle;

import high.rivamed.myapplication.base.BaseSimpleFragment;

/**
 * 项目名称:    Android_PV_2.6.1
 * 创建者:      DanMing
 * 创建时间:    2018/10/30 15:40
 * 描述:        使用记录
 * 包名:        high.rivamed.myapplication.fragment
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class ContentTakeNotes  extends BaseSimpleFragment {
   public static ContentTakeNotes newInstance() {
	Bundle args = new Bundle();
	ContentTakeNotes fragment = new ContentTakeNotes();
	fragment.setArguments(args);
	return fragment;
   }

   @Override
   protected int getContentLayoutId() {
	return 0;
   }
}
