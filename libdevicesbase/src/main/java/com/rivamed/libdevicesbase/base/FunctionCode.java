package com.rivamed.libdevicesbase.base;

/**
 * describe ：操作设备返回的标准码
 *
 * @author : Yich
 * data: 2019/2/27
 */
public class FunctionCode {
    private FunctionCode() {
    }

    /**
     * 操作成功
     */
    public static final int SUCCESS = 0;
    /**
     * 设备不存在（没有连接）
     */
    public static final int DEVICE_NOT_EXIST = 1;
    /**
     * 设备正忙，正在执行其他耗时的指令等
     */
    public static final int DEVICE_BUSY = 2;
    /**
     * 设备不支持该指令
     */
    public static final int DEVICE_NOT_SUPPORT = 3;

    /**
     * 参数错误
     */
    public static final Integer PARAM_ERROR = 4;

    /**
     * 连接设备失败
     */
    public static final int CONNECT_FAILED = 5;
    /**
     * 连接类型错误
     */
    public static final int CONNECT_TYPE_ERROR = 6;
    /**
     * 设备已存在，或者已经连接打开
     */
    public static final int DEVICE_ALREADY_CONNECTED = 7;


    /**
     * 因其他原因导致操作失败
     */
    public static final int OPERATION_FAIL = 100;


}
