package cn.rivamed.device;

/**
 * @Author 郝小鹏
 * @Description
 * @Date: Created in 2018-07-10 17:43
 * @Modyfied By :
 */
public enum DeviceType {
    /**
     * 科陆RfidReader
     */
    ColuUhfReader,
    /**
     * 罗丹贝尔阅读器
     */
    RodinBellReader,
    /**
     * 导引屏
     */
    Hmi,
    /***
     * ETH002 类型的设备，针对2.0版本耗材柜
     */
    Eth002V2,
    /***
     * EHT002v26 类型的设备，针对V2.6版本的耗材柜
     */
    Eth002V26

}
