package cn.rivamed;

import io.netty.handler.codec.mqtt.MqttMessageBuilders;

/**
 * @Author 郝小鹏
 * @Description 公共方法的返回值参考
 * @Date: Created in 2018-07-11 19:56
 * @Modyfied By :
 */
public class FunctionCode {
    /**
     * 操作成功
     */
    public static final Integer SUCCESS = 0;
    /**
     * 设备不存在
     */
    public static final Integer DEVICE_NOT_EXIST = 1;
    /**
     * 设备正忙，比如 正处于注册状态、或扫描状态时，请求的扫描启动等
     */
    public static final Integer DEVICE_BUSY = 2;
    /**
     * 设备不支持该指令
     * */
    public static final Integer DEVICE_NOT_SUPPORT = 3;

    /**
     * 参数错误
     * */
    public  static  final Integer PARAM_ERROR=4;
    /**
     * 因其他原因导致操作失败
     */
    public static final Integer OPERATION_FAIL = 100;

}
