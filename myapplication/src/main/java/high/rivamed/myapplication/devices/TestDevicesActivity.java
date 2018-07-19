package high.rivamed.myapplication.devices;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
   private Button   bt_startServer;
   private Button   bt_startScan;
   private Button   bt_stopScan;
   private Button   bt_fingerReg;
   private Button   bt_opendoor;

   String uhfDeviceId    = "";
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

		TestDevicesActivity.this.mTextMessage.setText(
			"设备已连接：" + deviceType + ":::ID=" + deviceIndentify);
	   }

	   @Override
	   public void OnDeviceDisConnected(DeviceType deviceType, String deviceIndentify) {
		TestDevicesActivity.this.mTextMessage.setText(
			"设备已断开：" + deviceType + ":::ID=" + deviceIndentify);
	   }

	   @Override
	   public void OnCheckState(DeviceType deviceType, String deviceId, Integer code) {
		TestDevicesActivity.this.mTextMessage.setText(
			"检查门锁开关：" + deviceType + ":::ID=" + deviceId + ":::RET=" + code);
	   }

	   @Override
	   public void OnIDCard(String deviceId, String idCard) {
		TestDevicesActivity.this.mTextMessage.setText(
			"接收到刷卡信息：" + deviceId + ":::ID=" + idCard);
	   }

	   @Override
	   public void OnFingerFea(String deviceId, String fingerFea) {
		TestDevicesActivity.this.mTextMessage.setText(
			"接收到指纹采集信息：" + deviceId + ":::FingerData=" + fingerFea);
	   }

	   @Override
	   public void OnFingerRegExcuted(String deviceId, boolean success) {
		TestDevicesActivity.this.mTextMessage.setText(
			"指纹注册命令已执行：" + deviceId + ":::success=" + success);
	   }

	   @Override
	   public void OnFingerRegisterRet(String deviceId, boolean success, String fingerData) {
		TestDevicesActivity.this.mTextMessage.setText(
			"接收到指纹注册结果：" + deviceId + ":::success=" + success + ":::FingerData=" +
			fingerData);
	   }

	   @Override
	   public void OnDoorOpened(String deviceIndentify, boolean success) {
		TestDevicesActivity.this.mTextMessage.setText(
			"开门结果：" + deviceIndentify + ":::success=" + success);
	   }

	   @Override
	   public void OnDoorClosed(String deviceIndentify, boolean success) {
		TestDevicesActivity.this.mTextMessage.setText(
			"门锁已关闭：" + deviceIndentify + ":::success=" + success);
	   }

	   @Override
	   public void OnDoorCheckedState(String deviceIndentify, boolean opened) {
		TestDevicesActivity.this.mTextMessage.setText(
			"门锁状态检查：" + deviceIndentify + ":::opened=" + opened);
	   }

	   @Override
	   public void OnUhfScanRet(
		   boolean success, String deviceId, String userInfo, Map<String, List<TagInfo>> epcs) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("UHF Reader (" + deviceId + ")扫描完成\n");
		stringBuilder.append("EPC数量" + epcs.size() + "个:\n");
		for (Map.Entry<String, List<TagInfo>> v : epcs.entrySet()) {
		   stringBuilder.append(v.getKey() + ";\n");
		}
		TestDevicesActivity.this.mTextMessage.setText(stringBuilder.toString());
	   }

	   @Override
	   public void OnUhfScanComplete(boolean success, String deviceId) {
		TestDevicesActivity.this.mTextMessage.setText(
			"RFID扫描结束：" + deviceId + ":::success=" + success);
	   }

	   @Override
	   public void OnUhfSetPowerRet(String deviceId, boolean success) {
		TestDevicesActivity.this.mTextMessage.setText(
			"RFID设置功率结果：" + deviceId + ":::success=" + success);
	   }

	   @Override
	   public void OnUhfQueryPowerRet(String deviceId, boolean success, int power) {
		TestDevicesActivity.this.mTextMessage.setText(
			"RFID读取功率结果：" + deviceId + ":::success=" + success + ":::power=" + power);
	   }
	});
	//  Toast.makeText(this ,new DeviceManager().getI(),Toast.LENGTH_LONG).show();

	mTextMessage.setText("程序已启动");
   }

   private void initListener() {
	bt_startServer.setOnClickListener(new View.OnClickListener() {
	   @Override
	   public void onClick(View view) {
		boolean ret = DeviceManager.getInstance()
			.StartUhfReaderService(UhfDeviceType.UHF_READER_COLU, 8010);
		ret = DeviceManager.getInstance().StartEth002Service(Eth002ServiceType.Eth002V2, 8012);
		ToastUtils.showShort("服务启动" + (ret ? "成功" : "失败"));
	   }
	});

	bt_startScan.setOnClickListener(new View.OnClickListener() {

	   @Override
	   public void onClick(View v) {
		int ret = DeviceManager.getInstance().StartUhfScan(uhfDeviceId);
		ToastUtils.showShort("启动扫描：结果" + ret);
	   }
	});

	bt_stopScan.setOnClickListener(new View.OnClickListener() {
	   @Override
	   public void onClick(View v) {
		int ret = DeviceManager.getInstance().StartUhfScan(uhfDeviceId);
		ToastUtils.showShort("停止扫描：结果" + ret);
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
		int ret = DeviceManager.getInstance().OpenDoor(uhfDeviceId);
		ToastUtils.showShort("开门命令已发出" + ret);
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
		ToastUtils.showShort("指纹注册命令已发出" + ret);
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
