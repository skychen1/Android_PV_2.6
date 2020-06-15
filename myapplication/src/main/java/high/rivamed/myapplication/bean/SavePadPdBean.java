package high.rivamed.myapplication.bean;

import java.io.Serializable;
import java.util.List;

import high.rivamed.myapplication.dto.vo.InventoryVo;

/**
 * 项目名称:    Android_PV_2.6.7.0
 * 创建者:      DanMing
 * 创建时间:    2019/7/22 0022 16:59
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class SavePadPdBean implements Serializable {
   private List<String>      epcs;
   private List<String>      deviceIds;
   private String            sthId;
   private String            thingId;
   private String            checkType;
   private String            checkFrom;
   private List<InventoryVo> inventoryVos;
   /**
    * operateSuccess : true
    * id : 0
    * opFlg : 200
    * pageNo : 1
    * pageSize : 20
    * epcs : []
   * checkType：（必传）盘点类型:{0.柜外,1.柜内}
    * checkFrom：（必传）盘点单来源,0自动服务生成,1手持机pda,2web 3:柜子端
    */

   private boolean           operateSuccess;
   private int               id;
   private String            opFlg;
   private int               pageNo;
   private int               pageSize;

   public String getCheckType() {
      return checkType;
   }

   public void setCheckType(String checkType) {
      this.checkType = checkType;
   }

   public String getCheckFrom() {
      return checkFrom;
   }

   public void setCheckFrom(String checkFrom) {
      this.checkFrom = checkFrom;
   }

   public List<InventoryVo> getInventoryVos() {
      return inventoryVos;
   }

   public void setInventoryVos(List<InventoryVo> inventoryVos) {
      this.inventoryVos = inventoryVos;
   }

   public List<String> getEpcs() {
	return epcs;
   }

   public void setEpcs(List<String> epcs) {
	this.epcs = epcs;
   }

   public List<String> getDeviceIds() {
	return deviceIds;
   }

   public void setDeviceIds(List<String> deviceIds) {
	this.deviceIds = deviceIds;
   }

   public String getSthId() {
	return sthId;
   }

   public void setSthId(String sthId) {
	this.sthId = sthId;
   }

   public String getThingId() {
	return thingId;
   }

   public void setThingId(String thingId) {
	this.thingId = thingId;
   }

   public boolean isOperateSuccess() { return operateSuccess;}

   public void setOperateSuccess(boolean operateSuccess) { this.operateSuccess = operateSuccess;}

   public int getId() { return id;}

   public void setId(int id) { this.id = id;}

   public String getOpFlg() { return opFlg;}

   public void setOpFlg(String opFlg) { this.opFlg = opFlg;}

   public int getPageNo() { return pageNo;}

   public void setPageNo(int pageNo) { this.pageNo = pageNo;}

   public int getPageSize() { return pageSize;}

   public void setPageSize(int pageSize) { this.pageSize = pageSize;}
}
