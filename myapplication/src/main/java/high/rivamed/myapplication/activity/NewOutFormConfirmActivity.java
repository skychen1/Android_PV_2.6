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
import android.util.SparseBooleanArray;
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
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.rivamed.DeviceManager;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.adapter.OutFormConfirmAdapter;
import high.rivamed.myapplication.base.App;
import high.rivamed.myapplication.base.BaseSimpleActivity;
import high.rivamed.myapplication.bean.BillOrderResultBean;
import high.rivamed.myapplication.bean.BillStockResultBean;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.FindBillOrderBean;
import high.rivamed.myapplication.bean.OrderSheetBean;
import high.rivamed.myapplication.bean.PushFormDateBean;
import high.rivamed.myapplication.bean.SureReciveOrder;
import high.rivamed.myapplication.dbmodel.BoxIdBean;
import high.rivamed.myapplication.dto.vo.InventoryVo;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.MusicPlayer;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;
import high.rivamed.myapplication.utils.UnNetCstUtils;
import high.rivamed.myapplication.views.LoadingDialog;
import high.rivamed.myapplication.views.SettingPopupWindow;
import high.rivamed.myapplication.views.TableTypeView;
import high.rivamed.myapplication.views.TwoDialog;

import static high.rivamed.myapplication.base.App.READER_TIME;
import static high.rivamed.myapplication.base.App.mPushFormOrders;
import static high.rivamed.myapplication.cont.Constants.FINISH_TIME;
import static high.rivamed.myapplication.cont.Constants.KEY_USER_NAME;
import static high.rivamed.myapplication.cont.Constants.KEY_USER_SEX;
import static high.rivamed.myapplication.cont.Constants.READER_TYPE;
import static high.rivamed.myapplication.cont.Constants.SAVE_DEPT_CODE;
import static high.rivamed.myapplication.cont.Constants.SAVE_RECEIVE_ORDERID;
import static high.rivamed.myapplication.cont.Constants.THING_CODE;
import static high.rivamed.myapplication.cont.Constants.UHF_TYPE;
import static high.rivamed.myapplication.devices.AllDeviceCallBack.mEthDeviceIdBack;
import static high.rivamed.myapplication.devices.AllDeviceCallBack.mEthDeviceIdBack3;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/29 17:41
 * 描述:       请领单确认领用界面
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class NewOutFormConfirmActivity extends BaseSimpleActivity {

   private String TAG = "NewOutFormConfirmActivity";
   public int      mSize;
   //重新扫描
   @BindView(R.id.timely_start_btn)
   public TextView mTimelyStartBtn;
   //打开柜门
   @BindView(R.id.timely_open_door)
   public TextView mTimelyOpenDoor;
   //查看医嘱清单
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
   public TextView      mAllOutText;
   public TableTypeView mTypeView;
   List<String> titeleList = null;

   public List<InventoryVo> mInventoryVos = new ArrayList<>(); //入柜扫描到的epc信息
   /**
    * 根据EPC请求网络参数
    */
   public  FindBillOrderBean       mFindBillOrderBean;
   /**
    * 确认领用使用参数
    */
   private OrderSheetBean.RowsBean mPrePageDate;
   /**
    * 所有耗材列表
    */
   private List<InventoryVo>       mInventoryVoList;
   /**
    * 柜子信息
    */
   List<BillStockResultBean.OrderDetailVo> mTransReceiveOrderDetailVosBean;
   private BillOrderResultBean   mBillOrderResultBean;
   private int                   mLayout;
   private View                  mHeadView;
   private OutFormConfirmAdapter mPublicAdapter;
   public SparseBooleanArray mCheckStates = new SparseBooleanArray();
   private LoadingDialog.Builder mLoading;
   /**
    * 传输的EPC
    *
    * @param event
    */
   private  List<String> mEPCMapDate = new ArrayList<>();

   private static boolean mIsFirst = true;

   @Override
   protected int getContentLayoutId() {
	return R.layout.activity_timely_layout;
   }

   @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
   public void onEventButton(Event.EventButton event) {
	LogUtils.i(TAG, "OutBoxBingActivity   少时诵诗书 cancel");
	if (event.type) {
	   if (event.bing) {//绑定的按钮转换
		for (InventoryVo b : mBillOrderResultBean.getInventoryVos()) {

		   if ((b.getIsErrorOperation() == 1 && b.getDeleteCount() == 0) ||
			 (b.getIsErrorOperation() == 1 && b.getDeleteCount() == 0 &&
			  b.getExpireStatus() == 0) ||
			 (!b.getRemark().equals("1") && b.getDeleteCount() == 0)) {
			mDownBtnOne.setEnabled(false);
			if (!b.getRemark().equals("1")){
			   mAllOutText.setVisibility(View.VISIBLE);
			   mAllOutText.setText(R.string.outform_error_string);
			}else {
			   mAllOutText.setVisibility(View.VISIBLE);
			   mAllOutText.setText(R.string.epc_error_string);
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

   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onEventLoading(Event.EventLoading event) {
	if (event.loading) {
	   if (mLoading == null) {
		LogUtils.i(TAG, "     mLoading  新建 ");
		mLoading = DialogUtils.showLoading(this);
	   } else {
		if (!mLoading.mDialog.isShowing()) {
		   LogUtils.i(TAG, "     mLoading   重新开启");
		   mLoading.create().show();
		}
	   }
	} else {
	   if (mLoading != null) {
		LogUtils.i(TAG, "     mLoading   关闭");
		mLoading.mAnimationDrawable.stop();
		mLoading.mDialog.dismiss();
		mLoading = null;
	   }
	}
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	super.initDataAndEvent(savedInstanceState);
	EventBusUtils.register(this);
	EventBusUtils.postSticky(new Event.EventLoading(true));
	mIsFirst = true;
	Event.EventBillStock data = (Event.EventBillStock) getIntent().getExtras()
		.getSerializable("DATA");
	mPrePageDate = data.orderSheetBean;
	mTransReceiveOrderDetailVosBean = data.transReceiveOrderDetailVosList;
	initView();
   }

   private void initView() {
	mBaseTabBack.setVisibility(View.GONE);
	mBaseTabIconRight.setEnabled(false);
	mBaseTabTvName.setEnabled(false);
	mBaseTabOutLogin.setEnabled(false);
	mBaseTabBtnMsg.setEnabled(false);
	mDownBtnOne.setEnabled(false);
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

	if (mFindBillOrderBean == null) {
	   mFindBillOrderBean = new FindBillOrderBean();
	   mFindBillOrderBean.setEpcs(new ArrayList<>());
	   mFindBillOrderBean.setDeviceIds(new ArrayList<>());

	}

	if (mInventoryVoList == null) {
	   mInventoryVoList = new ArrayList<>();
	}
	mBaseTabTvTitle.setText("请领单领用耗材");
	mTimelyNumber.setText(Html.fromHtml("耗材种类：<font color='#262626'><big>" + 0 +
							"</big>&emsp</font>耗材数量：<font color='#262626'><big>" + 0 +
							"</big></font>"));
	mTimelyStartBtn.setVisibility(View.VISIBLE);
	mDownBtnOneLL.setVisibility(View.VISIBLE);
	String[] array = mContext.getResources().getStringArray(R.array.six_form_arrays);
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
	mLyBingBtn.setText("查看术间请领单");

   }

   @OnClick({R.id.base_tab_tv_name, R.id.base_tab_icon_right, R.id.base_tab_tv_outlogin,
	   R.id.base_tab_btn_msg, R.id.base_tab_back, R.id.timely_start_btn, R.id.activity_btn_one,
	   R.id.ly_bing_btn, R.id.timely_open_door})
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
		mEthDeviceIdBack3.clear();
		mEthDeviceIdBack3.addAll(mEthDeviceIdBack);
		mEPCMapDate.clear();
		if (mFindBillOrderBean != null) {
		   mFindBillOrderBean.getEpcs().clear();
		   mFindBillOrderBean.getDeviceIds().clear();
		   mFindBillOrderBean = null;
		}
		mInventoryVoList.clear();
		for (String deviceInventoryVo : mEthDeviceIdBack) {
		   String deviceCode = deviceInventoryVo;
		   LogUtils.i(TAG, "deviceCode    " + deviceCode);
		   startScan(deviceCode);
		}
		break;
	   case R.id.timely_open_door:
		reOpenDoor();
		break;
	   case R.id.activity_btn_one:
		if (UIUtils.isFastDoubleClick(R.id.activity_btn_one)) {
		   return;
		} else {
		   mDownBtnOne.setEnabled(false);
		   sureTransReceiveOrder();
		}
		break;
	   case R.id.ly_bing_btn:
		LogUtils.i(TAG, "mTransReceiveOrderDetailVosBean   " +
				    mGson.toJson(mTransReceiveOrderDetailVosBean));
		DialogUtils.showLookUpDetailedListDialog(mContext, true,
								     mTransReceiveOrderDetailVosBean, mPrePageDate);
		break;
	}
   }

   private void initData() {
	mTimelyNumber.setText(Html.fromHtml(
		"耗材种类：<font color='#262626'><big>" + mBillOrderResultBean.getKindsOfCst() +
		"</big>&emsp</font>耗材数量：<font color='#262626'><big>" +
		mBillOrderResultBean.getCountNum() + "</big></font>"));
	mLayout = R.layout.item_formcon_six_layout;
	mHeadView = mContext.getLayoutInflater()
		.inflate(R.layout.item_formcon_six_title_layout, (ViewGroup) mLinearLayout.getParent(),
			   false);
	((TextView) mHeadView.findViewById(R.id.seven_one)).setText(titeleList.get(0));
	((TextView) mHeadView.findViewById(R.id.seven_two)).setText(titeleList.get(1));
	((TextView) mHeadView.findViewById(R.id.seven_three)).setText(titeleList.get(2));
	((TextView) mHeadView.findViewById(R.id.seven_four)).setText(titeleList.get(3));
	((TextView) mHeadView.findViewById(R.id.seven_five)).setText(titeleList.get(4));
	((TextView) mHeadView.findViewById(R.id.seven_six)).setText(titeleList.get(5));
	mPublicAdapter = new OutFormConfirmAdapter(mLayout, mInventoryVoList);
	mHeadView.setBackgroundResource(R.color.bg_green);

	mRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, LinearLayout.VERTICAL));
	mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
	mRefreshLayout.setEnableAutoLoadMore(false);
	mRefreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
	mRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
	mRecyclerview.setAdapter(mPublicAdapter);
	mLinearLayout.removeView(mHeadView);
	View inflate = LayoutInflater.from(this).inflate(R.layout.recy_null, null);
	mPublicAdapter.setEmptyView(inflate);
	mLinearLayout.addView(mHeadView);
	mPublicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
	   @Override
	   public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
		//TODO 10.23 选择要打开的耗材柜弹出框
		// DialogUtils.showSelectOpenCabinetDialog(mContext, genData6());
	   }
	});
   }

   private void getBillStockByEpc(FindBillOrderBean findBillOrderBean) {
	mInventoryVoList.clear();
	LogUtils.i(TAG, "mPrePageDategetSuiteId   " + mPrePageDate.getSuiteId());
	LogUtils.i(TAG, "mPrePageDategetOrderId   " + mPrePageDate.getOrderId());
	findBillOrderBean.setOrderId(mPrePageDate.getOrderId());
	findBillOrderBean.setThingId(SPUtils.getString(UIUtils.getContext(), THING_CODE));
	LogUtils.i(TAG, "json   " + mGson.toJson(findBillOrderBean));
	NetRequest.getInstance()
		.findBillStockByEpc(mGson.toJson(findBillOrderBean), this, new BaseResult() {
		   @Override
		   public void onSucceed(String result) {
			EventBusUtils.postSticky(new Event.EventLoading(false));
			LogUtils.i(TAG, "getBillStockByEpc   " + result);
			mBillOrderResultBean = mGson.fromJson(result, BillOrderResultBean.class);
//			if (mBillOrderResultBean.getErrorEpcs() != null &&
//			    mBillOrderResultBean.getErrorEpcs().size() > 0) {
//			   String string = StringUtils.listToString(mBillOrderResultBean.getErrorEpcs());
//			   ToastUtils.showLong(string);
//			   MusicPlayer.getInstance().play(MusicPlayer.Type.NOT_NORMAL);
//			}
			boolean isCanUse;
			if (mBillOrderResultBean.getInventoryVos() != null &&
			    mBillOrderResultBean.getInventoryVos().size() > 0) {
			   isCanUse = true;
			   mInventoryVoList.addAll(mBillOrderResultBean.getInventoryVos());
			} else {
			   isCanUse = false;
			   mDownBtnOne.setEnabled(false);
			   Toast.makeText(mContext, "未扫描到操作的耗材,即将返回主界面，请重新操作", Toast.LENGTH_SHORT).show();
			   new Handler().postDelayed(new Runnable() {
				public void run() {
				   finish();
				}
			   }, FINISH_TIME);
			}

			//是否可以点击领用按钮
			for (InventoryVo item : mInventoryVoList) {
			   if (!item.getRemark().equals("1")) {
				isCanUse = false;
				break;
			   }
			}
			if (isCanUse) {
			   mDownBtnOne.setEnabled(true);
			} else {
			   mDownBtnOne.setEnabled(false);
			}

			if (mPublicAdapter == null) {
			   initData();
			} else {
			   mPublicAdapter.notifyDataSetChanged();
			}
			EventBusUtils.post(new Event.EventButton(true, true));
		   }

		   @Override
		   public void onError(String result) {
			super.onError(result);
			EventBusUtils.postSticky(new Event.EventLoading(false));
		   }
		});
   }

   private void sureTransReceiveOrder() {
	mBillOrderResultBean.setOrderId(mPrePageDate.getOrderId());
	mBillOrderResultBean.setMedicalId(mPrePageDate.getMedicalId());
	mBillOrderResultBean.setDeptId(SPUtils.getString(UIUtils.getContext(), SAVE_DEPT_CODE));
	mBillOrderResultBean.setSurgeryId(mPrePageDate.getSurgeryId());
	mBillOrderResultBean.setThingId(SPUtils.getString(UIUtils.getContext(), THING_CODE));
	LogUtils.i(TAG, "mBillOrderResultBean   " + mGson.toJson(mBillOrderResultBean));
	NetRequest.getInstance()
		.sureReceiveOrder(mGson.toJson(mBillOrderResultBean), this, new BaseResult() {
		   @Override
		   public void onSucceed(String result) {
			mDownBtnOne.setEnabled(true);
			LogUtils.i(TAG, "getBillStockByEpc2s   " + result);
			SureReciveOrder sureReciveOrder = mGson.fromJson(result, SureReciveOrder.class);
			SPUtils.putString(mContext, SAVE_RECEIVE_ORDERID, mPrePageDate.getOrderId());

			if (sureReciveOrder.isOperateSuccess()) {
			   MusicPlayer.getInstance().play(MusicPlayer.Type.SUCCESS);
			   if (sureReciveOrder.getMsg().equals("") ||
				 sureReciveOrder.getMsg().contains("您已领取请领单中所有耗材")) {
				DialogUtils.showTwoDialog(mContext, mContext, 3, "耗材领用成功",
								  sureReciveOrder.getMsg());
			   } else {
				DialogUtils.showTwoDialog(mContext, mContext, 2, "耗材领用成功",
								  sureReciveOrder.getMsg());
			   }
			   UnNetCstUtils.putUnNetOperateYes(mGson, NewOutFormConfirmActivity.this);//提交离线耗材和重新获取在库耗材数据

			} else {
			   ToastUtils.showShort(sureReciveOrder.getMsg());
			}
			new Thread(new Runnable() {
			   @Override
			   public void run() {
				setPushFormOrderDate();
			   }
			}).start();
		   }

		   @Override
		   public void onError(String result) {
			mDownBtnOne.setEnabled(true);
			Log.e(TAG, "Erorr：" + result);
		   }
		});
   }

   /**
    * 需要上传的orders
    */
   private void setPushFormOrderDate() {
	if (mPushFormOrders != null && mPushFormOrders.size() == 0) {
	   PushFormDateBean.OrdersBean ordersBean = new PushFormDateBean.OrdersBean();
	   ordersBean.setOrderId(mPrePageDate.getOrderId());
	   mPushFormOrders.add(ordersBean);
	} else if (mPushFormOrders != null && mPushFormOrders.size() != 0) {
	   for (int x=0;x<mPushFormOrders.size();x++){
		if (!mPushFormOrders.get(x).getOrderId().equals(mPrePageDate.getOrderId())) {
		   PushFormDateBean.OrdersBean ordersBean = new PushFormDateBean.OrdersBean();
		   ordersBean.setOrderId(mPrePageDate.getOrderId());
		   mPushFormOrders.add(ordersBean);
		}
	   }
	}
   }

   private int k;

   @Subscribe(threadMode = ThreadMode.MAIN)
   public void scanEPCResult(Event.EventDeviceCallBack event) {
	if (mFindBillOrderBean == null) {
	   mFindBillOrderBean = new FindBillOrderBean();
	   mFindBillOrderBean.setEpcs(new ArrayList<>());
	   mFindBillOrderBean.setDeviceIds(new ArrayList<>());
	}
	if (mLoading != null) {
	   mLoading.mAnimationDrawable.stop();
	   mLoading.mDialog.dismiss();
	   mLoading = null;
	}
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
		if (boxIdBeansss.size() > 1) {
		   for (BoxIdBean BoxIdBean : boxIdBeansss) {
			LogUtils.i(TAG, "BoxIdBean.getDevice_id()   " + BoxIdBean.getDevice_id());
			if (BoxIdBean.getDevice_id().equals(event.deviceId)) {
			   mEPCMapDate.addAll(event.epcs);
			   k++;
			   LogUtils.i(TAG, "mEPCDate   " + mEPCMapDate.size());
			}
		   }
		   if (k == boxIdBeansss.size()) {
			LogUtils.i(TAG, "mEPCDate  zou l  ");
			k = 0;
			for (String epc :mEPCMapDate){
			   if (!mFindBillOrderBean.getEpcs().equals(epc)) {
				mFindBillOrderBean.getEpcs().add(epc);
			   }
			}

			mFindBillOrderBean.getDeviceIds().add(box_id);
//			getBillStockByEpc(mFindBillOrderBean);

		   }
		} else {
		   LogUtils.i(TAG, "event.epcs直接走   " + event.epcs.size());
		   for (String epc :event.epcs){
			if (!mFindBillOrderBean.getEpcs().equals(epc)) {
			   mFindBillOrderBean.getEpcs().add(epc);
			}
		   }

		   mFindBillOrderBean.getDeviceIds().add(box_id);
		}

	   }
	   if (mIsClick || mEthDeviceIdBack3.size() != 0) {
		return;
	   }
	   getBillStockByEpc(mFindBillOrderBean);
	}
   }

   /**
    * 重新打开柜门
    */
   private void reOpenDoor() {
      if (mFindBillOrderBean!=null){
	   mFindBillOrderBean.getEpcs().clear();
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
	if (mFindBillOrderBean != null) {
	   mFindBillOrderBean.getDeviceIds().clear();
	   mFindBillOrderBean.getEpcs().clear();
	   mInventoryVoList.clear();
	}
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
