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
import high.rivamed.myapplication.utils.LogUtils;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/9/25 19:42
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class LossCauseAdapter extends BaseAdapter {
   private int selected=0;
   Context          mContext;
   int              layoutResourceId;

   List<String> mStringData;

   public LossCauseAdapter(Context context, int resource, List<String> string) {
      this.mContext = context;
      this.layoutResourceId = resource;
      this.mStringData = string;

   }

   @Override
   public int getCount() {
      return mStringData.size();
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
      LogUtils.i("Loss","mStringData.get(position)    "+mStringData.get(position));
	holder.textView.setText(mStringData.get(position));
      return convertView;
   }

   private void disposalView(int position, ViewHolder holder) {
      if (selected == position) {
         holder.textView.setBackgroundResource(R.drawable.bg_btn_gray_nor);
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
