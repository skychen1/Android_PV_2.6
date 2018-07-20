package high.rivamed.myapplication.devices;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import cn.rivamed.DeviceManager;
import cn.rivamed.callback.DeviceCallBack;
import cn.rivamed.device.DeviceType;
import cn.rivamed.device.Service.Eth002Service.Eth002ServiceType;
import cn.rivamed.device.Service.UhfService.UhfDeviceType;
import cn.rivamed.model.TagInfo;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.SimpleActivity;
import high.rivamed.myapplication.utils.ToastUtils;


public class TestDevicesActivity extends SimpleActivity {

    private TextView mTextMessage;
    private Button bt_startServer;
    private Button bt_startScan;
    private Button bt_stopScan;
    private Button bt_fingerReg;
    private Button bt_opendoor;

    String uhfDeviceId = "";
    String eth002DeviceId = "";

    @Override
    public int getLayoutId() {
        return R.layout.activity_test_devices_layout;
    }

    @Override
    public void initDataAndEvent(Bundle savedInstanceState) {
        mTextMessage = (TextView) findViewById(R.id.message);
        bt_startServer = (Button) findViewById(R.id.bt_startuhf);
        bt_startScan = (Button) findViewById(R.id.bt_startscan);
        bt_stopScan = (Button) findViewById(R.id.bt_stopscan);
        bt_fingerReg = (Button) findViewById(R.id.bt_fingerreg);
        bt_opendoor = (Button) findViewById(R.id.bt_opendoor);

        initListener();
        initCallBack();
        //  Toast.makeText(this ,new DeviceManager().getI(),Toast.LENGTH_LONG).show();

        mTextMessage.setText("程序已启动");


        List<DeviceManager.DeviceInfo> deviceInfos = DeviceManager.getInstance().QueryConnectedDevice();
        String s = "";
        for (DeviceManager.DeviceInfo d : deviceInfos
                ) {
            if (d.getDeviceType() == DeviceType.Eth002V2) {
                eth002DeviceId = d.getIdentifition();
            }
            s += d.getIdentifition() + "|||";

        }

    }

    private void initCallBack() {
        DeviceManager.getInstance().RegisterDeviceCallBack(new DeviceCallBack() {
            @Override
            public void OnDeviceConnected(DeviceType deviceType, String deviceIndentify) {
                if (deviceType == DeviceType.ColuUhfReader) {
                    uhfDeviceId = deviceIndentify;
                } else if (deviceType == DeviceType.Eth002V2) {
                    eth002DeviceId = deviceIndentify;
                }
                setmTextMessage("设备已连接：" + deviceType + ":::ID=" + deviceIndentify);
            }

            @Override
            public void OnDeviceDisConnected(DeviceType deviceType, String deviceIndentify) {
                setmTextMessage("设备已断开：" + deviceType + ":::ID=" + deviceIndentify);
            }

            @Override
            public void OnCheckState(DeviceType deviceType, String deviceId, Integer code) {
                setmTextMessage("检查门锁开关：" + deviceType + ":::ID=" + deviceId + ":::RET=" + code);
            }

            @Override
            public void OnIDCard(String deviceId, String idCard) {
                setmTextMessage("接收到刷卡信息：" + deviceId + ":::ID=" + idCard);
            }

            @Override
            public void OnFingerFea(String deviceId, String fingerFea) {
                setmTextMessage("接收到指纹采集信息：" + deviceId + ":::FingerData=" + fingerFea);
            }

            @Override
            public void OnFingerRegExcuted(String deviceId, boolean success) {
                setmTextMessage("指纹注册命令已执行：" + deviceId + ":::success=" + success);
            }

            @Override
            public void OnFingerRegisterRet(String deviceId, boolean success, String fingerData) {
                setmTextMessage("接收到指纹注册结果：" + deviceId + ":::success=" + success + ":::FingerData=" + fingerData);
            }

            @Override
            public void OnDoorOpened(String deviceIndentify, boolean success) {
                setmTextMessage("开门结果：" + deviceIndentify + ":::success=" + success);
            }

            @Override
            public void OnDoorClosed(String deviceIndentify, boolean success) {

                setmTextMessage("门锁已关闭：" + deviceIndentify + ":::success=" + success);

            }

            @Override
            public void OnDoorCheckedState(String deviceIndentify, boolean opened) {
                setmTextMessage("门锁状态检查：" + deviceIndentify + ":::opened=" + opened);
            }


            @Override
            public void OnUhfScanRet(boolean success, String deviceId, String userInfo, Map<String, List<TagInfo>> epcs) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("UHF Reader (" + deviceId + ")扫描完成\n");
                stringBuilder.append("EPC数量" + epcs.size() + "个:\n");
                for (Map.Entry<String, List<TagInfo>> v : epcs.entrySet()) {
                    stringBuilder.append(v.getKey() + ";\n");
                }
                setmTextMessage(stringBuilder.toString());
            }

            @Override
            public void OnUhfScanComplete(boolean success, String deviceId) {
                setmTextMessage("RFID扫描结束：" + deviceId + ":::success=" + success);
            }

            @Override
            public void OnUhfSetPowerRet(String deviceId, boolean success) {
                setmTextMessage("RFID设置功率结果：" + deviceId + ":::success=" + success);
            }

            @Override
            public void OnUhfQueryPowerRet(String deviceId, boolean success, int power) {
                setmTextMessage("RFID读取功率结果：" + deviceId + ":::success=" + success + ":::power=" + power);
            }
        });
        //  Toast.makeText(this ,new DeviceManager().getI(),Toast.LENGTH_LONG).show();


    }

    private void setmTextMessage(String msg) {
        TestDevicesActivity.this.mTextMessage.post(new Runnable() {
            @Override
            public void run() {
                TestDevicesActivity.this.mTextMessage.setText(msg);
            }
        });
    }

    private void initListener() {
        bt_startServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   boolean ret = DeviceManager.getInstance().StartUhfReaderService(UhfDeviceType.UHF_READER_COLU, 8010);
                //   ret = DeviceManager.getInstance().StartEth002Service(Eth002ServiceType.Eth002V2, 8012);
                //    Toast.makeText(TestDevicesActivity.this, "服务启动" + (ret ? "成功" : "失败"), Toast.LENGTH_LONG).show();
            }
        });

        bt_startScan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int ret = DeviceManager.getInstance().StartUhfScan(uhfDeviceId);
                Toast.makeText(TestDevicesActivity.this, "启动扫描：结果" + ret, Toast.LENGTH_LONG).show();
            }
        });

        bt_stopScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ret = DeviceManager.getInstance().StartUhfScan(uhfDeviceId);
                Toast.makeText(TestDevicesActivity.this, "停止扫描：结果" + ret, Toast.LENGTH_LONG).show();
            }
        });

        bt_opendoor.setOnClickListener(new View.OnClickListener() {

            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                int ret = DeviceManager.getInstance().OpenDoor(eth002DeviceId);
                Toast.makeText(TestDevicesActivity.this, "开门命令已发出" + ret, Toast.LENGTH_LONG).show();
            }
        });

        bt_fingerReg.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                int ret = DeviceManager.getInstance().FingerReg(eth002DeviceId);
                Toast.makeText(TestDevicesActivity.this, "指纹注册命令已发出" + ret, Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public void onBindViewBefore() {

    }

    @Override
    public Object newP() {
        return null;
    }


}
