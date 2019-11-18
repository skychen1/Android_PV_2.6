package high.rivamed.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.activity.TemPatientBindActivity;
import high.rivamed.myapplication.adapter.ExceptionDealAdapter;
import high.rivamed.myapplication.adapter.ExceptionRecordAdapter;
import high.rivamed.myapplication.base.SimpleFragment;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.ExceptionOperatorBean;
import high.rivamed.myapplication.bean.ExceptionRecordBean;
import high.rivamed.myapplication.bean.HospNameBean;
import high.rivamed.myapplication.bean.InventoryUnNormal;
import high.rivamed.myapplication.bean.InventoryUnNormalQueryReqVo;
import high.rivamed.myapplication.bean.UnKnowHandleVo;
import high.rivamed.myapplication.bean.inventoryUnNormalHandleVo;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.timeutil.PowerDateUtils;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;
import high.rivamed.myapplication.views.NoDialog;
import high.rivamed.myapplication.views.SelectExceptionOperatorDialog;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;
import static high.rivamed.myapplication.cont.Constants.CONFIG_007;
import static high.rivamed.myapplication.cont.Constants.CONFIG_012;
import static high.rivamed.myapplication.cont.Constants.ERROR_200;
import static high.rivamed.myapplication.cont.Constants.SAVE_DEPT_CODE;
import static high.rivamed.myapplication.cont.Constants.STYPE_EXCEPTION_LEFT;
import static high.rivamed.myapplication.cont.Constants.TEMP_AFTERBIND;
import static high.rivamed.myapplication.http.NetRequest.sThingCode;
import static high.rivamed.myapplication.utils.ExceptionDateUtils.getTrueDate;
import static high.rivamed.myapplication.utils.ExceptionDateUtils.getTrueUnKnownDate;

/**
 * 项目名称：高值
 * 创建者：chenyanling
 * 创建时间：2019/5/13
 * 描述：
 */
public class PublicExceptionFrag extends SimpleFragment {

   private static final String TYPE_SIZE  = "TYPE_SIZE";
   private static final String TYPE_PAGE  = "TYPE_PAGE";
   private static final String DEVICECODE = "DEVICECODE";
   @BindView(R.id.search_et)
   EditText           searchEt;
   @BindView(R.id.search_iv_delete)
   ImageView          searchIvDelete;
   @BindView(R.id.search_time_start)
   TextView           searchTimeStart;
   @BindView(R.id.search_time_end)
   TextView           searchTimeEnd;
   @BindView(R.id.search_type_all)
   RadioButton        searchTypeAll;
   @BindView(R.id.search_type_continue)
   RadioButton        searchTypeContinue;
   @BindView(R.id.search_type_unbind)
   RadioButton        searchTypeUnbind;
   @BindView(R.id.search_type_force_in)
   RadioButton        searchTypeForceIn;
   @BindView(R.id.search_type_force_out)
   RadioButton        searchTypeForceOut;
   @BindView(R.id.search_type_rg)
   RadioGroup         searchTypeRg;
   @BindView(R.id.ll_except_type)
   LinearLayout       llExceptType;
   @BindView(R.id.timely_ll)
   LinearLayout       timelyLl;
   @BindView(R.id.header)
   MaterialHeader     header;
   @BindView(R.id.recyclerview)
   RecyclerView       recyclerview;
   @BindView(R.id.refreshLayout)
   SmartRefreshLayout refreshLayout;
   @BindView(R.id.public_ll)
   LinearLayout       publicLl;
   @BindView(R.id.exception_bind_btn)
   TextView           exceptionBindBtn;
   @BindView(R.id.search_ly)
   LinearLayout       exceptionSearchLy;

