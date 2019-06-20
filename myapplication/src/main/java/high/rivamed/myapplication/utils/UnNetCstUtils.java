package high.rivamed.myapplication.utils;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import high.rivamed.myapplication.bean.UnNetOperateBean;
import high.rivamed.myapplication.dto.InventoryDto;
import high.rivamed.myapplication.dto.vo.InventoryVo;
import high.rivamed.myapplication.http.BaseResult;
import high.rivamed.myapplication.http.NetRequest;

import static high.rivamed.myapplication.cont.Constants.THING_CODE;
import static high.rivamed.myapplication.utils.LyDateUtils.getVosType;

/**
 * 项目名称:    Android_PV_2.6.5New
 * 创建者:      DanMing
 * 创建时间:    2019/1/24 14:30
 * 描述:        无网获取新的再柜耗材和数据库改变后的提交
 * 包名:        high.rivamed.myapplication.utils
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class UnNetCstUtils {

   private static List<InventoryVo> sVos;
   private static List<InventoryVo> mAllCstVos = new ArrayList<>();
   static int type = 0;//控制第一次请求，然后需要等到重新获取新的耗材后才重置
   /**
    * 所有耗材数据的获取（用于本地）
    */
   public static void getAllCstDate(Gson mGson, Object activity) {

	NetRequest.getInstance().getUnEntCstDate(activity, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		type=0;
		InventoryDto dto = mGson.fromJson(result, InventoryDto.class);
		List<InventoryVo> voList = LitePal.findAll(InventoryVo.class);
		InventoryDto localDto = new InventoryDto();
		List<InventoryVo> vos = dto.getInventoryVos();
		List<InventoryVo> tVos = new ArrayList<>();
		localDto.setThingId(dto.getThingId());
		localDto.setAccount(dto.getAccount());
		localDto.setTotalCount(dto.getTotalCount());
		if (vos.size()==0){
		   LitePal.deleteAll(InventoryVo.class);
		}
		if (voList.size()==0){
		   localDto.setInventoryVos(vos);
		   localDto.save();
		   tVos.addAll(vos);
		   for (InventoryVo tVo : tVos) {
		      tVo.setDateNetType(true);
		   }
		   LitePal.saveAll(tVos);
		}else {
		   List<InventoryVo> list = LitePal.where("operationstatus > ? ", "0")
			   .find(InventoryVo.class);
		   if (list.size()> 0){
			LitePal.deleteAll(InventoryVo.class,"operationstatus > ? ", "0");
		   }
		   for (int i = 0; i < vos.size(); i++) {
			vos.get(i).setDateNetType(true);
			if (!getVosType(voList,vos.get(i).getEpc())){
			   boolean save = vos.get(i).save();
			   Log.i("UnNetc","结束save     "+ save);
			}
		   }
		}
	   }
	});
   }
   /**
    * 获取本地所有的耗材数据信息
    * @return
    */
   public static List<InventoryVo> getLocalAllCstVos(){
	mAllCstVos.clear();
	mAllCstVos = LitePal.findAll(InventoryVo.class);
	return mAllCstVos;
   }
   /**
    * 判断耗材数据库是否有变动
    * @return
    */
   public static boolean getSqlChangeType(){
	sVos = LitePal.where("operationstatus > ? ", "0").find(InventoryVo.class);
	LogUtils.i("UNCC","sVos   "+sVos.size());
	if (sVos != null && sVos.size() > 0){
	   return true;
	}
      return false;
   }

   /**
    * 查询需要提交的离线后修改的耗材数据
    * @return
    */
   public static String putSqlDate(Gson mGson) {
	InventoryDto localDto = new InventoryDto();
	localDto.setThingId(SPUtils.getString(UIUtils.getContext(), THING_CODE));
	localDto.setInventoryVos(sVos);
	return mGson.toJson(localDto);
   }

   /**
    * 来网后判断然后进行数据提交
    */
   public static void putUnNetOperateYes(Gson mGson, Object activity){
	if (getSqlChangeType()){//判断数据库是否有改变
	   type ++;
	   if (type==1){
		LogUtils.i("UNCC","putSqlDate(mGson)   "+putSqlDate(mGson));
		NetRequest.getInstance().putUnNetCstDate(putSqlDate(mGson),UIUtils.getContext(),new BaseResult(){
		   @Override
		   public void onSucceed(String result) {
			UnNetOperateBean bean = mGson.fromJson(result, UnNetOperateBean.class);
			if (bean.getOpFlg().equals("200")){
			   getAllCstDate(mGson, activity);	//重新获取在库耗材数据
			}
		   }
		});
	   }
	}
   }
   /**
    * 删除数据库已有的已经操作过的耗材
    * @param result
    */
   public static void deleteVo(Gson mGson, String result, Activity activity) {
	List<InventoryVo> voList = LitePal.findAll(InventoryVo.class);
	InventoryDto inventoryDto = mGson.fromJson(result, InventoryDto.class);
	List<InventoryVo> vos = inventoryDto.getInventoryVos();
	for (InventoryVo vo : vos) {
	   for (int i = 0; i < voList.size(); i++) {
		if (voList.get(i).getEpc().equals(vo.getEpc())) {
		   voList.get(i).delete();
		}
	   }
	}
	getAllCstDate(mGson, activity);
   }
}
