/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.ruihua.face.recognition.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ReplacementTransformationMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.idl.facesdk.FaceSDK;
import com.baidu.idl.license.AndroidLicenser;
import com.ruihua.face.recognition.R;
import com.ruihua.face.recognition.manager.FaceSDKManager;
import com.ruihua.face.recognition.utils.FileUitls;
import com.ruihua.face.recognition.utils.NetRequest;
import com.ruihua.face.recognition.utils.PreferencesUtil;
import com.ruihua.face.recognition.utils.ZipUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.Executors;

/**
 * describe ：激活界面展示
 *
 * @author : Yich
 * data: 2019/2/21
 */
public class Activation {

    private Context context;
    private Button activateBtn;
    private Button backBtn;
    private TextView deviceIdTv;
    private EditText keyEt;
    private String device = "";
    private Dialog activationDialog;
    private Handler handler = new Handler(Looper.getMainLooper());
    private ActivationCallback activationCallback;
    private int lastKeyLen = 0;

    private TextView tvOnLineText, tvOffLineText;
    private Button btOffLineActive;
    ArrayList<String> list = new ArrayList<>();

    private boolean success = false;

    public Activation(Context context) {
        this.context = context;
    }

    public void setActivationCallback(ActivationCallback callback) {
        this.activationCallback = callback;
    }

    /**
     * 展示弹窗
     */
    public void show() {
        PreferencesUtil.initPrefs(context.getApplicationContext());
        activationDialog = new Dialog(context);
        activationDialog.setTitle("设备激活");
        activationDialog.setContentView(initView());
        activationDialog.setCancelable(false);
        activationDialog.show();
        addLisenter();
    }

    public void hide() {
        if (activationDialog == null) {
            return;
        }
        if (activationDialog.isShowing()) {
            activationDialog.hide();
        }
    }

    /**
     * 自定义view
     *
     * @return view
     */
    private LinearLayout initView() {
        device = AndroidLicenser.get_device_id(context.getApplicationContext());
        final LinearLayout root = new LinearLayout(context);
        root.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams rootParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rootParams.gravity = Gravity.CENTER;
        root.setBackgroundColor(Color.WHITE);
        root.setFocusable(true);
        root.setFocusableInTouchMode(true);
        TextView titleTv = new TextView(context);
        titleTv.setText("设备激活");
        titleTv.setTextSize(dip2px(15));
        titleTv.setTextColor(context.getResources().getColor(R.color.face_black));
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        titleParams.gravity = Gravity.CENTER;
        titleParams.topMargin = dip2px(10);
        titleParams.rightMargin = dip2px(30);
        titleParams.leftMargin = dip2px(30);
        deviceIdTv = new TextView(context);
        deviceIdTv.setTextIsSelectable(true);
        deviceIdTv.setText("设备ID：" + device);
        deviceIdTv.setTextSize(dip2px(12));
        deviceIdTv.setTextColor(context.getResources().getColor(R.color.face_black));
        LinearLayout.LayoutParams deviceIdParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        deviceIdParams.gravity = Gravity.CENTER;
        deviceIdParams.topMargin = dip2px(40);
        deviceIdParams.rightMargin = dip2px(30);
        deviceIdParams.leftMargin = dip2px(30);
        keyEt = new EditText(context);
        keyEt.setHint("输入序列号");
        keyEt.setText(PreferencesUtil.getString("activate_key", ""));
        LinearLayout.LayoutParams keyParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        keyParams.gravity = Gravity.CENTER;
        keyParams.topMargin = dip2px(40);
        keyParams.rightMargin = dip2px(30);
        keyParams.leftMargin = dip2px(30);
        keyEt.setTransformationMethod(new AllCapTransformationMethod(true));
        keyEt.setWidth(dip2px(260));
        LinearLayout.LayoutParams activateParams = new LinearLayout.LayoutParams(dip2px(180), dip2px(40));
        activateParams.gravity = Gravity.CENTER;
        activateParams.topMargin = dip2px(40);
        activateParams.rightMargin = dip2px(40);
        activateParams.leftMargin = dip2px(40);
        activateBtn = new Button(context);
        activateBtn.setText("在线激活");
        activateBtn.setTextSize(dip2px(12));
        activateBtn.setTextColor(context.getResources().getColor(R.color.face_white));
        activateBtn.setBackgroundColor(context.getResources().getColor(R.color.face_buttonColor));
        LinearLayout.LayoutParams activateParamsone = new LinearLayout.LayoutParams(dip2px(360), dip2px(48));
        activateParamsone.gravity = Gravity.CENTER;
        activateParamsone.topMargin = dip2px(8);
        activateParamsone.rightMargin = dip2px(20);
        activateParamsone.leftMargin = dip2px(60);
        tvOnLineText = new TextView(context);
        tvOnLineText.setText("在线激活:输入序列号，保持设备联网，SDK会自动进行激活");
        tvOnLineText.setTextSize(dip2px(8));
        tvOnLineText.setTextColor(context.getResources().getColor(R.color.face_black));
        LinearLayout.LayoutParams activateParamsoffLine = new LinearLayout.LayoutParams(dip2px(180), dip2px(40));
        activateParamsoffLine.gravity = Gravity.CENTER;
        activateParamsoffLine.topMargin = dip2px(5);
        activateParamsoffLine.rightMargin = dip2px(40);
        activateParamsoffLine.leftMargin = dip2px(40);
        btOffLineActive = new Button(context);
        btOffLineActive.setText("离线激活");
        btOffLineActive.setTextSize(dip2px(12));
        btOffLineActive.setTextColor(context.getResources().getColor(R.color.face_white));
        btOffLineActive.setBackgroundColor(context.getResources().getColor(R.color.face_buttonColor));
        LinearLayout.LayoutParams tvOffLineParams = new LinearLayout.LayoutParams(dip2px(380), dip2px(48));
        tvOffLineParams.gravity = Gravity.CENTER;
        tvOffLineParams.topMargin = dip2px(8);
        tvOffLineParams.rightMargin = dip2px(20);
        tvOffLineParams.leftMargin = dip2px(20);
        tvOffLineText = new TextView(context);
        tvOffLineText.setText("离线激活:将激活文件置于SD卡根目录（/storage/emulated/0）中，SDK会自动进行激活");
        tvOffLineText.setTextSize(dip2px(8));
        tvOffLineText.setTextColor(context.getResources().getColor(R.color.face_black));
        LinearLayout.LayoutParams backParams = new LinearLayout.LayoutParams(dip2px(180), dip2px(40));
        backParams.gravity = Gravity.CENTER;
        backParams.topMargin = dip2px(5);
        backParams.bottomMargin = dip2px(20);
        backParams.rightMargin = dip2px(40);
        backParams.leftMargin = dip2px(40);
        backBtn = new Button(context);
        backBtn.setText("返      回");
        backBtn.setTextColor(context.getResources().getColor(R.color.face_white));
        backBtn.setBackgroundColor(context.getResources().getColor(R.color.face_buttonColor));
        backBtn.setTextSize(dip2px(12));
        root.addView(titleTv, titleParams);
        root.addView(deviceIdTv, deviceIdParams);
        root.addView(keyEt, keyParams);
        root.addView(activateBtn, activateParams);
        root.addView(tvOnLineText, activateParamsone);
        root.addView(btOffLineActive, activateParamsoffLine);
        root.addView(tvOffLineText, tvOffLineParams);
        root.addView(backBtn, backParams);
        return root;
    }

