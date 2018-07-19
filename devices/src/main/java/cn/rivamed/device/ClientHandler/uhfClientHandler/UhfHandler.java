package cn.rivamed.device.ClientHandler.uhfClientHandler;

public interface UhfHandler {
    public int StartScan();

    public int StopScan();

    public int SetPower(int power);

    public int QueryPower();

    public int Reset();

    public int Close();
}
