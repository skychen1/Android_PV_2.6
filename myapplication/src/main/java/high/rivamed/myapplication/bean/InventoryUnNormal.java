package high.rivamed.myapplication.bean;

import java.io.Serializable;

/**
 * 项目名称:    Android_PV_2.6.6_514C
 * 创建者:      DanMing
 * 创建时间:    2019/6/27 11:32
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class InventoryUnNormal implements Serializable {
   private InventoryUnNormalQueryReqVo inventoryUnNormalQueryReqVo;
   private String pageNo;
   private String pageSize;

   public InventoryUnNormalQueryReqVo getInventoryUnNormalQueryReqVo() {
	return inventoryUnNormalQueryReqVo;
   }

   public void setInventoryUnNormalQueryReqVo(
	   InventoryUnNormalQueryReqVo inventoryUnNormalQueryReqVo) {
	this.inventoryUnNormalQueryReqVo = inventoryUnNormalQueryReqVo;
   }

   public String getPageNo() {
	return pageNo;
   }

   public void setPageNo(String pageNo) {
	this.pageNo = pageNo;
   }

   public String getPageSize() {
	return pageSize;
   }

   public void setPageSize(String pageSize) {
	this.pageSize = pageSize;
   }
}
