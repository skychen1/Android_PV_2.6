package cn.rivamed.device.ClientHandler.eth002Handler;

public interface Eth002ClientHandler {

    int OpenDoor();

    int FingerReg();

    int CheckLockState();

    int OpenLight();

    int CloseLight();

    int Close();


    public void RegisterMessageListener(Eth002Message messageListener);

}
