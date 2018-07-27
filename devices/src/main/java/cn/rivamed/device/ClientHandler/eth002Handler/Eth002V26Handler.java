package cn.rivamed.device.ClientHandler.eth002Handler;

import cn.rivamed.FunctionCode;
import cn.rivamed.device.ClientHandler.NettyDeviceClientHandler;
import cn.rivamed.device.DeviceType;
import io.netty.util.internal.StringUtil;

/***
 * EHT002模块操作接口
 * V2.6 系列针对2.0类型的耗材柜，其操作逻辑与V2 不同，详情可参阅设备接口文档
 *
 *
 */
public class Eth002V26Handler extends NettyDeviceClientHandler implements Eth002ClientHandler {


    public Eth002V26Handler() {
    }

    @Override
    public DeviceType getDeviceType() {
        return DeviceType.Eth002;
    }

    @Override
    public int Close() {
        return 0;
    }

    Eth002Message eth002Message;

    @Override
    public String getRemoteIP() {
        String address = this.getCtx() == null ? "" : this.getCtx().pipeline().channel().remoteAddress().toString();
        if (StringUtil.isNullOrEmpty(address)) {
            address = address.replace("/", "");
            address = address.substring(0, address.indexOf(":"));
        }
        return address;
    }

    @Override
    public void RegisterMessageListener(Eth002Message messageListener) {
        this.eth002Message = messageListener;
        this.eth002Message.setDeviceHandler(this);
    }

    @Override
    public int OpenDoor() {
        return 0;
    }

    @Override
    public int FingerReg() {
        return 0;
    }

    @Override
    public int CheckLockState() {
        return 0;
    }

    @Override
    public int OpenLight() {
        return 0;
    }

    @Override
    public int CloseLight() {
        return FunctionCode.DEVICE_NOT_SUPPORT;
    }
}
