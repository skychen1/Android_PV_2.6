package high.rivamed.myapplication.devices;

import android.content.Context;
import android.fingeralg.FingerAlg;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.rivamed.DeviceManager;
import cn.rivamed.callback.DeviceCallBack;
import cn.rivamed.device.DeviceType;
import cn.rivamed.model.TagInfo;
import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.SimpleActivity;
import high.rivamed.myapplication.utils.DevicesUtils;
import high.rivamed.myapplication.utils.LogUtils;
import high.rivamed.myapplication.utils.StringUtils;

import static cn.rivamed.DeviceManager.getInstance;
import static high.rivamed.myapplication.base.App.READER_TIME;

public class TestDevicesActivity extends SimpleActivity {

    private TextView txt_log;
    private Button bt_startScan;
    private Button bt_fingerReg;
    private Button bt_opendoor;
    private Button bt_checkState;
    private Button bt_openLight;
    private Button bt_closelight;
    private Button bt_getpower;
    private Button bt_setpower;
    private Button bt_uhf_reset;
    private Button bt_clear;
    private Button bt_fingerCompare;
    private EditText txt_scantime;

    private EditText txt_power;
    private Button bt_queryConnDev;
    private Button bt_back;
    private ScrollView scroll_log;
    String uhfDeviceId = "";
    String eth002DeviceId = "";

    String fingerData;
    String fingerTemplate;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");


    @Override
    public int getLayoutId() {
        return R.layout.activity_test_devices_layout;
    }

    @Override
    public void initDataAndEvent(Bundle savedInstanceState) {
        txt_log = (TextView) findViewById(R.id.txt_log);
        bt_startScan = (Button) findViewById(R.id.bt_startscan);
        bt_fingerReg = (Button) findViewById(R.id.bt_fingerreg);
        bt_opendoor = (Button) findViewById(R.id.bt_opendoor);
        bt_checkState = (Button) findViewById(R.id.bt_checkState);
        bt_openLight = (Button) findViewById(R.id.bt_openLight);
        bt_closelight = (Button) findViewById(R.id.bt_closelight);
        bt_getpower = (Button) findViewById(R.id.bt_getpower);
        bt_setpower = (Button) findViewById(R.id.bt_setpower);
        txt_power = (EditText) findViewById(R.id.txt_power);
        bt_queryConnDev = (Button) findViewById(R.id.bt_queryConnDev);
        bt_uhf_reset = (Button) findViewById(R.id.bt_uhf_reset);
        bt_back = (Button) findViewById(R.id.bt_back);
        bt_clear = (Button) findViewById(R.id.bt_clear);
        scroll_log = (ScrollView) findViewById(R.id.scroll_log);
        bt_fingerCompare = (Button) findViewById(R.id.bt_fingerCompare);
        txt_scantime = (EditText) findViewById(R.id.txt_scantime);

        initListener();
        initCallBack();
        //  Toast.makeText(this ,new DeviceManager().getI(),Toast.LENGTH_LONG).show();

        try {
            WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(dm);
            AppendLog("获取有效屏幕分辨率:X=" + dm.widthPixels + ";Y=" + dm.heightPixels);
            int heightPixels = dm.heightPixels;
            int widthPixels = dm.widthPixels;
            float density = dm.density;
            float heightDP = heightPixels / density;
            float widthDP = widthPixels / density;
            float smallestWidthDP;
            if(widthDP < heightDP) {
                smallestWidthDP = widthDP;
            }else {
                smallestWidthDP = heightDP;
            }
            AppendLog("我是dp单位最小宽度:   " + smallestWidthDP+"    屏幕密度:  "+density);
        } catch (Throwable e) {

        }

        AppendLog("程序已启动");


        bt_queryConnDev.performClick();
    }

