package high.rivamed.myapplication.activity;

import high.rivamed.myapplication.base.BaseTimelyActivity;

import static high.rivamed.myapplication.cont.Constants.ACT_TYPE_TIMELY_PROFIT;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/20 14:39
 * 描述:        实时盘点 盘盈
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class TimelyProfitActivity extends BaseTimelyActivity{

   @Override
   public int getCompanyType() {
	super.my_id = ACT_TYPE_TIMELY_PROFIT;
	return my_id;
   }
}
