package high.rivamed.myapplication.http;

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

    //版本检测和下载
    String URL_GET_VER = "/rmApi/app/app/listPage";//获取版本信息
    String URL_UPDATE = "/rmApi/app/app/downLoadApk";//下载

    //工程模式
    String URL_TEST_SNQUERY = "/rmApi/device/tBaseThing/findEquipmentInfo";//SN码查询
    String URL_TEST_REGISTE = "/rmApi/device/tBaseThing/save";//预注册
    String URL_TEST_ACTIVE = "/rmApi/device/tBaseThing/active";//设备激活
    String URL_TEST_FINDDEVICE = "/rmApi/dict/tBaseDeviceDict/findDevice";//根据部件类型查名字
    String URL_TEST_FIND_HOSPHOME = "/rmApi/dept/tBaseHospital/findByHospName";//输入医院查询医院信息
    String URL_TEST_FIND_BRANCH = "/rmApi/dept/tBaseDept/findBranch";//根据医院id查询院区信息
    String URL_TEST_FIND_DEPT = "/rmApi/dept/tBaseDept/findDept";//根据院区编码查询科室信息
    String URL_TEST_FIND_BYDEPT = "/rmApi/store/tCstBaseStorehouse/findBydept";//根据科室查询库房情况
    String URL_TEST_FIND_OPERROOMS = "/rmApi/dept/tBaseOperationRoom/findOperRoomsByDept";//根据科室查询手术室信息

    String URL_HOME_BOXSIZE = "/rmApi/device/tBaseDevice/getCabinetCount";//获取柜子信息

    //耗材流水
    String URL_HOME_RUNWATE = "/rmApi/consumables/tCstInventoryJournal/findTCstInventoryJournal";//查询耗材流水


    //库存状态
    String URL_STOCKSTATUS_TOP = "/rmApi/consumables/tCstInventory/expireStatistics";//库存监控 耗材效期监控
    String URL_STOCKSTATUS_DETAILS = "/rmApi/consumables/tCstInventory/inventoryStatusAndDetails";//库存详情和耗材库存预警
    String URL_STOCKUNCON_RIGHT = "/rmApi/consumables/tCstInventory/findNoConfirm";//未确认耗材
    String URL_STOCK_DETAIL = "/rmApi/consumables/tCstInventory/detail";//查询单个耗材

    //耗材操作

    String URL_OPERATE_QUERY = "/rmApi/consumables/tCstInventory/findTCstInventory";//查询扫描出来的耗材和数据库耗材情况
    String URL_OPERATE_INOUTBOX_YES = "/rmApi/consumables/tCstInventory/operateTCstInventory";//查询后入柜出柜的确认操作
    String URL_OPERATE_YC_YES = "/rmApi/store/tCstBaseStorehouse/findBydept";//根据科室查询库房情况Dialog 移出查
    String URL_OPERATE_DB_YES = "/rmApi/store/tCstBaseStorehouse/findAllStorehouse";//查询非本科室的库房Dialog    调拨查


    //快速开柜
    String URL_QUERY_ALL_IN = "/rmApi/consumables/tCstInventory/inCupboardList";//同入同出入柜列表
    String URL_QUERY_ALL_OUT = "/rmApi/consumables/tCstInventory/outCupboardList";//同入同出出柜列表
    String URL_QUERY_ALL_YES = "/rmApi/consumables/tCstInventory/fastOperateTCstInventories";//快速开柜确认

    //用户
    String URL_USER_LOGIN = "/rmApi/restLogin/validateLoginPassword";//登录
    String URL_USER_GET_INFO = "/rmApi/userManage/account/findAppAccountInfo";//获取用户信息
    String URL_USER_REGISTER_FINGER = "/rmApi/userManage/userFeature/registerFinger";//绑定指纹
    String URL_USER_VALIDATELOGIN_FINGER = "/rmApi/login/accountLogin/validateLoginFinger";//指纹登录
    String URL_USER_RESET_PASSWORD = "/rmApi/userManage/account/resetPassword";//重置密码
    String URL_USER_VALIDATELOGINWRIST = "/rmApi/restLogin/validateLoginWrist";//腕带登录
    String URL_USER_REGISTERWAIDAI = "/rmApi/userManage/userFeature/registerWaidai";//腕带绑定
    String URL_USER_UNREGISTERWAIDAI = "/rmApi/userManage/userFeature/untieWaidai";//腕带解绑
    String URL_USER_EMERGENCY_PWD = "/rmApi/userManage/account/bindEmergencyPwd";//紧急登录密码修改

    //   实时盘点
    String URL_TIMELY_ONE = "/rmApi/consumables/tCstInventory/findStocktaking";//库存盘点
    String URL_TIMELY_PROFIT = "/rmApi/consumables/tCstInventory/inventoryProfit";//库存盘盈情况
    String URL_TIMELY_LOSSES = "/rmApi/consumables/tCstInventory/inventoryLosses";//库存盘亏情况
    String URL_TIMELY_DETAIL = "/rmApi/consumables/tCstInventory/inventoryCompareDetail";//耗材对比详情
    String URL_PUT_LOSSES = "/rmApi/consumables/cstStockJournal/saveInventoryLosses";//盘亏进行操作
    String URL_LOSSES_CAUSE = "/rmApi/consumables/cstStockJournal/getLossesList";//盘亏原因

    //患者绑定
    String URL_PATIENTS_FIND = "/rmApi/operation/tTransOperationSchedule/findAllOperationSchedulePatients";//查询本科室下24小时的手术预约患者（包含临时患者）