   private static final int loadTime    = 200;//上下拉加载时间
   private static final int INTENT_TYPE = 222;//用于选择原因、库房、科室
   private              int PAGE        = 1;//当前页数
   private              int SIZE        = 20;//分页：每页数据
   private int    mType_size;//柜子tab序号
   private String mType_page;//异常处理还是异常记录tab
   private String mDeviceCode      = "";
   private String mSearchKey       = "";//搜索关键字
   private String mSearchStartTime = "", mSearchEndTime = "";//搜索开始结束时间
   private String mSearchType = "";//搜索异常类型
   private ExceptionDealAdapter               dealAdapter;//异常处理适配器
   private ExceptionRecordAdapter             recordAdapter;//异常记录适配器
   private List<ExceptionRecordBean.RowsBean> showDealList;//异常处理数据源
   private List<ExceptionRecordBean.RowsBean> showRecordList;//异常记录数据源
   private View                               empty;//空白页
   private boolean                            hasNextPage;//分页：是否有下一页
   private int                                curPosition;//异常处理：当前操作的数据项
   private NoDialog.Builder                   mNoDialog;
   private String mSearchTypeInt =""; //对数据进行筛选
   /**
    * 重新加载数据
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onStartFrag(Event.EventFrag event) {
	if (event.type.equals("START6")) {
	   PAGE = 1;
	   if (mType_page != null) {
		loadData();
	   }
	}
   }  /**
    * 重新加载数据
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onDialog(Event.EventExceptionDialog event) {
	Log.i("FATRE","event.type      "+event.type);
	if (event.type){
	   PAGE = 1;
	   if (mType_page.equals(STYPE_EXCEPTION_LEFT)){
		Log.i("FATRE","已完成关联");
		mNoDialog = DialogUtils.showNoDialog(mContext, "已完成关联，请在异常记录中查看！", 2, "noJump", "");
		if (showDealList != null) {
		   showDealList.clear();
		   loadDealData();
		}
	   }else {
		if (showRecordList != null) {
		   showRecordList.clear();
		   loadRecordData();
		}
	   }
	}else {
	   DialogUtils.showNoDialog(mContext, "关联失败，请重新操作！", 2, "noJump", "");
	}
   }

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

   @OnClick({R.id.search_time_start, R.id.search_time_end, R.id.exception_unknow_btn,
	   R.id.exception_outlink_btn, R.id.exception_bind_btn, R.id.exception_yichu_btn})
   public void onViewClicked(View view) {
	switch (view.getId()) {
	   case R.id.search_time_start:
		if (!UIUtils.isFastDoubleClick(R.id.search_time_start)) {
		   DialogUtils.showTimeDialog(mContext, searchTimeStart);
		}
		break;
	   case R.id.search_time_end:
		if (!UIUtils.isFastDoubleClick(R.id.search_time_end)) {
		   if (mSearchStartTime == null) {
			ToastUtils.showShort("请先选择开始时间");
		   } else {
			DialogUtils.showTimeDialog(mContext, searchTimeEnd);
		   }
		}
		break;
	   case R.id.exception_unknow_btn://关联操作人    8  完成
		if (!UIUtils.isFastDoubleClick(R.id.exception_unknow_btn)) {
		   List<ExceptionRecordBean.RowsBean> trueDate = getTrueUnKnownDate(showDealList);
		   if (trueDate != null && trueDate.size() > 0) {
			connectOperator(trueDate);
		   } else {
			ToastUtils.showShortToast(getString(R.string.exception_text));
		   }
		}
		break;
	   case R.id.exception_outlink_btn://出柜关联   7
		mSearchTypeInt = "7";
		if (!UIUtils.isFastDoubleClick(R.id.exception_outlink_btn)) {
		   List<ExceptionRecordBean.RowsBean> trueDate = getTrueDate(showDealList,mSearchTypeInt);
		   if (trueDate != null && trueDate.size() > 0) {
			connectOutBox();
		   } else {
			ToastUtils.showShortToast(getString(R.string.exception_text));
		   }
		}
		break;
	   case R.id.exception_bind_btn://绑定患者  3   完成
		mSearchTypeInt = "3";
		if (!UIUtils.isFastDoubleClick(R.id.exception_bind_btn)) {
		   List<ExceptionRecordBean.RowsBean> trueDate = getTrueDate(showDealList,mSearchTypeInt);
		   if (trueDate != null && trueDate.size() > 0) {
			connectPatient(trueDate);
		   } else {
			ToastUtils.showShortToast(getString(R.string.exception_text));
		   }
		}
		break;
	   case R.id.exception_yichu_btn://移除判断  1
		mSearchTypeInt = "1";
		if (!UIUtils.isFastDoubleClick(R.id.exception_yichu_btn)) {
		   List<ExceptionRecordBean.RowsBean> trueDate = getTrueDate(showDealList,mSearchTypeInt);
		   if (trueDate != null && trueDate.size() > 0) {
			removeJudge();
		   } else {
			ToastUtils.showShortToast(getString(R.string.exception_text));
		   }
		}
		break;
	}
   }

   @Override
   public int getLayoutId() {
	return R.layout.exception_deal_frag;
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	EventBusUtils.register(this);
	Log.i("FATRE","initDataAndEvent.type      ");
	Bundle arguments = getArguments();
	mType_size = arguments.getInt(TYPE_SIZE);
	mType_page = arguments.getString(TYPE_PAGE);
	mDeviceCode = arguments.getString(DEVICECODE);

	empty = LayoutInflater.from(_mActivity).inflate(R.layout.recy_null, null);
	searchTypeRg.check(R.id.search_type_all);
	initListener();
	if (mType_page.equals(STYPE_EXCEPTION_LEFT)) {
	   //异常处理
	   exceptionSearchLy.setVisibility(View.VISIBLE);
	   if (UIUtils.getConfigType(mContext, CONFIG_007)) {
		exceptionBindBtn.setVisibility(View.VISIBLE);
		searchTypeUnbind.setVisibility(View.VISIBLE);
	   } else {
		exceptionBindBtn.setVisibility(View.GONE);
		searchTypeUnbind.setVisibility(View.GONE);
	   }
	   initExceptionDeal();
	} else {
	   //异常记录
	   exceptionSearchLy.setVisibility(View.GONE);
	   initExceptionRecord();
	}
	loadData();
   }

   private void initListener() {
	//初始化时间选择
	Date date = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	String format = sdf.format(date);
	searchTimeStart.setHint(PowerDateUtils.getTime(1));
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
	searchEt.addTextChangedListener(new TextWatcher() {
	   @Override
	   public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	   }

	   @Override
	   public void onTextChanged(CharSequence s, int start, int before, int count) {
		mSearchKey = s.toString().trim();
	   }

	   @Override
	   public void afterTextChanged(Editable s) {
		if (!TextUtils.isEmpty(mSearchKey)) {
		   //加载数据
		   PAGE = 1;
		   loadData();
		}
	   }
	});

	//设置刷新监听
	//每次加载20条，数据做分页
	refreshLayout.setEnableAutoLoadMore(true);
	refreshLayout.setEnableRefresh(true);//是否启用下拉刷新功能
	refreshLayout.setEnableLoadMore(true);//是否启用上拉加载功能
	refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
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
	});
	refreshLayout.setOnRefreshListener(new OnRefreshListener() {

	   @Override
	   public void onRefresh(RefreshLayout refreshLayout) {
		//刷新
		PAGE = 1;
		loadData();
	   }
	});
	//异常类型搜索
	searchTypeRg.setOnCheckedChangeListener((group, checkedId) -> {
	   switch (checkedId) {
		case R.id.search_type_all:
		   //全部
		   mSearchType = "";
		   break;
		case R.id.search_type_continue:
		   //连续移除
		   mSearchType = "1";
		   break;
		case R.id.search_type_unbind:
		   //未绑定患者
		   mSearchType = "3";
		   break;
		case R.id.search_type_force_in:
		   //强开入柜
		   mSearchType = "5";
		   break;
		case R.id.search_type_force_out:
		   //强开出柜
		   mSearchType = "7";
		   break;
	   }
	   loadData();
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

	dealAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
	   @Override
	   public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
		if (dealAdapter.getData().get(position).isSelected()) {
		   dealAdapter.getData().get(position).setSelected(false);
		} else {
		   dealAdapter.getData().get(position).setSelected(true);
		}
		dealAdapter.notifyDataSetChanged();
	   }
	});

   }

   private void loadData() {
	if (mType_page.equals(STYPE_EXCEPTION_LEFT)) {
	   //异常处理
	   if (PAGE == 1) {
		showDealList.clear();
	   }
	   loadDealData();
	} else {
	   //异常记录
	   if (PAGE == 1) {
		showRecordList.clear();
	   }
	   loadRecordData();
	}
   }

   private void loadDealData() {
	InventoryUnNormal unNormal = new InventoryUnNormal();
	InventoryUnNormalQueryReqVo queryReqVo = new InventoryUnNormalQueryReqVo();
	queryReqVo.setDeviceId(mDeviceCode);
	queryReqVo.setCstNameOrEpcOrAccountName(mSearchKey);
	queryReqVo.setStartDate(mSearchStartTime);
	queryReqVo.setEndDate(mSearchEndTime);
	queryReqVo.setUnNormalSource(mSearchType);
	queryReqVo.setThingId(sThingCode);
	unNormal.setPageNo(PAGE + "");
	unNormal.setPageSize(SIZE + "");
	unNormal.setInventoryUnNormalQueryReqVo(queryReqVo);
	NetRequest.getInstance().getExceptionLeftDate(mGson.toJson(unNormal), this, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		ExceptionRecordBean bean = mGson.fromJson(result, ExceptionRecordBean.class);
		List<ExceptionRecordBean.RowsBean> rows = bean.getRows();
		for (ExceptionRecordBean.RowsBean row : rows) {
		   row.setSelected(false);
		}
		showDealList.addAll(rows);
		hasNextPage = (rows.size() > SIZE - 1);
		dealAdapter.notifyDataSetChanged();
	   }
	});
	dealAdapter.notifyDataSetChanged();
	finishLoading();
   }

   private void loadRecordData() {
	InventoryUnNormal unNormal = new InventoryUnNormal();
	InventoryUnNormalQueryReqVo queryReqVo = new InventoryUnNormalQueryReqVo();
	queryReqVo.setDeviceId(mDeviceCode);
	queryReqVo.setCstNameOrEpcOrAccountName(mSearchKey);
	queryReqVo.setStartDate(mSearchStartTime);
	queryReqVo.setEndDate(mSearchEndTime);
	queryReqVo.setThingId(sThingCode);
	unNormal.setPageNo(PAGE + "");
	unNormal.setPageSize(SIZE + "");
	unNormal.setInventoryUnNormalQueryReqVo(queryReqVo);
	NetRequest.getInstance()
		.getExceptionRightDate(mGson.toJson(unNormal), this, new BaseResult() {
		   @Override
		   public void onSucceed(String result) {
			ExceptionRecordBean bean = mGson.fromJson(result, ExceptionRecordBean.class);
			List<ExceptionRecordBean.RowsBean> rows = bean.getRows();
			showRecordList.addAll(rows);
			hasNextPage = (rows.size() > SIZE - 1);
			recordAdapter.notifyDataSetChanged();
		   }
		});

	recordAdapter.notifyDataSetChanged();
	finishLoading();
   }

   /**
    * 操作处理
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onEvent(Event.outBoxEvent event) {
	//只更新异常处理tab，过滤掉其他tab
	if (mType_page.equals(STYPE_EXCEPTION_LEFT) && ExceptionDealFrag.CURRENT_TAB == mType_size) {

	   switch (event.type) {
		case "104": //连续移除
		   //移除判断
		   List<ExceptionRecordBean.RowsBean> trueDate = getTrueDate(showDealList,mSearchTypeInt);
		   event.dialog.dismiss();
		   onRemoveJudgeEvent(event, trueDate);
		   break;
		case "105":
		   //出柜关联
		   onOutBoxConnectEvent(event);
		   break;
		case "106":
		   //出柜关联-下一步-绑定患者
		   event.dialog.dismiss();
		   Log.i("ffad","showDealList   "+mGson.toJson(showDealList));
		   List<ExceptionRecordBean.RowsBean> trueDates = getTrueDate(showDealList,mSearchTypeInt);
		   goToFirstBindAC(trueDates);
		   break;
		case "x":
		   //选择库房结果处理
		   if (event.mIntentType == INTENT_TYPE) {
			List<ExceptionRecordBean.RowsBean> trueDatess = getTrueDate(showDealList,mSearchTypeInt);
			inventoryUnNormalHandleVo handleVo = setOutBoxVoDate(trueDatess, event.context,"9");//设置值  移出
			putExceptionDate(handleVo);
			event.dialog.dismiss();
		   }
		   break;
		case "2":
		   //选择原因结果处理  退货
		   if (event.mIntentType == INTENT_TYPE) {
			List<ExceptionRecordBean.RowsBean> trueDatess = getTrueDate(showDealList,mSearchTypeInt);
			inventoryUnNormalHandleVo handleVo = setOutBoxVoDate(trueDatess, event.context,"8");//设置值  退货
			putExceptionDate(handleVo);
			event.dialog.dismiss();
		   }
		   break;
//		case "3":
//		   //选择科室结果处理
//		   if (event.mIntentType == INTENT_TYPE) {
//
//			dealAdapter.notifyDataSetChanged();
//			event.dialog.dismiss();
//			ToastUtils.showShort("选择科室ID：" + event.context);
//		   }
//		   break;
	   }
	}
   }

   private void onOutBoxConnectEvent(Event.outBoxEvent event) {
	String deptId = SPUtils.getString(UIUtils.getContext(), SAVE_DEPT_CODE);
	switch (event.context) {
	   case "0"://领用
		List<ExceptionRecordBean.RowsBean> trueDate = getTrueDate(showDealList,mSearchTypeInt);
		inventoryUnNormalHandleVo handleVo = setOutBoxVoDate(trueDate, null,"3");//设置值  领用
		putExceptionDate(handleVo);
		event.dialog.dismiss();
		break;
	   case "1"://移出
		//选择库房
		event.dialog.dismiss();
		NetRequest.getInstance().getHospBydept(deptId, this, new BaseResult() {
		   @Override
		   public void onSucceed(String result) {
			HospNameBean hospNameBean = mGson.fromJson(result, HospNameBean.class);
			DialogUtils.showStoreDialog(mContext, 2, 1, hospNameBean, INTENT_TYPE);
		   }
		});
		break;
	   //	   case "2"://调拨出库
	   //		//选择原因
	   //		DialogUtils.showStoreDialog(_mActivity, 2, 2, null, INTENT_TYPE);
	   //		break;
	   case "2"://退货
		event.dialog.dismiss();
		DialogUtils.showStoreDialog(_mActivity, 2, 2, null, INTENT_TYPE);
		break;
	}
   }

   /**
    * 移出
    * @param trueDate
    * @param type
    * @return
    */
   private inventoryUnNormalHandleVo setOutBoxVoDate(
	   List<ExceptionRecordBean.RowsBean> trueDate,String code, String type) {
	inventoryUnNormalHandleVo handleVo = new inventoryUnNormalHandleVo();
	List<inventoryUnNormalHandleVo.InventoryUnNormalHandleVosBean> vos = new ArrayList<>();
	for (ExceptionRecordBean.RowsBean bean : trueDate) {
	   inventoryUnNormalHandleVo.InventoryUnNormalHandleVosBean vosBean = new inventoryUnNormalHandleVo.InventoryUnNormalHandleVosBean();
	   vosBean.setThingId(sThingCode);
	   vosBean.setUnNormalId(bean.getUnNormalId());
	   vosBean.setOperationStatue("7");
	   vosBean.setOutStatue(type);
	   if (type!=null&&type.equals("9")) {//移出
		vosBean.setTargetSthId(code);
	   }else if(type!=null&&type.equals("8")){
		vosBean.setReturnReason(code);
	   }
	   vos.add(vosBean);
	}
	handleVo.setInventoryUnNormalHandleVos(vos);
	return handleVo;
   }