    private void initCallBack() {
        getInstance().RegisterDeviceCallBack(new DeviceCallBack() {
            @Override
            public void OnDeviceConnected(DeviceType deviceType, String deviceIndentify) {
                if (deviceType == DeviceType.UHFREADER) {

                    uhfDeviceId = deviceIndentify;
                } else if (deviceType == DeviceType.Eth002) {
                    eth002DeviceId = deviceIndentify;
                }
                LogUtils.i("FFS", "设备已连接：" + deviceType + ":::ID=" + deviceIndentify);
                AppendLog("设备已连接：" + deviceType + ":::ID=" + deviceIndentify);
                mReaderDeviceId = DevicesUtils.getReaderDeviceId();
                eth002DeviceIdList = DevicesUtils.getEthDeviceId();
            }

            @Override
            public void OnDeviceDisConnected(DeviceType deviceType, String deviceIndentify) {
                AppendLog("设备已断开：" + deviceType + ":::ID=" + deviceIndentify);
            }

            @Override
            public void OnCheckState(DeviceType deviceType, String deviceId, Integer code) {
                AppendLog("检查门锁开关：" + deviceType + ":::ID=" + deviceId + ":::RET=" + code);
            }

            @Override
            public void OnIDCard(String deviceId, String idCard) {
                AppendLog("接收到刷卡信息：" + deviceId + ":::ID=" + idCard);
            }

            @Override
            public void OnFingerFea(String deviceId, String fingerFea) {
                AppendLog("接收到指纹采集信息：" + deviceId + ":::FingerData=" + fingerFea);
                Log.e("fff", fingerFea);
                fingerData = fingerFea;
            }

            @Override
            public void OnFingerRegExcuted(String deviceId, boolean success) {
                AppendLog("指纹注册命令已执行：" + deviceId + ":::success=" + success);
            }

            @Override
            public void OnFingerRegisterRet(String deviceId, boolean success, String fingerData) {
                AppendLog("接收到指纹注册结果：" + deviceId + ":::success=" + success + ":::FingerData=" + fingerData);
                if (success) {
                    fingerTemplate = fingerData;
                }
            }

            @Override
            public void OnDoorOpened(String deviceIndentify, boolean success) {
                AppendLog("开门结果：" + deviceIndentify + ":::success=" + success);
            }

            @Override
            public void OnDoorClosed(String deviceIndentify, boolean success) {

                AppendLog("门锁已关闭：" + deviceIndentify + ":::success=" + success);

            }

            @Override
            public void OnDoorCheckedState(String deviceIndentify, boolean opened) {
                AppendLog("门锁状态检查：" + deviceIndentify + ":::opened=" + opened);
            }


            @Override
            public void OnUhfScanRet(boolean success, String deviceId, String userInfo, Map<String, List<TagInfo>> epcs) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("UHF Reader (" + deviceId + ")扫描完成\n");
                stringBuilder.append("\t EPC数量" + epcs.size() + "个:\n");
                for (Map.Entry<String, List<TagInfo>> v : epcs.entrySet()) {
                    stringBuilder.append(v.getKey() + ";");
                }
                AppendLog(stringBuilder.toString());
            }

            @Override
            public void OnUhfScanComplete(boolean success, String deviceId) {
                AppendLog("RFID扫描结束：" + deviceId + ":::success=" + success);
            }

            /**
             * 获取UHF Reader 的天线状态
             * <p>
             * 仅标识已使能的天线 （开启）
             * <p>
             * 无法获取 天线是否连接；
             * <p>
             * 对部分reader,连接即意味着使能
             * 但部分Reader，连接并不意味着使能，使能也并不意味着连接
             *
             * @param deviceId
             * @param success
             * @param ants
             */
            @Override
            public void OnGetAnts(String deviceId, boolean success, List<Integer> ants) {

            }

            @Override
            public void OnUhfSetPowerRet(String deviceId, boolean success) {
                AppendLog("RFID设置功率结果：" + deviceId + ":::success=" + success);
            }

            @Override
            public void OnUhfQueryPowerRet(String deviceId, boolean success, int power) {
                AppendLog("RFID读取功率结果：" + deviceId + ":::success=" + success + ":::power=" + power);
            }
        });
        //  Toast.makeText(this ,new DeviceManager().getI(),Toast.LENGTH_LONG).show();


    }

    private void AppendLog(String msg) {
        TestDevicesActivity.this.txt_log.post(new Runnable() {
            @Override
            public void run() {
                Date time = new Date();
                String s = ">>>>" + simpleDateFormat.format(time) + "  " + msg + "\n";
                TestDevicesActivity.this.txt_log.append(s);


                int offset = txt_log.getMeasuredHeight() - scroll_log.getMeasuredHeight();
                if (offset < 0) {
                    offset = 0;
                }
                scroll_log.scrollTo(0, offset);
                Log.i("fff", msg);
            }
        });
    }

    private void initListener() {
        bt_fingerCompare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FingerAlg fingerAlg = new FingerAlg();
                int score = fingerAlg.AlgMatch(fingerTemplate, fingerData, 3);
                AppendLog("指纹对比结果 Score=" + score);
            }
        });
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestDevicesActivity.this.finish();
            }
        });

        bt_startScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int scanTime = READER_TIME;
                try {
                    scanTime = Integer.parseInt(txt_scantime.getText().toString());
                } catch (Throwable e) {

                }
                for (int i = 0; i < mReaderDeviceId.size(); i++) {
                    String DeviceId = (String) mReaderDeviceId.get(i);
                    int ret = getInstance().StartUhfScan(DeviceId, scanTime);
                    AppendLog("启动持续扫描,设备ID=" + DeviceId + "，扫描时间为" + scanTime + "s ;RET=" + ret);
                }

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

                int ret = getInstance().FingerReg(eth002DeviceId);
                AppendLog("指纹注册命令已发送 RET=" + ret + ";请等待质问注册执行结果");
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
                for (int i = 0; i < eth002DeviceIdList.size(); i++) {
                    String DeviceId = (String) eth002DeviceIdList.get(i);
                    int ret = getInstance().OpenDoor(DeviceId);
                    AppendLog("开门命令已发出 ret=" + ret+"      DeviceId   "+DeviceId);
                }
            }
        });

