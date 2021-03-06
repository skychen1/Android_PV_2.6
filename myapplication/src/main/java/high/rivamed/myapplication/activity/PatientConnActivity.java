package high.rivamed.myapplication.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.BaseSimpleActivity;
import high.rivamed.myapplication.bean.BingFindSchedulesBean;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.bean.FindInPatientBean;
import high.rivamed.myapplication.bean.PatientConnBean;
import high.rivamed.myapplication.bean.PatientConnResultBean;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.DialogUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.MusicPlayer;
import high.rivamed.myapplication.utils.ToastUtils;
import high.rivamed.myapplication.utils.UIUtils;
import high.rivamed.myapplication.views.RvDialog2;
import high.rivamed.myapplication.views.SettingPopupWindow;
import high.rivamed.myapplication.views.TableTypeView;
import high.rivamed.myapplication.views.TwoDialog;

import static high.rivamed.myapplication.base.App.SYSTEMTYPE;
import static high.rivamed.myapplication.cont.Constants.ACTIVITY;
import static high.rivamed.myapplication.cont.Constants.STYPE_DIALOG;
import static high.rivamed.myapplication.cont.Constants.SYSTEMTYPES_3;
import static high.rivamed.myapplication.utils.UIUtils.removeAllAct;

/*
 * 选择临时患者页面
 * */
public class PatientConnActivity extends BaseSimpleActivity {

   private static final String TAG = "PatientConnActivity";
   @BindView(R.id.search_et)
   EditText mSearchEt;
   @BindView(R.id.tv_patient_conn)
   TextView mTvConn;
   @BindView(R.id.base_tab_tv_title)
   public TextView mBaseTabTvTitle;
   @BindView(R.id.stock_search)
   FrameLayout  mStockSearch;
   @BindView(R.id.activity_down_patient_conn)
   LinearLayout mActivityDownPatientConn;
   @BindView(R.id.timely_ll)
   LinearLayout mLinearLayout;
   @BindView(R.id.recyclerview)
   RecyclerView mRecyclerview;
   @BindView(R.id.refreshLayout)
   public SmartRefreshLayout mRefreshLayout;
   private List<BingFindSchedulesBean.PatientInfoVos> mPatientInfos = new ArrayList<>();
   private RvDialog2.Builder mBuilder;
   private int    page    = 1;
   private String mString = "";
   List<String> titeleList = null;
   public int           mSize;
   public TableTypeView mTypeView;
   public List<BingFindSchedulesBean.PatientInfoVos> patientInfos = new ArrayList<>();

   @Override
   protected void onResume() {
	super.onResume();
	mString="";
	loadTempBingDate("");
	initDate();
	initlistener();
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	super.initDataAndEvent(savedInstanceState);
	loadTempBingDate("");
	mSearchEt.setHint("请输入患者姓名、患者ID、拼音码");
	mBaseTabTvTitle.setText("选择临时患者");
	mStockSearch.setVisibility(View.VISIBLE);
	mBaseTabBack.setVisibility(View.VISIBLE);
   }

   private void initDate() {
	String[] array = mContext.getResources().getStringArray(R.array.six_dialog_arrays);
	titeleList = Arrays.asList(array);
	mSize = titeleList.size();
	mTypeView = new TableTypeView(mContext, this, (Object) patientInfos, titeleList, mSize, mLinearLayout,
						mRecyclerview, mRefreshLayout, ACTIVITY, STYPE_DIALOG,-10);
   }

