package high.rivamed.myapplication.views;

/**
 * @作者: 钱凯
 * @创建时间: 2018/8/7
 * @功能描述:
 */

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import high.rivamed.myapplication.R;


/**
 * 自定义PopupWindow
 * Created by wangbohua on 2016/12/17 0017.
 */

public class MypopupWindow {

    private List<String> list = new ArrayList<>();
    private List<String> data;
    private Context mContext;
    private ListView listView;
    private PopupWindow popwindow;
    private IPopCallBack callback;
    private View parentView;

    public MypopupWindow(Context mContext, List<String> data, View parentView, IPopCallBack callback) {
        this.mContext = mContext;
        this.data = data;
        this.parentView = parentView;
        this.callback = callback;
    }


    public PopupWindow getWindow() {
        return popwindow;
    }

    /**
     * 设置PopupWindow的View
     *
     * @return
     */
    public View setView(int popupWindowCount) {
        View view = null;
        switch (popupWindowCount) {
            case 1:
                view = View.inflate(mContext, R.layout.pop_select_list, null);
                return view;
            default:
                break;
        }
        return view;
    }


    /**
     * @param popupWindowCount 展示PopupWindow的个数 0,1,2
     * @param windowLoaction   展示PopupWindow的位置 0:屏幕中央 1：屏幕底部 2：控件下方
     * @param widthStyle       宽度类型 1：Dialog类型，有空白 2：Dialog类型，match 3：控件宽度
     */
    public void showPopup(int popupWindowCount, int windowLoaction, int widthStyle) {
        //显示ListView的个数
        View view = setView(popupWindowCount);
        switch (widthStyle) {
            //Dialog效果
            case 0:
                //如果大于一定数量，设置高度
                if (this.data.size() < 13)
                    //创建PopupWindow
                    popwindow = new PopupWindow(view, parentView.getWidth() - 200,
                            LayoutParams.WRAP_CONTENT, true);
                else
                    //创建PopupWindow
                    popwindow = new PopupWindow(view, parentView.getWidth() - 200,
                            parentView.getHeight() / 2 + 200, true);
                break;
            //Dialog效果
            case 1:
                //如果大于一定数量，设置高度
                if (this.data.size() < 13)
                    //创建PopupWindow
                    popwindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
                            LayoutParams.WRAP_CONTENT, true);
                else
                    //创建PopupWindow
                    popwindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
                            parentView.getHeight() / 2 + 200, true);
                break;
            //下拉选项框效果
            case 2:
                //如果大于一定数量，设置高度
                if (this.data.size() < 4)
                    //创建PopupWindow
                    popwindow = new PopupWindow(view, parentView.getWidth(),
                            LayoutParams.WRAP_CONTENT, true);
                else
                    //创建PopupWindow
                    popwindow = new PopupWindow(view, parentView.getWidth(),
                            parentView.getHeight() / 3, true);
                break;
        }

        listView = (ListView) view.findViewById(R.id.first_listview);
        setData(this.data, listView);
        popwindow.setContentView(view);
        popwindow.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        popwindow.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        popwindow.setFocusable(true);
        popwindow.setOutsideTouchable(true);
        popwindow.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        popwindow.setBackgroundDrawable(dw);
        popwindow.setAnimationStyle(R.style.social_pop_anim);

        //显示的位置
        switch (windowLoaction) {
            //在屏幕中央
            case 0:
                popwindow.showAtLocation(parentView, Gravity.CENTER, 0, 0);
                break;
            //在屏幕底部
            case 1:
                popwindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                break;
            //在控件下面
            case 2:
                popwindow.showAsDropDown(parentView);
                break;
        }
        //        popwindow.showAtLocation(parentView, Gravity.CENTER,200,(parentView.getHeight()-popwindow.getHeight())/2);
        setListener();
    }

    /**
     * 设置listview点击事件
     */
    private void setListener() {
        // TODO Auto-generated method stub
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (callback != null) {
                    callback.callBack(list.get(position), position);
                }
                popwindow.dismiss();
            }
        });
    }

    /**
     * 给Popup设置数据
     *
     * @param data
     */
    public void setData(List<String> data, ListView listView) {
        this.list = new ArrayList<String>();
        list.addAll(data);
        PopupAdapter adapter = new PopupAdapter(list);
        listView.setAdapter(adapter);
    }

    private class PopupAdapter extends BaseAdapter {

        private List<String> list;

        public PopupAdapter(List<String> list) {
            super();
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.item_list_pop, null);
                holder.getView(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.poptext.setText(list.get(position));
            return convertView;
        }

        class ViewHolder {
            TextView poptext;

            public void getView(View v) {
                poptext = (TextView) v.findViewById(R.id.text);

            }
        }

    }

    /**
     * 点击pop回调接口
     */
    public interface IPopCallBack {
        void callBack(String content, int posotion);
    }

}

