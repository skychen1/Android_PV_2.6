package cn.rivamed.device.ClientHandler.uhfClientHandler;

import java.util.List;

public interface UhfHandler {
     int StartScan();

    /**
     *  开始扫描，并持续扫描一定时间后停止；
     *
     *  @param timeout
     * */
     int StartScan(int timeout);

     int StopScan();

     int SetPower(byte power);

     int QueryPower();

     List<Integer> getUhfAnts();

     int Reset();

     int Close();
}
