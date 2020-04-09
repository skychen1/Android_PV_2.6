package high.rivamed.myapplication.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称:    Android_PV_2.6.14
 * 创建者:      DanMing
 * 创建时间:    2020/4/2 0002 10:42
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class InBoxOrderDialogBean implements Serializable {

   /**
    * operateSuccess : true
    * id : 0
    * opFlg : 200
    * filterStr :
    * pageNo : 1
    * pageSize : 20
    * sthId : 4028effb70cd58d40170cd6358750000
    * orderVos : [{"orderId":"ff8081816e9708f8016ea00b8f260076","createTime":"2019-11-25 00:52:22","orderNo":"RK-1911250101","inventoryNum":8,"inStockNum":6,"currentNum":0},{"orderId":"ff8081816e9708f8016ea00b8f26007a","createTime":"2019-11-25 00:52:22","orderNo":"RK-1911250102","inventoryNum":2,"inStockNum":2,"currentNum":0},{"orderId":"ff8081816e9708f8016ea00b8f260080","createTime":"2019-11-25 00:52:22","orderNo":"RK-1911250103","inventoryNum":3,"inStockNum":3,"currentNum":0},{"orderId":"ff8081816e9708f8016ea00b8f260081","createTime":"2019-11-25 00:52:22","orderNo":"RK-1911250104","inventoryNum":4,"inStockNum":4,"currentNum":0},{"orderId":"ff8081816e9708f8016ea00b8f260082","createTime":"2019-11-25 00:52:22","orderNo":"RK-1911250105","inventoryNum":2,"inStockNum":2,"currentNum":0},{"orderId":"ff8081816e9708f8016ea00b 8f260083","createTime":"2019-11-25 00:52:22","orderNo":"RK-1911250106","inventoryNum":2,"inStockNum":2,"currentNum":0},{"orderId":"ff8081816e9708f8016ea00b8f260084","createTime":"2019-11-25 00:52:22","orderNo":"RK-1911250107","inventoryNum":4,"inStockNum":4,"currentNum":0},{"orderId":"ff8081816e9708f8016ea00b8f260085","createTime":"2019-11-25 00:52:22","orderNo":"RK-1911250108","inventoryNum":6,"inStockNum":5,"currentNum":0},{"orderId":"ff8081816e9708f8016ea00b8f260086","createTime":"2019-11-25 00:52:22","orderNo":"RK-1911250109","inventoryNum":3,"inStockNum":1,"currentNum":0},{"orderId":"ff8081816e9708f8016ea00b8f260087","createTime":"2019-11-25 00:52:22","orderNo":"RK-1911250110","inventoryNum":4,"inStockNum":3,"currentNum":0},{"orderId":"ff8081816e9708f8016ea00b8f260088","createTime":"2019-11-25 00:52:22","orderNo":"RK-1911250111","inventoryNum":3,"inStockNum":3,"currentNum":0},{"orderId":"ff8081816e9708f8016ea00b8f260089","createTime":"2019-11-25 00:52:22","orderNo":"RK-1911250112","inventoryNum":2,"inSto ckNum":2,"currentNum":0},{"orderId":"ff8081816e9708f8016ea00b8f260090","createTime":"2019-11-25 00:52:22","orderNo":"RK-1911250113","inventoryNum":2,"inStockNum":2,"currentNum":0},{"orderId":"ff8081816e9708f8016ea00b8f260091","createTime":"2019-11-25 00:52:22","orderNo":"RK-1911250114","inventoryNum":4,"inStockNum":3,"currentNum":0},{"orderId":"ff8081816e9708f8016ea00b8f260092","createTime":"2019-11-25 00:52:22","orderNo":"RK-1911250115","inventoryNum":2,"inStockNum":2,"currentNum":0},{"orderId":"ff8081816e9708f8016ea00b8f260093","createTime":"2019-11-25 00:52:22","orderNo":"RK-1911250116","inventoryNum":2,"inStockNum":2,"currentNum":0},{"orderId":"ff8081816e9708f8016ea00b8f260094","createTime":"2019-11-25 00:52:22","orderNo":"RK-1911250117","inventoryNum":4,"inStockNum":4,"currentNum":0},{"orderId":"ff8081816e9708f8016ea00b8f260095","createTime":"2019-11-25 00:52:22","orderNo":"RK-1911250118","inventoryNum":3,"inStockNum":3,"currentNum":0},{"orderId":"ff8081816e9708f8016ea00b8f260096","createTime":"2019-11-2 5 00:52:22","orderNo":"RK-1911250119","inventoryNum":3,"inStockNum":3,"currentNum":0},{"orderId":"ff8081816e9708f8016ea00b8f260097","createTime":"2019-11-25 00:52:22","orderNo":"RK-1911250120","inventoryNum":4,"inStockNum":3,"currentNum":0}]
    */

   private boolean operateSuccess;
   private int                id;
   private String             opFlg;
   private String             filterStr;
   private int                pageNo;
   private int                pageSize;
   private String             sthId;
   private List<OrderVosBean> orderVos;

   public boolean isOperateSuccess() { return operateSuccess;}

   public void setOperateSuccess(boolean operateSuccess) { this.operateSuccess = operateSuccess;}

   public int getId() { return id;}

   public void setId(int id) { this.id = id;}

   public String getOpFlg() { return opFlg;}

   public void setOpFlg(String opFlg) { this.opFlg = opFlg;}

   public String getFilterStr() { return filterStr;}

   public void setFilterStr(String filterStr) { this.filterStr = filterStr;}

   public int getPageNo() { return pageNo;}

   public void setPageNo(int pageNo) { this.pageNo = pageNo;}

   public int getPageSize() { return pageSize;}

   public void setPageSize(int pageSize) { this.pageSize = pageSize;}

   public String getSthId() { return sthId;}

   public void setSthId(String sthId) { this.sthId = sthId;}

   public List<OrderVosBean> getOrderVos() { return orderVos;}

   public void setOrderVos(List<OrderVosBean> orderVos) { this.orderVos = orderVos;}

   public static class OrderVosBean {

	/**
	 * orderId : ff8081816e9708f8016ea00b8f260076
	 * createTime : 2019-11-25 00:52:22
	 * orderNo : RK-1911250101
	 * inventoryNum : 8
	 * inStockNum : 6
	 * currentNum : 0
	 */

	private String orderId;
	private String createTime;
	private String orderNo;
	private int    inventoryNum;
	private int    inStockNum;
	private int    currentNum;
	private boolean    isSelected;

	public boolean isSelected() {
	   return isSelected;
	}

	public void setSelected(boolean selected) {
	   isSelected = selected;
	}

	public String getOrderId() { return orderId;}

	public void setOrderId(String orderId) { this.orderId = orderId;}

	public String getCreateTime() { return createTime;}

	public void setCreateTime(String createTime) { this.createTime = createTime;}

	public String getOrderNo() { return orderNo;}

	public void setOrderNo(String orderNo) { this.orderNo = orderNo;}

	public int getInventoryNum() { return inventoryNum;}

	public void setInventoryNum(int inventoryNum) { this.inventoryNum = inventoryNum;}

	public int getInStockNum() { return inStockNum;}

	public void setInStockNum(int inStockNum) { this.inStockNum = inStockNum;}

	public int getCurrentNum() { return currentNum;}

	public void setCurrentNum(int currentNum) { this.currentNum = currentNum;}

   }
}
