package android.fingerdev;

public class FingerUsbDevCmd {

	/**
	 * 采集指纹特征指令
	 * @return
	 */
	public byte[] getFingerFea(){
		byte[] cmd = new byte[12];
		cmd[0] = (byte) 0xef;
		cmd[1] = (byte) 0x01;
		cmd[2] = (byte) 0xff;
		cmd[3] = (byte) 0xff;
		cmd[4] = (byte) 0xff;
		cmd[5] = (byte) 0xff;
		cmd[6] = (byte) 0x01;
		cmd[7] = (byte) 0x00;
		cmd[8] = (byte) 0x03;
		cmd[9] = (byte) 0x22;
		cmd[10] = (byte) 0x00;
		cmd[11] = (byte) 0x26;
		return cmd;
	}
	
	/**
	 * 采集指纹模板指令
	 * @return
	 */
	public byte[] getFingerTemp(){
		byte[] cmd = new byte[12];
		cmd[0] = (byte) 0xef;
		cmd[1] = (byte) 0x01;
		cmd[2] = (byte) 0xff;
		cmd[3] = (byte) 0xff;
		cmd[4] = (byte) 0xff;
		cmd[5] = (byte) 0xff;
		cmd[6] = (byte) 0x01;
		cmd[7] = (byte) 0x00;
		cmd[8] = (byte) 0x03;
		cmd[9] = (byte) 0x21;
		cmd[10] = (byte) 0x00;
		cmd[11] = (byte) 0x25;
		return cmd;
	}
	
	/**
	 * 采集指纹图像
	 * @return
	 */
	public byte[] getImage(){
		byte[] cmd = new byte[12];
		cmd[0] = (byte) 0xef;
		cmd[1] = (byte) 0x01;
		cmd[2] = (byte) 0xff;
		cmd[3] = (byte) 0xff;
		cmd[4] = (byte) 0xff;
		cmd[5] = (byte) 0xff;
		cmd[6] = (byte) 0x01;
		cmd[7] = (byte) 0x00;
		cmd[8] = (byte) 0x03;
		cmd[9] = (byte) 0x01;
		cmd[10] = (byte) 0x00;
		cmd[11] = (byte) 0x05;
		return cmd;
	}
	
	
	
	/**
	 * 上传指纹图像
	 * @return
	 */
	public byte[] upImage(){
		byte[] cmd = new byte[12];
		cmd[0] = (byte) 0xef;
		cmd[1] = (byte) 0x01;
		cmd[2] = (byte) 0xff;
		cmd[3] = (byte) 0xff;
		cmd[4] = (byte) 0xff;
		cmd[5] = (byte) 0xff;
		cmd[6] = (byte) 0x01;
		cmd[7] = (byte) 0x00;
		cmd[8] = (byte) 0x03;
		cmd[9] = (byte) 0x0a;
		cmd[10] = (byte) 0x00;
		cmd[11] = (byte) 0x0e;
		return cmd;
	}
	
	/**
	 * 片内图像转特征
	 * @return
	 */
	public byte[] chipGenChar(){
		byte[] cmd = new byte[13];
		cmd[0] = (byte) 0xef;
		cmd[1] = (byte) 0x01;
		cmd[2] = (byte) 0xff;
		cmd[3] = (byte) 0xff;
		cmd[4] = (byte) 0xff;
		cmd[5] = (byte) 0xff;
		cmd[6] = (byte) 0x01;
		cmd[7] = (byte) 0x00;
		cmd[8] = (byte) 0x04;
		cmd[9] = (byte) 0x02;
		cmd[10] = (byte) 0x01;
		cmd[11] = (byte) 0x00;
		cmd[12] = (byte) 0x08;
		return cmd;
	}
	
	/**
	 * 上传指纹特征
	 * @return
	 */
	public byte[] upChar(){
		byte[] cmd = new byte[12];
		cmd[0] = (byte) 0xef;
		cmd[1] = (byte) 0x01;
		cmd[2] = (byte) 0xff;
		cmd[3] = (byte) 0xff;
		cmd[4] = (byte) 0xff;
		cmd[5] = (byte) 0xff;
		cmd[6] = (byte) 0x01;
		cmd[7] = (byte) 0x00;
		cmd[8] = (byte) 0x03;
		cmd[9] = (byte) 0x08;
		cmd[10] = (byte) 0x00;
		cmd[11] = (byte) 0x0c;
		return cmd;
	}
	
}
