package high.rivamed.myapplication.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ruihua.reader.net.bean.EpcInfo;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.OnClick;
import cn.rivamed.DeviceManager;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.adapter.BillOrderAdapter;
import high.rivamed.myapplication.base.App;
import high.rivamed.myapplication.base.BaseSimpleActivity;
import high.rivamed.myapplication.bean.BillOrderResultBean;
import high.rivamed.myapplication.bean.BillStockResultBean;
import high.rivamed.myapplication.bean.BingFindSchedulesBean;
import high.rivamed.myapplication.bean.BoxSizeBean;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.FindBillOrderBean;
import high.rivamed.myapplication.bean.OrderSheetBean;
import high.rivamed.myapplication.bean.UseCstOrderBean;
import high.rivamed.myapplication.dbmodel.BoxIdBean;
import high.rivamed.myapplication.dto.InventoryDto;
import high.rivamed.myapplication.dto.vo.InventoryVo;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.MusicPlayer;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.StringUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;
import high.rivamed.myapplication.utils.UnNetCstUtils;
import high.rivamed.myapplication.views.LoadingDialog;
import high.rivamed.myapplication.views.RvDialog;
import high.rivamed.myapplication.views.SettingPopupWindow;
import high.rivamed.myapplication.views.TwoDialog;

import static high.rivamed.myapplication.base.App.READER_TIME;
import static high.rivamed.myapplication.cont.Constants.CONFIG_007;
import static high.rivamed.myapplication.cont.Constants.CONFIG_009;
import static high.rivamed.myapplication.cont.Constants.CONFIG_012;
import static high.rivamed.myapplication.cont.Constants.CONFIG_019;
import static high.rivamed.myapplication.cont.Constants.FINISH_TIME;
import static high.rivamed.myapplication.cont.Constants.KEY_USER_NAME;
import static high.rivamed.myapplication.cont.Constants.KEY_USER_SEX;
import static high.rivamed.myapplication.cont.Constants.READER_TYPE;
import static high.rivamed.myapplication.cont.Constants.TEMP_AFTERBIND;
import static high.rivamed.myapplication.cont.Constants.THING_CODE;
import static high.rivamed.myapplication.cont.Constants.UHF_TYPE;
import static high.rivamed.myapplication.devices.AllDeviceCallBack.mEthDeviceIdBack;
import static high.rivamed.myapplication.devices.AllDeviceCallBack.mEthDeviceIdBack3;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/11 15:34
 * 描述:        套餐耗材扫描操作界面
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class NewOutMealBingConfirmActivity extends BaseSimpleActivity {

   private String TAG = "NewOutMealBingConfirmActivity";
   public int      my_id;
   public int      mSize;
   @BindView(R.id.timely_start_btn)
   public TextView mTimelyStartBtn;
   @BindView(R.id.timely_open_door)
   public TextView mTimelyOpenDoor;
   @BindView(R.id.ly_bing_btn)
   TextView mLyBingBtn;
   @BindView(R.id.timely_left)
   public TextView mTimelyLeft;
   @BindView(R.id.timely_right)
   public TextView mTimelyRight;
   @BindView(R.id.activity_down_btnll)
   LinearLayout mActivityDownBtnTwoll;
   @BindView(R.id.btn_four_ly)
   public TextView mBtnFourLy;
   @BindView(R.id.btn_four_yc)
   public TextView mBtnFourYc;
   @BindView(R.id.btn_four_tb)
   public TextView mBtnFourTb;
   @BindView(R.id.btn_four_th)
   public TextView mBtnFourTh;
   @BindView(R.id.activity_down_btn_four_ll)
   LinearLayout mActivityDownBtnFourLl;
   @BindView(R.id.activity_down_btn_one_ll)
   LinearLayout mDownBtnOneLL;
   @BindView(R.id.activity_btn_one)
   TextView     mDownBtnOne;
   @BindView(R.id.timely_name)
   TextView     mTimelyName;
   @BindView(R.id.timely_number)
   public TextView mTimelyNumber;
   @BindView(R.id.timely_ll)
   LinearLayout mLinearLayout;
   @BindView(R.id.recyclerview)
   RecyclerView mRecyclerview;
   @BindView(R.id.refreshLayout)
   public SmartRefreshLayout mRefreshLayout;
   @BindView(R.id.timely_rl_title)
   RelativeLayout mRelativeLayout;
   @BindView(R.id.timely_ll_gone)
   LinearLayout   mTimelyLlGone;
   @BindView(R.id.timely_number_left)
   TextView       mTimelyNumberLeft;
   @BindView(R.id.timely_start_btn_right)
   public TextView mTimelyStartBtnRight;
   @BindView(R.id.timely_open_door_right)
   public TextView mTimelyOpenDoorRight;
   @BindView(R.id.ly_bing_btn_right)
   TextView     mLyBingBtnRight;
   @BindView(R.id.timely_ll_gone_right)
   LinearLayout mTimelyLlGoneRight;
   @BindView(R.id.search_et)
   EditText     mSearchEt;
   @BindView(R.id.search_iv_delete)
   ImageView    mSearchIvDelete;
   @BindView(R.id.stock_search)
   FrameLayout  mStockSearch;
   @BindView(R.id.ly_creat_temporary_btn)
   TextView     mLyCreatTemporaryBtn;
   @BindView(R.id.dialog_left)
   TextView     mDialogLeft;
   @BindView(R.id.dialog_right)
   public TextView mDialogRight;
   @BindView(R.id.ly_bind_patient)
   public TextView mBindPatient;
   @BindView(R.id.activity_down_btn_seven_ll)
   LinearLayout   mActivityDownBtnSevenLl;
   @BindView(R.id.timely_rl)
   LinearLayout   mTimelyRl;
   @BindView(R.id.header)
   MaterialHeader mHeader;
   @BindView(R.id.public_ll)
   LinearLayout   mPublicLl;
   @BindView(R.id.tv_patient_conn)
   TextView       mTvPatientConn;
   @BindView(R.id.activity_down_patient_conn)
   LinearLayout   mActivityDownPatientConn;
   @BindView(R.id.all_out_text)
   public TextView mAllOutText;
   public String   mData;
   List<String> titeleList = null;
   public List<BingFindSchedulesBean.PatientInfoVos> patientInfos  = new ArrayList<>();
   public List<String> boxList  = new ArrayList<>();
   private int                                     mLayout;
   private int                                     mTitleLayout;
   private View                                    mHeadView;
   private BillOrderAdapter                        mPublicAdapter;
   /**
    * epc扫描请求数据使用
    */
   private OrderSheetBean.RowsBean                 mPrePageDate;
   /**
    * 查看套组清单使用
    */
   private List<BillStockResultBean.OrderDetailVo> mTransReceiveOrderDetailVos;
   /**
    * 根据EPC请求耗材列表参数
    */
   private FindBillOrderBean                       mFindBillOrderBean;
   /**
    * 查询耗材返回数据
    */
   private BillOrderResultBean                     mBillOrderResultBean;
   /**
    * 确认领用-请求参数
    */
   private UseCstOrderBean                         mUseCstOrderRequest;

   private int mNoTemPage = 1;
   private int mRows      = 20;
   private RvDialog.Builder mShowRvDialog2;
   private List<BingFindSchedulesBean.PatientInfoVos> mPatientInfos = new ArrayList<>();
   private List<BoxSizeBean.DevicesBean> mTbaseDevices;
   /**
    * 传输的EPC
    *
    * @param event
    */
   private Map<String, List<EpcInfo>> mEPCMapDate = new TreeMap<>();
   private LoadingDialog.Builder mLoading;

   @Override
   protected int getContentLayoutId() {
	return R.layout.activity_timely_layout;
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	super.initDataAndEvent(savedInstanceState);
	EventBusUtils.register(this);
	Event.EventBillStock data = (Event.EventBillStock) getIntent().getExtras()
		.getSerializable("DATA");
	mPrePageDate = data.orderSheetBean;
	mTransReceiveOrderDetailVos = data.transReceiveOrderDetailVosList;

	mTbaseDevices = data.tbaseDevices;
	mFindBillOrderBean = new FindBillOrderBean();
	mFindBillOrderBean.setSuiteId(mPrePageDate.getSuiteId());
	mFindBillOrderBean.setInventoryVos(new ArrayList<>());
	mFindBillOrderBean.setDeviceIds(new ArrayList<>());
	initData();
	if (mPublicAdapter != null && mBillOrderResultBean.getInventoryVos() != null) {
	   initView();
	}
   }
   @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
   public void onEventButton(Event.EventButton event) {
	LogUtils.i(TAG, "OutBoxBingActivity   少时诵诗书 cancel");
	if (event.type) {
	   if (event.bing) {//绑定的按钮转换
		for (InventoryVo b : mBillOrderResultBean.getInventoryVos()) {
		   if (UIUtils.getConfigType(mContext, CONFIG_009)&&!UIUtils.getConfigType(mContext, CONFIG_019) && ((b.getPatientId() == null || b.getPatientId().equals("")) ||
			  (b.getPatientName() == null || b.getPatientName().equals("")))) {
			mDownBtnOne.setEnabled(false);
			if (b.getPatientName() == null || b.getPatientName().equals("")){
			   mAllOutText.setVisibility(View.VISIBLE);
			   mAllOutText.setText(R.string.bind_error_string);
			}else {
			   mAllOutText.setVisibility(View.VISIBLE);
			   mAllOutText.setText(R.string.fast_out_error_string);
			}
			return;
		   }
		   if ((b.getIsErrorOperation() == 1 && b.getDeleteCount() == 0) ||
			 (b.getIsErrorOperation() == 1 && b.getDeleteCount() == 0 &&
			  b.getExpireStatus() == 0) || (UIUtils.getConfigType(mContext, CONFIG_007) && b.getPatientName() == null)) {
			mDownBtnOne.setEnabled(false);
			if (b.getPatientName() == null || b.getPatientName().equals("")){
			   mAllOutText.setVisibility(View.VISIBLE);
			   mAllOutText.setText(R.string.bind_error_string);
			}else {
			   mAllOutText.setVisibility(View.VISIBLE);
			   mAllOutText.setText(R.string.fast_out_error_string);
			}
			return;
		   } else {
			mDownBtnOne.setEnabled(true);
			mAllOutText.setVisibility(View.GONE);
		   }
		}
	   }
	}
   }
   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onEventLoading(Event.EventLoading event) {
	if (event.loading) {
	   if (mLoading == null) {
		mLoading = DialogUtils.showLoading(this);
	   } else {
		if (!mLoading.mDialog.isShowing()) {
		   mLoading.create().show();
		}
	   }
	} else {
	   if (mLoading != null) {
		mLoading.mAnimationDrawable.stop();
		mLoading.mDialog.dismiss();
		mLoading = null;
	   }
	}
   }

   /**
    * 数据加载
    */
   private void initData() {
	mBaseTabBack.setVisibility(View.GONE);
	mBaseTabIconRight.setEnabled(false);
	mBaseTabTvName.setEnabled(false);
	mBaseTabOutLogin.setEnabled(false);
	mBaseTabBtnMsg.setEnabled(false);
	mBaseTabTvTitle.setVisibility(View.VISIBLE);
	mBaseTabTvName.setText(SPUtils.getString(UIUtils.getContext(), KEY_USER_NAME));
	if (SPUtils.getString(UIUtils.getContext(), KEY_USER_SEX) != null &&
	    SPUtils.getString(UIUtils.getContext(), KEY_USER_SEX).equals("男")) {
	   Glide.with(this)
		   .load(R.mipmap.hccz_mrtx_nan)
		   .error(R.mipmap.hccz_mrtx_nan)
		   .into(mBaseTabIconRight);
	} else {
	   Glide.with(this)
		   .load(R.mipmap.hccz_mrtx_nv)
		   .error(R.mipmap.hccz_mrtx_nv)
		   .into(mBaseTabIconRight);
	}

	if (mUseCstOrderRequest == null) {
	   mUseCstOrderRequest = new UseCstOrderBean();
	   mUseCstOrderRequest.setInventoryVos(new ArrayList<>());
	}
	mBaseTabTvTitle.setText("套组领用识别耗材");
	mTimelyNumber.setText(Html.fromHtml("耗材种类：<font color='#262626'><big>" + 0 +
							"</big>&emsp</font>耗材数量：<font color='#262626'><big>" + 0 +
							"</big></font>"));
	mTimelyStartBtn.setVisibility(View.VISIBLE);
	mDownBtnOneLL.setVisibility(View.VISIBLE);
	String[] array;

	if ((UIUtils.getConfigType(mContext, CONFIG_007) ||UIUtils.getConfigType(mContext, CONFIG_019))) {
	   mBindPatient.setVisibility(View.VISIBLE);
	   mDownBtnOne.setEnabled(false);
	   array = mContext.getResources().getStringArray(R.array.seven_meal_arrays);
	} else {
	   mBindPatient.setVisibility(View.GONE);
	   mDownBtnOne.setEnabled(true);
	   array = mContext.getResources().getStringArray(R.array.six_ic_arrays);
	}
	titeleList = Arrays.asList(array);
	mSize = array.length;
	//重新扫描
	mTimelyStartBtn.setVisibility(View.VISIBLE);
	//打开柜门
	mTimelyOpenDoor.setVisibility(View.VISIBLE);
	//查看医嘱清单
	mLyBingBtn.setVisibility(View.VISIBLE);
	mTimelyStartBtn.setText("重新扫描");
	mTimelyOpenDoor.setText("打开柜门");
	mLyBingBtn.setText("查看套组清单");

	mTimelyOpenDoor.setEnabled(false);
	mLyBingBtn.setEnabled(false);
	mBindPatient.setEnabled(false);
   }

   @OnClick({R.id.base_tab_tv_name, R.id.base_tab_icon_right, R.id.base_tab_tv_outlogin,
	   R.id.base_tab_btn_msg, R.id.base_tab_back, R.id.ly_bing_btn_right, R.id.ly_bing_btn,
	   R.id.timely_start_btn, R.id.ly_bind_patient, R.id.timely_open_door, R.id.activity_btn_one})
   public void onViewClicked(View view) {
	switch (view.getId()) {
	   case R.id.base_tab_icon_right:
	   case R.id.base_tab_tv_name:
		mPopupWindow = new SettingPopupWindow(mContext);
		mPopupWindow.showPopupWindow(view);
		mPopupWindow.setmItemClickListener(new SettingPopupWindow.OnClickListener() {
		   @Override
		   public void onItemClick(int position) {
			switch (position) {
			   case 0:
				mContext.startActivity(new Intent(mContext, MyInfoActivity.class));
				break;
			   case 1:
				mContext.startActivity(new Intent(mContext, LoginInfoActivity.class));
				break;
			}
		   }
		});
		break;
	   case R.id.base_tab_tv_outlogin:
		TwoDialog.Builder builder = new TwoDialog.Builder(mContext, 1);
		builder.setTwoMsg("您确认要退出登录吗?");
		builder.setMsg("温馨提示");
		builder.setLeft("取消", new DialogInterface.OnClickListener() {
		   @Override
		   public void onClick(DialogInterface dialog, int i) {
			dialog.dismiss();
		   }
		});
		builder.setRight("确认", new DialogInterface.OnClickListener() {
		   @Override
		   public void onClick(DialogInterface dialog, int i) {
			mContext.startActivity(new Intent(mContext, LoginActivity.class));
			App.getInstance().removeALLActivity_();
			dialog.dismiss();
		   }
		});
		builder.create().show();
		break;
	   case R.id.base_tab_btn_msg:
		mContext.startActivity(new Intent(this, MessageActivity.class));
		break;
	   case R.id.base_tab_back:
		finish();
		break;
	   case R.id.timely_start_btn:
		if (UIUtils.isFastDoubleClick(R.id.timely_start_btn)) {
		   return;
		} else {
		   mEPCMapDate.clear();
		   if (mFindBillOrderBean.getInventoryVos()!=null){
			mFindBillOrderBean.getInventoryVos().clear();
			mFindBillOrderBean.getDeviceIds().clear();
		   }
		   mEthDeviceIdBack3.clear();
		   mEthDeviceIdBack3.addAll(mEthDeviceIdBack);
		   for (String deviceInventoryVo : mEthDeviceIdBack) {
			String deviceCode = deviceInventoryVo;
			LogUtils.i(TAG, "deviceCode    " + deviceCode);
			startScan(deviceCode);
		   }
		}
		break;
	   case R.id.timely_open_door:
		if (UIUtils.isFastDoubleClick(R.id.timely_open_door)) {
		   return;
		} else {
		   reOpenDoor();
		}
		break;
	   case R.id.ly_bing_btn:


		LogUtils.i(TAG, "mTransReceiveOrderDetailVos   " +
				    mGson.toJson(mTransReceiveOrderDetailVos));
		DialogUtils.showLookUpDetailedListDialog(mContext, false, mTransReceiveOrderDetailVos,
								     mPrePageDate);
		break;
	   case R.id.activity_btn_one:
		if (UIUtils.isFastDoubleClick(R.id.activity_btn_one)) {
		   return;
		} else {
		   useOrderCst();
		}
		break;
	   case R.id.ly_bind_patient:
	      if (StringUtils.isExceedTime(mBillOrderResultBean.getInventoryVos())){
		   DialogUtils.showNoDialog(mContext, "耗材中包含异常耗材，请取出异常耗材后再进行操作！", 1, "noJump", null);
		}else {
		   if (UIUtils.getConfigType(mContext, CONFIG_012)) {
			EventBusUtils.postSticky(new Event.EventButGone(true));//禁止触摸
			Intent intent = new Intent(mContext, TemPatientBindActivity.class);
			intent.putExtra("type", TEMP_AFTERBIND);
			intent.putExtra("mTemPTbaseDevices", (Serializable) mTbaseDevices);
			intent.putExtra("position", -1000);
			intent.putExtra("GoneType", "VISIBLE");
			startActivity(intent);
		   } else {
			EventBusUtils.postSticky(new Event.EventButGone(true));//禁止触摸
			Intent intent = new Intent(mContext, TemPatientBindActivity.class);
			intent.putExtra("type", TEMP_AFTERBIND);
			intent.putExtra("position", -1000);
			intent.putExtra("mTemPTbaseDevices", (Serializable) mTbaseDevices);
			intent.putExtra("GoneType", "GONE");
			startActivity(intent);
			//		   loadBingDateNoTemp("", 0, mTbaseDevices);
		   }
		}

		break;
	}
   }

   private void initView() {
	String[] array;
	if ((UIUtils.getConfigType(mContext, CONFIG_007) ||UIUtils.getConfigType(mContext, CONFIG_019))) {
	   array = mContext.getResources().getStringArray(R.array.seven_meal_arrays);
	   mLayout = R.layout.item_formcon_seven_layout;
	   mTitleLayout = R.layout.item_formcon_seven_title_layout;
	} else {
	   array = mContext.getResources().getStringArray(R.array.six_ic_arrays);
	   mLayout = R.layout.item_formcon_six_layout;
	   mTitleLayout = R.layout.item_formcon_six_title_layout;
	}
	titeleList = Arrays.asList(array);
	mSize = array.length;
	mHeadView = mContext.getLayoutInflater()
		.inflate(mTitleLayout, (ViewGroup) mLinearLayout.getParent(), false);
	((TextView) mHeadView.findViewById(R.id.seven_one)).setText(titeleList.get(0));
	((TextView) mHeadView.findViewById(R.id.seven_two)).setText(titeleList.get(1));
	((TextView) mHeadView.findViewById(R.id.seven_three)).setText(titeleList.get(2));
	((TextView) mHeadView.findViewById(R.id.seven_four)).setText(titeleList.get(3));
	((TextView) mHeadView.findViewById(R.id.seven_five)).setText(titeleList.get(4));
	if ((UIUtils.getConfigType(mContext, CONFIG_007) ||UIUtils.getConfigType(mContext, CONFIG_019))) {
	   ((TextView) mHeadView.findViewById(R.id.seven_seven)).setText(titeleList.get(5));
	   ((TextView) mHeadView.findViewById(R.id.seven_six)).setText(titeleList.get(6));
	} else {
	   ((TextView) mHeadView.findViewById(R.id.seven_six)).setText(titeleList.get(5));

	}
	mHeadView.setBackgroundResource(R.color.bg_green);
	mLinearLayout.removeView(mHeadView);
	if (mPublicAdapter != null) {
	   mPublicAdapter.notifyDataSetChanged();
	} else {
	   mPublicAdapter = new BillOrderAdapter(mLayout, mSize,
							     mBillOrderResultBean.getInventoryVos());

	   mRecyclerview.addItemDecoration(
		   new DividerItemDecoration(mContext, LinearLayout.VERTICAL));
	   mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
	   mRefreshLayout.setEnableAutoLoadMore(false);
	   mRefreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
	   mRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
	   mRecyclerview.setAdapter(mPublicAdapter);
	   mLinearLayout.removeAllViews();
	   View inflate = LayoutInflater.from(this).inflate(R.layout.recy_null, null);
	   mPublicAdapter.setEmptyView(inflate);
	   mLinearLayout.addView(mHeadView);
	}
   }

   /**
    * 根据EPC查询的套组耗材信息
    */
   private void findBillOrder() {

	NetRequest.getInstance()
		.findOrderCstListByEpc(mGson.toJson(mFindBillOrderBean), this, null, new BaseResult() {
		   @Override
		   public void onSucceed(String result) {
			EventBusUtils.postSticky(new Event.EventLoading(false));
			LogUtils.i(TAG, "result   " + result);
			mBillOrderResultBean = mGson.fromJson(result, BillOrderResultBean.class);
			mTimelyOpenDoor.setEnabled(true);
			mLyBingBtn.setEnabled(true);
			mBindPatient.setEnabled(true);
//			if (mBillOrderResultBean.getErrorEpcs() != null &&
//			    mBillOrderResultBean.getErrorEpcs().size() > 0) {
//			   String string = StringUtils.listToString(mBillOrderResultBean.getErrorEpcs());
//			   ToastUtils.showLong(string);
//			   MusicPlayer.getInstance().play(MusicPlayer.Type.NOT_NORMAL);
//			}
			if (mBillOrderResultBean.getInventoryVos() == null ||
			    mBillOrderResultBean.getInventoryVos().size() == 0) {
			   mDownBtnOne.setEnabled(false);
			   Toast.makeText(mContext, "未扫描到操作的耗材,即将返回主界面，请重新操作", Toast.LENGTH_SHORT).show();
			   new Handler().postDelayed(new Runnable() {
				public void run() {
				   finish();
				}
			   }, FINISH_TIME);
			} else {
			   if (mBillOrderResultBean.getMsg() != null) {
				ToastUtils.showLong(mBillOrderResultBean.getMsg());
			   }
			   if (mPublicAdapter == null) {
				initView();
			   } else {
				mPublicAdapter.setNewData(mBillOrderResultBean.getInventoryVos());
				mPublicAdapter.notifyDataSetChanged();
			   }
			   mTimelyNumber.setText(Html.fromHtml("耗材种类：<font color='#262626'><big>" +
									   mBillOrderResultBean.getKindsOfCst() +
									   "</big>&emsp</font>耗材数量：<font color='#262626'><big>" +
									   mBillOrderResultBean.getCountNum() +
									   "</big></font>"));
			   EventBusUtils.post(new Event.EventButton(true,true));
			}
		   }

		   @Override
		   public void onError(String result) {
			super.onError(result);
			EventBusUtils.postSticky(new Event.EventLoading(false));
			mTimelyOpenDoor.setEnabled(true);
			mLyBingBtn.setEnabled(true);
			mBindPatient.setEnabled(true);
		   }
		});
   }

   /**
    * 确认套组领用
    */
   private void useOrderCst() {
	mUseCstOrderRequest.getInventoryVos().clear();
	mUseCstOrderRequest.setInventoryVos(mBillOrderResultBean.getInventoryVos());
	mUseCstOrderRequest.setThingId(SPUtils.getString(UIUtils.getContext(), THING_CODE));
	if (mUseCstOrderRequest.getInventoryVos().size() == 0) {
	   ToastUtils.showShort("无耗材，无法领用");
	   return;
	}
	LogUtils.i(TAG, "JSON  " + mGson.toJson(mUseCstOrderRequest));
	NetRequest.getInstance()
		.useOrderCst(mGson.toJson(mUseCstOrderRequest), this,  new BaseResult() {
		   @Override
		   public void onSucceed(String result) {
			LogUtils.i(TAG, "result  " + result);
			InventoryDto info = mGson.fromJson(result, InventoryDto.class);
			List<InventoryVo> inventoryVos = info.getInventoryVos();
			EventBusUtils.post(new Event.EventMealType(inventoryVos));//用来判断套组是否已经领取
			if (info.isOperateSuccess()) {
			   MusicPlayer.getInstance().play(MusicPlayer.Type.SUCCESS);
			   ToastUtils.showShort(info.getMsg());
			   UnNetCstUtils.putUnNetOperateYes(mGson, NewOutMealBingConfirmActivity.this);//提交离线耗材和重新获取在库耗材数据
			   finish();
			} else {
			   ToastUtils.showShort(info.getMsg());
			}
		   }
		});
   }




   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onRvCheckBindEvent(Event.EventCheckbox event) {
	mUseCstOrderRequest.setPatientId(event.vo.getPatientId());
	for (InventoryVo item : mBillOrderResultBean.getInventoryVos()) {
	   item.setPatientId(event.vo.getPatientId());
	   item.setSurgeryId(event.vo.getSurgeryId());
	   item.setMedicalId(event.vo.getMedicalId());
	   item.setPatientName(event.vo.getPatientName());

	   if ("virtual".equals(event.vo.getPatientId())) {

		item.setOperationScheduleId(event.vo.getOperationScheduleId());
		item.setOperatingRoomName(event.vo.getOperatingRoomName());
		item.setOperatingRoomNo(event.vo.getOperatingRoomNo());
		item.setIdNo(event.vo.getIdNo());
		item.setSurgeryTime(event.vo.getSurgeryTime());
		item.setSex(event.vo.getSex());
		item.setCreate(event.vo.isCreate());
		item.setTempPatientId(event.vo.getTempPatientId());
		item.setMedicalId(event.vo.getMedicalId());
	   }

	   EventBusUtils.post(new Event.EventButton(true,true));
	}
	initView();
	mPublicAdapter.notifyDataSetChanged();
   }

   int k = 0;

   @Subscribe(threadMode = ThreadMode.MAIN)
   public void scanEPCResult(Event.EventDeviceCallBack event) {
//	AllDeviceCallBack.getInstance().initCallBack();
	List<BoxIdBean> boxIdBeanss = LitePal.where("device_id = ?", event.deviceId)
		.find(BoxIdBean.class);
	for (BoxIdBean boxIdBean : boxIdBeanss) {
	   String box_id = boxIdBean.getBox_id();
	   List<BoxIdBean> boxIdDoor = LitePal.where("box_id = ? and name = ?", box_id, UHF_TYPE)
		   .find(BoxIdBean.class);
	   for (BoxIdBean BoxIdBean : boxIdDoor) {
		String device_id = BoxIdBean.getDevice_id();
		for (int x = 0; x < mEthDeviceIdBack3.size(); x++) {
		   if (device_id.equals(mEthDeviceIdBack3.get(x))) {
			mEthDeviceIdBack3.remove(x);
		   }
		}
	   }
	   if (box_id != null) {
		List<BoxIdBean> boxIdBeansss = LitePal.where("box_id = ? and name = ?", box_id,
									   READER_TYPE).find(BoxIdBean.class);
		Log.e("xb", "boxIdBeansss.size" + boxIdBeansss.size());
		if (boxIdBeansss.size() > 1) {
		   for (BoxIdBean BoxIdBean : boxIdBeansss) {
			LogUtils.i(TAG, "BoxIdBean.getDevice_id()   " + BoxIdBean.getDevice_id());
			if (BoxIdBean.getDevice_id().equals(event.deviceId)) {
			   mEPCMapDate.putAll(event.epcs);
			   k++;
			   LogUtils.i(TAG, "mEPCDate   " + mEPCMapDate.size());
			}
		   }
		   if (k == boxIdBeansss.size()) {
			k = 0;
			for (Map.Entry<String, List<EpcInfo>> v : mEPCMapDate.entrySet()) {
			   InventoryVo item = new InventoryVo();
			   item.setEpc(v.getKey());
			   if (!mFindBillOrderBean.getInventoryVos().contains(item)) {
				mFindBillOrderBean.getInventoryVos().add(item);
			   }
			}
			mFindBillOrderBean.getDeviceIds().add(box_id);
//			findBillOrder();
		   }
		} else {
		   LogUtils.i(TAG, "event.epcs直接走   " + event.epcs.size());
		   for (Map.Entry<String, List<EpcInfo>> v : event.epcs.entrySet()) {
			InventoryVo item = new InventoryVo();
			item.setEpc(v.getKey());
			if (!mFindBillOrderBean.getInventoryVos().contains(item)) {
			   mFindBillOrderBean.getInventoryVos().add(item);
			}
		   }
		   mFindBillOrderBean.getDeviceIds().add(box_id);
//		   findBillOrder();
		}
	   }
	   LogUtils.i(TAG, "mEthDeviceIdBack3.size()  "+mEthDeviceIdBack3.size()+"   mIsClick   "+mIsClick);
	   if (mIsClick || mEthDeviceIdBack3.size() != 0) {
		return;
	   }
	   findBillOrder();
	}
   }
   /**
    * 重新打开柜门
    */
   private void reOpenDoor() {
	if (mFindBillOrderBean != null) {
	   mFindBillOrderBean.getInventoryVos().clear();
	   mFindBillOrderBean.getDeviceIds().clear();
	}
	for (String deviceInventoryVo : mEthDeviceIdBack) {
	   String deviceCode = deviceInventoryVo;
	   LogUtils.i(TAG, "deviceCode    " + deviceCode);
	   DeviceManager.getInstance().OpenDoor(deviceCode);
	}
   }

   @Subscribe(threadMode = ThreadMode.MAIN)
   public void isDoorOpened(Event.HomeNoClickEvent event) {
	if (event.isClick) {
	   DialogUtils.showNoDialog(mContext, "柜门已开", 2, "form", null);
	}
   }

   private void startScan(String deviceIndentify) {
	EventBusUtils.postSticky(new Event.EventLoading(true));
	List<BoxIdBean> boxIdBeans = LitePal.where("device_id = ? and name = ?", deviceIndentify,
								 UHF_TYPE).find(BoxIdBean.class);
	for (BoxIdBean boxIdBean : boxIdBeans) {
	   String box_id = boxIdBean.getBox_id();
	   List<BoxIdBean> deviceBean = LitePal.where("box_id = ? and name = ?", box_id, READER_TYPE)
		   .find(BoxIdBean.class);

	   for (BoxIdBean deviceid : deviceBean) {
		String device_id = deviceid.getDevice_id();
		int i = DeviceManager.getInstance().StartUhfScan(device_id, READER_TIME);
		LogUtils.i(TAG, "开始扫描了状态    " + i);
	   }
	}
   }

   @Override
   protected void onDestroy() {
	super.onDestroy();
	mEthDeviceIdBack3.clear();
   }
}