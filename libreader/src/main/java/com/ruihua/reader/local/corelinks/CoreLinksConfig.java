package com.ruihua.reader.local.corelinks;

/**
 * describe ：芯联设备配置
 *
 * @author : Yich
 * date: 2019/4/18
 */
public class CoreLinksConfig {
    private CoreLinksConfig() {
    }

    /**
     * 连接设备的端口
     */
    public static final String READER_COM_STR = "/dev/ttyUSB0";

    /**
     * 几根天线的设备
     */
    public static final int PORT_NUM = 8;


}
