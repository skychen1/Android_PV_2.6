package high.rivamed.myapplication.cont;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/22 14:22
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.cont
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class Constants {
   public static final long COUNTDOWN_TIME = 40000;     //默认无操作自动退出
    public static final long DEFAULT_MILLISECONDS = 8000;      //默认的超时时间
    public static final String TYPE_TIMELY               = "timely";
    public static final String TYPE_RUNWATE              = "runwate";
    public static final String STYPE_STOCK_LEFT          = "stock_left";
    public static final String STYPE_STOCK_MIDDLE        = "stock_middle";
    public static final String STYPE_STOCK_RIGHT         = "stock_right";
    public static final String STYPE_DIALOG              = "dialog";//dialog样式的recyclerview
    public static final String STYPE_DIALOG2              = "dialog2";//dialog样式的recyclerview
    public static final String STYPE_IN                  = "in";//快速开柜拿入
    public static final String STYPE_OUT                 = "out";//快速开柜拿出
    public static final String STYPE_BING                = "bing";//快速开柜拿出绑定患者的样式
    public static final String STYPE_FORM                = "form";//请领单界面
    public static final String STYPE_FORM_CONF           = "form_ly";//请领单item详情界面
    public static final String STYPE_MEAL_NOBING         = "meal_nobing";//套餐未绑定
    public static final String STYPE_TIMELY_FOUR_DETAILS = "timely_details";//实时盘点item详情
    public static final String STYPE_MEAL_BING           = "meal_bing";//套餐绑定
    public static final int    ACTIVITY                  = 1;
    public static final int    FRAGMENT                  = 2;
    public static final String ACT_TYPE_CONFIRM_HAOCAI = "act_type_confirm_haocai";//确认耗材领用界面

    public static final int ACT_TYPE_TIMELY_LOSS         = 0;
    public static final int ACT_TYPE_TIMELY_PROFIT       = 1;
    public static final int ACT_TYPE_STOCK_FOUR_DETAILS  = 2;
    public static final int ACT_TYPE_HCCZ_IN             = 3;
    public static final int ACT_TYPE_HCCZ_OUT            = 4;
    public static final int ACT_TYPE_HCCZ_BING           = 5;
    public static final int ACT_TYPE_FORM_CONFIRM        = 6;
    public static final int ACT_TYPE_TIMELY_FOUR_DETAILS = 7;//实时盘点item详情
    public static final int ACT_TYPE_MEAL_BING           = 8;//套餐绑定患者
    public static final int ACT_TYPE_TEMPORARY_BING = 9;//创建临时患者
    public static final int ACT_TYPE_CONFIRM_RECEIVE = 10;//确认领用耗材
    public static final int ACT_TYPE_PATIENT_CONN = 11;//选择临时患者

    public static final int TYPE_STOCK_LEFT   = 0;
    public static final int TYPE_STOCK_MIDDLE = 1;
    public static final int TYPE_STOCK_RIGHT  = 2;

   public static final int DELETE_TATUS1  = 1;//移除

    //配置项的code
    public static final String CONFIG_005 = "005";//是否启用患者来源于门诊
    public static final String CONFIG_006 = "006";//是否启用患者来源于手术室
    public static final String CONFIG_007 = "007";//是否启用绑定患者领用耗材
    public static final String CONFIG_008 = "008";//是否允许耗材同入同出
    public static final String CONFIG_009 = "009";//是否先开柜门后绑定患者
    public static final String CONFIG_010 = "010";//是否先绑定患者后开柜门
    public static final String CONFIG_011 = "011";//是否允许快速开柜
    public static final String CONFIG_012 = "012";//是否启用绑定临时患者领用耗材
    public static final String CONFIG_013 = "013";//设备禁用
    public static final String CONFIG_014 = "014";//是否启用套餐领用
    public static final String CONFIG_015 = "015";//是否启用请领单领用
    public static final String CONFIG_016 = "016";//是否启用功能开柜
    public static final String CONFIG_017 = "017";//是否启用紧急登录


   //sp存入的key
   public static final String SAVE_SEVER_IP           = "SAVE_SEVER_IPxxx";//存入服务器IP加端口
   public static final String SAVE_SEVER_IP_TEXT      = "SAVE_SEVER_IP_TEXT";//存入服务器IP
   public static final String SAVE_SEVER_CODE         = "SAVE_SEVER_CODE";//存入服务器端口
   public static final String SAVE_CONFIG_STRING      = "SAVE_CONFIG_STRING";//配置项存入
   public static final String READER_TYPE             = "2";//reader的type
   public static final String UHF_TYPE             = "1";//串口服务器的type
   public static final String SN_NUMBER               = "sn_number";
   public static final String THING_CODE              = "thing_code";
   public static final String SAVE_REGISTE_DATE       = "registe_date";//预注册保存到本地的数据
   public static final String SAVE_ONE_REGISTE        = "oneRegiste";//第一次预注册的状态存入
   public static final String SAVE_ACTIVATION_REGISTE = "activationRegiste";//激活的状态存入
   public static final String SAVE_BRANCH_CODE        = "branchCode";//院区存入code
   public static final String SAVE_DEPT_CODE          = "deptId";//科室存入code
   public static final String SAVE_DEPT_NAME          = "deptName";//科室存入name
   public static final String SAVE_STOREHOUSE_CODE    = "storehouse_Code";//库房code
   public static final String KEY_ACCOUNT_DATA        = "key_account_data";//用户资料
   public static final String KEY_USER_NAME           = "key_user_name";//用户名字
   public static final String KEY_USER_ICON           = "key_user_icon";//用户头像
   public static final String KEY_USER_SEX           = "key_user_sex";//用户性别
   public static final String KEY_ACCOUNT_ID          = "key_account_id";//用户ID


}
