package high.rivamed.myapplication.fragment;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.adapter.ExceptionDealAdapter;
import high.rivamed.myapplication.adapter.ExceptionRecordAdapter;
import high.rivamed.myapplication.base.SimpleFragment;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.ExceptionDealBean;
import high.rivamed.myapplication.bean.ExceptionOperatorBean;
import high.rivamed.myapplication.bean.ExceptionRecordBean;
import high.rivamed.myapplication.bean.HospNameBean;
import high.rivamed.myapplication.cont.Constants;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.timeutil.PowerDateUtils;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;
import high.rivamed.myapplication.views.SelectExceptionOperatorDialog;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;
import static high.rivamed.myapplication.cont.Constants.SAVE_BRANCH_CODE;
import static high.rivamed.myapplication.cont.Constants.SAVE_DEPT_CODE;
import static high.rivamed.myapplication.cont.Constants.STYPE_EXCEPTION_LEFT;
import static high.rivamed.myapplication.cont.Constants.STYPE_EXCEPTION_RIGHT;

/**
 * 项目名称：高值
 * 创建者：chenyanling
 * 创建时间：2019/5/13
 * 描述：
 */
public class PublicExceptionFrag extends SimpleFragment {

    private static final String TYPE_SIZE = "TYPE_SIZE";
    private static final String TYPE_PAGE = "TYPE_PAGE";
    private static final String DEVICECODE = "DEVICECODE";
    @BindView(R.id.search_et)
    EditText searchEt;
    @BindView(R.id.search_iv_delete)
    ImageView searchIvDelete;
    @BindView(R.id.search_time_start)
    TextView searchTimeStart;
    @BindView(R.id.search_time_end)
    TextView searchTimeEnd;
    @BindView(R.id.search_type_all)
    RadioButton searchTypeAll;
    @BindView(R.id.search_type_continue)
    RadioButton searchTypeContinue;
    @BindView(R.id.search_type_unbind)
    RadioButton searchTypeUnbind;
    @BindView(R.id.search_type_force_in)
    RadioButton searchTypeForceIn;
    @BindView(R.id.search_type_force_out)
    RadioButton searchTypeForceOut;
    @BindView(R.id.search_type_rg)
    RadioGroup searchTypeRg;
    @BindView(R.id.ll_except_type)
    LinearLayout llExceptType;
    @BindView(R.id.timely_ll)
    LinearLayout timelyLl;
    @BindView(R.id.header)
    MaterialHeader header;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.public_ll)
    LinearLayout publicLl;


    private static final int loadTime = 200;//上下拉加载时间
    private static final int INTENT_TYPE = 222;//用于选择原因、库房、科室
    private int PAGE = 1;//当前页数
    private int SIZE = 20;//分页：每页数据
    private int mType_size;//柜子tab序号
    private String mType_page;//异常处理还是异常记录tab
    private String mDeviceCode;
    private String mSearchKey;//搜索关键字
    private String mSearchStartTime, mSearchEndTime;//搜索开始结束时间
    private int mSearchType;//搜索异常类型
    private ExceptionDealAdapter dealAdapter;//异常处理适配器
    private ExceptionRecordAdapter recordAdapter;//异常记录适配器
    private ArrayList<ExceptionDealBean> showDealList;//异常处理数据源
    private ArrayList<ExceptionRecordBean> showRecordList;//异常记录数据源
    private View empty;//空白页
    private boolean hasNextPage;//分页：是否有下一页
    private int curPosition;//异常处理：当前操作的数据项

    /**
     * @param param 表格的列数  用来判断数据长度  表格的列表
     * @param type  表格类别
     * @returnList<RunWateBean.RowsBean>
     */
    public static PublicExceptionFrag newInstance(int param, String type, String deviceCode) {
        Bundle args = new Bundle();
        PublicExceptionFrag fragment = new PublicExceptionFrag();
        args.putInt(TYPE_SIZE, param);
        args.putString(TYPE_PAGE, type);
        args.putString(DEVICECODE, deviceCode);
        fragment.setArguments(args);
        return fragment;
    }

    @OnClick(R.id.search_time_start)
    void onStartTimeClick() {
        DialogUtils.showTimeDialog(mContext, searchTimeStart);
    }

    @OnClick(R.id.search_time_end)
    void onEndTimeClick() {
        if (mSearchStartTime == null) {
            ToastUtils.showShort("请先选择开始时间");
        } else {
            DialogUtils.showTimeDialog(mContext, searchTimeEnd);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.exception_deal_frag;
    }

    @Override
    public void initDataAndEvent(Bundle savedInstanceState) {
        EventBusUtils.register(this);
        Bundle arguments = getArguments();
        mType_size = arguments.getInt(TYPE_SIZE);
        mType_page = arguments.getString(TYPE_PAGE);
        mDeviceCode = arguments.getString(DEVICECODE);
        empty = LayoutInflater.from(_mActivity).inflate(R.layout.recy_null, null);
        initListener();
        if (mType_page.equals(STYPE_EXCEPTION_LEFT)) {
            //异常处理
            initExceptionDeal();
        } else {
            //异常记录
            initExceptionRecord();
        }
        loadData();
    }

    private void initListener() {
        //初始化时间选择
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String format = sdf.format(date);
        searchTimeStart.setHint(PowerDateUtils.getTime());
        searchTimeEnd.setHint(format);
        //设置时间选择监听
        searchTimeStart.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mSearchStartTime = s.toString().trim();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(mSearchEndTime)) {
                    //加载数据
                    PAGE = 1;
                    loadData();
                }
            }
        });
        searchTimeEnd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mSearchEndTime = s.toString().trim();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(mSearchStartTime)) {
                    //加载数据
                    PAGE = 1;
                    loadData();
                }

            }
        });
        //设置搜索监听
        searchEt.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                mSearchKey = searchEt.getText().toString().trim();
                UIUtils.hideSoftInput(_mActivity, searchEt);
                //加载数据
                PAGE = 1;
                loadData();
                return true;
            }
            return false;
        });
        //设置刷新监听
        //每次加载20条，数据做分页
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.setEnableRefresh(true);//是否启用下拉刷新功能
        refreshLayout.setEnableLoadMore(true);//是否启用上拉加载功能
        refreshLayout.setOnRefreshListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                //加载下一页
                if (hasNextPage) {
                    PAGE++;
                    loadData();
                } else {
                    finishLoading();
                    ToastUtils.showShort("暂无更多数据");
                }
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                //刷新
                PAGE = 1;
                loadData();
            }
        });
    }

    private void finishLoading() {
        refreshLayout.finishLoadMore(loadTime);
        refreshLayout.finishRefresh(loadTime);
    }

    private void initExceptionRecord() {
        llExceptType.setVisibility(View.GONE);
        searchEt.setHint("请输入耗材名称、EPC、处理人");
        timelyLl.addView(LayoutInflater.from(_mActivity)
                .inflate(R.layout.item_excp_record_nine_title_layout,
                        (ViewGroup) timelyLl.getParent(), false));
        showRecordList = new ArrayList<>();
        recordAdapter = new ExceptionRecordAdapter(showRecordList);
        recordAdapter.setEmptyView(empty);
        recyclerview.setLayoutManager(new LinearLayoutManager(_mActivity));
        recyclerview.addItemDecoration(new DividerItemDecoration(_mActivity, VERTICAL));
        recyclerview.setAdapter(recordAdapter);
    }

    private void initExceptionDeal() {
        llExceptType.setVisibility(View.VISIBLE);
        searchEt.setHint("请输入耗材名称、EPC");
        timelyLl.addView(LayoutInflater.from(_mActivity)
                .inflate(R.layout.item_excp_nine_title_layout,
                        (ViewGroup) timelyLl.getParent(), false));
        showDealList = new ArrayList<>();
        dealAdapter = new ExceptionDealAdapter(showDealList);
        dealAdapter.setEmptyView(empty);
        recyclerview.setLayoutManager(new LinearLayoutManager(_mActivity));
        recyclerview.addItemDecoration(new DividerItemDecoration(_mActivity, VERTICAL));
        recyclerview.setAdapter(dealAdapter);
        dealAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            curPosition = position;
            switch (view.getId()) {
                case R.id.nine_seven1:
                    // TODO: 2019/5/15 异常处理-关联操作人
                    if (showDealList.get(position).getOperator().equals("Unknown")){
                        connectOperator();
                    }
                    break;
                case R.id.nine_nine1:
                    // TODO: 2019/5/15 异常处理-操作
                    //1.移除判断 选择 2.出柜关联 根据配置是否需要绑定患者 3.关联患者
                    String operate = showDealList.get(position).getOperate();
                    switch (operate) {
                        case "移除判断":
                            removeJudge();
                            break;
                        case "出柜关联":
                            connectOutBox();
                            break;
                        case "绑定患者":
                            connectPatient();
                            break;
                    }
                    break;
            }
        });
        //异常类型搜索
        searchTypeRg.setOnCheckedChangeListener((group, checkedId) -> {
            // TODO: 2019/5/17 根据实际异常类型赋值： mSearchType
            switch (checkedId) {
                case R.id.search_type_all:
                    //全部
                    mSearchType = 0;
                    break;
                case R.id.search_type_continue:
                    //连续移除
                    mSearchType = 1;
                    break;
                case R.id.search_type_unbind:
                    //未绑定患者
                    mSearchType = 2;
                    break;
                case R.id.search_type_force_in:
                    //强开入柜
                    mSearchType = 3;
                    break;
                case R.id.search_type_force_out:
                    //强开出柜
                    mSearchType = 4;
                    break;
            }
            loadDealData();
        });
    }

    private void loadData() {
        if (mType_page.equals(STYPE_EXCEPTION_LEFT)) {
            //异常处理
            loadDealData();
        } else {
            //异常记录
            loadRecordData();
        }
    }

    private void loadDealData() {
        // TODO: 2019/5/17 加载异常处理数据
        //getDealData(mSearchType,mSearchKey,mSearchStartTime,mSearchEndTime)

        //测试：加载数据
        showDealList.clear();
        String[] operates = {"已退回", "已过期", "出柜关联", "移除判断", "绑定患者"};
        for (int i = 0; i < 5; i++) {
            ExceptionDealBean bean = new ExceptionDealBean();
            bean.setOperate(operates[i]);
            bean.setOperator("Unknown");
            showDealList.add(bean);
        }

        hasNextPage = (showDealList.size() > SIZE - 1);
        dealAdapter.notifyDataSetChanged();
        finishLoading();
    }

    private void loadRecordData() {
        // TODO: 2019/5/17 加载异常记录数据
        //getRecordData(mSearchKey,mSearchStartTime,mSearchEndTime)

        //测试：加载数据
        showRecordList.clear();
        String[] operates = {"Unknown", "Unknown", "未出柜关联", "连续移除", "未绑定患者"};
        for (int i = 0; i < 5; i++) {
            ExceptionRecordBean bean = new ExceptionRecordBean();
            bean.setExceptContent(operates[i]);
            showRecordList.add(bean);
        }

        hasNextPage = (showRecordList.size() > SIZE - 1);
        recordAdapter.notifyDataSetChanged();
        finishLoading();
    }

    /**
     * 操作处理
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event.outBoxEvent event) {
        // TODO: 2019/5/17 判断当前选中 首页 tab是异常处理
        //只更新异常处理tab，过滤掉其他tab
        if (mType_page.equals(STYPE_EXCEPTION_LEFT) && ExceptionDealFrag.CURRENT_TAB == mType_size ) {
            // TODO: 2019/5/17  根据选择的操作项处理
            switch (event.type){
                case "104":
                    //移除判断
                    event.dialog.dismiss();
                    onRemoveJudgeEvent(event);
                    break;
                case "105":
                    //出柜关联
                    onOutBoxConnectEvent(event);
                    break;
                case "106":
                    //出柜关联-下一步-绑定患者
                    event.dialog.dismiss();
                    showDealList.get(curPosition).setOperate("绑定患者");
                    dealAdapter.notifyDataSetChanged();
                    connectPatient();
                    break;
                case "x":
                    //选择库房结果处理
                    if (event.mIntentType==INTENT_TYPE){
                        //修改操作项
                        showDealList.get(curPosition).setOperate(Constants.EXCEPTION_DEAL_OUT_BOX_CONNECT[1]);
                        dealAdapter.notifyDataSetChanged();

                        event.dialog.dismiss();
                        ToastUtils.showShort("选择库房ID：" + event.context);
                    }
                    break;
                case "2":
                    //选择原因结果处理
                    if (event.mIntentType==INTENT_TYPE){
                        showDealList.get(curPosition).setOperate(Constants.EXCEPTION_DEAL_OUT_BOX_CONNECT[2]);
                        dealAdapter.notifyDataSetChanged();
                        event.dialog.dismiss();
                        ToastUtils.showShort("选择原因：" + event.context);
                    }
                    break;
                case "3":
                    //选择科室结果处理
                    if (event.mIntentType==INTENT_TYPE){
                        showDealList.get(curPosition).setOperate(Constants.EXCEPTION_DEAL_OUT_BOX_CONNECT[3]);
                        dealAdapter.notifyDataSetChanged();
                        event.dialog.dismiss();
                        ToastUtils.showShort("选择科室ID：" + event.context);
                    }
                    break;
            }
        }
    }

    private void onOutBoxConnectEvent(Event.outBoxEvent event) {
        //出柜关联：{"领用", "移出", "调拨出库", "退货"}

        //根据配置是否需要绑定患者 是否有下一步
        boolean hasNext = event.mIntentType == 1;
        if (!hasNext) {
            event.dialog.dismiss();
        }
        String deptId = SPUtils.getString(UIUtils.getContext(), SAVE_DEPT_CODE);
        // TODO: 2019/5/17  根据选择的操作项处理
        switch (event.context) {
            case "0"://领用
                showDealList.get(curPosition).setOperate(Constants.EXCEPTION_DEAL_OUT_BOX_CONNECT[0]);
                dealAdapter.notifyDataSetChanged();
                break;
            case "1"://移出
                //选择库房
                NetRequest.getInstance().getHospBydept(deptId, this, new BaseResult() {
                    @Override
                    public void onSucceed(String result) {
                        HospNameBean hospNameBean = mGson.fromJson(result, HospNameBean.class);
                        DialogUtils.showStoreDialog(mContext, 2, 1, hospNameBean, INTENT_TYPE);
                    }
                });
                break;
            case "2"://调拨出库
                //选择原因
                DialogUtils.showStoreDialog(_mActivity, 2, 2, null, INTENT_TYPE);
                break;
            case "3"://退货
                //选择科室
                String branchCode = SPUtils.getString(UIUtils.getContext(), SAVE_BRANCH_CODE);
                NetRequest.getInstance().getOperateDbDialog(deptId, branchCode, this, new BaseResult() {
                    @Override
                    public void onSucceed(String result) {
                        HospNameBean hospNameBean = mGson.fromJson(result, HospNameBean.class);
                        DialogUtils.showStoreDialog(mContext, 2, 3, hospNameBean, INTENT_TYPE);
                    }
                });
                break;
        }
    }

    private void onRemoveJudgeEvent(Event.outBoxEvent event) {
        //EXCEPTION_DEAL_REMOVE_JUDGE :{"标签损坏","取消异常标记","出柜关联"}
        // TODO: 2019/5/17 移除结果处理
        switch (event.context) {
            case "0"://标签损坏
                showDealList.get(curPosition).setOperate( Constants.EXCEPTION_DEAL_REMOVE_JUDGE[0]);
                dealAdapter.notifyDataSetChanged();
                break;
            case "1"://取消异常标记
                showDealList.get(curPosition).setOperate(Constants.EXCEPTION_DEAL_REMOVE_JUDGE[1]);
                dealAdapter.notifyDataSetChanged();
                break;
            case "2"://出柜关联
                showDealList.get(curPosition).setOperate(Constants.EXCEPTION_DEAL_REMOVE_JUDGE[2]);
                dealAdapter.notifyDataSetChanged();
                connectOutBox();
                break;
        }
    }

    /**
     * 关联操作人：如果是该条数据的最后一个异常处理项，提示“已完成关联，请在异常记录中查看”，
     * 该异常处理中不再出现，异常记录中增加一条处理记录
     */
    private void connectOperator() {
        // TODO: 2019/5/17 加载操作人数据
        //测试：操作人列表数据
        ArrayList<ExceptionOperatorBean> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ExceptionOperatorBean bean = new ExceptionOperatorBean();
            bean.setOperator("小仙女" + i);
            bean.setId("6666666");
            list.add(bean);
        }
        //显示操作人选择
        DialogUtils.showSelectOperatorDialog(_mActivity, list, new SelectExceptionOperatorDialog.Builder.OnSelectOperatorListener() {
            @Override
            public void onSelectOperator(ExceptionOperatorBean operator) {
                showDealList.get(curPosition).setOperator(operator.getOperator());
                dealAdapter.notifyDataSetChanged();
                DialogUtils.showNoDialog(_mActivity, "已完成关联，请在异常记录中查看！", 2, "noJump", "");
            }

            @Override
            public void onCancel() {

            }
        });
    }

    /**
     * 关联患者
     * 1.绑定手术排班患者
     * 2.非手术排班医嘱患者
     */
    private void connectPatient() {
        // TODO: 2019/5/17 关联患者 （有两种患者）
        ToastUtils.showShort("关联患者");
    }

    /**
     * 移除判断：连续多次移除的耗材为异常耗材，记录最后一次标记的操作人与时间
     */
    private void removeJudge() {
        DialogUtils.showStoreDialog(_mActivity, 2, 104, null, 0);
    }

    /**
     * 关联出柜：
     * 1、根据配置，如果需要绑定患者显示“下一步”，不需要显示确定；
     * 2、如果是该条数据的最后一个异常处理项，提示“已完成关联，请在异常记录中查看”，
     * 该异常处理中不再出现，异常记录中增加一条处理记录
     */
    private void connectOutBox() {
        DialogUtils.showOutBoxConnectDialog(_mActivity, curPosition % 2 == 0, 2, 0);
    }


    @Override
    public void onBindViewBefore(View view) {

    }
}
