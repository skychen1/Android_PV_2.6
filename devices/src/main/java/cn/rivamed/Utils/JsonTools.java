package cn.rivamed.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonTools {
    /**
     * 得到一个json类型的字符串对象
     * @param key
     * @param value
     * @return
     */
    public static String getJsonString(String key, Object value)
    {
        JSONObject jsonObject = new JSONObject();
        //put和element都是往JSONObject对象中放入 key/value 对
//        jsonObject.put(key, value);
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {
        }
        return jsonObject.toString();
    }

    /**
     * 得到一个json对象
     * @param key
     * @param value
     * @return
     */
    public static JSONObject getJsonObject(String key, Object value)
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
