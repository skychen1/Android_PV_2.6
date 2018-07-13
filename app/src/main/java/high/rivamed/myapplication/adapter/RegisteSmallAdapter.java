package high.rivamed.myapplication.adapter;

import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.RegisteAddBean;
import high.rivamed.myapplication.utils.RotateUtils;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/13 9:53
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class RegisteSmallAdapter extends BaseQuickAdapter<RegisteAddBean, BaseViewHolder> {


   private boolean mType = false;

   public RegisteSmallAdapter(
	   int layoutResId, @Nullable List<RegisteAddBean> data) {
	super(layoutResId, data);
   }

   @Override
   protected void convert(BaseViewHolder holder, RegisteAddBean item) {

		Log.i("ccc","TYPE_HEAD "+item.getBoxname());
		EditText leftName = (EditText) holder.getView(R.id.head_left_name);
		ImageView rightDelete = (ImageView) holder.getView(R.id.right_delete);
		if(holder.getAdapterPosition()==0){
		   leftName.setText("1号柜");
		}else if (item.boxname.equals("")){

		}

		final ImageView rightFold = (ImageView) holder.getView(R.id.right_fold);
		rightFold.setOnClickListener(new View.OnClickListener() {
		   @Override
		   public void onClick(View v) {
			if (mType) {
			   RotateUtils.rotateArrow(rightFold, mType);
			   mType = false;
			} else {
			   RotateUtils.rotateArrow(rightFold, mType);
			   mType = true;
			}
		   }
		});
	EditText footName = (EditText) holder.getView(R.id.foot_name);
	TextView footMac = (TextView) holder.getView(R.id.foot_mac);
	EditText footIp = (EditText) holder.getView(R.id.foot_ip);
	ImageView footDelete = (ImageView) holder.getView(R.id.foot_delete);
	ImageView footAdd = (ImageView) holder.getView(R.id.foot_add);


   }
}