//        bt_checkState.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                for (int i = 0; i < eth002DeviceIdList.size(); i++) {
//                    String DeviceId = (String) eth002DeviceIdList.get(i);
//                    int ret = getInstance().CheckDoorState(DeviceId);
//                    AppendLog("检查门锁指令已发出 ret=" + ret+"      DeviceId   "+DeviceId);
//                }
//
//            }
//        });

        bt_queryConnDev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<DeviceManager.DeviceInfo> deviceInfos = getInstance().QueryConnectedDevice();
                String s = "";
                for (DeviceManager.DeviceInfo d : deviceInfos
                        ) {
                    if (d.getDeviceType() == DeviceType.Eth002) {
                        eth002DeviceId = d.getIdentifition();
                    }
                    if (d.getDeviceType() == DeviceType.UHFREADER) {
                        uhfDeviceId = d.getIdentifition();
                    }
                    s += "\t  设备类型 \t" + d.getDeviceType() + ";\t\t设备ID \t" + d.getIdentifition() + "\n";
                }
                AppendLog(StringUtils.isEmpty(s) ? "目前暂无设备连接" : ("已连接设备如下：\n" + s));
            }
        });
        bt_checkState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < eth002DeviceIdList.size(); i++) {
                    String DeviceId = (String) eth002DeviceIdList.get(i);
                    int ret = getInstance().CheckDoorState(DeviceId);
                    AppendLog("检查门锁指令已发出 ret=" + ret+"      DeviceId   "+DeviceId);
                }

            }
        });

        bt_openLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ret = getInstance().OpenLight(eth002DeviceId);
                AppendLog("开灯指令发送 RET=" + ret);
            }
        });
        bt_closelight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ret = getInstance().CloseLight(eth002DeviceId);
                AppendLog("关灯指令发送 RET=" + ret);
            }
        });

        bt_getpower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < mReaderDeviceId.size(); i++) {
                    String DeviceId = (String) mReaderDeviceId.get(i);
                    int ret = getInstance().getUhfReaderPower(DeviceId);
                    AppendLog("获取RFID reader功率指令发送 RET=" + ret+"     DeviceId =  "+DeviceId);
                }


            }
        });

        bt_setpower.setOnClickListener(new View.OnClickListener() {

            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                try {
                    byte powerByte = Byte.parseByte(txt_power.getText().toString());
                    if (powerByte < 0 || powerByte > 30) {
                        throw new Exception();
                    }
                    for (int i = 0; i < mReaderDeviceId.size(); i++) {
                        String DeviceId = (String) mReaderDeviceId.get(i);
                        int ret = getInstance().setUhfReaderPower(DeviceId, powerByte);
                        AppendLog("设置RFID reader功率指令发送 设备Ｉ=" + DeviceId + ",功率=" + powerByte + " RET=" + ret);
                    }

                } catch (Exception ex) {
                    Toast.makeText(TestDevicesActivity.this, "请输入有效的功率数值,1-30", Toast.LENGTH_LONG).show();
                }
            }
        });
        bt_uhf_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < mReaderDeviceId.size(); i++) {
                    String DeviceId = (String) mReaderDeviceId.get(i);
                    int ret = getInstance().ResetUhfReader(DeviceId);
                    AppendLog("复位RFID reader功率指令发送 设备Ｉ=" + DeviceId + " RET=" + ret);
                }
            }
        });

        bt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestDevicesActivity.this.txt_log.setText("");
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
