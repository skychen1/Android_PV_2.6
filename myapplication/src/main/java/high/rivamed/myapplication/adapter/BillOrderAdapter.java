package high.rivamed.myapplication.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.daimajia.swipe.SwipeLayout;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.dto.vo.InventoryVo;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.UIUtils;
import high.rivamed.myapplication.views.OnlyCodePopupWindow;

import static android.view.View.GONE;
import static high.rivamed.myapplication.base.App.mDm;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/19 17:08
 * 描述:         套组领用--根据EPC查回的耗材
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class BillOrderAdapter extends BaseQuickAdapter<InventoryVo, BaseViewHolder> {
    private TextView mSeven_one;
    private TextView mSeven_two;
    private TextView mSeven_three;
    private TextView mSeven_four;
    private TextView mSeven_five;
    private TextView mSeven_seven;
    public String TAG = "BillOrderAdapter";
    public int mSize;
    public String mType;
    LinearLayout mLl;
    boolean menuDownType;
    public BillOrderAdapter(
            int layout, int type, List<InventoryVo> data,boolean menuDownType) {
        super(layout, data);
        this.mType = "" + type;
        this.menuDownType=menuDownType;
    }

    public void clear() {

        mData.clear();
        notifyDataSetChanged();
    }

    @Override
    protected void convert(final BaseViewHolder helper, InventoryVo item) {
        mLl=((LinearLayout) helper.getView(R.id.seven_ll));

        mSeven_one = ((TextView) helper.getView(R.id.seven_one));
        mSeven_two = ((TextView) helper.getView(R.id.seven_two));
        mSeven_three = ((TextView) helper.getView(R.id.seven_three));
        mSeven_four = ((TextView) helper.getView(R.id.seven_four));
        mSeven_five = ((TextView) helper.getView(R.id.seven_five));

        if ("8".equals(mType)) {
            mSeven_seven = ((TextView) helper.getView(R.id.seven_seven));
            mSeven_seven.setText(item.getPatientName());
        } else {
            mSeven_seven = null;
        }
        ImageView mImageView =(ImageView) helper.getView(R.id.seven_six);;
//        Drawable drawable = mContext.getResources().getDrawable(R.mipmap.gcms_ic_yc);

        if (item.getRemark() != null) {
            if (item.getRemark().equals("1")) {
                mImageView.setVisibility(View.VISIBLE);
                mImageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.hccz_ic_tx));
            } else {
                mImageView.setVisibility(View.INVISIBLE);
            }
        } else {
            mImageView.setVisibility(View.INVISIBLE);
        }
        if (item.getIsErrorOperation()==1){
            mImageView.setVisibility(View.VISIBLE);
            mImageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.hccz_ic_xx));
        }

        SwipeLayout swipe = (SwipeLayout) helper.getView(R.id.swipe);
        swipe.setShowMode(SwipeLayout.ShowMode.LayDown);
        LinearLayout  delete = (LinearLayout) helper.getView(R.id.ll_delete);
        TextView mdeleteTv = (TextView) helper.getView(R.id.tv_delete);
        ImageView mdeleteIv = (ImageView) helper.getView(R.id.iv_delete);

        if (item.getDeleteCount()>0){
            LogUtils.i("InBox", "解除移除");
            mdeleteTv.setText("取消移除");
            delete.setBackgroundColor(UIUtils.getContext().getResources().getColor(R.color.bg_greens));
            mdeleteIv.setVisibility(GONE);
        }else {
            LogUtils.i("InBox","移除");
            mdeleteTv.setText("移除");
            delete.setBackgroundColor(UIUtils.getContext().getResources().getColor(R.color.bg_delete));
            mdeleteIv.setVisibility(View.VISIBLE);
        }

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InventoryVo inventoryVo = mData.get(helper.getAdapterPosition());
                if (inventoryVo.getDeleteCount()>0){
                    inventoryVo.setDelete(false);
                    inventoryVo.setDeleteCount(0);
                    mData.remove(helper.getAdapterPosition());
                    mData.add(inventoryVo);
                }else {
                    inventoryVo.setDelete(true);
                    inventoryVo.setDeleteCount(inventoryVo.getDeleteCount()+1);
                    mData.remove(helper.getAdapterPosition());
                    mData.add(inventoryVo);
                }

                EventBusUtils.post(new Event.EventButton(true, true));
                notifyDataSetChanged();
            }
        });


        mSeven_one.setText(item.getCstName());
        mSeven_two.setText(item.getEpc());
        mSeven_three.setText(item.getCstSpec());
        mSeven_four.setText(item.getExpiryDate());
        mSeven_five.setText(item.getDeviceName());
        mSeven_four.setBackgroundResource(R.color.bg_f);
        helper.setText(R.id.seven_eight,"展开");

        helper.getView(R.id.seven_eight).setOnClickListener(view ->{
            OnlyCodePopupWindow window = new OnlyCodePopupWindow(mContext, item.getBarcode());
            window.showPopupWindow(helper.getView(R.id.seven_eight),mDm.widthPixels);
        });

        if (item.getExpireStatus()!=null){
            UIUtils.initTermOfValidity(mContext, item.getIsErrorOperation(), item.getExpireStatus(), mSeven_four);
        }
        if (item.getExpireStatus()==0){
            mSeven_four.setBackgroundResource(R.drawable.bg_text_red);
            mSeven_four.setTextColor(mContext.getResources().getColor(R.color.bg_f));
            mSeven_four.setText("已过期");
        }
        setDeleteView(item.isDelete(),swipe);

        if ((item.getIsErrorOperation() == 1 && item.getDeleteCount() == 0) ||
            (item.getIsErrorOperation() == 1 && item.getDeleteCount() == 0 && item.getExpireStatus() == 1 )){
            mSeven_one.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
            mSeven_two.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
            mSeven_three.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
            mSeven_five.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
            mSeven_seven.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
            if (item.getExpireStatus() != 0 ){
                mSeven_four.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
            }
        }else if (item.getIsErrorOperation() == 1){
            mSeven_one.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
            mSeven_two.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
            mSeven_three.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
            mSeven_five.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
            mSeven_seven.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
            if (item.getExpireStatus() != 0 ){
                mSeven_four.setBackgroundResource(R.drawable.bg_text_red);
                mSeven_four.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
                mSeven_four.setBackgroundResource(R.drawable.bg_text_red);
            }
        }else {
            mSeven_one.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
            mSeven_two.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
            mSeven_three.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
            mSeven_five.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
            if(mSeven_seven!=null){
                mSeven_seven.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
            }
            mLl.setBackgroundResource(R.color.bg_f);
        }
        if (!menuDownType){
            swipe.setSwipeEnabled(false);
        }
    }
    public void setDeleteView(boolean isDeleteView, SwipeLayout swipe) {
        if (isDeleteView) {
            mSeven_one.setTextColor(Color.parseColor("#999999"));
            mSeven_two.setTextColor(Color.parseColor("#999999"));
            mSeven_three.setTextColor(Color.parseColor("#999999"));
            mSeven_four.setTextColor(Color.parseColor("#999999"));
            mSeven_four.setBackgroundResource(R.color.bg_color);
            mSeven_five.setTextColor(Color.parseColor("#999999"));
            mSeven_seven.setTextColor(Color.parseColor("#999999"));
            mLl.setBackgroundResource(R.color.bg_color);
            swipe.setSwipeEnabled(true);
        } else {
            mSeven_one.setTextColor(Color.parseColor("#333333"));
            mSeven_two.setTextColor(Color.parseColor("#333333"));
            mSeven_three.setTextColor(Color.parseColor("#333333"));
            mSeven_five.setTextColor(Color.parseColor("#333333"));
            mLl.setBackgroundResource(R.color.bg_f);
            swipe.setSwipeEnabled(true);
        }
    }
}