//    String URL_PATIENTS_FIND_NO_TEMP = "/rmApi/operation/tTransOperationSchedule/findNearOperationSchedulePatients";//查询本科室下24小时的手术预约患者（不包含临时患者）
    String URL_BIND_PATIENT = "/rmApi/consumables/tCstInventory/tCstInventoryBingdingPatient";//患者绑定   后绑定患者用
    String URL_SAVE_TEMP_PATIENT = "/rmApi/operation/tTransOperationSchedule/saveTempPatient";//创建临时患者信息
    String URL_FIND_IN_PATIENT_PAGE = "/rmApi/patient/tTransInPatientInfo/findInPatientPage";//查询所有在院患者信息
    String URL_FIND_TEMP_PATIENTS = "/rmApi/operation/tTransOperationSchedule/findTempPatients";//查询所有的未绑定临时患者
    String URL_TEMP_PATIENT_CONN_PATIENT = "/rmApi/consumables/tCstInventory/tempPatientLinkInPatient";//临时患者与在院患者进行关联

    //查询配置项
    String URL_THING_CONFIG_FIND = "/rmApi/config/tCstConfigThing/findThingConfig";//查询所有的配置项
    String URL_AUTHORITY_MENU = "/rmApi/userManage/func/getFuncsTreeByAccountIdForPad";//根据账户权限显示菜单

    //使用记录
    String URL_FIND_PATIENT = "/rmApi/consumables/tCstInventoryJournal/findPatientUseRecordForPad";//患者的列表
    String URL_FIND_EPC_DETAILS = "/rmApi/consumables/tCstInventoryJournal/findCstDetailByPatientForPad";//使用耗材的详情

    //医嘱请领单领用
    String URL_RECEIVEORDER_LISTALL = "/rmApi/order/transReceiveOrder/listAll";//顶部医嘱单列表
    String URL_RECEIVEORDER_FINDBYORDERID = "/rmApi/order/transReceiveOrderDetail/findByOrderId";//根据医嘱单ID查询单柜耗材的库存
    String URL_RECEIVEORDER_CONTAINORDERCST = "/rmApi/order/transReceiveOrderDetail/containOrderCst";//根据EPC获取耗材
    String URL_RECEIVEORDER_TWOOUTBYRECEIVEORDER = "/rmApi/consumables/tCstInventory/twoOutByReceiveOrder";//确认领用耗材
    String URL_RECEIVEORDER_FINDDETAILBYORDERID = "/rmApi/order/transReceiveOrderDetail/findOrderAndOrderDetailByOrderId";//根据医嘱单ID查询顶部医嘱单和单柜耗材的库存数据

    //套组领用
    String URL_CSTPLAN_LISTFORPAD = "/rmApi/consumables/cstPlan/cstPlanListForPad";//套组列表-（本科室的套组列表）
    String URL_CSTPLAN_FINDCSTLIST = "/rmApi/consumables/cstPlanDetail/findCstList";//查询单个套组的耗材详情
    String URL_CSTPLAN_FINDCSTANDCOMPARETOCSTPLAN = "/rmApi/consumables/cstPlanDetail/findCstAndCompareToCstPlan";//根据EPC查询耗材
    String URL_CSTPLAN_OPERATETCSTINVENTORY= "/rmApi/consumables/tCstInventory/receiveCstByCstPlan";//套组领用
    String URL_CSTPLAN_SAVERECEIVEORDERMSG= "/rmApi/message/message/saveReceiveOrderMsg";//产生医嘱单的消息

    //消息
    String URL_FIND_MESSAGE_BY_ACCOUNTID = "/rmApi/message/message/findMessageByAccountId";//查询待办任务列表
    String URL_DELETE_MESSAGE_BY_ID = "/rmApi/message/message/deleteMessageById";//删除待办任务

    //本地存储
    String URL_GET_ALLCST = "/rmApi/consumables/tCstInventory/getAllCst";//获取设备中所有的耗材
    String URL_GET_LIST_ACCOUNT = "/rmApi/login/accountLogin/listAccount";//获取离线账户信息
    String URL_GET_FIND_OPERATIONROOM = "/rmApi/dept/tBaseOperationRoom/findByAccountId";//获取离线手术间
}
