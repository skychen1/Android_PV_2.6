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
    String URL_GET_VER = "/rmApi/userManage/systemVersion/getApkInfo";//获取版本信息    新
    String URL_UPDATE = "/rmApi/userManage/systemVersion/downloadApk";//下载    新
    //工程模式
    String URL_TEST_FINDDEVICE = "/dict/deviceDict/rmApi/findDevice";//根据部件类型查名字   新
    String URL_TEST_SNQUERY = "/basic/thing/findEquipmentInfo";//SN码查询   新
    String URL_TEST_REGISTE = "/basic/thing/save";//预注册   新
    String URL_TEST_ACTIVE = "/basic/thing/active";//设备激活    新
    String URL_TEST_FIND_BRANCH = "/basic/dept/findBranchs";//查询院区信息   新
    String URL_TEST_FIND_DEPT = "/basic/dept/findDepts";//根据院区编码查询科室信息   新
    String URL_TEST_FIND_BYDEPT = "/basic/storehouse/findStoreHouseByDeptId";//根据科室查询库房情况    新

    String URL_TEST_FIND_OPERROOMS = "/basic/operationRoom/findOperRoomsByDept";//根据科室查询手术室信息   新

    String URL_HOME_BOXSIZE = "/basic/device/rmApi/getCabinetCount";//获取柜子信息   新

    //用户
    String URL_USER_LOGIN = "/rmApi/restLogin/validateLoginPassword";//登录    新
    String URL_USER_UNNET_LOGIN = "/rmApi/restLogin/validateLoginPasswordOffline";//离线登录换Token    新
    String URL_REFRESH_TOKEN = "/rmApi/restLogin/refreshToken";//token刷新换取   新
    String URL_USER_REGISTER_FINGER = "/rmApi/userManage/userFeature/registerFinger";//绑定指纹    新
    String URL_USER_VALIDATELOGIN_FINGER = "/rmApi/restLogin/validateLoginFinger";//指纹登录    新
    String URL_USER_RESET_PASSWORD = "/rmApi/userManage/account/resetPassword";//重置密码    新
    String URL_USER_VALIDATELOGINWRIST = "/rmApi/restLogin/validateLoginWrist";//腕带登录    新
    String URL_USER_REGISTERWAIDAI = "/rmApi/userManage/userFeature/registerWaidai";//腕带绑定    新
    String URL_USER_UNREGISTERWAIDAI = "/rmApi/userManage/userFeature/untieWaidai";//腕带解绑    新
    String URL_USER_EMERGENCY_PWD = "/rmApi/userManage/account/bindEmergencyPwd";//紧急登录密码修改    新

    //耗材流水
    String URL_HOME_RUNWATE = "/consumables/inventoryJournal/findInventoryJournal";//查询耗材流水    新

    //库存状态
    String URL_STOCKSTATUS_TOP = "/consumables/inventory/rmApi/expireStatistics";//库存监控 耗材效期监控     新
    String URL_STOCKSTATUS_DETAILS = "/consumables/inventory/rmApi/inventoryStatusAndDetails";//库存详情和耗材库存预警    新
    String URL_STOCKUNCON_RIGHT = "/consumables/inventory/findNoConfirm";//未确认耗材
    String URL_STOCK_DETAIL = "/consumables/inventory/rmApi/detail";//查询单个耗材    新


    //耗材操作
    String URL_OPERATE_QUERY = "/consumables/inventory/findInventory";//查询扫描出来的耗材和数据库耗材情况    新
    String URL_OPERATE_QUERY_LYTH = "/consumables/inventory/findTwooutOrBackInventory";//查询扫描出来的耗材和数据库耗材情况    领用/退回新加接口查询epc信息
    String URL_OPERATE_INOUTBOX_YES = "/consumables/inventory/operateInventory";//查询后入柜出柜的确认操作     新
    String URL_OPERATE_INOUTBOX_YES_LYTH = "/consumables/inventory/operateTwoOutOrBackInventory";//查询后入柜出柜的确认操作     领用/退回新加接口确认操作
    String URL_OPERATE_DB_YES = "/rmApi/store/tCstBaseStorehouse/findAllStorehouse";//查询非本科室的库房Dialog    调拨查  暂停使用

    //快速开柜
    String URL_QUERY_ALL_YES = "/consumables/inventory/fastOperateTCstInventories";//快速开柜确认    新
    String URL_QUERY_OUTANDIN = "/consumables/inventory/outAndInCupboardList";//快速开柜数据查询    新

    //  实时盘点
    String URL_TIMELY_ONE = "/consumables/inventory/findStocktaking";//库存盘点     新
    String URL_TIMELY_PROFIT = "/consumables/inventory/inventoryProfit";//库存盘盈情况    新
    String URL_TIMELY_LOSSES = "/consumables/inventory/inventoryLosses";//库存盘亏情况    新
    String URL_TIMELY_DETAIL = "/consumables/inventory/inventoryCompareDetail";//耗材对比详情    新

    //患者绑定
    String URL_PATIENTS_FIND = "/consumables/surgeryInfo/findAllOperationSchedulePatients";//查询本科室下24小时的手术预约患者（包含临时患者）    新
    String URL_FIND_IN_PATIENT_PAGE = "/consumables/surgeryInfo/findInPatientPage";//查询所有在院患者信息    新
    String URL_FIND_TEMP_PATIENTS = "/patient/tempPatient/findTempPatients";//查询所有的未绑定临时患者     新
    String URL_TEMP_PATIENT_CONN_PATIENT = "/patient/patient/tempPatientLinkInPatient";//临时患者与在院患者进行关联    新

    //查询配置项
    String URL_THING_CONFIG_FIND = "/basic/thing/findConfig";//查询所有的配置项   新
    String URL_AUTHORITY_MENU = "/rmApi/userManage/func/getFuncsTreeByAccountIdForPad";//根据账户权限显示菜单   新

    //使用记录
    String URL_FIND_PATIENT = "/consumables/inventoryJournal/padFindPatientUseRecord";//患者的列表  新
    String URL_FIND_EPC_DETAILS = "/consumables/inventoryJournal/padFindCstDetailByPatient";//使用耗材的详情    新

    //医嘱请领单领用
    String URL_RECEIVEORDER_LISTALL = "/order/order/listAll";//顶部医嘱单列表    新
    String URL_RECEIVEORDER_FINDBYORDERID        = "/order/orderDetail/findByOrderId";//根据医嘱单ID查询单柜耗材的库存    新
    String URL_RECEIVEORDER_CONTAINORDERCST      = "/order/orderDetail/containOrderCst";//根据EPC获取耗材判断是否属于请领单的耗材   新
    String URL_RECEIVEORDER_TWOOUTBYRECEIVEORDER = "/order/orderDetail/twoOutByReceiveOrder";//确认领用耗材    新
    String URL_RECEIVEORDER_FINDDETAILBYORDERID  = "/order/orderDetail/findOrderAndDetail";//根据医嘱单ID查询顶部医嘱单和单柜耗材的库存数据
    String URL_RECEIVEORDER_SAVERECEIVEORDERMSG  = "/hvcMessage/saveReceiveOrderMsg";//产生医嘱单的消息    新

    //套组领用
    String URL_CSTPLAN_LISTFORPAD = "/consumables/suite/getSuites";//套组列表-（本科室的套组列表）   新
    String URL_CSTPLAN_FINDCSTLIST = "/consumables/suiteDetail/getCstList";//查询单个套组的耗材详情    新
    String URL_CSTPLAN_FINDCSTANDCOMPARETOCSTPLAN = "/consumables/suiteDetail/findCstAndCompareToSuite";//根据EPC查询耗材对比   新
    String URL_CSTPLAN_OPERATETCSTINVENTORY= "/consumables/inventory/receiveCstBySuite";//套组领用    新


    //消息
    String URL_FIND_MESSAGE_BY_ACCOUNTID = "/hvcMessage/findMessageByAccountId";//查询待办任务列表    新
    String URL_DELETE_MESSAGE_BY_ID = "/hvcMessage/deleteMessageById";//删除待办任务

    //本地存储
    String URL_UNENT_GET_ALLCST = "/consumables/inventory/rmApi/getAllCst";//获取设备中所有的耗材
    String URL_UNENT_GET_LIST_ACCOUNT = "/rmApi/userManage/account/listAccount";//获取离线账户信息
    String URL_UNENT_GET_FIND_OPERATIONROOM = "/basic/operationRoom/rmApi/findByThingId";//获取离线手术间
    String URL_UNENT_CST_OFFLINE = "/consumables/inventory/rmApi/forceOperateInventoryCst";//离线领用和强开

    String URL_ROBOT = "/clrApi/basic/clrRobot/callRobot";//召唤机器人

    //搜集日志
    String URL_OPEN = "/rmApi/openLog";//日志收集开启
    String URL_CLOSE = "/rmApi/closeLog";//日志收集关闭

    String URL_CONNECT = "/rmApi/userManage/account/connect";//测试连接提示


    //视频上传
    String URL_VIDEO_UPLOAD_RECORD="/video/video/videoUpload";
}
