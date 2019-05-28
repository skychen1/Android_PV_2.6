package com.ruihua.face.recognition.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ruihua.face.recognition.FaceManager;
import com.ruihua.face.recognition.R;
import com.ruihua.face.recognition.callback.InitListener;
import com.ruihua.face.recognition.config.FaceCode;
import com.ruihua.face.recognition.entity.User;
import com.ruihua.face.recognition.manager.FaceSDKManager;
import com.ruihua.face.recognition.utils.LogUtils;

/**
 * 人脸识别导航页面
 */
public class FaceGatewayActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String USE_GROUP = "ruihua";
    private Button settingBtn;
    private Button registerFaceBtn;
    private Button identifyFaceBtn;
    private Button groupFaceBtn;
    private Handler handler = new Handler(Looper.getMainLooper());

    /**
     * 启动方法
     *
     * @param act 界面
     */
    public static void launch(Activity act) {
        Intent intent = new Intent(act, FaceGatewayActivity.class);
        act.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.face_activity_face_gateway);
        settingBtn = (Button) findViewById(R.id.setting_btn);
        registerFaceBtn = (Button) findViewById(R.id.register_face_btn);
        identifyFaceBtn = (Button) findViewById(R.id.identify_face_btn);
        groupFaceBtn = (Button) findViewById(R.id.group_face_btn);
        settingBtn.setOnClickListener(this);
        registerFaceBtn.setOnClickListener(this);
        identifyFaceBtn.setOnClickListener(this);
        groupFaceBtn.setOnClickListener(this);
        //初始化sdk
        FaceManager.getManager().init(this, new InitListener() {
            @Override
            public void initSuccess() {
                toast("SDK初始化成功");
                //初始化分组
                boolean b = FaceManager.getManager().initGroup();
                if (!b) {
                    toast("创建分组失败");
                    return;
                }
                //设置需要活体
                FaceManager.getManager().setNeedLive(true);
            }

            @Override
            public void initFail(int errorCode, String msg) {
                toast("SDK初始化失败：：errorCode = " + errorCode + ":::msg：" + msg);
            }
        });
    }


    @Override
    public void onClick(View v) {
        int initStatus = FaceManager.getManager().getInitStatus();
        if (initStatus == FaceCode.SDK_NOT_ACTIVE) {
            toast("SDK还未激活，请先激活");
            return;
        } else if (initStatus == FaceCode.SDK_NOT_INIT) {
            toast("SDK还未初始化完成，请先初始化");
            return;
        } else if (initStatus == FaceCode.SDK_INITING) {
            toast("SDK正在初始化，请稍后再试");
            return;
        } else if (initStatus == FaceCode.SDK_INIT_FAIL) {
            toast("SDK初始化失败，请重新初始化SDK");
            return;
        }
        //跳转到设置页面
        if (v == settingBtn) {
            Intent intent = new Intent(FaceGatewayActivity.this, LivenessSettingActivity.class);
            startActivity(intent);
        } else if (v == registerFaceBtn) {
            //注册人脸
            RegActivity.launch(FaceGatewayActivity.this);
        } else if (v == identifyFaceBtn) {
            //跳转到识别页面
            FaceManager.getManager().identityFace(FaceGatewayActivity.this);
        } else if (v == groupFaceBtn) {
            //底库管理页面
            Intent intent = new Intent(FaceGatewayActivity.this, UserListActivity.class);
            intent.putExtra("group_id", USE_GROUP);
            startActivity(intent);
        }
    }

    private void toast(final String text) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(FaceGatewayActivity.this, text, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //验证身份回调
        if (requestCode == resultCode && resultCode == RgbVideoIdentityActivity.CODE_RECOGNISE && data != null) {
            //拿到识别的人员的id
            String userId = data.getStringExtra(RgbVideoIdentityActivity.USER_ID);
            User user = FaceManager.getManager().getUserById(userId);
            if (user != null) {
                toast("识别到：：：" + user.getUserInfo());
            }
        }
    }

}
