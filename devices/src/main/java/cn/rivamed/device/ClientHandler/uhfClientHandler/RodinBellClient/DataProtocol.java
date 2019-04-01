package cn.rivamed.device.ClientHandler.uhfClientHandler.RodinBellClient;

/**
 * describe ：  /**
 * 数据协议部分（仅包含部分数据协议）
 * 在进行开发时，仍然需要参照厂家的开发文档
 *
 * @author : Yich
 * data: 2019/2/20
 */
public class DataProtocol {

    /**
     * 485地址，默认设置位0x01
     */
    public static final byte ADDRESS = 0x01;
    /**
     * 协议指令集
     */
    public static final byte BEGIN_FLAG = (byte) 0xA0;
    public static final byte CMD_SETDEVICEID = (byte) 0x67;
    public static final byte CMD_GETDEVICEID = (byte) 0x68;
    public static final byte CMD_SET_OUTPUTPOWER = (byte) 0x76;
    public static final byte CMD_GET_OUTPUTPOWER = (byte) 0x77;
    public static final byte CMD_RESET = (byte) 0x70;
    public static final byte CMD_SET_WORK_ANT = 0X74;
    public static final byte CMD_GET_WORK_ANT = 0X75;

    /**
     * 发送实时盘点（0x8B）时的参数
     * RTINVENTORY_SESSION此命令是0X8B方式盘存的时候必须发送的指令
     * RTINVENTORY_TARGET此命令是0X8B方式盘存的时候必须发送的指令
     * RTINVENTORY_REPEAT 注：此命令为为0X01的时候，盘存最快，用的时间最短
     */
    public static final byte CMD_REALTIME_INVENTORY = (byte) 0x8B;
    public static final byte RTINVENTORY_SESSION = (byte) 0X01;
    public static final byte RTINVENTORY_TARGET = (byte) 0X00;
    public static final byte RTINVENTORY_REPEAT = (byte) 0X01;
    /**
     * 预制天线列表，总共是8根天线
     */
    public static final byte[] ANTS = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07};
    /**
     * 快速多天线盘点，适用于快速切换天线进行盘点
     */
    public static final byte CMD_FAST_SWITCH_ANT_INVENTORY = (byte) 0x8A;
    public static final byte CMD_SUCCESS = 0X10;

    private DataProtocol() {
    }

    public static byte[] pieceCommond(byte address, byte cmd, byte[] data) {
        /**
         *  head |  Len   |   Address |  cmd    |  data    | check
         *  --------------------------------------------------------
         *  1byte|  1byte |  1byte    |  1byte  |  N byte  |  1Byte
         *
         * */
        byte[] picecBuf = new byte[(data == null ? 0 : data.length) + 5];
        //前4 后1
        picecBuf[0] = BEGIN_FLAG;
        picecBuf[1] = (byte) ((picecBuf.length - 2) & 0xff);
        //len  从address开始计算直至结尾，不含len
        picecBuf[2] = (byte) (address & 0xff);
        picecBuf[3] = cmd;
        if (data != null) {
            System.arraycopy(data, 0, picecBuf, 4, data.length);
        }
        picecBuf[picecBuf.length - 1] = (byte) checkSum(picecBuf, 0, picecBuf.length - 1);
        return picecBuf;
    }

    public static int checkSum(byte[] buf, int offset, int len) {
        int i = 0, uSum = 0;
        for (i = 0; i < len; i++) {
            int v = buf[i + offset];
            uSum += 0xff & v;
        }
        uSum = (~(uSum & 0xff)) + 1;
        return (uSum) & 0xff;
    }
}