   /**
    * 移除的操作
    *
    * @param event
    */
   private void onRemoveJudgeEvent(
	   Event.outBoxEvent event, List<ExceptionRecordBean.RowsBean> trueDate) {
	inventoryUnNormalHandleVo handleVo = null;
	switch (event.context) {
	   case "0"://标签损坏  1
		handleVo = setRemoveDate(trueDate, "1");
		putExceptionDate(handleVo);
		break;
	   case "1"://取消异常标记  3
		handleVo = setRemoveDate(trueDate, "3");
		putExceptionDate(handleVo);
		break;
	   case "2"://出柜关联    7
		connectOutBox();
		break;
	}
   }

   /**
    * 移除
    * @param trueDate
    * @param type
    * @return
    */
   private inventoryUnNormalHandleVo setRemoveDate(
	   List<ExceptionRecordBean.RowsBean> trueDate, String type) {
	inventoryUnNormalHandleVo handleVo = new inventoryUnNormalHandleVo();
	List<inventoryUnNormalHandleVo.InventoryUnNormalHandleVosBean> vos = new ArrayList<>();
	for (ExceptionRecordBean.RowsBean bean : trueDate) {
	   inventoryUnNormalHandleVo.InventoryUnNormalHandleVosBean vosBean = new inventoryUnNormalHandleVo.InventoryUnNormalHandleVosBean();
	   vosBean.setUnNormalId(bean.getUnNormalId());
	   vosBean.setOperationStatue("1");
	   vosBean.setRemoveStatue(type);
	   vosBean.setThingId(sThingCode);
	   vos.add(vosBean);
	}
	handleVo.setInventoryUnNormalHandleVos(vos);
	return handleVo;
   }

