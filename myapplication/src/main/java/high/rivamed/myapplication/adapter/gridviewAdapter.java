package high.rivamed.myapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.DialogBean;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/25 19:42
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class gridviewAdapter extends BaseAdapter {
   private int selected=0;
   Context          mContext;
   int              layoutResourceId;

   List<DialogBean> mGridData;

   public gridviewAdapter(Context context, int resource, List<DialogBean> objects) {
      this.mContext = context;
      this.layoutResourceId = resource;
      this.mGridData = objects;

   }

   @Override
   public int getCount() {
      return mGridData.size();
   }

   @Override
   public Object getItem(int position) {
      return null;
   }

   @Override
   public long getItemId(int position) {
      return 0;
   }

   @Override
   public View getView(final int position, View convertView, ViewGroup parent) {
      ViewHolder holder;
      if (convertView == null) {

         LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
         convertView = inflater.inflate(layoutResourceId, parent, false);
         holder = new ViewHolder();
         holder.textView = (TextView) convertView.findViewById(R.id.tag);
         holder.mGoneTextView = (TextView) convertView.findViewById(R.id.gone_tag);
//         if (mNumColumn==2){
//		ViewGroup.LayoutParams params = holder.textView.getLayoutParams();
//		params.width = 300;
//		holder.textView.setLayoutParams(params);
//	   }
         convertView.setTag(holder);
      } else {
         holder = (ViewHolder) convertView.getTag();
      }



      disposalView(position,holder);

	holder.textView.setText(mGridData.get(position).getName());
	holder.mGoneTextView.setText(mGridData.get(position).getCode());
      return convertView;
   }

   private void disposalView(int position, ViewHolder holder) {
      if (selected == position) {
         holder.textView.setBackgroundResource(R.drawable.bg_btn_gray_nor4);
         holder.textView.setTextColor(mContext.getResources().getColor(R.color.bg_f));
      } else {
         holder.textView.setBackgroundResource(R.drawable.bg_btn_line_nor);
         holder.textView.setTextColor(mContext.getResources().getColor(R.color.text_color_58));
      }
   }
   public void setSelected(int position){
      this.selected=position;
   }
   private class ViewHolder {
      TextView  textView;
      TextView  mGoneTextView;
   }

}
