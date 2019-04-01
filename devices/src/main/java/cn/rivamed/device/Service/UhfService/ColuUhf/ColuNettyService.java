package cn.rivamed.device.Service.UhfService.ColuUhf;

import android.util.Log;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

import cn.rivamed.DeviceManager;
import cn.rivamed.Utils.RivamedLengthFieldBasedFrameDecoder;
import cn.rivamed.device.ClientHandler.DeviceHandler;
import cn.rivamed.device.ClientHandler.uhfClientHandler.ColuClient.ColuNettyClientHandle;
import cn.rivamed.device.ClientHandler.uhfClientHandler.UhfClientMessage;
import cn.rivamed.device.Service.BaseService;
import cn.rivamed.device.Service.UhfService.UhfService;
import cn.rivamed.model.TagInfo;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;



public class ColuNettyService extends BaseService implements UhfService {

    String log_tag = "DEV_COLU_NS";

    Integer port;


    EventLoopGroup group = null;
    ServerBootstrap b = null;
    boolean serverRunning = false;


    public ColuNettyService(int port) {
        this.port = port;
    }


    @Override
    public boolean StartService(DeviceManager deviceManager) {
        super.StartService(deviceManager);
        Thread t = new Thread(() -> {

            if (port <= 0) {
                Log.i(log_tag, "鸿陆（高值） 设备端口设置为" + port + "，跳过启动");
                return;
            }

            EventLoopGroup group = new NioEventLoopGroup();
            try {
                ServerBootstrap b = new ServerBootstrap();
                b.option(ChannelOption.TCP_NODELAY, true);
                b.childOption(ChannelOption.SO_KEEPALIVE, true);
                b.group(group)
                        .channel(NioServerSocketChannel.class)
                        .localAddress(new InetSocketAddress(port))
                        .childHandler(new ChannelInitializer() {
                            protected void initChannel(Channel channel) throws Exception {
                                channel.config().setOption(ChannelOption.SO_KEEPALIVE, true);
                                ColuNettyClientHandle channelHandler = new ColuNettyClientHandle();
                                Log.i(log_tag, "接收到新的链接请求" + channel.remoteAddress());
                                channelHandler.RegisterMessageListener(new ColuNettyMessageListener());
                              //  channel.pipeline().addLast(new IdleStateHandler(10, 0, 0));
                                /**
                                 *
                                 * maxFrameLength 为信息最大长度，超过这个长度回报异常，
                                 * lengthFieldOffset 为长度属性的起始（偏移）位，
                                 * lengthFieldLength 为“长度属性”的长度，
                                 * lengthAdjustment  为长度调节值，在总长被定义为包含包头长度时，修正信息长度，不包含为0，包含 则为负数
                                 * initialBytesToStrip 为跳过的字节数，从长度属性结束的位置往前数，
                                 *
                                 * */
//                                channel.pipeline().addLast(new RivamedLengthFieldBasedFrameDecoder(10000, 3, 2, 0, 0,2,false    ));
                                channel.pipeline().addLast(channelHandler);
                            }
                        });
                ChannelFuture f = b.bind().sync();
                Log.i(log_tag, "启动服务:port=" + port + ",服务类型为鸿陆阅读器监听服务");
                serverRunning = true;
                f.channel().closeFuture().sync();

            } catch (InterruptedException e) {
                Log.e(log_tag, "启动服务RodinBell失败", e);
            } finally {
                try {
                    group.shutdownGracefully().sync();
                } catch (InterruptedException e) {
                    Log.e(log_tag, "服务RodinBell结束失败", e);
                }
            }
        });
        t.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {

        }
        return isAlive();
    }

    @Override
    public boolean isAlive() {
        return serverRunning;
    }

    @Override
    public boolean StopService() {
        return false;
    }

    /**
     * 处理阅读器相关回调事件
     */
    class ColuNettyMessageListener implements UhfClientMessage {

        DeviceHandler deviceHandler;

        @Override
        public DeviceHandler getDeviceHandler() {
            return this.deviceHandler;
        }

        @Override
        public void setDeviceHandler(DeviceHandler handler) {
            this.deviceHandler = handler;
        }

        @Override
        public void OnDisconnected() {
            if (ColuNettyService.this.getDeviceManager() != null) {
                ColuNettyService.this.getDeviceManager().fireDeviceDisconnected(deviceHandler.getIdentification());
            }
        }

        @Override
        public void OnConnected() {
            if (ColuNettyService.this.getDeviceManager() != null) {
                ColuNettyService.this.getDeviceManager().AppendConnectedDevice(deviceHandler.getIdentification(), deviceHandler);
                if (ColuNettyService.this.getDeviceManager().getDeviceCallBack() != null) {
                    ColuNettyService.this.getDeviceManager().getDeviceCallBack().OnDeviceConnected(this.deviceHandler.getDeviceType(), this.deviceHandler.getIdentification());
                }
            }
        }

        @Override
        public void OnUhfScanRet(boolean success, String deviceId, String userInfo, Map<String, List<TagInfo>> epcs) {
            if (ColuNettyService.this.getDeviceManager() != null) {
                if (ColuNettyService.this.getDeviceManager().getDeviceCallBack() != null) {
                    ColuNettyService.this.getDeviceManager().getDeviceCallBack().OnUhfScanRet(success, this.deviceHandler.getIdentification(), null, epcs);
                }
            }
        }

        @Override
        public void OnUhfScanComplete(boolean success, String deviceId) {
            if (ColuNettyService.this.getDeviceManager() != null) {
                if (ColuNettyService.this.getDeviceManager().getDeviceCallBack() != null) {
                    ColuNettyService.this.getDeviceManager().getDeviceCallBack().OnUhfScanComplete(success, this.deviceHandler.getIdentification());
                }
            }
        }

        @Override
        public void OnUhfSetPowerRet(String deviceId, boolean success) {
            if (ColuNettyService.this.getDeviceManager() != null) {
                if (ColuNettyService.this.getDeviceManager().getDeviceCallBack() != null) {
                    ColuNettyService.this.getDeviceManager().getDeviceCallBack().OnUhfSetPowerRet(deviceId, success);
                }
            }
        }

        @Override
        public void OnUhfQueryPowerRet(String deviceId, boolean success, int power) {
            if (ColuNettyService.this.getDeviceManager() != null) {
                if (ColuNettyService.this.getDeviceManager().getDeviceCallBack() != null) {
                    ColuNettyService.this.getDeviceManager().getDeviceCallBack().OnUhfQueryPowerRet(deviceId, success, power);
                }
            }
        }
    }
}