   /**
    * 关联操作人：如果是该条数据的最后一个异常处理项，提示“已完成关联，请在异常记录中查看”，
    * 该异常处理中不再出现，异常记录中增加一条处理记录
    */
   private void connectOperator(List<ExceptionRecordBean.RowsBean> trueDate) {
	//显示操作人选择
	DialogUtils.showSelectOperatorDialog(_mActivity, trueDate,
							 new SelectExceptionOperatorDialog.Builder.OnSelectOperatorListener() {
							    @Override
							    public void onSelectOperator(
								    ExceptionOperatorBean.PageModelBean.RowsBean operator) {
								 UnKnowHandleVo knowHandleVo = new UnKnowHandleVo();
								 List<UnKnowHandleVo.UnKnowHandleVosBean> handleVos = new ArrayList<>();
								 for (ExceptionRecordBean.RowsBean bean : trueDate) {
								    UnKnowHandleVo.UnKnowHandleVosBean vosBean = new UnKnowHandleVo.UnKnowHandleVosBean();
								    vosBean.setUnNormalId(bean.getUnNormalId());
								    vosBean.setOperatorName(operator.getName());
								    vosBean.setOperatorId(operator.getAccountId());
								    vosBean.setThingId(sThingCode);
								    handleVos.add(vosBean);
								 }
								 knowHandleVo.setUnKnowHandleVos(handleVos);
								 putExceptionOperateUnknow(knowHandleVo);

							    }

							    @Override
							    public void onCancel() {
							    }
							 });
	dealAdapter.notifyDataSetChanged();
   }