   private void initlistener() {
	mSearchEt.addTextChangedListener(new TextWatcher() {
	   @Override
	   public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

	   }

	   @Override
	   public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
		String trim = charSequence.toString().trim();
		loadTempBingDate(trim);
	   }

	   @Override
	   public void afterTextChanged(Editable editable) {

	   }
	});
   }

   @Override
   protected int getContentLayoutId() {
	return R.layout.activity_patientconn_layout;
   }

   @OnClick({R.id.tv_patient_conn, R.id.base_tab_icon_right, R.id.base_tab_tv_name,
	   R.id.base_tab_tv_outlogin, R.id.base_tab_btn_msg, R.id.base_tab_back})
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
				startActivity(new Intent(getApplicationContext(), MyInfoActivity.class));
				break;
			   case 1:
				if (SYSTEMTYPE.equals(SYSTEMTYPES_3)){
				   startActivity(new Intent(getApplicationContext(), LoginInfoActivity3.class));
				}else {
				   startActivity(new Intent(getApplicationContext(), LoginInfoActivity2.class));
				}
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
			dialog.dismiss();
			removeAllAct(mContext);
			MusicPlayer.getInstance().play(MusicPlayer.Type.LOGOUT_SUC);
		   }
		});
		builder.create().show();
		break;
	   case R.id.base_tab_btn_msg:
		if (UIUtils.isFastDoubleClick(R.id.base_tab_btn_msg)) {
		   return;
		} else {
		   mContext.startActivity(new Intent(this, MessageActivity.class));
		}
		break;
	   case R.id.base_tab_back:
		finish();
		break;
	   case R.id.tv_patient_conn://选择患者关联
		if (UIUtils.isFastDoubleClick(R.id.tv_patient_conn)) {
		   return;
		} else {
		   if (patientInfos.size() > 0) {
			mPatientInfos.clear();
			page = 1;
			loadAllDate("");
		   }
		}
		break;
	}
   }

   /**
    * 获取所有未绑定临时患者
    */
   private void loadTempBingDate(String optienNameOrId) {
	NetRequest.getInstance().findTempPatients(optienNameOrId, this, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LogUtils.i(TAG, "result   " + result);
		BingFindSchedulesBean bingFindSchedulesBean = mGson.fromJson(result,
												 BingFindSchedulesBean.class);
		if (bingFindSchedulesBean != null && bingFindSchedulesBean.getPatientInfoVos() != null) {
		   patientInfos.clear();
		   patientInfos.addAll(bingFindSchedulesBean.getPatientInfoVos());
//		   if (patientInfos.size() > 0) {
//			patientInfos.get(0).setSelected(true);
//		   }
		   mTypeView.mTempPatientAdapter.notifyDataSetChanged();
		}
	   }
	});
   }

   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onRvEvent(Event.EventString event) {
	if (event != null && event.mString != null) {
	   mString = event.mString;
	} else {
	   mString = "";
	}
	mPatientInfos.clear();
	page = 1;
	loadAllDate(mString);
   }

   /**
    * 查询所有在院患者信息
    */
   private void loadAllDate(String optienNameOrId) {
	final int rows = 20;
	NetRequest.getInstance()
		.findInPatientPage(optienNameOrId, page, rows, this, null, new BaseResult() {
		   @Override
		   public void onSucceed(String result) {
			LogUtils.i(TAG, "result    " + result);
			FindInPatientBean bean = mGson.fromJson(result, FindInPatientBean.class);

			if (bean != null && bean.getRows() != null && bean.getRows().size() > 0) {
			   boolean isClear;
			   if (mPatientInfos.size() == 0) {
				isClear = true;
			   } else {
				isClear = false;
			   }
			   if (mPatientInfos != null) {
				for (int i = 0; i < bean.getRows().size(); i++) {
				   BingFindSchedulesBean.PatientInfoVos data = new BingFindSchedulesBean.PatientInfoVos();
				   data.setPatientId(bean.getRows().get(i).getPatientId());
				   data.setPatientName(bean.getRows().get(i).getPatientName());
				   data.setDeptName(bean.getRows().get(i).getDeptName());
				   data.setDoctorName(bean.getRows().get(i).getDoctorName());
				   data.setRoomName(bean.getRows().get(i).getRoomName());
				   data.setSurgeryTime(bean.getRows().get(i).getSurgeryTime());
				   data.setUpdateTime(bean.getRows().get(i).getUpdateTime());
				   data.setSurgeryId(bean.getRows().get(i).getSurgeryId());
				   data.setMedicalId(bean.getRows().get(i).getMedicalId());
				   data.setHisPatientId(bean.getRows().get(i).getHisPatientId());
				   data.setBedNo(bean.getRows().get(i).getBedNo());
				   data.setWardName(bean.getRows().get(i).getWardName());
				   mPatientInfos.add(data);
				}

				if (mBuilder != null && mBuilder.mDialog.isShowing()) {
				   RvDialog2.sTableTypeView2.mBingOutAdapter.notifyDataSetChanged();
				} else {
				   mBuilder = DialogUtils.showRvDialog2(PatientConnActivity.this, mContext,
										    mPatientInfos,
										    new OnClickBackListener() {
											 @Override
											 public void OnClickBack(
												 int position,
												 DialogInterface dialog) {
											    Log.e("PatientConnActivity",
												    "mTypeView.mTempPatientAdapter position:" +
												    mTypeView.mTempPatientAdapter.mSelectedPos);
											    Log.e("PatientConnActivity",
												    "showRvDialog2 position:" +
												    position);
											    //进行患者关联
											    if (bean.getRows().size() >
												  0) {
												 connPatient(position,
														 dialog);
											    }else {
												 mTvConn.setEnabled(false);
											    }
											 }
										    });
				   mBuilder.mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
					@Override
					public void onRefresh(RefreshLayout refreshLayout) {
					   mBuilder.mRefreshLayout.setNoMoreData(false);
					   page = 1;
					   mPatientInfos.clear();
					   loadAllDate(mString);
					   mBuilder.mRefreshLayout.finishRefresh();
					}
				   });
				   mBuilder.mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
					@Override
					public void onLoadMore(RefreshLayout refreshLayout) {
					   page++;
					   loadAllDate(mString);
					   mBuilder.mRefreshLayout.finishLoadMore();
					}
				   });
				}
			   } else {
				for (int i = 0; i < bean.getRows().size(); i++) {
				   BingFindSchedulesBean.PatientInfoVos data = new BingFindSchedulesBean.PatientInfoVos();
				   data.setPatientId(bean.getRows().get(i).getPatientId());
				   data.setPatientName(bean.getRows().get(i).getPatientName());
				   data.setDeptName(bean.getRows().get(i).getDeptName());
				   data.setDoctorName(
					   bean.getRows().get(i).getDoctorName());
				   data.setRoomName(
					   bean.getRows().get(i).getRoomName());
				   data.setSurgeryTime(bean.getRows().get(i).getSurgeryTime());
				   data.setUpdateTime(bean.getRows().get(i).getUpdateTime());
				   data.setSurgeryId(bean.getRows().get(i).getSurgeryId());
				   data.setPatientId(bean.getRows().get(i).getPatientId());
				   data.setMedicalId(bean.getRows().get(i).getMedicalId());
				   data.setHisPatientId(bean.getRows().get(i).getHisPatientId());
				   data.setBedNo(bean.getRows().get(i).getBedNo());
				   data.setWardName(bean.getRows().get(i).getWardName());
				   mPatientInfos.add(data);
				}
//				if (isClear && mPatientInfos.size() > 0) {
//				   mPatientInfos.get(0).setSelected(true);
//				}
				mBuilder = DialogUtils.showRvDialog2(PatientConnActivity.this, mContext,
										 mPatientInfos,
										 new OnClickBackListener() {
										    @Override
										    public void OnClickBack(
											    int position,
											    DialogInterface dialog) {
											 Log.e("PatientConnActivity",
												 "mTypeView.mTempPatientAdapter position:" +
												 mTypeView.mTempPatientAdapter.mSelectedPos);
											 Log.e("PatientConnActivity",
												 "showRvDialog2 position:" +
												 position);
											 //进行患者关联
											 if (bean.getRows().size() > 0) {

											    connPatient(position, dialog);
											 }else {
											    mTvConn.setEnabled(false);
											 }
										    }
										 });
				mBuilder.mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
				   @Override
				   public void onRefresh(RefreshLayout refreshLayout) {
					mBuilder.mRefreshLayout.setNoMoreData(false);
					page = 1;
					mPatientInfos.clear();
					loadAllDate(mString);
					mBuilder.mRefreshLayout.finishRefresh();
				   }
				});
				mBuilder.mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
				   @Override
				   public void onLoadMore(RefreshLayout refreshLayout) {
					page++;
					loadAllDate(mString);
					mBuilder.mRefreshLayout.finishLoadMore();
				   }
				});
			   }
			}else {
			   if (page==1&& RvDialog2.sTableTypeView2!=null&&RvDialog2.sTableTypeView2.mBingOutAdapter!=null){
				RvDialog2.sTableTypeView2.mBingOutAdapter.getData().clear();
				RvDialog2.sTableTypeView2.mBingOutAdapter.notifyDataSetChanged();

			   }
			}
		   }
		});
   }

   //进行患者关联
   private void connPatient(int position, DialogInterface dialog) {
	PatientConnBean bean = new PatientConnBean();
	bean.setPatientId(mPatientInfos.get(position).getPatientId());
	bean.setMedicalId(mPatientInfos.get(position).getMedicalId());
	bean.setSurgeryId(mPatientInfos.get(position).getSurgeryId());
	bean.setTempPatientId(patientInfos.get(mTypeView.mTempPatientAdapter.mSelectedPos).getTempPatientId());
	LogUtils.i(TAG, "mGson.toJson(bean)   " + mGson.toJson(bean));
	NetRequest.getInstance()
		.tempPatientConnPatient(mGson.toJson(bean), this, new BaseResult() {
		   @Override
		   public void onSucceed(String result) {
			LogUtils.i(TAG, "result   " + result);
			try {
			   PatientConnResultBean bean = mGson.fromJson(result,
										     PatientConnResultBean.class);
			   if (bean.isOperateSuccess()) {
				DialogUtils.showNoDialog(mContext, bean.getMsg(), 2, "form", null);
				loadTempBingDate("");
			   } else {
				ToastUtils.showShort(bean.getMsg());
			   }
			} catch (Exception e) {
			   ToastUtils.showShort("关联患者失败");
			   e.printStackTrace();
			}
			dialog.dismiss();
		   }
		});
   }

   //提供接口
   public interface OnClickBackListener {

	void OnClickBack(int x, DialogInterface dialog);
   }

}