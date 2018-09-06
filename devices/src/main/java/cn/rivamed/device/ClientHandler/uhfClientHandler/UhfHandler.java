package cn.rivamed.device.ClientHandler.uhfClientHandler;

import java.util.List;

public interface UhfHandler {
    public int StartScan();

    /**
     *  开始扫描，并持续扫描一定时间后停止；
     *
     *  @param timeout
     * */
    public int StartScan(int timeout);

    public int StopScan();

    public int SetPower(byte power);

    public int QueryPower();

    public List<Integer> getUhfAnts();

    public int Reset();

    public int Close();
}