    private void addLisenter() {
        keyEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 19) {
                    keyEt.setText(s.toString().substring(0, 19));
                    keyEt.setSelection(keyEt.getText().length());
                    lastKeyLen = s.length();
                    return;
                }
                if (s.toString().length() < lastKeyLen) {
                    lastKeyLen = s.length();
                    return;
                }
                String text = s.toString().trim();
                if (keyEt.getSelectionStart() < text.length()) {
                    return;
                }
                if (text.length() == 4 || text.length() == 9 || text.length() == 14) {
                    keyEt.setText(text + "-");
                    keyEt.setSelection(keyEt.getText().length());
                }
                lastKeyLen = s.length();
            }
        });
        activateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = keyEt.getText().toString().trim().toUpperCase();
                if (TextUtils.isEmpty(key)) {
                    Toast.makeText(context, "序列号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                request(key);
            }
        });

        btOffLineActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String path = getSDPath();
                offLineActive(path);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activationDialog != null) {
                    activationDialog.dismiss();
                }
            }
        });

    }


    private void offLineActive(String path) {
        if (FaceSDK.getAuthorityStatus() == AndroidLicenser.ErrorCode.SUCCESS.ordinal()) {
            Toast.makeText(context, "已经激活成功", Toast.LENGTH_LONG).show();
            return;
        }
        String firstPath = path + "/" + "License.zip";
        if (fileIsExists(firstPath)) {
            if (!TextUtils.isEmpty(firstPath)) {
                ZipUtil.unzip(firstPath);
            }
            if (ZipUtil.isSuccess) {
                String secondPath = path + "/" + "Win.zip";
                if (!TextUtils.isEmpty(secondPath)) {
                    ZipUtil.unzip(secondPath);
                }
            }
            String keyPath = path + "/" + "license.key";
            String key = readFile(keyPath, "key");
            PreferencesUtil.putString("activate_key", key);
            String liscensePaht = path + "/" + "license.ini";
            String liscense = readFile(liscensePaht, "liscense");
            success = FileUitls.c(context, FaceSDKManager.LICENSE_NAME, list);
            if (success) {
                toast("激活成功");
                FaceSDKManager.initStatus = FaceSDKManager.SDK_UNINIT;
                FaceSDKManager.getInstance().init(context);
                hide();
            } else {
                toast("激活失败");
            }
        } else {
            toast("授权文件不存在!");
        }
    }

    /**
     * 读取文件内容
     *
     * @param strFilePath 文件路劲
     * @param mark        key
     * @return 内容
     */
    private String readFile(String strFilePath, String mark) {
        String path = strFilePath;
        String content = ""; //文件内容字符串
        //打开文件
        File file = new File(path);
        //如果path是传递过来的参数，可以做一个非目录的判断
        if (file.isDirectory()) {
            Log.d("TestFile", "The File doesn't not exist.");
        } else {
            try {
                InputStream instream = new FileInputStream(file);
                if (instream != null) {
                    InputStreamReader inputreader = new InputStreamReader(instream);
                    BufferedReader buffreader = new BufferedReader(inputreader);
                    String line;
                    //分行读取
                    while ((line = buffreader.readLine()) != null) {
                        content = line;
                        if (mark.equals("liscense")) {
                            list.add(line);
                        }
                    }
                    instream.close();
                }
            } catch (java.io.FileNotFoundException e) {
                Log.d("TestFile", "The File doesn't not exist.");
            } catch (IOException e) {
                Log.d("TestFile", e.getMessage());
            }
        }
        return content;
    }

    /**
     * 判断文件是否存在
     *
     * @param strFile 文件路劲
     * @return 是否存在
     */
    private boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    private String getSDPath() {
        File sdDir = null;
        //判断sd卡是否存在
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            //获取跟目录
            sdDir = Environment.getExternalStorageDirectory();
        }
        return sdDir == null ? "" : sdDir.toString();
    }

    private void toast(final String text) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, text, Toast.LENGTH_LONG).show();
            }
        });
    }


    private int dip2px(int dip) {
        Resources resources = context.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, resources.getDisplayMetrics());
    }

    private void request(final String key) {
        Executors.newSingleThreadExecutor().submit(new Runnable() {
            @Override
            public void run() {
                netRequest(key);
            }
        });
    }

    private void netRequest(final String key) {
        if (NetRequest.isConnected(context)) {
            NetRequest.request(new NetRequest.RequestAdapter() {
                @Override
                public String getURL() {
                    return "https://ai.baidu.com/activation/key/activate";
                }

                @Override
                public String getRequestString() {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("deviceId", device);
                        jsonObject.put("key", key);
                        jsonObject.put("platformType", 2);
                        jsonObject.put("version", "3.4.2");

                        return jsonObject.toString();
                    } catch (JSONException var10) {
                        var10.printStackTrace();
                        return null;
                    }
                }

                @Override
                public void parseResponse(InputStream in) throws IOException, JSONException {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];

                    try {
                        int e;
                        while ((e = in.read(buffer)) > 0) {
                            out.write(buffer, 0, e);
                        }
                        out.flush();
                        JSONObject json = new JSONObject(new String(out.toByteArray(), "UTF-8"));
                        Log.i("wtf", "netRequest->" + json.toString());
                        int errorCode = json.optInt("error_code");
                        if (errorCode != 0) {
                            String errorMsg = json.optString("error_msg");
                            toast(errorMsg);
                        } else {
                            parse(json, key);
                        }
                    } catch (Exception e) {
                        toast("激活失败");
                    } finally {
                        if (out != null) {
                            try {
                                out.close();
                            } catch (IOException var12) {
                                var12.printStackTrace();
                            }
                        }
                    }
                }
            });
        } else {
            toast("没有连接网络");
        }
    }

    private void parse(JSONObject json, String key) {
        boolean success = false;
        JSONObject result = json.optJSONObject("result");
        if (result != null) {
            String license = result.optString("license");
            if (!TextUtils.isEmpty(license)) {
                String[] licenses = license.split(",");
                if (licenses != null && licenses.length == 2) {
                    PreferencesUtil.putString("activate_key", key);
                    ArrayList<String> list = new ArrayList<>();
                    list.add(licenses[0]);
                    list.add(licenses[1]);
                    success = FileUitls.c(context, FaceSDKManager.LICENSE_NAME, list);
                }
            }
        }

        if (success) {
            toast("激活成功");
            if (activationCallback != null) {
                activationCallback.callback(true);
                activationDialog.dismiss();
            }
        } else {
            toast("激活失败");
        }
    }

    public interface ActivationCallback {

        public void callback(boolean success);
    }

    public class AllCapTransformationMethod extends ReplacementTransformationMethod {

        private char[] lower = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
                'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        private char[] upper = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
                'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        private boolean allUpper = false;

        public AllCapTransformationMethod(boolean needUpper) {
            this.allUpper = needUpper;
        }

        @Override
        protected char[] getOriginal() {
            if (allUpper) {
                return lower;
            } else {
                return upper;
            }
        }

        @Override
        protected char[] getReplacement() {
            if (allUpper) {
                return upper;
            } else {
                return lower;
            }
        }
    }

}
