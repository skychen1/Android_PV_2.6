package cn.rivamed.device.Service.Eth002Service;

import android.util.Log;

import java.net.InetSocketAddress;

import cn.rivamed.DeviceManager;
import cn.rivamed.device.ClientHandler.DeviceHandler;
import cn.rivamed.device.ClientHandler.eth002Handler.Eth002ClientHandler;
import cn.rivamed.device.ClientHandler.eth002Handler.Eth002Message;
import cn.rivamed.device.ClientHandler.eth002Handler.Eth002V26Handler;
import cn.rivamed.device.ClientHandler.eth002Handler.Eth002V2Handler;
import cn.rivamed.device.Service.BaseService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.internal.StringUtil;

public class Eth002Service extends BaseService {


    private int port = -1;
    private static final String LOG_TAG = "DEV_ETH002_S";
    private Eth002ServiceType serviceType;

    private EventLoopGroup group = null;
    private ServerBootstrap b = null;
    private boolean serverRunning = false;

    public Eth002Service(Eth002ServiceType serviceType, int port) {
        this.serviceType = serviceType;
        this.port = port;
    }

    private Eth002ServiceType getServiceType() {
        return serviceType;
    }

    @Override
    public boolean isAlive() {
        return serverRunning;
    }

    @Override
    public boolean StartService(DeviceManager deviceManager) {
        super.StartService(deviceManager);
        Thread t = new Thread(() -> {
            if (port <= 0) {
                Log.w(LOG_TAG, "Etv002设备端口设置为" + port + "跳过启动");
                return;
            }

            try {
                group = new NioEventLoopGroup();
                b = new ServerBootstrap();
                b.option(ChannelOption.TCP_NODELAY, true);
                b.childOption(ChannelOption.SO_KEEPALIVE, true);
                b.option(ChannelOption.SO_BACKLOG, 1024);
                b.group(group)
                        .channel(NioServerSocketChannel.class)
                        .localAddress(new InetSocketAddress(port))
                        .childHandler(new ChannelInitializer() {
                            @Override
                            protected void initChannel(Channel channel) {
                                channel.config().setOption(ChannelOption.SO_KEEPALIVE, true);
                                Eth002ClientHandler channelHandler = null;
                                switch (serviceType) {
                                    case Eth002V2:
                                        channelHandler = new Eth002V2Handler();
                                        break;
                                    case Eth002V26:
                                        channelHandler = new Eth002V26Handler();
                                        break;
                                    default:
                                        break;
                                }
                                if (channelHandler == null) {
                                    channel.close();
                                }
                                Log.i(LOG_TAG, "接收到新的链接请求" + channel.remoteAddress());
                                channelHandler.RegisterMessageListener(new Eth002ClientMessageListener());
                                channel.config().setOption(ChannelOption.SO_RCVBUF, 1024);
//                                channel.pipeline().addLast(new IdleStateHandler(5, 5, 0));
                                /**
                                 * 第一个参数为信息最大长度，超过这个长度回报异常，
                                 * 第二参数为长度属性的起始（偏移）位，
                                 * 第三个参数为“长度属性”的长度，
                                 * 第四个参数为长度调节值，在总长被定义为包含包头长度时，修正信息长度，不包含为0，包含 则为负数
                                 * 第五个参数为跳过的字节数，从长度属性结束的位置往前数，
                                 * */
                                channel.pipeline().addLast(new LengthFieldBasedFrameDecoder(10000, 1, 2, 0, 0));
                                channel.pipeline().addLast((ChannelHandler) channelHandler);
                            }
                        });
                ChannelFuture f = b.bind().sync();
                serverRunning = true;
                Log.i(LOG_TAG, "启动服务:port=" + port + ",服务类型为HfRfid_ETV001服务");
                f.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                Log.e(LOG_TAG, "启动服务Etv002失败", e);
            } finally {
                try {
                    group.shutdownGracefully().sync();
                } catch (InterruptedException e) {
                    Log.e(LOG_TAG, "服务Etv002结束失败", e);
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
    public boolean StopService() {
        if (serverRunning && group != null) {
            try {
                group.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                Log.e(LOG_TAG, "关闭Eth002服务失败");
                return false;
            }
        }
        return true;

    }

    private class Eth002ClientMessageListener implements Eth002Message {

        DeviceHandler clientHandler;

        @Override
        public DeviceHandler getDeviceHandler() {
            return this.clientHandler;
        }

        @Override
        public void setDeviceHandler(DeviceHandler handler) {
            this.clientHandler = handler;
        }

        @Override
        public void ReciveIDCard(String cardNo) {
            if (Eth002Service.this.getDeviceManager() != null) {
                if (Eth002Service.this.getDeviceManager().getDeviceCallBack() != null) {
                    Eth002Service.this.getDeviceManager().getDeviceCallBack().OnIDCard(this.clientHandler.getIdentification(), cardNo);
                }
            }
        }

        @Override
        public void DoorClosed(boolean success) {
            if (Eth002Service.this.getDeviceManager() != null) {
                if (Eth002Service.this.getDeviceManager().getDeviceCallBack() != null) {
                    Eth002Service.this.getDeviceManager().getDeviceCallBack().OnDoorClosed(clientHandler.getIdentification(), success);
                }
            }
        }

        @Override
        public void DoorOpenRet(boolean opened) {
            if (Eth002Service.this.getDeviceManager() != null) {
                if (Eth002Service.this.getDeviceManager().getDeviceCallBack() != null) {
                    Eth002Service.this.getDeviceManager().getDeviceCallBack().OnDoorOpened(clientHandler.getIdentification(), opened);

                }
            }
        }

        @Override
        public void DoorState(boolean opened) {
            if (Eth002Service.this.getDeviceManager() != null) {
                if (Eth002Service.this.getDeviceManager().getDeviceCallBack() != null) {
                    Eth002Service.this.getDeviceManager().getDeviceCallBack().OnDoorCheckedState(clientHandler.getIdentification(), opened);
                }
            }
        }

        @Override
        public void FingerRegisterRet(boolean regestedSuccess, String fingerData, String userid) {
            if (Eth002Service.this.getDeviceManager() != null) {
                if (Eth002Service.this.getDeviceManager().getDeviceCallBack() != null) {
                    Eth002Service.this.getDeviceManager().getDeviceCallBack().OnFingerRegisterRet(this.clientHandler.getIdentification(), regestedSuccess, fingerData);
                }
            }
        }

        @Override
        public void FingerGetImage(String fingerData) {
            if (StringUtil.isNullOrEmpty(fingerData)) return;
            if (Eth002Service.this.getDeviceManager() != null) {
                if (Eth002Service.this.getDeviceManager().getDeviceCallBack() != null) {
                    Eth002Service.this.getDeviceManager().getDeviceCallBack().OnFingerFea(this.clientHandler.getIdentification(), fingerData);
                }
            }
        }

        @Override
        public void FingerRegisgerCmdExcuted() {
            if (Eth002Service.this.getDeviceManager() != null) {
                if (Eth002Service.this.getDeviceManager().getDeviceCallBack() != null) {
                    Eth002Service.this.getDeviceManager().getDeviceCallBack().OnFingerRegExcuted(this.clientHandler.getIdentification(), true);
                }
            }
        }

        @Override
        public void FingerDeviceError(boolean disconnected) {

        }

        @Override
        public void OnDisconnected() {
            if (Eth002Service.this.getDeviceManager() != null) {
                Eth002Service.this.getDeviceManager().fireDeviceDisconnected(clientHandler.getIdentification());
            }
        }

        @Override
        public void OnConnected() {
            if (Eth002Service.this.getDeviceManager() != null) {
                Eth002Service.this.getDeviceManager().AppendConnectedDevice(clientHandler.getIdentification(), clientHandler);
                if (Eth002Service.this.getDeviceManager().getDeviceCallBack() != null) {
                    Eth002Service.this.getDeviceManager().getDeviceCallBack().OnDeviceConnected(clientHandler.getDeviceType(), clientHandler.getIdentification());
                }
            }
        }
    }
}
