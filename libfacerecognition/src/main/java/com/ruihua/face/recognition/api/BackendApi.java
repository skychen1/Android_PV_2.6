package com.ruihua.face.recognition.api;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.ruihua.face.recognition.ui.FaceGatewayActivity;
import com.ruihua.face.recognition.entity.Feature;
import com.ruihua.face.recognition.entity.User;
import com.ruihua.face.recognition.utils.HttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * 主要api调用
 */
public class BackendApi {

    //根据分组id加载人脸库信息
    public static List<Feature> queryFeaturesByGroupId(String groupId) throws Exception {
        if (TextUtils.isEmpty(groupId)) {
            return null;
        }
        String url = "http://192.168.2.54:9999/queryFeaturesByGroupId";
        HashMap<String, String> param = new HashMap<>();
        param.put("groupId", groupId);
        String resultStr = HttpUtil.post(url, param);
        if (resultStr == null || resultStr.isEmpty()) {
            return null;
        }
        List<Feature> features = new ArrayList<>();
        JSONArray featuresJA = new JSONArray(resultStr);
        for (int i = 0; i < featuresJA.length(); i++) {
            JSONObject featureJO = featuresJA.getJSONObject(i);
            Feature feature = new Feature();
            feature.setFaceToken(featureJO.getString("face_token"));
            byte[] featureBytes = Base64.decode(featureJO.getString("face_token"), Base64.NO_WRAP);
            feature.setFeature(featureBytes);
            feature.setUserId(featureJO.getString("user_id"));
            feature.setImageName(featureJO.getString("image_name"));
            features.add(feature);
        }
        return features;
    }

    //同步人脸库信息
    public static void syncFaceFeatures() {
        String url = "http://192.168.2.54:9999/syncFaceFeatures";
        Set userIdSet = FaceApi.getInstance().getGroup2Facesets().get(FaceGatewayActivity.USE_GROUP).keySet();
        List<String> featureList = new ArrayList<>(userIdSet);
        JSONArray ja = new JSONArray(featureList);
        String resultStr = HttpUtil.post(url, ja);
        if (resultStr == null || resultStr.isEmpty()) {
            return;
        }
        try {
            JSONObject jo = new JSONObject(resultStr);
            if (jo == null || jo.length() == 0) {
                return;
            }
//            JSONArray
            //TODO 未完成
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //加载用户信息
    public static List<Object> loadUserInfo() {
        String url = "http://192.168.2.54:9999/getUserInfo";
        String resultStr = HttpUtil.get(url);
        if (resultStr == null || resultStr.isEmpty()) {
            return null;
        }
        List<Object> result = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        List<String> infos = new ArrayList<>();
        try {
            JSONArray ja = new JSONArray(resultStr);
            for (int i = 0; i < ja.length(); i++) {
                JSONArray ja2 = ja.getJSONArray(i);
                StringBuffer sb = new StringBuffer();
                for (int j = 0; j < ja2.length(); j++) {
                    if (j == 0) {
                        ids.add((String) ja2.get(j));
                    } else {
                        sb.append(ja2.get(j)).append(",");
                    }
                }
                infos.add(sb.toString().substring(0, sb.toString().length() - 1));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        result.add(ids);
        result.add(infos);
        return result;
    }


    //绑定用户与人脸
    public static boolean bindFaceAndUser(String faceId, String userId) {
        String url = "http://192.168.2.54:9999/bindFaceAndUser";
        HashMap<String, String> params = new HashMap<>();
        params.put("faceId", faceId);
        params.put("userId", userId);
        String result = HttpUtil.post(url, params);
        try {
            JSONObject jo = new JSONObject(result);
            if ("1".equals(jo.getString("opFlg"))) {
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    //通过userId在数据库中查询用户信息
    public static List<String> queryUserInfoByFace(String userId) {
        String url = "http://192.168.2.54:9999/queryUserInfoById";
        HashMap<String, String> param = new HashMap<>();
        param.put("userId", userId);
        String resultJson = HttpUtil.post(url, param);
        if (resultJson == null || resultJson.isEmpty()) {
            return null;
        }
        List<String> resList = new ArrayList<>();
        try {
            JSONArray ja = new JSONArray(resultJson);
            if (ja == null || ja.length() == 0) {
                return null;
            }
            JSONArray firstInfo = ja.getJSONArray(0);
            for (int i = 0; i < firstInfo.length(); i++) {
                String data = firstInfo.getString(i);
                resList.add(data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resList;
    }

    //添加用户
    public static int addUser(User user) {
        String url = "http://192.168.2.54:9999/addUser";
        HashMap<String, String> param = new HashMap<>();
        param.put("userId", user.getUserId());
        param.put("userInfo", user.getUserInfo());
        param.put("groupId", user.getGroupId());
        String result = HttpUtil.post(url, param);
        Log.i("POST RESULT:", result);

        try {
            if (result == null) {
                return 0;
            }
            JSONObject jo = new JSONObject(result);
            String opFlg = jo.getString("opFlg");
            Log.i("reched OPFLG:", opFlg);
            if (!"1".equals(jo.getString("opFlg"))) {
                return Integer.parseInt(jo.getString("opFlg"));
            }
            List<Feature> featureList = user.getFeatureList();
            if (addFeatures(featureList)) {
                Log.i("reached ADD FEATURES:", "YES");
                return 1;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //添加人脸特征
    public static boolean addFeatures(List<Feature> features) {
        String url = "http://192.168.2.54:9999/addFeatures";
        JSONArray paramArray = new JSONArray();
        try {
            for (Feature feature : features) {
                JSONObject jo = new JSONObject();
                jo.put("faceToken", feature.getFaceToken());
                jo.put("feature", encodeFeture(feature.getFeature()));
                jo.put("imageName", feature.getImageName());
                jo.put("groupId", feature.getGroupId());
                jo.put("userId", feature.getUserId());
                paramArray.put(jo);
            }
            String result = HttpUtil.post(url, paramArray);
            JSONObject resObj = new JSONObject(result);
            if ("1".equals(resObj.getString("opFlg"))) {
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    //添加用户组

    //feature字符串BASE64解码
    private static byte[] decodeFeature(String featureString) {
        byte[] featureBytes = Base64.decode(featureString, Base64.DEFAULT);
        return featureBytes;
    }

    //feature字节数组BASE64编码
    private static String encodeFeture(byte[] featureBytes) {
        String featureStr = Base64.encodeToString(featureBytes, Base64.DEFAULT);
        return featureStr;
    }
}
