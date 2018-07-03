package high.rivamed.myapplication;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      LiangDanMing
 * 创建时间:    2018/7/1 23:02
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class MyValueFormatter implements IAxisValueFormatter {
   private DecimalFormat mFormat;

   public MyValueFormatter() {
	mFormat = new DecimalFormat("###");
   }


   @Override
   public String getFormattedValue(float value, AxisBase axis) {
	return mFormat.format(value);
   }

}
