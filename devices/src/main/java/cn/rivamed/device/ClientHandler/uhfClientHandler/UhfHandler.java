package cn.rivamed.device.ClientHandler.uhfClientHandler;

import java.util.List;

public interface UhfHandler {
    public int StartScan();

    public int StartScan(int repeat);

    public int StopScan();

    public int SetPower(byte power);

    public int QueryPower();

    public List<Integer> getUhfAnts();

    public int Reset();

    public int Close();
}