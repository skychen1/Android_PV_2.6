package cn.rivamed.device.ClientHandler.uhfClientHandler;

import java.util.List;

public interface UhfHandler {
    public int StartScan();

    public int StopScan();

    public int SetPower(int power);

    public int QueryPower();

    public List<Integer> getUhfAnts();

    public int Reset();

    public int Close();
}
