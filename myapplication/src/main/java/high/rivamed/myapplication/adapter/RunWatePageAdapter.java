package high.rivamed.myapplication.adapter;

import android.support.annotation.Nullable;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.RunWateBean;
import high.rivamed.myapplication.views.OnlyCodePopupWindow;

import static high.rivamed.myapplication.base.App.mDm;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/25 11:23
 * 描述:        耗材流水pager adapter
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class RunWatePageAdapter extends BaseQuickAdapter<RunWateBean.RowsBean, BaseViewHolder> {

    public  RunWatePageAdapter(
            int layoutResId, @Nullable List<RunWateBean.RowsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(
            BaseViewHolder helper, RunWateBean.RowsBean item) {
            ((LinearLayout) helper.getView(R.id.seven_ll)).setBackgroundResource(R.color.bg_f);

        helper.setText(R.id.seven_one,item.getStatus());
        helper.setText(R.id.seven_two,item.getCstName());
        helper.setText(R.id.seven_three,item.getEpc());
        helper.setText(R.id.seven_four,item.getCstSpec());
        helper.setText(R.id.seven_nine,"展开");
        if (item.getPatientName() == null || item.getPatientName().equals("")){
            helper.setText(R.id.seven_five,"/");
        }else {
            helper.setText(R.id.seven_five,item.getPatientName());
        }
        helper.setText(R.id.seven_six,item.getDeviceName());
        helper.setText(R.id.seven_seven,item.getOperationTime());
        if(item.getName()==null||item.getName().equals("")){
            helper.setText(R.id.seven_eight,"unknown");
        }else {
            helper.setText(R.id.seven_eight,item.getName());
        }

        helper.setTextColor(R.id.seven_one,mContext.getResources().getColor(R.color.color_green));

        helper.getView(R.id.seven_nine).setOnClickListener(view ->{
            int dimensionPixelSize = mContext.getResources().getDimensionPixelSize(R.dimen.x20);
            OnlyCodePopupWindow window = new OnlyCodePopupWindow(mContext, item.getBarcode());
            window.showPopupWindow(helper.getView(R.id.seven_nine),mDm.widthPixels-dimensionPixelSize);
        });
    }
}
