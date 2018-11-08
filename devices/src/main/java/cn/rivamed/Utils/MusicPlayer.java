package cn.rivamed.Utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;

import java.util.Map;
import java.util.TreeMap;

import cn.rivamed.devices.R;


public class MusicPlayer {
	private Context mContext;
	private static MusicPlayer sInstance;

	public static class Type {
		public final static int DOOR_OPEN = 11;//柜门已打开.
		public final static int DOOR_CLOSED = 12;//柜门已关闭.
	}

	private SoundPool mSp;
	private Map<Integer, Integer> sSpMap;

	private MusicPlayer(Context context) {
		mContext = context;
		sSpMap = new TreeMap<Integer, Integer>();
		mSp = new SoundPool(2, AudioManager.STREAM_MUSIC, 100);
//		sSpMap.put(Type.LOGIN_SUC, mSp.load(mContext, R.raw.login_suc, 1));
//		sSpMap.put(Type.LOGOUT_SUC, mSp.load(mContext, R.raw.logout_suc, 1));
//		sSpMap.put(Type.NOT_NORMAL, mSp.load(mContext, R.raw.not_normal, 1));
//		sSpMap.put(Type.IN_BOX_SUC, mSp.load(mContext, R.raw.in_box_suc, 1));
//		sSpMap.put(Type.MOVE_IN_SUC, mSp.load(mContext, R.raw.move_in_suc, 1));
//		sSpMap.put(Type.RETURN_SUC, mSp.load(mContext, R.raw.return_suc, 1));
//		sSpMap.put(Type.USE_SUC, mSp.load(mContext, R.raw.use_suc, 1));
//		sSpMap.put(Type.MOVE_OUT_SUC, mSp.load(mContext, R.raw.move_out_suc, 1));
//		sSpMap.put(Type.RETURN_GOOD_SUC, mSp.load(mContext, R.raw.return_good_suc, 1));
//		sSpMap.put(Type.UNCONFIRM_SUC, mSp.load(mContext, R.raw.unconfirm_suc, 1));
		sSpMap.put(Type.DOOR_OPEN, mSp.load(mContext, R.raw.door_open, 1));
		sSpMap.put(Type.DOOR_CLOSED, mSp.load(mContext, R.raw.door_closed, 1));
		// sSpMap.put(Type.MUSIC_FOCUSED, mSp.load(mContext, R.raw.focused, 1))
		// ;
	}

	static {
		sInstance = new MusicPlayer(DeviceApp.getInstance().getApplicationContext());
	}

	public static MusicPlayer getInstance() {
			return sInstance;
	}

	public void play(int type) {
		if (sSpMap.get(type) == null)
			return;
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				mSp.play(sSpMap.get(type), 1, 1, 0, 0, 1);
			}
		}, 500);
		}
}