package high.rivamed.myapplication.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.ruihua.reader.ReaderManager;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import high.rivamed.myapplication.base.App;
import high.rivamed.myapplication.dbmodel.BoxIdBean;
import high.rivamed.myapplication.dto.InventoryDto;
import high.rivamed.myapplication.dto.entity.Inventory;
import high.rivamed.myapplication.dto.vo.DeviceInventoryVo;
import high.rivamed.myapplication.dto.vo.InventoryVo;

import static high.rivamed.myapplication.cont.Constants.KEY_ACCOUNT_ID;
import static high.rivamed.myapplication.cont.Constants.KEY_USER_NAME;
import static high.rivamed.myapplication.cont.Constants.READER_TYPE;
import static high.rivamed.myapplication.cont.Constants.SAVE_SEVER_IP;
import static high.rivamed.myapplication.cont.Constants.UHF_TYPE;
import static high.rivamed.myapplication.devices.AllDeviceCallBack.mEthDeviceIdBack;
import static high.rivamed.myapplication.timeutil.PowerDateUtils.getDates;
import static high.rivamed.myapplication.utils.UnNetCstUtils.getLocalAllCstVos;

/**
 * 项目名称:    Android_PV_2.6.6_514C
 * 创建者:      DanMing
 * 创建时间:    2019/6/12 14:02
 * 描述:       选择操作和绑定患者领用的部分逻辑抽取
 * 包名:        high.rivamed.myapplication.utils
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class LyDateUtils {

   /**
    * 是否包含epc
    *
    * @return
    */
   public static boolean getVosType(List<InventoryVo> vos, String epc) {
	if (vos != null) {
	   for (int i = 0; i < vos.size(); i++) {
		if (vos.get(i).getEpc().equals(epc)) {
		   return true;
		}
	   }
	}
	return false;
   }

   /**
    * 是否包含柜号
    * @return
    */
   public static boolean getVosBoxId(List<DeviceInventoryVo> vos, String box_id) {
	if (vos != null) {
	   for (int i = 0; i < vos.size(); i++) {
		if (vos.get(i).getDeviceId().equals(box_id)) {
		   return true;
		}
	   }
	}
	return false;
   }

   /**
    * 给显示的vos赋值（区分各个柜子）
    * @param box_id
    */
   public static void setAllBoxVosDate(List<InventoryVo> mBoxInventoryVos, String box_id) {
	List<InventoryVo> cstVos = getLocalAllCstVos();
	if (cstVos.size() > 0) {
	   for (int i = cstVos.size() - 1; i >= 0; i--) {
		if (!box_id.equals(cstVos.get(i).getDeviceId())) {
		   cstVos.remove(i);
		}
	   }
	   mBoxInventoryVos.addAll(cstVos);
	   for (int i = 0; i < mBoxInventoryVos.size() - 1; i++) {
		for (int x = mBoxInventoryVos.size() - 1; x > i; x--) {
		   if (mBoxInventoryVos.get(x).getEpc().equals(mBoxInventoryVos.get(i).getEpc())) {
			mBoxInventoryVos.remove(x);
		   }
		}
	   }
	}
   }

   /**
    * 重新扫描
    */
   public static void moreStartScan(
	   List<InventoryVo> mBoxInventoryVos, RxUtils.BaseEpcObservable mObs) {
	for (String deviceInventoryVo : mEthDeviceIdBack) {
	   String deviceCode = deviceInventoryVo;
	   startScan(mBoxInventoryVos, mObs, deviceCode);
	}
   }

   /**
    * 开始扫描
    * @param mBoxInventoryVos
    * @param mObs
    * @param deviceIndentify
    */
   public static void startScan(
	   List<InventoryVo> mBoxInventoryVos, RxUtils.BaseEpcObservable mObs,
	   String deviceIndentify) {
	List<BoxIdBean> boxIdBeans = LitePal.where("device_id = ? and name = ?", deviceIndentify,
								 UHF_TYPE).find(BoxIdBean.class);
	for (BoxIdBean boxIdBean : boxIdBeans) {
	   String box_id = boxIdBean.getBox_id();
	   List<BoxIdBean> deviceBean = LitePal.where("box_id = ? and name = ?", box_id, READER_TYPE)
		   .find(BoxIdBean.class);
	   for (BoxIdBean deviceid : deviceBean) {
		String device_id = deviceid.getDevice_id();
		int i = ReaderManager.getManager().startScan(device_id, 10000);
		if (i == 2) {
		   ReaderManager.getManager().stopScan(device_id);
		   ReaderManager.getManager().startScan(device_id, 10000);
		}
		setAllBoxVosDate(mBoxInventoryVos, box_id);
		if (mObs != null) {
		   mObs.removeVos();
		}
	   }
	}
//	return mBoxInventoryVos;
   }
   public static void setInventoryVoDate( List<InventoryVo> mBoxInventoryVos,List<DeviceInventoryVo> vos, int x) {
	List<Inventory> list = vos.get(x).getInventories();
	for (int i = 0; i < list.size(); i++) {
	   if (!getVosType(mBoxInventoryVos, list.get(i).getEpc())) {
		InventoryVo inventoryVo = new InventoryVo();
		inventoryVo.setEpc(list.get(i).getEpc());
		inventoryVo.setDeviceId(vos.get(x).getDeviceId());
		inventoryVo.setOperationStatus(99);
		inventoryVo.setStatus("禁止放入");
		inventoryVo.setIsErrorOperation(1);
		inventoryVo.setRenewTime(getDates());
		inventoryVo.setAccountId(SPUtils.getString(App.getAppContext(), KEY_ACCOUNT_ID));
		inventoryVo.setUserName(SPUtils.getString(App.getAppContext(), KEY_USER_NAME));
		mBoxInventoryVos.add(inventoryVo);
	   }
	}
   }
   /**
    * 无网的扫描后的EPC信息赋值 不绑定患者
    * @param toJson
    */
   public static InventoryDto setUnNetDate(Context mContext, Gson mGson,int mOperationType,String toJson, String result, int type) {
	if (SPUtils.getString(mContext, SAVE_SEVER_IP) != null && result.equals("-1") && (mOperationType == type)) {
	   return getInventoryDto(mContext, mGson, mOperationType, toJson);
	}
	return null;
   }
   /**
    * 无网的扫描后的EPC信息赋值  绑定患者
    * @param toJson
    */
   public static InventoryDto setUnNetDate(Context mContext, Gson mGson,int mOperationType,String toJson, String result) {
	if (SPUtils.getString(mContext, SAVE_SEVER_IP) != null && result.equals("-1")) {
	   return getInventoryDto(mContext, mGson, mOperationType, toJson);
	}
	return null;
   }
   @NonNull
   private static InventoryDto getInventoryDto(
	   Context mContext, Gson mGson, int mOperationType, String toJson) {
	List<InventoryVo> mInVo = new ArrayList<>();
	InventoryDto cc = LitePal.findFirst(InventoryDto.class);
	InventoryDto inventoryDto = new InventoryDto();
	inventoryDto.setOperation(mOperationType);
	inventoryDto.setThingId(cc.getThingId());
	InventoryDto dto = mGson.fromJson(toJson, InventoryDto.class);
	if (dto.getDeviceInventoryVos().size() > 0) {
	   List<Inventory> list = dto.getDeviceInventoryVos().get(0).getInventories();
	   String deviceId = dto.getDeviceInventoryVos().get(0).getDeviceId();
	   List<InventoryVo> vos = LitePal.where("deviceid = ? and status = ?", deviceId, "2")
		   .find(InventoryVo.class);
	   BoxIdBean boxIdBean = LitePal.where("device_id = ? ", deviceId)
		   .findFirst(BoxIdBean.class);
	   mInVo.addAll(vos);
	   if (list.size() != 0) {
		for (Inventory s : list) {
		   InventoryVo first = LitePal.where("epc = ? and deviceid = ?", s.getEpc(),
								 deviceId).findFirst(InventoryVo.class);
		   if (!getVosType(vos, s.getEpc())) {//无网放入
			InventoryVo inventoryVo = new InventoryVo();
			inventoryVo.setEpc(s.getEpc());
			inventoryVo.setDeviceId(deviceId);
			inventoryVo.setDeviceName(boxIdBean.getName());
			inventoryVo.setAccountId(SPUtils.getString(mContext, KEY_ACCOUNT_ID));
			inventoryVo.setUserName(SPUtils.getString(mContext, KEY_USER_NAME));
			inventoryVo.setIsErrorOperation(0);
			inventoryVo.setOperationStatus(99);
			inventoryVo.setStatus("2");
			inventoryVo.setRenewTime(getDates());
			if (!getVosType(mInVo, s.getEpc())) {//避免重复加入
			   mInVo.add(inventoryVo);
			}
		   } else {
			if (first != null) {
			   mInVo.remove(first);
			}
		   }
		}
	   }
	}
	inventoryDto.setInventoryVos(mInVo);
	return inventoryDto;
   }
}