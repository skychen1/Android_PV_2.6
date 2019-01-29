package high.rivamed.myapplication.utils;

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
   static int type = 0;//控制第一次请求，然后需要等到重新获取新的耗材后才重置
   /**
    * 所有耗材数据的获取（用于本地）
    */
   public static void getAllCstDate(Gson mGson, Object activity) {

	NetRequest.getInstance().getUnEntCstDate(activity, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		LitePal.deleteAll(InventoryDto.class);
		LitePal.deleteAll(InventoryVo.class);
		type=0;
		InventoryDto dto = mGson.fromJson(result, InventoryDto.class);
		InventoryDto localDto = new InventoryDto();
		List<InventoryVo> tVos = new ArrayList<>();
		localDto.setThingId(dto.getThingId());
		localDto.setAccount(dto.getAccount());
		localDto.setTotalCount(dto.getTotalCount());
		localDto.setInventoryVos(dto.getInventoryVos());
		localDto.save();
		tVos.addAll(dto.getInventoryVos());
		LitePal.saveAll(tVos);
	   }
	});
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
}
