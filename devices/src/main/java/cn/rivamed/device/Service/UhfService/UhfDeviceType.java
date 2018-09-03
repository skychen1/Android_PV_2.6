package cn.rivamed.device.Service.UhfService;

public enum UhfDeviceType {
    /**
     * 采用SDK编写的鸿陆 RFID reader 接口服务
     * */
    UHF_READER_COLU,
    /**
     * 采用数据协议编写的鸿陆RFID READER 接口服务
     * */
    UHF_READER_COLU_NETTY,
    /**
     * 罗丹贝尔 RFID READER 接口服务
     * */
    UHF_READER_RODINBELL,
    /**
     * 芯联 RFID READER 接口服务
     * */
    UHF_READER_XINLIAN
}
