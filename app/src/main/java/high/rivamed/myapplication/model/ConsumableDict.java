package high.rivamed.myapplication.model;

import org.litepal.crud.LitePalSupport;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/10 17:42
 * 描述:        需要存储的耗材的数据模型
 * 包名:        high.rivamed.myapplication.model
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class ConsumableDict extends LitePalSupport {

   private String cstName;//耗材名称
   private String cstSpec;//耗材规格
   private String epc;//EPC
   private String expirationTime;//效期
   private String operator;//操作人


}
