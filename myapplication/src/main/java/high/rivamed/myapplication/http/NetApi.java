package high.rivamed.myapplication.http;

import static high.rivamed.myapplication.base.App.MAIN_URL;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/13 15:28
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.http
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public interface NetApi {

   //正式
   String RELEASED_URL = "http://cc.p5w.net/";
   //测试
   String BETA_URL     = "http://192.168.2.32:8015/cst";

   //工程模式
   String URL_TEST_SNQUERY  = MAIN_URL + "/rmApi/device/tBaseThing/findEquipmentInfo";//SN码查询

   //耗材流水
   String URL_HOME_RUNWATE  = MAIN_URL + "/rmApi/consumables/tCstInventoryJournal/findTCstInventoryJournal";//查询耗材流水
   String URL_HOME_BOXSIZE  = MAIN_URL + "/rmApi/device/tBaseDevice/getCabinetCount";//查询耗材流水

   //库存状态
   String URL_STOCKSTATUS_TOP  = MAIN_URL + "/rmApi/consumables/tCstInventory/expireStatistics";//库存监控 耗材效期监控
   String URL_STOCKSTATUS_DETAILS  = MAIN_URL + "/rmApi/consumables/tCstInventory/inventoryStatusAndDetails";//库存详情和耗材库存预警
   String URL_STOCKUNCON_RIGHT  = MAIN_URL + "/rmApi/consumables/tCstInventory/findNoConfirm";//未确认耗材
   String URL_STOCK_DETAIL  = MAIN_URL + "/rmApi/consumables/tCstInventory/detail";//查询单个耗材
}
