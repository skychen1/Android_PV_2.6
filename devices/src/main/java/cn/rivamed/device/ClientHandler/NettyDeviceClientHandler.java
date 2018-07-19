package cn.rivamed.device.ClientHandler;

import cn.rivamed.FunctionCode;
import cn.rivamed.device.DeviceType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public abstract class NettyDeviceClientHandler extends ChannelInboundHandlerAdapter implements DeviceHandler {


    String identification;

    ChannelHandlerContext ctx;

    public String getIdentification() {
        return this.identification;
    }

    protected void setIdentification(String value) {
        this.identification = value;
    }


    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    protected void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public int Close() {
        ctx.close();
        return FunctionCode.SUCCESS;
    }
}
