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

    //工程模式
    String URL_TEST_SNQUERY = MAIN_URL + "/rmApi/device/tBaseThing/findEquipmentInfo";//SN码查询
    String URL_TEST_REGISTE = MAIN_URL + "/rmApi/device/tBaseThing/save";//预注册
    String URL_TEST_ACTIVE = MAIN_URL + "/rmApi/device/tBaseThing/active";//设备激活
    String URL_TEST_FINDDEVICE = "/rmApi/dict/tBaseDeviceDict/findDevice";//根据部件类型查名字
    String URL_TEST_FIND_HOSPHOME = MAIN_URL + "/rmApi/dept/tBaseHospital/findByHospName";//输入医院查询医院信息
    String URL_TEST_FIND_BRANCH = MAIN_URL + "/rmApi/dept/tBaseDept/findBranch";//根据医院id查询院区信息
    String URL_TEST_FIND_DEPT = MAIN_URL + "/rmApi/dept/tBaseDept/findDept";//根据院区编码查询科室信息
    String URL_TEST_FIND_BYDEPT = MAIN_URL + "/rmApi/store/tCstBaseStorehouse/findBydept";//根据科室查询库房情况
    String URL_TEST_FIND_OPERROOMS = MAIN_URL + "/rmApi/dept/tBaseOperationRoom/findOperRoomsByDept";//根据科室查询手术室信息

    String URL_HOME_BOXSIZE = MAIN_URL + "/rmApi/device/tBaseDevice/getCabinetCount";//获取柜子信息

    //耗材流水
    String URL_HOME_RUNWATE = MAIN_URL + "/rmApi/consumables/tCstInventoryJournal/findTCstInventoryJournal";//查询耗材流水


    //库存状态
    String URL_STOCKSTATUS_TOP = MAIN_URL + "/rmApi/consumables/tCstInventory/expireStatistics";//库存监控 耗材效期监控
    String URL_STOCKSTATUS_DETAILS = MAIN_URL + "/rmApi/consumables/tCstInventory/inventoryStatusAndDetails";//库存详情和耗材库存预警
    String URL_STOCKUNCON_RIGHT = MAIN_URL + "/rmApi/consumables/tCstInventory/findNoConfirm";//未确认耗材
    String URL_STOCK_DETAIL = MAIN_URL + "/rmApi/consumables/tCstInventory/detail";//查询单个耗材

    //耗材操作

    String URL_OPERATE_QUERY = MAIN_URL + "/rmApi/consumables/tCstInventory/findTCstInventory";//查询扫描出来的耗材和数据库耗材情况
    String URL_OPERATE_INOUTBOX_YES = MAIN_URL + "/rmApi/consumables/tCstInventory/operateTCstInventory";//查询后入柜出柜的确认操作
    String URL_OPERATE_YC_YES = MAIN_URL + "/rmApi/store/tCstBaseStorehouse/findBydept";//根据科室查询库房情况Dialog 移出查
    String URL_OPERATE_DB_YES = MAIN_URL + "/rmApi/store/tCstBaseStorehouse/findAllStorehouse";//查询非本科室的库房Dialog    调拨查


    //用户
    String URL_USER_LOGIN = MAIN_URL + "/rmApi/restLogin/validateLoginPassword";//登录
    String URL_USER_GET_INFO = MAIN_URL + "/rmApi/userManage/account/findAppAccountInfo";//获取用户信息
    String URL_USER_REGISTER_FINGER = MAIN_URL + "/rmApi/userManage/userFeature/registerFinger";//绑定指纹
    String URL_USER_VALIDATELOGIN_FINGER = MAIN_URL + "/rmApi/login/accountLogin/validateLoginFinger";//指纹登录
    String URL_USER_RESET_PASSWORD = MAIN_URL + "/rmApi/userManage/account/resetPassword";//重置密码
    String URL_USER_VALIDATELOGINWRIST = MAIN_URL + "/rmApi/restLogin/validateLoginWrist";//腕带登录
    String URL_USER_REGISTERWAIDAI = MAIN_URL + "/rmApi/userManage/userFeature/registerWaidai";//腕带绑定

    //   实时盘点
    String URL_TIMELY_ONE = MAIN_URL + "/rmApi/consumables/tCstInventory/findStocktaking";//库存盘点
    String URL_TIMELY_PROFIT = MAIN_URL + "/rmApi/consumables/tCstInventory/inventoryProfit";//库存盘盈情况
    String URL_TIMELY_LOSSES = MAIN_URL + "/rmApi/consumables/tCstInventory/inventoryLosses";//库存盘亏情况
    String URL_TIMELY_DETAIL = MAIN_URL + "/rmApi/consumables/tCstInventory/inventoryCompareDetail";//耗材对比详情

    //患者绑定
    String URL_PATIENTS_FIND = MAIN_URL + "/rmApi/operation/tTransOperationSchedule/findAllOperationSchedulePatients";//查询本科室下24小时的手术预约患者（包含临时患者）
    String URL_PATIENTS_FIND_NO_TEMP = MAIN_URL + "/rmApi/operation/tTransOperationSchedule/findNearOperationSchedulePatients";//查询本科室下24小时的手术预约患者（不包含临时患者）
    String URL_BIND_PATIENT = MAIN_URL + "/rmApi/consumables/tCstInventory/tCstInventoryBingdingPatient";//患者绑定
    String URL_SAVE_TEMP_PATIENT = MAIN_URL + "/rmApi/operation/tTransOperationSchedule/saveTempPatient";//创建临时患者信息
    String URL_FIND_IN_PATIENT_PAGE = MAIN_URL + "/rmApi/patient/tTransInPatientInfo/findInPatientPage";//查询所有在院患者信息
    String URL_FIND_TEMP_PATIENTS = MAIN_URL + "/rmApi/operation/tTransOperationSchedule/findTempPatients";//查询所有的未绑定临时患者
    String URL_TEMP_PATIENT_CONN_PATIENT = MAIN_URL + "/rmApi/consumables/tCstInventory/tempPatientLinkInPatient";//临时患者与在院患者进行关联

    //查询配置项
    String URL_THING_CONFIG_FIND = MAIN_URL + "/rmApi/config/tCstConfigThing/findThingConfig";//查询所有的配置项


}
