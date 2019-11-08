package high.rivamed.myapplication.adapter;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.activity.TimelyDetailsActivity;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.dto.InventoryDto;
import high.rivamed.myapplication.dto.vo.DeviceInventoryVo;
import high.rivamed.myapplication.dto.vo.InventoryGroupVos;
import high.rivamed.myapplication.dto.vo.InventoryVo;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;
import high.rivamed.myapplication.utils.EventBusUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.SPUtils;
import high.rivamed.myapplication.utils.UIUtils;

import static high.rivamed.myapplication.cont.Constants.THING_CODE;
import static high.rivamed.myapplication.devices.AllDeviceCallBack.mGson;

/**
 * 项目名称:    Android_PV_2.6.10
 * 创建者:      DanMing
 * 创建时间:    2019/11/6 0006 16:20
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class TimelyGroupCstAdapter extends BaseQuickAdapter<InventoryGroupVos, BaseViewHolder> {
   String mToJson;
   String mDeviceCode;
   public TimelyGroupCstAdapter(@Nullable List<InventoryGroupVos> mInventoryGroupVos,String mToJson,String mDeviceCode) {
	super(R.layout.item_stockgroup_title_layout, mInventoryGroupVos);
	this.mToJson=mToJson;
	this.mDeviceCode=mDeviceCode;
   }

   @Override
   protected void convert(
	   BaseViewHolder helper, InventoryGroupVos item) {
	List<InventoryVo> mInventoryVos = item.getInventoryVos();
	helper.setText(R.id.seven_one,item.getCstName());
	TextView seven_two=(TextView)helper.getView(R.id.seven_two);
//	helper.setText(R.id.seven_two,"实际扫描："+item.getCountActual()+"\n账面库存："+item.getCountStock());
	if (item.getCountActual()!=item.getCountStock()){
	   seven_two.setText(Html.fromHtml(
		   "实际扫描：<font color='#F5222D'>" + item.getCountActual() +"</font>"+ "<br>账面库存："+item.getCountStock()));
	}else {
	   seven_two.setText(Html.fromHtml(
		   "实际扫描：<font color='#262626'>" + item.getCountActual() +"</font>"+ "<br>账面库存："+item.getCountStock()));
	}
	RecyclerView mRecyclerView=(RecyclerView)helper.getView(R.id.recyclerviews);

	TimelyGroupCstDataAdapter dataAdapter = new TimelyGroupCstDataAdapter(item.getInventoryVos());
	mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
	mRecyclerView.setAdapter(dataAdapter);
	dataAdapter.setOnItemClickListener(new OnItemClickListener() {
	   @Override
	   public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
		String cstId = mInventoryVos.get(position).getCstCode();
		InventoryDto inventoryDto = mGson.fromJson(mToJson, InventoryDto.class);
		inventoryDto.setCstCode(cstId);
		String deviceCode1 = mInventoryVos.get(position).getDeviceId();

		InventoryDto dto = new InventoryDto();
		dto.setCstCode(cstId);
		String xxx = "";
		if (mDeviceCode == null || mDeviceCode.equals("")) {//全部的柜子详情
		   dto.setThingId(SPUtils.getString(UIUtils.getContext(), THING_CODE));
		   List<DeviceInventoryVo> deviceInventoryVos = inventoryDto.getDeviceInventoryVos();
		   List<DeviceInventoryVo> devo = new ArrayList<>();
		   for (int i = 0; i < deviceInventoryVos.size(); i++) {
			String deviceCode = deviceInventoryVos.get(i).getDeviceId();
			if (deviceCode.equals(deviceCode1)) {
			   devo.add(deviceInventoryVos.get(i));
			}
		   }
		   dto.setDeviceInventoryVos(devo);
		   xxx = mGson.toJson(dto);
		} else {//单柜
		   dto = inventoryDto;
		   xxx = mGson.toJson(dto);
		}
		LogUtils.i(TAG, "详情 xxx   " + xxx);
		NetRequest.getInstance().getDetailDate(xxx, this, new BaseResult() {
		   @Override
		   public void onSucceed(String result) {
			LogUtils.i(TAG, "详情 result   " + result);
			InventoryDto inventoryDto = mGson.fromJson(result, InventoryDto.class);
			inventoryDto.setEpcName(mInventoryVos.get(position).getCstName());
			inventoryDto.setCstSpec(mInventoryVos.get(position).getCstSpec());
			mContext.startActivity(new Intent(mContext, TimelyDetailsActivity.class));
			EventBusUtils.postSticky(new Event.timelyDate("详情", inventoryDto));
		   }
		});
	   }

	});
   }
}
