package com.uhf.api.cls;

/**
 * describe ：  芯联usb连接reader 动态连接库加载
 *
 * @author : Yich
 * date: 2019/4/18
 */
public class JniModuleAPI {
    static {
        System.loadLibrary("ModuleAPIJni");
    }

    public JniModuleAPI() {
    }

    public static native int InitReader(int[] var1, String var2, int var3);

    public static native int InitReader_Notype(int[] var1, String var2, int var3);

    public static native int GetHardwareDetails(int var1, Object var2);

    public static native void CloseReader(int var1);

    public static native int GetTagData(int var1, int var2, char var3, int var4, int var5, byte[] var6, byte[] var7, short var8);

    public static native int WriteTagData(int var1, int var2, char var3, int var4, byte[] var5, int var6, byte[] var7, short var8);

    public static native int WriteTagEpcEx(int var1, int var2, byte[] var3, int var4, byte[] var5, short var6);

    public static native int TagInventory_Raw(int var1, int[] var2, int var3, short var4, int[] var5);

    public static native int TagInventory_BaseType(int var1, int[] var2, int var3, short var4, byte[] var5, int[] var6);

    public static native int GetNextTag_BaseType(int var1, byte[] var2);

    public native int LockTag(int var1, int var2, byte var3, short var4, byte[] var5, short var6);

    public static native int KillTag(int var1, int var2, byte[] var3, short var4);

    public static native int Lock180006BTag(int var1, int var2, int var3, int var4, short var5);

    public static native int BlockPermaLock(int var1, int var2, int var3, int var4, int var5, byte[] var6, byte[] var7, short var8);

    public static native int BlockErase(int var1, int var2, int var3, int var4, int var5, byte[] var6, short var7);

    public static native int EraseDataOnReader(int var1);

    public static native int SaveDataOnReader(int var1, int var2, byte[] var3, int var4);

    public static native int ReadDataOnReader(int var1, int var2, byte[] var3, int var4);

    public static native int CustomCmd_BaseType(int var1, int var2, int var3, byte[] var4, byte[] var5);

    public static native int ParamGet(int var1, int var2, Object var3);

    public static native int ParamSet(int var1, int var2, Object var3);

    public static native int SetGPO(int var1, int var2, int var3);

    public static native int GetGPI(int var1, int var2, int[] var3);

    public static native int Get_GPIEx(int var1, GpiInfo_ST var2);

    public static native int PsamTransceiver(int var1, int var2, int var3, byte[] var4, int[] var5, byte[] var6, byte[] var7, short var8);

    public static native int AsyncStartReading(int var1, int[] var2, int var3, int var4);

    public static native int AsyncGetTagCount(int var1, int[] var2);

    public static native int AsyncGetNextTag(int var1, TAGINFO[] var2);

    public static native int AsyncStopReading(int var1);

    public static native int GetLastDetailError(int var1, ErrInfo var2);

    public static native int DataTransportSend(int var1, byte[] var2, int var3, int var4);

    public static native int DataTransportRecv(int var1, byte[] var2, int var3, int var4);

    public static native int FirmwareLoadFromSerialPort(String var0, String var1);

    public static native int RebootReader(String var0);

}
