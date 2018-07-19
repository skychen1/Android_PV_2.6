package android.fingerdev;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.widget.Toast;


public class FingerUtil {

	private final int VID = 1107;
	private final int PID = 36869;
	private UsbManager usbManager;
	private UsbDeviceConnection conn;
	private UsbInterface usbInterface;
	private UsbEndpoint inEndpoint;
	private UsbEndpoint outEndpoint;
	
	private final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";

	public FingerUtil(Context context) {
		usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);

		IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
		context.registerReceiver(mUsbPermissionActionReceiver, filter);

		PendingIntent mPermissionIntent = PendingIntent.getBroadcast(context, 0,
				new Intent(ACTION_USB_PERMISSION), 0);

		for (final UsbDevice usbDevice : usbManager.getDeviceList().values()) {
			if (usbDevice != null && usbDevice.getProductId() == PID && usbDevice.getVendorId() == VID) {
				if (usbManager.hasPermission(usbDevice)) {
					openUsbDevice(usbDevice);
				} else {
					usbManager.requestPermission(usbDevice, mPermissionIntent);
				}
			}
		}
	}

	public void openUsbDevice(UsbDevice usbDevice) {
		this.conn = usbManager.openDevice(usbDevice);
		this.usbInterface = usbDevice.getInterface(0);
		this.inEndpoint = usbInterface.getEndpoint(0);
		this.outEndpoint = usbInterface.getEndpoint(1);
	}
	
	public int exeCmd(byte[] outByte){
		if(conn==null){
			return -1;
		}
		byte[] inByte = new FingerUsbDevCmd().upImage();
		this.conn.bulkTransfer(outEndpoint, inByte, inByte.length, 10000);
		
		
		byte[] data1 = new byte[outByte.length/2];
		for(int i=0;i<30;i++){
			if(conn.controlTransfer(0xc0, 1, data1.length, 0, data1, 2, 10000)<2){
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}else{
				break;
			}
		}
		
		conn.bulkTransfer(inEndpoint, data1, data1.length, 10000);
		
		
		byte[] data2 = new byte[outByte.length/2];
		for(int i=0;i<30;i++){
			if(conn.controlTransfer(0xc0, 1, data2.length, 0, data2, 2, 10000)<2){
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}else{
				break;
			}
		}
		
		conn.bulkTransfer(inEndpoint, data2, data2.length, 10000);
		
		for(int i=0;i<data1.length;i++){
			outByte[i] = data1[i];
		}
		
		for(int i=0;i<data2.length;i++){
			outByte[i+15200] = data2[i];
		}
		
		return 0;
	}
	
	public int exeCmd(byte[] inByte, byte[] outByte){
		if(conn==null){
			return -1;
		}
		this.conn.bulkTransfer(outEndpoint, inByte, inByte.length, 10000);
		
		for(int i=0;i<30;i++){
			if(conn.controlTransfer(0xc0, 1, outByte.length, 0, outByte, 2, 10000)<2){
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}else{
				break;
			}
		}
		
		int ret = conn.bulkTransfer(inEndpoint, outByte, outByte.length, 10000);
		return ret;
	}
	
	public UsbDeviceConnection getConn(){
		return conn;
	}
	
	public void close(){
		if(conn != null){
			boolean ret = conn.releaseInterface(usbInterface);
			while(!ret)
			{
				ret = conn.releaseInterface(usbInterface);
			}
			conn.close();
			conn = null;
		}
	}

	private final BroadcastReceiver mUsbPermissionActionReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (ACTION_USB_PERMISSION.equals(action)) {
				synchronized (this) {
					UsbDevice usbDevice = (UsbDevice) intent
							.getParcelableExtra(UsbManager.EXTRA_DEVICE);
					if (intent.getBooleanExtra(
							UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
						if (null != usbDevice) {
							openUsbDevice(usbDevice);
						}
					} else {
						Toast.makeText(
								context,
								String.valueOf("Permission denied for device"
										+ usbDevice), Toast.LENGTH_LONG).show();
					}
				}
			}
		}
	};
	
}
