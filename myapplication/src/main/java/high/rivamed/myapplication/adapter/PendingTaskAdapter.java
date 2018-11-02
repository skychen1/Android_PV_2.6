package high.rivamed.myapplication.adapter;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.PendingTaskBean;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/24 19:37
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class PendingTaskAdapter
        extends BaseQuickAdapter<PendingTaskBean.MessagesBean, BaseViewHolder> {

    private TextView tv_top_name;
    private TextView tv_bottom_name;
    private TextView tv_check_detail;

    public PendingTaskAdapter(
            int layoutResId, @Nullable List<PendingTaskBean.MessagesBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(
            BaseViewHolder helper, PendingTaskBean.MessagesBean item) {
        findId(helper);
        tv_top_name.setText(item.getType());
//        tv_bottom_name.setText(Html.fromHtml("任务在2018-05-22 18:00已启动，请前往<font color='#ff0000'><big><big>5号</big></big></font>柜领取"));
        tv_bottom_name.setText(item.getText());
    }

    private void findId(BaseViewHolder helper) {
        tv_top_name = ((TextView) helper.getView(R.id.tv_top_name));
        tv_bottom_name = ((TextView) helper.getView(R.id.tv_bottom_name));
        tv_check_detail = ((TextView) helper.getView(R.id.tv_check_detail));
    }
}