   /**
    * 操作人绑定
    * @param handleVo
    */
   private void putExceptionOperateUnknow(UnKnowHandleVo handleVo) {
	NetRequest.getInstance()
		.getExceptionOperateUnknow(mGson.toJson(handleVo), this, new BaseResult() {
		   @Override
		   public void onSucceed(String result) {
			overPutEnter(result);
		   }
		});
	dealAdapter.notifyDataSetChanged();
   }

   /**
    * 出柜、绑定患者、连续移除确认
    * @param handleVo
    */
   private void putExceptionDate(inventoryUnNormalHandleVo handleVo) {
	NetRequest.getInstance()
		.getExceptionRelevance(mGson.toJson(handleVo), this, new BaseResult() {
		   @Override
		   public void onSucceed(String result) {
			overPutEnter(result);
		   }

		   @Override
		   public void onError(String result) {
			super.onError(result);
			EventBusUtils.post(new Event.EventExceptionDialog(false));
		   }
		});
   }

   /**
    * 处理完成后的页面刷新和提示，
    */
   private void overPutEnter(String result) {
	JSONObject jsonObject = JSON.parseObject(result);
	if (null == jsonObject.getString("opFlg") ||
	    jsonObject.getString("opFlg").equals(ERROR_200)) {//正常
	   Log.i("FATRE","opFlg");
	   EventBusUtils.post(new Event.EventExceptionDialog(true));
	}else {
	   EventBusUtils.post(new Event.EventExceptionDialog(false));
	}
   }

