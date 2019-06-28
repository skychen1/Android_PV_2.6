package high.rivamed.myapplication.utils;

import java.util.ArrayList;
import java.util.List;

import high.rivamed.myapplication.bean.ExceptionRecordBean;

/**
 * 项目名称:    Android_PV_2.6.6_514C
 * 创建者:      DanMing
 * 创建时间:    2019/6/26 14:06
 * 描述:       异常处理选中
 * 包名:        high.rivamed.myapplication.utils
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class ExceptionDateUtils  {
   /**
    * 是否有选择操作的耗材
    * @return
    */
   public static List<ExceptionRecordBean.RowsBean> getTrueDate(List<ExceptionRecordBean.RowsBean> rows ) {
	List<ExceptionRecordBean.RowsBean> rowList = new ArrayList<>();
	if (rows != null) {
	   for (int i = 0; i < rows.size(); i++) {
		if (rows.get(i).isSelected()) {
		   rowList.add(rows.get(i));
		}
	   }
	   return rowList;
	}
	return null;
   }
}
