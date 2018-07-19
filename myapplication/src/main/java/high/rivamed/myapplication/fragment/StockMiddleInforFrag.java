package high.rivamed.myapplication.fragment;

import high.rivamed.myapplication.base.BaseStockFrag;

import static high.rivamed.myapplication.cont.Constants.TYPE_STOCK_MIDDLE;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/14 19:04
 * 描述:        库存详情
 * 包名:        high.rivamed.myapplication.fragment
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class StockMiddleInforFrag extends BaseStockFrag {
   public int mStockNumber;//列表的列数
   public int mStockType;//列表的类型


   @Override
   public int getStockType() {
	super.mStockType =TYPE_STOCK_MIDDLE;
	return mStockType;
   }
   @Override
   public int getStockNumber() {
	super.mStockNumber=5;//表格的列数
	return mStockNumber;
   }

}
