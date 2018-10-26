package high.rivamed.myapplication.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.BingFindSchedulesBean;
import high.rivamed.myapplication.bean.Movie;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/8/3 13:40
 * 描述:        选择绑定的患者
 * 包名:       医嘱单请领-确认-弹出框-选择柜子
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class OpenCabinetConfirmDialogAdapter
        extends BaseQuickAdapter<Movie, BaseViewHolder> {

    RelativeLayout mRootView;
    private TextView mTvCabinetNumber;
    private ImageView mIvCabinet;
    private int mSelectedPos = 0;
    private List<Movie> mMovies;

    public OpenCabinetConfirmDialogAdapter(List<Movie> movies) {
        super(R.layout.item_outformconfim_cabinet, movies);
        this.mMovies = movies;
    }

    public int getCheckedPosition() {
        return mSelectedPos;
    }

    @Override
    protected void convert(
            BaseViewHolder helper, Movie item) {
        if (mSelectedPos == 0) {
            mMovies.get(mSelectedPos).setDelete(true);
        }
        for (int i = 0; i < mMovies.size(); i++) {
            if (mMovies.get(i).isDelete()) {
                mSelectedPos = i;
            }
        }
        mRootView = helper.getView(R.id.form_card_ll);
        mTvCabinetNumber = helper.getView(R.id.tv_cabinet_number);
        mIvCabinet = helper.getView(R.id.iv_cabinet);
        mTvCabinetNumber.setText("柜子01");
        //点击切换背景
        setViewStlye(item.isDelete());
        int position = helper.getAdapterPosition();
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMovies.get(mSelectedPos).setDelete(false);
                //设置新的Item勾选状态
                mSelectedPos = position;
                mMovies.get(mSelectedPos).setDelete(true);
                notifyDataSetChanged();
            }
        });
    }

    /***
     * 点击切换背景
     * @param isSelected
     */
    private void setViewStlye(boolean isSelected) {
        if (isSelected) {
            mIvCabinet.setBackgroundResource(R.mipmap.icon_ofcd_cabinet_pre);
            mTvCabinetNumber.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            mRootView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_navy_blue));
        } else {
            mIvCabinet.setBackgroundResource(R.mipmap.icon_ofcd_cabinet_nor);
            mTvCabinetNumber.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_0));
            mRootView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
        }
    }
}
