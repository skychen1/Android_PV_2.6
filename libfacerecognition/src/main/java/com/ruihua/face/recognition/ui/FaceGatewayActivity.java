package com.ruihua.face.recognition.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.rivamed.libdevicesbase.utils.ToastUtils;
import com.ruihua.face.recognition.FaceManager;
import com.ruihua.face.recognition.R;
import com.ruihua.face.recognition.callback.InitListener;
import com.ruihua.face.recognition.config.FaceCode;
import com.ruihua.face.recognition.entity.User;
import com.ruihua.face.recognition.manager.FaceSDKManager;
import com.ruihua.face.recognition.utils.FileUitls;
import com.ruihua.face.recognition.utils.LogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 人脸识别导航页面
 */
public class FaceGatewayActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String USE_GROUP = "ruihua";
    private Button settingBtn;
    private Button registerFaceBtn;
    private Button identifyFaceBtn;
    private Button groupFaceBtn;
    private Button file_register_face_btn;
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
        file_register_face_btn = (Button) findViewById(R.id.file_register_face_btn);
        settingBtn.setOnClickListener(this);
        registerFaceBtn.setOnClickListener(this);
        identifyFaceBtn.setOnClickListener(this);
        groupFaceBtn.setOnClickListener(this);
        file_register_face_btn.setOnClickListener(this);
        //检测设备是否授权
        boolean b = FaceManager.getManager().hasActivation(this);
        ToastUtils.show(b ? "设备已授权" : "设备未授权");

        //初始化sdk
        FaceManager.getManager().init(this, false, new InitListener() {
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
//                FaceManager.getManager().setNeedLive(true);
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
        } else if (v == file_register_face_btn) {
            //使用照片注册人脸
            new Thread(() -> getAllFiles()).start();
        }
    }

    /**
     * 获取指定目录内所有文件路径
     */
    public void getAllFiles() {
        imageFiles.clear();
        imageNames.clear();
        //人脸照缓存路径
        File f = FileUitls.getBatchFaceDirectory("face_library");
        Log.d("LOGCAT", "cachePath:" + f.getAbsolutePath());
        if (!f.exists()) {//判断路径是否存在
            Log.d("LOGCAT", "cachePath not exist" );
            toast("文件路径不存在");
            return;
        }
        File[] files = f.listFiles();
        if (files == null) {//判断权限
            Log.d("LOGCAT", "files is null" );
            toast("文件路径为空");
            return;
        }
        for (File _file : files) {//遍历目录
            if (_file.isFile()) {
                String _name = _file.getName();
                String filePath = _file.getAbsolutePath();//获取文件路径

                int end=_file.getName().lastIndexOf('.');
                String fileName = _file.getName().substring(0,end);//获取文件名
                Log.d("LOGCAT", "_name:" + _name);
                Log.d("LOGCAT", "fileName:" + fileName);
                Log.d("LOGCAT", "filePath:" + filePath);
                imageFiles.add(_file);
                imageNames.add(fileName);
            }
        }
        if (imageNames.size() == 0 || imageFiles.size() == 0) {
            LogUtils.d("LoginFace:暂时没有人脸照");
            toast("文件路径下没有图片");
            return;
        }
        index = 0;
        registerFaceLibrary();
    }

    //记录当前缓存的图片
    private int index = 0;
    //图片列表
    private List<File> imageFiles = new ArrayList<>();
    private List<String> imageNames = new ArrayList<>();

    private void registerFaceLibrary() {
        String userId = imageNames.get(index);
        String userName = imageNames.get(index);
        File file = imageFiles.get(index);
        FaceManager.getManager().registerFace(userId, userName, file.getAbsolutePath(),
                (code, msg) -> {
                    LogUtils.d("LoginFace:人脸注册结果：：code=" + code + ":::msg=" + msg+":::userId="+userId);
                    if (code == FaceCode.CODE_REGISTER_SUCCESS) {
                        //注册成功即可删除缓存照片
//                        file.delete();
                    }
                    //注册完成，继续注册下一张
                    if (imageFiles.size() - 1 > index) {
                        index++;
                        registerFaceLibrary();
                    }else {
                        toast("注册完成");
                    }
                });
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
