package high.rivamed.myapplication.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;

import java.util.Map;
import java.util.TreeMap;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.base.App;

public class MusicPlayer {
    private Context mContext;
    private static MusicPlayer sInstance;
    private Handler         mHandler = new Handler();

    public static class Type {
        public final static int LOGIN_SUC = 1;//您已登录成功.
        public final static int LOGOUT_SUC = 2;//您已退出登录.
        public final static int NOT_NORMAL = 3;//识别到非正常操作耗材，请重新选择.
        public final static int IN_BOX_SUC = 4;//耗材成功入库.
        public final static int MOVE_IN_SUC = 5;//耗材成功移入.
        public final static int RETURN_SUC = 6;//耗材成功退回.
        public final static int USE_SUC = 7;//耗材成功领用.
        public final static int MOVE_OUT_SUC = 8;//耗材成功移出.
        public final static int RETURN_GOOD_SUC = 9;//耗材成功退货.
        public final static int UNCONFIRM_SUC = 10;//您有入柜耗材尚未确认，请点击确认.
        public final static int DOOR_OPEN = 11;//柜门已打开.
        public final static int DOOR_CLOSED = 12;//柜门已关闭
        public final static int SUCCESS = 13;//耗材操作成功
        public final static int NO_EVERY = 14;//未扫描到操作耗材
        public final static int PLEASE_CLOSSDOOR = 16;//请关闭柜门
        public final static int QRS_MOREOPEN = 15;//请关闭右侧门后关闭左侧门
    }

    private SoundPool mSp;
    private Map<Integer, Integer> sSpMap;

    private MusicPlayer(Context context) {
        mContext = context;
        sSpMap = new TreeMap<Integer, Integer>();
        mSp = new SoundPool(2, AudioManager.STREAM_MUSIC, 100);
        sSpMap.put(Type.LOGIN_SUC, mSp.load(mContext, R.raw.login_suc, 1));
        sSpMap.put(Type.LOGOUT_SUC, mSp.load(mContext, R.raw.logout_suc, 1));
        sSpMap.put(Type.NOT_NORMAL, mSp.load(mContext, R.raw.not_normal, 1));
        sSpMap.put(Type.IN_BOX_SUC, mSp.load(mContext, R.raw.in_box_suc, 1));
        sSpMap.put(Type.MOVE_IN_SUC, mSp.load(mContext, R.raw.move_in_suc, 1));
        sSpMap.put(Type.RETURN_SUC, mSp.load(mContext, R.raw.return_suc, 1));
        sSpMap.put(Type.USE_SUC, mSp.load(mContext, R.raw.use_suc, 1));
        sSpMap.put(Type.MOVE_OUT_SUC, mSp.load(mContext, R.raw.move_out_suc, 1));
        sSpMap.put(Type.RETURN_GOOD_SUC, mSp.load(mContext, R.raw.return_good_suc, 1));
        sSpMap.put(Type.UNCONFIRM_SUC, mSp.load(mContext, R.raw.unconfirm_suc, 1));
        sSpMap.put(Type.DOOR_OPEN, mSp.load(mContext, R.raw.door_open, 1));
        sSpMap.put(Type.DOOR_CLOSED, mSp.load(mContext, R.raw.door_closed, 1));
        sSpMap.put(Type.SUCCESS, mSp.load(mContext, R.raw.succes, 1));
        sSpMap.put(Type.NO_EVERY, mSp.load(mContext, R.raw.no_every, 1));
        sSpMap.put(Type.PLEASE_CLOSSDOOR, mSp.load(mContext, R.raw.clossdoor, 1));

    }

    static {
        sInstance = new MusicPlayer(App.getInstance().getApplicationContext());
    }

    public static MusicPlayer getInstance() {
        return sInstance;
    }

    /**
     * 根据操作类型播放操作成功提示音
     *
     * @param operation
     */
    public static void playSoundByOperation(int operation) {
        switch (operation) {
            case 2://入库
                MusicPlayer.getInstance().play(MusicPlayer.Type.IN_BOX_SUC);
                break;
            case 4://领用退回
                MusicPlayer.getInstance().play(MusicPlayer.Type.SUCCESS);
                break;
            case 10://移入
                MusicPlayer.getInstance().play(MusicPlayer.Type.MOVE_IN_SUC);
                break;
            case 7://退回
//                MusicPlayer.getInstance().play(MusicPlayer.Type.SUCCESS);
                MusicPlayer.getInstance().play(MusicPlayer.Type.RETURN_SUC);
                break;
            case 3://领用
                MusicPlayer.getInstance().play(MusicPlayer.Type.USE_SUC);
//                MusicPlayer.getInstance().play(MusicPlayer.Type.SUCCESS);
                break;
            case 9://移出
                MusicPlayer.getInstance().play(MusicPlayer.Type.MOVE_OUT_SUC);
                break;
            case 8://退货
                MusicPlayer.getInstance().play(MusicPlayer.Type.RETURN_GOOD_SUC);
                break;

            default:
                break;
        }
    }

    public void play(int type) {
        if (sSpMap.get(type) == null)
            return;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSp.play(sSpMap.get(type), 1, 1, 0, 0, 1);
            }
        }, 500);
    }
}