   /**
    * 关联患者
    * 1.绑定手术排班患者
    * 2.非手术排班医嘱患者
    */
   private void connectPatient(List<ExceptionRecordBean.RowsBean> trueDate) {
	// TODO: 2019/5/17 关联患者 （有两种患者）
	ToastUtils.showShort("关联患者");
	goToFirstBindAC(trueDate);
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
    * 该异常处理中不再出现，异常记录中增加一条处理记录   7
    */
   private void connectOutBox() {
	boolean hasNext = UIUtils.getConfigType(mContext, CONFIG_007) ? true : false;
	DialogUtils.showOutBoxConnectDialog(_mActivity, hasNext, 2, 0);
   }

   @Override
   public void onBindViewBefore(View view) {

   }

   private void goToFirstBindAC(List<ExceptionRecordBean.RowsBean> trueDate) {
	if (UIUtils.getConfigType(mContext, CONFIG_012)) {
	   mContext.startActivity(
		   new Intent(mContext, TemPatientBindActivity.class).putExtra("type", TEMP_AFTERBIND)
			   .putExtra("Exception", true)
			   .putExtra("GoneType", "VISIBLE")
			   .putExtra("ExceptionDate", (Serializable) trueDate));
	} else {
	   mContext.startActivity(
		   new Intent(mContext, TemPatientBindActivity.class).putExtra("Exception", true)
			   .putExtra("type", TEMP_AFTERBIND)
			   .putExtra("GoneType", "GONE")
			   .putExtra("ExceptionDate", (Serializable) trueDate));
	}
   }

   @Override
   public void onDestroyView() {
	super.onDestroyView();
	mSearchTypeInt="";
   }
}